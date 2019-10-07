package co.com.foundation.sgd.apigateway.apis;


import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/consulta-documentos-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class ConsultaDocumentosGatewayApi {

    @Autowired
    private ECMClient ecmClient;

    public ConsultaDocumentosGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/consultar-documentos")
    @JWTTokenSecurity
    @RolesAllowed({"Consulta público", "Consulta reservado", "Consulta clasificado"})
    public Response consultarDocumentos(@QueryParam("usuario") String userLogin,
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

        log.info("UnidadDocumentalGatewayApi: Listar Solicitude de Unidaddes documentales");

        Response response = ecmClient.consultarDocumento(userLogin, startDate, endDate, tipoComunicacion, nroRadicado, nroIdentificacion,
                depOrigen, depDestino, depCode, sCode, ssCode, nroGuia, nombre, asunto, solicitante, destinatario, tipoDocumento, tramite, evento, actuacion, tipologiaDocumental);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/consultar-expediente")
    @JWTTokenSecurity
    @RolesAllowed({"Consulta público", "Consulta reservado", "Consulta clasificado"})
    public Response consultarExpediente(@QueryParam("usuario") final String usuario,
                                        @QueryParam("depJ") final String codSede,
                                        @QueryParam("depP") final String codDependencia,
                                        @QueryParam("sCode") final String serie,
                                        @QueryParam("ssCode") final String subSerie,
                                        @QueryParam("udId") final String idUnidadDocumental,
                                        @QueryParam("udName") final String nombreUnidadDocumental,
                                        @QueryParam("desc1") final String descriptor1,
                                        @QueryParam("desc2") final String descriptor2
    ) {

        log.info("UnidadDocumentalGatewayApi: Listar Solicitude de Unidaddes documentales");

        Response response = ecmClient.consultarExpediente(usuario, codSede, codDependencia, serie, subSerie, idUnidadDocumental, nombreUnidadDocumental, descriptor1, descriptor2);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/ver-detalles-documento/{idDocumento}")
    @JWTTokenSecurity
    @RolesAllowed({"Consulta público", "Consulta reservado", "Consulta clasificado"})
    public Response verDetallesDocumento(@PathParam("idDocumento") final String idDcoumento) {

        log.info("UnidadDocumentalGatewayApi: Listar Solicitude de Unidaddes documentales");

        Response response = ecmClient.verDetalleDocumento(idDcoumento);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/documentos-por-expediente/{idExpediente}")
    @JWTTokenSecurity
    @RolesAllowed({"Consulta público", "Consulta reservado", "Consulta clasificado"})
    public Response consultarDocumentosPorExpediente(@PathParam("idExpediente") final String idExpediente) {

        log.info("UnidadDocumentalGatewayApi: Listar Solicitude de Unidaddes documentales");

        Response response = ecmClient.consultarDocumentosPorExpediente(idExpediente);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/descargar-pdf/{idEcm}")

      public Response obtenerDocumentoPdf(@PathParam("idEcm") final String ideEcm,@QueryParam("nombreDocumento") @DefaultValue("documento") String nombreDocumento) {

        log.info("UnidadDocumentalGatewayApi: Listar Solicitude de Unidaddes documentales");

        Response response = ecmClient.obtenerDocumentoPdf(ideEcm);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus())
                .entity(responseContent)
                .header("Content-Type","application/pdf")
                .header("Content-Disposition","inline;filename='"+nombreDocumento+".pdf'")
                .build();
    }
}
