package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.sgd.apigateway.apis.delegator.NotificationClient;
import co.com.foundation.sgd.apigateway.apis.delegator.UnidadDocumentalClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.foundation.sgd.dto.DisposicionFinalDTOAG;
import co.com.foundation.sgd.dto.NoTramiteRequestDTO;
import co.com.foundation.sgd.dto.UnidadDocumentalRequestDTO;
import co.com.foundation.sgd.dto.UnidadDocumetalAGDTO;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.NotificacionDTO;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;
import co.com.soaint.foundation.canonical.ecm.*;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;


@Path("/unidad-documental-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class UnidadDocumentalGatewayApi {

    @Autowired
    private ECMClient ecmClient;

    @Autowired
    private UnidadDocumentalClient unidadDocumentalClient;

    @Autowired
    private NotificationClient notificationClient;

    @Context
    private UriInfo uriInfo;

    public UnidadDocumentalGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/listado-serie-subserie")
    @JWTTokenSecurity
    public Response listarSerie(@RequestBody ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO ) {
        log.info("UnidadDocumentalGatewayApi - [trafic] - listing series");
        Response response = ecmClient.listarSeriesSubseriePorDependencia(contenidoDependenciaTrdDTO);
        String responseContent = response.readEntity(String.class);
        log.info("UnidadDocumentalGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/crear-unidad-documental")
    @JWTTokenSecurity
    @RolesAllowed("Archivista")
    public Response crearUnidadDocumental(@RequestBody UnidadDocumentalRequestDTO unidadDocumentalDTO) {
        log.info("UnidadDocumentalGatewayApi - [trafic] - creating unidad documental");
        Response response = unidadDocumentalClient.salvarUnidadDocumental(unidadDocumentalDTO);

        if(response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return  response;

        if(!unidadDocumentalDTO.getIdArchivista().equals(unidadDocumentalDTO.getIdSolicitante())){

            final  AgenteDTO remitente = new AgenteDTO();
            remitente.setIdeFunci(unidadDocumentalDTO.getIdArchivista());

           final AgenteDTO destinatario = new AgenteDTO();
            destinatario.setIdeFunci(unidadDocumentalDTO.getIdSolicitante());

            final String asunto = "La unidad documental solicitadad por usted ha sido creada";

            NotificacionDTO notificacionDTO = new NotificacionDTO(null,remitente,destinatario,asunto);

            notificationClient.notificate(notificacionDTO);
        }

       return response;
    }

    @POST
    @Path("/listar-unidad-documental")
    @JWTTokenSecurity
    @RolesAllowed({"Archivista","Archivista_AC","Solicitante"})
    public Response listarUnidadDocumental(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO ) {
        log.info("ListarUnidadesDocumentalesGatewayApi - [trafic] - listing unidades documentales");
        Response response = ecmClient.listarUnidadesDocumentales(unidadDocumentalDTO);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/gestionar-unidades-documentales")
    @JWTTokenSecurity
    @RolesAllowed("Archivista")
    public Response abrirUnidadesDocumentales(@RequestBody List<UnidadDocumetalAGDTO> unidadesDocumentalesDTO ) {

         log.info("AbrirUnidadesDocumentalesGatewayApi - [trafic] - abrir unidades documentales");
        return unidadDocumentalClient.gestionarUnidadDocumentales(unidadesDocumentalesDTO);

    }

    @GET
    @Path("/listar-unidades-documentales-disposicion/{depCode}")
    @JWTTokenSecurity
    @RolesAllowed("Archivista_AC")
    public Response listarUnidadesDocumentalesDisposicion(@QueryParam("disposicionFinal") DisposicionFinalDTOAG disposicionFinal,
                                                          @PathParam("depCode") String dependencyCode) {
        log.info("ListarrUnidadesDocumentalesGatewayApi - [trafic] - listar unidades documentales disposicion final");
        log.info("Disposicion request :"+ disposicionFinal);
        return unidadDocumentalClient.listarDisposicionFinal(dependencyCode,disposicionFinal);
    }

    @POST
    @Path("/aprobar-rechazar-disposiciones-finales")
    @JWTTokenSecurity
    @RolesAllowed("Archivista_AC")
    public Response aprobarRechazarUnidadesDocumentalesDisposicion(@RequestBody List<UnidadDocumetalAGDTO> unidadDocumental ) {
        log.info("Aprobar/Rechazar UnidadesDocumentalesGatewayApi - [trafic] - aprobar rechazar unidades documentales disposicion");
        return  unidadDocumentalClient.aprobarRechazarDisposicion(unidadDocumental);
    }

    @GET
    @Path("/detalle-unidad-documental/{id}")
    @JWTTokenSecurity
    @RolesAllowed("Archivista_AC")
    public Response detalleUnidadDocumental(@PathParam("id") final String idUnidadDocumental) {
        log.info("DetalleUnidadDocumentalGatewayApi - [trafic] - detalle unidad documental");
        Response response = ecmClient.detalleUnidadDocumental(idUnidadDocumental);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/listar-documentos-por-archivar/{codigoDependencia}")
    @JWTTokenSecurity
    public Response documentosPorArchivar(@PathParam("codigoDependencia") final String codigoDependencia) {
        log.info("DocumentosPorArchivarGatewayApi - [trafic] - Listar documentos por archivar");
        Response response = ecmClient.documentosPorArchivar(codigoDependencia);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/listar-documentos-archivados/{codigoDependencia}")
    @JWTTokenSecurity
    public Response documentosArchivados(@PathParam("codigoDependencia") final String codigoDependencia) {
        log.info("DocumentosArchivadosGatewayApi - [trafic] - Listar documentos archivados");
        Response response = ecmClient.documentosArchivados(codigoDependencia);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/subir-documentos-por-archivar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @JWTTokenSecurity
    @RolesAllowed("Solicitante")
    public Response subirDocumentosPorArchivar(MultipartFormDataInput formDataInputs) {
        log.info("SubirDocumentosPorArchivarGatewayApi - [trafic] - Subir documentos por archivar");

        Response response = ecmClient.subirDocumentosPorArchivar(formDataInputs);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @PUT
    @Path("/modificar-unidades-documentales")
    @JWTTokenSecurity
    @RolesAllowed("Archivista")
    public Response modificarUnidadesDocumentales(@RequestBody List<UnidadDocumentalDTO> unidadesDocumentalesDTO) {
        log.info("ModificarUnidadesDocumentalesGatewayApi - [trafic] - Modificar Unidades Documentales");
        Response response = ecmClient.modificarUnidadesDocumentales(unidadesDocumentalesDTO);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @PUT
    @Path("/transformar-documento-html-pdf/{tipoPlantilla}")
    @JWTTokenSecurity
    public Response transformarDocumentoPdf(@PathParam("tipoPlantilla") String tipoPlantilla, String htmlContent) {
        log.info("ModificarUnidadesDocumentalesGatewayApi - [trafic] - Modificar Unidades Documentales");
        Response response = ecmClient.transformarDocumentoPdf(tipoPlantilla, htmlContent);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/subir-documentos-unidad-documental")
    @JWTTokenSecurity
    public Response subirDocumentosUnidadDocumental(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO) {
        log.info("SubirDocumentosUnidadDocumentalGatewayApi - [trafic] - Subir Documentos a Unidades Documentales");
        Response response = ecmClient.subirDocumentosUnidadDocumental(unidadDocumentalDTO);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/restablecer-archivar-documento-task/{proceso}/{tarea}")
    @JWTTokenSecurity
    public Response restablecerArchivarDocumentoTask(@PathParam("proceso") final String idproceso, @PathParam("tarea") final String idtarea) {
        log.info("UnidadDocumentalGatewayApi - [trafic] - Restableciendo Correspondencia Entrada. proceso:" + idproceso + " tarea :" + idtarea);
        Response response = ecmClient.restablecerArchivarDocumentoTask(idproceso, idtarea);
        String responseObject = response.readEntity(String.class);
        if (response.getStatus() == HttpStatus.NO_CONTENT.value() || response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseObject).build();

    }

    @GET
    @Path("/listar-unidades-documentales-transferir")
    @JWTTokenSecurity
    @RolesAllowed({"Archivista_AC","Aprobador_dependencia"})
    public Response listarUnidadesDocumentalesATransferir() {
        log.info("DocumentosPorArchivarGatewayApi - [trafic] - Listar documentos por archivar");

        final String tipoTransferencia = uriInfo.getQueryParameters().getFirst("tipoTransferencia");
        final String dependencyCode = uriInfo.getQueryParameters().getFirst("depCode");

        return unidadDocumentalClient.listarUnidadesTransferir(dependencyCode,tipoTransferencia);

    }

    @PUT
    @Path("/aprobar-rechazar-transferencias/{tipoTransferencia}")
    @JWTTokenSecurity
    @RolesAllowed({"Archivista_AC","Aprobador_dependencia"})
    public Response aprobarRechazarTransferenciasUnidadesDocumentales(List<UnidadDocumetalAGDTO> unidadesDocumentalesDTO,
                                                                      @PathParam("tipoTransferencia") String tipoTransferencia) {
        log.info("ModificarUnidadesDocumentalesGatewayApi - [trafic] - aprobar-rechazar-transferencias-unidades-documentales");
        log.info("unidaddes que llegan:" + unidadesDocumentalesDTO);

        return unidadDocumentalClient.aprobarRechazarTransferencia(unidadesDocumentalesDTO, tipoTransferencia);

    }

    @GET
    @Path("/listar-unidades-documentales-verificar")
    @JWTTokenSecurity
    @RolesAllowed("Archivista_AC")
    public Response listaUnidadesDocumentalesParaVerificar() {
        log.info("DocumentosPorArchivarGatewayApi - [trafic] - Listar documentos por archivar");
        final String dependencyCode = uriInfo.getQueryParameters().getFirst("depCode");
        final String numTransf = uriInfo.getQueryParameters().getFirst("numTransferencia");
        Response response = unidadDocumentalClient.listarUnidadesVerificar(dependencyCode,numTransf);

        return response;
    }

    @PUT
    @Path("/confirmar-unidades-documentales-transferidas")
    @JWTTokenSecurity
    @RolesAllowed("Archivista_AC")
    public Response confirmarUnidadesDocumentalesTransferidas(@RequestBody List<UnidadDocumetalAGDTO> unidadesDocumentalesDTO) {
        log.info("ConfirmarUnidadesDocumentalesTransferidasGatewayApi - [trafic] - Confirmar UD Transferidas");
         return  unidadDocumentalClient.verificarRechazarTransferencia(unidadesDocumentalesDTO);

    }

    @GET
    @Path("/listar-unidades-documentales-confirmadas")
    @JWTTokenSecurity
    @RolesAllowed("Archivista_AC")
    public Response listarUnidadesDocumentalesConfirmadas() {
        log.info("ListarUnidadesDocumentalesConfirmadasGatewayApi - [trafic] - Listar UD Transferidas");
        final String dependencyCode = uriInfo.getQueryParameters().getFirst("depCode");
        final String numTransf = uriInfo.getQueryParameters().getFirst("numTransferencia");
        return  unidadDocumentalClient.listarUnidadesUbicar(dependencyCode,numTransf);
    }

    @PUT
    @Path("/no-tramitar-unidad-documental")
    @JWTTokenSecurity
    @RolesAllowed("Archivista")
    public Response noTramitarUnidadDocumental(NoTramiteRequestDTO noTramiteRequestDTO){

        Response response = unidadDocumentalClient.noTramitarSolicitudUnidadDocumental(noTramiteRequestDTO);

        return Response.status(response.getStatus()).entity(response.readEntity(String.class)).build();
    }

    @PUT
    @Path("/actualizar-solicitud-unidad-documental")
    @JWTTokenSecurity
    public Response actualizarSolicitudUnidadDocumental(SolicitudUnidadDocumentalDTO solicitudUnidadDocumentalDTO){

        Response response = unidadDocumentalClient.actualizarSolicitudUnidadDcoumental(solicitudUnidadDocumentalDTO);

        return Response.status(response.getStatus()).entity(response.readEntity(String.class)).build();
    }

    @GET
    @Path("/buscar-unidades-documentales")
    @JWTTokenSecurity
    @RolesAllowed({"Archivista","Archivista_AC","Solicitante"})
    public Response buscarUnidadesDocumentales(@QueryParam("codigoSerie") String codSerie,@QueryParam("codigoSubSerie") String codSubSerie,@QueryParam("id") String idUnidad,
                                               @QueryParam("nombreUnidadDocumental") String nombreUnidad,@QueryParam("descriptor1") String descriptor1,@QueryParam("descriptor2") String descriptor2,
                                               @QueryParam("accion") String accion,@QueryParam("codigoDependencia") String codOrg){

        log.info("Buscando unidades documentales");

      return  unidadDocumentalClient.buscarUnidadesDocumentales(codSerie, codSubSerie, idUnidad, nombreUnidad, descriptor1, descriptor2, accion, codOrg);

    }

    @GET
    @Path("/listar-solicitud-ud-tramitadas")
    @JWTTokenSecurity
    @RolesAllowed("Archivista")
    public Response listarSolicitudUnidadTramitadas(@QueryParam("codSede") final String codSede,
                                                    @QueryParam("codDependencia") final String codDependencia,
                                                    @QueryParam("idSolicitante") final String idSolicitante) {

        log.info("UnidadDocumentalGatewayApi: Listar Solicitude de Unidaddes documentales");

       return  unidadDocumentalClient.listaUnidadesSolicitadas(codSede, codDependencia, idSolicitante);
    }

    @GET
    @Path("/obtener-documentos-ud/{idEcm}")
    @JWTTokenSecurity
    public Response obtenerDocumentosUnidadDocumental(@PathParam("idEcm") String ideEcm) {

        log.info("UnidadDocumentalGatewayApi: Listar Solicitude de Unidaddes documentales");

        Response response = ecmClient.obtenerDocumentosUnidadDocumental(ideEcm);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

}
