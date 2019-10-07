package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.DigitalizacionClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.foundation.sgd.dto.DigilitazacionDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/digitalizacion-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class DigitalizacionGatewayApi {

    @Autowired
    private DigitalizacionClient client;

    public DigitalizacionGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/digitalizar")
    @JWTTokenSecurity
    @RolesAllowed("Radicador")
    public Response digitalizar(DigilitazacionDTO digitalizacionDto){

        log.info(digitalizacionDto);

        Response response = client.digitalizar(digitalizacionDto);

        String responseContent = response.readEntity(String.class);

        if(response.getStatus() < 400)
             return  Response.status(response.getStatus()).entity("{\"success\":true}").build();

        return Response.status(response.getStatus()).entity(responseContent).build();
    }
}
