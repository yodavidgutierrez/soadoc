package co.com.soaint.funcionario.apis.delegator.security;

import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.infrastructure.ApiDelegator;
import co.com.soaint.funcionario.util.SystemParameters;
import com.soaint.services.security_cartridge._1_0.*;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by esanchez on 8/28/2017.
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class SecurityApiClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.SECURITY_FOUNDATION_SERVICE_ENDPOINT_URL);

    /**
     * @param rol
     * @return
     * @throws SystemException
     */
    public List<FuncionarioDTO> listarUsusriosByRol(String rol) throws SystemException {
        List<FuncionarioDTO> funcionarios = new ArrayList<>();
        /*try {
            SecurityAPIService securityApiService = getSecutrityApiService();
            OperationPrincipalContextListStatus respuesta = securityApiService.getSecurityAPIPort().obtenerUsuariosporRol(rol);
            if (respuesta.isSuccessful())
                respuesta.getUsuarios().getUsuario().forEach(usuario -> {
                    FuncionarioDTO funcionario = FuncionarioDTO.newInstance()
                            .loginName(usuario.getUsername())
                            .roles(new ArrayList<>())
                            .build();
                    usuario.getRoles().getRol().forEach(uRol -> funcionario.getRoles().add(RolDTO.newInstance().rol(uRol.getName()).build()));
                    funcionarios.add(funcionario);
                });
            return funcionarios;
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }*/
        return funcionarios;
    }

    /**
     *
     * @param credenciales
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public FuncionarioDTO verificarCredenciales(CredencialesDTO credenciales) throws BusinessException, SystemException {
        FuncionarioDTO funcionario;
        try {
            final PrincipalContext principalContext = new PrincipalContext();

            principalContext.setUsername(credenciales.getLoginName());
            principalContext.setPassword(credenciales.getPassword());
            final WebTarget wt = ClientBuilder.newClient().target(endpoint);
            Response response = wt
                    .path("/login")
                    .request()
                    .buildPost(Entity.json(principalContext)).invoke();
            if (response.getStatus() == 200) {
                AuthenticationResponseContext respuesta = response.readEntity(AuthenticationResponseContext.class);
                if (respuesta.isSuccessful()) {
                    funcionario = FuncionarioDTO.newInstance()
                            .loginName(respuesta.getPrincipalContext().getUsername())
                            .roles(new ArrayList<>())
                            .build();
                    respuesta.getPrincipalContext().getRoles().getRol()
                            .forEach(rol -> funcionario.getRoles().add(RolDTO.newInstance().rol(rol.getName()).build()));
                    return funcionario;
                }
                else
                    throw ExceptionBuilder.newBuilder()
                            .withMessage("funcionario.autentication_failed")
                            .buildBusinessException();
            }
            return null;

        } catch (BusinessException e) {
            log.error("Api Delegator - a business error has occurred", e);
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
     *
     * @param funcionario
     * @throws BusinessException
     * @throws SystemException
     */
    public void crearFuncionario(FuncionarioDTO funcionario)throws BusinessException, SystemException{
        /*try {
            SecurityAPIService securityApiService = getSecutrityApiService();
            OperationPrincipalStatusContext respuesta = securityApiService.getSecurityAPIPort().crearUsuario(transformToPrincipalContext(funcionario));
            if (!respuesta.isSuccessful())
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.creation_failed")
                        .buildBusinessException();
        } catch (BusinessException e) {
            log.error("Api Delegator - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }*/
    }

    /**
     *
     * @param funcionario
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarFuncionario(FuncionarioDTO funcionario)throws BusinessException, SystemException{
        /*try {
            SecurityAPIService securityApiService = getSecutrityApiService();
            OperationStatus respuesta = securityApiService.getSecurityAPIPort().actualizarUsuario(transformToPrincipalContext(funcionario));
            if (!respuesta.isSuccessful())
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.update_failed")
                        .buildBusinessException();
        } catch (BusinessException e) {
            log.error("Api Delegator - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }*/
    }

    /**
     *
     * @param uid
     * @throws BusinessException
     * @throws SystemException
     */
    public void eliminarFuncionario(String uid)throws BusinessException, SystemException{
        /*try {
            SecurityAPIService securityApiService = getSecutrityApiService();
            OperationStatus respuesta = securityApiService.getSecurityAPIPort().eliminarUsuarioporNombre(uid);
            if (!respuesta.isSuccessful())
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.delete_failed")
                        .buildBusinessException();
        } catch (BusinessException e) {
            log.error("Api Delegator - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }*/
    }

    public List<RolDTO> obtenerRoles() throws SystemException {
        try {
            final WebTarget wt = ClientBuilder.newClient().target(endpoint);
            Response response = wt
                    .path("/getAllRoles")
                    .request()
                    .get();
            if (response.getStatus() == 200) {
                Roles roles = response.readEntity(Roles.class);
                return roles.getRol().stream().map(rol -> RolDTO.newInstance()
                        .rol(rol.getName())
                        .build()).collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (Exception ex) {
            throw new SystemException(ex.getMessage());
        }
    }

    public List<RolDTO> obtenerRolesUsuario(String loginName) throws SystemException {

        final WebTarget wt = ClientBuilder.newClient().target(endpoint);
        Response response = wt
                .path("/rolesByUser/" + loginName)
                .request()
                .get();
        if (response.getStatus() == 200) {
            PrincipalContext respuesta = response.readEntity(PrincipalContext.class);
            return respuesta.getRoles().getRol().stream()
                    .map(rol -> new RolDTO(rol.getName())).collect(Collectors.toList());
        }
        throw new SystemException("No se pudo establecer la conexion con el modulo de seguridad");
    }

    /*private SecurityAPIService getSecutrityApiService()throws MalformedURLException{
        return new SecurityAPIService(new URL(endpoint));
    }*/

    private PrincipalContext transformToPrincipalContext(FuncionarioDTO funcionario){
        PrincipalContext usuario = new PrincipalContext();
        usuario.setUsername(funcionario.getLoginName());
        usuario.setFirstName(funcionario.getNomFuncionario());
        usuario.setLastName(StringUtils.defaultString(funcionario.getValApellido1(), "") + " " + StringUtils.defaultString(funcionario.getValApellido2(), ""));
        usuario.setEmail(funcionario.getCorrElectronico());
        usuario.setPassword(funcionario.getPassword());

        if(funcionario.getRoles() != null){
            Roles roles = new Roles();
            for(RolDTO rolDTO : funcionario.getRoles()){
                Rol rol = new Rol();
                rol.setName(rolDTO.getRol());
                roles.getRol().add(rol);
            }
            usuario.setRoles(roles);
        }

        return usuario;
    }



}
