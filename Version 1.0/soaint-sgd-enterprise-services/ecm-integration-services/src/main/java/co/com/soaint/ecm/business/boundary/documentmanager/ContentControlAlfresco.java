package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.*;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl.ConnectionImpl;
import co.com.soaint.ecm.domain.entity.*;
import co.com.soaint.ecm.domain.entity.DocumentType;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.canonical.ecm.util.StructureUtils;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.MimeTypes;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Dasiel
 */
@Log4j2
@BusinessControl
public final class ContentControlAlfresco extends ContentControlUtilities {

    private static final long serialVersionUID = 1L;

    @Autowired
    private IRecordServices recordServices;
    @Autowired
    private ReporteContent reporteContent;
    @Autowired
    @Qualifier("contentStamper")
    private ContentStamper contentStamper;
    @Autowired
    @Qualifier("digitalSignature")
    private DigitalSignature digitalSignature;
    @Autowired
    @Qualifier("contentStructure")
    private StructureAlfresco structureAlfresco;
    @Autowired
    @Qualifier("contentTemplate")
    private ContentTemplate contentTemplate;
    @Autowired
    @Qualifier("connection")
    private ConnectionImpl connection;

    @PostConstruct
    public void init() {
        setConfiguracion(connection.getConfiguracion());
        setContentStamper(contentStamper);
        setDigitalSignature(digitalSignature);
        setStructureAlfresco(structureAlfresco);
        setConnection(connection);
        setContentTemplate(contentTemplate);
    }

    /**
     * Metodo para devolver documento para su visualización
     *
     * @param documentoDTO Objeto que contiene los metadatos del documento dentro del ECM
     * @return Objeto de tipo response que devuleve el documento
     */
    @Override
    public MensajeRespuesta descargarDocumento(DocumentoDTO documentoDTO) throws SystemException {

        final Session session = getSession();

        try {
            log.info("Se entra al metodo de descargar el documento");
            Document doc = (Document) session.getObject(documentoDTO.getIdDocumento());

            String versionLabel = documentoDTO.getVersionLabel();

            if (versionLabel != null) {
                documentoDTO.setVersionLabel(getLabelDocument(versionLabel, false));
                List<Document> versions = doc.getAllVersions();
                //Filtrar la version correcta dentro de las versiones del documento para obtener el file
                Optional<Document> version = versions.stream()
                        .filter(p -> p.getVersionLabel().equals(documentoDTO.getVersionLabel())).findFirst();
                if (version.isPresent()) {
                    doc = version.get();
                }
            }

            final ArrayList<DocumentoDTO> documento = new ArrayList<>();
            final DocumentoDTO documentoDTO1 = new DocumentoDTO();
            final MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();

            documentoDTO1.setDocumento(getDocumentBytes(doc.getContentStream()));

            mensajeRespuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            mensajeRespuesta.setMensaje("Documento Retornado con exito");
            documento.add(documentoDTO1);
            mensajeRespuesta.setDocumentoDTOList(documento);
            return mensajeRespuesta;

        } catch (Exception e) {
            log.error("An error has occurred: {}", e.getMessage());
            throw new SystemException("Error: " + e.getMessage());
        }
    }

    /**
     * Servicio que devuelve el listado de las Series y de las Dependencias
     *
     * @param dependenciaTrdDTO Objeto dependencia que contiene los datos necesarios para realizar la busqueda
     * @return Objeto de dependencia que contiene las sedes o las dependencias buscadas
     */
    @Override
    public MensajeRespuesta devolverSerieSubSerie(ContenidoDependenciaTrdDTO dependenciaTrdDTO) throws SystemException {
        try {
            final String depCode = dependenciaTrdDTO.getIdOrgOfc();
            if (StringUtils.isEmpty(depCode)) {
                log.error("Oopss... No se ha especificado el codigo de la dependencia");
                throw new BusinessException("No se ha especificado el codigo de la dependencia");
            }

            /*final Folder depFolder = getFolderBy(ConstantesECM.CLASE_DEPENDENCIA, ConstantesECM.CMCOR_DEP_CODIGO, depCode);
            final String serieName = dependenciaTrdDTO.getNomSerie();*/
            final String serieCode = dependenciaTrdDTO.getCodSerie();

//            ContenidoDependenciaTrdDTO trdDTO = getDependenciaTrdDTO(depFolder, serieCode, serieName);

            final MensajeRespuesta respuesta = new MensajeRespuesta();
            final List<ContenidoDependenciaTrdDTO> listaSerieSubSerie = new ArrayList<>();

            respuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            respuesta.setMensaje("Series o Subseries devueltas correctamente");
            listaSerieSubSerie.add(listaSerieSubSerie.size(), getDependenciaTrdDTO(depCode, serieCode));
            respuesta.setContenidoDependenciaTrdDTOS(listaSerieSubSerie);
            return respuesta;

        } catch (Exception e) {
            log.error("*** Error al obtener las series o subseries *** {}", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * Metodo que crea las unidades documentales del ECM
     *
     * @param unidadDocumentalDTO Objeto dependencia que contiene los datos necesarios para realizar la busqueda
     * @return MensajeRespuesta
     */
    @Override
    public MensajeRespuesta crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("Executing crearUnidadDocumental method");

        final MensajeRespuesta response = new MensajeRespuesta();
        response.setResponse(new HashMap<>());

        final String dependencyCode = unidadDocumentalDTO.getCodigoDependencia();
        Optional<Folder> optionalFolder = crearUnidadDocumentalFolder(unidadDocumentalDTO, false);
        if (!optionalFolder.isPresent()) {
            log.error(ConstantesECM.NO_RESULT_MATCH);
            throw new SystemException(ConstantesECM.NO_RESULT_MATCH);
        }
        response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
        response.setMensaje(ConstantesECM.OPERACION_COMPLETADA_SATISFACTORIAMENTE);
        response.getResponse().put("unidadDocumental", transformarUnidadDocumental(optionalFolder.get()));
        reporteContent.processReporte8(dependencyCode);
        return response;
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @return Mensaje de respuesta
     */
    @Override
    public MensajeRespuesta listarUnidadDocumental(final UnidadDocumentalDTO dto) throws SystemException {
        final MensajeRespuesta respuesta = new MensajeRespuesta();
        final List<UnidadDocumentalDTO> unidadDocumentalDTOS = listarUnidadesDocumentales(dto);
        final String messaje = unidadDocumentalDTOS.isEmpty() ? "El sistema no encuentra la unidad documental que está " +
                "buscando. Por favor, solicite su creación" : "Listado seleccionado correctamente";
        respuesta.setMensaje(messaje);
        respuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
        final Map<String, Object> map = new HashMap<>();
        map.put("unidadDocumental", unidadDocumentalDTOS
                .parallelStream()
                .filter(dto1 -> {
                    final PhaseType phaseType = PhaseType.getPhaseTypeBy(dto1.getFaseArchivo());
                    return phaseType != PhaseType.AH;
                }).collect(Collectors.toList()));
        respuesta.setResponse(map);
        return respuesta;
    }

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idDocumento Id Documento
     * @return MensajeRespuesta con los detalles del documento
     */
    @Override
    public MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento) throws SystemException {
        log.info("Ejecutando el metodo MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento, Session session)");

        final MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();

        if (ObjectUtils.isEmpty(idDocumento)) {
            log.info("El ID del documento esta vacio");
            mensajeRespuesta.setMensaje("El ID del documento esta vacio");
            mensajeRespuesta.setCodMensaje("11111");
            return mensajeRespuesta;
        }

        try {
            final Session session = getSession();
            final CmisObject cmisObjectDocument = session.getObject(session.getObject(idDocumento));

            if (!(cmisObjectDocument instanceof Document)) {
                log.info(ConstantesECM.NO_RESULT_MATCH);
                mensajeRespuesta.setMensaje(ConstantesECM.NO_RESULT_MATCH);
                mensajeRespuesta.setCodMensaje("11111");
                return mensajeRespuesta;
            }

            final Map<String, Object> mapResponsonse = new HashMap<>();
            mapResponsonse.put("documentoDTO", transformarDocumento((Document) cmisObjectDocument, true));
            mensajeRespuesta.setMensaje("Documento devuelto correctamente");
            mensajeRespuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            mensajeRespuesta.setResponse(mapResponsonse);

            return mensajeRespuesta;
        } catch (CmisObjectNotFoundException ex) {
            log.error("Error al buscar el documento {}", ex.getMessage());
            throw new SystemException(ConstantesECM.NO_RESULT_MATCH);
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @return MensajeRespuesta      Unidad Documntal
     */
    @Override
    public MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental) throws SystemException {
        log.info("Ejecutando el metodo detallesUnidadDocumental(String idUnidadDocumental)");

        final Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental);
        if (optionalFolder.isPresent()) {
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            respuesta.setMensaje("Detalles Unidad Documental");
            respuesta.setResponse(new HashMap<>());
            respuesta.getResponse().put("unidadDocumental", transformarUnidadDocumental(optionalFolder.get()));
            return respuesta;
        }
        throw new SystemException("Unidad Documental no encontrada con ID: '" + idUnidadDocumental + "'");
    }

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param idUnidadDocumental Id de la Unidad Documental
     * @return UnidadDocumentalDTO si existe, null si no existe
     */
    @Override
    public Optional<UnidadDocumentalDTO> getUDToCloseById(String idUnidadDocumental) {
        log.info("Ejecutando UnidadDocumentalDTO listarDocsDadoIdUD(String idUnidadDocumental)");

        try {
            final Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental);
            if (optionalFolder.isPresent()) {

                final Folder unidadDocumentalFolder = optionalFolder.get();
                final UnidadDocumentalDTO documentalDTO = transformarUnidadDocumental(unidadDocumentalFolder);
                final List<Document> ecmDocumentsFromFolder = getEcmDocumentsFromFolder(unidadDocumentalFolder);
                final List<DocumentoDTO> documentoDTOS = new ArrayList<>();

                for (Document document : ecmDocumentsFromFolder) {
                    final List<String> aspects = document.getPropertyValue("cmis:secondaryObjectTypeIds");
                    if (aspects == null || !aspects.contains("P:rma:recordComponentIdentifier")) {
                        documentoDTOS.add(documentoDTOS.size(), transformarDocumento(document, true));
                    }
                }
                documentalDTO.setListaDocumentos(documentoDTOS);
                return Optional.of(documentalDTO);
            }
        } catch (Exception e) {
            log.error("Error ocurrido: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<UnidadDocumentalDTO> getUDById(String idUnidadDocumental) {
        log.info("Ejecutando UnidadDocumentalDTO getUDById(String idUnidadDocumental)");
        Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental);
        if (optionalFolder.isPresent()) {
            final Folder folder = optionalFolder.get();
            UnidadDocumentalDTO documentalDTO = transformarUnidadDocumental(folder);
            return Optional.of(documentalDTO);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Folder> getUDFolderById(String idUnidadDocumental) {
        log.info("Ejecutando Folder getUDFolderById(String idProperty)");
        Optional<Folder> response = Optional.empty();
        if (!ObjectUtils.isEmpty(idUnidadDocumental)) {
            idUnidadDocumental = idUnidadDocumental.toUpperCase().trim();
            final String queryString = "SELECT * FROM " + ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL) +
                    " WHERE " + ConstantesECM.CMCOR_UD_ID + " = '" + idUnidadDocumental + "'";
            try {
                final Session session = getSession();
                final ItemIterable<QueryResult> queryResults = session.query(queryString, false);
                final Iterator<QueryResult> iterator = queryResults.iterator();
                if (iterator.hasNext()) {
                    QueryResult queryResult = iterator.next();
                    String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                    Folder folder = (Folder) session.getObject(session.getObject(objectId));
                    response = ObjectUtils.isEmpty(folder) ? Optional.empty() : Optional.of(folder);
                }
            } catch (Exception ex) {
                log.error("Ocurrio un error en el Servidor {}", ex.getMessage());
                return Optional.empty();
            }
        }
        return response;
    }

    @Override
    public boolean actualizarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) {
        final Optional<Folder> optionalFolder = getUDFolderById(unidadDocumentalDTO.getId());
        return optionalFolder.isPresent() && optionalFolder.get()
                .updateProperties(getPropertyMapFrom(unidadDocumentalDTO), true) != null;
    }

    /**
     * Metodo para mover carpetas dentro de Alfresco
     *
     * @param documento      Identificador del documento que se va a mover
     * @param carpetaFuente  Carpeta desde donde se va a mover el documento
     * @param carpetaDestino Carpeta a donde se va a mover el documento
     * @return Mensaje de respuesta del metodo(codigo y mensaje)
     */
    @Override
    public MensajeRespuesta movDocumento(String documento, String carpetaFuente, String carpetaDestino) throws SystemException {
        log.info("### Mover documento: " + documento);
        MensajeRespuesta response = new MensajeRespuesta();
        try {

            Carpeta carpetaF;
            Carpeta carpetaD;

            carpetaF = obtenerCarpetaPorNombre(carpetaFuente);
            carpetaD = obtenerCarpetaPorNombre(carpetaDestino);
            ObjectId idDoc = new ObjectIdImpl(documento);

            final Session session = getSession();
            CmisObject object = session.getObject(idDoc);
            Document mvndocument = (Document) object;
            mvndocument.move(carpetaF.getFolder(), carpetaD.getFolder());
            response.setMensaje("OK");
            response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            return response;
        } catch (Exception e) {
            log.error("*** Error al mover el documento *** {}", e);
            throw new SystemException(e.getMessage());
        }
    }

    /**
     * Metodo para generar el arbol de carpetas dentro del Alfresco
     *
     * @param estructuraList Estrcutura que sera creada dentro del Alfresco
     * @param folder         Carpeta padre de la estructura
     * @return Mensaje de respuesta (codigo y mensaje)
     */
    @Override
    public MensajeRespuesta generarArbol(List<EstructuraTrdDTO> estructuraList, Carpeta folder) throws SystemException {
        log.info("### Generando arbol");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            int bandera = 0;
            //Recorremos la lista general
            for (EstructuraTrdDTO estructura : estructuraList) {
                List<OrganigramaDTO> organigramaList = estructura.getOrganigramaItemList();
                List<ContenidoDependenciaTrdDTO> trdList = estructura.getContenidoDependenciaList();

                //Grantizar que el orden de la estructura sea de menor a mayor, ideOrgaAdmin
                StructureUtils.ordenarListaOrganigrama(organigramaList);
                Carpeta folderFather = null;
                Carpeta folderSon;
                Carpeta folderFatherContainer = null;
                //Recorremos la lista organigrama
                for (OrganigramaDTO organigrama : organigramaList)
                    if (bandera == 0) {

                        folderFather = chequearCapetaPadre(folder, organigrama.getCodOrg());
                        if (ObjectUtils.isEmpty(folderFather)) {
                            if (!ObjectUtils.isEmpty(obtenerCarpetaPorNombre(organigrama.getNomOrg()).getFolder())) {
                                folderFather = obtenerCarpetaPorNombre(organigrama.getNomOrg());
                                log.info("Organigrama -- ya existe la carpeta: " + folderFather.getFolder().getName());
                            } else {
                                log.info("Organigrama --  Creando folder: " + organigrama.getNomOrg());
                                folderFather = crearCarpeta(folder, organigrama.getNomOrg(), organigrama.getCodOrg(), ConstantesECM.CLASE_BASE, null, null);
                            }

                        } else {
                            log.info("Organigrama --  La carpeta ya esta creado: " + folderFather.getFolder().getName());
                            //Actualización de folder
                            if (!(organigrama.getNomOrg().equals(folderFather.getFolder().getName()))) {
                                log.info("Se debe actualizar al nombre: " + organigrama.getNomOrg());
                                actualizarNombreCarpeta(folderFather, organigrama.getNomOrg());
                            } else {
                                log.info("Organigrama --  El folder ya esta creado: " + organigrama.getNomOrg());
                            }
                        }
                        bandera++;
                        folderFatherContainer = folderFather;

                    } else {
                        folderSon = chequearCapetaPadre(folderFather, organigrama.getCodOrg());
                        if (ObjectUtils.isEmpty(folderSon)) {
                            log.info("Organigrama --  Creando folder: " + organigrama.getNomOrg());
                            folderSon = crearCarpeta(folderFather, organigrama.getNomOrg(), organigrama.getCodOrg(), ConstantesECM.CLASE_DEPENDENCIA, folderFather, null);
                        } else {
                            log.info("Organigrama --  El folder ya esta creado2: " + folderSon.getFolder().getName());
                            //Actualización de folder
                            if (!(organigrama.getNomOrg().equals(folderSon.getFolder().getName()))) {
                                log.info("Se debe actualizar al nombre: " + organigrama.getNomOrg());
                                actualizarNombreCarpeta(folderSon, organigrama.getNomOrg());
                            } else {
                                log.info("Organigrama --  El folder ya esta creado: " + organigrama.getNomOrg());
                            }
                        }
                        folderFather = folderSon;
                        folderFatherContainer = folderSon;
                        bandera++;

                    }
                //Recorremos la lista de Dependencias TRD
                for (ContenidoDependenciaTrdDTO dependencias : trdList) {
                    String[] dependenciasArray = {dependencias.getIdOrgAdm(),
                            dependencias.getIdOrgOfc(),
                            dependencias.getCodSerie(),
                            dependencias.getNomSerie(),
                            dependencias.getCodSubSerie(),
                            dependencias.getNomSubSerie(),
                    };
                    String nombreSerie = formatearNombre(dependenciasArray, "formatoNombreSerie");
                    folderSon = chequearCapetaPadre(folderFatherContainer, dependencias.getCodSerie());
                    if (ObjectUtils.isEmpty(folderSon)) {
                        if (!ObjectUtils.isEmpty(nombreSerie)) {
                            log.info("TRD --  Creando folder: " + nombreSerie);
                            folderSon = crearCarpeta(folderFatherContainer, nombreSerie, dependencias.getCodSerie(), ConstantesECM.CLASE_SERIE, folderFather, dependencias.getIdOrgOfc());
                        } else {
                            log.info("El formato para el nombre de la serie no es valido.");
                            break;
                        }
                    } else {
                        //Actualización de folder
                        if (!(nombreSerie.equals(folderSon.getFolder().getName()))) {
                            log.info("Se debe cambiar el nombre: " + nombreSerie);
                            actualizarNombreCarpeta(folderSon, nombreSerie);
                        } else {
                            log.info("TRD --  El folder ya esta creado: " + nombreSerie);
                        }
                    }
                    folderFather = folderSon;
                    if (!ObjectUtils.isEmpty(dependencias.getCodSubSerie())) {
                        String nombreSubserie = formatearNombre(dependenciasArray, "formatoNombreSubserie");
                        folderSon = chequearCapetaPadre(folderFather, dependencias.getCodSubSerie());
                        if (ObjectUtils.isEmpty(folderSon)) {
                            if (!ObjectUtils.isEmpty(nombreSubserie)) {
                                log.info("TRD --  Creando folder: " + nombreSubserie);
                                folderSon = crearCarpeta(folderFather, nombreSubserie, dependencias.getCodSubSerie(), ConstantesECM.CLASE_SUBSERIE, folderFather, dependencias.getIdOrgOfc());
                            } else {
                                log.info("El formato para el nombre de la subserie no es valido.");
                                break;
                            }
                        } else {

                            //Actualización de folder
                            if (!(nombreSubserie.equals(folderSon.getFolder().getName()))) {
                                log.info("Se debe cambiar el nombre: " + nombreSubserie);
                                actualizarNombreCarpeta(folderSon, nombreSubserie);
                            } else {
                                log.info("TRD --  El folder ya esta creado: " + nombreSubserie);
                            }
                        }
                        folderFather = folderSon;
                    }
                }
                bandera = 0;
                response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
                response.setMensaje("OK");
            }
            return response;
        } catch (Exception e) {
            log.error("An error has occurred {}", e);
            throw new SystemException("An error has occurred {}" + e);
        }
    }

    /**
     * Metodo para obtener documentos asociados a un documento principal en Alfresco
     *
     * @param documento DTO que contiene los metadatos el documento que se va a buscar
     * @return Devuelve el listado de documentos asociados al id de documento padre
     */
    @Override
    public MensajeRespuesta obtenerDocumentosAdjuntos(DocumentoDTO documento) throws SystemException {

        log.info("Se entra al metodo obtenerDocumentosAdjuntos");

        MensajeRespuesta response = new MensajeRespuesta();
        try {
            ItemIterable<QueryResult> resultsPrincipalAdjunto = getPrincipalAdjuntosQueryResults(documento);

            List<DocumentoDTO> documentosLista = new ArrayList<>();
            for (QueryResult qResult : resultsPrincipalAdjunto) {

                DocumentoDTO documentoDTO = new DocumentoDTO();

                String idDocumento = qResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);

                documentoDTO.setIdDocumento(idDocumento);
                if (qResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL) != null) {
                    documentoDTO.setIdDocumentoPadre(documento.getIdDocumento());
                    documentoDTO.setTipoPadreAdjunto(qResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_TIPO_DOCUMENTO).toString());
                } else {
                    documentoDTO.setTipoPadreAdjunto(DocumentType.MAIN.getType());
                }
                final Session session = getSession();
                Document document = (Document) session.getObject(idDocumento);
                documentoDTO.setNombreDocumento(qResult.getPropertyValueByQueryName(PropertyIds.NAME));
                GregorianCalendar newGregCal = qResult.getPropertyValueByQueryName(PropertyIds.CREATION_DATE);
                documentoDTO.setFechaCreacion(newGregCal.getTime());
                documentoDTO.setTipologiaDocumental(qResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_DOC_TIPO_DOCUMENTAL));
                documentoDTO.setTipoDocumento(qResult.getPropertyValueByQueryName(PropertyIds.CONTENT_STREAM_MIME_TYPE).toString());
                documentoDTO.setTamano(qResult.getPropertyValueByQueryName(PropertyIds.CONTENT_STREAM_LENGTH).toString());
                documentoDTO.setVersionLabel(document.getVersionLabel());

                documentoDTO.setDocumento(getDocumentBytes(document.getContentStream()));
                String nroRadicado = qResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_DOC_RADICADO);
                if (ObjectUtils.isEmpty(nroRadicado))
                    nroRadicado = "";
                documentoDTO.setNroRadicado(nroRadicado);
                documentoDTO.setNombreRemitente(qResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_DOC_REMITENTE) != null ? qResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_DOC_REMITENTE).toString() : null);

                documentosLista.add(documentoDTO);
            }
            response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            response.setMensaje("OK");
            response.setDocumentoDTOList(documentosLista);
            log.info("Se sale del metodo obtenerDocumentosAdjuntos con respuesta: " + response.toString());
            return response;

        } catch (Exception e) {
            response.setCodMensaje(ConstantesECM.ERROR_COD_MENSAJE);
            throw new SystemException("Error en la obtención de los documentos adjuntos: " + e);
        }
    }

    /**
     * Metodo para obtener versiones del documento
     *
     * @param idDoc Id Documento que se va pedir versiones
     * @return Devuelve el listado de versiones asociados al id de documento
     */
    @Override
    public MensajeRespuesta obtenerVersionesDocumento(String idDoc) throws SystemException {

        log.info("Se entra al metodo obtenerVersionesDocumento");

        MensajeRespuesta response = new MensajeRespuesta();

        ArrayList<DocumentoDTO> versionesLista = new ArrayList<>();
        try {
            final Session session = getSession();
            //Obtener documento dado id
            Document doc = (Document) session.getObject(idDoc);
            List<Document> versions = doc.getAllVersions();
            for (Document version : versions) {
                DocumentoDTO documentoDTO = new DocumentoDTO();
                documentoDTO.setNombreDocumento(version.getName());
                documentoDTO.setVersionLabel(version.getVersionLabel());
                documentoDTO.setTamano(String.valueOf(version.getContentStreamLength()));
                documentoDTO.setIdDocumento(idDoc);
                documentoDTO.setTipoDocumento(version.getContentStreamMimeType());
                versionesLista.add(documentoDTO);
            }
            response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            response.setMensaje("OK");
            response.setDocumentoDTOList(versionesLista);
            return response;
        } catch (Exception e) {
            log.error("Error en la obtención de las versiones del documento: ", e);
            throw new SystemException("Error en la obtención de las versiones del documento: " + e.getMessage());
        }
    }

    /**
     * Metodo para subir documentos al Alfresco
     *
     * @param documento Objeto que contiene los metadatos del documento.
     * @param selector  Selector que dice donde se va a gauardar el documento
     * @return Devuelve el id de la carpeta creada
     */
    @Override
    public MensajeRespuesta subirDocumentoPrincipalAdjunto(DocumentoDTO documento, String selector) throws SystemException {
        log.info("Se entra al metodo subirDocumentoPrincipalAdjunto");
        try {
            final String idDocPrincipal = documento.getIdDocumentoPadre();
            if (StringUtils.isEmpty(idDocPrincipal)) {
                DocumentoDTO dto;
                final String nroRadicado = documento.getNroRadicado();
                if (!StringUtils.isEmpty(selector) && "PD".equals(selector.toUpperCase())) {
                    documento.setNombreProceso(CommunicationType.PD.getProcessName());
                    dto = subirDocumentoPrincipalPD(documento);
                } else {
                    if (StringUtils.isEmpty(nroRadicado)) {
                        throw new SystemException("No se ha especificado el numero de radicado");
                    }
                    CommunicationType communicationType = CommunicationType.getSelectorBy(nroRadicado);
                    if (null == communicationType) {
                        throw new SystemException("El selector no valido '" + nroRadicado + "'");
                    }
                    documento.setNombreProceso(communicationType.getProcessName());
                    dto = subirDocumentoPrincipalRadicacion(documento, communicationType);
                }
                final List<DocumentoDTO> documentoDTOS = new ArrayList<>();
                documentoDTOS.add(documentoDTOS.size(), dto);
                reporteContent.processReporte7(dto.getCodigoDependencia());
                return MensajeRespuesta.newInstance()
                        .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                        .mensaje("Documento añadido correctamente")
                        .documentoDTOList(documentoDTOS)
                        .build();
            }
            return subirDocumentoAnexo(documento);
        } catch (Exception ex) {
            log.error("An Exception has occurred {}", ex.getMessage());
            throw new SystemException("Error del Sistema " + ex.getMessage());
        }
    }

    /**
     * Metodo para crear Link a un documento dentro de la carpeta Documentos de apoyo
     *
     * @param documento Objeto qeu contiene los datos del documento
     * @return Mensaje de respuesta
     */
    @Override
    public MensajeRespuesta crearLinkDocumentosApoyo(DocumentoDTO documento) {
        log.info("Se entra al metodo crearLinkDocumentosApoyo");

        MensajeRespuesta response = new MensajeRespuesta();

        Map<String, Object> properties = new HashMap<>();

        buscarCrearCarpeta(documento, response, documento.getDocumento(), properties, ConstantesECM.DOCUMENTOS_APOYO);

        log.info("Se sale del metodo crearLinkDocumentosApoyo");
        return response;
    }

    @Override
    public List<Document> getEcmDocumentsFromFolder(Folder folder) {
        final List<Document> documents = new ArrayList<>();
        if (folder == null) {
            log.error("El Folder introducido es null");
            return documents;
        }
        final ItemIterable<CmisObject> children = folder.getChildren();
        for (CmisObject cmisObject : children) {
            final String ecmTypeString = cmisObject.getType().getId();
            if (cmisObject instanceof Document && (ecmTypeString.startsWith("D:cm"))) {
                documents.add(documents.size(), (Document) cmisObject);
            }
        }
        return documents;
    }

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param folder Obj ECm
     * @return List<DocumentoDTO> Lista de los documentos de la carpeta
     */
    @Override
    public List<DocumentoDTO> getDocumentsFromFolder(Folder folder) throws SystemException {

        final List<Document> documents = getEcmDocumentsFromFolder(folder);
        final List<DocumentoDTO> documentoDTOS = new ArrayList<>();

        final String[] serieSubSerie = getSerieSubSerie(folder);
        final String udName = folder.getName();
        final String udId = folder.getPropertyValue(ConstantesECM.CMCOR_UD_ID);
        final String depCode = folder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);

        String depName = "";
        if (!StringUtils.isEmpty(depCode)) {
            Folder folderBy = getFolderBy(ConstantesECM.CLASE_DEPENDENCIA, ConstantesECM.CMCOR_DEP_CODIGO, depCode);
            depName = folderBy.getName();
        }
        for (Document document : documents) {
            final DocumentoDTO documentoDTO = transformarDocumento(document, true);
            documentoDTO.setSerie(serieSubSerie[0]);
            documentoDTO.setSubSerie(serieSubSerie[1]);
            documentoDTO.setIdUnidadDocumental(udId);
            documentoDTO.setCodigoDependencia(depCode);
            documentoDTO.setDependencia(depName);
            documentoDTO.setNombreUnidadDocumental(udName);
            documentoDTOS.add(documentoDTOS.size(), documentoDTO);
        }
        return documentoDTOS;
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO Obj Unidad Documental
     * @return MensajeRespuesta       Unidad Documental
     */
    @Override
    public MensajeRespuesta subirDocumentosUnidadDocumentalECM(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("Se entra al metodo subirDocumentosUnidadDocumentalECM");
        if (ObjectUtils.isEmpty(unidadDocumentalDTO) || StringUtils.isEmpty(unidadDocumentalDTO.getId())) {
            throw new SystemException("No se ha identificado el id de la Unidad Documental");
        }
        try {
            final Optional<Folder> optionalFolder = getUDFolderById(unidadDocumentalDTO.getId());
            if (!optionalFolder.isPresent()) {
                throw new SystemException("No existe la unidad documental con id " + unidadDocumentalDTO.getId());
            }
            final Folder targetFolder = optionalFolder.get();
            final List<DocumentoDTO> listaDocumentos = unidadDocumentalDTO.getListaDocumentos();
            String codigoDependencia = targetFolder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);
            if (ObjectUtils.isEmpty(listaDocumentos)) {
                throw new SystemException("No se han especificado los documentos para archivar en la Unidad Documental");
            }
            for (DocumentoDTO documentoDTO : listaDocumentos) {
                final String docId = documentoDTO.getIdDocumento();
                final String docName = documentoDTO.getNombreDocumento();
                if (StringUtils.isEmpty(docId)) {
                    throw new SystemException("No se ha especificado el ID DOC ECM para el documento " + docName);
                }
                final Session session = getSession();
                final CmisObject documentObj = session.getObject(docId);
                if (documentObj.getType().getId().startsWith("D:cm")) {
                    final Document document = (Document) documentObj;
                    final Folder sourceFolder = getFolderFrom(document);
                    if (null != sourceFolder) {
                        if (!document.getName().equals(docName)) {
                            document.rename(docName);
                        }
                        document.move(sourceFolder, targetFolder);

                        final Map<String, Object> properties = new HashMap<>();
                        properties.put(ConstantesECM.CMCOR_DOC_ID_UD, unidadDocumentalDTO.getId());
                        properties.put(ConstantesECM.CMCOR_DOC_TIPO_DOCUMENTAL, documentoDTO.getTipologiaDocumental());
                        document.updateProperties(properties);

                        properties.clear();
                        Calendar startDate = targetFolder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_INICIAL);
                        if (ObjectUtils.isEmpty(startDate)) {
                            properties.put(ConstantesECM.CMCOR_UD_FECHA_INICIAL, GregorianCalendar.getInstance());
                        }
                        properties.put(ConstantesECM.CMCOR_UD_FECHA_FINAL, GregorianCalendar.getInstance());
                        targetFolder.updateProperties(properties);
                    }
                }
            }
            unidadDocumentalDTO = transformarUnidadDocumental(targetFolder);
            final List<DocumentoDTO> documentsFromFolder = getDocumentsFromFolder(targetFolder);
            unidadDocumentalDTO.setListaDocumentos(documentsFromFolder);
            if (!StringUtils.isEmpty(codigoDependencia)) {
                reporteContent.processReporte7(codigoDependencia);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("unidadDocumental", unidadDocumentalDTO);
            return MensajeRespuesta.newInstance()
                    .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                    .response(response)
                    .mensaje("Operacion realizada satisfactoriamente").build();
        } catch (Exception e) {
            log.error("Error al subir los documentos a la unidad documental");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * Metodo para subir/versionar documentos al Alfresco
     *
     * @param documento Documento que se va a subir/versionar
     * @param selector  parametro que indica donde se va a guardar el documento
     * @return Devuelve el id de la carpeta creada
     */
    @Override
    public MensajeRespuesta subirVersionarDocumentoGenerado(DocumentoDTO documento, String selector) throws SystemException {
        log.info("Se entra al metodo subirVersionarDocumentoGenerado");
        MensajeRespuesta response = new MensajeRespuesta();
        List<DocumentoDTO> documentoDTOList = new ArrayList<>();
        Map<String, Object> properties = new HashMap<>();

        byte[] bytes = documento.getDocumento();
        if ("html".equals(documento.getTipoDocumento())) {
            documento.setTipoDocumento(MimeTypes.getMIMEType("html"));
        } else {
            documento.setTipoDocumento(MimeTypes.getMIMEType("pdf"));
        }

        if (documento.getIdDocumento() == null) {
            return subirDocumentoPrincipalAdjunto(documento, selector);
        } else {
            //Obtener documento dado id
            final Session session = getSession();
            Document doc = (Document) session.getObject(documento.getIdDocumento());
            properties.put(PropertyIds.NAME, documento.getNombreDocumento());
            properties.put(PropertyIds.CONTENT_STREAM_FILE_NAME, documento.getNombreDocumento());
            doc.updateProperties(properties, true);

            //Obtener el PWC (Private Working copy)
            Document pwc = (Document) session.getObject(doc.checkOut());

            ContentStream contentStream = new ContentStreamImpl(documento.getNombreDocumento(), BigInteger.valueOf(bytes.length), documento.getTipoDocumento(), new ByteArrayInputStream(bytes));
            // Check in the pwc
            try {
                pwc.checkIn(false, properties, contentStream, "nueva version");
                Document docAux = (Document) session.getObject(documento.getIdDocumento());
                response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
                response.setMensaje("Documento versionado correctamente");

                documento.setVersionLabel(getLabelDocument(docAux.getVersionLabel(), true));
                documentoDTOList.add(documento);
                response.setDocumentoDTOList(documentoDTOList);
                log.info("Documento versionado correctamente con metadatos: " + documento);
            } catch (CmisBaseException e) {
                log.error("checkin failed, trying to cancel the checkout", e);
                pwc.cancelCheckOut();
                response.setCodMensaje("222222");
                response.setMensaje("Error versionando documento: " + e);
            }
        }
        return response;
    }

    /**
     * Metodo para modificar metadatos del documento de Alfresco
     *
     * @param dto Obj DocumentoDTO con las modificaciones
     */
    @Override
    public MensajeRespuesta modificarMetadatosDocumento(DocumentoDTO dto) throws SystemException {

        final String idEcm = dto.getIdDocumento();

        if (StringUtils.isEmpty(idEcm)) {
            throw new SystemException("Especifique idEcm");
        }
        final MensajeRespuesta response = new MensajeRespuesta();
        response.setMensaje(ConstantesECM.SUCCESS);
        response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);

        try {
            final Session session = getSession();
            final CmisObject object = session.getObject(idEcm);
            if (!(object instanceof Document)) {
                throw new SystemException("ID: " + idEcm + " no es un ID de Documento");
            }

            final String nroRad = dto.getNroRadicado();
            if (!StringUtils.isEmpty(nroRad)) {
                final Map<String, Object> anexMap = new HashMap<>();
                getAnexoListBy(idEcm).parallelStream().forEach(anexDocument -> {
                    anexMap.put(ConstantesECM.CMCOR_DOC_RADICADO, nroRad);
                    anexDocument.updateProperties(anexMap);
                });
            }
            final Map<String, Object> propertyMap = getPropertyMapFrom(dto);
            if (!propertyMap.isEmpty()) {
                final Document mainDocument = (Document) object;
                mainDocument.updateProperties(propertyMap);
            }
            return response;

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new SystemException(ex.getMessage());
        }

        /*final MensajeRespuesta response = new MensajeRespuesta();
        final Map<String, Object> idResponse = new HashMap<>();
        final Session session = getSession();
        idResponse.put("idECM", null);
        response.setCodMensaje(ConstantesECM.ERROR_COD_MENSAJE);
        response.setResponse(idResponse);
        if (ObjectUtils.isEmpty(dto)) {
            response.setMensaje("El documento introducido es nulo");
            return response;
        }
        final String idDocumento = dto.getIdDocumento();
        if (StringUtils.isEmpty(idDocumento)) {
            response.setMensaje("El ID del documento introducido es nulo");
            return response;
        }
        log.info("### Modificar documento: " + idDocumento);
        final String docName = dto.getNombreDocumento();
        if (!StringUtils.isEmpty(docName)) {
            try {
                final CmisObject object = session.getObject(idDocumento);
                if (!(object instanceof Document)) {
                    response.setMensaje("El id especificado no coincide con el de un documento");
                    return response;
                }
                final Map<String, Object> updateProperties = new HashMap<>();
                final String nroRadicado = dto.getNroRadicado();
                final CommunicationType communicationType = CommunicationType.getSelectorBy(nroRadicado);
                if (null == communicationType) {
                    response.setMensaje("El selector no es valido '" + nroRadicado + "' para un numero de radicado");
                    return response;
                }
                updateProperties.put(ConstantesECM.CMCOR_DOC_RADICADO, nroRadicado);
                updateProperties.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, idDocumento);
                final String docType = object.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO);
                if (DocumentType.ATTACHED.getType().equalsIgnoreCase(docType)) {
                    response.setMensaje("No se debe modificar el numero de radicado de un documento anexo");
                    return response;
                }
                final Folder sourceFolder = getFolderFrom((Document) object);
                if (null != sourceFolder) {
                    dto.setNroRadicado(null);
                    final Carpeta linkTargetFolder = buscarCarpetaRadicacion(communicationType);
                    final ItemIterable<QueryResult> principalAdjuntosQueryResults = getPrincipalAdjuntosQueryResults(dto);
                    for (QueryResult queryResult :
                            principalAdjuntosQueryResults) {
                        String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                        final Document document = (Document) session.getObject(objectId)
                                .updateProperties(updateProperties);
                        document.move(sourceFolder, linkTargetFolder.getFolder());
                        //crearLink(linkTargetFolder.getFolder(), document);
                    }
                }
                final String nombreRemitente = dto.getNombreRemitente();
                if (!StringUtils.isEmpty(nombreRemitente)) {
                    updateProperties.put(ConstantesECM.CMCOR_DOC_REMITENTE, nombreRemitente);
                }
                final String tipologiaDocumental = dto.getTipologiaDocumental();
                if (!StringUtils.isEmpty(tipologiaDocumental)) {
                    updateProperties.put(ConstantesECM.CMCOR_DOC_TIPO_DOCUMENTAL, tipologiaDocumental);
                }
                updateProperties.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, "");
                CmisObject cmisObject = object.updateProperties(updateProperties);
                log.info("### Modificados los metadatos de correctamente");
                updateProperties.clear();
                updateProperties.put("idECM", cmisObject.getId());
                response.setMensaje("OK");
                response.setResponse(updateProperties);
                response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
                return response;

            } catch (Exception e) {
                log.error("*** Error al modificar el documento *** ", e);
                response.setMensaje(e.getMessage());
                return response;
            }
        }
        dto.setIdDocumento(null);
        if (!StringUtils.isEmpty(dto.getNroRadicado())) {
            final ItemIterable<QueryResult> principalAdjuntosQueryResults = getPrincipalAdjuntosQueryResults(dto);
            for (QueryResult queryResult : principalAdjuntosQueryResults) {
                String objectId = queryResult.getPropertyValueById(PropertyIds.OBJECT_ID);
                CmisObject cmisObject = session.getObject(session.createObjectId(objectId));
                Document document = (Document) cmisObject;
                String docType = document.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO);
                if (!StringUtils.isEmpty(docType) && DocumentType.MAIN.getType().equalsIgnoreCase(docType.toLowerCase())) {
                    dto.setIdDocumento(document.getId());
                    break;
                }
            }
        }
        response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
        response.getResponse().put("idECM", dto.getIdDocumento());
        response.setMensaje(ConstantesECM.SUCCESS);
        return response;*/
    }

    private Document getDocumentBy(String nroRad) throws SystemException {
        final String query = "select * from " + PropertyIds.OBJECT_ID + " FROM cmcor:CM_Unidad_Documental" +
                " where " + ConstantesECM.CMCOR_DOC_RADICADO + " = '" + nroRad + "'" +
                " AND " + ConstantesECM.CMCOR_TIPO_DOCUMENTO + " = '" + DocumentType.MAIN.getType() + "'";
        final Session session = getSession();
        final Iterator<QueryResult> iterator = session.query(query, false).iterator();
        if (iterator.hasNext()) {
            final String objectId = iterator.next().getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            return (Document) session.getObject(objectId);
        }
        return null;
    }

    private List<Document> getAnexoListBy(String idEcmPadre) throws SystemException {
        String query = "select * from " + PropertyIds.OBJECT_ID + " FROM cmcor:CM_Unidad_Documental" +
                " where " + ConstantesECM.CMCOR_TIPO_DOCUMENTO + " <> '" + DocumentType.MAIN.getType() + "'" +
                " And " + ConstantesECM.CMCOR_ID_DOC_PRINCIPAL + " = '" + idEcmPadre + "'";

        final Session session = getSession();
        final List<Document> docList = new ArrayList<>();
        for (QueryResult queryResult : session.query(query, false)) {
            final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            docList.add(docList.size(), (Document) session.getObject(objectId));
        }
        return docList;
    }

    private Map<String, Object> getPropertyMapFrom(DocumentoDTO dto) {
        final Map<String, Object> propertyMap = new HashMap<>();
        if (!StringUtils.isEmpty(dto.getActuacion())) {
            propertyMap.put(ConstantesECM.CMCOR_DOC_ACTUACION, dto.getActuacion());
        }
        if (!StringUtils.isEmpty(dto.getDocAutor())) {
            propertyMap.put(ConstantesECM.CMCOR_DOC_AUTOR, dto.getDocAutor());
        }
        if (!StringUtils.isEmpty(dto.getEvento())) {
            propertyMap.put(ConstantesECM.CMCOR_DOC_EVENTO, dto.getEvento());
        }
        if (!StringUtils.isEmpty(dto.getTramite())) {
            propertyMap.put(ConstantesECM.CMCOR_DOC_TRAMITE, dto.getTramite());
        }
        if (!StringUtils.isEmpty(dto.getIdDocumentoPadre())) {
            propertyMap.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, dto.getIdDocumentoPadre());
        }
        if (!StringUtils.isEmpty(dto.getIdUnidadDocumental())) {
            propertyMap.put(ConstantesECM.CMCOR_DOC_ID_UD, dto.getIdUnidadDocumental());
        }
        if (!StringUtils.isEmpty(dto.getNroRadicado())) {
            propertyMap.put(ConstantesECM.CMCOR_DOC_RADICADO, dto.getNroRadicado());
        }
        if (!StringUtils.isEmpty(dto.getTipologiaDocumental())) {
            propertyMap.put(ConstantesECM.CMCOR_DOC_TIPO_DOCUMENTAL, dto.getTipologiaDocumental());
        }
        if (!StringUtils.isEmpty(dto.getTipoPadreAdjunto())) {
            propertyMap.put(ConstantesECM.CMCOR_TIPO_DOCUMENTO, dto.getTipoPadreAdjunto());
        }
        if (!ObjectUtils.isEmpty(dto.getFechaRadicacion())) {
            propertyMap.put(ConstantesECM.CMCOR_FECHA_RADICACION, dto.getFechaRadicacion());
        }

        return propertyMap;
    }

    /**
     * Eliminar documento del Alfresco
     *
     * @param idDoc Identificador del documento a borrar
     */
    @Override
    public void eliminardocumento(String idDoc) throws SystemException {
        try {
            log.info("Se buscan los documentos Anexos al documento que se va a borrar");
            if (StringUtils.isEmpty(idDoc)) {
                throw new SystemException("No se especifico el ID del documento");
            }
            final Session session = getSession();
            CmisObject object = session.getObject(idDoc);
            Document delDoc = (Document) object;
            DocumentoDTO documentoDTO = transformarDocumento(delDoc, true);
            delDoc.delete(false);
            if (DocumentType.MAIN.getType().equalsIgnoreCase(documentoDTO.getTipoPadreAdjunto())) {
                eliminarDocumentosAnexos(idDoc);
            }

        } catch (CmisObjectNotFoundException e) {
            log.error("Documento no encontrado :", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception e) {
            log.error("No se pudo eliminar el documento :", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    @Override
    public MensajeRespuesta modificarUnidadesDocumentales(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException {
        log.info("Se procede a modificar las uniddes documentales");
        if (!ObjectUtils.isEmpty(unidadDocumentalDTOS)) {
            final List<UnidadDocumentalDTO> lista = new ArrayList<>();
            for (UnidadDocumentalDTO dto :
                    unidadDocumentalDTOS) {
                lista.add(lista.size(), getAndModificarUnidadDocumental(dto));
            }
            final MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setResponse(new HashMap<>());
            respuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            respuesta.setMensaje("Actualizacion realizada satisfactoriamente");
            respuesta.getResponse().put("unidadesDocumentales", lista);
            return respuesta;
        }
        throw new SystemException("No se ha introducido la unidad documental a modificar");
    }

    @Override
    public MensajeRespuesta modificarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        try {
            String topografica = unidadDocumentalDTO.getUbicacionTopografica();
            if (!StringUtils.isEmpty(topografica) && !"".equals(topografica.trim()) && !StringUtils.containsIgnoreCase("null", topografica)) {
                unidadDocumentalDTO.setSoporte(SupportType.HYBRID.getSupport());
            }
            super.getAndModificarUnidadDocumental(unidadDocumentalDTO);
            final MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            respuesta.setMensaje("Actualizacion realizada satisfactoriamente");
            return respuesta;
        } catch (Exception ex) {
            throw new SystemException("Error ocurrido " + ex.getMessage());
        }
    }

    @Override
    public MensajeRespuesta listarUnidadDocumentalECM(String idUd, String nombreUd) throws SystemException {
        final MensajeRespuesta respuesta = new MensajeRespuesta();
        try {
            final List<UnidadDocumentalDTO> unidadDocumentalDTOS = getListaUdByIdAndName(idUd, nombreUd);
            Map<String, Object> map = new HashMap<>();
            map.put("unidadDocumental", unidadDocumentalDTOS);
            respuesta.setResponse(map);
            respuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            respuesta.setMensaje(ConstantesECM.SUCCESS);
            return respuesta;
        } catch (Exception ex) {
            throw new SystemException("Error: " + ex.getMessage());
        }
    }

    @Override
    public MensajeRespuesta subirDocumentosTemporalesUD(List<DocumentoDTO> documentoDTOS) throws SystemException {
        log.info("Se procede a modificar las uniddes documentales");
        if (ObjectUtils.isEmpty(documentoDTOS)) {
            throw new SystemException("La lista de documentos esta vacia");
        }
        final Optional<String> stringOptional = documentoDTOS.stream()
                .filter(documentoDTO -> !StringUtils.isEmpty(documentoDTO.getCodigoDependencia()))
                .map(DocumentoDTO::getCodigoDependencia)
                .findFirst();

        if (stringOptional.isPresent()) {
            try {
                final List<DocumentoDTO> dtos = new ArrayList<>();
                final String codigoDependencia = stringOptional.get();
                final Optional<Folder> optionalFolder = getDocumentoPorArchivarFolder(codigoDependencia);

                if (optionalFolder.isPresent()) {
                    final Folder folder = optionalFolder.get();
                    for (DocumentoDTO documentoDTO :
                            documentoDTOS) {
                        documentoDTO.setTipoPadreAdjunto(DocumentType.MAIN.getType());
                        final Optional<DocumentoDTO> optionalDocumentoDTO = subirDocumentoDtoTemp(folder, documentoDTO);
                        optionalDocumentoDTO.ifPresent(documentoDTO1 -> dtos.add(dtos.size(), documentoDTO1));
                    }
                    reporteContent.processReporte7(codigoDependencia);
                }
                return MensajeRespuesta.newInstance()
                        .documentoDTOList(dtos)
                        .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                        .mensaje(ConstantesECM.SUCCESS)
                        .build();
            } catch (Exception ex) {
                log.error("An error has occurred in subirDocumentosTemporalesUD method from ContentControlAlfresco {}", ex.getMessage());
                throw new SystemException("An error has occurred in subirDocumentosTemporalesUD method from ContentControlAlfresco " + ex.getMessage());
            }
        }
        throw new SystemException("No se ha especificado el codigo de la dependencia");
    }

    /**
     * Operacion para Subir documentos a una UD temporal ECM
     *
     * @param documentoDTO Obj de documento DTO a archivar
     * @return MensajeRespuesta
     */
    @Override
    public MensajeRespuesta subirDocumentoTemporalUD(DocumentoDTO documentoDTO) throws SystemException {
        log.info("processing rest request - Subir Documento temporal ECM");
        if (ObjectUtils.isEmpty(documentoDTO)) {
            throw new SystemException("El documento es nulo");
        }
        final String codigoDependencia = documentoDTO.getCodigoDependencia();
        try {
            if (!StringUtils.isEmpty(codigoDependencia)) {
                final Optional<Folder> optionalFolder = getDocumentoPorArchivarFolder(codigoDependencia);
                if (optionalFolder.isPresent()) {
                    final Folder folder = optionalFolder.get();
                    final Optional<DocumentoDTO> optionalDocumentoDTO = subirDocumentoDtoTemp(folder, documentoDTO);
                    if (optionalDocumentoDTO.isPresent()) {
                        documentoDTO = optionalDocumentoDTO.get();
                        final Map<String, Object> mapResponse = new HashMap<>();
                        mapResponse.put("documento", documentoDTO);
                        reporteContent.processReporte7(codigoDependencia);
                        return MensajeRespuesta.newInstance()
                                .response(mapResponse)
                                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                                .mensaje(ConstantesECM.SUCCESS)
                                .build();
                    }
                }
            }
            return MensajeRespuesta.newInstance()
                    .codMensaje(ConstantesECM.ERROR_COD_MENSAJE)
                    .mensaje("No se ha identificado el codigo de la dependencia")
                    .build();
        } catch (Exception ex) {
            log.error("An error has occurred in subirDocumentoTemporalUD method from ContentControlAlfresco {}", ex.getMessage());
            throw new SystemException("An error has occurred in subirDocumentoTemporalUD method from ContentControlAlfresco " + ex.getMessage());
        }
    }

    @Override
    public MensajeRespuesta obtenerDocumentosArchivados(String codigoDependencia) throws SystemException {
        log.info("Se procede a obtener los documentos archivados");
        if (StringUtils.isEmpty(codigoDependencia)) {
            throw new SystemException("No se ha especificado el codigo de la dependencia");
        }

        final List<Map<String, Object>> mapList = new ArrayList<>();
        final List<DocumentoDTO> documentsFromFolder = obtenerDocumentosArchivadosList(codigoDependencia);

        documentsFromFolder.stream().filter(documentoDTO -> {
            final DocumentType documentType = DocumentType.getDocumentTypeBy(documentoDTO.getTipoPadreAdjunto());
            return documentType == DocumentType.MAIN;
        }).forEach(documentoDTO -> {
            final Map<String, Object> item = new HashMap<>();
            final String serieName = documentoDTO.getSerie();
            final String subSerieName = documentoDTO.getSubSerie();
            item.put("id", documentoDTO.getIdUnidadDocumental());
            item.put("nombreUnidadDocumental", documentoDTO.getNombreUnidadDocumental());
            item.put("serieName", serieName);
            item.put("subSerieName", subSerieName);
            item.put("currentFatherName", StringUtils.isEmpty(subSerieName) ? serieName : subSerieName);
            item.put("fechaCreacion", documentoDTO.getFechaCreacion());
            item.put("idDocumento", StringUtils.isEmpty(documentoDTO.getNroRadicado()) ?
                    documentoDTO.getIdDocumento() : documentoDTO.getNroRadicado());
            item.put("tipologiaDocumental", documentoDTO.getTipologiaDocumental());
            item.put("nombreDocumento", documentoDTO.getNombreDocumento());
            item.put("nombreProceso", documentoDTO.getNombreProceso());

            mapList.add(mapList.size(), item);
        });
        reporteContent.processReporte7(codigoDependencia);
        final Map<String, Object> response = new HashMap<>();
        response.put("documentos", mapList);
        return MensajeRespuesta
                .newInstance()
                .response(response)
                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                .mensaje(ConstantesECM.SUCCESS).build();
    }

    @Override
    public MensajeRespuesta listarDependenciaMultiple(ContenidoDependenciaTrdDTO dependenciaTrdDTO) throws SystemException {
        log.info("Se procede a listar todas las dependencias");
        final MensajeRespuesta respuesta = MensajeRespuesta.newInstance()
                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                .mensaje(ConstantesECM.SUCCESS)
                .build();
        final Map<String, Object> mapResponse = new HashMap<>();

        try {
            if (!ObjectUtils.isEmpty(dependenciaTrdDTO)) {

                final String depCode = dependenciaTrdDTO.getIdOrgOfc();
                final String serieCode = dependenciaTrdDTO.getCodSerie();
//                final String serieName = dependenciaTrdDTO.getNomSerie();

                final ContenidoDependenciaTrdDTO trdDTO = getDependenciaTrdDTO(depCode, serieCode);


//                final ContenidoDependenciaTrdDTO trdDTO = getDependenciaTrdDTO(depFolder, serieCode, serieName);

                mapResponse.put("series", trdDTO.getListaSerie());
                mapResponse.put("subSeries", trdDTO.getListaSubSerie());
                mapResponse.put("dependencias", trdDTO.getListaDependencia());

            } else {
                mapResponse.put("series", new ArrayList<>());
                mapResponse.put("subSeries", new ArrayList<>());
                mapResponse.put("dependencias", new ArrayList<>());
            }
            mapResponse.put("sedes", getSedeList());
            respuesta.setResponse(mapResponse);
            return respuesta;

        } catch (Exception e) {
            log.error("Ocurrio un error al listar todas las dependencias");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    @Override
    public MensajeRespuesta transformaEstampaPd(DocumentoDTO documentoDTO) throws SystemException {
        log.info("processing rest request - Estampar la etiquta de radicacion");
        try {
            if(transformaEstampaRad(documentoDTO)){
                final Map<String, Object> mapResponse = new HashMap<>();
                mapResponse.put("documentoDto", documentoDTO);
                return MensajeRespuesta.newInstance()
                        .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                        .mensaje("Imagen guardada satisfactoriamente")
                        .response(mapResponse)
                        .build();
            }else{
                final Map<String, Object> mapResponse = new HashMap<>();
                mapResponse.put("documentoDto", documentoDTO);
                return MensajeRespuesta.newInstance()
                        .codMensaje(ConstantesECM.FAIL_RADICADO_COD_MENSAJE)
                        .mensaje("No se pudo firmar el documento, vuelva a intentarlo, y/o consulte con el administrador")
                        .response(mapResponse)
                        .build();
            }



        } catch (Exception e) {
            log.info("Ocurrio un error al estampar la etiqueta");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    @Override
    public MensajeRespuesta subirDocumentoAnexo(DocumentoDTO documento) throws SystemException {
        if (ObjectUtils.isEmpty(documento)) {
            throw new SystemException("No se ha especificado el DocumentoDTO");
        }
        final byte[] bytes = documento.getDocumento();
        if (ObjectUtils.isEmpty(bytes)) {
            throw new SystemException("No se ha especificado el contenido del documento");
        }
        final String idDocPincipal = documento.getIdDocumentoPadre();
        if (StringUtils.isEmpty(idDocPincipal)) {
            throw new SystemException("No se ha especificado el ID del documento Principal");
        }
        final String nombreDoc = documento.getNombreDocumento();
        if (StringUtils.isEmpty(nombreDoc)) {
            throw new SystemException("No se ha especificado el nombre del documento");
        }
        try {
            final Session session = getSession();
            ObjectId objectId = new ObjectIdImpl(idDocPincipal);
            CmisObject cmisObject = session.getObject(objectId);
            Document document = (Document) cmisObject;
            final DocumentoDTO docPrincipal = transformarDocumento(document, true);
            final String docType = docPrincipal.getTipoPadreAdjunto();
            if (StringUtils.isEmpty(docType) || !docType.equalsIgnoreCase(DocumentType.MAIN.getType())) {
                throw new SystemException("El id proporcionado no coincide con el de un documento principal");
            }
            final String documentMimeType = StringUtils.isEmpty(documento.getTipoDocumento()) ?
                    MimeTypes.getMIMEType("pdf") : documento.getTipoDocumento();
            final ContentStream contentStream = new ContentStreamImpl(nombreDoc,
                    BigInteger.valueOf(bytes.length), documentMimeType, new ByteArrayInputStream(bytes));
            //final String nroRadicado
            final Folder folder = getFolderFrom(document);
            final Map<String, Object> properties = new HashMap<>();
            properties.put(PropertyIds.NAME, nombreDoc);
            properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
            properties.put(ConstantesECM.CMCOR_TIPO_DOCUMENTO, DocumentType.ATTACHED.getType());
            properties.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, idDocPincipal);
            properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, documentMimeType);
            properties.put(ConstantesECM.CMCOR_DOC_RADICADO, docPrincipal.getNroRadicado());
            properties.put(ConstantesECM.CMCOR_DOC_REMITENTE, documento.getNombreRemitente());
            if (null != folder) {
                properties.put(ConstantesECM.CMCOR_DOC_ID_UD, folder.getPropertyValue(ConstantesECM.CMCOR_UD_ID));
                document = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
                if (null != document) {
                    final List<DocumentoDTO> response = new ArrayList<>();
                    response.add(response.size(), transformarDocumento(document, true));
                    return MensajeRespuesta.newInstance()
                            .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                            .mensaje("Operacion completada satisfactoriamente")
                            .documentoDTOList(response)
                            .build();
                }
            }
            throw new SystemException("Ocurrio un error al anezar el documento");

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

    @Override
    public void formatoListaUnidadDocumental(List<UnidadDocumentalDTO> unidadDocumentalDTOS) {
        unidadDocumentalDTOS.forEach(unidadDocumentalDTO -> {
            final String idUnidadDocumental = unidadDocumentalDTO.getId();
            final Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental);
            optionalFolder.ifPresent(folder -> {
                final String[] serieSubserie = getSerieSubSerie(optionalFolder.get());

                String serieName = serieSubserie[0];
                int index = serieName.indexOf('_');
                serieName = index != -1 ? serieName.substring(index + 1) : serieName;

                String subSerieName = serieSubserie[1];
                index = subSerieName.indexOf('_');
                subSerieName = index != -1 ? subSerieName.substring(index + 1) : subSerieName;

                unidadDocumentalDTO.setNombreSerie(serieName);
                unidadDocumentalDTO.setNombreSubSerie(subSerieName);
                final FinalDispositionType type = FinalDispositionType.getDispositionBy(unidadDocumentalDTO.getDisposicion());
                if (null != type) {
                    unidadDocumentalDTO.setDisposicion(type.name());
                }
            });
        });
    }

    @Override
    public MensajeRespuesta transformarDocumento(String tipoPlantilla, String htmlContent) throws SystemException {
        final TemplateType templateType = TemplateType.getTemplateNameBy(tipoPlantilla);
        if (templateType == null) {
            throw new SystemException("No se Procesa tipo de Plantilla '" + tipoPlantilla + "', solucion (oficio, memorando)");
        }
        if (StringUtils.isEmpty(htmlContent)) {
            throw new SystemException("No hay Informacion para procesar");
        }
        byte[] bytes = transformHtmlToPdf(templateType, htmlContent);
        final Map<String, Object> response = new HashMap<>();
        response.put("documento", bytes);
        return MensajeRespuesta.newInstance()
                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                .mensaje(ConstantesECM.OPERACION_COMPLETADA_SATISFACTORIAMENTE)
                .response(response).build();
    }

    @Override
    public Map<String, Object> obtenerPropiedadesDocumento(Document document) {

        final Map<String, Object> map = new HashMap<>();
        map.put(PropertyIds.NAME, document.getName());
        map.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
        map.put(PropertyIds.OBJECT_ID, document.getId());

        map.put(ConstantesECM.CMCOR_DOC_REMITENTE, document.getPropertyValue(ConstantesECM.CMCOR_DOC_REMITENTE));
        map.put(ConstantesECM.CMCOR_DOC_RADICADO, document.getPropertyValue(ConstantesECM.CMCOR_DOC_RADICADO));
        map.put(ConstantesECM.CMCOR_DOC_TIPO_DOCUMENTAL, document.getPropertyValue(ConstantesECM.CMCOR_DOC_TIPO_DOCUMENTAL));
        map.put(ConstantesECM.CMCOR_DOC_ACTUACION, document.getPropertyValue(ConstantesECM.CMCOR_DOC_ACTUACION));
        map.put(ConstantesECM.CMCOR_DOC_DEP_ORIGEN, document.getPropertyValue(ConstantesECM.CMCOR_DOC_DEP_ORIGEN));
        map.put(ConstantesECM.CMCOR_DOC_EVENTO, document.getPropertyValue(ConstantesECM.CMCOR_DOC_EVENTO));
        map.put(ConstantesECM.CMCOR_NOMBRE_PROCESO, document.getPropertyValue(ConstantesECM.CMCOR_NOMBRE_PROCESO));
        map.put(ConstantesECM.CMCOR_DOC_TRAMITE, document.getPropertyValue(ConstantesECM.CMCOR_DOC_TRAMITE));
        map.put(ConstantesECM.CMCOR_FECHA_RADICACION, document.getPropertyValue(ConstantesECM.CMCOR_FECHA_RADICACION));
        map.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, document.getPropertyValue(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL));
        map.put(ConstantesECM.CMCOR_TIPO_DOCUMENTO, document.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO));
        map.put(ConstantesECM.CMCOR_NUMERO_REFERIDO, document.getPropertyValue(ConstantesECM.CMCOR_NUMERO_REFERIDO));
        return map;
    }

    @Override
    public MensajeRespuesta getDocumentosPorArchivar(final String codigoDependencia) throws SystemException {
        log.info("Se buscan los documentos por Archivar");

        List<DocumentoDTO> docsXArchivar = getDocumentosPorArchivarList(codigoDependencia);
        reporteContent.processReporte7(codigoDependencia);
        return MensajeRespuesta.newInstance()
                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE).documentoDTOList(docsXArchivar)
                .mensaje("Listado devuelto satisfactoriamente").build();
    }

    @Override
    public MensajeRespuesta eliminarUnidadDocumental(String idUnidadDocumental) throws SystemException {
        recordServices.eliminarRecordFolder(idUnidadDocumental);
        Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental);
        if (optionalFolder.isPresent()) {
            Folder folder = optionalFolder.get();
            folder.deleteTree(true, UnfileObject.DELETE, true);
            return MensajeRespuesta.newInstance()
                    .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                    .mensaje(ConstantesECM.SUCCESS)
                    .build();
        } else {
            return MensajeRespuesta.newInstance()
                    .codMensaje(ConstantesECM.ERROR_COD_MENSAJE)
                    .mensaje("Ocurrio un error al eliminar la unidad documental '" + idUnidadDocumental + "'")
                    .build();
        }
    }
}