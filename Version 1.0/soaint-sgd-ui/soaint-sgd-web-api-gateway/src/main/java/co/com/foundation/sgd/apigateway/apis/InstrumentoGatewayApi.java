package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.FuncionarioClient;
import co.com.foundation.sgd.apigateway.apis.delegator.InstrumentoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.TvsOrgaAdminXFunciPkDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/instrumento-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class InstrumentoGatewayApi {

    @Autowired
    private InstrumentoClient instrumentoClient;

   public InstrumentoGatewayApi(){

        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/list-dependencias")
    @JWTTokenSecurity
    public Response get() {


        Response response = instrumentoClient.listarDependencias();
        String responseContent = response.readEntity(String.class);
        log.info("InstrumentoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @PUT
    @Path("/asociar-funcionario-dependnecias")
    @JWTTokenSecurity

    public Response bindFuncionarioDependencias(@RequestBody TvsOrgaAdminXFunciPkDTO funciPkDTO){

        Response response = instrumentoClient.bindFuncionarioDependencias(funciPkDTO);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();

    }

    @GET
    @Path("/listar-series")
    @JWTTokenSecurity
    public Response getSeries() {


        Response response = instrumentoClient.listarSeries();
        String responseContent = response.readEntity(String.class);
        log.info("InstrumentoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/dependencia-radicadora")
    @JWTTokenSecurity
    public Response getDependenciaRadicadora() {

       return  instrumentoClient.obtenerDependenciaRadicadora();
      }

    @GET
    @Path("/tipologias-documentales")
    @JWTTokenSecurity
    public Response getTipologiasDocumentales(@QueryParam("codSerie") String codSerie,@QueryParam("codSubserie") String codSubserie) {

        Response response =  instrumentoClient.obtenerTipologiasDocumentales(codSerie,codSubserie);

        String content = response.readEntity(String.class);

        return Response.status(response.getStatus()).entity(content).build();
    }

    @GET
    @Path("/subseries-por-dependencia/{codDependencia}")
    @JWTTokenSecurity
    public Response getSubseriesPorDependencia(@PathParam("codDependencia") String codDependencia) {

        Response response =  instrumentoClient.listarSubseriesPorDependecia(codDependencia);

        String content = response.readEntity(String.class);

        return Response.status(response.getStatus()).entity(content).build();
    }

    @GET
    @Path("/dependencia/{codDependencia}")
    @JWTTokenSecurity
    public Response getDependenciaPorCodigo(@PathParam("codDependencia") String codDependencia) {

        Response response =  instrumentoClient.ObtenerDependenciaPorCodigo(codDependencia);

        String content = response.readEntity(String.class);

        return Response.status(response.getStatus()).entity(content).build();
    }

}



