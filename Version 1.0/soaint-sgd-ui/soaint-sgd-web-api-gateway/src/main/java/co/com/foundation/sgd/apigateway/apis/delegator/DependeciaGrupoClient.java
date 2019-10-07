package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class DependeciaGrupoClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    //private Client client = ClientBuilder.newClient();

    public DependeciaGrupoClient() {
        super();
    }

    public Response listBySedeAdministrativa(String codigoSedeAdministrativa) {
        log.info("DependeciaGrupo - [trafic] - listing DependeciaGrupo with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/organigrama-web-api/organigrama/dependencias-check/" + codigoSedeAdministrativa)
                .request()
                .get();
    }

    public Response obtenerPorCodigo(String codigo) {
        log.info("DependeciaGrupo - [trafic] - Get by code with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/dependencia-web-api/dependencia/".concat(codigo))
                .request()
                .get();
    }

    public Response obtenerPorDependencias(String codigosDependencia) {
        log.info("DependeciaGrupo - [trafic] - listing DependeciaGrupo with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/dependencia-web-api/dependencia")
                .queryParam("codigos", codigosDependencia)
                .request()
                .get();
    }

    public Response listarDependencias() {
        log.info("DependeciaGrupo - [trafic] - listing Dependecias with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/dependencia-web-api/dependencias")
                .request()
                .get();
    }

}
