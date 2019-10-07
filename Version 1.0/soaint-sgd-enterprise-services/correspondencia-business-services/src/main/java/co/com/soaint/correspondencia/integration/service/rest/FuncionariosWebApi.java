package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarFuncionarios;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import java.math.BigInteger;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Path("/funcionarios-web-api")
@Produces({"application/json", "application/xml"})
@Log4j2
@Api(value = "FuncionariosWebApi")
public class FuncionariosWebApi {

    @Autowired
    private GestionarFuncionarios boundary;

    /**
     * Constructor
     */
    public FuncionariosWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param loginName
     * @param estado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/funcionarios/{login_name}/{estado}")
    public FuncionarioDTO listarFuncionarioByLoginNameAndEstado(@PathParam("login_name") final String loginName, @PathParam("estado") final String estado) throws BusinessException, SystemException {
        log.info("processing rest request - listar funcionarios por login_name and estado");
        return boundary.listarFuncionarioByLoginNameAndEstado(loginName, estado);
    }

    /**
     * @param loginNames
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/funcionarios/listar-by-login-names")
    public FuncionariosDTO listarFuncionariosByLoginNameList(@QueryParam("login_names") final String loginNames) throws SystemException {
        log.info("processing rest request - listar funcionarios por loginName list");
        return boundary.listarFuncionariosByLoginNameList(loginNames.split(","));
    }

    /**
     * @param codDependencia
     * @param codEstado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/funcionarios/dependencia/{cod_dependencia}/{cod_estado}")
    public FuncionariosDTO listarFuncionariosByCodDependenciaAndCodEstado(@PathParam("cod_dependencia") final String codDependencia, @PathParam("cod_estado") final String codEstado) throws SystemException {
        log.info("processing rest request - listar funcionarios por dependencia");
        return boundary.listarFuncionariosByCodDependenciaAndCodEstado(codDependencia, codEstado);
    }

    /**
     * @param loginName
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/funcionarios/{login_name}")
    public FuncionarioDTO listarFuncionarioByLoginName(@PathParam("login_name") final String loginName) throws SystemException {
        log.info("processing rest request - listar funcionarios por login_name");
        return boundary.listarFuncionarioByLoginName(loginName);
    }

    @GET
    @Path("/funcionarios/rol/{rol}")
    public List<FuncionarioDTO> listarFuncionarioByRol(@PathParam("rol") final String rol) throws SystemException {
        log.info("processing rest request - listar funcionarios por login_name");
        return boundary.listarFuncionarioByRol(rol);
    }


    @GET
    @Path("/funcionarios/listar/{nro_identificacion}")
    public List<FuncionarioDTO> consultarFuncionarioByNroIdentificacion(@PathParam("nro_identificacion") final String nroIdentificacion) throws SystemException, BusinessException {
        log.info("processing rest request - listar funcionarios por login_name");
        return boundary.consultarFuncionarioByNroIdentificacion(nroIdentificacion);
    }


    /**
     * @param funcionarioDTO
     * @throws SystemException
     */
    @POST
    @Path("/funcionarios")
    public void crearFuncionario(FuncionarioDTO funcionarioDTO) throws SystemException {
        log.info("processing rest request - crear funcionario");
        boundary.crearFuncionario(funcionarioDTO);
    }

    /**
     * @param funcionarioDTO
     * @return
     * @throws SystemException
     */
    @POST
    @Path("/funcionarios/buscar")
    public FuncionariosDTO buscarFuncionario(FuncionarioDTO funcionarioDTO) throws SystemException {
        log.info("processing rest request - buscar funcionarios" + funcionarioDTO);
        return boundary.buscarFuncionario(funcionarioDTO);
    }

    /**
     * @param funcionarioDTO
     * @throws SystemException
     */
    @PUT
    @Path("/funcionarios/actualizar")
    public String actualizarFuncionario(FuncionarioDTO funcionarioDTO) throws SystemException {
        log.info("processing rest request - actualizar funcionario: " + funcionarioDTO.getIdeFunci());
        return boundary.actualizarFuncionario(funcionarioDTO);
    }

    @PUT
    @Path("/funcionarios/actualizar/roles")
    public  Boolean actualizarRolesFuncionario(FuncionarioDTO funcionario) throws SystemException {
        log.info("processing rest request - actualizar roles funcionario: {}",funcionario);
        return boundary.actualizarRolesFuncionario(funcionario);
    }


    /**
     * @param ideFunci
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/funcionarios/by-login/{Ide_Funci}")
    public String consultarLoginNameByIdeFunci(@PathParam("Ide_Funci") final BigInteger ideFunci) throws BusinessException, SystemException {
        log.info("processing rest request - obtener login_name por id de funcionario: " + ideFunci.toString());
        return boundary.consultarLoginNameByIdeFunci(ideFunci);
    }

    /**
     * @param ideFunci
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/funcionarios/by-id/{Ide_Funci}")
    public FuncionarioDTO consultarFuncionarioByIdeFunci(@PathParam("Ide_Funci") final BigInteger ideFunci) throws BusinessException, SystemException {
        log.info("processing rest request - obtener Funcionario por id de funcionario");
        return boundary.consultarFuncionarioByIdeFunci(ideFunci);
    }

    @GET
    @Path("/funcionarios/firmaPoliticaFuncionarioByLoginName/{login}")
    public String firmaPoliticaFuncionarioByLoginName(@PathParam("login") final String login) throws  BusinessException, SystemException {
        log.info("processing rest request - firm Politica Funcionario By LoginName");
        return boundary.firmaPoliticaFuncionarioByLoginName(login);
    }

}
