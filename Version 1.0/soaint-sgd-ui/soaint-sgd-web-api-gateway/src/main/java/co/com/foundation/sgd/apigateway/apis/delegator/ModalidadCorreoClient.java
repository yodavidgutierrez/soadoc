package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2

public class ModalidadCorreoClient  {

    private String endpoint  =  SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    @Value("${contants.modalidadcorreo.value}")
    private String tipoValue = "";

    public ModalidadCorreoClient() {
        super();
    }

    public Response list() {
        log.info("ModalidadCorreo - [trafic] - listing ModalidadCorreo with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/constantes-web-api/constantes/hijos/" + tipoValue + "/A")
                .request()
                .get();
    }


}
