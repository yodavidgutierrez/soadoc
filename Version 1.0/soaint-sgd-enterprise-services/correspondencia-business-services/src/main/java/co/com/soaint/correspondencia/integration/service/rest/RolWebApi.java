package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarRol;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

@Path("/rol-web-api")
@Produces({"application/json", "application/xml"})
@Log4j2
@Api(value = "RolWebApi")
public class RolWebApi {

    @Autowired
    private GestionarRol boundary;

    /**
     * Constructor
     */
    public RolWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


    @GET
    @Path("/rol/listar/id/{id-funcionario}")
    public List<RolDTO> listarRolesByIdFuncionario(@PathParam("id-funcionario") final BigInteger idFunci) throws SystemException {
        log.info("processing rest request - listar roles por id-funcionario {}", idFunci);
        return boundary.listarRolesByIdFuncionario( idFunci);
    }

    @GET
    @Path("/rol/listar/login/{login-name}")
    public List<RolDTO> listarRolesByLoginName(@PathParam("login-name") final String loginName) throws SystemException {
        log.info("processing rest request - listar roles por id-funcionario {}", loginName);
        return boundary.listarRolesByLoginName( loginName);
    }


    @GET
    @Path("/rol/listar")
    public List<RolDTO> listarRoles() throws SystemException {
        return boundary.listarRoles();
    }

}
