package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.AnexoDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.List;

@ApiDelegator
@Log4j2
public class TipoAnexosClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    @Value("${contants.tipoanexo.value}")
    private String tipoValue = "";

    public TipoAnexosClient() {
        super();
    }

    public Response list() {
        log.info("TipoAnexos - [trafic] - listing TipoAnexos with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/constantes-web-api/constantes/hijos/" + tipoValue + "/A")
                .request()
                .get();
    }

    public Response updateAnexo(List<AnexoDTO> anexoList){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        return  wt.path("/anexo-web-api/anexo/update")
                .request()
                .put(Entity.json(anexoList));

    }

}
