package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ReporteContent;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.StructureAlfresco;
import co.com.soaint.ecm.domain.entity.*;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by amartinez on 24/01/2018.
 */
@Log4j2
@BusinessControl
public class RecordServices implements IRecordServices {

    //@Value("${tokken.record}")
    private final String encoding = SystemParameters.encodeAlfrescCredenciales();
    private final String NONE_DATE = "none";

    @Autowired
    private ContentControl contentControl;
    @Autowired
    @Qualifier("recordStructure")
    private StructureAlfresco recordStructure;
    @Autowired
    @Qualifier("contentStructure")
    private StructureAlfresco contentStructure;

    @Autowired
    private ReporteContent reporteContent;

    @Value("${mensaje.error.sistema}")
    private String errorSistema = "";
    @Value("${mensaje.error.sistema.generico}")
    private String errorSistemaGenerico = "";
    @Value("${header.accept}")
    private String headerAccept = "";
    @Value("${header.authorization}")
    private String headerAuthorization = "";
    @Value("${header.value.application.type}")
    private String valueApplicationType = "";
    @Value("${header.value.authorization}")
    private String valueAuthorization = "";
    @Value("${mensaje.error.negocio.fallo}")
    private String errorNegocioFallo = "";
    @Value("${nodeType}")
    private String tipoNodo = "";
    @Value("${recordCategory}")
    private String recordCategoria = "";
    @Value("${tag.propiedades}")
    private String tagPropiedades = "";
    @Value("${recordCarpeta}")
    private String recordCarpeta = "";
    @Value("${id.sitio.record.manager}")
    private String idRecordManager = "";
    @Value("${api.service.alfresco}")
    private String apiServiceAlfresco = "";
    @Value("${tag.nombre}")
    private String nombre = "";
    @Value("${tag.descripcion}")
    private String descripcion = "";
    @Value("${tag.periodo}")
    private String periodo = "";
    @Value("${tag.localizacion}")
    private String localizacion = "";
    @Value("${tag.propiedad.periodo}")
    private String propiedadPeriodo = "";
    @Value("${valor.periodo}")
    private String valorPeriodo = "";
    @Value("${valor.mensaje.descripcion}")
    private String mensajeDescripcion = "";
    @Value("${auto.close.num.days}")
    private int AUTO_CLOSE_NUM_DAYS = 8;

    @Override
    public MensajeRespuesta crearEstructuraEcm(List<EstructuraTrdDTO> structure) throws SystemException {
        try {
            if (contentStructure == null) {
                log.info("Content Control es nulo en metodo crearEstructuraEcm");
                throw ExceptionBuilder.newBuilder()
                        .withMessage("Content Control es nulo")
                        .buildSystemException();
            }

            MensajeRespuesta mensajeRespuesta = contentStructure.crearEstructura(structure);
            if (!ConstantesECM.SUCCESS_COD_MENSAJE.equals(mensajeRespuesta.getCodMensaje())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("Ocurrio un error al generar la estructura en el Content")
                        .buildSystemException();
            }
            //mensajeRespuesta = crearEstructura(structure);
            mensajeRespuesta = this.recordStructure.crearEstructura(structure);
            if (!ConstantesECM.SUCCESS_COD_MENSAJE.equals(mensajeRespuesta.getCodMensaje())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("Ocurrio un error al generar la estructura en el Record")
                        .buildSystemException();
            }
            return mensajeRespuesta;
        } catch (SystemException s) {
            log.error("Error Ocurrido {}", s.getCause());
            throw new SystemException("Error Ocurrido " + s.getMessage());
        }
    }

    private String crearCarpetaRecord(final UnidadDocumentalDTO documentalDTO) throws SystemException {

        String queryPrincipal = "select * from rmc:rmarecordCategoryCustomProperties" +
                " where rmc:xSeccion = '" + documentalDTO.getCodigoDependencia() + "'" +
                " and  rmc:xCodSerie = '" + documentalDTO.getCodigoSerie() + "' ";
        if (!"".equals(documentalDTO.getCodigoSubSerie())) {
            String condicionSubserie = " and  rmc:xCodSubSerie = '" + documentalDTO.getCodigoSubSerie() + "' ";
            queryPrincipal = queryPrincipal.concat(condicionSubserie);
        }
        final Map<String, String> query = new HashMap<>();
        final JSONObject parametro = new JSONObject();

        query.put("query", queryPrincipal);
        query.put("language", "cmis");
        parametro.put("query", query);

        final Map<String, Object> mapProperties = new HashMap<>();

        mapProperties.put(ConstantesECM.RMC_X_IDENTIFICADOR, documentalDTO.getId());
        mapProperties.put(ConstantesECM.RMC_X_CODIGO_DEPENDENCIA_CARPETA, documentalDTO.getCodigoDependencia());
        mapProperties.put("cm:title", documentalDTO.getNombreUnidadDocumental());

        final JSONObject carpeta = new JSONObject();
        carpeta.put("name", documentalDTO.getNombreUnidadDocumental());
        carpeta.put("properties", mapProperties);
        carpeta.put(tipoNodo, recordCarpeta);
        return crearNodo(carpeta, buscarRuta(parametro));
    }

    @Override
    public MensajeRespuesta gestionarUnidadDocumentalECM(final UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("Ejecutando MensajeRespuesta gestionarUnidadDocumentalECM(UnidadDocumentalDTO unidadDocumentalDTO)");
        gestionarUnidadDocumental(unidadDocumentalDTO);
        return MensajeRespuesta.newInstance()
                .codMensaje("0000").mensaje("Success")
                .build();
    }

    private void gestionarUnidadDocumental(final UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        if (ObjectUtils.isEmpty(unidadDocumentalDTO) || StringUtils.isEmpty(unidadDocumentalDTO.getId())) {
            throw new SystemException("No se ha especificado el Id de la Unidad Documental");
        }
        if (StringUtils.isEmpty(unidadDocumentalDTO.getAccion())) {
            throw new SystemException("No se ha especificado la accion a realizar");
        }
        final AccionUsuario accionUsuario = AccionUsuario.getActionBy(unidadDocumentalDTO.getAccion());
        if (accionUsuario == null) {
            throw new SystemException("La accion especificada no es valida '" + unidadDocumentalDTO.getAccion() + "'");
        }
        final String idUD = unidadDocumentalDTO.getId();
        final Folder udFolder = getRecordFolderByUdId(idUD).orElse(null);
        switch (accionUsuario) {
            case ABRIR:
                abrirUnidadDocumental(udFolder, unidadDocumentalDTO);
                break;
            case CERRAR:
                cerrarUnidadDocumental(udFolder, unidadDocumentalDTO);
                break;
            case REACTIVAR:
                reactivarUnidadDocumental(udFolder, unidadDocumentalDTO);
                break;
        }
        final Optional<UnidadDocumentalDTO> udById = contentControl.getUDById(idUD);
        if (udById.isPresent()) {
            final UnidadDocumentalDTO dto = udById.get();
            final String dependencyCode = dto.getCodigoDependencia();
            if (!StringUtils.isEmpty(dependencyCode)) {
                reporteContent.processReporte8(dependencyCode);
            }
        }
    }

    private void reactivarUnidadDocumental(Folder recordFolder, UnidadDocumentalDTO unidadDocumental) throws SystemException {
//        abrirUnidadDocumental(recordFolder, unidadDocumental);
        contentControl.getUDFolderById(unidadDocumental.getId()).ifPresent(contentFolder -> {
            final ItemIterable<CmisObject> children = recordFolder.getChildren();
            children.forEach(cmisObject -> {
                if (cmisObject.getType().getId().startsWith("D:cm")) {
                    final Document document = (Document) cmisObject;
                    final String docName = document.getName();
                    final ContentStream contentStream = document.getContentStream();
                    if (eliminarRecordObjECM(BaseTypeId.CMIS_DOCUMENT, document.getId().split(";")[0])) {
                        log.info("Deleted Document {}", docName);
                    }
                    final Map<String, Object> tmpMap = contentControl.obtenerPropiedadesDocumento(document);
                    tmpMap.put(PropertyIds.CONTENT_STREAM_FILE_NAME, docName);
                    contentFolder.createDocument(tmpMap, contentStream, VersioningState.MAJOR);
                }
            });
            final String recordFolderName = recordFolder.getName();
            final boolean objECM = eliminarRecordObjECM(BaseTypeId.CMIS_FOLDER, recordFolder.getId());
            if (objECM) {
                log.info("Deleted Record Folder {}", recordFolderName);
            }
            final Map<String, Object> map = new HashMap<>();
            map.put(ConstantesECM.CMCOR_UD_DISPOSICION, null);
            map.put(ConstantesECM.CMCOR_UD_ESTADO, null);
            map.put(ConstantesECM.CMCOR_UD_FASE_ARCHIVO, null);
            map.put(ConstantesECM.CMCOR_UD_FECHA_CIERRE, null);
            map.put(ConstantesECM.CMCOR_UD_INACTIVO, false);
            map.put(ConstantesECM.CMCOR_UD_CERRADA, false);
            map.put(ConstantesECM.CMCOR_UD_ACCION, AccionUsuario.REACTIVAR.getState());
            contentFolder.updateProperties(map);
        });
    }

    @Override
    public MensajeRespuesta gestionarUnidadesDocumentalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException {
        log.info("Ejecutando MensajeRespuesta gestionarUnidadesDocumentalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTO)");
        for (UnidadDocumentalDTO unidadDocumentalDTO :
                unidadDocumentalDTOS) {
            gestionarUnidadDocumental(unidadDocumentalDTO);
        }
        final MensajeRespuesta respuesta = new MensajeRespuesta();
        respuesta.setMensaje("Operación realizada satisfactoriamente");
        respuesta.setCodMensaje("0000");
        return respuesta;
    }

    @Override
    public void eliminarRecordFolder(String idUnidadDocumental) throws SystemException {
        Optional<Folder> optionalFolder = getRecordFolderByUdId(idUnidadDocumental);
        if (optionalFolder.isPresent()) {
            Folder recordFolder = optionalFolder.get();
            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
            Response response = wt.path("/record-folders/" + recordFolder.getId())
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .delete();
            if (response.getStatus() != HttpURLConnection.HTTP_NO_CONTENT) {
                throw new SystemException("Ocurrio un error al eliminar la carpeta de registro");
            }
        }
    }

    @Override
    public Optional<Folder> getRecordFolderByUdId(String idUnidadDocumental) throws SystemException {
        if (StringUtils.isEmpty(idUnidadDocumental)) {
            return Optional.empty();
        }
        idUnidadDocumental = idUnidadDocumental.toUpperCase().trim();
        log.info("Invocando el metodo obtener  record folder con Id {}", idUnidadDocumental);
        String query = "select * from rmc:rmarecordFolderCustomProperties where " + ConstantesECM.RMC_X_IDENTIFICADOR + " = '" + idUnidadDocumental + "'";
        Session session = contentControl.getSession();
        ItemIterable<QueryResult> queryResults = session.query(query, false);
        Iterator<QueryResult> iterator = queryResults.iterator();
        if (iterator.hasNext()) {
            QueryResult next = iterator.next();
            String objectId = next.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            CmisObject object = session.getObject(session.createObjectId(objectId));
            return Optional.of((Folder) object);
        }
        return Optional.empty();
    }

    @Override
    public Response modifyRecord(String idObject, Map<String, Object> propertyMap, EcmRecordObjectType ecmRecordObjectType) {
        final JSONObject properties = new JSONObject();
        properties.put("properties", propertyMap);
        final WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
        final WebTarget webTarget;
        switch (ecmRecordObjectType) {
            case RECORD_FOLDER:
                webTarget = wt.path("/record-folders/" + idObject);
                break;
            case RECORD_CATEGORY:
                webTarget = wt.path("/record-categories/" + idObject);
                break;
            default:
                webTarget = wt.path("/records/" + idObject);
                break;
        }
        return webTarget.request()
                .header(headerAuthorization, valueAuthorization + " " + encoding)
                .header(headerAccept, valueApplicationType)
                .put(Entity.json(properties.toString()));
    }

    private void compruebaIdEstado(UnidadDocumentalDTO unidadDocumentalDTO, String consecutivo, boolean comprobarConsecutivo) throws SystemException {
        if (unidadDocumentalDTO == null) {
            return;
        }
        if (StringUtils.isEmpty(unidadDocumentalDTO.getId())) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("No se procesa el ID = null")
                    .buildSystemException();
        }
        final StateType stateType = StateType.getStateBy(unidadDocumentalDTO.getEstado());
        if (stateType == null) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("No se procesa el estado = null")
                    .buildSystemException();
        }
        if (comprobarConsecutivo && stateType != StateType.REJECTED) {
            final String consetutiveTmp = unidadDocumentalDTO.getConsecutivoTransferencia();
            if (StringUtils.isEmpty(consetutiveTmp)) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("No se ha especificado el numero de transferencia para alguna Unidad Documental")
                        .buildSystemException();
            }
            if (consecutivo != null && !StringUtils.equals(consecutivo, consetutiveTmp)) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("Los consecutivos introducidos son diferentes")
                        .buildSystemException();
            }
        }
    }

    void compruebaIdEstado(List<UnidadDocumentalDTO> unidadDocumentalDTOS, boolean comprobarConsecutivo) throws SystemException {
        if (ObjectUtils.isEmpty(unidadDocumentalDTOS)) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("No se ha realizado alguna accion sobre las unidades documentales")
                    .buildSystemException();
        }
        String consecutivo = null;
        for (final UnidadDocumentalDTO dto :
                unidadDocumentalDTOS) {
            compruebaIdEstado(dto, consecutivo, comprobarConsecutivo);
            consecutivo = dto.getConsecutivoTransferencia();
        }
    }

    @Override
    public MensajeRespuesta listarUdDisposicionFinal(DisposicionFinalDTO disposicionFinalDTO, String dependencyCode) throws SystemException {
        log.info("Se procede a listar las unidades documentales por tipo de disposicion");
        if (StringUtils.isEmpty(dependencyCode) || StringUtils.containsIgnoreCase(dependencyCode.trim(), "null")) {
            throw new SystemException("No se procesa codigo de dependencia = '" + dependencyCode + "'");
        }
        if (ObjectUtils.isEmpty(disposicionFinalDTO)) {
            throw new SystemException("No se ha identificado la disposicion de la Unidad Documental");
        }
        UnidadDocumentalDTO dto = disposicionFinalDTO.getUnidadDocumentalDTO();
        if (ObjectUtils.isEmpty(dto)) {
            throw new SystemException("No se ha identificado la Unidad Documental");
        }
        try {

            final String desc1 = dto.getDescriptor1();
            final String desc2 = dto.getDescriptor2();
            final String name = dto.getNombreUnidadDocumental();
            final String id = dto.getId();

            final List<FinalDispositionType> disposicionFinalList = FinalDispositionType.convert(disposicionFinalDTO.getDisposicionFinalList());
            final List<UnidadDocumentalDTO> responseList = new ArrayList<>();

            final List<Folder> udWhereIn = recordStructure
                    .findAllUDListBy(dependencyCode, dto.getCodigoSerie(), dto.getCodigoSubSerie());

            for (Folder folder : udWhereIn) {

                final String ecmId = folder.getPropertyValue(ConstantesECM.RMC_X_IDENTIFICADOR);
                final String ecmName = folder.getName();

                if (!StringUtils.isEmpty(name) && !StringUtils.containsIgnoreCase(ecmName, name)) continue;
                if (!StringUtils.isEmpty(id) && !StringUtils.containsIgnoreCase(ecmId, id)) continue;

                final String phaseFileEcm = folder.getPropertyValue(ConstantesECM.RMC_X_FASE_ARCHIVO);
                final PhaseType phaseTypeEcm = PhaseType.getPhaseTypeBy(phaseFileEcm);

                if (phaseTypeEcm == PhaseType.AC) {
                    final String disposision = folder.getPropertyValue(ConstantesECM.RMC_X_DISPOSICION_FINAL_CARPETA);
                    final FinalDispositionType tmpType = FinalDispositionType.getDispositionBy(disposision);
                    if (tmpType != null && disposicionFinalList.contains(tmpType)) {
                        String statetransfEcm = folder.getPropertyValue(ConstantesECM.RMC_X_ESTADO_TRANSFERENCIA);
                        final StateType stateTypeTransEcm = StateType.getStateBy(statetransfEcm);
                        String stateDispEcm = folder.getPropertyValue(ConstantesECM.RMC_X_ESTADO_DISPOSICION);
                        final StateType stateTypeDispEcm = StateType.getStateBy(stateDispEcm);
                        final String idUd = folder.getPropertyValue(ConstantesECM.RMC_X_IDENTIFICADOR);
                        if (stateTypeTransEcm == StateType.LOCATED && (stateTypeDispEcm == null || stateTypeDispEcm == StateType.REJECTED)) {

                            final Optional<UnidadDocumentalDTO> dtoOptional = contentControl.getUDById(idUd);
                            if (dtoOptional.isPresent()) {
                                final UnidadDocumentalDTO documentalDTO = dtoOptional.get();

                                final String ecmDes1 = documentalDTO.getDescriptor1();
                                final String ecmDes2 = documentalDTO.getDescriptor2();

                                if (!StringUtils.isEmpty(desc1) && !StringUtils.containsIgnoreCase(ecmDes1, desc1))
                                    continue;
                                if (!StringUtils.isEmpty(desc2) && !StringUtils.containsIgnoreCase(ecmDes2, desc2))
                                    continue;

                                documentalDTO.setDisposicion(tmpType.getDispositionName());
                                documentalDTO.setEstado(stateTypeTransEcm == StateType.LOCATED ? "" : StateType.REJECTED.getStateName());
                                responseList.add(responseList.size(), documentalDTO);
                                new Thread(() -> {

                                    final String xDispDateEcm = folder. getPropertyValue(ConstantesECM.RMC_X_DISPOSICION_HASTA_FECHA);
                                    final LocalDateTime dateTimeEcm = Utilities.getDispDateTimeEcm(xDispDateEcm);
                                    final LocalDateTime currentDate = LocalDateTime.now();

                                    if (null != dateTimeEcm && currentDate.isBefore(dateTimeEcm)) {
                                        final LocalDateTime localDateTime = Utilities.toLocalDateTime(Calendar.getInstance());
                                        final Map<String, Object> map = new HashMap<>();
                                        map.put(ConstantesECM.RMC_X_DISPOSICION_HASTA_FECHA, localDateTime.toString());
                                        final Response response = modifyRecord(folder.getId(), map, EcmRecordObjectType.RECORD_FOLDER);
                                        log.info("Modifying RecordFolder: {} -- ResponseStatus: {}", folder.getName(), response.getStatus());
                                    }
                                }).start();
                            }
                        }
                    }
                }
            }
            contentControl.formatoListaUnidadDocumental(responseList);
            final Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("unidadesDocumentales", responseList);
            return MensajeRespuesta.newInstance()
                    .response(responseMap)
                    .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                    .mensaje(ConstantesECM.SUCCESS).build();
        } catch (Exception e) {
            log.error("Ocurrio un error al listar las unidades documentales por tipo de disposicion --- {}", e.getMessage());
            throw new SystemException("An error has ocurred " + e.getMessage());
        } finally {
            log.info("Fin del metodo listarUdDisposicionFinal");
        }
    }

    @Override
    public MensajeRespuesta aprobarRechazarDisposicionesFinales(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException {
        log.info("Ejecutando metodo MensajeRespuesta aprobarRechazarDisposicionesFinalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS)");
        compruebaIdEstado(unidadDocumentalDTOS, false);
        if (ObjectUtils.isEmpty(unidadDocumentalDTOS)) {
            throw new SystemException("La lista de unidades documentales enviada esta vacia");
        }

        for (UnidadDocumentalDTO dto : unidadDocumentalDTOS) {
            aprobarRechazarDisposisionFinal(dto);
        }
        return MensajeRespuesta.newInstance()
                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                .mensaje("Operación realizada satisfactoriamente")
                .build();
    }

    private void aprobarRechazarDisposisionFinal(UnidadDocumentalDTO dto) throws SystemException {

        final StateType stateType = StateType.getStateBy(dto.getEstado());

        if (stateType != StateType.APPROVED && stateType != StateType.REJECTED) {
            throw new SystemException("No se procesa el estado " + stateType);
        }

        final String idUnidadDocumental = dto.getId();
        final Optional<Folder> optionalFolder = contentControl.getUDFolderById(idUnidadDocumental);

        if (optionalFolder.isPresent()) {

            final FinalDispositionType dispositionType = FinalDispositionType.getDispositionBy(dto.getDisposicion());
            final Folder folder = optionalFolder.get();

            if (stateType == StateType.APPROVED) {
                if (dispositionType == FinalDispositionType.E) {
                    contentControl.eliminarUnidadDocumental(idUnidadDocumental);
                    return;
                }
            }

            final Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put(ConstantesECM.CMCOR_UD_ESTADO, stateType.getStateName());
            final String dispositionString = folder.getPropertyValue(ConstantesECM.CMCOR_UD_DISPOSICION);
            propertyMap.put(ConstantesECM.CMCOR_UD_DISPOSICION, dispositionType == null ? dispositionString : dispositionType.getDispositionName());

            folder.updateProperties(propertyMap);
            Optional<Folder> recordFolderByUdId = getRecordFolderByUdId(idUnidadDocumental);
            recordFolderByUdId.ifPresent(folderTmp -> {
                propertyMap.put(ConstantesECM.RMC_X_ESTADO_DISPOSICION, propertyMap.remove(ConstantesECM.CMCOR_UD_ESTADO));
                propertyMap.put(ConstantesECM.RMC_X_DISPOSICION_FINAL_CARPETA, propertyMap.remove(ConstantesECM.CMCOR_UD_DISPOSICION));
                modifyRecord(folderTmp.getId(), propertyMap, EcmRecordObjectType.RECORD_FOLDER);
            });
        }
    }

    private void abrirUnidadDocumental(Folder udRecordFolder, UnidadDocumentalDTO unidadDocumental) throws SystemException {
        final String idUnidadDocumental = unidadDocumental.getId();
        if (udRecordFolder == null) {
            throw new SystemException("No existe en los record Unidad Documental ID = '" + idUnidadDocumental + "'");
        }
        final Optional<Folder> optionalFolder = contentControl.getUDFolderById(idUnidadDocumental);
        if (!optionalFolder.isPresent()) {
            throw new SystemException("No existe en en Content Unidad Documental ID = '" + idUnidadDocumental + "'");
        }
        final boolean isClosed = udRecordFolder.getPropertyValue(ConstantesECM.RMA_IS_CLOSED);
        if (!isClosed) {
            throw new SystemException("La unidad documental con ID = '" + idUnidadDocumental + "' ya esta abierta");
        }
        final Map<String, Object> propertyMapRecord = new HashMap<>();

        propertyMapRecord.put(ConstantesECM.RMA_IS_CLOSED, false);
        propertyMapRecord.put(ConstantesECM.RMC_X_AUTO_CIERRE, LocalDateTime.now().plusDays(AUTO_CLOSE_NUM_DAYS).toString());

        closeOrOpenUnidadDocumentalRecord(udRecordFolder.getId(), propertyMapRecord);

        final Map<String, Object> propertyMapContent = new HashMap<>();
        propertyMapContent.put(ConstantesECM.CMCOR_UD_INACTIVO, false);
        propertyMapContent.put(ConstantesECM.CMCOR_UD_CERRADA, false);
        propertyMapContent.put(ConstantesECM.CMCOR_UD_ACCION, AccionUsuario.ABRIR.getState());
        optionalFolder.get().updateProperties(propertyMapContent);
    }

    /**
     * Permite Abrir/Cerrar Record Folder
     *
     * @return identificador de la subserie creada
     */
    private void closeOrOpenUnidadDocumentalRecord(String id, Map<String, Object> propertyMap) throws SystemException {
        log.info("Se entra al metodo closeOrOpenUnidadDocumentalRecord para cerrar la unidad documental con id: {}", id);
        try {
            Response response = modifyRecord(id, propertyMap, EcmRecordObjectType.RECORD_FOLDER);
            if (response.getStatus() != HttpURLConnection.HTTP_OK) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            }
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - Abrir o cerrar el record folder ");
        }
    }

    private void cerrarUnidadDocumental(Folder recordFolder, UnidadDocumentalDTO unidadDocumental) throws SystemException {
        final String documentalId = unidadDocumental.getId();
        final Optional<UnidadDocumentalDTO> optionalDto = contentControl.getUDToCloseById(documentalId);
        if (!optionalDto.isPresent()) {
            throw new SystemException("No existe en en Content Unidad Documental ID = '" + documentalId + "'");
        }
        final boolean isClosed = !ObjectUtils.isEmpty(recordFolder) ? recordFolder.getPropertyValue(ConstantesECM.RMA_IS_CLOSED) : false;
        if (isClosed) {
            throw new SystemException("La unidad documental con ID = '" + documentalId + "' ya esta Cerrada");
        }

        unidadDocumental = optionalDto.get();

        final boolean isNew;
        final String idRecordFolder;
        if (!ObjectUtils.isEmpty(recordFolder)) {
            isNew = false;
            idRecordFolder = recordFolder.getId();
        } else {
            idRecordFolder = crearCarpetaRecord(unidadDocumental);
            isNew = true;
        }

        unidadDocumental
                .getListaDocumentos()
                .parallelStream().forEach(documentoDTO -> {
                    try {
                        completeRecordFile(documentoDTO, idRecordFolder);
                    } catch (Exception ex) {
                        log.error(ex);
                    }
                });

        final Map<String, Object> propertyMapRecord = new HashMap<>();
        propertyMapRecord.put(ConstantesECM.RMA_IS_CLOSED, true);
        propertyMapRecord.put(ConstantesECM.RMC_X_AUTO_CIERRE, NONE_DATE);

        final Map<String, Object> propertyMapContent = new HashMap<>();
        propertyMapContent.put(ConstantesECM.CMCOR_UD_ACCION, AccionUsuario.CERRAR.getState());
        propertyMapContent.put(ConstantesECM.CMCOR_UD_INACTIVO, true);
        propertyMapContent.put(ConstantesECM.CMCOR_UD_CERRADA, true);

        try {
            final Session session = contentControl.getSession();
            recordFolder = (Folder) session.getObject(idRecordFolder);
            if (isNew) {
                final Folder recordCategoryParent = recordFolder.getFolderParent();
                final String xDisposition = recordCategoryParent.getPropertyValue(ConstantesECM.RMC_X_DISPOSICION_FINAL_CATEGORIA);

                final String localDateTimeString;
                if (!ObjectUtils.isEmpty(unidadDocumental.getFechaCierre())) {

                    final String xRetArchivoString = recordCategoryParent.getPropertyValue(ConstantesECM.RMC_X_RET_ARCHIVO_GESTION);
                    LocalDateTime localDateTime = Utilities.toLocalDateTime(unidadDocumental.getFechaCierre());

                    if (!StringUtils.isEmpty(xRetArchivoString)) {
                        final long xRetArchivolong = Long.parseLong(xRetArchivoString);
                        localDateTime = ((null == localDateTime) ? Utilities.toLocalDateTime(Calendar.getInstance()) : localDateTime)
                                .plusYears(xRetArchivolong);
                    }
                    localDateTimeString = localDateTime.toString();
                } else {
                    localDateTimeString = getRetentionDateOf(recordFolder, PhaseType.AG);
                    propertyMapContent.put(ConstantesECM.CMCOR_UD_FECHA_CIERRE, GregorianCalendar.getInstance());
                }

                propertyMapRecord.put(ConstantesECM.RMC_X_FASE_ARCHIVO, PhaseType.AG.getPhaseName());
                propertyMapRecord.put(ConstantesECM.RMC_X_DISPOSICION_HASTA_FECHA, localDateTimeString);
                propertyMapRecord.put(ConstantesECM.RMC_X_DISPOSICION_FINAL_CARPETA, xDisposition);

                propertyMapContent.put(ConstantesECM.CMCOR_UD_FASE_ARCHIVO, PhaseType.AG.getPhaseName());
                propertyMapContent.put(ConstantesECM.CMCOR_UD_DISPOSICION, xDisposition);
            }

            contentControl.getUDFolderById(documentalId)
                    .ifPresent(folder -> folder.updateProperties(propertyMapContent));
            closeOrOpenUnidadDocumentalRecord(idRecordFolder, propertyMapRecord);
        } catch (Exception ex) {
            log.error("An error has occurred {}", ex.getMessage());
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }

    String getRetentionDateOf(Folder udRecordFolder, PhaseType phaseType) {
        if (udRecordFolder != null && (phaseType == PhaseType.AG || phaseType == PhaseType.AC)) {
            try {
                final Folder parentFolder = udRecordFolder.getFolderParent();
                final String property = phaseType == PhaseType.AG
                        ? ConstantesECM.RMC_X_RET_ARCHIVO_GESTION : ConstantesECM.RMC_X_RET_ARCHIVO_CENTRAL;
                final String xRetArchivoString = parentFolder.getPropertyValue(property);
                if (!StringUtils.isEmpty(xRetArchivoString)) {
                    final long xRetArchivolong = Long.parseLong(xRetArchivoString);
                    return LocalDateTime.now().plusYears(xRetArchivolong).toString();
                }
            } catch (Exception ex) {
                log.error("Arror occurred: " + ex.getMessage());
            }
        }
        return NONE_DATE;
    }

    private void completeRecordFile(DocumentoDTO documentoDTO, final String idRecordFolder) throws SystemException {
        //Se declara el record
        String s = declararRecord(documentoDTO);
        log.info("Declarando '{}' como record con id {}", documentoDTO.getNombreDocumento(), s);
        Map<String, Object> properties = new HashMap<>();
        properties.put("cm:title", documentoDTO.getNombreDocumento());
        properties.put("cm:name", documentoDTO.getNombreDocumento());
        properties.put("cm:description", documentoDTO.getNombreDocumento());
        Response response = modifyRecord(documentoDTO.getIdDocumento(), properties, EcmRecordObjectType.RECORD_DOCUMENT);
        log.info("Status: {}", response.getStatus());
        //Se completa el record
        String s1 = completeRecord(documentoDTO.getIdDocumento());
        log.info("Completando '{}' como record con id {}", documentoDTO.getNombreDocumento(), s1);
        //Se archiva el record
        String s2 = fileRecord(documentoDTO.getIdDocumento(), idRecordFolder);
        log.info("Archivando '{}' como record con id {}", documentoDTO.getNombreDocumento(), s2);
    }

    /**
     * Permite Completar Records
     *
     * @param idRecord Identificador del documento en record
     * @return identificador de la subserie creada
     * @throws SystemException SystemException
     */
    private String completeRecord(String idRecord) throws SystemException {
        log.info("Se entra al metodo completeRecord para el record de id: {}", idRecord);
        try {
            if (!idRecord.isEmpty()) {
                WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
                Response response = wt.path("/records/" + idRecord + "/complete")
                        .request()
                        .header(headerAuthorization, valueAuthorization + " " + encoding)
                        .header(headerAccept, valueApplicationType)
                        .post(Entity.json(idRecord));
                if (response.getStatus() != 201) {
                    throw ExceptionBuilder.newBuilder()
                            .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                            .buildBusinessException();
                } else {
                    return obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
                }
            }
            return null;

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - Completar record ");
        }
    }

    /**
     * Permite archivar Records
     *
     * @param idRecord       Identificador del documento en record
     * @param idRecordFolder identificador de la unidad Documental a donde se va a llevar el documento
     * @return identificador de la subserie creada
     * @throws SystemException SystemException
     */
    private String fileRecord(String idRecord, String idRecordFolder) throws SystemException {
        log.info("Se entra al metodo fileRecord para archivar el documento de id: {}", idRecord);
        try {
            JSONObject recordFolder = new JSONObject();
            if (!idRecord.isEmpty() && !idRecordFolder.isEmpty()) {
                recordFolder.put("targetParentId", idRecordFolder);
                WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
                Response response = wt.path("/records/" + idRecord + "/file")
                        .request()
                        .header(headerAuthorization, valueAuthorization + " " + encoding)
                        .header(headerAccept, valueApplicationType)
                        .post(Entity.json(recordFolder.toString()));
                if (response.getStatus() != 201) {
                    throw ExceptionBuilder.newBuilder()
                            .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                            .buildBusinessException();
                } else {
                    return obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
                }
            }
            return null;

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - Guardar record en su record folder ");
        }
    }

    /**
     * Permite declarar un documento como record
     *
     * @param documentoDTO Obj documento dentro del content
     * @return el id del record creado
     * @throws SystemException SystemException
     */
    private String declararRecord(DocumentoDTO documentoDTO) throws SystemException {
        final String idDocumentoContent = documentoDTO.getIdDocumento();
        log.info("iniciar - Declarar como record el documento con id: {}", idDocumentoContent);
        try {
            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
            Response response = wt.path("/files/" + idDocumentoContent + "/declare")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(idDocumentoContent));
            if (response.getStatus() != HttpURLConnection.HTTP_CREATED) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            }
            final String fatherId = obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
            final Map<String, String> query = new HashMap<>();
            final JSONObject parametro = new JSONObject();
            final String tipologiaDocumental = !StringUtils.isEmpty(documentoDTO.getTipologiaDocumental()) ?
                    documentoDTO.getTipologiaDocumental() : "";
            final String referencia = StringUtils.isEmpty(documentoDTO.getNroRadicado()) ?
                    idDocumentoContent : documentoDTO.getNroRadicado();
            query.put("rmc:referencia", referencia);
            query.put("rmc:tipologia_documental", tipologiaDocumental);
            parametro.put("properties", query);
            response = wt.path("/records/" + idDocumentoContent)
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .put(Entity.json(parametro.toString()));
            if (response.getStatus() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            }
            return fatherId;

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - Declarar como record el documento ");
        }
    }

    /**
     * Permite buscar la el id de la ruta necesaria de los nodos para poder realizar las operaciones en el record
     *
     * @param entrada objeto json con los parametros necesarios par apoder efectua la operacion
     * @return el id de la ruta que se esta buscando
     * @throws SystemException
     */
    private String buscarRuta(JSONObject entrada) throws SystemException {
        log.info("iniciar - buscar ruta: {}", entrada);
        try {
            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.API_SEARCH_ALFRESCO));
            Response response = wt.path("")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(entrada.toString()));

            if (response.getStatus() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdRuta(new JSONObject(response.readEntity(String.class)), "nodeType", "rma:recordCategory");
            }

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - buscar ruta ");
        }
    }

    /**
     * Permite obtener el id del plan de ficheros
     *
     * @return el id del plan de ficheros
     * @throws SystemException
     */
    private String obtenerIdFilePlan() throws SystemException {
        log.info("iniciar - obtener id file plan: {}");
        try {
            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.API_CORE_ALFRESCO));
            Response response = wt.path("/sites/" + idRecordManager + "/containers")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .get();

            if (response.getStatus() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdRuta(new JSONObject(response.readEntity(String.class)), "folderId", "documentLibrary");
            }
        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - obtener id file plan ");
        }
    }

    /**
     * Permite crear el root category
     *
     * @param entrada objeto json con los paramtreos necesario para la creacion de la categoria
     * @return el id de la categoria
     * @throws SystemException
     */
    private String crearRootCategory(JSONObject entrada) throws SystemException {
        log.info("iniciar - Crear categoria padre: {}", entrada);
        try {

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
            Response response = wt.path("/file-plans/" + obtenerIdFilePlan() + "/categories")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(entrada.toString()));

            if (response.getStatus() != 201) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
            }

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - crear categoria padre ");
        }
    }

    /**
     * Permite crear nodo partiendo del tipo de categoria
     *
     * @param entrada objeto json con la informacion necesaria para crear el nodo
     * @param idSerie identificardor del nodo padre
     * @return el id del nodo creado
     * @throws SystemException
     */
    private String crearNodo(JSONObject entrada, String idSerie) throws SystemException {
        log.info("iniciar - Crear categoria hija: {}", entrada.toString());
        try {

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
            Response response = wt.path("/record-categories/" + idSerie + "/children")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(entrada.toString()));
            if (response.getStatus() != 201) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
            }
        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - crear categoria hija ");
        }
    }

    /**
     * Permite crear los tiempos de retencion asociado a series y subseries
     *
     * @param entrada objeto json con lo paramtros necesarios para crear las los tiempos
     * @param idPadre id del nodo al que se le aplicaran los tiempos de retencion
     * @return
     * @throws SystemException
     */
    private String crearTiempoRetencion(Map<String, Object> entrada, String idPadre) throws SystemException {
        log.info("iniciar - Crear tiempo retencion: {}", entrada.toString());
        try {
            JSONObject jsonPostDataretencion = new JSONObject();
            jsonPostDataretencion.put(nombre, entrada.get(nombre));
            jsonPostDataretencion.put(descripcion, entrada.get(descripcion));
            jsonPostDataretencion.put(periodo, entrada.get(periodo));
            jsonPostDataretencion.put(localizacion, entrada.get(localizacion));
            jsonPostDataretencion.put(propiedadPeriodo, entrada.get(propiedadPeriodo));
            jsonPostDataretencion.put("automatic", true);
            //jsonPostDataretencion.put(eventoCompletar, entrada.get(eventoCompletar));
            //JSONArray events = new JSONArray();
            //events.put(entrada.get("events"));
            //jsonPostDataretencion.put(evento, events);

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.API_SERVICE_ALFRESCO));
            Response response = wt.path("/" + idPadre + "/dispositionschedule/dispositionactiondefinitions")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(jsonPostDataretencion.toString()));
            if (response.getStatus() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
            }

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - crear categoria hija ");
        }
    }

    /**
     * Permite obtner el id del objeto json de respuesta
     *
     * @param respuestaJson objeto json que contiene el mensaje de repuesta para procesar
     * @return el valor del campo id en la respusta json
     */
    private String obtenerIdPadre(JSONObject respuestaJson) {
        String codigoId = "";
        Iterator keys = respuestaJson.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            if ("entry".equalsIgnoreCase(key.toString())) {
                JSONObject valor = respuestaJson.getJSONObject((String) key);
                codigoId = valor.getString("id");
            }
        }
        return codigoId;
    }

    /**
     * Permite obtener id de la ruta
     *
     * @param respuestaJson objeto json con las informacion necesaria para obtener el id de la ruta
     * @param nodo
     * @param nombreNodo
     * @return
     */
    private String obtenerIdRuta(JSONObject respuestaJson, String nodo, String nombreNodo) {
        String codigoId = "";
        Iterator keys = respuestaJson.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            if ("list".equalsIgnoreCase(key.toString())) {
                JSONObject valor = respuestaJson.getJSONObject((String) key);
                JSONArray listaNodosJson = valor.getJSONArray("entries");
                for (int i = 0; i < listaNodosJson.length(); i++) {
                    JSONObject valorJson = (JSONObject) listaNodosJson.get(i);
                    codigoId = obtenerIdNodo(valorJson, nodo, nombreNodo);
                }
            }
        }
        return codigoId;
    }

    /**
     * Permite obtener el id del nodo
     *
     * @param respuestaJson objeto json con los paramtros necesarios para obtener el id del nodo
     * @param nodo          tipo de nodo a manejar
     * @param nombreNodo    nombre del nodo
     * @return id del nodo
     */
    private String obtenerIdNodo(JSONObject respuestaJson, String nodo, String nombreNodo) {
        String nodoId = "";
        Iterator keys1 = respuestaJson.keys();
        while (keys1.hasNext()) {
            Object key1 = keys1.next();
            if ("entry".equalsIgnoreCase(key1.toString())) {
                JSONObject valor1 = respuestaJson.getJSONObject((String) key1);
                if (valor1.getString(nodo).equalsIgnoreCase(nombreNodo))
                    nodoId = valor1.getString("id");
            }
        }
        return nodoId;
    }

    private boolean eliminarRecordObjECM(BaseTypeId type, String id) {
        final String path = ((type == BaseTypeId.CMIS_DOCUMENT) ? "/records/" : "/record-folders/") + id;
        WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
        Response response = wt.path(path)
                .request()
                .header(headerAuthorization, valueAuthorization + " " + encoding)
                .header(headerAccept, valueApplicationType)
                .delete();
        return (response.getStatus() != HttpURLConnection.HTTP_NO_CONTENT);
    }
}