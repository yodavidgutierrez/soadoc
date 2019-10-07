package co.com.foundation.sgd.apigateway.apis.delegator;


import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class AgenteClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);


    public AgenteClient(){ super();}

    public Response getAgenteByIdentification(String nroIdentificacion,String tipoPersona){
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/agente-web-api/agente/" + nroIdentificacion + "/"+ tipoPersona)
                .request()
                .get();
    }
}
