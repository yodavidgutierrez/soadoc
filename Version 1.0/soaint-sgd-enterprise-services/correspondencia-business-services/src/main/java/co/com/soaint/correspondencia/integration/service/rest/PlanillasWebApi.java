package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarPlanillas;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 05-Sep-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/planillas-web-api")
/*@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})*/
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
@Api(value = "PlanillasWebApi")
public class PlanillasWebApi {

    @Autowired
    GestionarPlanillas boundary;

    /**
     * Constructor
     */
    public PlanillasWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param planilla
     * @return
     * @throws SystemException
     */
    @POST
    @Path("/planillas")
    public PlanillaDTO generarPlanilla(PlanillaDTO planilla) throws SystemException {
        log.info("processing rest request - generar planilla distribucion");
            return boundary.generarPlanilla(planilla);
    }


    @POST
    @Path("/planillas-prueba")
    public PlanillaDTO generarPlanillaPrueba(String planilla) throws SystemException {
        log.info("processing rest request - generar planilla distribucion");
        log.info("****DTO**** que llega al servicio de generarPlanillaPrueba ::  "+ planilla);
        //return boundary.generarPlanilla(planilla);
        return null;
    }

    /**
     * @param planilla
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/planillas")
    public void cargarPlanilla(PlanillaDTO planilla) throws SystemException {
        log.info("processing rest request - cargar planilla distribucion");
        boundary.cargarPlanilla(planilla);
    }

    /**
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/planillas-salida")
    public PlanillasDTO listarPlanillasSalida() {
        log.info("processing rest request - listar planilla distribucion by nroPlanilla");
        List<PlanillaDTO> lista = new ArrayList<PlanillaDTO>();
        lista.add(PlanillaDTO.newInstance().nroPlanilla("creado").build());
        return PlanillasDTO.newInstance().planillaDTOList(lista).build();
    }

    /**
     * @param nroPlanilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/planillas/{nro_planilla}")
    public PlanillaDTO listarPlanillasByNroPlanilla(@PathParam("nro_planilla") final String nroPlanilla) throws BusinessException, SystemException {
        log.info("processing rest request - listar planilla distribucion by nroPlanilla");
        return boundary.listarPlanillasByNroPlanilla(nroPlanilla);
    }

    /**
     * @param nroPlanilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/planillas-salida/{nro_planilla}")
    public PlanillaSalidaDTO listarPlanillasSalidaByNroPlanilla(@PathParam("nro_planilla") final String nroPlanilla) throws BusinessException, SystemException {
        log.info("processing rest request - listar planilla distribucion by nroPlanilla");
        return boundary.listarPlanillasSalidaByNroPlanilla(nroPlanilla);
    }

    /**
     * @param nroPlanilla
     * @param formato
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/planillas/{nro_planilla}/{formato}")
    public ReportDTO exportarPlanilla(@PathParam("nro_planilla") final String nroPlanilla,
                                      @PathParam("formato") final String formato) throws SystemException {
        log.info("processing rest request - exportar planilla distribucion by nroPlanilla and formato");
        return boundary.exportarPlanilla(nroPlanilla, formato);
    }

    @GET
    @Path("/planillas/salida/{nro_planilla}/{formato}/{tipo_comunicacion}")
    public ReportDTO exportarPlanillaSalida(@PathParam("nro_planilla") final String nroPlanilla,
                                            @PathParam("formato") final String formato,
                                            @PathParam("tipo_comunicacion") final String tipoComunicacion) throws SystemException {
        log.info("processing rest request - exportar planilla distribucion by nroPlanilla and formato");
        return boundary.exportarPlanillaSalida(nroPlanilla, formato, tipoComunicacion);
    }
}
