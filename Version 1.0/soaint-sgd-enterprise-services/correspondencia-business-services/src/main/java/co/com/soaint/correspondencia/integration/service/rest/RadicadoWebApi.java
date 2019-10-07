package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarRadicado;
import co.com.soaint.foundation.canonical.correspondencia.RadicadoDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 06-Sep-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/radicado-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
@Api(value = "RadicadoWebApi")
public class RadicadoWebApi {

    @Autowired
    GestionarRadicado boundary;

    /**
     * Constructor
     */
    public RadicadoWebApi() {

        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param nroRadicado
     * @param nroIdentidad
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/radicado/")
    public List<RadicadoDTO> listarRadicados(@QueryParam("nro-radicado") final String nroRadicado,
                                             @QueryParam("nro-identidad") final String nroIdentidad,
                                             @QueryParam("noGuia") final String noGuia,
                                             @QueryParam("nombre") final String nombre,
                                             @QueryParam("anno") final String anno,
                                             @QueryParam("tipoDocumento") final String tipoDocumento) throws SystemException {

        log.info("processing rest request - listar estado de una tarea");
        return boundary.listarRadicados(nroRadicado, nroIdentidad, noGuia, nombre, anno, tipoDocumento);
    }

    @GET
    @Path("/obtener-fecha-Radicacion/{nro-rad}")
    public RadicadoDTO getFechaRadicacion(@PathParam("nro-rad") final String nroRadicado) {
        log.info("processing rest request - listar estado de una tarea");
        try {
            return boundary.getFechaRadicacion(nroRadicado);
        } catch (Exception ex) {
            log.error("Error: " + ex.getMessage());
            return RadicadoDTO.newInstance().build();
        }
    }

    @GET
    @Path("/existe-radicado/{nro-rad}")
    public boolean existeRadicado(@PathParam("nro-rad") final String nroRadicado) throws SystemException {
        log.info("processing rest request - existe radicado");

            return boundary.existeRadicado(nroRadicado);
    }
}
