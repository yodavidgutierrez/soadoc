package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarConstantes;
import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.foundation.canonical.correspondencia.ConstantesDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by esanchez on 5/24/2017.
 */
@Path("/constantes-web-api")
@Produces({"application/json", "application/xml"})
@Log4j2
@Api(value = "ConstantesWebApi")
public class ConstantesWebApi {

    @Autowired
    private GestionarConstantes boundary;

    /**
     * Constructor
     */
    public ConstantesWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param estado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("constantes/{estado}")
    public ConstantesDTO listarConstantesByEstado(@PathParam("estado") final String estado) throws SystemException {
        log.info("processing rest request - listar constantes por estado");
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByEstado(estado)).build();
    }

    /**
     * @param codigo
     * @param estado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("constantes/{codigo}/{estado}")
    public ConstantesDTO listarConstantesByCodigoAndEstado(@PathParam("codigo") final String codigo, @PathParam("estado") final String estado) throws SystemException {
        log.info("processing rest request - listar constantes por codigo y estado");
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByCodigoAndEstado(codigo, estado)).build();
    }

    /**
     * @param codPadre
     * @param estado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("constantes/hijos/{cod-padre}/{estado}")
    public ConstantesDTO listarConstantesByCodPadreAndEstado(@PathParam("cod-padre") final String codPadre, @PathParam("estado") final String estado) throws SystemException {
        log.info("processing rest request - listar constantes por codigo del padre y estado");
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByCodPadreAndEstado(codPadre, estado)).build();
    }

    /**
     * @param codigos
     * @return ConstantesDTO
     * @throws SystemException
     */
    @GET
    @Path("constantes")
    public ConstantesDTO listarConstantesByCodigo(@QueryParam("codigos") String codigos) throws SystemException {
        log.info("processing rest request - listar constantes por codigo");
        return boundary.listarConstantesByCodigo(codigos.split(","));
    }

    /**
     * @param codigos
     * @param nombre
     * @return List<ConstanteDTO>
     * @throws SystemException
     */
    @GET
    @Path("constantes/buscar/hijos")
    public List<ConstanteDTO> listarConstantesByCodigoPadre(@QueryParam("codigos") String codigos, @QueryParam("nombre") String nombre) throws SystemException {
        log.info("processing rest request - listar constantes por codigo padre: " + codigos);
        return boundary.listarConstantesByCodigoPadre(codigos, nombre);
    }

    /**
     * @param constante
     * @return boolean
     * @throws SystemException
     */
    @POST
    @Path("constantes/adicionar")
    public Boolean adicionarConstante(ConstanteDTO constante) {
        log.info("processing rest request - adicionar constante");
        return boundary.adicionarConstante(constante);
    }

    /**
     * @param constante
     * @return boolean
     * @throws SystemException
     */
    @PUT
    @Path("constantes/actualizar")
    public Boolean actualizarConstante(ConstanteDTO constante) {
        log.info("processing rest request - actualizar constante");
        return boundary.actualizarConstante(constante);
    }

    /**
     * @param idConstante
     * @return boolean
     * @throws SystemException
     */

    @DELETE
    @Path("constantes/eliminar/{id_constante}")
    public Boolean eliminarConstante(@PathParam("id_constante") final BigInteger idConstante) {
        log.info("processing rest request - eliminar constantes por id");
        return boundary.eliminarConstante(idConstante);
    }
}
