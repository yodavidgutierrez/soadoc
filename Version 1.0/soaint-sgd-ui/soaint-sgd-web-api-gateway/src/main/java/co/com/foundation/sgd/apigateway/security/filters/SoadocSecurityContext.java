package co.com.foundation.sgd.apigateway.security.filters;

import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Log4j2
public class SoadocSecurityContext  implements SecurityContext {

    private final String username;

    private String funcionarioEndpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_FUNCIONARIO_SERVICE_ENDPOINT_URL);

    public SoadocSecurityContext(String username) {
        this.username = username;
    }

    @Override
    public Principal getUserPrincipal() {
        return new Principal() {
            @Override
            public String getName() {
                return username;
            }
        };

    }

    @Override
    public boolean isUserInRole(String s) {

        log.info("rol en cuestión :" + s);


        Response response = getAuthenticatedFuncionario(this.username);

        FuncionariosDTO funcionariosDTO = response.readEntity(FuncionariosDTO.class);

        if(funcionariosDTO.getFuncionarios().size() == 0) {
            return false;
        }

        FuncionarioDTO funcionarioDTO = funcionariosDTO.getFuncionarios().get(0);

        if(s == null)
             return true;
        return funcionarioDTO.getRoles().parallelStream().anyMatch( rolDTO -> rolDTO.getRol().equals(s));
    }

    @Override
    public boolean isSecure() {
        return true;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }

    private Response getAuthenticatedFuncionario(String loginNames){

        WebTarget wt = ClientBuilder.newClient().target(funcionarioEndpoint);
        return wt.path("/funcionarios-web-api/funcionarios/listar-by-login-names/")
                .queryParam("login_names", loginNames)
                .request()
                .get();
    }
}
