package co.com.soaint.funcionario.apis.delegator.roles;

import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.infrastructure.ApiDelegator;
import co.com.soaint.funcionario.util.SystemParameters;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by esanchez on 8/28/2017.
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class RolesWebApiClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);



    public List<RolDTO> listarRolesByIdFuncionario(BigInteger idFuncionario) throws SystemException {
        log.info("Funcionarios - [trafic] - listing Funcionarios with endpoint: " + endpoint);
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/rol-web-api/rol/listar/id/" + idFuncionario)
                    .request()
                    .get();
            if (Response.Status.OK.getStatusCode() == respuesta.getStatus()) {
                return respuesta.readEntity(List.class);
            } else {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("Rol.error consultando servicio de negocio listarRolesByIdFuncionario")
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

    public List<RolDTO> listarRolesByLoginName(String loginName) throws SystemException {
        log.info("Funcionarios - listar Roles por login-------------------------: " + endpoint);
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/rol-web-api/rol/listar/login/" + loginName)
                    .request()
                    .get();
            if (Response.Status.OK.getStatusCode() == respuesta.getStatus()) {

               List<RolDTO> roles = respuesta.readEntity(List.class);
                log.info("Funcionarios - los roles qu devuelve-------------------------:{} " + roles);
                return  roles;
            } else {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("Rol.error consultando servicio de negocio listarRolesByLoginName")
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


    public List<RolDTO> listarRoles() throws SystemException {
        log.info("Funcionarios - [trafic] - listing Funcionarios with endpoint: " + endpoint);
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/rol-web-api/rol/listar/" )
                    .request()
                    .get();
            if (Response.Status.OK.getStatusCode() == respuesta.getStatus()) {
                return respuesta.readEntity(List.class);
            } else {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("Rol.error consultando servicio de negocio listarRoles")
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


    public String adicionarRol(List<RolDTO> roles) throws SystemException {
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/rol-web-api/rol/adicionar")
                    .request()
                    .put(Entity.json(roles));
            return (Response.Status.OK.getStatusCode() == respuesta.getStatus()) ? "1" : "0";
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
}
