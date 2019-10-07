package co.com.sic.web.apis.rest;

import co.com.foundation.soaint.documentmanager.integration.DependenciaINT;
import co.com.foundation.soaint.documentmanager.integration.OrganigramaINT;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.sic.business.organigrama.OrganigramaBoundary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by erodriguez on 24/10/2018.
 */

@RestController
@RequestMapping(value = "/organigrama-api", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganigramaWebApi {
    private static final Logger LOGGER = LogManager.getLogger(OrganigramaWebApi.class);

    @Autowired
    private OrganigramaBoundary organigramaBoundary;

    @RequestMapping(value = "/obtener_por_id", method = RequestMethod.GET, params = {"idOrg"})
    public OrganigramaINT getOrganigramabyId(@RequestParam("idOrg") Long idOrg) throws SystemException {
        LOGGER.info("OrganigramaWebApi. Consultando organigrama por id: {}", idOrg);
        return organigramaBoundary.findById(idOrg);
    }

    @RequestMapping(value = "/obtener_por_codigo", method = RequestMethod.GET, params = {"codOrg"})
    public OrganigramaINT getOrganigramabyCod(@RequestParam("codOrg") String codOrg) throws SystemException {
        LOGGER.info("OrganigramaWebApi. Consultando organigrama por id: {}", codOrg);
        return organigramaBoundary.findByCodigo(codOrg);
    }

    @RequestMapping(value = "/listar_dependencias", method = RequestMethod.GET)
    public List<DependenciaINT> listDependencias() throws SystemException {
        LOGGER.info("OrganigramaWebApi. Listar todas las dependencias.");
        return organigramaBoundary.listDependencias();
    }

    @RequestMapping(value = "/listar_por_id_padre", method = RequestMethod.GET, params = {"idOrg"})
    public List<OrganigramaINT> getOrganigramasbyIdPadre(@RequestParam("idOrg") Long idOrg) throws SystemException {
        LOGGER.info("OrganigramaWebApi. Listando organigramas por id padre: {}", idOrg);
        return organigramaBoundary.getOrganigramasbyIdPadre(idOrg);
    }

    @RequestMapping(value = "/listar_por_cod_padre", method = RequestMethod.GET, params = {"codOrg"})
    public List<OrganigramaINT> getOrganigramasbyCod(@RequestParam("codOrg") String codOrg) throws SystemException {
        LOGGER.info("OrganigramaWebApi. Listando organigramas por cod padre: {}", codOrg);
        return organigramaBoundary.getOrganigramasbyCodPadre(codOrg);
    }

    @RequestMapping(value = "/obtener_arbol_organigrama", method = RequestMethod.GET)
    public OrganigramaINT obtenerArbolOrganigrama() throws SystemException {
        LOGGER.info("OrganigramaWebApi. Obtener arbol de organigrama.");
        return organigramaBoundary.obtenerArbolOrganigrama();
    }

    @RequestMapping(value = "/listar_org_jer_por_padre", method = RequestMethod.GET, params = {"idOrg", "idOrgExc"})
    public List<OrganigramaINT> listOrganigramaJerarquicoPorPadre(@RequestParam("idOrg") Long idOrg,
                                                                  @RequestParam("idOrgExc") Long idOrgExc) throws SystemException {
        LOGGER.info("OrganigramaWebApi. Listar organigramas de forma jerarquica dado padre: {}", idOrg, idOrgExc);
        return organigramaBoundary.listOrganigramaJerarquicoPorPadre(idOrg, idOrgExc);
    }

    @RequestMapping(value = "/listar_org_jer_por_cod", method = RequestMethod.GET, params = {"codOrg", "codOrgExc"})
    public List<OrganigramaINT> listOrganigramaJerarquicoPorCod(@RequestParam("codOrg") String codOrg,
                                                                  @RequestParam("codOrgExc") String codOrgExc) throws SystemException {
        LOGGER.info("OrganigramaWebApi. Listar organigramas de forma jerarquica dado padre: {}", codOrg, codOrgExc);
        return organigramaBoundary.listOrganigramaJerarquicoPorCod(codOrg, codOrgExc);
    }

    @RequestMapping(value = "/obtener_padre", method = RequestMethod.GET)
    public OrganigramaINT obtenerPadre() throws SystemException {
        LOGGER.info("OrganigramaWebApi. Obtener organigrama principal.");
        return organigramaBoundary.obtenerPadre();
    }

    @RequestMapping(value = "/listarCodHijos", method = RequestMethod.GET)
    public List<OrganigramaINT> listarCodHijos() throws SystemException {
        LOGGER.info("OrganigramaWebApi. Obtener organigrama principal.");
        return organigramaBoundary.listarCodHijos();
    }

    @RequestMapping(value = "/consultarPadrePorCodigoHijo", method = RequestMethod.GET, params = {"codOrg"})
    public OrganigramaINT obtenerPadrePorCodHijo(@RequestParam("codOrg") String codOrg) throws SystemException {
        LOGGER.info("OrganigramaWebApi. Consultar padre por codigo de organigrama.");
        return organigramaBoundary.obtenerPadrePorCodHijo(codOrg);
    }
}
