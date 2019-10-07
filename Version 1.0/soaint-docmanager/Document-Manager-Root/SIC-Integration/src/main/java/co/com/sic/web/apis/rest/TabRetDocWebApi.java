package co.com.sic.web.apis.rest;

import co.com.foundation.soaint.documentmanager.integration.FiltroSerSubINT;
import co.com.foundation.soaint.documentmanager.integration.ResponseDTO;
import co.com.foundation.soaint.documentmanager.integration.TabRetDocINT;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.sic.business.tabretdoc.TabRecDocBoundary;
import lombok.extern.log4j.Log4j2;
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
 * Created by erodriguez.
 */
@Log4j2
@RestController
@RequestMapping(value = "/tabretdoc-api", produces = MediaType.APPLICATION_JSON_VALUE)
public class TabRetDocWebApi {
    private static final Logger LOGGER = LogManager.getLogger(TabRetDocWebApi.class);

    @Autowired
    private TabRecDocBoundary tabRecDocBoundary;

    @RequestMapping(value = "/obtener_por_id", method = RequestMethod.GET, params = {"idTab"})
    public TabRetDocINT getById(@RequestParam("idTab") BigInteger idTab) throws SystemException {
        LOGGER.info("TabRetDocWebApi. Consultar tabla de retencion por id: {}", idTab);
        log.info("Prueba log 1");
        return tabRecDocBoundary.findById(idTab);
    }

    @RequestMapping(value = "/obtener_por_serie_subserie", method = RequestMethod.GET, params = {"idSerie", "idSubserie"})
    public List<TabRetDocINT> getByIdSerieSubserie(@RequestParam("idSerie") BigInteger idSerie,
                                                   @RequestParam("idSubserie") BigInteger idSubserie) throws SystemException {
        LOGGER.info("TabRetDocWebApi. Consultar tabla de retencion por serie y subserie: {}", idSerie, idSubserie);
        log.info("Prueba log 2");
        return tabRecDocBoundary.getByIdSerieSubserie(idSerie, idSubserie);
    }

    @RequestMapping(value = "/obtener_por_serie_subserie_org", method = RequestMethod.GET, params = {"codOrg", "codSerie", "codSubserie", "ideOrg"})
    public TabRetDocINT getByCodOrgSerieSubserie(@RequestParam("codOrg") String codOrg,
                                                 @RequestParam("codSerie") String codSerie,
                                                 @RequestParam("codSubserie") String codSubserie,
                                                 @RequestParam("ideOrg") Long idOrg) throws SystemException {
        LOGGER.info("TabRetDocWebApi. Consultar tabla de retencion por codigo de organigrama, serie y subserie: {}", codOrg, codSerie, codSubserie, idOrg);
        log.info("Prueba log 3");
        return tabRecDocBoundary.getByCodOrgSerieSubserie(codOrg, codSerie, codSubserie, idOrg);
    }

    @RequestMapping(value = "/obtener_version_trd_por_cod_org", method = RequestMethod.GET, params = {"codOrg"})
    public ResponseDTO getVersionTrdPorCodOrg(@RequestParam("codOrg") String codOrg) throws SystemException {
        LOGGER.info("TabRetDocWebApi. Obtener version TRD por codigo de organigrama: {}", codOrg);
        log.info("Prueba log 3");
        return tabRecDocBoundary.getVersionTrdPorCodOrg(codOrg);
    }

    @RequestMapping(value = "/obtener_id_disp_final", method = RequestMethod.GET, params = {"codDF"})
    public ResponseDTO getIdDisposicionFinal(@RequestParam("codDF") String codDF) throws SystemException {
        LOGGER.info("TabRetDocWebApi. Obtener id de disposicion final dado el codigo: {}", codDF);
        log.info("Prueba log 4");
        return tabRecDocBoundary.getIdDisposicionFinal(codDF);
    }

    @RequestMapping(value = "/obtener_dispo_final", method = RequestMethod.GET, params = {"idDispoFinal"})
    public FiltroSerSubINT getDisposicionFinal(@RequestParam("idDispoFinal") BigInteger idDispoFinal) throws SystemException {
        LOGGER.info("TabRetDocWebApi. Obtener disposicion final dado el id: {}", idDispoFinal);
        return tabRecDocBoundary.getDisposicionFinal(idDispoFinal);
    }
}
