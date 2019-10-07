package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarAgente;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 11-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/agente-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
@Api(value = "AgenteWebApi")
public class AgenteWebApi {

    @Autowired
    GestionarAgente boundary;

    /**
     * Constructor
     */
    public AgenteWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param agenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/agente/actualizar-estado")
    public void actualizarEstadoAgente(AgenteDTO agenteDTO) throws BusinessException, SystemException {
        log.info("processing rest request - actualizar estado agente");
        boundary.actualizarEstadoAgente(agenteDTO);
    }

    /**
     * @param redireccion
     * @throws SystemException
     */
    @PUT
    @Path("/agente/redireccionar")
    public void redireccionarCorrespondencia(RedireccionDTO redireccion) throws SystemException {
        log.info("processing rest request - redireccionar correspondencia");
        boundary.redireccionarCorrespondencia(redireccion);
    }

    /**
     * @param devolucion
     * @throws SystemException
     */
    @PUT
    @Path("/agente/devolver")
    public void devolverCorrespondencia(DevolucionDTO devolucion) throws SystemException {
        log.info("processing rest request - devolver correspondencia");
        log.info("devolucion DTO" + devolucion);
        boundary.devolverCorrespondencia(devolucion);
    }

    /**
     * @param remitenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/agente/actualizar-remitente")
    public String actualizarRemitente(RemitenteDTO remitenteDTO) throws SystemException {
        log.info("processing rest request - actualizar remitente");
        return boundary.actualizarRemitente(remitenteDTO);
    }

    /**
     * @param destinatarioDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/agente/actualizar-destinatario")
    public String actualizarDestinatario(DestinatarioDTO destinatarioDTO) throws SystemException {
        log.info("processing rest request - actualizar destinatario");
        return boundary.actualizarDestinatario(destinatarioDTO);
    }

    /**
     * @param nroRadicado
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/agente/remitente/{nro_radicado}")
    public AgenteDTO consultarRemitenteByNroRadicado(@PathParam("nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        log.info("processing rest request - consultar remitente por numero de radicado");
        return boundary.consultarRemitenteByNroRadicado(nroRadicado);
    }

    @GET
    @Path("/agente/{nro_identificacion}/{tipo_persona}")
    public AgenteDTO consultarAgenteByNroOrNit(@PathParam("nro_identificacion") final String nroIdentificacion,
                                               @PathParam("tipo_persona") final String tipoPersona) throws SystemException {
        log.info("processing rest request - consultar remitente por numero de radicado");
        return boundary.consultarAgenteByNroOrNit(nroIdentificacion, tipoPersona);
    }

}
