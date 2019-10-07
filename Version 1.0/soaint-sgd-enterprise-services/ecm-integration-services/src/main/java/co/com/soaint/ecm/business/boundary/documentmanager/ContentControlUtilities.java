package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Configuracion;
import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.*;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl.ConnectionImpl;
import co.com.soaint.ecm.domain.entity.*;
import co.com.soaint.ecm.domain.entity.DocumentType;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.RadicadoDTO;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.alfresco.cmis.client.AlfrescoDocument;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.util.ContentStreamUtils;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.impl.MimeTypes;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Setter
abstract class ContentControlUtilities implements ContentControl {

    private static final long serialVersionUID = 155L;
    private final String CORRESPONDENCIA_ENDPOINT = SystemParameters
            .getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT_CORRESPONDENCIA);

    Configuracion configuracion;

    private ContentStamper contentStamper;
    private DigitalSignature digitalSignature;
    private StructureAlfresco structureAlfresco;
    private ConnectionImpl connection;
    private ContentTemplate contentTemplate;
    @Value("${id.cliente.firma}")
    private String usuarioFirma = "";
    @Value("${password.cliente.firma}")
    private String passFirma = "";
    @Override
    public List<DocumentoDTO> obtenerDocumentosArchivadosList(String dependencyCode) throws SystemException {
        log.info("Se procede a obtener los documentos archivados");
        if (StringUtils.isEmpty(dependencyCode)) {
            throw new SystemException("No se ha especificado el codigo de la dependencia");
        }
        final List<DocumentoDTO> responseList = new ArrayList<>();
        final List<Document> savedDocumentsFrom = structureAlfresco.findSavedDocumentsFrom(dependencyCode);
        for (final Document document : savedDocumentsFrom) {
            responseList.add(responseList.size(), transformarDocumento(document, true));
        }
        return responseList;
    }

    public List<UnidadDocumentalDTO> listarUnidadesDocumentales(UnidadDocumentalDTO dto) throws SystemException {

        final String depCode = dto.getCodigoDependencia();
        if (StringUtils.isEmpty(depCode)) {
            throw new SystemException("No se ha especificado el codigo de la dependencia");
        }
        return structureAlfresco.findAllUDListBy(dto)
                .parallelStream()
                .filter(folder -> {
                    final String nameFolder = folder.getName();
                    if (StringUtils.startsWithIgnoreCase(nameFolder, CommunicationType.PA.getCommunicationName())
                            || StringUtils.startsWithIgnoreCase(nameFolder, CommunicationType.PD.getCommunicationName())) {
                        return false;
                    }
                    final Folder parentFolder = folder.getFolderParent();
                    final boolean isRadicadora = parentFolder.getPropertyValue(ConstantesECM.CMCOR_DEP_RADICADORA);
                    return !isRadicadora;
                })
                .map(this::transformarUnidadDocumental)
                .collect(Collectors.toList());
    }

    @Override
    public Folder getFolderFromRootByName(String folderName) throws SystemException {
        Session session = getSession();
        Folder rootFolder = session.getRootFolder();
        final ItemIterable<CmisObject> children = rootFolder.getChildren();
        for (CmisObject cmisObject :
                children) {
            if (cmisObject.getName().equals(folderName)) {
                return (Folder) cmisObject;
            }
        }
        return null;
    }

    /**
     * Convierte la Interfaz Document de opencemis a un DocumentoDTO
     *
     * @param document         Objeto a transformar
     * @param getContentStream
     * @return DocumentoDTO
     */
    @Override
    public DocumentoDTO transformarDocumento(Document document, boolean getContentStream) throws SystemException {
        final DocumentoDTO documentoDTO = new DocumentoDTO();
        final String ecmType = document.getType().getId();
        if (document == null)
            return documentoDTO;
        try {
            int index;
            documentoDTO.setIdDocumento(document.getId());
            final String nroRadicado = document.getPropertyValue(ConstantesECM.CMCOR_DOC_RADICADO);
            if (!StringUtils.isEmpty(nroRadicado) & !ecmType.endsWith("cmmig:CM_Migracion")) {
                documentoDTO.setNroRadicado((index = nroRadicado.indexOf("--")) != 0 ? nroRadicado.substring(index + 2) : nroRadicado);
            }else {
                documentoDTO.setNroRadicado(nroRadicado);
            }
            documentoDTO.setTipologiaDocumental(document.getPropertyValue(ConstantesECM.CMCOR_DOC_TIPO_DOCUMENTAL));
            documentoDTO.setNombreRemitente(document.getPropertyValue(ConstantesECM.CMCOR_DOC_REMITENTE));
            documentoDTO.setNombreDocumento(document.getName());
            documentoDTO.setIdDocumentoPadre(document.getPropertyValue(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL));
            documentoDTO.setNombreProceso(document.getPropertyValue(ConstantesECM.CMCOR_NOMBRE_PROCESO));
            Calendar calendar = document.getPropertyValue(ConstantesECM.CMCOR_FECHA_RADICACION);
            if (calendar != null) {
                documentoDTO.setFechaRadicacion(calendar.getTime());
            }
            documentoDTO.setTipoDocumento(document.getPropertyValue(PropertyIds.CONTENT_STREAM_MIME_TYPE));
            documentoDTO.setTamano(document.getContentStreamLength() + "");
            documentoDTO.setTipoPadreAdjunto(document.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO));
            documentoDTO.setVersionLabel(getLabelDocument(document.getVersionLabel(), true));
            documentoDTO.setIdUnidadDocumental(document.getPropertyValue(ConstantesECM.CMCOR_DOC_ID_UD));
            documentoDTO.setDocAutor(document.getPropertyValue(ConstantesECM.CMCOR_DOC_AUTOR));
            documentoDTO.setDocumento(getContentStream ? getDocumentBytes(document.getContentStream()) : null);
            documentoDTO.setTramite(document.getPropertyValue(ConstantesECM.CMCOR_DOC_TRAMITE));
            documentoDTO.setEvento(document.getPropertyValue(ConstantesECM.CMCOR_DOC_EVENTO));
            documentoDTO.setActuacion(document.getPropertyValue(ConstantesECM.CMCOR_DOC_ACTUACION));
            documentoDTO.setAnulado(document.getPropertyValue(ConstantesECM.CMCOR_DOC_ANULADO));
            calendar = document.getPropertyValue(PropertyIds.CREATION_DATE);
            if (calendar != null) {
                documentoDTO.setFechaCreacion(calendar.getTime());
            }

            final String nroReferido = document.getPropertyValue(ConstantesECM.CMCOR_NUMERO_REFERIDO);

            if (!StringUtils.isEmpty(nroReferido)) {
                String[] splitRadicado = nroReferido.split(ConstantesECM.SEPARADOR);
                documentoDTO.setNroRadicadoReferido(splitRadicado);
            }

            if (ecmType.endsWith("cmmig:CM_Migracion")) {
                documentoDTO.setOrigen(StateType.MIGRATED.getStateName());
            }
            return documentoDTO;
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    Folder getFolderBy(final String classType, final String propertyName,
                       final String value) throws SystemException {
        return structureAlfresco.getFolderBy(classType, propertyName, value);
    }

    byte[] transformHtmlToPdf(TemplateType templateType, String htmlContent) throws SystemException {
        return contentStamper.getStampedHtmlDocument(null, htmlContent.getBytes(), templateType);
    }

    UnidadDocumentalDTO getAndModificarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        final String id = unidadDocumentalDTO.getId();
        if (StringUtils.isEmpty(id)) {
            throw new SystemException("Especifique el id de la Unidad Documental");
        }
        final Optional<Folder> optionalFolder = getUDFolderById(unidadDocumentalDTO.getId());
        if (!optionalFolder.isPresent()) {
            throw new SystemException(ConstantesECM.NO_RESULT_MATCH);
        }
        final Folder folder = optionalFolder.get();
        final String ubTopografica = unidadDocumentalDTO.getUbicacionTopografica();
        if (!StringUtils.isEmpty(ubTopografica)) {
            unidadDocumentalDTO.setSoporte(SupportType.HYBRID.getSupport());
        }
        final CmisObject object = folder.updateProperties(getPropertyMapFrom(unidadDocumentalDTO));
        return transformarUnidadDocumental((Folder) object);
    }

    Optional<DocumentoDTO> subirDocumentoDtoTemp(Folder folder, DocumentoDTO documentoDTO) throws SystemException {

        if (ObjectUtils.isEmpty(documentoDTO)) {
            throw new SystemException("No se ha especificado el documento");
        }
        final String docName = StringUtils.isEmpty(documentoDTO.getNombreDocumento()) ? "" : documentoDTO.getNombreDocumento();
        if ("".equalsIgnoreCase(docName)) {
            throw new SystemException("No se ha especificado el nombre del documento");
        }
        if (ObjectUtils.isEmpty(documentoDTO.getDocumento())) {
            throw new SystemException("El documento no contiene informacion");
        }
        documentoDTO.setNombreDocumento(docName);

        try {
            byte[] bytes = documentoDTO.getDocumento();
            Map<String, Object> properties = new HashMap<>();
            properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
            properties.put(PropertyIds.NAME, documentoDTO.getNombreDocumento());
            properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, documentoDTO.getTipoDocumento());
            properties.put(ConstantesECM.CMCOR_DOC_ID_UD, folder.getPropertyValue(ConstantesECM.CMCOR_UD_ID));
            properties.put(ConstantesECM.CMCOR_DOC_AUTOR, documentoDTO.getDocAutor());
            ContentStream contentStream = new ContentStreamImpl(documentoDTO.getNombreDocumento(), BigInteger.valueOf(bytes.length), documentoDTO.getTipoDocumento(), new ByteArrayInputStream(bytes));
            Document document = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
            documentoDTO = transformarDocumento(document, true);
            documentoDTO.setNroRadicado(documentoDTO.getIdDocumento());
            return Optional.of(documentoDTO);
        } catch (CmisContentAlreadyExistsException ex) {
            log.error("Ya existe documento con nombre " + docName + " en el ECM");
            throw new SystemException("Ya existe documento con nombre " + docName + " en el ECM");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new SystemException(ex.getMessage());
        }
    }

    final Optional<Folder> crearUnidadDocumentalFolder(UnidadDocumentalDTO unidadDocumentalDTO, boolean esTemporal) throws SystemException {
        final Optional<Folder> optionalFolder = getUDFolderById(unidadDocumentalDTO.getId());
        if (optionalFolder.isPresent()) {
            log.error("Ya existe una unidad documental con el id {}", unidadDocumentalDTO.getId());
            throw new SystemException("Ya existe la unidad documental con id " + unidadDocumentalDTO.getId());
        }

        final String dependenciaCode = unidadDocumentalDTO.getCodigoDependencia();

        if (ObjectUtils.isEmpty(dependenciaCode)) {
            log.error("La Unidad Documental {} no contiene el codigo de la Dependencia", unidadDocumentalDTO);
            throw new SystemException("La Unidad Documental no contiene el codigo de la Dependencia");
        }

        final String nombreUnidadDocumental = unidadDocumentalDTO.getNombreUnidadDocumental();

        if (ObjectUtils.isEmpty(nombreUnidadDocumental)) {
            log.error("La Unidad Documental no contiene Nombre");
            throw new SystemException("La Unidad Documental no contiene Nombre");
        }
        final Folder folderDep;
        unidadDocumentalDTO.setGrupoSeguridad(SecurityGroupType.PUBLIC.getSecurityName());
        if (!esTemporal) {
            final String sCode = unidadDocumentalDTO.getCodigoSerie();

            if (StringUtils.isEmpty(sCode)) {
                log.error("No se ha especificado el Codigo de Serie");
                throw new SystemException("No se ha especificado el Codigo de Serie");
            }
            final String ssCode = unidadDocumentalDTO.getCodigoSubSerie();

            folderDep = getUnidadFolderFather(dependenciaCode, sCode, ssCode);

            if (null == folderDep) {
                log.error(ConstantesECM.NO_RESULT_MATCH);
                throw new SystemException(ConstantesECM.NO_RESULT_MATCH);
            }

            final String ubTopografica = unidadDocumentalDTO.getUbicacionTopografica();
            final String soporte = StringUtils.isEmpty(ubTopografica) ? SupportType.ELECTRONIC.getSupport() : SupportType.HYBRID.getSupport();
            final boolean inactivo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getInactivo()) ?
                    unidadDocumentalDTO.getInactivo() : Boolean.FALSE;
            final boolean cerrada = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCerrada()) ?
                    unidadDocumentalDTO.getCerrada() : Boolean.FALSE;
            unidadDocumentalDTO.setSoporte(soporte);
            unidadDocumentalDTO.setInactivo(inactivo);
            unidadDocumentalDTO.setCerrada(cerrada);
            unidadDocumentalDTO.setAccion(AccionUsuario.ABRIR.getState());
            unidadDocumentalDTO.setCodigoDependencia(folderDep.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));
            final String security = folderDep.getPropertyValue(ConstantesECM.CMCOR_SER_SECURITY_GROUP);
            final SecurityGroupType groupType = SecurityGroupType.getSecurityBy(security);
            unidadDocumentalDTO.setGrupoSeguridad(null != groupType ? groupType.getSecurityName() : null);
        } else {
            folderDep = getFolderBy(ConstantesECM.CLASE_DEPENDENCIA, ConstantesECM.CMCOR_DEP_CODIGO, dependenciaCode);
        }
        try {
            final Map<String, Object> props = getPropertyMapFrom(unidadDocumentalDTO);
            if (!props.containsKey(ConstantesECM.CMCOR_UD_ID)) {
                final String nameFolder = (String) props.get(PropertyIds.NAME);
                if (CommunicationType.PA.getCommunicationName().startsWith(nameFolder)) {
                    props.put(ConstantesECM.CMCOR_UD_ID, CommunicationType.PA.generateId(dependenciaCode));
                }
            }
            props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL));
            log.info("Making the tmpFolder!!!");
            final Folder folder = folderDep.createFolder(props);
            log.info("Make success");
            return Optional.of(folder);
        } catch (CmisContentAlreadyExistsException ex) {
            if (esTemporal) {
                final ItemIterable<CmisObject> children = folderDep.getChildren();
                for (CmisObject next : children) {
                    if (StringUtils.equalsIgnoreCase(next.getName(), nombreUnidadDocumental)) {
                        return Optional.of((Folder) next);
                    }
                }
            }
            log.error("Ya existe una Carpeta con el nombre {}", nombreUnidadDocumental);
            log.error("Cmis Error: {}", ex.getErrorContent());
            throw new SystemException("Ya existe la unidad documental de nombre " + nombreUnidadDocumental + " en el ECM");
        } catch (Exception ex) {
            log.error("An error has occurred '{}'", ex.getMessage());
            throw new SystemException("An error has occurred: " + ex.getMessage());
        }
    }

    private Folder getUnidadFolderFather(String depCode, String sCode, String ssCode) throws SystemException {
        final Session session = getSession();
        final String ecmTypeReplace = "{ECM_TYPE}";
        final String ssAppendReplace = "{SS_APPEND}";
        final String queryPattern = "SELECT " + PropertyIds.OBJECT_ID + " FROM " + ecmTypeReplace +
                " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + depCode + "'" +
                " AND " + ConstantesECM.CMCOR_SER_CODIGO + " = '" + sCode + "'" +
                " AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + ecmTypeReplace + "'" + ssAppendReplace;
        final String ecmType = StringUtils.isEmpty(ssCode) ? "cmcor:CM_Serie" : "cmcor:CM_Subserie";
        final String ssAppend = StringUtils.isEmpty(ssCode) ? "" : " AND " + ConstantesECM.CMCOR_SS_CODIGO + " = '" + ssCode + "'";
        final String queryString = queryPattern.replace(ecmTypeReplace, ecmType).replace(ssAppendReplace, ssAppend);

        final Iterator<QueryResult> iterator = session.query(queryString, false).iterator();
        if (iterator.hasNext()) {
            final String objectId = iterator.next().getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            return (Folder) session.getObject(objectId);
        }
        return null;
    }

    List<SedeDTO> getSedeList() throws SystemException {
        try {
            final Folder folder = getFolderBy(ConstantesECM.CLASE_BASE, ConstantesECM.CMCOR_UB_CODIGO, "000");
            final List<SedeDTO> sedeDTOS = new ArrayList<>();
            final ItemIterable<CmisObject> children = folder.getChildren();
            children.forEach(objectChild -> {
                final String dependencyCode = objectChild.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);
                SedeDTO baseDTO = new SedeDTO();
                baseDTO.setNombreBase(objectChild.getName());
                baseDTO.setCodigoBase(dependencyCode);
                baseDTO.setCodigoSede(dependencyCode);
                baseDTO.setNombreSede(objectChild.getName());
                sedeDTOS.add(sedeDTOS.size(), baseDTO);
            });
            return sedeDTOS;
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    String[] getSerieSubSerie(Folder udFolder) {

        Folder parent = udFolder.getFolderParent();
        String serieName = "";
        String subSerieName = "";
        if (parent.getType().getId().startsWith("F:cmcor:CM_Subserie")) {
            subSerieName = parent.getName();
            serieName = parent.getFolderParent().getName();

        }
        if (parent.getType().getId().startsWith("F:cmcor:CM_Serie")) {
            serieName = parent.getName();
        }
        int index;
        subSerieName = (index = subSerieName.indexOf('_')) != -1 ? subSerieName.substring(index + 1) : subSerieName;
        serieName = (index = serieName.indexOf('_')) != -1 ? serieName.substring(index + 1) : serieName;
        return new String[]{serieName, subSerieName};
    }

    /*public List<DocumentoDTO> getDocumentosProducidos(Session session, String dependencyCode) throws SystemException {
        if (StringUtils.isEmpty(dependencyCode)) {
            throw new SystemException("Especifique el codigo de la dependencia");
        }
        final String query = "SELECT * FROM " + ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL) +
                " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + dependencyCode + "'" +
                " AND " + PropertyIds.NAME + " LIKE '" + ConstantesECM.PRODUCCION_DOCUMENTAL + "%'";
        try {
            final ItemIterable<QueryResult> queryResults = session.query(query, false);
            final List<DocumentoDTO> dtos = new ArrayList<>();
            for (final QueryResult queryResult : queryResults) {
                final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                final Folder udFolder = (Folder) session.getObject(session.createObjectId(objectId));

                final ItemIterable<CmisObject> children = udFolder.getChildren();
                for (final CmisObject cmisObject : children) {
                    if (cmisObject instanceof Document) {
                        final Document document = (Document) cmisObject;
                        final String documentTypeString = document.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO);
                        final DocumentType documentType = DocumentType.getDocumentTypeBy(documentTypeString);
                        if (documentType == DocumentType.MAIN) {
                            dtos.add(dtos.size(), transformarDocumento(document));
                        }
                    }
                }
            }
            return dtos;
        } catch (Exception ex) {
            log.error("An error has occurred {}", ex.getMessage());
            throw new SystemException("An error has occurred " + ex.getMessage());
        }

    }*/

    List<DocumentoDTO> getDocumentosPorArchivarList(String dependencyCode) throws SystemException {

        final List<Folder> udTempFolderList = getUdRadication();
        fillUdTempFolderList(udTempFolderList, getFolderBy(ConstantesECM.CLASE_DEPENDENCIA, ConstantesECM.CMCOR_DEP_CODIGO, dependencyCode));
        try {
            final List<DocumentoDTO> dtos = new ArrayList<>();
            for (Folder folder : udTempFolderList) {
                for (final CmisObject cmisObject : folder.getChildren()) {
                    if (cmisObject instanceof Document) {
                        final Document document = (Document) cmisObject;
                        final String documentTypeString = document.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO);
                        final DocumentType documentType = DocumentType.getDocumentTypeBy(documentTypeString);
                        if (cmisObject.getType().getId().startsWith("D:cm")
                                && documentType == DocumentType.MAIN) {
                            final DocumentoDTO documentoDTO = transformarDocumento(document, true);
                            final String nroRadicado = documentoDTO.getNroRadicado();
                            if (!StringUtils.isEmpty(nroRadicado) && !"null".equalsIgnoreCase(nroRadicado.trim())) {
                                documentoDTO.setCodigoDependencia(folder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));
                            }
                            dtos.add(dtos.size(), documentoDTO);
                        }
                    }
                }
            }
            return dtos;
        } catch (Exception ex) {
            log.error("An error has occurred {}", ex.getMessage());
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }

    private void fillUdTempFolderList(List<Folder> udComOficFolderList, Folder folder) {
        final String folderName = folder.getName();
        if (folder.getType().getId().endsWith("CM_Unidad_Documental")
                && StringUtils.startsWithIgnoreCase(folderName, CommunicationType.PA.getCommunicationName())) {
            udComOficFolderList.add(udComOficFolderList.size(), folder);
        }
        for (CmisObject cmisObject : folder.getChildren()) {
            if (cmisObject instanceof Folder) {
                fillUdTempFolderList(udComOficFolderList, (Folder) cmisObject);
            }
        }
    }

    private List<Folder> getUdRadication() throws SystemException {
        final List<Folder> udRadicationList = new ArrayList<>();
        final List<Folder> ssRadicationList = getSsRadicationFolders();
        ssRadicationList.forEach(folder -> {
            if (folder.getType().getId().endsWith("CM_Unidad_Documental")) {
                udRadicationList.add(udRadicationList.size(), folder);
            }
        });
        return udRadicationList;
    }

    private List<Folder> getSsRadicationFolders() throws SystemException {
        final Folder serieRadicadora = getSerieRadicadora();
        final List<Folder> ssRadicationFolders = new ArrayList<>();
        for (CmisObject cmisObject : serieRadicadora.getChildren()) {
            if (cmisObject instanceof Folder) {
                final String ecmType = cmisObject.getType().getId();
                final Folder folder = (Folder) cmisObject;
                boolean esSerieRadicadora = cmisObject.getPropertyValue(ConstantesECM.CMCOR_DEP_RADICADORA);
                if (ecmType.endsWith("CM_Subserie") && esSerieRadicadora) {
                    ssRadicationFolders.add(ssRadicationFolders.size(), folder);
                }
            }
        }
        if (ssRadicationFolders.isEmpty()) {
            throw new SystemException("No existe Sub Serie radicadoras en la dependencia de radicacion");
        }
        return ssRadicationFolders;
    }

    private Folder getDepRadicationFolder() throws SystemException {

        final String ecmClass = ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_DEPENDENCIA);
        final String query = "SELECT " + PropertyIds.OBJECT_ID + " FROM " + ecmClass +
                " WHERE " + ConstantesECM.CMCOR_DEP_RADICADORA + " = true" +
                " AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + ecmClass + "'";

        final Session session = getSession();
        final ItemIterable<QueryResult> queryResults = session.createQueryStatement(query).query();
        final Iterator<QueryResult> resultIterator = queryResults.iterator();
        if (resultIterator.hasNext()) {
            final QueryResult queryResult = resultIterator.next();
            final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            return (Folder) session.getObject(objectId);
        }
        throw new SystemException("La estructura no contiene dependencia de radicacion");
    }

    /**
     * Metodo para buscar crear carpetas
     *
     * @param documentoDTO       Objeto qeu contiene los metadatos
     * @param response           Mensaje de respuesta
     * @param bytes              Contenido del documento
     * @param properties         propiedades de carpeta
     * @param carpetaCrearBuscar Carpeta
     */
    void buscarCrearCarpeta(DocumentoDTO documentoDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, String carpetaCrearBuscar) {
        log.info("MetaDatos: {}", documentoDTO.toString());
        String idDocumento;
        List<DocumentoDTO> documentoDTOList = new ArrayList<>();
        try {
            //Se obtiene la carpeta dentro del ECM al que va a ser subido el documento
            log.info("### Se elige la carpeta donde se va a guardar el documento principal..");
            log.info("###------------ Se elige la sede donde se va a guardar el documento principal..");

            if (ObjectUtils.isEmpty(documentoDTO.getCodigoDependencia())) {
                throw new BusinessException("No se ha identificado el codigo de la Dependencia");
            }
            Folder folderBy = getFolderBy(ConstantesECM.CLASE_DEPENDENCIA, ConstantesECM.CMCOR_DEP_CODIGO,
                    documentoDTO.getCodigoDependencia());
            final Carpeta folderAlfresco = new Carpeta();
            folderAlfresco.setFolder(folderBy);
            log.info("Se busca si existe la carpeta de Produccion documental para el año en curso dentro de la dependencia " + documentoDTO.getDependencia());
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            List<Carpeta> carpetasDeLaDependencia = obtenerCarpetasHijasDadoPadre(folderAlfresco);

            Carpeta carpetaTarget;

            Optional<Carpeta> produccionDocumental = carpetasDeLaDependencia.stream()
                    .filter(p -> p.getFolder().getName().equals(carpetaCrearBuscar + year)).findFirst();
            carpetaTarget = getCarpeta(carpetaCrearBuscar, Optional.of(folderAlfresco), year, produccionDocumental);

            idDocumento = crearDocumentoDevolverId(documentoDTO, response, bytes, properties, documentoDTOList, carpetaTarget);
            //Creando el mensaje de respuesta
            response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            response.setMensaje("Documento añadido correctamente");
            log.info(ConstantesECM.AVISO_CREA_DOC_ID + idDocumento);

        } catch (CmisContentAlreadyExistsException ccaee) {
            log.error(ConstantesECM.ECM_ERROR_DUPLICADO, ccaee);
            response.setCodMensaje("1111");
            response.setMensaje(ConstantesECM.ECM_ERROR_DUPLICADO);
        } catch (CmisConstraintException cce) {
            log.error(ConstantesECM.ECM_ERROR, cce);
            response.setCodMensaje("2222");
            response.setMensaje("El nombre del documento('" + documentoDTO.getNombreDocumento() + "') no supera la convencion proporcionada por el ECM");
        } catch (Exception e) {
            log.error(ConstantesECM.ERROR_TIPO_EXCEPTION, e);
            response.setCodMensaje("2222");
            response.setMensaje(e.getMessage());
        }
    }

    /**
     * Metodo para buscar crear carpetas de radicacion de entrada
     *
     * @param session    Objeto session
     * @param documento  Objeto qeu contiene los metadatos
     * @param response   Mensaje de respuesta
     * @param properties propiedades de carpeta
     *//*
    void buscarCrearCarpetaRadicacion(Session session, DocumentoDTO documento, MensajeRespuesta response, Map<String, Object> properties, String tipoComunicacion) {

        try {
            //Se obtiene la carpeta dentro del ECM al que va a ser subido el documento
            new Carpeta();
            Carpeta folderAlfresco;
            log.info("### Se elige la carpeta donde se va a guardar el documento radicado..");
            log.info("###------------ Se elige la sede donde se va a guardar el documento radicado..");
            folderAlfresco = obtenerCarpetaPorNombre(documento.getSede(), session);

            if (folderAlfresco.getFolder() != null) {
                log.info("###------------------- Se obtienen todas las dependencias de la sede..");
                List<Carpeta> carpetasHijas = obtenerCarpetasHijasDadoPadre(folderAlfresco);

                String comunicacionOficial = "";
                String tipoComunicacionSelector;
                tipoComunicacionSelector = getTipoComunicacionSelector(tipoComunicacion);

                CommunicationType selectorType = CommunicationType.getSelectorBy(tipoComunicacion);

                if (selectorType != null) {
                    final Folder ssRadicationFolder = getSsRadicationFolder(selectorType, session);

                }


                *//*if ("EI".equals(tipoComunicacion) || "SI".equals(tipoComunicacion)) {
                    comunicacionOficial = ConstantesECM.TIPO_COMUNICACION_INTERNA;
                } else if ("EE".equals(tipoComunicacion) || "SE".equals(tipoComunicacion)) {
                    comunicacionOficial = ConstantesECM.TIPO_COMUNICACION_EXTERNA;
                }*//*

                //Se busca si existe la dependencia
                Optional<Carpeta> dependencia = carpetasHijas.stream()
                        .filter(p -> p.getFolder().getName().equals(documento.getDependencia())).findFirst();

                log.info("Se obtienen la dependencia referente a la sede: " + documento.getSede());
                if (dependencia.isPresent()) {

                    log.info("Se busca si existe la carpeta de Comunicaciones Oficiales dentro de la dependencia " + documento.getDependencia());

                    List<Carpeta> carpetasDeLaDependencia = obtenerCarpetasHijasDadoPadre(dependencia.get());

                    //Obtengo la carpeta de comunicaciones oficiales si existe
                    Optional<Carpeta> comunicacionOficialFolder = carpetasDeLaDependencia.stream()
                            .filter(p -> p.getFolder().getName().contains("0231_COMUNICACIONES OFICIALES")).findFirst();

                    crearInsertarCarpetaRadicacion(documento, response, documento.getDocumento(), properties, comunicacionOficial, tipoComunicacionSelector, comunicacionOficialFolder);
                } else {
                    response.setMensaje(ConstantesECM.NO_EXISTE_DEPENDENCIA + documento.getDependencia());
                    response.setCodMensaje("4445");
                    log.info(ConstantesECM.NO_EXISTE_DEPENDENCIA + documento.getDependencia());
                }
            } else {
                response.setMensaje(ConstantesECM.NO_EXISTE_SEDE + documento.getSede());
                response.setCodMensaje("4444");
                log.info(ConstantesECM.NO_EXISTE_SEDE + documento.getSede());
            }
        } catch (CmisContentAlreadyExistsException ccaee) {
            log.error(ConstantesECM.ECM_ERROR_DUPLICADO, ccaee);
            response.setCodMensaje("1111");
            response.setMensaje("El documento ya existe en el ECM");
        } catch (CmisConstraintException cce) {
            log.error(ConstantesECM.ECM_ERROR, cce);
            response.setCodMensaje("2222");
            response.setMensaje(configuracion.getPropiedad(ConstantesECM.ECM_ERROR));
        } catch (Exception e) {
            log.error(ConstantesECM.ERROR_TIPO_EXCEPTION, e);
            response.setCodMensaje("2222");
            response.setMensaje(configuracion.getPropiedad(ConstantesECM.ECM_ERROR));
        }
    }*/

    /**
     * Convierte la Interfaz Folder de opencemis a una UnidadDocumentalDTO
     *
     * @param folder Objeto a transformar
     * @return UnidadDocumentalDTO
     */
    @Override
    public UnidadDocumentalDTO transformarUnidadDocumental(Folder folder) {

        final String[] serieSubSerie = getSerieSubSerie(folder);
        final UnidadDocumentalDTO unidadDocumentalDTO = new UnidadDocumentalDTO();
        unidadDocumentalDTO.setEcmObjId(folder.getId());
        unidadDocumentalDTO.setId(folder.getPropertyValue(ConstantesECM.CMCOR_UD_ID));
        unidadDocumentalDTO.setAccion(folder.getPropertyValue(ConstantesECM.CMCOR_UD_ACCION));
        unidadDocumentalDTO.setDescriptor2(folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_2));
        unidadDocumentalDTO.setCerrada(folder.getPropertyValue(ConstantesECM.CMCOR_UD_CERRADA));
        unidadDocumentalDTO.setFechaCierre(folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_CIERRE));
        unidadDocumentalDTO.setFechaExtremaInicial(folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_INICIAL));
        unidadDocumentalDTO.setFechaExtremaFinal(folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_FINAL));
        unidadDocumentalDTO.setSoporte(folder.getPropertyValue(ConstantesECM.CMCOR_UD_SOPORTE));
        unidadDocumentalDTO.setInactivo(folder.getPropertyValue(ConstantesECM.CMCOR_UD_INACTIVO));
        unidadDocumentalDTO.setUbicacionTopografica(folder.getPropertyValue(ConstantesECM.CMCOR_UD_UBICACION_TOPOGRAFICA));
        unidadDocumentalDTO.setFaseArchivo(folder.getPropertyValue(ConstantesECM.CMCOR_UD_FASE_ARCHIVO));
        unidadDocumentalDTO.setDescriptor1(folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_1));
        unidadDocumentalDTO.setCodigoSerie(folder.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO));
        unidadDocumentalDTO.setCodigoSubSerie(folder.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO));
        unidadDocumentalDTO.setCodigoDependencia(folder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));
        unidadDocumentalDTO.setNombreUnidadDocumental(folder.getPropertyValue(PropertyIds.NAME));
        unidadDocumentalDTO.setObservaciones(folder.getPropertyValue(ConstantesECM.CMCOR_UD_OBSERVACIONES));
        unidadDocumentalDTO.setEstado(folder.getPropertyValue(ConstantesECM.CMCOR_UD_ESTADO));
        unidadDocumentalDTO.setDisposicion(folder.getPropertyValue(ConstantesECM.CMCOR_UD_DISPOSICION));
        unidadDocumentalDTO.setGrupoSeguridad(folder.getPropertyValue(ConstantesECM.CMCOR_SER_SECURITY_GROUP));
        unidadDocumentalDTO.setFechaCreacion(folder.getPropertyValue(PropertyIds.CREATION_DATE));
        unidadDocumentalDTO.setAutor(folder.getPropertyValue(ConstantesECM.CMCOR_UD_AUTOR));
        unidadDocumentalDTO.setNombreSerie(serieSubSerie[0]);
        unidadDocumentalDTO.setNombreSubSerie(serieSubSerie[1]);
        return unidadDocumentalDTO;
    }

    /**
     * Metodo para crear el link
     *
     * @param document Obj Document Ecm
     * @param target   Obj Folder Ecm
     */
    void crearLink(Folder target, Document document) {
        log.info("Se entra al metodo crearLink");

        Map<String, Object> properties = new HashMap<>();
        properties.put(PropertyIds.BASE_TYPE_ID, BaseTypeId.CMIS_ITEM.value());
        String id = document.getId();
        // define a name and a description for the link
        properties.put(PropertyIds.NAME, document.getName());
        properties.put(PropertyIds.DESCRIPTION, id);
        properties.put(PropertyIds.OBJECT_TYPE_ID, "I:app:filelink");

        //define the destination node reference
        properties.put("cm:destination", "workspace://SpacesStore/" + id);
        //Se crea el link

        target.createItem(properties);

        if (document instanceof AlfrescoDocument) {
            AlfrescoDocument alfrescoDocument = (AlfrescoDocument) document;
            alfrescoDocument.addAspect(ConstantesECM.P_APP_LINKED);
            alfrescoDocument.refresh();
        }
        log.info("Se crea el link y se sale del método crearLink");
    }

    @Override
    public Map<String, Object> getPropertyMapFrom(UnidadDocumentalDTO unidadDocumentalDTO) {
        log.info("Creating props to make the folder");
        final Map<String, Object> props = new HashMap<>();
        props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL));
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getId())) {
            props.put(ConstantesECM.CMCOR_UD_ID, unidadDocumentalDTO.getId().toUpperCase().trim());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getNombreUnidadDocumental())) {
            props.put(PropertyIds.NAME, Utilities.reemplazarCaracteresRaros(unidadDocumentalDTO.getNombreUnidadDocumental()));
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getAccion())) {
            props.put(ConstantesECM.CMCOR_UD_ACCION, unidadDocumentalDTO.getAccion());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getUbicacionTopografica())) {
            props.put(ConstantesECM.CMCOR_UD_UBICACION_TOPOGRAFICA, unidadDocumentalDTO.getUbicacionTopografica());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getEstado())) {
            props.put(ConstantesECM.CMCOR_UD_ESTADO, unidadDocumentalDTO.getEstado());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getFaseArchivo())) {
            props.put(ConstantesECM.CMCOR_UD_FASE_ARCHIVO, unidadDocumentalDTO.getFaseArchivo());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getSoporte())) {
            props.put(ConstantesECM.CMCOR_UD_SOPORTE, unidadDocumentalDTO.getSoporte());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getDescriptor1())) {
            props.put(ConstantesECM.CMCOR_UD_DESCRIPTOR_1, unidadDocumentalDTO.getDescriptor1());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getDescriptor2())) {
            props.put(ConstantesECM.CMCOR_UD_DESCRIPTOR_2, unidadDocumentalDTO.getDescriptor2());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getObservaciones())) {
            props.put(ConstantesECM.CMCOR_UD_OBSERVACIONES, unidadDocumentalDTO.getObservaciones());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getDisposicion())) {
            props.put(ConstantesECM.CMCOR_UD_DISPOSICION, unidadDocumentalDTO.getDisposicion());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getAutor())) {
            props.put(ConstantesECM.CMCOR_UD_AUTOR, unidadDocumentalDTO.getAutor());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getCodigoSerie())) {
            props.put(ConstantesECM.CMCOR_SER_CODIGO, unidadDocumentalDTO.getCodigoSerie());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getCodigoSubSerie())) {
            props.put(ConstantesECM.CMCOR_SS_CODIGO, unidadDocumentalDTO.getCodigoSubSerie());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getCodigoDependencia())) {
            props.put(ConstantesECM.CMCOR_DEP_CODIGO, unidadDocumentalDTO.getCodigoDependencia());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getCodigoSede())) {
            props.put(ConstantesECM.CMCOR_DEP_CODIGO_PADRE, unidadDocumentalDTO.getCodigoSede());
        }
        if (!StringUtils.isEmpty(unidadDocumentalDTO.getGrupoSeguridad())) {
            props.put(ConstantesECM.CMCOR_SER_SECURITY_GROUP, unidadDocumentalDTO.getGrupoSeguridad());
        }
        if (!ObjectUtils.isEmpty(unidadDocumentalDTO.getFechaExtremaInicial())) {
            props.put(ConstantesECM.CMCOR_UD_FECHA_INICIAL, unidadDocumentalDTO.getFechaExtremaInicial());
        }
        if (!ObjectUtils.isEmpty(unidadDocumentalDTO.getFechaExtremaFinal())) {
            props.put(ConstantesECM.CMCOR_UD_FECHA_FINAL, unidadDocumentalDTO.getFechaExtremaFinal());
        }
        if (!ObjectUtils.isEmpty(unidadDocumentalDTO.getFechaCierre())) {
            props.put(ConstantesECM.CMCOR_UD_FECHA_CIERRE, unidadDocumentalDTO.getFechaCierre());
        }
        if (!ObjectUtils.isEmpty(unidadDocumentalDTO.getCerrada())) {
            props.put(ConstantesECM.CMCOR_UD_CERRADA, unidadDocumentalDTO.getCerrada());
        }
        if (!ObjectUtils.isEmpty(unidadDocumentalDTO.getInactivo())) {
            props.put(ConstantesECM.CMCOR_UD_INACTIVO, unidadDocumentalDTO.getInactivo());
        }
        return props;
    }

    @Override
    public MensajeRespuesta obtenerDocumentosUnidadDocumental(String ecmFolderUdId) throws SystemException {
        if (StringUtils.isEmpty(ecmFolderUdId)) {
            throw new SystemException("No se ha especificado el id Folder Ecm");
        }
        try {
            final Session session = getSession();
            final CmisObject object = session.getObject(ecmFolderUdId);
            if (!(object instanceof Folder)) {
                throw new SystemException("El ID especificado no corresponde al de un Ecm Folder ");
            }
            final List<Document> documents = new ArrayList<>();
            final Folder folder = (Folder) object;
            for (CmisObject cmisObject : folder.getChildren()) {
                final String ecmType = cmisObject.getType().getId();
                if (ecmType.startsWith("D:cm")) {
                    final String docTypeEcm = cmisObject.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO);
                    final DocumentType documentType = DocumentType.getDocumentTypeBy(docTypeEcm);
                    if (documentType == DocumentType.MAIN) {
                        documents.add(documents.size(), (Document) cmisObject);
                    }
                }
            }

            final String[] serieSubSerie = getSerieSubSerie(folder);

            final List<DocumentoDTO> dtos = documents
                    .parallelStream()
                    .map(document -> {
                        try {
                            final DocumentoDTO dto = transformarDocumento(document, false);
                            dto.setSerie(serieSubSerie[0]);
                            dto.setSubSerie(serieSubSerie[1]);
                            dto.setNombreUnidadDocumental(folder.getName());
                            return dto;
                        } catch (SystemException e) {
                            log.error("Error: {}", e);
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            final Map<String, Object> response = new HashMap<>();
            response.put("documentos", dtos);
            final MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setResponse(response);
            respuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            respuesta.setMensaje(ConstantesECM.SUCCESS);
            return respuesta;

        } catch (Exception ex) {
            throw new SystemException("Error: " + ex.getMessage());
        }
    }

    /**
     * Metodo para actualizar el nombre de la carpeta
     *
     * @param carpeta Carpeta a la cual se le va a actualizar el nombre
     * @param nombre  Nuevo nombre de la carpeta
     */
    void actualizarNombreCarpeta(Carpeta carpeta, String nombre) {
        log.info("### Actualizando nombre folder: " + nombre);
        try {
            carpeta.getFolder().rename(nombre);
        } catch (Exception e) {
            log.error("*** Error al actualizar nombre folder *** ", e);
        }
    }

    /**
     * Metodo que devuelve la carpeta padre de la carpeta que se le pase.
     *
     * @param folderFather Carpeta padre
     * @param codFolder    Codigo de la carpeta que se le va a chequear la carpeta padre
     * @return Carpeta padre
     */
    Carpeta chequearCapetaPadre(Carpeta folderFather, String codFolder) throws SystemException {
        Carpeta folderReturn = null;
        List<Carpeta> listaCarpeta = obtenerCarpetasHijasDadoPadre(folderFather);

        Iterator<Carpeta> iterator;
        if (!ObjectUtils.isEmpty(listaCarpeta)) {
            iterator = listaCarpeta.iterator();
            while (iterator.hasNext()) {
                Carpeta aux = iterator.next();
                Carpeta carpeta = obtenerCarpetaPorNombre(aux.getFolder().getName());
                String description = carpeta.getFolder().getDescription();
                if (description.equals(configuracion.getPropiedad(ConstantesECM.CLASE_DEPENDENCIA))) {
                    folderReturn = getCarpeta(codFolder, aux, "metadatoCodDependencia", folderReturn);
                } else if (description.equals(configuracion.getPropiedad(ConstantesECM.CLASE_SERIE))) {
                    folderReturn = getCarpeta(codFolder, aux, "metadatoCodSerie", folderReturn);
                } else if (description.equals(configuracion.getPropiedad(ConstantesECM.CLASE_SUBSERIE))) {
                    log.info("Entro a clase subserie cargando los valores");
                    folderReturn = getCarpeta(codFolder, aux, "metadatoCodSubserie", folderReturn);
                }
            }
        }
        return folderReturn;
    }

    byte[] getDocumentBytes(ContentStream contentStream) throws SystemException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ContentStreamUtils.writeContentStreamToOutputStream(contentStream, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("An error has occurred {}", e.getMessage());
            throw new SystemException("Error: " + e.getMessage());
        }
    }

    ItemIterable<QueryResult> getPrincipalAdjuntosQueryResults(DocumentoDTO documento) throws SystemException {
        //Obtener el documentosAdjuntos
        String query = "SELECT * FROM cmcor:CM_DocumentoPersonalizado";
        boolean where = false;
        if (!StringUtils.isEmpty(documento.getIdDocumento())) {
            where = true;
            query += " WHERE " + PropertyIds.OBJECT_ID + " = '" + documento.getIdDocumento() + "'" +
                    " OR " + ConstantesECM.CMCOR_ID_DOC_PRINCIPAL + " = '" + documento.getIdDocumento() + "'";
        }
        if (!StringUtils.isEmpty(documento.getNroRadicado())) {
            query += (where ? " AND " : " WHERE ") + ConstantesECM.CMCOR_DOC_RADICADO + " = '" + documento.getNroRadicado().trim() + "'";
        }
        return getSession().query(query, false);
    }

    /**
     * Metodo que crea carpetas dentro de Alfresco
     *
     * @param folder          Objeto carpeta que contiene un Folder de Alfresco
     * @param nameOrg         Nombre de la carpeta
     * @param codOrg          Codigo de la carpeta que se va a crear
     * @param classDocumental Clase documental que tiene la carpeta que se va a crar.
     * @param folderFather    Carpeta dentro de la cual se va a crear la carpeta
     * @return Devuelve la carpeta creada dentro del objeto Carpeta
     */
    Carpeta crearCarpeta(Carpeta folder, String nameOrg, String codOrg,
                         String classDocumental, Carpeta folderFather, String idOrgOfc) {
        Carpeta newFolder = null;
        try {

            log.info("### Creando Carpeta.. con clase documental:" + classDocumental);
            Map<String, Object> props = new HashMap<>();
            //Se define como nombre de la carpeta nameOrg
            props.put(PropertyIds.NAME, nameOrg);

            //Se estable como codigo unidad administrativa Padre el codigo de la carpeta padre
            switch (classDocumental) {
                case ConstantesECM.CLASE_BASE:
                    llenarPropiedadesCarpeta(ConstantesECM.CLASE_BASE, props, codOrg);
                    break;
                case ConstantesECM.CLASE_DEPENDENCIA:
                    llenarPropiedadesCarpeta(ConstantesECM.CMCOR_DEP_CODIGO, ConstantesECM.CLASE_DEPENDENCIA, props, codOrg, folderFather, idOrgOfc);
                    break;
                case ConstantesECM.CLASE_SERIE:
                    llenarPropiedadesCarpeta(ConstantesECM.CMCOR_SER_CODIGO, ConstantesECM.CLASE_SERIE, props, codOrg, folderFather, idOrgOfc);
                    break;
                case ConstantesECM.CLASE_SUBSERIE:
                    llenarPropiedadesCarpeta(ConstantesECM.CMCOR_SS_CODIGO, ConstantesECM.CLASE_SUBSERIE, props, codOrg, folderFather, idOrgOfc);
                    break;
                case ConstantesECM.CLASE_UNIDAD_DOCUMENTAL:

                    final Folder tmpFolder = folderFather.getFolder();

                    final String depCodeValue = tmpFolder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);
                    String depCode = !ObjectUtils.isEmpty(depCodeValue) ? depCodeValue : "";

                    final String serieCodeValue = tmpFolder.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO);
                    String serieCode = !ObjectUtils.isEmpty(serieCodeValue) ? serieCodeValue : "";

                    final String subSerieCodeValue = tmpFolder.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO);
                    String subSerieCode = !ObjectUtils.isEmpty(subSerieCodeValue) ? subSerieCodeValue : "";

                    final String codUnidadAdminPadreValue = tmpFolder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO_PADRE);
                    String codUnidadAdminPadre = !ObjectUtils.isEmpty(codUnidadAdminPadreValue) ? codUnidadAdminPadreValue : "";

                    props.put(ConstantesECM.CMCOR_UD_ID, "");
                    props.put(ConstantesECM.CMCOR_UD_ACCION, "");
                    props.put(ConstantesECM.CMCOR_UD_DESCRIPTOR_2, "");
                    props.put(ConstantesECM.CMCOR_UD_CERRADA, false);
                    props.put(ConstantesECM.CMCOR_UD_SOPORTE, SupportType.ELECTRONIC.getSupport());
                    props.put(ConstantesECM.CMCOR_UD_INACTIVO, false);
                    props.put(ConstantesECM.CMCOR_UD_UBICACION_TOPOGRAFICA, "");
                    props.put(ConstantesECM.CMCOR_UD_FASE_ARCHIVO, "");
                    props.put(ConstantesECM.CMCOR_UD_DESCRIPTOR_1, "");
                    props.put(ConstantesECM.CMCOR_SER_SECURITY_GROUP, SecurityGroupType.PUBLIC.getSecurityName());
                    props.put(ConstantesECM.CMCOR_DEP_CODIGO_PADRE, !"".equals(subSerieCode) ? subSerieCode :
                            !"".equals(serieCode) ? serieCode :
                                    !"".equals(depCode) ? depCode : codUnidadAdminPadre);
                    props.put(ConstantesECM.CMCOR_DEP_CODIGO, depCode);
                    props.put(ConstantesECM.CMCOR_SER_CODIGO, serieCode);
                    props.put(ConstantesECM.CMCOR_SS_CODIGO, subSerieCode);
                    props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL));
                    props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL));
                    break;
                default:
                    break;
            }
            //Se crea la carpeta dentro de la carpeta folder
            log.info("*** Se procede a crear la carpeta ***");
            newFolder = new Carpeta();
            newFolder.setFolder(folder.getFolder().createFolder(props));
            log.info("---------------------Carpeta: " + newFolder.getFolder().getName() + " creada--------------");
        } catch (Exception e) {
            log.error("*** Error al crear carpeta *** ", e);
        }
        return newFolder;
    }

    /**
     * Metodo que obtiene la carpeta dado el nombre
     *
     * @param nombreCarpeta NOmbre de la carpeta que se va a buscar
     * @return Retorna la Carpeta que se busca
     */
    Carpeta obtenerCarpetaPorNombre(String nombreCarpeta) {
        Carpeta folder = new Carpeta();
        try {
            final Session session = getSession();
            String queryString = "SELECT " + PropertyIds.OBJECT_ID + " FROM cmis:folder" +
                    " WHERE " + PropertyIds.NAME + " = '" + nombreCarpeta + "'" +
                    " and (cmis:objectTypeId = 'F:cmcor:CM_Unidad_Base'" +
                    " or cmis:objectTypeId = 'F:cmcor:CM_Serie'" +
                    " or cmis:objectTypeId = 'F:cmcor:CM_Subserie'" +
                    " or cmis:objectTypeId = 'F:cmcor:CM_Unidad_Administrativa'" +
                    " or cmis:objectTypeId = 'F:cmcor:CM_Unidad_Documental')";
            ItemIterable<QueryResult> results = session.query(queryString, false);
            for (QueryResult qResult : results) {
                String objectId = qResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                folder.setFolder((Folder) session.getObject(session.createObjectId(objectId)));
            }
        } catch (Exception e) {
            log.error("*** Error al obtenerCarpetas *** ", e);
        }
        return folder;
    }

    /**
     * @param informationArray Arreglo que trae el nombre de la carpeta para formatearlo para ser usado por el ECM
     * @param formatoConfig    Contiene el formato que se le dara al nombre
     * @return Nombre formateado
     */
    String formatearNombre(String[] informationArray, String formatoConfig) {
        String formatoCadena;
        StringBuilder formatoFinal = new StringBuilder();
        try {
            formatoCadena = configuracion.getPropiedad(formatoConfig);
            String[] formatoCadenaArray = formatoCadena.split("");
            for (String aFormatoCadenaArray : formatoCadenaArray) {

                String nomSerie = "3";
                String idOrgAdm = "0";
                String idOrgOfc = "1";
                String codSerie = "2";
                String codSubserie = "4";
                String nomSubserie = "5";
                if (aFormatoCadenaArray.equals(idOrgAdm)) {
                    formatoFinal.append(informationArray[Integer.parseInt(idOrgAdm)]);
                } else if (aFormatoCadenaArray.equals(idOrgOfc)) {
                    formatoFinal.append(informationArray[Integer.parseInt(idOrgOfc)]);
                } else if (aFormatoCadenaArray.equals(codSerie)) {
                    formatoFinal.append(informationArray[Integer.parseInt(codSerie)]);
                } else if (aFormatoCadenaArray.equals(nomSerie)) {
                    formatoFinal.append(informationArray[Integer.parseInt(nomSerie)]);
                } else if (aFormatoCadenaArray.equals(codSubserie)) {
                    formatoFinal.append(informationArray[Integer.parseInt(codSubserie)]);
                } else if (aFormatoCadenaArray.equals(nomSubserie)) {
                    formatoFinal.append(informationArray[Integer.parseInt(nomSubserie)]);
                } else if (isNumeric(aFormatoCadenaArray)) {
                    //El formato no cumple con los requerimientos minimos
                    log.info("El formato no cumple con los requerimientos.");
                    formatoFinal = new StringBuilder();
                    break;
                } else {
                    formatoFinal.append(aFormatoCadenaArray);
                }
            }
        } catch (Exception e) {
            log.error("*** Error al formatear nombre *** ", e);
        }
        return !ObjectUtils.isEmpty(formatoFinal) ? formatoFinal.toString() : "";
    }

    void eliminarDocumentosAnexos(String idDoc) throws SystemException {
        log.info("Eliminar todos los documentos anexos a {}", idDoc);
        final String query = "select * from cmcor:CM_DocumentoPersonalizado" +
                " where " + ConstantesECM.CMCOR_ID_DOC_PRINCIPAL + " = '" + idDoc + "'";
        try {
            final ItemIterable<QueryResult> queryResults = getSession().query(query, false);
            for (QueryResult queryResult :
                    queryResults) {
                String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                eliminardocumento(objectId);
            }
        } catch (Exception e) {
            log.error("Ocurrio un error al eliminar documentos anexos");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    boolean transformaEstampaRad(DocumentoDTO docDto) throws SystemException {

        boolean radicado = true;

        log.info("************************-- Entro transformaEstampaRad ---**********************************");
        final String idDocumento = docDto.getIdDocumento();
        if (StringUtils.isEmpty(idDocumento)) {
            throw new SystemException("No se ha especificado el Id Ecm");
        }

        final String nroRadicado = docDto.getNroRadicado();
        final CommunicationType communicationType = CommunicationType.getSelectorBy(nroRadicado);

        if (communicationType == null ) {
            throw new SystemException("No se procesa el numero de radicado = '" + nroRadicado + "'");
        }
        byte[] imageBytes = null;
        if(!docDto.getReintentar().equals("true")){
            imageBytes = docDto.getDocumento();
            new Thread(() -> {
                try {
                    saveStamperImageFile(docDto, nroRadicado);
                } catch (SystemException e) {
                    e.printStackTrace();
                }
            }).start();

        }


        log.info("*******---------------------- Reintentar LOG ::::" +docDto.getReintentar());
        if (ObjectUtils.isEmpty(imageBytes) && !docDto.getReintentar().equals("true")) {
            throw new SystemException("No se especifico el contenido del archivo");
        }
        final Session session = getSession();
        try {
            final Document documentECM = (Document) session.getObject(idDocumento);
            final Folder folderFrom = getFolderFrom(documentECM);
            final Carpeta carpetaRadicacion = buscarCarpetaRadicacion(communicationType);
            final Folder folderDest = carpetaRadicacion.getFolder();
            final Map<String, Object> properties = new HashMap<>();

            boolean canMove = false;
            if (null != folderDest && null != folderFrom && !folderDest.getId().equals(folderFrom.getId())) {
                canMove = true;
                properties.put(ConstantesECM.CMCOR_DOC_ID_UD, folderDest.getPropertyValue(ConstantesECM.CMCOR_UD_ID));
            }

            final String docMimeType = documentECM.getPropertyValue(PropertyIds.CONTENT_STREAM_MIME_TYPE);


            final String docName = documentECM.getName()
                    .replace(".pdf", "")
                    .replace(".html", "")
                    .replace(".htm", "") + ".pdf";

            final TemplateType templateType = communicationType == CommunicationType.SI
                    ? TemplateType.MEMORANDO : TemplateType.OFICIO;
            final byte[] contentBytes = getDocumentBytes(documentECM.getContentStream());
            log.info("**********---******----********DOCUMENTO:" + documentECM.getContentStream().getFileName() );
            //se firma el documento
                /*final byte[] stampedDocument = digitalSignature.signPDF(contentStamper
                        .getStampedHtmlDocument(imageBytes, contentBytes, templateType));*/


            FirmaDigitalDTO firmaDTO = new FirmaDigitalDTO();
            if (docDto.getReintentar().equals("true")){
                firmaDTO.setPdfDocument(contentBytes);
            }else{
                if (MimeTypes.getMIMEType("html").equals(docMimeType)) {
                    //Conversion Pdf a Pdfa por: Erick Saavedra, Carlos Gutierrez
                    firmaDTO.setPdfDocument(contentStamper.convertPdfToPdfaDocument(contentStamper.getStampedHtmlDocument(imageBytes, contentBytes, templateType)) );
                }else {
                    //Conversion Pdf a Pdfa por: Erick Saavedra, Carlos Gutierrez
                    firmaDTO.setPdfDocument(contentStamper.convertPdfToPdfaDocument(contentStamper.getStampedDocument(imageBytes, contentBytes, "pdf")));
                }
            }

            log.info("********---- firma Politica :::: "+docDto.getPoliticaFirma());
            firmaDTO.setIdCliente(usuarioFirma);
            firmaDTO.setPasswordCliente(passFirma);
            firmaDTO.setIdPolitica(docDto.getPoliticaFirma());
            firmaDTO.setNoPagina("");
            firmaDTO.setNameDocument("");
            firmaDTO.setStringToFind("");
            firmaDTO.setPasswordCifrado("");
            //Firma de Documento


            byte[] stampedDocument = digitalSignature.signPDF(firmaDTO);

            if (stampedDocument == null){
                stampedDocument = firmaDTO.getPdfDocument();
                log.info("No se Logro firmar el documeto se crea version del documento sin Firma - ERROR SERVICIO FIRMA- ");
                radicado = false;

            }

            log.info("------------- :: Docuemento :: -------::::: " +stampedDocument);
            properties.put(PropertyIds.CONTENT_STREAM_FILE_NAME, docName);
            properties.put(ConstantesECM.CMCOR_DOC_RADICADO, nroRadicado);
            properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, MimeTypes.getMIMEType("pdf"));
            properties.put(ConstantesECM.CMCOR_TIPO_DOCUMENTO, DocumentType.MAIN.getType());
            properties.put(ConstantesECM.CMCOR_FECHA_RADICACION, getFechaRadicacionCalendar(nroRadicado));
            final String nombreProceso = String.valueOf(properties.get(ConstantesECM.CMCOR_NOMBRE_PROCESO));
            if (StringUtils.isEmpty(nombreProceso)) {
                properties.put(ConstantesECM.CMCOR_NOMBRE_PROCESO, communicationType.getProcessName());
            }

            documentECM.rename(docName, true);

            final Document newDocument = (Document) session.getObject(documentECM.checkOut());
            final ContentStream contentStream = new ContentStreamImpl(docName, BigInteger.valueOf(stampedDocument.length),
                    MimeTypes.getMIMEType("pdf"), new ByteArrayInputStream(stampedDocument));

            newDocument.checkIn(false, properties, contentStream, "Version PDF");
            docDto.setDocumento(stampedDocument);

            if (canMove) documentECM.move(folderFrom, folderDest);

            System.out.println("************************-- Funciona hasta aca ---**********************************");
            log.info("************************-- Funciona hasta aca ---**********************************");


            /*
            final String nroRadicado = docDto.getNroRadicado();
            final CommunicationType communicationType = CommunicationType.getSelectorBy(nroRadicado);

            if (communicationType == null) {
                throw new SystemException("No se procesa el numero de radicado = '" + nroRadicado + "'");
            }

            byte[] documentBytes = docDto.getDocumento();
            if (ObjectUtils.isEmpty(documentBytes)) {
                throw new SystemException("No se especifico el contenido del archivo");
            }
            String idDocument = docDto.getIdDocumento();
            if (StringUtils.isEmpty(idDocument)) {
                saveStamperImageFile(docDto, nroRadicado);
                return;
            }

            final Session session = getSession();
            final Document documentECM = (Document) session.getObject(idDocument);
            final String docMimeType = documentECM.getPropertyValue(PropertyIds.CONTENT_STREAM_MIME_TYPE);
            final Folder folder = getFolderFrom(documentECM);

            if (null == folder) {
                throw new SystemException("Ocurrio un error al estampar la etiqueta de radicacion");
            }

            final byte[] imageBytes;
            final boolean isHtmlDoc;

            String mimeType = MimeTypes.getMIMEType("pdf");
            if (MimeTypes.getMIMEType("html").equals(docMimeType)) {
                imageBytes = documentBytes;
                documentBytes = getDocumentBytes(documentECM.getContentStream());
                mimeType = MimeTypes.getMIMEType("html");
                saveStamperImageFile(docDto, nroRadicado);
                isHtmlDoc = true;
            } else {
                final Document documentImg = getStamperImage(nroRadicado);
                if (null == documentImg) {
                    throw new SystemException("No existe imagen con numero de radicado " + nroRadicado);
                }
                imageBytes = getDocumentBytes(documentImg.getContentStream());
                isHtmlDoc = false;
            }

            final byte[] stampedDocument = digitalSignature.signPDF(contentStamper
                    .getStampedDocument(imageBytes, documentBytes, mimeType));

            String docName = documentECM.getName()
                    .replace(".pdf", "")
                    .replace(".html", "")
                    .replace(".htm", "") + ".pdf";

            docDto.setNombreDocumento(docName);

            final Map<String, Object> properties = obtenerPropiedadesDocumento(documentECM);
            properties.put(ConstantesECM.CMCOR_DOC_RADICADO, docDto.getNroRadicado());
            properties.put(PropertyIds.NAME, docDto.getNombreDocumento());
            properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, MimeTypes.getMIMEType("pdf"));
            properties.put(ConstantesECM.CMCOR_TIPO_DOCUMENTO, DocumentType.MAIN.getType());
            properties.put(ConstantesECM.CMCOR_FECHA_RADICACION, GregorianCalendar.getInstance());
            final String nombreProceso = String.valueOf(properties.get(ConstantesECM.CMCOR_NOMBRE_PROCESO));
            if (StringUtils.isEmpty(nombreProceso)) {
                properties.put(ConstantesECM.CMCOR_NOMBRE_PROCESO, communicationType.getProcessName());
            }

            *//*if (isHtmlDoc) {
                properties.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, idDocument);
            }//

            documentECM.delete(false);

            final ContentStream contentStream = new ContentStreamImpl(docName,
                    BigInteger.valueOf(stampedDocument.length), MimeTypes.getMIMEType("pdf"),
                    new ByteArrayInputStream(stampedDocument));
            final Document document = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
            final String newIdDocument = document.getId();
            docDto.setIdDocumento(newIdDocument);

            *//*if (null != documentImg) {
                documentImg.delete();
            }//

            if (isHtmlDoc) {
                updateAnexos(idDocument, newIdDocument, session);
                docDto.setNroRadicado(nroRadicado);
                docDto.setNombreDocumento(document.getName());
                modificarMetadatosDocumento(docDto);
            }*/

            log.info("::::::::: Se radico el documento ::::::::::::: "+radicado);

            return radicado;
        } catch (CmisBaseException e) {
            log.error("checkin failed, trying to cancel the checkout '{}'", e);
            return false;
        } catch (Exception e) {
            log.error("Error al estampar la etiqueta de radicacion '{}'", e);
            return false;
        }
    }

    Folder getFolderFrom(Document document) {
        return getUDFolderById(document.getPropertyValue(ConstantesECM.CMCOR_DOC_ID_UD))
                .orElse(null);
    }

    DocumentoDTO subirDocumentoPrincipalPD(DocumentoDTO documentoDTO) throws SystemException {
        if (ObjectUtils.isEmpty(documentoDTO.getCodigoDependencia())) {
            throw new SystemException("No se ha identificado el codigo de la Dependencia");
        }
        final String nombreDoc = documentoDTO.getNombreDocumento();
        if (StringUtils.isEmpty(nombreDoc)) {
            throw new SystemException("No se ha especificado el nombre del documento");
        }
        final byte[] bytes = documentoDTO.getDocumento();
        if (ObjectUtils.isEmpty(bytes)) {
            throw new SystemException("No se ha especificado el contenido del documento");
        }
        Folder folderBy = getFolderBy(ConstantesECM.CLASE_DEPENDENCIA, ConstantesECM.CMCOR_DEP_CODIGO,
                documentoDTO.getCodigoDependencia());
        Carpeta carpeta = new Carpeta();
        carpeta.setFolder(folderBy);
        final CommunicationType communicationType = CommunicationType.PD;
        final Optional<Folder> optionalFolder = getUdContainsName(carpeta.getFolder(), communicationType);
        if (!optionalFolder.isPresent()) {
            carpeta = crearCarpeta(carpeta, communicationType.getFolderName(), documentoDTO.getCodigoDependencia(), ConstantesECM.CLASE_UNIDAD_DOCUMENTAL, carpeta, null);
            final Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put(ConstantesECM.CMCOR_UD_ID, communicationType.generateId(documentoDTO.getCodigoDependencia()));
            carpeta.getFolder().updateProperties(propertyMap, true);
        } else {
            carpeta.setFolder(optionalFolder.get());
        }
        Document document = createDocument(carpeta, documentoDTO);
        DocumentoDTO dto = transformarDocumento(document, true);
        dto.setCodigoDependencia(documentoDTO.getCodigoDependencia());
        return dto;
    }

    DocumentoDTO subirDocumentoPrincipalRadicacion(DocumentoDTO documentoDTO, CommunicationType communicationType) throws SystemException {
        final String nombreDoc = documentoDTO.getNombreDocumento();
        if (StringUtils.isEmpty(nombreDoc)) {
            throw new SystemException("No se ha especificado el nombre del documento");
        }
        byte[] bytes = documentoDTO.getDocumento();
        if (ObjectUtils.isEmpty(bytes)) {
            throw new SystemException("No se ha especificado el contenido del documento");
        }
        final boolean isLabelRequired = ObjectUtils.isEmpty(documentoDTO.getLabelRequired())
                ? true : documentoDTO.getLabelRequired();
        final Carpeta carpetaTarget = buscarCarpetaRadicacion(communicationType);
        //bytes = contentStamper.getResizedPdfDocument(bytes);
        /*if (communicationType == CommunicationType.EE) {
            bytes = digitalSignature.signPDF(bytes);
        }*/
        documentoDTO.setDocumento(bytes);
        final String nroRadicado = documentoDTO.getNroRadicado();
        documentoDTO.setFechaRadicacion(getFechaRadicacion(nroRadicado));
        final Document document = createDocument(carpetaTarget, documentoDTO);
        documentoDTO = transformarDocumento(document, true);
        if (isLabelRequired && (communicationType == CommunicationType.SE || communicationType == CommunicationType.SI)) {
            documentoDTO.setNroRadicado(nroRadicado);
            //transformaEstampaRad(documentoDTO);
        }
        documentoDTO.setCodigoDependencia(carpetaTarget.getFolder().getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));
        return documentoDTO;
    }

    Carpeta buscarCarpetaRadicacion(CommunicationType communicationType) throws SystemException {

        final Folder folder = getSsRadicationFolder(communicationType);
        final String depCode = folder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);
        final Optional<Folder> folderOptional = getUdContainsName(folder, communicationType);

        Carpeta carpeta = new Carpeta();
        carpeta.setFolder(folder);
        if (folderOptional.isPresent()) {
            carpeta.setFolder(folderOptional.get());
            return carpeta;
        }
        final Carpeta crearCarpeta = crearCarpeta(carpeta, communicationType.getFolderName(), depCode, ConstantesECM.CLASE_UNIDAD_DOCUMENTAL, carpeta, null);
        final Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put(ConstantesECM.CMCOR_UD_ID, communicationType.generateId(depCode));
        crearCarpeta.getFolder().updateProperties(propertyMap);

        return crearCarpeta;
    }

    Optional<Folder> getDocumentoPorArchivarFolder(String codigoDependencia) throws SystemException {

        UnidadDocumentalDTO dto = new UnidadDocumentalDTO();
        dto.setNombreUnidadDocumental(CommunicationType.PA.getCommunicationName());
        dto.setCodigoDependencia(codigoDependencia);
        return crearUnidadDocumentalFolder(dto, true);
    }

    public ContenidoDependenciaTrdDTO getDependenciaTrdDTO(String depCode, String serieCode) throws SystemException {
        final ContenidoDependenciaTrdDTO trdDTO = ContenidoDependenciaTrdDTO
                .newInstance().listaSerie(new ArrayList<>()).listaSubSerie(new ArrayList<>()).build();

        final String ecmSerieType = "cmcor:CM_Serie";
        final Session session = getSession();
        String query = "SELECT " + PropertyIds.OBJECT_ID + " FROM " + ecmSerieType +
                " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + depCode + "'" +
                " AND " + PropertyIds.OBJECT_TYPE_ID + " <> 'F:cmcor:CM_Unidad_Documental'";
        if (!StringUtils.isEmpty(serieCode)) {
            query += " AND " + ConstantesECM.CMCOR_SER_CODIGO + " = '" + serieCode + "'";
        }

        for (QueryResult queryResult : session.query(query, false)) {

            final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            final Folder folder = (Folder) session.getObject(objectId);
            final String ecmType = folder.getType().getId();
            final String ecmName = folder.getName();

            if (ecmType.endsWith(ecmSerieType)) {
                final String sCode = folder.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO);
                final SerieDTO serieDTO = new SerieDTO();
                serieDTO.setNombreSerie(ecmName);
                serieDTO.setCodigoSerie(sCode);
                trdDTO.getListaSerie().add(serieDTO);
            } else {
                final SubSerieDTO subSerieDTO = new SubSerieDTO();
                final String ssCode = folder.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO);
                subSerieDTO.setNombreSubSerie(ecmName);
                subSerieDTO.setCodigoSubSerie(ssCode);
                trdDTO.getListaSubSerie().add(subSerieDTO);
            }
        }

        trdDTO.getListaSerie().sort(Comparator.comparing(SerieDTO::getCodigoSerie));
        trdDTO.getListaSubSerie().sort(Comparator.comparing(SubSerieDTO::getCodigoSubSerie));

        return trdDTO;
    }

    /*ContenidoDependenciaTrdDTO getDependenciaTrdDTO(Folder depFolder, String serieCode, String serieName) throws SystemException {
        serieCode = StringUtils.isEmpty(serieCode) ? "" : serieCode.trim();
        final ContenidoDependenciaTrdDTO trdDTO = ContenidoDependenciaTrdDTO
                .newInstance().listaSerie(new ArrayList<>()).listaSubSerie(new ArrayList<>()).build();
        if ("".equals(serieCode)) {
            trdDTO.getListaSerie().addAll(getAllSeries(depFolder));
        } else {
            if (StringUtils.isEmpty(serieName)) {
                throw new SystemException("No se ha especificado el nombre de la serie documental");
            }
            trdDTO.getListaSubSerie().addAll(getAllSubSeries(depFolder, serieCode, serieName));
        }
        trdDTO.getListaSerie().sort(Comparator.comparing(SerieDTO::getCodigoSerie));
        trdDTO.getListaSubSerie().sort(Comparator.comparing(SubSerieDTO::getCodigoSubSerie));
        return trdDTO;
    }*/

    /*private Collection<? extends SubSerieDTO> getAllSubSeries(Folder depFolder, String serieCode, String serieName) {
        final List<SubSerieDTO> subSerieDTOS = new ArrayList<>();
        fillSubSerieList(depFolder, subSerieDTOS, serieCode, serieName);
        return subSerieDTOS;
    }*/

    /*private void fillSubSerieList(Folder depFolder, List<SubSerieDTO> subSerieDTOS, String serieCode, String serieName) {
        final ItemIterable<CmisObject> children = depFolder.getChildren();
        for (CmisObject cmisObject : children) {
            if (cmisObject instanceof Folder) {
                final boolean esActivo = cmisObject.getPropertyValue(ConstantesECM.CMCOR_DEP_ORG_ACTIVO);
                if (esActivo) {
                    if (cmisObject.getType().getId().endsWith("Subserie")) {
                        final String sCode = cmisObject.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO);
                        String fatherFolderName = ((Folder) cmisObject).getFolderParent().getName();
                        if (StringUtils.endsWithIgnoreCase(fatherFolderName, serieName) && serieCode.equalsIgnoreCase(sCode)) {
                            final String ssCode = cmisObject.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO);
                            final String name = cmisObject.getName();
                            subSerieDTOS.add(subSerieDTOS.size(), SubSerieDTO.newInstance()
                                    .codigoSubSerie(ssCode).nombreSubSerie(name).build());
                        }
                        continue;
                    }
                    fillSubSerieList((Folder) cmisObject, subSerieDTOS, serieCode, serieName);
                }
            }
        }
    }*/

    /*private Collection<? extends SerieDTO> getAllSeries(Folder depFolder) {
        final List<SerieDTO> serieDTOS = new ArrayList<>();
        fillSerieList(depFolder, serieDTOS);
        return serieDTOS;
    }*/

    /*private void fillSerieList(Folder depFolder, List<SerieDTO> serieDTOS) {
        final ItemIterable<CmisObject> children = depFolder.getChildren();
        for (CmisObject cmisObject : children) {
            if (cmisObject instanceof Folder) {
                final boolean esActivo = cmisObject.getPropertyValue(ConstantesECM.CMCOR_DEP_ORG_ACTIVO);
                if (esActivo) {
                    if (cmisObject.getType().getId().endsWith("Serie")) {
                        final String code = cmisObject.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO);
                        final String name = cmisObject.getName();
                        final SerieDTO serieDTO = new SerieDTO();
                        serieDTO.setCodigoSerie(code);
                        serieDTO.setNombreSerie(name);
                        serieDTOS.add(serieDTOS.size(), serieDTO);
                        continue;
                    }
                    fillSerieList((Folder) cmisObject, serieDTOS);
                }
            }
        }
    }*/

    private Folder getSerieRadicadora() throws SystemException {
        final Folder depRadFolder = getDepRadicationFolder();
        for (CmisObject child : depRadFolder.getChildren()) {
            if (child.getType().getId().endsWith("CM_Serie")) {
                final boolean esRadicadora = child.getPropertyValue(ConstantesECM.CMCOR_DEP_RADICADORA);
                if (esRadicadora) {
                    return (Folder) child;
                }
            }
        }
        throw new SystemException("En la dependencia radicadora no existe serie de radicacion");
    }

    private Folder getSsRadicationFolder(CommunicationType communicationType) throws SystemException {

        final Folder serieRadicadora = getSerieRadicadora();
        final ItemIterable<CmisObject> children = serieRadicadora.getChildren();

        for (CmisObject cmisObject : children) {
            if (cmisObject.getType().getId().endsWith("CM_Subserie")) {
                final boolean esSsRadicadora = cmisObject.getPropertyValue(ConstantesECM.CMCOR_DEP_RADICADORA);
                if (esSsRadicadora && StringUtils.containsIgnoreCase(cmisObject.getName(), communicationType.getContains())) {
                    return (Folder) cmisObject;
                }
            }
        }
        String docType = "Internos";
        if (communicationType == CommunicationType.SE) {
            docType = "de Salida";
        } else if (communicationType == CommunicationType.EE) {
            docType = "de Entrada";
        }
        throw new SystemException("En la estructura no existe Sub Serie de radicacion para los documentos " + docType);
    }

    void updateAnexos(String idDocument, String newIdDocument, String nroRadicado) throws SystemException {
        final Session session = getSession();
        new Thread(() -> {
            final String query = "SELECT * FROM cmcor:CM_DocumentoPersonalizado" +
                    " WHERE " + ConstantesECM.CMCOR_ID_DOC_PRINCIPAL + " = '" + idDocument + "'";
            final ItemIterable<QueryResult> queryResults = session.query(query, false);
            final Map<String, Object> updateProperties = new HashMap<>();
            updateProperties.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, newIdDocument);
            updateProperties.put(ConstantesECM.CMCOR_DOC_RADICADO, nroRadicado);
            queryResults.forEach(queryResult -> {
                final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                final CmisObject cmisObject = session.getObject(session.createObjectId(objectId));
                cmisObject.updateProperties(updateProperties);
            });
        }).start();
    }

    private Document createDocument(Carpeta carpetaTarget, DocumentoDTO documento) throws SystemException {
        final Map<String, Object> properties = new HashMap<>();
        final byte[] bytes = documento.getDocumento();
        final String documentMimeType = StringUtils.isEmpty(documento.getTipoDocumento()) ?
                MimeTypes.getMIMEType("pdf") : documento.getTipoDocumento();
        final String sufix = MimeTypes.getExtension(documentMimeType);
        final String nombreDoc = (documento.getNombreDocumento().replace(sufix, "")) + sufix;
        final ContentStream contentStream = new ContentStreamImpl(nombreDoc,
                BigInteger.valueOf(bytes.length), documentMimeType, new ByteArrayInputStream(bytes));
        final Folder folder = carpetaTarget.getFolder();

        properties.put(PropertyIds.NAME, nombreDoc);
        properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
        properties.put(ConstantesECM.CMCOR_TIPO_DOCUMENTO, !StringUtils.isEmpty(documento.getTipoPadreAdjunto())
                ? documento.getTipoPadreAdjunto() : DocumentType.MAIN.getType());
        properties.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, documento.getIdDocumentoPadre());
        properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, documentMimeType);
        properties.put(ConstantesECM.CMCOR_DOC_RADICADO, documento.getNroRadicado());
        properties.put(ConstantesECM.CMCOR_DOC_REMITENTE, documento.getNombreRemitente());
        properties.put(ConstantesECM.CMCOR_DOC_ID_UD, folder.getPropertyValue(ConstantesECM.CMCOR_UD_ID));
        properties.put(ConstantesECM.CMCOR_NOMBRE_PROCESO, documento.getNombreProceso());
        properties.put(ConstantesECM.CMCOR_DOC_TIPO_DOCUMENTAL, documento.getTipologiaDocumental());
        properties.put(ConstantesECM.CMCOR_DOC_AUTOR, documento.getDocAutor());
        properties.put(ConstantesECM.CMCOR_DOC_ANULADO, documento.isAnulado());
        if (!ObjectUtils.isEmpty(documento.getFechaRadicacion())) {
            final Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(documento.getFechaRadicacion());
            properties.put(ConstantesECM.CMCOR_FECHA_RADICACION, calendar);
        }
        try {
            return folder.createDocument(properties, contentStream, VersioningState.MAJOR);
        } catch (CmisContentAlreadyExistsException ccaee) {
            log.error(ConstantesECM.ECM_ERROR_DUPLICADO, ccaee);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("El documento ya existe en el ECM")
                    .withRootException(ccaee)
                    .buildSystemException();
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    private Optional<Folder> getUdContainsName(Folder folder, CommunicationType communicationType) {
        final ItemIterable<CmisObject> children = folder.getChildren();
        communicationType.setNameFolder(CommunicationType.PD.getCommunicationName());
        if (communicationType != CommunicationType.PD) {
            int index;
            final String ssName = (index = folder.getName().indexOf('_')) != -1 ? folder.getName().substring(index + 1) : folder.getName();
            communicationType.setNameFolder(ssName);
        }
        for (CmisObject cmisObject :
                children) {
            if (cmisObject instanceof Folder && StringUtils.containsIgnoreCase(cmisObject.getName(), communicationType.getFolderName())) {
                return Optional.of((Folder) cmisObject);
            }
        }
        return Optional.empty();
    }


    /**
     * Metodo que retorna true en caso de que la cadena que se le pasa es numerica y false si no.
     *
     * @param cadena Cadena de texto que se le pasa al metodo
     * @return Retorna true o false
     */
    private boolean isNumeric(String cadena) {
        return cadena.matches("[+-]?\\d*(\\.\\d+)?") && "".equals(cadena) == Boolean.FALSE;
    }

    /*private List<UnidadDocumentalDTO> listarUnidadesDocumentales(UnidadDocumentalDTO dto, String query, Session session) throws SystemException {
        log.info("Executing query {}", query);
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        final List<UnidadDocumentalDTO> unidadDocumentalDTOS = new ArrayList<>();
        final String nombreUd = dto.getNombreUnidadDocumental();
        final String depCode = dto.getCodigoDependencia();
        String depName = "";
        if (!StringUtils.isEmpty(depCode)) {
            Folder folderBy = getFolderBy(ConstantesECM.CLASE_DEPENDENCIA, ConstantesECM.CMCOR_DEP_CODIGO, depCode);
            depName = folderBy.getName();
        }
        for (QueryResult queryResult :
                queryResults) {
            final String idUnidadDocumental = queryResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_UD_ID);
            final String folderName = queryResult.getPropertyValueByQueryName(PropertyIds.NAME);
            if ((!StringUtils.isEmpty(idUnidadDocumental) && !idUnidadDocumental.trim().isEmpty())
                    && (StringUtils.isEmpty(nombreUd) || StringUtils.containsIgnoreCase(folderName, nombreUd))) {
                final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                final Folder folder = (Folder) session.getObject(session.createObjectId(objectId));
                UnidadDocumentalDTO newDto = transformarUnidadDocumental(folder);
                PhaseType phaseType = PhaseType.getPhaseTypeBy(newDto.getFaseArchivo());
                if (phaseType == PhaseType.ACI) {
                    newDto.setFaseArchivo(PhaseType.AC.getPhaseName());
                }
                String[] serieSubSeries = getSerieSubSerie(folder);
                newDto.setNombreSerie(serieSubSeries[0]);
                newDto.setNombreSubSerie(serieSubSeries[1]);
                newDto.setNombreDependencia(depName);
                unidadDocumentalDTOS.add(unidadDocumentalDTOS.size(), newDto);
            }
        }
        return unidadDocumentalDTOS;
    }*/

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @param query   una previa consulta de seleccion de valores del dto UD
     * @param session Obj coneccion de Alfresco
     * @return List<UnidadDocumentalDTO> Lst
     *//*
    private List<UnidadDocumentalDTO> listarUnidadesDocumentalesPorAccion(UnidadDocumentalDTO dto, String query, Session session) throws BusinessException {
        try {
            AccionUsuario accionUsuario = AccionUsuario.valueOf(dto.getAccion().toUpperCase());
            query += (!query.contains("WHERE") ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_FECHA_INICIAL + " IS NOT NULL";
            switch (accionUsuario) {
                case ABRIR:
                case REACTIVAR:
                    query += " AND " + ConstantesECM.CMCOR_UD_CERRADA + " = 'true' AND " + ConstantesECM.CMCOR_UD_FECHA_FINAL + " IS NOT NULL" +
                            " AND " + ConstantesECM.CMCOR_UD_FECHA_CIERRE + " IS NOT NULL AND " + ConstantesECM.CMCOR_UD_INACTIVO + " = 'true'";
                    break;
                case CERRAR:
                    query += " AND " + ConstantesECM.CMCOR_UD_CERRADA + " = 'false' AND " + ConstantesECM.CMCOR_UD_INACTIVO + " = 'false'";
                    break;
            }
            final List<UnidadDocumentalDTO> unidadDocumentalDTOS = listarUnidadesDocumentales(dto, query, session);
            final List<UnidadDocumentalDTO> udTmp = new ArrayList<>();
            unidadDocumentalDTOS.forEach(unidadDocumentalDTO -> {
                final String soporte = unidadDocumentalDTO.getSoporte();
                if (!StringUtils.isEmpty(soporte) && !soporte.trim().isEmpty()) {
                    udTmp.add(udTmp.size(), unidadDocumentalDTO);
                }
            });
            return udTmp;

        } catch (Exception e) {
            throw new BusinessException("ECM ERROR: " + e.getMessage());
        }
    }*/

    /**
     * Metodo auxuliar para devolver la carpeta padre
     *
     * @param codFolder Codigo de carpeta
     * @param aux       Carpeta por la cual se realiza la busqueda
     * @param metadato  Propiedad por la cual se filtra
     * @return Carpeta padre
     */
    private Carpeta getCarpeta(String codFolder, Carpeta aux, String metadato, Carpeta folderReturn) {
        Carpeta folderAux = folderReturn;
        final Object propertyValue = aux.getFolder().getPropertyValue(ConstantesECM.CMCOR + configuracion.getPropiedad(metadato));
        if (!ObjectUtils.isEmpty(propertyValue) &&
                propertyValue.equals(codFolder)) {
            folderAux = aux;
        }
        return folderAux;
    }

    /**
     * Metodo que devuelve las carpetas hijas de una carpeta
     *
     * @param carpetaPadre Carpeta a la cual se le van a buscar las carpetas hijas
     * @return Lista de carpetas resultantes de la busqueda
     */
    private List<Carpeta> obtenerCarpetasHijasDadoPadre(Carpeta carpetaPadre) {
        log.info("### Obtener Carpetas Hijas Dado Padre: " + carpetaPadre.getFolder().getName());
        List<Carpeta> listaCarpetas = new ArrayList<>();

        try {
            ItemIterable<CmisObject> listaObjetos = carpetaPadre.getFolder().getChildren();
            listaCarpetas = new ArrayList<>();
            //Lista de carpetas hijas
            for (CmisObject contentItem : listaObjetos) {

                if (contentItem instanceof Folder && contentItem.getType().getId().startsWith("F:cmcor")) {
                    Carpeta folder = new Carpeta();
                    folder.setFolder((Folder) contentItem);
                    listaCarpetas.add(folder);
                }
            }
        } catch (Exception e) {
            log.error("*** Error al obtener Carpetas Hijas dado padre*** ", e);
        }
        return listaCarpetas;
    }

    /*private String getTipoComunicacionSelector(String tipoComunicacion) {
        switch (tipoComunicacion) {
            case "EI":
                return ConstantesECM.COMUNICACIONES_INTERNAS_RECIBIDAS;

            case "SI":
                return ConstantesECM.COMUNICACIONES_INTERNAS_ENVIADAS;

            case "EE":
                return ConstantesECM.COMUNICACIONES_EXTERNAS_RECIBIDAS;

            case "SE":
                return ConstantesECM.COMUNICACIONES_EXTERNAS_ENVIADAS;
            default:
                return ConstantesECM.COMUNICACIONES_INTERNAS_RECIBIDAS;
        }
    }*/

    private void crearInsertarCarpetaRadicacion(DocumentoDTO documentoDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, String comunicacionOficial, String tipoComunicacionSelector, Optional<Carpeta> comunicacionOficialFolder) throws SystemException {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        List<DocumentoDTO> documentoDTOList = new ArrayList<>();
        Carpeta carpetaTarget = new Carpeta();
        String idDocumento;
        if (comunicacionOficialFolder.isPresent()) {
            log.info(ConstantesECM.EXISTE_CARPETA + comunicacionOficialFolder.get().getFolder().getName());

            List<Carpeta> carpetasDeComunicacionOficial = obtenerCarpetasHijasDadoPadre(comunicacionOficialFolder.get());

            Optional<Carpeta> comunicacionOficialInOut = carpetasDeComunicacionOficial.stream()
                    .filter(p -> p.getFolder().getName().contains(comunicacionOficial)).findFirst();

            if (!comunicacionOficialInOut.isPresent()) {

                Carpeta carpetaCreada = crearCarpeta(comunicacionOficialFolder.get(), comunicacionOficial, "11", ConstantesECM.CLASE_SUBSERIE, comunicacionOficialFolder.get(), null);
                if (carpetaCreada != null) {
                    log.info(ConstantesECM.EXISTE_CARPETA + carpetaCreada.getFolder().getName());
                    List<Carpeta> carpetasDeComunicacionOficialDentro = obtenerCarpetasHijasDadoPadre(carpetaCreada);

                    Optional<Carpeta> comunicacionOficialInOutDentro = carpetasDeComunicacionOficialDentro.stream()
                            .filter(p -> p.getFolder().getName().contains(tipoComunicacionSelector)).findFirst();
                    carpetaTarget = comunicacionOficialInOutDentro.orElseGet(() -> crearCarpeta(carpetaCreada, tipoComunicacionSelector + year, "11", ConstantesECM.CLASE_UNIDAD_DOCUMENTAL, carpetaCreada, null));
                }

            } else {
                log.info(ConstantesECM.EXISTE_CARPETA + comunicacionOficialInOut.get().getFolder().getName());

                List<Carpeta> carpetasDeComunicacionOficialDentro = obtenerCarpetasHijasDadoPadre(comunicacionOficialInOut.get());

                Optional<Carpeta> comunicacionOficialInOutDentro = carpetasDeComunicacionOficialDentro.stream()
                        .filter(p -> p.getFolder().getName().contains(tipoComunicacionSelector)).findFirst();
                carpetaTarget = comunicacionOficialInOutDentro.orElseGet(() -> crearCarpeta(comunicacionOficialInOut.get(), tipoComunicacionSelector + year, "11", ConstantesECM.CLASE_UNIDAD_DOCUMENTAL, comunicacionOficialInOut.get(), null));
            }
            idDocumento = crearDocumentoDevolverId(documentoDTO, response, bytes, properties, documentoDTOList, carpetaTarget);
            //Creando el mensaje de respuesta
            response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            response.setMensaje("Documento añadido correctamente");
            log.info(ConstantesECM.AVISO_CREA_DOC_ID + idDocumento);

        } else {
            response.setCodMensaje("3333");
            response.setMensaje("En esta sede y dependencia no esta permitido realizar radicaciones");
        }
    }

    private String crearDocumentoDevolverId(DocumentoDTO documentoDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, List<DocumentoDTO> documentoDTOList, Carpeta carpetaTarget) throws SystemException {

        log.info("Se llenan los metadatos del documento a crear");
        ContentStream contentStream = new ContentStreamImpl(documentoDTO.getNombreDocumento(), BigInteger.valueOf(bytes.length), documentoDTO.getTipoDocumento(), new ByteArrayInputStream(bytes));

        if (documentoDTO.getNroRadicado() != null) {
            properties.put(ConstantesECM.CMCOR_DOC_RADICADO, documentoDTO.getNroRadicado());
        }
        if (documentoDTO.getNombreRemitente() != null) {
            properties.put(ConstantesECM.CMCOR_DOC_REMITENTE, documentoDTO.getNombreRemitente());
        }
        if (documentoDTO.getTipologiaDocumental() != null) {
            properties.put(ConstantesECM.CMCOR_DOC_TIPO_DOCUMENTAL, documentoDTO.getTipologiaDocumental());
        }
        log.info(ConstantesECM.AVISO_CREA_DOC);
        final String docName = StringUtils.isEmpty(documentoDTO.getNombreDocumento()) ?
                "" : documentoDTO.getNombreDocumento().trim();
        properties.put(PropertyIds.NAME, docName);
        documentoDTO.setNombreDocumento(docName);
        Document newDocument = carpetaTarget.getFolder().createDocument(properties, contentStream, VersioningState.MAJOR);
        DocumentoDTO dto = transformarDocumento(newDocument, true);

        documentoDTO.setIdDocumentoPadre(dto.getIdDocumentoPadre());
        documentoDTO.setIdDocumento(dto.getIdDocumento());
        documentoDTO.setTipoDocumento(dto.getTipoDocumento());
        documentoDTO.setNroRadicado(dto.getNroRadicado());
        documentoDTO.setNombreDocumento(dto.getNombreDocumento());
        documentoDTO.setTipoPadreAdjunto(dto.getTipoPadreAdjunto());
        documentoDTO.setVersionLabel(dto.getVersionLabel());
        documentoDTOList.add(documentoDTO);
        response.setDocumentoDTOList(documentoDTOList);
        return dto.getIdDocumento();
    }

    /**
     * Metodo para llenar propiedades para crear carpeta
     *
     * @param tipoCarpeta tipo de carpeta
     * @param clase       clase de la carpeta
     * @param props       objeto de propiedades
     * @param codOrg      codigo
     */
    private void llenarPropiedadesCarpeta(String tipoCarpeta, String clase, Map<String, Object> props, String codOrg, Carpeta folderFather, String idOrgOfc) {

        props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(clase));
        props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(clase));
        props.put(tipoCarpeta, codOrg);

        if (ConstantesECM.CMCOR_SS_CODIGO.equals(tipoCarpeta)) {
            if (folderFather != null) {
                props.put(ConstantesECM.CMCOR_DEP_CODIGO_PADRE, folderFather.getFolder().getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO));
                props.put(ConstantesECM.CMCOR_SER_CODIGO, folderFather.getFolder().getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO));
                props.put(ConstantesECM.CMCOR_DEP_CODIGO, idOrgOfc);
            }
        } else if (ConstantesECM.CMCOR_SER_CODIGO.equals(tipoCarpeta)) {
            if (folderFather != null) {
                props.put(ConstantesECM.CMCOR_DEP_CODIGO_PADRE, folderFather.getFolder().getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));
                props.put(ConstantesECM.CMCOR_DEP_CODIGO, idOrgOfc);
            }
        } else {
            props.put(ConstantesECM.CMCOR_DEP_CODIGO_PADRE, codOrg);
            props.put(ConstantesECM.CMCOR_DEP_CODIGO, codOrg);
        }
    }

    /**
     * Metodo para llenar propiedades para crear carpeta
     *
     * @param clase  clase de la carpeta
     * @param props  objeto de propiedades
     * @param codOrg codigo
     */
    private void llenarPropiedadesCarpeta(String clase, Map<String, Object> props, String codOrg) {
        props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(clase));
        props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(clase));
        props.put(ConstantesECM.CMCOR_UB_CODIGO, codOrg);
    }

    private Carpeta getCarpeta(String carpetaCrearBuscar, Optional<Carpeta> dependencia, int year, Optional<Carpeta> produccionDocumental) {
        Carpeta carpetaTarget = null;
        if (produccionDocumental.isPresent()) {
            log.info(ConstantesECM.EXISTE_CARPETA + carpetaCrearBuscar + year);
            carpetaTarget = produccionDocumental.get();
        } else {
            log.info("Se crea la Carpeta: " + carpetaCrearBuscar + year);
            if (dependencia.isPresent()) {
                carpetaTarget = crearCarpeta(dependencia.get(), carpetaCrearBuscar + year, "11", ConstantesECM.CLASE_UNIDAD_DOCUMENTAL, dependencia.get(), null);
            }
        }
        return carpetaTarget;
    }

    private void saveStamperImageFile(DocumentoDTO documentoDTO, String filename) throws SystemException {
        Folder rootFolder = getSession().getRootFolder();
        Folder folderImage = getFolderFromRootByName("Images");
        if (null == folderImage) {
            final Map<String, Object> map = new HashMap<>();
            map.put(PropertyIds.NAME, "Images");
            map.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
            folderImage = rootFolder.createFolder(map);
        }
        final Map<String, Object> map = new HashMap<>();
        final String mimeType = MimeTypes.getMIMEType("png");
        map.put(PropertyIds.NAME, "tag_" + filename + "_temp.png");
        map.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        map.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, mimeType);
        byte[] bytes = documentoDTO.getDocumento();
        ContentStream stream = new ContentStreamImpl(filename, BigInteger.valueOf(bytes.length),
                mimeType, new ByteArrayInputStream(bytes));
        Document document = folderImage.createDocument(map, stream, VersioningState.MAJOR);
        documentoDTO.setIdDocumento(document.getId());
    }

    private Document getStamperImage(String filename) throws SystemException {
        Folder folder = getFolderFromRootByName("Images");
        if (null != folder) {
            final ItemIterable<CmisObject> children = folder.getChildren();
            for (CmisObject cmisObject :
                    children) {
                if (cmisObject.getName().contains(filename)) {
                    return (Document) cmisObject;
                }
            }
        }
        return null;
    }

    private Date getFechaRadicacion(String numeroRadicado) {
        WebTarget wt = ClientBuilder.newClient().target(CORRESPONDENCIA_ENDPOINT);
        Response response = wt
                .path("/radicado-web-api/obtener-fecha-Radicacion/" + numeroRadicado)
                .request()
                .get();
        if (response.getStatus() == 200) {
            final RadicadoDTO radicadoDTO = response.readEntity(RadicadoDTO.class);
            return radicadoDTO.getFechaRadicacion() == null ? new Date() : radicadoDTO.getFechaRadicacion();
        }
        return new Date();
    }

    private Calendar getFechaRadicacionCalendar(String numeroRadicado) {
        final Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(getFechaRadicacion(numeroRadicado));
        return calendar;
    }

    String getLabelDocument(String ecmDocumentLabel, boolean isToPrint) {
        String newLabelString = ecmDocumentLabel;
        if (isToPrint) {
            int index;
            if (!StringUtils.isEmpty(newLabelString) && (index = newLabelString.lastIndexOf('.')) >= 0) {
                newLabelString = (Integer.parseInt(newLabelString.substring(index + 1)) + 1) + ".0";
            }
            return newLabelString;
        }
        int intLabel = (int) Double.parseDouble(newLabelString);
        return "1." + (intLabel - 1);
    }

    List<UnidadDocumentalDTO> getListaUdByIdAndName(String idUd, String nombreUd) throws SystemException {
        try {
            final Session session = getSession();
            final String ecmClass = ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_BASE);
            final String query = "select * from " + ecmClass + " where " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + ecmClass + "'";
            final ItemIterable<QueryResult> queryResults = session.query(query, false);
            final Iterator<QueryResult> iterator = queryResults.iterator();
            final List<Folder> dtos = new ArrayList<>();
            if (iterator.hasNext()) {
                final QueryResult next = iterator.next();
                final String objId = next.getPropertyValueById(PropertyIds.OBJECT_ID);
                final Folder folder = (Folder) session.getObject(objId);
                fillUnidadesDocumentalesListByIdName(dtos, folder, idUd, nombreUd);
            }
            return dtos.stream().map(this::transformarUnidadDocumental).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new SystemException(ex.getMessage());
        }
    }

    private void fillUnidadesDocumentalesListByIdName(List<Folder> dtos, Folder folder, String idUd, String nombreUd) {
        final ItemIterable<CmisObject> children = folder.getChildren();
        children.forEach(cmisObject -> {
            final String ecmTypeString = cmisObject.getType().getId();
            if (ecmTypeString.endsWith("CM_Unidad_Documental")) {
                final Folder folderTmp = (Folder) cmisObject;
                final String id = cmisObject.getPropertyValue(ConstantesECM.CMCOR_UD_ID);
                if (!StringUtils.isEmpty(id)) {
                    final String nameUd = cmisObject.getName();
                    if (StringUtils.isEmpty(idUd) && StringUtils.isEmpty(nombreUd)) {
                        dtos.add(dtos.size(), folderTmp);
                    } else if (!StringUtils.isEmpty(idUd) && StringUtils.isEmpty(nombreUd) && StringUtils.equalsIgnoreCase(id, idUd)) {
                        dtos.add(dtos.size(), folderTmp);
                    } else if (StringUtils.isEmpty(idUd) && !StringUtils.isEmpty(nombreUd) && StringUtils.containsIgnoreCase(nameUd, nombreUd)) {
                        dtos.add(dtos.size(), folderTmp);
                    } else if (StringUtils.equalsIgnoreCase(id, idUd) && StringUtils.containsIgnoreCase(nameUd, nombreUd)) {
                        dtos.add(dtos.size(), folderTmp);
                    }
                }
            } else if (ecmTypeString.endsWith("CM_Unidad_Administrativa") || ecmTypeString.endsWith("CM_Serie") || ecmTypeString.endsWith("CM_Subserie")) {
                fillUnidadesDocumentalesListByIdName(dtos, (Folder) cmisObject, idUd, nombreUd);
            }
        });
    }

    public Session getSession() throws SystemException {
        return connection.getSession();
    }
}