package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarKpiReporte;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
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
import java.util.List;

@Log4j2
@Path("/reporte-web-api")
@Api(value = "KpiWebApi")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
public class ReporteWebApi {

    @Autowired
    private GestionarKpiReporte kpiReporte;

    /**
     * Constructor
     */
    public ReporteWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/processRep7")
    public Response processRep7(List<DocumentoDTO> dtoList) throws SystemException {
        return kpiReporte.processRep7(dtoList);
    }

    @POST
    @Path("/processRep8")
    public Response processRep8(List<UnidadDocumentalDTO> dtoList) throws SystemException {
        return kpiReporte.processRep8(dtoList);
    }

    @POST
    @Path("/processRep9")
    public Response processRep9(List<UnidadDocumentalDTO> dtoList) throws SystemException {
        return kpiReporte.processRep9(dtoList);
    }
}
