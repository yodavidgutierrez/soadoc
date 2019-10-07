package co.com.foundation.sgd.apigateway.apis;


import co.com.foundation.sgd.apigateway.apis.delegator.AgenteClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/agente-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class AgenteGatewayApi {

    @Autowired
    private  AgenteClient client;

    public AgenteGatewayApi(){
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/obtener-agente-por-identificacion/{nroIdentificacion}/{tipoPersona}")
    @JWTTokenSecurity
    public Response getAgenteByIdentification(@PathParam("nroIdentificacion") String nroIdentificacion,@PathParam("tipoPersona") String tipoPersona){

        Response response = client.getAgenteByIdentification(nroIdentificacion, tipoPersona);
        String responseContent = response.readEntity(String.class);
        log.info( responseContent);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }
}
