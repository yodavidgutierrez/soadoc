package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import lombok.extern.log4j.Log4j2;


@ApiDelegator
@Log4j2
public class EstadoEntregaClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    @Value("${contants.estadoentregasalida.value}")
    private String codPadreSalida = "";
 @Value("${contants.estadoentregaentrada.value}")
    private String codPadreEntrada = "";

    public Response listSalida() {
        log.info("Estado entrega - [trafic] - listing MediosRecepcion with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/constantes-web-api/constantes/hijos/" + codPadreSalida + "/A")
                .request()
                .get();
    }

     public Response listEntrada() {
        log.info("Estado entrega - [trafic] - listing MediosRecepcion with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/constantes-web-api/constantes/hijos/" + codPadreEntrada + "/A")
                .request()
                .get();
    }
}
