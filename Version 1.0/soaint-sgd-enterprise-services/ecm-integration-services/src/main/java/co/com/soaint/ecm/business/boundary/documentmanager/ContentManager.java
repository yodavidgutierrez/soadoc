package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentTemplate;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.canonical.ecm.util.StructureUtils;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;


/**
 * Created by dasiel
 */

@Log4j2
@BusinessBoundary
public class ContentManager implements Serializable {
    
    private static final Long serialVersionUID = 150L;

    private final ContentControl contentControl;
    private final ContentTemplate contentTemplate;

    @Autowired
    public ContentManager(ContentControl contentControl, ContentTemplate contentTemplate) {
        this.contentControl = contentControl;
        this.contentTemplate = contentTemplate;
    }

    /**
     * Metodo que crea la estructura en el ECM
     *
     * @param structure Listado que contiene la estructura a crear
     * @return Mensaje de respuesta
     * @throws InfrastructureException Excepcion que se recoje ante cualquier error
     */
    public MensajeRespuesta crearEstructuraContent(List<EstructuraTrdDTO> structure) throws SystemException {
        log.info("### Creando estructura content..");
        for (EstructuraTrdDTO EstructuraTrdDTO : structure) {
            StructureUtils.ordenarListaOrganigrama(EstructuraTrdDTO.getOrganigramaItemList());
        }
        log.info("### Estableciendo Conexion con el ECM..");
        Carpeta carpeta = new Carpeta();
        carpeta.setFolder(contentControl.getSession().getRootFolder());
        return contentControl.generarArbol(structure, carpeta);        
    }

    /**
     * Metodo generico para subir los dccumentos adjuntos al content
     *
     * @param documento Documento que se va a subir
     * @param selector  Selector que dice donde se va a gauardar el documento
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta subirDocumentoPrincipalAdjuntoContent(DocumentoDTO documento, String selector) throws SystemException {
        log.info("### Subiendo documento principal/adjunto al content..");
        log.info("### Se invoca el metodo de subir el documento principal/adjunto..");
        return contentControl.subirDocumentoPrincipalAdjunto(documento, selector);
    }

    /**
     * Metodo generico para crear el link del documento en el content
     *
     * @param documento Documento que se va a subir
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta crearLinkContent(DocumentoDTO documento) {
        log.info("### Creando link del documento en el content..");
        log.info("### Se invoca el metodo crearLinkDocumentosApoyo..");
        return contentControl.crearLinkDocumentosApoyo(documento);
    }

    /**
     * Metodo generico para subir los dccumentos adjuntos al content
     *
     * @param documento Documento que se va a subir
     * @param selector  parametro que indica donde se va a guardar el documento
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta subirVersionarDocumentoGeneradoContent(DocumentoDTO documento, String selector) throws SystemException {
        log.info("### Subiendo versionando documento generado al content..");
        log.info("### Se invoca el metodo de subir/versionar el documento..");
        return contentControl.subirVersionarDocumentoGenerado( documento, selector);
    }

    /**
     * Metodo generico para subir los dccumentos adjuntos al content
     *
     * @param documento DTO que contiene los datos de la búsqueda
     * @return Lista de objetos de documentos asociados al idDocPrincipal
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta obtenerDocumentosAdjuntosContent(DocumentoDTO documento) throws SystemException {
        log.info("### Obtener documento principal y adjunto del content..");
        log.info("### Se invoca el metodo de obtener documentos principales y adjuntos..");
        return contentControl.obtenerDocumentosAdjuntos( documento);
    }

    /**
     * Metodo generico para obtener las versiones de un documento del content
     *
     * @param idDoc Id Documento que se va  a pedir Versiones
     * @return Lista de objetos de documentos asociados al idDocPrincipal
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta obtenerVersionesDocumentoContent(String idDoc) throws SystemException {
        log.info("### Obtener versiones documento del content..");
        log.info("### Se invoca el metodo de obtener versiones del documento..");
        return contentControl.obtenerVersionesDocumento( idDoc);
    }

    /**
     * Metodo generico para subir los dccumentos al content
     *
     * @param metadatosDocumentos Metadatos del documento a modificar
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta modificarMetadatoDocumentoContent(DocumentoDTO metadatosDocumentos) throws SystemException {
        log.info("### Modificando metadatos del documento..");log.info("### Se invoca el metodo de modificar el documento..");
        return contentControl.modificarMetadatosDocumento(metadatosDocumentos);
    }

    /**
     * Metodo generico para mover documentos dentro del ECM
     *
     * @param documento      Identificador del documento que se va a mover
     * @param carpetaFuente  Carpeta donde esta actualmente el documento.
     * @param carpetaDestino Carpeta a donde se movera el documento.
     * @return Mensaje de respuesta del metodo (coigo y mensaje)
     */
    public MensajeRespuesta moverDocumento(String documento, String carpetaFuente, String carpetaDestino) throws SystemException {
        log.info("### Moviendo Documento " + documento + " desde la carpeta: " + carpetaFuente + " a la carpeta: " + carpetaDestino);
        return contentControl.movDocumento( documento, carpetaFuente, carpetaDestino);
    }

    /**
     * Metodo generico para descargar los documentos del ECM
     *
     * @param documentoDTO Metadatos del documento dentro del ECM
     * @return Documento
     */
    public MensajeRespuesta descargarDocumentoContent(DocumentoDTO documentoDTO) throws SystemException {
        log.info("### Descargando documento del content..");
        log.info("### Se invoca el metodo de descargar el documento..");
        return contentControl.descargarDocumento(documentoDTO);
    }

    /**
     * Metodo generico para eliminar los documentos del ECM
     *
     * @param idDoc Identificador del documento dentro del ECM
     * @return true en exito y false en error
     */
    public void eliminarDocumento(String idDoc) throws SystemException {
        log.info("### Eliminando documento del content..");
        log.info("### Se invoca el metodo de eliminar el documento..");
        contentControl.eliminardocumento(idDoc);
    }

    /**
     * Metodo generico para devolver series o subseries
     *
     * @param contenidoDependenciaTrdDTO Objeto que contiene los datos para realizar la busqueda
     * @return Objeto response
     */
    public MensajeRespuesta devolverSeriesSubseries(ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO) throws SystemException {
        log.info("### Obteniendo las series y subseries del content..");
        log.info("### Se invoca el metodo de devolver serie o subserie..");
        MensajeRespuesta response = contentControl.devolverSerieSubSerie(contenidoDependenciaTrdDTO);
        log.info("Series o subseries devueltas exitosamente");
        return response;
    }

    /**
     * Metodo para devolver crear las unidades documentales
     *
     * @param unidadDocumentalDTO DTO que contiene los parametro de búsqueda
     * @return MensajeRespuesta
     */
    public MensajeRespuesta crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("### Creando la unidad documental {} ..", unidadDocumentalDTO);
        log.info("### Invocando metodo para crear Unidad Documental..");
        return contentControl.crearUnidadDocumental(unidadDocumentalDTO);
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @return Mensaje de respuesta
     */
    public MensajeRespuesta listarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("### Listando las Unidades Documentales listarUnidadDocumental method");
        return contentControl.listarUnidadDocumental(unidadDocumentalDTO);
    }

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idDocumento Id Documento
     * @return MensajeRespuesta con los detalles del documento
     */
    public MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento) throws Exception {
        log.info("### mostrando la UnidadDocumental obtenerDetallesDocumentoDTO(String idDocumento) method");
        return contentControl.obtenerDetallesDocumentoDTO(idDocumento);
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @return MensajeRespuesta      Unidad Documntal
     */
    public MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental) throws Exception {
        log.info("### Ejecutando MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental)");
        return contentControl.detallesUnidadDocumental(idUnidadDocumental);
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO Obj Unidad Documental
     * @return MensajeRespuesta       Unidad Documental
     */
    public MensajeRespuesta subirDocumentosUnidadDocumentalECM(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("### Ejecutando MensajeRespuesta subirDocumentosUnidadDocumentalECM(unidadDocumentalDTO, documentoDTO)");
        return contentControl.subirDocumentosUnidadDocumentalECM(unidadDocumentalDTO);
    }

    public MensajeRespuesta getDocumentosPorArchivar(final String codigoDependencia) throws SystemException {
        log.info("processing rest request - Obtener los documentos por archivar en el ECM");
        return contentControl.getDocumentosPorArchivar(codigoDependencia);
    }

    /**
     * Metodo para Modificar Unidades Documentales
     *
     * @param unidadDocumentalDTOS Lista de unidades a modificar
     * @return MensajeRespuesta       Unidad Documental
     */
    public MensajeRespuesta modificarUnidadesDocumentales(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException {
        log.info("processing rest request - modificar las unidades documentales en el ECM");
        return contentControl.modificarUnidadesDocumentales(unidadDocumentalDTOS);
    }

    /**
     * Operacion para devolver series o subseries
     *
     * @param documentoDTOS Lista de documentos a archivar
     * @return MensajeRespuesta
     */
    public MensajeRespuesta subirDocumentosTemporalesUD(List<DocumentoDTO> documentoDTOS) throws SystemException {
        log.info("processing rest request - Subir Documentos temporales ECM");
        return contentControl.subirDocumentosTemporalesUD(documentoDTOS);
    }

    /**
     * Operacion para devolver series o subseries
     *
     * @param codigoDependencia Codigo de la dependencia
     * @return MensajeRespuesta
     */
    public MensajeRespuesta obtenerDocumentosArchivados(String codigoDependencia) throws SystemException {
        log.info("processing rest request - Obtener Documentos archivados ECM");
        return contentControl.obtenerDocumentosArchivados(codigoDependencia);
    }

    /**
     * Operacion para devolver sedes, dependencias, series o subseries
     *
     * @param dependenciaTrdDTO Objeto que contiene los datos de filtrado
     * @return MensajeRespuesta
     */
    public MensajeRespuesta listarDependenciaMultiple(ContenidoDependenciaTrdDTO dependenciaTrdDTO) throws SystemException {
        log.info("processing rest request - Obtener las sedes, dependencias, series o subseries");
        return contentControl.listarDependenciaMultiple(dependenciaTrdDTO);
    }

    /**
     * Operacion para Subir documentos a una UD temporal ECM
     *
     * @param documentoDTO Obj de documento DTO a archivar
     * @return MensajeRespuesta
     */
    public MensajeRespuesta subirDocumentoTemporalUD(DocumentoDTO documentoDTO) throws SystemException {
        log.info("processing rest request - Subir Documento temporal ECM");
        return contentControl.subirDocumentoTemporalUD(documentoDTO);
    }

    /**
     * Operacion para devolver sedes, dependencias, series o subseries
     *
     * @param documentoDTO Obj con el tag a agregar
     * @return MensajeRespuesta
     */
    public MensajeRespuesta transformaEstampaPd(DocumentoDTO documentoDTO) throws SystemException {
        log.info("processing rest request - Estampar la etiquta de radicacion");
        return contentControl.transformaEstampaPd(documentoDTO);
    }

    /**
     * Subir Documento Anexo al ECM
     *
     * @param documento DTO que contiene los datos del documento Anexo
     * @return MensajeRespuesta DocumentoDTO adicionado
     */
    public MensajeRespuesta subirDocumentoAnexo(DocumentoDTO documento) throws SystemException {
        log.info("processing rest request - Subir Documento Anexo al ECM:");
        return contentControl.subirDocumentoAnexo(documento);
    }

    public MensajeRespuesta transformarDocumento(String tipoPlantilla, String htmlContent) throws SystemException {
        return contentControl.transformarDocumento(tipoPlantilla, htmlContent);
    }

    public String loadTemplateECM(String filename) throws SystemException {
        return contentTemplate.loadTemplateFromResources(filename);
    }

    public MensajeRespuesta modificarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        return contentControl.modificarUnidadDocumental(unidadDocumentalDTO);
    }

    public void removeDocument(String idDocumento, String nroRadicado) throws SystemException {
        contentControl.removeDocument(idDocumento, nroRadicado);
    }

    public MensajeRespuesta listarUnidadDocumentalECM(String idUd, String nombreUd) throws SystemException {
        return contentControl.listarUnidadDocumentalECM(idUd, nombreUd);
    }

    public MensajeRespuesta obtenerDocumentosUnidadDocumental(String ecmFolderUdId) throws SystemException {
        return contentControl.obtenerDocumentosUnidadDocumental(ecmFolderUdId);
    }
}
