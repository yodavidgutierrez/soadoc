package co.com.foundation.sgd.apigateway.apis.delegator;


import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@ApiDelegator
@Log4j2
public class TipoPlantillaClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);
    private String ecmEndpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ECM_SERVICE_ENDPOINT_URL);

    //private Client client = ClientBuilder.newClient();

    @Value("${contants.tipoplantilla.value}")
    private String tipoplantilla = "";

    @Value("${locations.tiposplantilla.root}")
    private String location_root = "";

    public TipoPlantillaClient() {
        super();
    }

    public Response get(String codClasificacion, String estado) {
        log.info("TipoPlantilla - [trafic] - listing TipoPlantilla with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/plantilla-web-api/plantilla/" + codClasificacion + "/" +estado)
                .request()
                .get();
    }

    public String readFromFile(String codigo) {
        /*String location = location_root.concat(codigo).concat(".html");
        return new String(Files.readAllBytes(Paths.get(location)));*/
        log.info("TipoPlantilla - [trafic] - listing TipoPlantilla with endpoint: " + ecmEndpoint);
        try {
            final WebTarget wt = ClientBuilder.newClient().target(ecmEndpoint);
            final Response response = wt.path("/loadTemplate/" + codigo).request()
                    .get();
            return response.getStatus() == 200 ? response.readEntity(String.class) : "Unable to load file " + codigo + ".html";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

}
