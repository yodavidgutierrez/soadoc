package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarOrganigramaAdministrativo;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaAdministrativoDTO;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 24-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Path("/organigrama-web-api")
@Produces({"application/json", "application/xml"})
@Log4j2
@Api(value = "OrganigramaAdministrativoWebApi")
public class OrganigramaAdministrativoWebApi {

    @Autowired
    private GestionarOrganigramaAdministrativo boundary;

    /**
     * Constructor
     */
    public OrganigramaAdministrativoWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/organigrama")
    public OrganigramaAdministrativoDTO consultarOrganigrama() throws BusinessException, SystemException {
        log.info("processing rest request - consultar organigrama administrativo");
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.consultarOrganigrama()).build();
    }

    /**
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/organigrama/sedes")
    public OrganigramaAdministrativoDTO listarDescendientesPadres() throws SystemException {
        log.info("processing rest request - listar descendientes directos del elemento raiz");
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.listarDescendientesPadres()).build();
    }

    /**
     * @param idPadre
     * @return
     * @throws SystemException
     */
    @GET
        @Path("/organigrama/dependencias/{ide_orga_admin_padre}")
    public OrganigramaAdministrativoDTO listarElementosDeNivelInferior(@PathParam("ide_orga_admin_padre") final String idPadre) throws SystemException {
        log.info("processing rest request - listar descendientes directos de un elemento");
        //return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.listarElementosDeNivelInferior(new BigInteger(idPadre))).build();
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.listarElementosDeNivelInferior(new ArrayList<>(), idPadre)).build();
    }

    @GET
    @Path("/organigrama/dependencias-check/{ide_orga_admin_padre}")
    public OrganigramaAdministrativoDTO listarElementosCheck(@PathParam("ide_orga_admin_padre") final String idPadre) throws SystemException {
        log.info("processing rest request - listar descendientes directos de un elemento");
        //return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.listarElementosDeNivelInferior(new BigInteger(idPadre))).build();
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.listarElementosCheck(new ArrayList<>(), idPadre)).build();
    }

    /**
     * @param idDependencia
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/organigrama/sede/dependencia/{ide_orga_admin}")
    public OrganigramaItemDTO consultarPadreDeSegundoNivel(@PathParam("ide_orga_admin") final String idDependencia) throws BusinessException, SystemException {
        log.info("processing rest request - listar padre de segundo nivel");
        return boundary.consultarPadreDeSegundoNivel(new BigInteger(idDependencia));
    }

    @POST
    @Path("/organigrama/generarEstructura")
    public boolean generarEstructuraDatabase(@RequestBody List<EstructuraTrdDTO> structure) {
        log.info("processing rest request - Generar estructura de organigrama Base de Datos");
        try {
            return boundary.generarEstructuraDatabase(structure);
        } catch (Exception e) {
            log.error("Error servicio creando estructura --> {}", e.getMessage());
            return false;
        }
    }
}
