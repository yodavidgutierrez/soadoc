package co.com.foundation.sgd.apigateway.apis;


import co.com.foundation.sgd.apigateway.apis.delegator.PdObservacionesClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.correspondencia.ObservacionDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/pd-observaciones-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class PdObservacionesGatewayApi {


    @Autowired
    private  PdObservacionesClient client;

   public   PdObservacionesGatewayApi(){
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/listar/{idInstancia}")
    @JWTTokenSecurity
    @RolesAllowed({"Aprobador","Revisor","Proyector"})
   public  Response listarOnservacionesPorInstancia(@PathParam("idInstancia") String idInstancia){

        log.info("PrefijoCuadranteGatewayApi - [trafic] - listing PrefijoCuadrante");
        Response response = client.listarObserciones(idInstancia);
        String responseContent = response.readEntity(String.class);
        log.info("PrefijoCuadranteGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @PUT
    @Path("/actualizar-observaciones")
    @JWTTokenSecurity
    @RolesAllowed({"Aprobador","Revisor","Proyector"})
    public  Response actualizar(List<ObservacionDTO> observaciones){

        log.info("PrefijoCuadranteGatewayApi - [trafic] - listing PrefijoCuadrante");
        Response response = client.actualizarObservaciones(observaciones);
        String responseContent = response.readEntity(String.class);
        log.info("PrefijoCuadranteGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }


}
