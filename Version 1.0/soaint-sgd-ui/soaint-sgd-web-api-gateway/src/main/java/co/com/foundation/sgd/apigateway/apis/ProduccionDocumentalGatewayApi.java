package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ProduccionDocumentalClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Path("/produccion-documental-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class ProduccionDocumentalGatewayApi {

    private static final String CONTENT = "ProduccionDocumentalGatewayApi - [content] : ";

    @Autowired
    private ProduccionDocumentalClient client;

    @Autowired
    private ECMClient clientECM;


    public ProduccionDocumentalGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/ejecutar-proyeccion-multiple")
/*    @JWTTokenSecurity
    @RolesAllowed("Proyector")*/
    public Response ejecutarProyeccionMultiple(EntradaProcesoDTO entrada) {

            Response response = client.ejecutarProyeccionMultiple(entrada);

            if(response.getStatus() >= 400)
                 return  Response.status(response.getStatus()).build();

            String responseObject = response.readEntity(String.class);
            if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
                return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
            }
            log.info("\n\rENDED");
            return Response.status(response.getStatus()).entity(responseObject).build();

    }

    @GET
    @Path("/datos-documento/{nro_radicado}")
    @JWTTokenSecurity
    public Response obtenerDatosDocumento(@PathParam("nro_radicado") String nro_radicado) {
        Response response = client.obtenerDatosDocumentoPorNroRadicado(nro_radicado);
        String responseObject = response.readEntity(String.class);
        if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        log.info("\n\rENDED");
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/versionar-documento")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @JWTTokenSecurity
    @RolesAllowed("Proyector")
    public Response versionarDocumento(MultipartFormDataInput formDataInput) {

        log.info("ProduccionDocumentalGatewayApi - [content] : Subir version documento");

        MensajeRespuesta clientResponse = null;
        DocumentoDTO documentoDTO = new DocumentoDTO();
        try {
            InputStream inputStream = formDataInput.getFormDataPart("documento", InputStream.class, null);
            documentoDTO.setDocumento(IOUtils.toByteArray(inputStream));

            if (null != formDataInput.getFormDataPart("idDocumento", String.class, null)) {
                documentoDTO.setIdDocumento(formDataInput.getFormDataPart("idDocumento", String.class, null));
            }

            documentoDTO.setNombreDocumento(URLDecoder.decode(formDataInput.getFormDataPart("nombreDocumento", String.class, null), "UTF-8"));
            log.info("-------------------------Nombre del documento: " + documentoDTO.getNombreDocumento());
            documentoDTO.setTipoDocumento(formDataInput.getFormDataPart("tipoDocumento", String.class, null));
            documentoDTO.setSede(formDataInput.getFormDataPart("sede", String.class, null));
            documentoDTO.setCodigoDependencia(formDataInput.getFormDataPart("codigoDependencia", String.class, null));
            documentoDTO.setDependencia(formDataInput.getFormDataPart("dependencia", String.class, null));
            String selector = formDataInput.getFormDataPart("selector", String.class, null);
            if (0 < formDataInput.getFormDataPart("nroRadicado", String.class, null).length()) {
                documentoDTO.setNroRadicado(formDataInput.getFormDataPart("nroRadicado", String.class, null));
            }

            clientResponse = this.clientECM.uploadVersionDocumento(documentoDTO, selector);
            log.info(clientResponse);

        } catch (Exception ex) {
            log.error("Exepcion arrojada en la captura de datos del multipart");
            return this.EcmErrorMessage(ex);
        }

        return Response.status(Response.Status.OK).entity(clientResponse).build();
    }

    @POST
    @Path("/agregar-anexo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @JWTTokenSecurity
    @RolesAllowed("Proyector")
    public Response agregarAnexo(MultipartFormDataInput formDataInput) {

        log.info("ProduccionDocumentalGatewayApi - [content] : Subir anexo");

        MensajeRespuesta clientResponse = null;
        DocumentoDTO documentoDTO = new DocumentoDTO();
        try {
            InputStream inputStream = formDataInput.getFormDataPart("documento", InputStream.class, null);
            documentoDTO.setDocumento(IOUtils.toByteArray(inputStream));

            documentoDTO.setIdDocumentoPadre(formDataInput.getFormDataPart("idDocumentoPadre", String.class, null));
            documentoDTO.setNombreDocumento(formDataInput.getFormDataPart("nombreDocumento", String.class, null));
            documentoDTO.setTipoDocumento(formDataInput.getFormDataPart("tipoDocumento", String.class, null));
            documentoDTO.setSede(formDataInput.getFormDataPart("sede", String.class, null));
            documentoDTO.setDependencia(formDataInput.getFormDataPart("dependencia", String.class, null));
            documentoDTO.setCodigoDependencia(formDataInput.getFormDataPart("codigoDependencia", String.class, null));
            documentoDTO.setNroRadicado(formDataInput.getFormDataPart("nroRadicado", String.class, null));

            //clientResponse = this.clientECM.uploadVersionDocumento(documentoDTO, "PD");
            clientResponse = clientECM.uploadDocumentoAnexo(documentoDTO);
            //log.info(clientResponse);

        } catch (Exception ex) {
            return this.EcmErrorMessage(ex);
        }

        return Response.status(Response.Status.OK).entity(clientResponse).build();
    }


    @DELETE
    @Path("/eliminar-version/{identificadorDoc}")
    @JWTTokenSecurity
    @RolesAllowed("Proyector")
    public Response eliminarVersionDocumento(@PathParam("identificadorDoc") String identificadorDoc) {
        boolean response = true;

        try {
            response = this.clientECM.eliminarVersionDocumento(identificadorDoc);
        } catch (Exception ex) {
            return this.EcmErrorMessage(ex);
        }

        return this.EcmBooleanResponse(response);
    }

    @DELETE
    @Path("/eliminar-anexo/{identificadorDoc}")
    @JWTTokenSecurity
    @RolesAllowed("Proyector")
    public Response eliminarAnexo(@PathParam("identificadorDoc") String identificadorDoc) {
        return this.eliminarVersionDocumento(identificadorDoc);
    }


    @GET
    @Path("/obtener-versiones-documento/{identificadorDoc}")
    @JWTTokenSecurity
    @RolesAllowed({"Proyector","Aprobador","Revisor"})
    public Response obtenerVersionesDocumento(@PathParam("identificadorDoc") String identificadorDoc) {
        MensajeRespuesta response = null;
        try {
            response = this.clientECM.obtenerVersionesDocumento(identificadorDoc);
        } catch (Exception ex) {
            return this.EcmErrorMessage(ex);
        }

        return Response.status(Response.Status.OK).entity(response).build();
    }



    private Response EcmErrorMessage(@NotNull Exception ex) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("codMensaje","9999");
        jsonResponse.put("mensaje",ex.getMessage());

        return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse.toJSONString()).build();
    }

    private Response EcmBooleanResponse(boolean response) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("codMensaje","0000");
        jsonResponse.put("mensaje", "El documento fue eliminado satisfactoriamente");

        return Response.status(Response.Status.OK).entity(jsonResponse.toJSONString()).build();
    }
}
