package co.com.foundation.sgd.apigateway.apis;


import co.com.foundation.sgd.apigateway.apis.delegator.EstadoEntregaClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/estado-entrega-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class EstadoEntregaGatewayApi {

    @Autowired
    EstadoEntregaClient client;

    public EstadoEntregaGatewayApi(){
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/salida")
    @JWTTokenSecurity
    public Response listSalida() {

        log.info("EstadoEntregaGatewayApi - [trafic] - listing EstadoEntregaGatewayApi");
        Response response = client.listSalida();
        String responseContent = response.readEntity(String.class);
        log.info("EstadoEntregaGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/entrada")
    @JWTTokenSecurity
    public Response listEntrada() {

        log.info("EstadoEntregaGatewayApi - [trafic] - listing EstadoEntregaGatewayApi");
        Response response = client.listEntrada();
        String responseContent = response.readEntity(String.class);
        log.info("EstadoEntregaGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }
}
