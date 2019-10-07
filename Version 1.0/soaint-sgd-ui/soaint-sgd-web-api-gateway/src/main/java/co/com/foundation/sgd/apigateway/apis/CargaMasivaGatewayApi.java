package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.CargaMasivaClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Path("/carga-masiva-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class CargaMasivaGatewayApi {
    private static final String CONTENT = "CargaMasivaGatewayApi - [content] : ";
    @Autowired
    private CargaMasivaClient client;

    public CargaMasivaGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/listadocargamasiva")
    @JWTTokenSecurity
    @RolesAllowed("Radicador_contingencia")
    public Response listCargaMasiva() {
        log.info("CargaMasivaGatewayApi - [trafic] - listing Carga Masiva");
        Response response = client.listCargaMasiva();
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/estadocargamasiva")
    @JWTTokenSecurity
    @RolesAllowed("Radicador_contingencia")
    public Response listEstadoCargaMasiva() {
        log.info("CargaMasivaGatewayApi - [trafic] - listing Estado Carga Masiva");
        Response response = client.listEstadoCargaMasiva();
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/estadocargamasiva/{id}")
//    @JWTTokenSecurity
    public Response listEstadoCargaMasivaDadoId(@PathParam("id") String id) {
        log.info("CargaMasivaGatewayApi - [trafic] - listing Estado Carga Masiva dado Id");
        Response response = client.listEstadoCargaMasivaDadoId(id);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }


    @POST
    @Path("/cargar-fichero/{codigoSede}/{codigoDependencia}/{codfunRadica}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @JWTTokenSecurity
    public Response cargarDocumento(@PathParam("codigoSede") String codigoSede,
                                    @PathParam("codigoDependencia") String codigoDependencia,
                                    @PathParam("codfunRadica") String codfunRadica,
                                    MultipartFormDataInput file) {
        final String[] responseContent = {""};
        final int[] estadoRespuesta = {0};
        log.info("CargaMasivaGatewayApi - [trafic] - carga masiva");
        log.info("sede: ".concat(codigoSede).concat(" -|- dependencia: ").concat(codigoDependencia));


        file.getFormDataMap().forEach((key, parts) -> {
            log.info("Valor key: =====> " + key + "   Valor part: =====> " + parts);
            parts.forEach((part) -> {
                MultivaluedMap<String, String> headers = part.getHeaders();
                String fileName = parseFileName(headers);
                log.info("Valor fileName: ===> " + fileName);
                if (key.equals("file")){
                    Response response = client.cargarDocumento(part, codigoSede, codigoDependencia, codfunRadica, fileName);
                    responseContent[0] = response.readEntity(String.class);
                    estadoRespuesta[0] = response.getStatus();
                }


            });
        });
        log.info("Response: ".concat(responseContent[0]));
        log.info("Estado: ".concat(String.valueOf(estadoRespuesta[0])));
        log.info(CONTENT + responseContent[0]);

        return Response.status(estadoRespuesta[0]).entity(responseContent[0]).build();
    }

    // Parse Content-Disposition header to get the original file name
    private String parseFileName(MultivaluedMap<String, String> headers) {
        String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");
        for (String name : contentDispositionHeader) {
            if ((name.trim().startsWith("filename"))) {
                String[] tmp = name.split("=");
                String fileName = tmp[1].trim().replaceAll("\"", "");
                return fileName;
            }
        }
        return "randomName";
    }

}

