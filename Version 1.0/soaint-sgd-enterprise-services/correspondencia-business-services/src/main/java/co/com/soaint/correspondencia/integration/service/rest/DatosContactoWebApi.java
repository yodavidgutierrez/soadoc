package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarDatosContacto;
import co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO;
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
@Path("/datos-contacto-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
@Api(value = "DatosContactoWebApi")
public class DatosContactoWebApi {

    @Autowired
    GestionarDatosContacto boundary;


    /**
     * Constructor
     */
    public DatosContactoWebApi() {

        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


    /**
     * @param idAgente
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/datoscontacto/{id-agente}")
    public List<DatosContactoDTO> listarRadicados(@PathParam("id-agente") final String idAgente) throws SystemException {
        log.info("processing rest request - Datos contactos por nro idAgente");
        return boundary.consultarDatosContactoByIdAgente(idAgente);
    }

    @GET
    @Path("/datoscontacto/principal/{id-agente}")
    public DatosContactoDTO consultarDatosContactoPrincipalByIdAgente(@PathParam("id-agente") final String idAgente) throws SystemException {
        log.info("processing rest request - Datos contactos por nro idAgente");
        return boundary.consultarDatosContactoPrincipalByIdAgente(idAgente);
    }

    /**
     * @param nroIdentidad
     * @param tipoAgente
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/datoscontacto-nro/{nro-identidad}/{tipo-agente}/{tipo-doc}")
    public List<DatosContactoDTO> consultarDatosContactoByNroIdentidad(@PathParam("nro-identidad") final String nroIdentidad,
                                                                       @PathParam("tipo-agente") final String tipoAgente,
                                                                       @PathParam("tipo-doc") final String tipoDoc) throws BusinessException {
        log.info("processing rest request - Datos contactos por nro Identidad");
        return boundary.consultarDatosContactoByNroIdentidad(nroIdentidad, tipoAgente, tipoDoc);
    }

}
