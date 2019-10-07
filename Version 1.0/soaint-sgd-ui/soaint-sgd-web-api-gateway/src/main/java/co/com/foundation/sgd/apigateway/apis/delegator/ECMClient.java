package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.Futures;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.ws.rs.QueryParam;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;


@ApiDelegator
@Log4j2
public class ECMClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ECM_SERVICE_ENDPOINT_URL);
    private String record_endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ECM_RECORD_SERVICE_ENDPOINT_URL);
    private String corresponencia_endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    @Autowired
    @Qualifier("dfrThreadPoolTaskExecutor")
    private Executor executor;

    //private Client client = ClientBuilder.newClient();

    public ECMClient() {
        super();
    }

    public MensajeRespuesta uploadVersionDocumento(DocumentoDTO documentoDTO, String selector) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        Response response = wt.path("/subirVersionarDocumentoGeneradoECM/" + selector)
                .request()
                .post(Entity.json(documentoDTO));

        return response.readEntity(MensajeRespuesta.class);
    }

    public MensajeRespuesta obtenerVersionesDocumento(String documentId) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        Response response = wt.path("/obtenerVersionesDocumentos/" + documentId).request()
                .post(Entity.json(""));

        return response.readEntity(MensajeRespuesta.class);
    }

    public boolean eliminarVersionDocumento(String idDocumento) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        Response response = wt.path("/eliminarDocumentoECM/" + idDocumento).request()
                .delete();

        return response.readEntity(Boolean.class);
    }

    public MensajeRespuesta uploadDocumentoAnexo(DocumentoDTO documentoDTO) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        Response response = wt.path("/subirDocumentoAnexoECM/").request()
                .post(Entity.json(documentoDTO));

        return response.readEntity(MensajeRespuesta.class);
    }

    public MensajeRespuesta uploadDocument(DocumentoDTO documentoDTO, String tipoComunicacion) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        Response response = wt.path("/subirDocumentoRelacionECM/" + tipoComunicacion)
                .request()
                .post(Entity.json(documentoDTO));

        return response.readEntity(MensajeRespuesta.class);
    }

    public Future<Response> uploadDocumentAsync(DocumentoDTO documentoDTO, String tipoComunicacion, InvocationCallback<Response> cb) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        return wt.path("/subirDocumentoRelacionECM/" + tipoComunicacion)
                .request()
                .async()
                .post(Entity.json(documentoDTO), cb);
    }

    public MensajeRespuesta estamparEtiquetaRadicacion(DocumentoDTO documentoDTO) {
        log.info("Digitalizar Documento - [trafic] - Invocando Servicio Remoto Estampar Documento Radicacion Salida: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        Response response = wt.path("/transforma-estampa-pd/")
                .request().post(Entity.json(documentoDTO));
        return response.readEntity(MensajeRespuesta.class);
    }

    public List<MensajeRespuesta> uploadDocumentsAsociates(String parentId, Map<String, InputPart> files, String sede, String dependencia, String tipoComunicacion, String numero, String[] referidoList) {

        List<MensajeRespuesta> mensajeRespuestas = new ArrayList<>();
        final List<CompletableFuture<Response>> completableFutureArrayList = new ArrayList<>();


        try {
            files.forEach((key, part) -> {

                DocumentoDTO documentoAsociadoECMDTO = new DocumentoDTO();
                try {
                    documentoAsociadoECMDTO.setDependencia(dependencia);
                    documentoAsociadoECMDTO.setSede(sede);
                    InputStream result = part.getBody(InputStream.class, null);
                    documentoAsociadoECMDTO.setDocumento(IOUtils.toByteArray(result));
                    documentoAsociadoECMDTO.setTipoDocumento("application/pdf");
                    documentoAsociadoECMDTO.setNombreDocumento(key);
                    documentoAsociadoECMDTO.setIdDocumentoPadre(parentId);
                    documentoAsociadoECMDTO.setNroRadicado(numero);
                    documentoAsociadoECMDTO.setNroRadicadoReferido(referidoList);

                    completableFutureArrayList.add(Futures
                            .toCompletable(this.uploadDocumentAsync(documentoAsociadoECMDTO, tipoComunicacion, new InvocationCallback<Response>() {

                                @Override
                                public void completed(Response response) {
                                    mensajeRespuestas.add(response.readEntity(MensajeRespuesta.class));
                                }

                                @Override
                                public void failed(Throwable throwable) {

                                    log.info("error subiendo archivo:" + throwable.toString());
                                }
                            }), executor));

                } catch (Exception e) {
                    log.info("Error generando el documento ", e);
                }

            });

            CompletableFuture.allOf(completableFutureArrayList.toArray(new CompletableFuture[completableFutureArrayList.size()])).get();

        } catch (Exception e) {
            log.error("Se ha generado un error al subir los documentos asociados: ", e);
        }
        return mensajeRespuestas;
    }


    public Response findByIdDocument(String idDocumento) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/descargarDocumentoECM/")
                .queryParam("identificadorDoc", idDocumento)
                .request()
                .get();
    }

    public Response deleteDocumentById(String documentId) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/eliminarDocumentoECM/" + documentId).request()
                .delete();
    }

    public MensajeRespuesta findDocumentosAsociadosID(String idDocumento) {
        DocumentoDTO dto = DocumentoDTO.newInstance().idDocumento(idDocumento).build();
        Response response = getDocumentosAsociados(dto);
        return response.readEntity(MensajeRespuesta.class);
    }

    public MensajeRespuesta findDocumentosAsociadosRadicado(String nroRadicado) {
        DocumentoDTO dto = DocumentoDTO.newInstance().nroRadicado(nroRadicado).build();
        Response response = getDocumentosAsociados(dto);
        return response.readEntity(MensajeRespuesta.class);
    }

    public Response listarSeriesSubseriePorDependencia(ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/devolverSerieOSubserieECM")
                .request()
                .post(Entity.json(contenidoDependenciaTrdDTO));
    }

    public Response crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/crearUnidadDocumentalECM")
                .request()
                .post(Entity.json(unidadDocumentalDTO));
    }

    public Response listarUnidadesDocumentales(UnidadDocumentalDTO unidadDocumentalDTO) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/listarUnidadesDocumentalesECM")
                .request()
                .post(Entity.json(unidadDocumentalDTO));
    }

    public Response listarUnidadesDocumentalesDisposicion(DisposicionFinalDTO disposicionFinal, String dependencyCode) {
        WebTarget wt = ClientBuilder.newClient().target(record_endpoint);
        return wt.path("/listar-unidades-documentales-disposicion/" + dependencyCode)
                .request()
                .post(Entity.json(disposicionFinal));
    }

    public Response aprobarRechazarUnidadesDocumentalesDisposicion(List<UnidadDocumentalDTO> unidadesDocumentales) {
        WebTarget wt = ClientBuilder.newClient().target(record_endpoint);
        return wt.path("/aprobar-rechazar-disposiciones-finales")
                .request()
                .put(Entity.json(unidadesDocumentales));
    }

    public Response abrirCerrarReactivarUnidadDocumental(List<UnidadDocumentalDTO> dtoList) {
        log.info("AbrirCerrarReactivarUnidadesDocumentalesECMClient - [trafic] - cerrar unidades documentales");
        WebTarget wt = ClientBuilder.newClient().target(record_endpoint);
        return wt.path("/abrirCerrarReactivarUnidadesDocumentalesECM")
                .request()
                .put(Entity.json(dtoList));
    }

    public Future<Response> abrirCerrarReactivarUnidadDocumentalAsync(List<UnidadDocumentalDTO> dtoList, InvocationCallback<Response> cb) {
        log.info("AbrirCerrarReactivarUnidadesDocumentalesECMClient - [trafic] - cerrar unidades documentales");
        WebTarget wt = ClientBuilder.newClient().target(record_endpoint);
        return wt.path("/abrirCerrarReactivarUnidadesDocumentalesECM")
                .request()
                .async()
                .put(Entity.json(dtoList), cb);
    }


    public Response detalleUnidadDocumental(String idUnidadDocumental) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/verDetalleUnidadDocumentalECM/" + idUnidadDocumental)
                .request()
                .get();
    }

    public Response documentosPorArchivar(final String codigoDependencia) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/devolverDocumentosPorArchivarECM/" + codigoDependencia)
                .request()
                .get();
    }

    public Response documentosArchivados(String codigoDependencia) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/obtenerDocumentosArchivadosECM/" + codigoDependencia)
                .request()
                .get();
    }

    public Response modificarUnidadesDocumentales(List<UnidadDocumentalDTO> unidadesDocumentalesDTO) {
        log.info("ModificarUnidadesDocumentalesGatewayApi - [trafic] - Modificar Unidades Documentales");
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/modificarUnidadesDocumentalesECM")
                .request()
                .put(Entity.json(unidadesDocumentalesDTO));
    }

    public Response subirDocumentosUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) {
        log.info("SubirDocumentosUnidadDocumentalGatewayApi - [trafic] - Subir Documentos a Unidades Documentales");
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/subirDocumentosUnidadDocumentalECM")
                .request()
                .post(Entity.json(unidadDocumentalDTO));
    }

    public Response subirDocumentosPorArchivar(MultipartFormDataInput formDataInput) {
        log.info("SubirDocumentosPorArchivarGatewayApi - [trafic] - Subir documentos por archivar");
        if (null == formDataInput) {
            log.error("Esta vacia la lista Multipart");
            return Response.serverError().build();
        }
        try {
            log.info("Procesando la informacion del multipart");
            final Map<String, InputPart> _files = ECMUtils.findFiles(formDataInput);
            log.info("Devolviendo Mapa de Documentos");
            _files.forEach((fileName, inputPart) -> log.info("Nombre Archivo: {}, => documento: {}", fileName, inputPart));
            final String dependencyCode = formDataInput.getFormDataPart("codigoDependencia", String.class, null);
            final String autor = formDataInput.getFormDataPart("autor",String.class, null);
            log.info("Codigo de Dependencia: {}", dependencyCode);
            final List<DocumentoDTO> documentoDTOS = new ArrayList<>();
            final Collection<InputPart> values = _files.values();
            log.info("Cantidad de Documentos: {}", values.size());

            for (InputPart inputPart :
                    values) {
                final DocumentoDTO tmpDto = new DocumentoDTO();
                InputStream result = inputPart.getBody(InputStream.class, null);
                tmpDto.setDocumento(IOUtils.toByteArray(result));
                tmpDto.setCodigoDependencia(dependencyCode);
                tmpDto.setDocAutor(autor);
                tmpDto.setTipoDocumento("application/pdf");
                tmpDto.setNombreDocumento(ECMUtils.findName(inputPart));
                documentoDTOS.add(documentoDTOS.size(), tmpDto);
            }
            log.info("Cantidad de Documentos DTOs: {}", documentoDTOS.size());
            final WebTarget wt = ClientBuilder.newClient().target(endpoint);
            return wt.path("/subirDocumentosTemporalesECM")
                    .request()
                    .post(Entity.json(documentoDTOS));

        } catch (Exception e) {
            log.error("Error del Sistema {}", e.getMessage());
            MensajeRespuesta respuesta = MensajeRespuesta.newInstance()
                    .codMensaje("1223")
                    .mensaje(e.getMessage())
                    .build();
            return Response.status(Response.Status.OK).entity(respuesta).build();
        }
    }

    public Response restablecerArchivarDocumentoTask(String idproceso, String idtarea) {
        log.info("Unidad Documental - [trafic] - Invocando Servicio Remoto Salvar Tarea Archivar Documento: " + corresponencia_endpoint);
        WebTarget wt = ClientBuilder.newClient().target(corresponencia_endpoint);
        return wt.path("/tarea-web-api/tarea/" + idproceso + "/" + idtarea)
                .request().get();
    }

    private Response getDocumentosAsociados(DocumentoDTO documentoDTO) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/obtenerDocumentosAdjuntosECM").request().post(Entity.json(documentoDTO));
    }

    public Response transformarDocumentoPdf(String tipoPlantilla, String htmlContent) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/transformHtmlToPdf/" + tipoPlantilla).request().put(Entity.json(htmlContent));
    }

    public Response listarUnidadesDocumentalesATransferir(String tipoTransferencia, String dependencyCode) {
        WebTarget wt = ClientBuilder.newClient().target(record_endpoint);
        return wt.path("/listar-unidades-documentales-transferir")
                .queryParam("tipoTransferencia", tipoTransferencia)
                .queryParam("depCode", dependencyCode)
                .request()
                .get();
    }

    public Response aprobarRechazarTransferenciasUniadesDocumentales(List<UnidadDocumentalDTO> dtoList, String tipoTransferencia) {
        log.info("aprobarRechazarTransferenciasUniadesDocumentales - [trafic] - aprobar Rechazar Transferencias Uniades Documentales");
        WebTarget wt = ClientBuilder.newClient().target(record_endpoint);
        return wt.path("/aprobar-rechazar-transferencias/" + tipoTransferencia)
                .request()
                .put(Entity.json(dtoList));
    }

    public Response listaUnidadesDocumentalesParaVerificar(String dependencyCode, String numTransfer) {
        WebTarget wt = ClientBuilder.newClient().target(record_endpoint);
        return wt.path("/listar-unidades-documentales-verificar")
                .queryParam("depCode", dependencyCode)
                .queryParam("numTransfer", numTransfer)
                .request()
                .get();
    }

    public Response confirmarUnidadesDocumentalesTransferidas(List<UnidadDocumentalDTO> dtoList) {
        WebTarget wt = ClientBuilder.newClient().target(record_endpoint);
        return wt.path("/confirmar-unidades-documentales-transferidas")
                .request()
                .put(Entity.json(dtoList));
    }

    public Response listarUnidadesDocumentalesConfirmadas(String dependencyCode, String numTransfer) {
        WebTarget wt = ClientBuilder.newClient().target(record_endpoint);
        return wt.path("/listar-unidades-documentales-confirmadas")
                .queryParam("depCode", dependencyCode)
                .queryParam("numTransfer", numTransfer)
                .request()
                .get();
    }

    public Response consultarDocumento(String userLogin, String startDate, String endDate, String tipoComunicacion, String nroRadicado,
                                       String nroIdentificacion, String depOrigen, String depDestino, String depCode, String sCode, String ssCode,
                                       String nroGuia, String nombre, String asunto, String solicitante, String destinatario,
                                       String tipoDocumento, String tramite, String evento, String actuacion, String tipologiaDocumental) {


        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/consultar-documentos")
                .queryParam("usuario", userLogin)
                .queryParam("fecha_ini", startDate)
                .queryParam("fecha_fin", endDate)
                .queryParam("tipo_comunicacion", tipoComunicacion)
                .queryParam("nro_radicado", nroRadicado)
                .queryParam("nro_identificacion", nroIdentificacion)
                .queryParam("dep_origen", depOrigen)
                .queryParam("dep_destino", depDestino)
                .queryParam("depCode", depCode)
                .queryParam("sCode", sCode)
                .queryParam("ssCode", ssCode)
                .queryParam("nro_guia", nroGuia)
                .queryParam("nombre", nombre)
                .queryParam("asunto", asunto)
                .queryParam("solicitante", solicitante)
                .queryParam("destinatario", destinatario)
                .queryParam("tipoDocumento", tipoDocumento)
                .queryParam("evento", evento)
                .queryParam("tramite", tramite)
                .queryParam("actuacion", actuacion)
                .queryParam("tipologiaDocumental", tipologiaDocumental)
                .request()
                .get();
    }

    public Response verDetalleDocumento(final String idDcoumento) {

        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/consultar-detalle-documento/" + idDcoumento)
                .request()
                .get();
    }

    public Response consultarExpediente(final String usuario, final String codSede, final String codDependencia,
                                        final String serie, final String subSerie, final String idUnidadDocumental,
                                        final String nombreUnidadDocumental, final String descriptor1, final String descriptor2
    ) {


        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/consultar-expedientes")
                .queryParam("usuario", usuario)
                .queryParam("depJ", codSede)
                .queryParam("depP", codDependencia)
                .queryParam("sCode", serie)
                .queryParam("ssCode", subSerie)
                .queryParam("udId", idUnidadDocumental)
                .queryParam("udName", nombreUnidadDocumental)
                .queryParam("desc1", descriptor1)
                .queryParam("desc2", descriptor2)
                .request()
                .get();
    }

    public Response consultarDocumentosPorExpediente(final String idExpediente) {

        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/consultar-documentos-expediente/" + idExpediente)
                .request()
                .get();
    }

    public Response obtenerDocumentoPdf(String ideEcm){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/descargarDocumentoECM")
                 .queryParam("identificadorDoc",ideEcm)
                .request()
                .get();

    }

    public Response obtenerDocumentosUnidadDocumental(String ideEcm) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/obtener-documentos-ud/" + ideEcm)
                .request()
                .get();

    }
}
