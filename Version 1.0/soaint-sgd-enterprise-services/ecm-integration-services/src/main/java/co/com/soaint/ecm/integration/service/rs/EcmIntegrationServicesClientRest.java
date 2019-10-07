package co.com.soaint.ecm.integration.service.rs;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.QueryContent;
import co.com.soaint.ecm.business.boundary.mediator.EcmManager;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.foundation.canonical.ecm.*;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Dasiel
 */

@Log4j2
@Service
@Path("/ecm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EcmIntegrationServicesClientRest {

    @Autowired
    private EcmManager fEcmManager;

    @Autowired
    private QueryContent queryContent;

    /**
     * Constructor de la clase
     */
    public EcmIntegrationServicesClientRest() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * Crear estructura del ECM
     *
     * @param structure Estructura a crear
     * @return Mensaje de respuesta
     */
    @POST
    @Path("/crearEstructuraContent/")
    public MensajeRespuesta crearEstructuraContent(@RequestBody List<EstructuraTrdDTO> structure) {
        log.info("processing rest request - Crear Estructura ECM");
        try {
            return fEcmManager.crearEstructuraECM(structure);
        } catch (Exception e) {
            log.error("Error servicio creando estructura ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    @GET
    @Path("/loadTemplate/{filename}")
    public String loadTemplateECM(@PathParam("filename") String filename) {
        log.info("processing rest request - Load Template ECM");
        try {
            return fEcmManager.loadTemplateECM(filename);
        } catch (Exception e) {
            log.error("Error servicio Load Template from file {} -- {} ", filename, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Vista previa Documento html a pdf
     *
     * @param htmlContent
     * @return Mensaje de respuesta
     */
    @PUT
    @Path("/transformHtmlToPdf/{tipoPlantilla}")
    public MensajeRespuesta transformarDocumento(@PathParam("tipoPlantilla") String tipoPlantilla, String htmlContent) {
        log.info("processing rest request - Crear Estructura ECM");
        try {
            return fEcmManager.transformarDocumento(tipoPlantilla, htmlContent);
        } catch (Exception e) {
            log.error("Error servicio creando estructura ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Subir documento Principal al ECM
     *
     * @param selector Selector que dice donde se va a gauardar el documento
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/subirDocumentoRelacionECM/{selector}")
    public MensajeRespuesta subirDocumentoPrincipalAdjuntoECM(@RequestBody DocumentoDTO documento,
                                                              @PathParam("selector") String selector) {
        log.info("processing rest request - Subir Documento Adjunto al ECM " + documento.getNombreDocumento());
        try {
            return fEcmManager.subirDocumentoPrincipalAdjunto(documento, selector);
        } catch (Exception e) {
            log.error("Error en operacion - Subir Documento Adjunto ECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Subir documento Principal al ECM
     *
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/digitalizar-documento-radicado")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public MensajeRespuesta digitalizarDocumentoRadicadoEcm(@MultipartForm DocumentoDTO documento) {

        log.info("processing rest request - Subir Documento Adjunto al ECM " + documento.getNombreDocumento());
        try {
            final MensajeRespuesta respuesta = fEcmManager.subirDocumentoPrincipalAdjunto(documento, null);
            final List<DocumentoDTO> documentoDTOList = respuesta.getDocumentoDTOList();
            documentoDTOList.parallelStream().forEach(dto -> dto.setDocumento(null));
            return respuesta;
        } catch (Exception e) {
            log.error("Error en operacion - Subir Documento Adjunto ECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Crear link de documento en el ECM
     *
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/crearLinkDocumento/")
    public MensajeRespuesta crearLinkDocumentosApoyo(@RequestBody DocumentoDTO documento) {
        log.info("processing rest request - Crear Link de Documento en la carpeta Documentos de Apoyo para el documento: {}", documento.getNombreDocumento());
        try {
            return fEcmManager.crearLinkDocumentosApoyo(documento);
        } catch (Exception e) {
            log.error("Error en operacion - Crear Link de Documento en la carpeta Documentos de Apoyo ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Subir versionar documento Generado al ECM
     *
     * @param documento documento a subir
     * @param selector  parametro que indica donde se va a guardar el documento
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/subirVersionarDocumentoGeneradoECM/{selector}")
    public MensajeRespuesta subirVersionarDocumentoGeneradoECM(@RequestBody DocumentoDTO documento,
                                                               @PathParam("selector") String selector) {
        log.info("processing rest request - Subir Versionar Documento Generado al ECM " + documento.getNombreDocumento());
        try {
            return fEcmManager.subirVersionarDocumentoGenerado(documento, selector);
        } catch (Exception e) {
            log.error("Error en operacion - Subir Versionar Documento Generado al ECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Obtener id documentos adjuntos dado id documento principal
     *
     * @param documento DTO que contiene los datos de la búsqueda
     * @return Lista de documentos adjuntos ECM
     */
    @POST
    @Path("/obtenerDocumentosAdjuntosECM/")
    public MensajeRespuesta obtenerDocumentoPrincipalAdjunto(@RequestBody DocumentoDTO documento) {
        log.info("processing rest request - Buscar Documento en el ECM: {}", documento);
        try {
            return fEcmManager.obtenerDocumentosAdjuntos(documento);
        } catch (Exception e) {
            log.error("Error en operacion - Buscar Documento Adjunto en el ECM {}", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Subir Documento Anexo al ECM
     *
     * @param documento DTO que contiene los datos del documento Anexo
     * @return MensajeRespuesta DocumentoDTO adicionado
     */
    @POST
    @Path("/subirDocumentoAnexoECM/")
    public MensajeRespuesta subirDocumentoAnexoECM(@RequestBody DocumentoDTO documento) {
        log.info("processing rest request - Subir Documento Anexo al ECM: ");
        try {
            return fEcmManager.subirDocumentoAnexoECM(documento);
        } catch (Exception e) {
            log.error("Error en operacion - Subir Documento Anexo al ECM ");
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Obtener versiones de un documento dado su id
     *
     * @param idDoc Identificador del documento
     * @return Lista de documentos adjuntos ECM
     */
    @POST
    @Path("/obtenerVersionesDocumentos/{idDoc}")
    public MensajeRespuesta obtenerVersionesDocumento(@PathParam("idDoc") String idDoc) {
        log.info("processing rest request - Buscar Versiones del Documento en el ECM dado id: " + idDoc);
        try {
            return fEcmManager.obtenerVersionesDocumentos(idDoc);
        } catch (Exception e) {
            log.error("Error en operacion - Buscar Versiones de Documento en el ECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Modificar metadatos a documento en el ECM
     *
     * @param metadatos metadatos del documento
     * @return identificador del documento en el ecm
     */
    @PUT
    @Path("/modificarMetadatosDocumentoECM/")
    public MensajeRespuesta modificarMetadatosDocumentoECM(@RequestBody DocumentoDTO metadatos) {
        log.info("processing rest request - Subir Documento ECM " + metadatos.getIdDocumento());
        try {
            return fEcmManager.modificarMetadatosDocumento(metadatos);
        } catch (Exception e) {
            log.error("Error en operacion - Modificar Metadatos Documento ECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Operacion para mover documentos
     *
     * @param moverDocumento nombre del documento
     * @param carpetaFuente  fuente
     * @param carpetaDestino destino
     * @return Respuesta
     */
    @POST
    @Path("/moverDocumentoECM/")
    public MensajeRespuesta moverDocumentoECM(@QueryParam("moverDocumento") final String moverDocumento,
                                              @QueryParam("carpetaFuente") final String carpetaFuente,
                                              @QueryParam("carpetaDestino") final String carpetaDestino) {
        log.info("processing rest request - Mover Documento ECM");
        try {
            return fEcmManager.moverDocumento(moverDocumento, carpetaFuente, carpetaDestino);
        } catch (Exception e) {
            log.error("Error servicio moviendo documento ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Operacion para descargar documentos
     *
     * @param identificadorDoc identificador del documento
     * @return Documento
     */
    @GET
    @Path("/descargarDocumentoECM/")
    @Produces("application/pdf")
    public Response descargarDocumentoECM(@QueryParam("identificadorDoc") final String identificadorDoc) {
        log.info("processing rest request - Descargar Documento ECM");
        try {
            DocumentoDTO documentoDTO = new DocumentoDTO();
            documentoDTO.setIdDocumento(identificadorDoc);
            return fEcmManager.descargarDocumento(documentoDTO);
        } catch (Exception e) {
            log.error("Error servicio descargando documento ", e);
            return Response.serverError().build();
        }
    }

    /**
     * Operación para descargar una versión específica de un documento
     *
     * @param identificadorDoc identificador del documento
     * @param version          Versión que servirá de filtro para obtener el documento
     * @return Documento
     */
    @GET
    @Path("/descargarDocumentoVersionECM/")
    @Produces("application/pdf")
    public Response descargarDocumentoVersionECM(@QueryParam("identificadorDoc") final String identificadorDoc,
                                                 @QueryParam("version") final String version) {
        log.info("processing rest request - Descargar Documento ECM");
        try {
            DocumentoDTO documentoDTO = new DocumentoDTO();
            documentoDTO.setIdDocumento(identificadorDoc);
            documentoDTO.setVersionLabel(version);
            return fEcmManager.descargarDocumento(documentoDTO);
        } catch (Exception e) {
            log.error("Error servicio descargando documento ", e);
            return Response.serverError().build();
        }
    }

    /**
     * Operacion para eliminar documentos
     *
     * @param idDocumento Identificador del documento
     * @return True en exito false en error
     */
    @DELETE
    @Path("/eliminarDocumentoECM/{idDocumento}")
    public boolean eliminarDocumentoECM(@PathParam("idDocumento") String idDocumento) {
        log.info("processing rest request - Eliminar Documento ECM");
        try {
            fEcmManager.eliminarDocumentoECM(idDocumento);
            return true;
        } catch (Exception e) {
            log.error("Error servicio eliminando documento ", e);
            return false;
        }
    }

    /**
     * Operacion para eliminar documentos
     *
     * @param idDocumento Identificador del documento
     * @return True en exito false en error
     */
    @DELETE
    @Path("/removeDocumentECM/")
    public MensajeRespuesta removeDocumentECM(@QueryParam("idDoc") String idDocumento,
                                              @QueryParam("nroRad") String nroRadicado) {
        log.info("processing rest request - Eliminar Documento ECM");
        try {
            fEcmManager.removeDocumentECM(idDocumento, nroRadicado);
            return MensajeRespuesta.newInstance()
                    .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                    .mensaje(ConstantesECM.SUCCESS)
                    .build();
        } catch (Exception e) {
            log.error("Error servicio eliminando documento ", e);
            return getSmsErrorResponse(e);
        }
    }

    /**
     * Operacion para devolver series o subseries
     *
     * @param dependenciaTrdDTO Objeto que contiene los datos de filtrado
     * @return MensajeRespuesta
     */
    @POST
    @Path("/devolverSerieOSubserieECM/")
    public MensajeRespuesta devolverSerieSubserie(@RequestBody ContenidoDependenciaTrdDTO dependenciaTrdDTO) {
        log.info("processing rest request - Obtener las series o subseries de la dependencia con código " + dependenciaTrdDTO.getIdOrgOfc());
        try {
            return fEcmManager.devolverSerieSubserie(dependenciaTrdDTO);
        } catch (Exception e) {
            log.error("Error en operacion - Devolver Serie Subserie ECM {}", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Operacion para devolver sedes, dependencias, series o subseries
     *
     * @param dependenciaTrdDTO Objeto que contiene los datos de filtrado
     * @return MensajeRespuesta
     */
    @POST
    @Path("/listar-dependencia-multiple-ecm/")
    public MensajeRespuesta listarDependenciaMultipleECM(@RequestBody ContenidoDependenciaTrdDTO dependenciaTrdDTO) {
        log.info("processing rest request - Obtener las sedes, dependencias, series o subseries");
        try {
            return fEcmManager.listarDependenciaMultipleECM(dependenciaTrdDTO);
        } catch (Exception e) {
            log.error("Error en operacion - Obtener las sedes, dependencias, series o subseries ECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Operacion para devolver sedes, dependencias, series o subseries
     *
     * @param documentoDTO Obj con el tag a agregar
     * @return MensajeRespuesta
     */
    @POST
    @Path("/transforma-estampa-pd/")
    public MensajeRespuesta transformaEstampaPdEcm(@RequestBody DocumentoDTO documentoDTO) {
        log.info("processing rest request - Estampar la etiquta de radicacion");
        try {
            return fEcmManager.transformaEstampaPd(documentoDTO);
        } catch (Exception e) {
            log.error("Error en operacion - Estampar la etiquta de radicacion ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /*
     * UNIDADES DOCUMENTALES
     */

    /**
     * Crear unidad documental en el ECM
     *
     * @param unidadDocumentalDTO Unidad a crear
     * @return Mensaje de respuesta
     */
    @POST
    @Path("/crearUnidadDocumentalECM/")
    public MensajeRespuesta crearUnidadDocumentalECM(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO) {
        log.info("processing rest request - Crear Unidad Documental ECM");
        try {
            return fEcmManager.crearUnidadDocumental(unidadDocumentalDTO);
        } catch (Exception e) {
            log.error("Error en operacion - crearUnidadDocumentalECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @param unidadDocumentalDTO Objeto que contiene el criterio de Busqueda
     * @return MensajeRespuesta Mensaje de respuesta
     */
    @POST
    @Path("/listarUnidadesDocumentalesECM/")
    public MensajeRespuesta listarUnidadDocumentalECM(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO) {
        log.info("processing rest request - Listar Unidades Documentales ECM");
        try {
            return fEcmManager.listarUnidadDocumental(unidadDocumentalDTO);
        } catch (Exception e) {
            log.error("Error en operacion - listarUnidadDocumentalECM(unidadDocumentalDTO) {}", e);
            return this.getSmsErrorResponse(e);
        }
    }

    @GET
    @Path("/lista-unidad-documental")
    public MensajeRespuesta listarUnidadDocumentalECM(@QueryParam("idUd") final String idUd, @QueryParam("nameUd") final String nombreUd) {
        log.info("processing rest request - Listar Unidades Documentales ECM");
        try {
            return fEcmManager.listarUnidadDocumentalECM(idUd, nombreUd);
        } catch (Exception e) {
            log.error("Error en operacion - listarUnidadDocumentalECM(unidadDocumentalDTO) {}", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idDocumento Id Documento
     * @return MensajeRespuesta con los detalles del documento
     */
    @GET
    @Path("/obtenerDetallesDocumentoECM/{idDoc}")
    public MensajeRespuesta obtenerDetallesDocumentoDTO(@PathParam("idDoc") String idDocumento) {
        log.info("Ejecutando metodo MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento)");
        try {
            return fEcmManager.obtenerDetallesDocumentoDTO(idDocumento);
        } catch (Exception e) {
            log.error("Error en operacion - obtenerDetallesDocumentoDTO ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @return MensajeRespuesta      Unidad Documntal
     */
    @GET
    @Path("/verDetalleUnidadDocumentalECM/{idUnidadDocumental}")
    public MensajeRespuesta detallesUnidadDocumentalECM(@PathParam("idUnidadDocumental") String idUnidadDocumental) {
        log.info("Ejecutando metodo MensajeRespuesta detallesUnidadDocumentalECM(String idUnidadDocumental)");
        try {
            return fEcmManager.detallesUnidadDocumental(idUnidadDocumental);
        } catch (Exception e) {
            log.error("Error en operacion - detallesUnidadDocumentalECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO Obj Unidad Documental
     * @return MensajeRespuesta       Unidad Documental
     */
    @POST
    @Path("/subirDocumentosUnidadDocumentalECM/")
    public MensajeRespuesta subirDocumentosUnidadDocumentalECM(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO) {
        log.info("Ejecutando metodo MensajeRespuesta subirDocumentosUnidadDocumentalECM(unidadDocumentalDTO, documentoDTOS)");
        try {
            return fEcmManager.subirDocumentosUnidadDocumental(unidadDocumentalDTO);
        } catch (Exception e) {
            log.error("Error en operacion - subirDocumentosUnidadDocumentalECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Metodo para Modificar Unidades Documentales
     *
     * @param unidadDocumentalDTOS Lista de unidades a modificar
     * @return MensajeRespuesta       Unidad Documental
     */
    @PUT
    @Path("/modificarUnidadesDocumentalesECM/")
    public MensajeRespuesta modificarUnidadesDocumentalesECM(@RequestBody List<UnidadDocumentalDTO> unidadDocumentalDTOS) {
        log.info("Ejecutando metodo MensajeRespuesta modificarUnidadesDocumentalesECM(List<UnidadDocumentalDTO> documentoDTOS)");
        try {
            return fEcmManager.modificarUnidadesDocumentales(unidadDocumentalDTOS);
        } catch (Exception e) {
            log.error("Error en operacion - modificarUnidadesDocumentalesECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Metodo para Modificar Unidades Documentales
     *
     * @param unidadDocumentalDTO unidad a modificar
     * @return MensajeRespuesta       Unidad Documental
     */
    @PUT
    @Path("/modificarUnidadDocumentalECM/")
    public MensajeRespuesta modificarUnidadDocumentalECM(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO) {
        log.info("Ejecutando metodo MensajeRespuesta modificarUnidadesDocumentalesECM(List<UnidadDocumentalDTO> documentoDTOS)");
        try {
            return fEcmManager.modificarUnidadDocumental(unidadDocumentalDTO);
        } catch (Exception e) {
            log.error("Error en operacion - modificarUnidadesDocumentalesECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Operacion para devolver los documentos por archivar
     */
    @GET
    @Path("/devolverDocumentosPorArchivarECM/{codigoDependencia}")
    public MensajeRespuesta getDocumentosPorArchivarECM(@PathParam("codigoDependencia") final String codigoDependencia) {
        log.info("processing rest request - Obtener los documentos por archivar en el ECM");
        try {
            return fEcmManager.getDocumentosPorArchivar(codigoDependencia);
        } catch (Exception e) {
            log.error("Error en operacion - getDocumentosPorArchivarECM ECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Operacion para devolver series o subseries
     *
     * @param codigoDependencia Codigo de la dependencia
     * @return MensajeRespuesta
     */
    @GET
    @Path("/obtenerDocumentosArchivadosECM/{codigoDependencia}")
    public MensajeRespuesta obtenerDocumentosArchivadosECM(@PathParam("codigoDependencia") final String codigoDependencia) {
        log.info("processing rest request - Obtener documentos Archivados ECM");
        try {
            return fEcmManager.obtenerDocumentosArchivados(codigoDependencia);
        } catch (Exception e) {
            log.error("Error en operacion - Obtener documentos Archivados ECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    @GET
    @Path("/obtener-documentos-ud/{ecmFolderUdId}")
    public MensajeRespuesta obtenerDocumentosUnidadDocumentalECM(@PathParam("ecmFolderUdId") final String ecmFolderUdId) {
        log.info("processing rest request - Obtener documentos Archivados ECM");
        try {
            return fEcmManager.obtenerDocumentosUnidadDocumental(ecmFolderUdId);
        } catch (Exception e) {
            log.error("Error en operacion - Obtener documentos Archivados ECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Operacion para devolver series o subseries
     *
     * @param documentoDTOS Lista de documentos a archivar
     * @return MensajeRespuesta
     */
    @POST
    @Path("/subirDocumentosTemporalesECM/")
    public MensajeRespuesta subirDocumentosTemporalesUDECM(@RequestBody List<DocumentoDTO> documentoDTOS) {
        log.info("processing rest request - Subir Documentos temporales ECM");
        try {
            return fEcmManager.subirDocumentosTemporalesUD(documentoDTOS);
        } catch (Exception e) {
            log.error("Error en operacion - Subir Documentos temporales ECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /**
     * Operacion para Subir documentos a una UD temporal ECM
     *
     * @param documentoDTO Obj de documento DTO a archivar
     * @return MensajeRespuesta
     */
    @POST
    @Path("/subirDocumentoTemporalECM/")
    public MensajeRespuesta subirDocumentoTemporalUDECM(@RequestBody DocumentoDTO documentoDTO) {
        log.info("processing rest request - Subir Documento temporal ECM");
        try {
            return fEcmManager.subirDocumentoTemporalUD(documentoDTO);
        } catch (Exception e) {
            log.error("Error en operacion - Subir Documento temporal ECM ", e);
            return this.getSmsErrorResponse(e);
        }
    }

    /*
     * MODULO CONSULTA
     */

    @GET
    @Path("/consultar-documentos/")
    public MensajeRespuesta consultarDocumentosECM(@QueryParam("usuario") String userLogin,
                                                   @QueryParam("fecha_ini") final String startDate,
                                                   @QueryParam("fecha_fin") final String endDate,
                                                   @QueryParam("tipo_comunicacion") final String tipoComunicacion,
                                                   @QueryParam("nro_radicado") final String nroRadicado,
                                                   @QueryParam("nro_identificacion") final String nroIdentificacion,
                                                   @QueryParam("dep_origen") final String depOrigen,
                                                   @QueryParam("dep_destino") final String depDestino,
                                                   @QueryParam("depCode") final String depCode,
                                                   @QueryParam("sCode") final String sCode,
                                                   @QueryParam("ssCode") final String ssCode,
                                                   @QueryParam("nro_guia") final String nroGuia,
                                                   @QueryParam("nombre") final String nombre,
                                                   @QueryParam("asunto") final String asunto,
                                                   @QueryParam("solicitante") final String solicitante,
                                                   @QueryParam("destinatario") final String destinatario,
                                                   @QueryParam("tipoDocumento") final String tipoDocumento,
                                                   @QueryParam("tramite") final String tramite,
                                                   @QueryParam("evento") final String evento,
                                                   @QueryParam("actuacion") final String actuacion,
                                                   @QueryParam("tipologiaDocumental") final String tipologiaDocumental) {
        try {
            return queryContent.consultarDocumentos(userLogin, startDate, endDate, tipoComunicacion, nroRadicado, nroIdentificacion,
                    depOrigen, depDestino, depCode, sCode, ssCode, nroGuia, nombre, asunto, solicitante, destinatario, tipoDocumento, tramite, evento, actuacion, tipologiaDocumental);
        } catch (Exception e) {
            log.error(e);
            return getSmsErrorResponse(e);
        }
    }

    @GET
    @Path("/consultar-detalle-documento/{docEcmId}")
    public MensajeRespuesta verDetalleDocumentoECM(@PathParam("docEcmId") String docEcmId) {
        try {
            return queryContent.verDetalleDocumento(docEcmId);
        } catch (Exception e) {
            log.error(e);
            return getSmsErrorResponse(e);
        }
    }

    @GET
    @Path("/consultar-expedientes/")
    public MensajeRespuesta consultarExpedientesECM(@QueryParam("usuario") String userLogin,
                                                   @QueryParam("depJ") final String depJ,
                                                   @QueryParam("depP") final String depP,
                                                   @QueryParam("sCode") final String sCode,
                                                   @QueryParam("ssCode") final String ssCode,
                                                   @QueryParam("udId") final String udId,
                                                   @QueryParam("udName") final String udName,
                                                   @QueryParam("desc1") final String desc1,
                                                   @QueryParam("desc2") final String desc2) {
        try {
            return queryContent.consultarExpedientes(userLogin, depJ, depP, sCode, ssCode, udId, udName,
                    desc1, desc2);
        } catch (Exception e) {
            log.error(e);
            return getSmsErrorResponse(e);
        }
    }

    @GET
    @Path("/consultar-documentos-expediente/{ecmFolderId}")
    public MensajeRespuesta consultarDocsExpedienteECM(@PathParam("ecmFolderId") String ecmFolderId) {
        try {
            return queryContent.consultarDocsExpediente(ecmFolderId);
        } catch (Exception e) {
            log.error(e);
            return getSmsErrorResponse(e);
        }
    }

    private MensajeRespuesta getSmsErrorResponse(@NotNull Exception e) {
        return MensajeRespuesta.newInstance().
                codMensaje(ConstantesECM.ERROR_COD_MENSAJE).
                mensaje(e.getMessage()).build();
    }
}