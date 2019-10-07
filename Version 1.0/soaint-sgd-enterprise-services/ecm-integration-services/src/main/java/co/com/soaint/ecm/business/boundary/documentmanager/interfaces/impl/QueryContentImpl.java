package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.ContentControlAlfresco;
import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.QueryContent;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.StructureAlfresco;
import co.com.soaint.ecm.domain.entity.CommunicationType;
import co.com.soaint.ecm.domain.entity.DocumentType;
import co.com.soaint.ecm.domain.entity.SecurityGroupType;
import co.com.soaint.ecm.domain.entity.StateType;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.ModeloConsultaDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service("queryContent")
public class QueryContentImpl implements QueryContent {

    private static final long serialVersionUID = 1123123123L;

    @Autowired
    @Qualifier("contentStructure")
    private StructureAlfresco structureAlfresco;

    @Autowired
    @Qualifier("contentControlAlfresco")
    private ContentControlAlfresco controlAlfresco;

    private final String CORRESPONDENCIA_ENDPOINT = SystemParameters
            .getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT_CORRESPONDENCIA);

    @Override
    public MensajeRespuesta consultarDocumentos(String userLogin, String startDate, String endDate, String tipoComunicacion, String nroRadicado,
                                                String nroIdentificacion, String depOrigen, String depDestino, String depCode, String sCode, String ssCode,
                                                String nroGuia, String nombre, String asunto, String solicitante, String destinatario,
                                                String tipoDocumento, String tramite, String evento, String actuacion, String tipologiaDocumental) throws SystemException {

        log.info("--------------------- consultar documentos Start ------------------------");
        log.info("Logged User \t----- \t{}", userLogin);
        log.info("Fecha Inicial \t----- \t{}", startDate);
        log.info("Fecha Final \t----- \t{}", endDate);
        log.info("tipo de comunicacion  \t----- \t{}", tipoComunicacion);
        log.info("numero de radicado  \t----- \t{}", nroRadicado);
        log.info("numero de identificación \t----- \t{}", nroIdentificacion);
        log.info("dependencia de origen \t----- \t{}", depOrigen);
        log.info("dependencia de destino \t----- \t{}", depDestino);
        log.info("dep Codigo \t----- \t{}", depCode);
        log.info("serie Code \t----- \t{}", sCode);
        log.info("sub serie Code \t----- \t{}", ssCode);
        log.info("Nombre Doc \t----- \t{}", nombre);
        log.info("numero guia  \t----- \t{}", nroGuia);
        log.info("asunto \t----- \t{}", asunto);
        log.info("Solicitante  \t----- \t{}", solicitante);
        log.info("Destinatario  \t----- \t{}", destinatario);
        log.info("Tipo Documento  \t----- \t{}", tipoDocumento);
        log.info("Tipologia Documental \t----- \t{}", tipologiaDocumental);
        log.info("--------------------- consultar documentos End ------------------------");

        if (StringUtils.isEmpty(userLogin)) {
            log.error("No se ha especificado el nombre del usuario");
            throw new SystemException("No se ha especificado el nombre del usuario");
        }
        final List<SecurityGroupType> groupTypeList = getSecurityRoleBy(userLogin);
        if (groupTypeList.isEmpty()) {
            log.error("El usuario {} no contiene roles de Consulta", userLogin);
            throw new SystemException("El usuario " + userLogin + " no contiene roles de Consulta");
        }
        final CommunicationType communicationType = CommunicationType.getSelectorBy(tipoComunicacion);
        if (null == communicationType) {
            log.error("No se ha especificado el tipo de comunicacion Valores: '{}', '{}', '{}', '{}'",
                    CommunicationType.EE.getCommunicationName(), CommunicationType.SE.getCommunicationName(),
                    CommunicationType.SI.getCommunicationName(), CommunicationType.GN.getCommunicationName());
            throw new SystemException("No se ha especificado el tipo de comunicacion Valores: " +
                    "'" + CommunicationType.SI.getCommunicationName() + "' ('SI'), " +
                    "'" + CommunicationType.SE.getCommunicationName() + "' ('SE'), " +
                    "'" + CommunicationType.EE.getCommunicationName() + "' ('EE'), " +
                    "'" + CommunicationType.GN.getCommunicationName() + "' ('GN')");
        }

        final MensajeRespuesta response = new MensajeRespuesta();
        final Map<String, Object> responseMap = new HashMap<>();
        final List<ModeloConsultaDocumentoDTO> queryResponse = new ArrayList<>();

        responseMap.put("consultar-documentos", queryResponse);
        response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
        response.setMensaje(ConstantesECM.SUCCESS);
        response.setResponse(responseMap);

        try {
            Date startDateObj = null;
            if (!StringUtils.isEmpty(startDate)) {
                final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                startDateObj = df.parse(startDate);
            }
            Date endDateObj = null;
            if (!StringUtils.isEmpty(endDate)) {
                final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                endDateObj = df.parse(endDate);
            }
            final LocalDateTime startDateTime = Utilities.toLocalDateTime(startDateObj);
            final LocalDateTime endDateTime = Utilities.toLocalDateTime(endDateObj);
            if (communicationType == CommunicationType.GN) {
                final List<Folder> udFOlders = new ArrayList<>();
                if (!StringUtils.isEmpty(sCode) && StringUtils.isEmpty(depCode)) {
                    throw new SystemException("No se ha especificado el Codigo de la dependencia");
                }
                if (!StringUtils.isEmpty(ssCode) && StringUtils.isEmpty(sCode)) {
                    throw new SystemException("No se ha especificado el Codigo de la Serie");
                }
                final List<Folder> allforQueryModule = new ArrayList<>();
                final List<String> codes = getDepCodesByUserName(userLogin);

                if (null != depCode && !"".equals(depCode.trim())) {
                    if (codes.contains(depCode.trim())) {
                        allforQueryModule.addAll(structureAlfresco.findAllforQueryModule(depCode, sCode, ssCode, groupTypeList));
                    }
                } else {
                    allforQueryModule.addAll(structureAlfresco.findAllforQueryModule(depCode, sCode, ssCode, groupTypeList)
                            .parallelStream()
                            .filter(folder -> {
                                final String depCodeEcm = folder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);
                                return codes.parallelStream().anyMatch(s -> StringUtils.equalsIgnoreCase(s, depCodeEcm));
                            }).collect(Collectors.toList()));
                }
                queryResponse.addAll(getDocumentsBy(allforQueryModule, udFOlders)
                        .parallelStream()
                        .filter(documentDTO -> (StringUtils.isEmpty(tramite) || StringUtils.containsIgnoreCase(documentDTO.getTramite(), tramite)) &&
                                (StringUtils.isEmpty(tipologiaDocumental) || StringUtils.containsIgnoreCase(documentDTO.getTipologiaDocumental(), tipologiaDocumental)) &&
                                (StringUtils.isEmpty(evento) || StringUtils.containsIgnoreCase(documentDTO.getEvento(), evento)) &&
                                (StringUtils.isEmpty(actuacion) || StringUtils.containsIgnoreCase(documentDTO.getActuacion(), actuacion)) &&
                                (StringUtils.isEmpty(nombre) || StringUtils.containsIgnoreCase(documentDTO.getNombreDocumento(), nombre)))
                        .map(documentoDTO -> {
                            final LocalDateTime dateTimeEcm = Utilities.toLocalDateTime(documentoDTO.getFechaCreacion());
                            if (null != dateTimeEcm) {
                                if (null != startDateTime && null != endDateTime) {
                                    if ((!dateTimeEcm.toLocalDate().isEqual(startDateTime.toLocalDate()) && !dateTimeEcm.toLocalDate().isAfter(startDateTime.toLocalDate()))
                                            || (!dateTimeEcm.toLocalDate().isEqual(endDateTime.toLocalDate()) && !dateTimeEcm.toLocalDate().isBefore(endDateTime.toLocalDate())))
                                        return null;
                                }
                                if (null != startDateTime) {
                                    if (!dateTimeEcm.toLocalDate().isEqual(startDateTime.toLocalDate()) && !dateTimeEcm.toLocalDate().isAfter(startDateTime.toLocalDate()))
                                        return null;
                                }
                                if (null != endDateTime) {
                                    if (!dateTimeEcm.toLocalDate().isEqual(endDateTime.toLocalDate()) && !dateTimeEcm.toLocalDate().isBefore(endDateTime.toLocalDate()))
                                        return null;
                                }
                                final String nroRad = documentoDTO.getNroRadicado();
                                if (!StringUtils.isEmpty(nroRad)) {
                                    try {
                                        final ModeloConsultaDocumentoDTO consultaDocumentoDTO = getModeloConsultaDocumentoDTOBy(nroRad);

                                        if (null != consultaDocumentoDTO) {
                                            documentoDTO.setTipologiaDocumental(consultaDocumentoDTO.getTipologiaDocumental());
                                        }
                                    } catch (Exception ex) {
                                        log.error("Error Occurred: {}", ex);
                                        return null;
                                    }
                                }
                                final ModeloConsultaDocumentoDTO dto = new ModeloConsultaDocumentoDTO();
                                fillModeloConsultaDTO(dto, documentoDTO);
                                return dto;
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
                if (groupTypeList.contains(SecurityGroupType.PUBLIC) && isBasicSearch(startDate, endDate, nroRadicado,
                        nroIdentificacion, depOrigen, depDestino, nroGuia, nombre,
                        asunto, solicitante, destinatario, tipoDocumento, tramite,
                        evento, actuacion, tipologiaDocumental, depCode, sCode, ssCode)) {

                    final List<Folder> folderList = structureAlfresco.getUdListBySecurityType(SecurityGroupType.PUBLIC)
                            .parallelStream()
                            .filter(folderSecurity -> udFOlders.stream()
                                    .noneMatch(folder -> folder.getId().equals(folderSecurity.getId())))
                            .collect(Collectors.toList());
                    if (!folderList.isEmpty()) {
                        final List<ModeloConsultaDocumentoDTO> queriedPublicDocument = consultarCorrespondencias(folderList);
                        queryResponse.addAll(queriedPublicDocument);
                    }
                }
                if (StringUtils.isEmpty(nroIdentificacion) && StringUtils.isEmpty(depOrigen) && StringUtils.isEmpty(depDestino)
                        && StringUtils.isEmpty(nroGuia) && StringUtils.isEmpty(asunto) && StringUtils.isEmpty(solicitante)
                        && StringUtils.isEmpty(destinatario) && StringUtils.isEmpty(tipoDocumento) && StringUtils.isEmpty(tipologiaDocumental)) {
                    log.info("******-----/////----*** Entra condicion de migracion:  " + userLogin  +"    "+communicationType );
                    final CommunicationType communicationTypeSE = CommunicationType.getSelectorBy("SE");
                    final CommunicationType communicationTypeSI = CommunicationType.getSelectorBy("SI");
                    final CommunicationType communicationTypeEE = CommunicationType.getSelectorBy("EE");

                    queryResponse.addAll(getMigratedDocuments(userLogin, communicationTypeSE, startDateTime, endDateTime, nroRadicado, nombre, groupTypeList));
                    queryResponse.addAll(getMigratedDocuments(userLogin, communicationTypeSI, startDateTime, endDateTime, nroRadicado, nombre, groupTypeList));
                    queryResponse.addAll(getMigratedDocuments(userLogin, communicationTypeEE, startDateTime, endDateTime, nroRadicado, nombre, groupTypeList));

                }


            } else {
                final List<ModeloConsultaDocumentoDTO> queriedDocument = consultarCorrespondencias(userLogin, communicationType, startDate, endDate, nroRadicado,
                        nroIdentificacion, depOrigen, depDestino, nroGuia, asunto, solicitante, destinatario, tipoDocumento, tipologiaDocumental);
                for (ModeloConsultaDocumentoDTO consultaDocumentoDTO : queriedDocument) {
                    final Document document = structureAlfresco.findDocumentByNroRad(consultaDocumentoDTO.getNroRadicado());
                    if (null != document) {
                        final String idUd = document.getPropertyValue(ConstantesECM.CMCOR_DOC_ID_UD);
                        final Folder udFolder = structureAlfresco.findUdByIdAndDepCode(idUd);
                        if (null != udFolder) {
                            structureAlfresco.updateUdSecurityGroup(udFolder);
                            final String sGroupEcm = udFolder.getPropertyValue(ConstantesECM.CMCOR_SER_SECURITY_GROUP);
                            final SecurityGroupType securityGroupTypeEcm = SecurityGroupType.getSecurityBy(sGroupEcm);
                            if (groupTypeList.contains(securityGroupTypeEcm)) {

                                if (!StringUtils.isEmpty(nombre) && !StringUtils.containsIgnoreCase(document.getName(), nombre))
                                    continue;

                                final DocumentoDTO documentoDTO = controlAlfresco.transformarDocumento(document, false);
                                fillModeloConsultaDTO(consultaDocumentoDTO, documentoDTO);
                                queryResponse.add(queryResponse.size(), consultaDocumentoDTO);
                            }
                        }
                    }

                }
                if (StringUtils.isEmpty(nroIdentificacion) && StringUtils.isEmpty(depOrigen) && StringUtils.isEmpty(depDestino)
                        && StringUtils.isEmpty(nroGuia) && StringUtils.isEmpty(asunto) && StringUtils.isEmpty(solicitante)
                        && StringUtils.isEmpty(destinatario) && StringUtils.isEmpty(tipoDocumento) && StringUtils.isEmpty(tipologiaDocumental)) {
                    log.info("******-----/////----*** Entra condicion de migracion:  " + userLogin  +"    "+communicationType );
                    queryResponse.addAll(getMigratedDocuments(userLogin, communicationType, startDateTime, endDateTime, nroRadicado, nombre, groupTypeList));
                }
            }




            return response;
        } catch (Exception ex) {
            throw new SystemException("An error has occurred: " + ex.getMessage());
        }
    }

    @Override
    public MensajeRespuesta consultarExpedientes(String userLogin, String depJ, String depP, String sCode, String ssCode,
                                                 String udId, String udName, String desc1, String desc2) throws SystemException {

        if (StringUtils.isEmpty(userLogin)) {
            log.error("No se ha especificado el nombre del usuario");
            throw new SystemException("No se ha especificado el nombre del usuario");
        }

        final List<SecurityGroupType> groupTypeList = getSecurityRoleBy(userLogin);
        if (groupTypeList.isEmpty()) {
            log.error("El usuario {} no contiene roles de Consulta", userLogin);
            throw new SystemException("El usuario " + userLogin + " no contiene roles de Consulta");
        }

        final Map<String, Object> responseMap = new HashMap<>();

        String depCode = depJ;
        if (!StringUtils.isEmpty(depP)) {
            depCode = depP;
        }

        final List<String> codes = getDepCodesByUserName(userLogin);

        final List<Folder> folderList = new ArrayList<>();
        if (!StringUtils.isEmpty(depCode)) {
            if (codes.contains(depCode.trim())) {
                folderList.addAll(structureAlfresco.findAllforQueryModule(depCode.trim(), sCode, ssCode, groupTypeList));
            }
        } else {
            folderList.addAll(structureAlfresco.findAllforQueryModule(codes, groupTypeList));
        }
        final List<UnidadDocumentalDTO> queryResponse = folderList
                .parallelStream()
                .filter(folder -> {
                    final String ecmName = folder.getName();
                    if (ecmName.startsWith(CommunicationType.PA.getCommunicationName())
                            || ecmName.startsWith(CommunicationType.PD.getCommunicationName())) {
                        return false;
                    }

                    final String ecmId = folder.getPropertyValue(ConstantesECM.CMCOR_UD_ID);
                    final String ecmDes1 = folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_1);
                    final String ecmDes2 = folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_2);

                    if (!StringUtils.isEmpty(udId) && !StringUtils.containsIgnoreCase(ecmId, udId)) return false;
                    if (!StringUtils.isEmpty(desc1) && !StringUtils.containsIgnoreCase(ecmDes1, desc1)) return false;
                    if (!StringUtils.isEmpty(desc2) && !StringUtils.containsIgnoreCase(ecmDes2, desc2)) return false;
                    return StringUtils.isEmpty(udName) || StringUtils.containsIgnoreCase(ecmName, udName);
                })
                .map(folder -> {
                    try {
                        structureAlfresco.updateUdSecurityGroup(folder);
                        final UnidadDocumentalDTO dto = controlAlfresco.transformarUnidadDocumental(folder);
                        dto.setEcmObjId(folder.getId());
                       // boolean  docMigrado = folder.getPropertyValue("cmcor:migrado");
                        String estadoMigrado = folder.getPropertyValue("cmcor:estado");
                        log.info("************************************** Expediente Migrado "+ /*docMigrado   +*/ "  ESTADO    ::: "+estadoMigrado);
                        log.info("************************************** ID  Expediente Migrado "+folder.getId());
                        if (estadoMigrado!=null && estadoMigrado.equals("Migrado")){
                            log.info("************************************** Set docMigrado "+estadoMigrado);
                            dto.setEstado("Migración");
                        }
                        log.info("************************************** ESTADO  Expediente Migrado "+dto.getEstado());
                        return dto;
                    } catch (SystemException e) {
                        log.error(e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (StringUtils.isEmpty(depCode) && isBasicSearch(udId, udName, desc1, desc2) && groupTypeList.contains(SecurityGroupType.PUBLIC)) {
            final List<Folder> folders = structureAlfresco.getUdListBySecurityType(SecurityGroupType.PUBLIC)
                    .parallelStream().filter(folder -> folderList.stream()
                            .noneMatch(folder1 -> folder.getId().equals(folder1.getId())))
                    .collect(Collectors.toList());
            for (Folder folder : folders) {
                final UnidadDocumentalDTO dto = controlAlfresco.transformarUnidadDocumental(folder);
                dto.setEcmObjId(folder.getId());
                queryResponse.add(queryResponse.size(), dto);
            }
        }

        final MensajeRespuesta response = new MensajeRespuesta();

        responseMap.put("consultar-expedientes", queryResponse);
        response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
        response.setMensaje(ConstantesECM.SUCCESS);
        response.setResponse(responseMap);

        return response;
    }

    @Override
    public MensajeRespuesta verDetalleDocumento(String docEcmId) throws SystemException {

        if (StringUtils.isEmpty(docEcmId)) {
            log.error("No se ha especificado el id Ecm");
            throw new SystemException("No se ha especificado el id Ecm");
        }
        try {
            final MensajeRespuesta response = new MensajeRespuesta();
            final Map<String, Object> responseMap = new HashMap<>();

            response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            response.setMensaje(ConstantesECM.SUCCESS);
            response.setResponse(responseMap);

            final Session session = controlAlfresco.getSession();
            final Document document = (Document) session.getObject(docEcmId);
            final String nroRad = document.getPropertyValue(ConstantesECM.CMCOR_DOC_RADICADO);
            DocumentoDTO documentoDTO = controlAlfresco.transformarDocumento(document, true);
            log.info("***********Esta antes del IF ***************");
            if (!StringUtils.isEmpty(nroRad)) {
                final ModeloConsultaDocumentoDTO modeloConsultaDocumentoDTOBy = getModeloConsultaDocumentoDTOBy(nroRad);
                log.info("***********Esta antes del IF despues de  getModeloConsultaDocumentoDTOBy ***************");
                if (null != modeloConsultaDocumentoDTOBy) {
                    fillModeloConsultaDTO(modeloConsultaDocumentoDTOBy, documentoDTO);
                    documentoDTO = modeloConsultaDocumentoDTOBy;
                }
            }

            final List<Document> allVersions = document.getAllVersions();
            log.info("***********Esta antes del FOR ***************");
            for (Document allVersion : allVersions) {
                documentoDTO.getHistorialVersiones().add(controlAlfresco.transformarDocumento(allVersion, true));
            }
            log.info("***********Esta llegando al Final***************");
            responseMap.put("consultar-documento", documentoDTO);
            return response;

        } catch (CmisObjectNotFoundException e) {
            log.error("En el repositorio no existe documento con id {}", docEcmId);
            throw new SystemException("En el repositorio no existe documento con id " + docEcmId);
        } catch (Exception ex) {
            log.error("An error has occurred: {}", ex.getMessage());
            throw new SystemException("An error has occurred: " + ex.getMessage());
        }
    }


    @Override
    public MensajeRespuesta consultarDocsExpediente(String ecmFolderId) throws SystemException {

        final MensajeRespuesta response = new MensajeRespuesta();
        final Map<String, Object> responseMap = new HashMap<>();
        response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
        response.setMensaje(ConstantesECM.SUCCESS);
        response.setResponse(responseMap);

        try {
            final Session session = controlAlfresco.getSession();
            final Folder folder = (Folder) session.getObject(ecmFolderId);

            final List<Document> ecmDocumentsFromFolder = controlAlfresco.getEcmDocumentsFromFolder(folder);
            final List<DocumentoDTO> documentoDTOS = ecmDocumentsFromFolder.parallelStream()
                    .filter(document -> {
                        final String docTypeEcm = document.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO);
                        final DocumentType documentType = DocumentType.getDocumentTypeBy(docTypeEcm);
                        return documentType == DocumentType.MAIN || documentType == null;
                    })
                    .map(document -> {
                        try {
                            final DocumentoDTO documentoDTO = controlAlfresco.transformarDocumento(document, false);
                            final String nroRad = documentoDTO.getNroRadicado();
                            if (!StringUtils.isEmpty(nroRad)) {
                                final ModeloConsultaDocumentoDTO consultaDocumentoDTO = getModeloConsultaDocumentoDTOBy(nroRad);
                                if (null != consultaDocumentoDTO) {
                                    documentoDTO.setTipologiaDocumental(consultaDocumentoDTO.getTipologiaDocumental());
                                }
                            }
                            return documentoDTO;
                        } catch (Exception ex) {
                            log.error(ex);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            responseMap.put("consultar-documentos", documentoDTOS);
            return response;
        } catch (CmisObjectNotFoundException e) {
            log.error("No existe carpeta con id {}", ecmFolderId);
            throw new SystemException("Ecm Folder not found with ID: " + ecmFolderId);
        } catch (Exception ex) {
            log.error("An error has Occurred: " + ex.getMessage());
            throw new SystemException("An error has Occurred: " + ex.getMessage());
        }
    }

    private List<ModeloConsultaDocumentoDTO> consultarCorrespondencias(String userLogin, CommunicationType communicationType, String startDate, String endDate, String nroRadicado, String nroIdentificacion,
                                                                       String depOrigen, String depDestino, String nroGuia, String asunto, String solicitante,
                                                                       String destinatario, String tipoDocumento, String tipologiaDocumental) throws SystemException {
        final WebTarget webTarget = getWebTarget();
        final Response response = webTarget.path("consulta-web-api/consulta/")
                .queryParam("userLogin", null == userLogin || "".equals(userLogin.trim()) ? null : userLogin)
                .queryParam("fecha_ini", null == startDate || "".equals(startDate.trim()) ? null : startDate)
                .queryParam("fecha_fin", null == endDate || "".equals(endDate.trim()) ? null : endDate)
                .queryParam("tipo_comunicacion", communicationType == null ? null : communicationType.name())
                .queryParam("nro_radicado", null == nroRadicado || "".equals(nroRadicado.trim()) ? null : nroRadicado)
                .queryParam("nro_identificacion", null == nroIdentificacion || "".equals(nroIdentificacion.trim()) ? null : nroIdentificacion)
                .queryParam("dep_origen", null == depOrigen || "".equals(depOrigen.trim()) ? null : depOrigen)
                .queryParam("dep_destino", null == depDestino || "".equals(depDestino.trim()) ? null : depDestino)
                .queryParam("nro_guia", null == nroGuia || "".equals(nroGuia.trim()) ? null : nroGuia)
                .queryParam("asunto", null == asunto || "".equals(asunto.trim()) ? null : asunto)
                .queryParam("solicitante", null == solicitante || "".equals(solicitante.trim()) ? null : solicitante)
                .queryParam("destinatario", null == destinatario || "".equals(destinatario.trim()) ? null : destinatario)
                .queryParam("tipoDocumento", null == tipoDocumento || "".equals(tipoDocumento.trim()) ? null : tipoDocumento)
                .queryParam("tipologiaDocumental", null == tipologiaDocumental || "".equals(tipologiaDocumental.trim()) ? null : tipologiaDocumental)
                .request().get();
        log.info("Sending response: " + response.getMetadata());
        if (Response.Status.OK.getStatusCode() != response.getStatus()) {
            log.error("Error al Consultar Servicio Correspondencia {}", response.getStatusInfo());
            throw new SystemException("Error al Consultar Servicio Correspondencia " + response.getStatusInfo());
        }
        final List<ModeloConsultaDocumentoDTO> resultList = new ArrayList<>();
        final List<Object> list = response.readEntity(new GenericType<>(List.class));
        list.forEach(o -> {
            if (o instanceof HashMap) {
                final ModeloConsultaDocumentoDTO dto = new ModeloConsultaDocumentoDTO();
                dto.setEstado((String) ((HashMap) o).get("estado"));
                dto.setFuncionario((String) ((HashMap) o).get("funcionario"));
                final Object dateObjVenc = ((HashMap) o).get("fechaVencimiento");
                if (dateObjVenc instanceof Long) {
                    final Long radDateInt = (Long) dateObjVenc;
                    dto.setFechaVencimiento(new Date(radDateInt));
                } else if (dateObjVenc instanceof Integer) {
                    final Integer radDateInt = (Integer) dateObjVenc;
                    dto.setFechaVencimiento(new Date(radDateInt));
                }
                dto.setNroGuia((String) ((HashMap) o).get("nroGuia"));
                dto.setTipoCMC((String) ((HashMap) o).get("tipoCMC"));
                dto.setEstadoEntrega((String) ((HashMap) o).get("estadoEntrega"));
                dto.setTipologiaDocumental((String) ((HashMap) o).get("tipologiaDocumental"));
                final String nroRad = (String) ((HashMap) o).get("nroRadicado");
                if (!StringUtils.isEmpty(nroRad)) {
                    dto.setNroRadicado(reduceRad(nroRad));
                }
                final Object dateObjRad = ((HashMap) o).get("fechaRadicacion");
                if (dateObjRad instanceof Long) {
                    final Long radDateInt = (Long) dateObjRad;
                    dto.setFechaRadicacion(new Date(radDateInt));
                } else if (dateObjRad instanceof Integer) {
                    final Integer radDateInt = (Integer) dateObjRad;
                    dto.setFechaRadicacion(new Date(radDateInt));
                }
                dto.setDestinatario((String) ((HashMap) o).get("destinatario"));
                dto.setNombreRemitente((String) ((HashMap) o).get("nombreRemitente"));
                dto.setCodigoDependencia((String) ((HashMap) o).get("codigoDependencia"));
                dto.setIdUnidadDocumental((String) ((HashMap) o).get("idUnidadDocumental"));
                dto.setDocAutor((String) ((HashMap) o).get("docAutor"));
                resultList.add(resultList.size(), dto);
            } else if (o instanceof ModeloConsultaDocumentoDTO) {
                resultList.add(resultList.size(), (ModeloConsultaDocumentoDTO) o);
            }
        });
        return resultList;
    }

    private Collection<? extends ModeloConsultaDocumentoDTO> getMigratedDocuments(String userLogin, CommunicationType communicationType, LocalDateTime startDateTime,
                                                                                  LocalDateTime endDateTime, String nroRadicado, String nombre,
                                                                                  List<SecurityGroupType> groupTypeList) throws SystemException {

        log.info("*************************--------------------- Entro a Consulta de migrados: Datos:");
        log.info("************************* Entro a Consulta de migrados: Datos: userLogin:: " + userLogin );
        log.info("************************* Entro a Consulta de migrados: Datos: communicationType:: " + communicationType);
        log.info("************************* Entro a Consulta de migrados: Datos: nroRadicado:: " + nroRadicado);
        if (StringUtils.isEmpty(userLogin) || null == groupTypeList) {
            final List<ModeloConsultaDocumentoDTO> responseList = new ArrayList<>();
            if (!StringUtils.isEmpty(nroRadicado)) {
                final Document documentByNroRad = structureAlfresco.findDocumentByNroRad(nroRadicado);
                if (null != documentByNroRad) {
                    log.info("************************* ------ documentByNroRad de migrados: Datos: documentByNroRad:: " + documentByNroRad);
                    final DocumentoDTO documentoDTO = controlAlfresco.transformarDocumento(documentByNroRad, true);
                    log.info("************************* ------ documentoDTO de migrados: Datos: documentoDTO:: " + documentoDTO.getIdDocumento());
                    log.info("************************* ------ documentoDTO de migrados: Datos: documentoDTO:: " + documentoDTO.getDocumento());
                    final ModeloConsultaDocumentoDTO consultaDocumentoDTO = new ModeloConsultaDocumentoDTO();
                    responseList.add(responseList.size(), newfillModeloConsultaDTO(consultaDocumentoDTO, documentoDTO));
                    log.info("************************* ------ responseList de migrados: Datos: responseList.getIdDocumento :: " + responseList.get(0).getIdDocumento());
                    log.info("************************* ------ responseList de migrados: Datos: responseList :: " + responseList.get(0).getDocumento());
                }
            }
            return responseList;
        }

        final List<Folder> allforQueryModule = structureAlfresco.findAllforQueryModule(getDepCodesByUserName(userLogin), groupTypeList);
        return allforQueryModule.parallelStream()
                .map(folder -> {

                    final List<ModeloConsultaDocumentoDTO> dtos = new ArrayList<>();
                    final ItemIterable<CmisObject> children = folder.getChildren();
                    for (CmisObject cmisObject : children) {

                        final String ecmType = cmisObject.getType().getId();
                        if (ecmType.endsWith("cmmig:CM_Migracion")) {



                            final DocumentType docTypeEcm = DocumentType
                                    .getDocumentTypeBy(cmisObject.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO));
                            final CommunicationType communicationTypeEcm = CommunicationType
                                    .getSelectorBy(cmisObject.getPropertyValue(ConstantesECM.CMMIG_DOC_TIPO_COMUNICACION));

                            if (docTypeEcm == DocumentType.MAIN && (communicationType == null || communicationType == communicationTypeEcm)) {

                                final Calendar radDateEcm = cmisObject.getPropertyValue(ConstantesECM.CMCOR_FECHA_RADICACION);


                                final LocalDateTime dateTimeEcm = Utilities.toLocalDateTime(radDateEcm);
                                if (null != dateTimeEcm) {
                                    if (null != startDateTime && null != endDateTime) {
                                        if ((!dateTimeEcm.toLocalDate().isEqual(startDateTime.toLocalDate()) && !dateTimeEcm.toLocalDate().isAfter(startDateTime.toLocalDate()))
                                                || (!dateTimeEcm.toLocalDate().isEqual(endDateTime.toLocalDate()) && !dateTimeEcm.toLocalDate().isBefore(endDateTime.toLocalDate())))
                                            continue;
                                    }
                                    if (null != startDateTime) {
                                        if (!dateTimeEcm.toLocalDate().isEqual(startDateTime.toLocalDate()) && !dateTimeEcm.toLocalDate().isAfter(startDateTime.toLocalDate()))
                                            continue;
                                    }
                                    if (null != endDateTime && !dateTimeEcm.toLocalDate().isEqual(endDateTime.toLocalDate()) && !dateTimeEcm.toLocalDate().isBefore(endDateTime.toLocalDate()))
                                        continue;
                                }

                                final String nroRadEcm = cmisObject.getPropertyValue(ConstantesECM.CMCOR_DOC_RADICADO);
                                final String docNameEcm = cmisObject.getName();

                                if (!StringUtils.isEmpty(nroRadicado) && !StringUtils.containsIgnoreCase(nroRadEcm, nroRadicado)
                                        || !StringUtils.isEmpty(nombre) && !StringUtils.containsIgnoreCase(docNameEcm, nombre)) {
                                    continue;
                                }
                                final String idDocumento = cmisObject.getId();
                                final String docAuthorEcm = cmisObject.getPropertyValue(ConstantesECM.CMCOR_DOC_AUTOR);
                                final String docIdExpEcm = cmisObject.getPropertyValue(ConstantesECM.CMCOR_DOC_ID_UD);
                                final boolean esAnuladoEcm = cmisObject.getPropertyValue(ConstantesECM.CMCOR_DOC_ANULADO);
                                final String tipoDocEcm = cmisObject.getPropertyValue(ConstantesECM.CMCOR_DOC_TIPO_DOCUMENTAL);
                                final String tramiteEcm = cmisObject.getPropertyValue(ConstantesECM.CMCOR_DOC_TRAMITE);
                                final String eventoEcm = cmisObject.getPropertyValue(ConstantesECM.CMCOR_DOC_EVENTO);
                                final String actuacionEcm = cmisObject.getPropertyValue(ConstantesECM.CMCOR_DOC_ACTUACION);

                                log.info("-----------*****-------- Valores de IdDocumento: " + idDocumento);
                                log.info("-----------*****-------- Valores de nroRadEcm: " + nroRadEcm);

                                final ModeloConsultaDocumentoDTO modeloConsultaDocumentoDTO = new ModeloConsultaDocumentoDTO();
                                modeloConsultaDocumentoDTO.setIdDocumento(idDocumento);
                                modeloConsultaDocumentoDTO.setTipologiaDocumental(tipoDocEcm);
                                modeloConsultaDocumentoDTO.setNroRadicado(nroRadEcm);
                                modeloConsultaDocumentoDTO.setNombreDocumento(docNameEcm);
                                modeloConsultaDocumentoDTO.setDocAutor(docAuthorEcm);
                                modeloConsultaDocumentoDTO.setIdUnidadDocumental(docIdExpEcm);
                                modeloConsultaDocumentoDTO.setAnulado(esAnuladoEcm);
                                //modeloConsultaDocumentoDTO.setEstado("");
                                modeloConsultaDocumentoDTO.setTramite(tramiteEcm);
                                modeloConsultaDocumentoDTO.setEvento(eventoEcm);
                                modeloConsultaDocumentoDTO.setActuacion(actuacionEcm);
                                modeloConsultaDocumentoDTO.setOrigen("Migración");


                                if (null != radDateEcm) {
                                    modeloConsultaDocumentoDTO.setFechaRadicacion(radDateEcm.getTime());
                                }
                                dtos.add(dtos.size(), modeloConsultaDocumentoDTO);
                            }
                        }
                    }
                    return dtos;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<SecurityGroupType> getSecurityRoleBy(String username) throws SystemException {
        final List<String> roleList = getRolesByUser(username);
        return SecurityGroupType.getSecurityListBy(roleList);
    }

    private List<String> getRolesByUser(String username) throws SystemException {
        final WebTarget wt = getWebTarget();
        final Response response = wt.path("/rol-web-api/rol/listar/login/" + username)
                .request().get();
        if (Response.Status.OK.getStatusCode() != response.getStatus()) {
            log.error("Error al Obtener los roles del usuario: " + username);
            throw new SystemException("Error al Obtener los roles del usuario: " + username);
        }
        final List<String> roleList = new ArrayList<>();
        final List<Object> responseList = response.readEntity(new GenericType<>(List.class));
        responseList.forEach(o -> {
            if (o instanceof HashMap) {
                roleList.add(roleList.size(), (String) ((HashMap) o).get("rol"));
            } else if (o instanceof RolDTO) {
                roleList.add(roleList.size(), ((RolDTO) o).getRol());
            }
        });
        return roleList;
    }

    private WebTarget getWebTarget() {
        return ClientBuilder.newClient().target(CORRESPONDENCIA_ENDPOINT);
    }

    private List<DocumentoDTO> getDocumentsBy(List<Folder> allforQueryModule, List<Folder> udFolders) throws SystemException {
        final List<DocumentoDTO> documents = new ArrayList<>();
        for (Folder folder : allforQueryModule) {
            final String ecmFolderName = folder.getName();
            boolean flag = false;
            System.out.println("############## FolderName: " + ecmFolderName + " -- " + folder.getFolderParent().getName());
            for (CmisObject documentChild : folder.getChildren()) {
                final String ecmDocName = documentChild.getName();
                if (!documentChild.getType().getId().startsWith("D:cm") || ecmFolderName.startsWith(CommunicationType.PA.getCommunicationName())
                        || ecmFolderName.startsWith(CommunicationType.PD.getCommunicationName())) {
                    continue;
                }
                final String docTypeEcm = documentChild.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO);
                final DocumentType documentType = DocumentType.getDocumentTypeBy(docTypeEcm);
                if (documentType == DocumentType.MAIN) {
                    System.out.println("----DocumentName: " + ecmDocName);
                    documents.add(documents.size(), controlAlfresco.transformarDocumento((Document) documentChild, false));
                    flag = true;
                }
            }
            if (flag) udFolders.add(udFolders.size(), folder);
        }
        return documents;
    }

    private FuncionarioDTO getFuncionarioBy(String username) throws SystemException {
        final WebTarget wt = getWebTarget();
        final Response response = wt.path("/funcionarios-web-api/funcionarios/" + username)
                .request().get();
        if (Response.Status.OK.getStatusCode() != response.getStatus()) {
            log.error("Error al Obtener las dependencias del usuario: " + username);
            throw new SystemException("Error al Obtener las dependencias del usuario: " + username);
        }
        return response.readEntity(FuncionarioDTO.class);
    }

    private List<String> getDepCodesByUserName(String username) throws SystemException {
        final FuncionarioDTO dto = getFuncionarioBy(username);
        return dto.getDependencias().stream()
                .parallel().map(DependenciaDTO::getCodDependencia)
                .collect(Collectors.toList());
    }

    private String reduceRad(String largeRad) {
        final int index;
        return (index = largeRad.indexOf("--")) != -1
                ? largeRad.substring(index + 2) : largeRad;
    }
    private ModeloConsultaDocumentoDTO newfillModeloConsultaDTO(ModeloConsultaDocumentoDTO modeloConsultaDocumentoDTO, DocumentoDTO documentoDTO) {
        modeloConsultaDocumentoDTO.setIdDocumento(documentoDTO.getIdDocumento());
        modeloConsultaDocumentoDTO.setDocAutor(documentoDTO.getDocAutor());
        modeloConsultaDocumentoDTO.setIdUnidadDocumental(documentoDTO.getIdUnidadDocumental());
        modeloConsultaDocumentoDTO.setNombreDocumento(documentoDTO.getNombreDocumento());
        modeloConsultaDocumentoDTO.setFechaCreacion(documentoDTO.getFechaCreacion());
        modeloConsultaDocumentoDTO.setNroRadicado(documentoDTO.getNroRadicado());
        modeloConsultaDocumentoDTO.setTamano(documentoDTO.getTamano());
        modeloConsultaDocumentoDTO.setVersionLabel(documentoDTO.getVersionLabel());
        modeloConsultaDocumentoDTO.setDocumento(documentoDTO.getDocumento());
        modeloConsultaDocumentoDTO.setTipoDocumento(documentoDTO.getTipoDocumento());
        modeloConsultaDocumentoDTO.setAnulado(documentoDTO.isAnulado());
        if (StateType.MIGRATED.getStateName().equalsIgnoreCase(documentoDTO.getEstado())) {
            modeloConsultaDocumentoDTO.setEstado(documentoDTO.getEstado());
        }
        if (StringUtils.isEmpty(modeloConsultaDocumentoDTO.getTipologiaDocumental())) {
            modeloConsultaDocumentoDTO.setTipologiaDocumental(documentoDTO.getTipologiaDocumental());
        }
        if (null == modeloConsultaDocumentoDTO.getFechaRadicacion()) {
            modeloConsultaDocumentoDTO.setFechaRadicacion(documentoDTO.getFechaRadicacion());
        }
        return modeloConsultaDocumentoDTO;
    }

    private void fillModeloConsultaDTO(ModeloConsultaDocumentoDTO modeloConsultaDocumentoDTO, DocumentoDTO documentoDTO) {
        modeloConsultaDocumentoDTO.setIdDocumento(documentoDTO.getIdDocumento());
        modeloConsultaDocumentoDTO.setDocAutor(documentoDTO.getDocAutor());
        modeloConsultaDocumentoDTO.setIdUnidadDocumental(documentoDTO.getIdUnidadDocumental());
        modeloConsultaDocumentoDTO.setNombreDocumento(documentoDTO.getNombreDocumento());
        modeloConsultaDocumentoDTO.setFechaCreacion(documentoDTO.getFechaCreacion());
        modeloConsultaDocumentoDTO.setNroRadicado(documentoDTO.getNroRadicado());
        modeloConsultaDocumentoDTO.setTamano(documentoDTO.getTamano());
        modeloConsultaDocumentoDTO.setVersionLabel(documentoDTO.getVersionLabel());
        modeloConsultaDocumentoDTO.setDocumento(documentoDTO.getDocumento());
        modeloConsultaDocumentoDTO.setTipoDocumento(documentoDTO.getTipoDocumento());
        modeloConsultaDocumentoDTO.setAnulado(documentoDTO.isAnulado());
        if (StateType.MIGRATED.getStateName().equalsIgnoreCase(documentoDTO.getOrigen())) {
            modeloConsultaDocumentoDTO.setOrigen(documentoDTO.getOrigen());
        }
        if (StringUtils.isEmpty(modeloConsultaDocumentoDTO.getTipologiaDocumental())) {
            modeloConsultaDocumentoDTO.setTipologiaDocumental(documentoDTO.getTipologiaDocumental());
        }
        if (null == modeloConsultaDocumentoDTO.getFechaRadicacion()) {
            modeloConsultaDocumentoDTO.setFechaRadicacion(documentoDTO.getFechaRadicacion());
        }
    }

    private List<ModeloConsultaDocumentoDTO> consultarCorrespondencias(List<Folder> udPublicList) throws SystemException {
        final List<ModeloConsultaDocumentoDTO> dtoList = new ArrayList<>();
        for (Folder folder : udPublicList) {
            for (CmisObject documentChild : folder.getChildren()) {
                if (documentChild.getType().getId().startsWith("D:cm")) {
                    final String docTypeEcm = documentChild.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO);
                    final DocumentType documentType = DocumentType.getDocumentTypeBy(docTypeEcm);
                    final String nroRad = documentChild.getPropertyValue(ConstantesECM.CMCOR_DOC_RADICADO);
                    if (documentType == DocumentType.MAIN && !StringUtils.isEmpty(nroRad)) {
                        final DocumentoDTO documentoDTO = controlAlfresco.transformarDocumento((Document) documentChild, false);
                        if (StringUtils.equalsIgnoreCase(StateType.MIGRATED.getStateName(), documentoDTO.getEstado())) {
                            final ModeloConsultaDocumentoDTO consultaDocumentoDTO = new ModeloConsultaDocumentoDTO();
                            fillModeloConsultaDTO(consultaDocumentoDTO, documentoDTO);
                            dtoList.add(dtoList.size(), consultaDocumentoDTO);
                            continue;
                        }
                        final ModeloConsultaDocumentoDTO dto = getModeloConsultaDocumentoDTOBy(nroRad);
                        if (dto != null) {
                            fillModeloConsultaDTO(dto, documentoDTO);
                            dtoList.add(dtoList.size(), dto);
                        }
                    }
                }
            }
        }
        return dtoList;
    }

    private ModeloConsultaDocumentoDTO getModeloConsultaDocumentoDTOBy(String nroRad) throws SystemException {
        final String nroRadReduced = reduceRad(nroRad);
        log.info("-----*****------ nroRadReduced:::::::::"+ nroRadReduced);
        final List<ModeloConsultaDocumentoDTO> documentoDTOS = consultarCorrespondencias(null, null, null,
                null, nroRadReduced, null, null, null,
                null, null, null, null, null, null);
        if (!documentoDTOS.isEmpty()) {
            log.info("-----*****------ documentoDTOS:::::::::"+ documentoDTOS.get(0));
            return documentoDTOS.get(0);
        }
        final Collection<? extends ModeloConsultaDocumentoDTO> migratedDocuments = getMigratedDocuments(null, null, null, null, nroRadReduced, null, null);
        if (!migratedDocuments.isEmpty()) {
            log.info("-----*****------ documentoDTOS--Migrados:::::::::"+ migratedDocuments);
            return migratedDocuments.iterator().next();
        }
        return null;
    }

    private boolean isBasicSearch(String udId, String udName, String desc1, String desc2) {
        if (!StringUtils.isEmpty(udId) || !StringUtils.isEmpty(udName) || !StringUtils.isEmpty(desc1)) {
            return false;
        }
        return StringUtils.isEmpty(desc2);
    }

    private boolean isBasicSearch(String startDate, String endDate, String nroRadicado,
                                  String nroIdentificacion, String depOrigen, String depDestino, String nroGuia, String nombre,
                                  String asunto, String solicitante, String destinatario, String tipoDocumento, String tramite,
                                  String evento, String actuacion, String tipologiaDocumental, String depCode, String sCode, String ssCode) {
        if (!StringUtils.isEmpty(startDate) || !StringUtils.isEmpty(endDate) || !StringUtils.isEmpty(nroRadicado) ||
                !StringUtils.isEmpty(nroIdentificacion) || !StringUtils.isEmpty(depOrigen) || !StringUtils.isEmpty(depDestino) ||
                !StringUtils.isEmpty(nroGuia) || !StringUtils.isEmpty(nombre) || !StringUtils.isEmpty(asunto) || !StringUtils.isEmpty(solicitante) ||
                !StringUtils.isEmpty(destinatario) || !StringUtils.isEmpty(tipoDocumento) || !StringUtils.isEmpty(tramite) ||
                !StringUtils.isEmpty(evento) || !StringUtils.isEmpty(actuacion) || !StringUtils.isEmpty(depCode) ||
                !StringUtils.isEmpty(sCode) || !StringUtils.isEmpty(ssCode)) {
            return false;
        }
        return StringUtils.isEmpty(tipologiaDocumental);
    }
}
