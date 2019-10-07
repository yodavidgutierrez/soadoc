package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarKpiDatacenter;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Map;

@Log4j2
@Path("/kpi-web-api")
@Api(value = "KpiWebApi")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
public class KpiWebApi {

    @Autowired
    private GestionarKpiDatacenter kpiDatacenter;

    /**
     * Constructor
     */
    public KpiWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/processKpi11")
    public Response processKpi11(Map<String, String> processMap) throws SystemException {
        return kpiDatacenter.processKpi11(processMap);
    }
}
