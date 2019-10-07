package co.com.soaint.funcionario.apis.delegator.funcionarios;

import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.infrastructure.ApiDelegator;
import co.com.soaint.funcionario.util.SystemParameters;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ObjectUtils;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by esanchez on 8/28/2017.
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class FuncionariosWebApiClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    /**
     * @param codDependencia
     * @param codEstado
     * @return
     * @throws SystemException
     */
    public FuncionariosDTO listarFuncionariosByDependenciaAndEstado(String codDependencia, String codEstado) throws SystemException {
        log.info("Funcionarios - [trafic] - listing Funcionarios with endpoint: " + endpoint);
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/funcionarios-web-api/funcionarios/dependencia/" + codDependencia + "/" + codEstado)
                    .request()
                    .get();
            if (Response.Status.OK.getStatusCode() == respuesta.getStatus()) {
                return respuesta.readEntity(FuncionariosDTO.class);
            } else {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionarios.error consultando servicio de negocio GestionarFuncionarios")
                        .buildSystemException();
            }
        } catch (SystemException e) {
            log.error("Api Delegator - a api delegator error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param loginName
     * @return
     * @throws SystemException
     */
    public FuncionarioDTO listarFuncionarioByLoginName(String loginName) throws SystemException {
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/funcionarios-web-api/funcionarios/" + loginName)
                    .request()
                    .get();

            if (Response.Status.OK.getStatusCode() == respuesta.getStatus()) {
                return respuesta.readEntity(FuncionarioDTO.class);
            } else
               if (respuesta.getStatus()==204){
                   return null;
                }else
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionarios.error consultando servicio de negocio GestionarFuncionarios Response " + respuesta.getStatusInfo())
                        .buildSystemException();
        } catch (SystemException e) {
            log.error("Api Delegator - a api delegator error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public List<FuncionarioDTO> listarFuncionariosByRol(String rol) throws SystemException {
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/funcionarios-web-api/funcionarios/rol/" + rol)
                    .request()
                    .get();

            if (Response.Status.OK.getStatusCode() == respuesta.getStatus()) {
                List<FuncionarioDTO> funcionarioDTOS = respuesta.readEntity(List.class);
                return funcionarioDTOS;
            } else
            if (respuesta.getStatus()==204){
                return null;
            }else
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionarios.error consultando servicio de negocio ListarFuncionariosByRol Response " + respuesta.getStatusInfo())
                        .buildSystemException();
        } catch (SystemException e) {
            log.error("Api Delegator - a api delegator error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
    /**
     * @param loginName
     * @param estado
     * @return
     * @throws SystemException
     */
    public FuncionarioDTO listarFuncionarioByLoginNameAndEstado(String loginName, String estado) throws SystemException {
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/funcionarios-web-api/funcionarios/" + loginName + "/" + estado)
                    .request()
                    .get();
            if (Response.Status.OK.getStatusCode() == respuesta.getStatus()) {
                return respuesta.readEntity(FuncionarioDTO.class);
            } else
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionarios.error consultando servicio de negocio GestionarFuncionarios")
                        .buildSystemException();
        } catch (SystemException e) {
            log.error("Api Delegator - a api delegator error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param funcionario
     * @throws SystemException
     */
    public String crearFuncionario(FuncionarioDTO funcionario) throws SystemException {
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/funcionarios-web-api/funcionarios")
                    .request()
                    .post(Entity.json(funcionario));
            return (Response.Status.OK.getStatusCode() == respuesta.getStatus()) ? "1" : "0";
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @throws SystemException
     */
    public String actualizarFuncionario(FuncionarioDTO funcionario) throws SystemException {
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/funcionarios-web-api/funcionarios")
                    .request()
                    .put(Entity.json(funcionario));
            return (Response.Status.OK.getStatusCode() == respuesta.getStatus()) ? "1" : "0";
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    public String actualizarRolesFuncionario(FuncionarioDTO funcionario) throws SystemException {
        try {
            if(ObjectUtils.isEmpty(funcionario)){
                return "0";
            }
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/funcionarios-web-api/funcionarios/actualizar/roles")
                    .request()
                    .put(Entity.json(funcionario));
            return (Response.Status.OK.getStatusCode() == respuesta.getStatus()) ? "1" : "0";
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
    /**
     * @param funcionarioDTO
     * @return
     * @throws SystemException
     */
    public FuncionariosDTO buscarFuncionario(FuncionarioDTO funcionarioDTO) throws SystemException {
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/funcionarios-web-api/funcionarios/buscar")
                    .request()
                    .post(Entity.json(funcionarioDTO));
            return respuesta.readEntity(FuncionariosDTO.class);
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param ideFunci
     * @return
     * @throws SystemException
     */
    public FuncionarioDTO consultarFuncionarioByIdeFunci(BigInteger ideFunci) throws SystemException {
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/funcionarios-web-api/funcionarios/by-id/" + ideFunci)
                    .request()
                    .get();
            return respuesta.readEntity(FuncionarioDTO.class);
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public FuncionariosDTO listarFuncionariosByLoginNameList(String loginNames) throws SystemException{
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/funcionarios-web-api/funcionarios/listar-by-login-names/")
                    .queryParam("login_names", loginNames)
                    .request()
                    .get();
            return respuesta.readEntity(FuncionariosDTO.class);
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    private WebTarget getWebTarget() {
        return ClientBuilder.newClient().target(endpoint);
    }

    public String buscarFirmaLogin(String login) {

        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/funcionarios-web-api/funcionarios/firmaPoliticaFuncionarioByLoginName/"+login)
                    .request()
                    .get();
            return respuesta.readEntity(String.class);
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            return "4005";
        }
    }
}
