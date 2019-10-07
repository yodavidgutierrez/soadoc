package co.com.sic.web.apis.rest;

import co.com.foundation.soaint.documentmanager.domain.SerieINT;
import co.com.foundation.soaint.documentmanager.domain.SubserieINT;
import co.com.foundation.soaint.documentmanager.integration.FiltroSerSubINT;
import co.com.foundation.soaint.documentmanager.integration.ResponseSubserieINT;
import co.com.foundation.soaint.documentmanager.integration.TipologiaDocINT;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.sic.business.serie.SerieBoundary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by erodriguez on 09/11/2018.
 */

@RestController
@RequestMapping(value = "/serie-api", produces = MediaType.APPLICATION_JSON_VALUE)
public class SerieWebApi {
    private static final Logger LOGGER = LogManager.getLogger(SerieWebApi.class);

    @Autowired
    private SerieBoundary serieBoundary;

    @RequestMapping(value = "/obtener_serie_por_id", method = RequestMethod.GET, params = {"idSerie"})
    public SerieINT findSerieById(@RequestParam("idSerie") BigInteger idSerie) throws SystemException {
        LOGGER.info("SerieWebApi. Consultar serie por id: {}", idSerie);
        return serieBoundary.findSerieById(idSerie);
    }

    @RequestMapping(value = "/listar_serie", method = RequestMethod.GET)
    public List<SerieINT> listSerie() throws SystemException {
        LOGGER.info("SerieWebApi. Listar todas las series.");
        return serieBoundary.listSerie();
    }

    @RequestMapping(value = "/obtener_subserie_por_id", method = RequestMethod.GET, params = {"idSubserie"})
    public SubserieINT findsubserieById(@RequestParam("idSubserie") BigInteger idSubserie) throws SystemException {
        LOGGER.info("SerieWebApi. Consultar Subserie por id: {}", idSubserie);
        return serieBoundary.findsubserieById(idSubserie);
    }

    @RequestMapping(value = "/listarSubserieByCodOrg", method = RequestMethod.GET, params = {"codOrg"})
    public List<ResponseSubserieINT> listarSubserieByCodOrg(@RequestParam("codOrg") String codOrg) throws SystemException {
        LOGGER.info("SerieWebApi. Consultar Subserie por cod Organigrama: {}", codOrg);
        return serieBoundary.listarSubserieByCodOrg(codOrg);
    }

    @RequestMapping(value = "/listar_subserie_por_serie", method = RequestMethod.GET, params = {"idSerie"})
    public List<SubserieINT> listSubseriePorSerie(@RequestParam("idSerie") BigInteger idSerie) throws SystemException {
        LOGGER.info("SerieWebApi. Listar todas las Subseries por la serie: {}", idSerie);
        return serieBoundary.listSubseriePorSerie(idSerie);
    }

    @RequestMapping(value = "/listar_subserie_por_serie_trd", method = RequestMethod.GET, params = {"idSerie", "codOrg"})
    public List<SubserieINT> listSubseriePorSerieTrd(@RequestParam("idSerie") BigInteger idSerie,
                                                     @RequestParam("codOrg") String codOrg) throws SystemException {
        LOGGER.info("SerieWebApi. Listar todas las Subseries por la serie: {}", idSerie, codOrg);
        return serieBoundary.listSubseriePorSerieTrd(idSerie, codOrg);
    }

    @RequestMapping(value = "/listar_serie_por_organigrama", method = RequestMethod.GET, params = {"ideOrg"})
    public List<SerieINT> listSeriePorOrganigrama(@RequestParam("ideOrg") Long ideOrg) throws SystemException {
        LOGGER.info("SerieWebApi. Listar todas las Series por organigrama: {}", ideOrg);
        return serieBoundary.listSeriePorOrganigrama(ideOrg);
    }

    @RequestMapping(value = "/listar_serie_por_cod_organigrama", method = RequestMethod.GET, params = {"codOrg"})
    public List<SerieINT> listSeriePorOrganigramaByCod(@RequestParam("codOrg") String codOrg) throws SystemException {
        LOGGER.info("SerieWebApi. Listar todas las Series por organigrama por cod: {}", codOrg);
        return serieBoundary.listSeriePorOrganigramaByCod(codOrg);
    }

    @RequestMapping(value = "/listar_serie_por_cod_organigrama_trd", method = RequestMethod.GET, params = {"codOrg"})
    public List<SerieINT> listSeriePorOrganigramaByCodNew(@RequestParam("codOrg") String codOrg) throws SystemException {
        LOGGER.info("SerieWebApi. Listar todas las Series por organigrama por cod: {}", codOrg);
        return serieBoundary.listSeriePorOrganigramaByCodTrd(codOrg);
    }

    @RequestMapping(value = "/filtrar_serie_subserie", method = RequestMethod.GET, params = {"idOrg", "idSerie", "idSubserie", "idTrd"})
    public List<FiltroSerSubINT> filtrarSerieSubserie(@RequestParam("idOrg") Long idOrg,
                                                      @RequestParam("idSerie") BigInteger idSerie,
                                                      @RequestParam("idSubserie") BigInteger idSubserie,
                                                      @RequestParam("idTrd") BigInteger idTrd) throws SystemException {
        LOGGER.info("SerieWebApi. Filtrar serie y subserie por id, organigrama y trd: {}", idOrg, idSerie, idSubserie, idTrd);
        return serieBoundary.filtrarSerieSubserie(idOrg, idSerie, idSubserie, idTrd);
    }

    @RequestMapping(value = "/filtrar_serie_subserie_cod", method = RequestMethod.GET, params = {"codOrg", "idSerie", "idSubserie", "idTrd"})
    public List<FiltroSerSubINT> filtrarSerieSubserie(@RequestParam("codOrg") String codOrg,
                                                      @RequestParam("idSerie") BigInteger idSerie,
                                                      @RequestParam("idSubserie") BigInteger idSubserie,
                                                      @RequestParam("idTrd") BigInteger idTrd) throws SystemException {
        LOGGER.info("SerieWebApi. Filtrar serie y subserie por id, organigrama y trd: {}", codOrg, idSerie, idSubserie, idTrd);
        return serieBoundary.filtrarSerieSubserieByCod(codOrg, idSerie, idSubserie, idTrd);
    }

    @RequestMapping(value = "/listarTipologia", method = RequestMethod.GET, params = {"codSerie", "codSubserie"})
    public List<TipologiaDocINT> listarTipologia (@RequestParam(value = "codSerie") String codSerie,
                                                  @RequestParam(value = "codSubserie", defaultValue = "") String codSubserie){
        LOGGER.info("SerieWebApi. Listar tipología por serie y subserie: {}", codSerie, codSubserie);
        return serieBoundary.listarTipologia(codSerie, codSubserie);
    }
}
