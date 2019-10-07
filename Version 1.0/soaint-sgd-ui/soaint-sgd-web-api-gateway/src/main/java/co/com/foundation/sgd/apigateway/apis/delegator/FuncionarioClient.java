package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@ApiDelegator
@Log4j2
public class FuncionarioClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private String funcionarioEndpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_FUNCIONARIO_SERVICE_ENDPOINT_URL);

    //private Client client = ClientBuilder.newClient();

    public FuncionarioClient() {
        super();
    }

    public Response obtenerFuncionario(String login) {
        log.info("Funcionario - [trafic] - obtener Funcionario with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/funcionarios-web-api/funcionarios/" + login + "/A")
                .request()
                .get();
    }

    public Response listarFuncionarios(String codigoDependencia) {
        log.info("Funcionario - [trafic] - obtener Funcionario with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/funcionarios-web-api/funcionarios/dependencia/" + codigoDependencia + "/A")
                .request()
                .get();
    }

    public Response listarFuncionariosByLoginnames(String loginNames) {
        log.info("Funcionario - [trafic] - obtener Funcionario with endpoint: " + funcionarioEndpoint);
        WebTarget wt = ClientBuilder.newClient().target(funcionarioEndpoint);
        return wt.path("/funcionarios-web-api/funcionarios/listar-by-login-names/")
                .queryParam("login_names", loginNames)
                .request()
                .get();
    }

    public Response listarFuncionariosPorRol(String codigoDependencia, String rol) {
        log.info("Funcionario - [trafic] - obtener Funcionario with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(funcionarioEndpoint);
        return wt.path("/funcionarios-web-api/funcionarios/" + codigoDependencia + "/" + rol + "/A")
                .request()
                .get();
    }

    public Future<Response> listarFuncionariosPorRolAsync(String codigoDependencia, String rol, InvocationCallback<Response> cb) {
        log.info("Funcionario - [trafic] - obtener Funcionario with endpoint: " + funcionarioEndpoint);
        WebTarget wt = ClientBuilder.newClient().target(funcionarioEndpoint);
        return wt.path("/funcionarios-web-api/funcionarios/" + codigoDependencia + "/" + rol + "/A")
                .request()
                .async()
                .get(cb);
    }

    // temporal implementation
    public List<FuncionarioDTO> getListaFuncionariosRolDependencia(String codigoDependencia, String rol){

        Response response = buscarFuncionario(new FuncionarioDTO());
          if(response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return new ArrayList<>();

        List<FuncionarioDTO> responseContent = response.readEntity(FuncionariosDTO.class).getFuncionarios();

        log.info("funcionarios :" + responseContent );
        return filterFuncionariosRolDependencia(codigoDependencia,rol,responseContent);
    }

    public List<FuncionarioDTO> filterFuncionariosRolDependencia(String codigoDependencia, String rol,List<FuncionarioDTO> funcionariosFilter){

        try {
            List<FuncionarioDTO> funcionarioFiltred = funcionariosFilter.stream().filter(funcionarioDTO ->{
                     if(funcionarioDTO.getDependencias() == null || funcionarioDTO.getRoles() == null)
                         return false;
                     return  funcionarioDTO.getDependencias().stream().anyMatch(dependenciaDTO -> dependenciaDTO.getCodDependencia().equals(codigoDependencia))
                             && funcionarioDTO.getRoles().stream().anyMatch(rolDTO -> rolDTO.getRol().equals(rol));
                    }

            ).collect(Collectors.toList());

            log.info("funcionarios filtrados:" + funcionarioFiltred);

            return funcionarioFiltred;
        }
        catch (Exception exception){

            log.info("Excepcion al filtrar:"+exception.toString());

            return  new ArrayList<>();
        }
    }

    public List<FuncionarioDTO> filterFuncionariosByRol( String rol,List<FuncionarioDTO> funcionariosFilter){

        try {
            List<FuncionarioDTO> funcionarioFiltred = funcionariosFilter.stream().filter(funcionarioDTO ->{
                     if( funcionarioDTO.getRoles() == null)
                         return false;
                     return   funcionarioDTO.getRoles().stream().anyMatch(rolDTO -> rolDTO.getRol().equals(rol));
                    }

            ).collect(Collectors.toList());

            log.info("funcionarios filtrados:" + funcionarioFiltred);

            return funcionarioFiltred;
        }
        catch (Exception exception){

            log.info("Excepcion al filtrar:"+exception.toString());

            return  new ArrayList<>();
        }
    }

    public Response updateFuncionarioRoles(FuncionarioDTO funcionarioDTO){
        log.info("Funcionario - [trafic] - update Funcionario Roles with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/funcionarios-web-api/funcionarios/actualizar/roles").request().buildPut(Entity.json(funcionarioDTO)).invoke();
    }

    public Response buscarFuncionario(FuncionarioDTO funcionarioDTO){
        log.info("Funcionario - [trafic] - buscar Funcionarios with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(funcionarioEndpoint);
        return wt.path("/funcionarios-web-api/funcionarios/buscar-funcionarios").request().buildPost(Entity.json(funcionarioDTO)).invoke();
    }

    public Future<Response> buscarFuncionarioAsync(FuncionarioDTO funcionarioDTO,InvocationCallback<Response> cb){
        log.info("Funcionario - [trafic] - buscar Funcionarios with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(funcionarioEndpoint);
        return wt.path("/funcionarios-web-api/funcionarios/buscar-funcionarios")
                 .request()
                 .async()
                .post(Entity.json(funcionarioDTO),cb);
    }

    public Response updateFuncionario(FuncionarioDTO funcionarioDTO){
        log.info("Funcionario - [trafic] - update Funcionario Roles with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/funcionarios-web-api/funcionarios/actualizar").request().buildPut(Entity.json(funcionarioDTO)).invoke();
    }

    public String buscarFirmaLogin(String loginName) {
        log.info("Funcionario - [trafic] - buscar FirmaLogin: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        Response response = wt.path("/funcionarios-web-api/funcionarios/firmaPoliticaFuncionarioByLoginName/"+loginName)
                .request()
                .get();

            String respuesta = response.readEntity(String.class);
        return respuesta;
    }
}
