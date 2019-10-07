package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarObservacion;
import co.com.soaint.foundation.canonical.correspondencia.ObservacionDTO;
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
@Path("/observacion-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
@Api(value = "ObservacionWebApi")
public class ObservacionWebApi {

    @Autowired
    GestionarObservacion boundary;

    /**
     * Constructor
     */
    public ObservacionWebApi() {

        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


    @GET
    @Path("/observacion/{id-instancia}")
    public List<ObservacionDTO> listarObservacionesByIdInstancia(@PathParam("id-instancia") final String idInstancia) throws SystemException {
        log.info("processing rest request - listar observaciones");
        return boundary.listarObservacionesByIdInstancia(idInstancia);
    }

    @PUT
    @Path("/observacion/actualizar/")
    public boolean insertarObservaciones(List<ObservacionDTO> observaciones) throws SystemException {
        log.info("processing rest request - actualizar funcionario: " + observaciones.size());
        return boundary.insertarObservaciones(observaciones);
    }

}
