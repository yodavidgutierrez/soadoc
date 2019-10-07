package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import co.com.soaint.foundation.canonical.correspondencia.ObservacionDTO;
import lombok.extern.log4j.Log4j2;

import java.util.List;


@ApiDelegator
@Log4j2
public class PdObservacionesClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

   public  PdObservacionesClient(){

    }

  public   Response listarObserciones(String idInstancia){
        log.info("Prefijo Cuadrante - [trafic] - listing Observaciones with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/observacion-web-api/observacion/" + idInstancia)
                .request()
                .get();
    }


    public Response actualizarObservaciones(List<ObservacionDTO> observaciones){


        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("observacion-web-api/observacion/actualizar")
                .request()
                .put(Entity.json(observaciones));

    }
}
