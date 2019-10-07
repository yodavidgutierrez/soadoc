package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Creado por Dasiel
 */
public interface ContentControl extends Connection {

    /**
     * Generar estructura
     *
     * @param estructuraList lista de estructura
     * @param folder         carpeta padre
     * @return mensaje respuesta
     */
    MensajeRespuesta generarArbol(List<EstructuraTrdDTO> estructuraList, Carpeta folder) throws SystemException;

    /**
     * Subir documento Principal Adjuntos al ECM
     *
     * @param documentoDTO Objeto qeu contiene los metadatos de los documentos ECM
     * @param selector     Selector que dice donde se va a gauardar el documento
     * @return ide de documento
     */
    MensajeRespuesta subirDocumentoPrincipalAdjunto(DocumentoDTO documentoDTO, String selector) throws SystemException;

    /**
     * Metodo para crear Link a un documento dentro de la carpeta Documentos de apoyo
     *
     * @param documento Objeto que contiene los datos del documento
     */
    MensajeRespuesta crearLinkDocumentosApoyo(DocumentoDTO documento);

    /**
     * Subir Versionar documento Generado al ECM
     *
     * @param documento documento a subir/versionar
     * @param selector  parametro que indica donde se va a guardar el documento
     * @return ide de documento
     */
    MensajeRespuesta subirVersionarDocumentoGenerado(DocumentoDTO documento, String selector) throws SystemException;

    /**
     * Obtener documento Adjunto dado id Documento Principal
     *
     * @param documento DTO que contiene los datos de la búsqueda
     * @return Lista de documentos adjuntos
     */
    MensajeRespuesta obtenerDocumentosAdjuntos(DocumentoDTO documento) throws SystemException;

    /**
     * Obtener versiones del documento dado id Documento
     *
     * @param idDoc   documento a subir
     * @return Lista de documentos adjuntos
     */
    MensajeRespuesta obtenerVersionesDocumento(String idDoc) throws SystemException;

    /**
     * Metodo para modificar metadatos del documento de Alfresco
     *
     * @param dto                 Obj DocumentoDTO con las modificaciones
     */
    MensajeRespuesta modificarMetadatosDocumento(DocumentoDTO dto) throws SystemException;


    /**
     * Descargar documento
     *
     * @param documentoDTO Objeto que contiene metadatos del documento en el ECM
     * @return Se retorna el documento
     */
    MensajeRespuesta descargarDocumento(DocumentoDTO documentoDTO) throws SystemException;

    /**
     * MOver documento
     *
     * @param documento      nombre de documento
     * @param carpetaFuente  carpeta fuente
     * @param carpetaDestino carpeta destino
     * @return mensaje respuesta
     */
    MensajeRespuesta movDocumento(String documento, String carpetaFuente, String carpetaDestino) throws SystemException;

    /**
     * Eliminar documento del ECM
     *
     * @param idDoc   Identificador del documento a borrar
     */
    void eliminardocumento(String idDoc) throws SystemException;

    /**
     * Servicio que devuelve el listado de las Series y de las Dependencias del ECM
     *
     * @param dependenciaTrdDTO Objeto dependencia que contiene los datos necesarios para realizar la busqueda
     * @return Objeto de dependencia que contiene las sedes o las dependencias buscadas
     */
    MensajeRespuesta devolverSerieSubSerie(ContenidoDependenciaTrdDTO dependenciaTrdDTO) throws SystemException;

    /**
     * Servicio que crea las unidades documentales del ECM
     *
     * @param unidadDocumentalDTO Objeto dependencia que contiene los datos necesarios para realizar la busqueda
     * @return MensajeRespuesta
     */
    MensajeRespuesta crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException;

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @return Mensaje de respuesta
     */
    MensajeRespuesta listarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException;

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idDocumento Id Documento
     * @return MensajeRespuesta con los detalles del documento
     */
    MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento) throws SystemException;

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @return MensajeRespuesta      Unidad Documntal
     */
    MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental) throws SystemException;

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param idUnidadDocumental Id de la Unidad Documental
     * @return UnidadDocumentalDTO si existe, null si no existe
     */
    Optional<UnidadDocumentalDTO> getUDToCloseById(String idUnidadDocumental);

    List<Document> getEcmDocumentsFromFolder(Folder folder);

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param idUnidadDocumental Id de la Unidad Documental
     * @return UnidadDocumentalDTO si existe, null si no existe
     */
    Optional<UnidadDocumentalDTO> getUDById(String idUnidadDocumental) throws SystemException;

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param idProperty Id de la Unidad Documental
     * @return Folder si existe, null si no existe
     */
    Optional<Folder> getUDFolderById(String idProperty);

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param unidadDocumentalDTO Obj Unidad Documental
     * @return boolean true/false
     */
    boolean actualizarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO);

    MensajeRespuesta eliminarUnidadDocumental(String idUnidadDocumental) throws SystemException;

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param folder Obj ECm
     * @return List<DocumentoDTO> Lista de los documentos de la carpeta
     */
    List<DocumentoDTO> getDocumentsFromFolder(Folder folder) throws SystemException;

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO     Obj Unidad Documental
     * @return MensajeRespuesta       Unidad Documental
     */
    MensajeRespuesta subirDocumentosUnidadDocumentalECM(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException;

    Map<String, Object> obtenerPropiedadesDocumento(Document document);

    /**
     * Operacion para devolver los documentos por archivar
     */
    MensajeRespuesta getDocumentosPorArchivar(final String codigoDependencia) throws SystemException;

    /**
     * Metodo para Modificar Unidades Documentales
     *
     * @param unidadDocumentalDTOS    Lista de unidades a modificar
     * @return MensajeRespuesta       Unidad Documental
     */
    MensajeRespuesta modificarUnidadesDocumentales(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException;

    /**
     * Operacion para devolver series o subseries
     *
     * @param documentoDTOS Lista de documentos a archivar
     * @return MensajeRespuesta
     */
    MensajeRespuesta subirDocumentosTemporalesUD(List<DocumentoDTO> documentoDTOS) throws SystemException;

    /**
     * Operacion para Subir documentos a una UD temporal ECM
     *
     * @param documentoDTO Obj de documento DTO a archivar
     * @return MensajeRespuesta
     */
    MensajeRespuesta subirDocumentoTemporalUD(DocumentoDTO documentoDTO) throws SystemException;

    /**
     * Operacion para devolver series o subseries
     *
     * @param codigoDependencia Codigo de la dependencia
     * @return MensajeRespuesta
     */
    MensajeRespuesta obtenerDocumentosArchivados(String codigoDependencia) throws SystemException;

    List<DocumentoDTO> obtenerDocumentosArchivadosList(String dependencyCode) throws SystemException;

    /**
     * Operacion para devolver sedes, dependencias, series o subseries
     *
     * @param dependenciaTrdDTO Objeto que contiene los datos de filtrado
     * @return MensajeRespuesta
     */
    MensajeRespuesta listarDependenciaMultiple(ContenidoDependenciaTrdDTO dependenciaTrdDTO) throws SystemException;

    /**
     * Operacion para devolver sedes, dependencias, series o subseries
     *
     * @param documentoDTO Obj con el tag a agregar
     * @return MensajeRespuesta
     */
    MensajeRespuesta transformaEstampaPd(DocumentoDTO documentoDTO) throws SystemException;

    /**
     * Subir Documento Anexo al ECM
     *
     * @param documento DTO que contiene los datos del documento Anexo
     * @return MensajeRespuesta DocumentoDTO adicionado
     */
    MensajeRespuesta subirDocumentoAnexo(DocumentoDTO documento) throws SystemException;

    void formatoListaUnidadDocumental(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException;

    MensajeRespuesta transformarDocumento(String tipoPlantilla, String htmlContent) throws SystemException;

    DocumentoDTO transformarDocumento(Document document, boolean getContentStream) throws SystemException;

    UnidadDocumentalDTO transformarUnidadDocumental(Folder folder);

    MensajeRespuesta modificarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException;

    default void removeDocument(String idDocumento, String nroRadicado) throws SystemException {
        try {
            Session session = getSession();
            if (StringUtils.isEmpty(idDocumento) && StringUtils.isEmpty(nroRadicado)) {
                throw new SystemException("No se ha especificado el criterio para eliminar el documento (ID = null, #RAD = null)");
            }
            if (!StringUtils.isEmpty(idDocumento)) {
                CmisObject object = session.getObject(idDocumento);
                object.delete();
                return;
            }
            if (nroRadicado.length() < 11) {
                throw new SystemException("El numero de radicado no cumple con la longitud minima");
            }
            final String query = "SELECT * FROM cmcor:CM_DocumentoPersonalizado " +
                    "WHERE " + ConstantesECM.CMCOR_DOC_RADICADO + " LIKE '%" + nroRadicado + "'";
            final ItemIterable<QueryResult> queryResults = session.query(query, false);
            if (queryResults.getPageNumItems() == 0) {
                throw new SystemException("No existe documento con #Rad: " + nroRadicado);
            }
            queryResults.forEach(queryResult -> {
                String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                final int index = objectId.indexOf(';');
                objectId = index != -1 ? objectId.substring(0, index) : objectId;
                CmisObject object = session.getObject(objectId);
                object.delete();
            });
        } catch (CmisObjectNotFoundException ex) {
            throw new SystemException("Error: No existe documento con ID: " + idDocumento);
        } catch (Exception ex) {
            throw new SystemException("Error: " + ex.getMessage());
        }
    }

    MensajeRespuesta listarUnidadDocumentalECM(String idUd, String nombreUd) throws SystemException;

    List<UnidadDocumentalDTO> listarUnidadesDocumentales(UnidadDocumentalDTO dto) throws SystemException;

    Folder getFolderFromRootByName(String folderName) throws SystemException;

    Map<String, Object> getPropertyMapFrom(UnidadDocumentalDTO unidadDocumentalDTO);

    MensajeRespuesta obtenerDocumentosUnidadDocumental(String ecmFolderUdId) throws SystemException;
}