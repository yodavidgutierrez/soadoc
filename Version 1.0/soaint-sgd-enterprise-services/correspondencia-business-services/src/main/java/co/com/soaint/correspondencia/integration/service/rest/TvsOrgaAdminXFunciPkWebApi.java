package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarTvsOrgaAdminXFunciPk;
import co.com.soaint.foundation.canonical.correspondencia.TvsOrgaAdminXFunciPkDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 24-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Path("/organigrama-admin-web-api")
@Produces({"application/json", "application/xml"})
@Log4j2
@Api(value = "TvsOrgaAdminXFunciPkWebApi")
public class TvsOrgaAdminXFunciPkWebApi {

    @Autowired
    private GestionarTvsOrgaAdminXFunciPk boundary;


    /**
     * Constructor
     */
    public TvsOrgaAdminXFunciPkWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/organigrama")
    public boolean insertarFuncionarioConCodigoDependencia(TvsOrgaAdminXFunciPkDTO tvs) throws BusinessException, SystemException {
        log.info("processing rest request - consultar organigrama administrativo");
        return boundary.insertarFuncionarioConCodigoDependencia(tvs);
    }


}
