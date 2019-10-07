package co.com.sic.business.serie;

import co.com.foundation.soaint.documentmanager.domain.SerieINT;
import co.com.foundation.soaint.documentmanager.domain.SubserieINT;
import co.com.foundation.soaint.documentmanager.integration.FiltroSerSubINT;
import co.com.foundation.soaint.documentmanager.integration.ResponseSubserieINT;
import co.com.foundation.soaint.documentmanager.integration.TipologiaDocINT;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSerie;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSubserie;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
public class SerieBoundary {
    private static final Logger LOGGER = LogManager.getLogger(SerieBoundary.class);

    @Autowired
    private SerieControl serieControl;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public SerieINT findSerieById(BigInteger idSerie) throws SystemException {
        LOGGER.info("SerieBoundary. Consultar serie por id: {}", idSerie);
        AdmSerie admSerie = serieControl.findSerieById(idSerie);
        return serieControl.transformSerie(admSerie);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<SerieINT> listSerie() throws SystemException {
        LOGGER.info("SerieBoundary. Listar todas las series.");
        List<AdmSerie> lista = serieControl.listSerie();
        List<SerieINT> resultado = new ArrayList<>();

        for (AdmSerie admSerie :lista) {
            resultado.add(serieControl.transformSerie(admSerie));
        }

        return resultado;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public SubserieINT findsubserieById (BigInteger idSubserie) throws SystemException {
        LOGGER.info("SerieBoundary. Consultar Subserie por id: {}", idSubserie);
        AdmSubserie admSubserie = serieControl.findsubserieById(idSubserie);
        return serieControl.transformSubserie(admSubserie);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ResponseSubserieINT> listarSubserieByCodOrg (String codOrg) throws SystemException {
        LOGGER.info("SerieBoundary. Consultar Subserie por cod Org: {}", codOrg);
        return serieControl.listarSubserieByCodOrg(codOrg);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<SubserieINT> listSubseriePorSerie(BigInteger idSerie) throws SystemException {
        LOGGER.info("SerieBoundary. Listar todas las Subseries por la serie: {}", idSerie);
        List<AdmSubserie> lista = serieControl.listSubseriePorSerie(idSerie);
        List<SubserieINT> resultado = new ArrayList<>();

        for (AdmSubserie admSubserie :lista) {
            resultado.add(serieControl.transformSubserie(admSubserie));
        }

        return resultado;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<SubserieINT> listSubseriePorSerieTrd(BigInteger idSerie, String codOrg) throws SystemException {
        LOGGER.info("SerieBoundary. Listar todas las Subseries por la serie: {}", idSerie);
        List<AdmSubserie> lista = serieControl.listSubseriePorSerieTrd(idSerie, codOrg);
        List<SubserieINT> resultado = new ArrayList<>();

        for (AdmSubserie admSubserie :lista) {
            resultado.add(serieControl.transformSubserie(admSubserie));
        }

        return resultado;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<SerieINT> listSeriePorOrganigrama(Long ideOrg) throws SystemException {
        LOGGER.info("SerieBoundary. Listar todas las Series por organigrama: {}", ideOrg);
        List<AdmSerie> lista = serieControl.listSeriePorOrganigrama(ideOrg);
        List<SerieINT> resultado = new ArrayList<>();

        for (AdmSerie admSerie :lista) {
            resultado.add(serieControl.transformSerie(admSerie));
        }

        return resultado;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<SerieINT> listSeriePorOrganigramaByCod(String codOrg) throws SystemException {
        LOGGER.info("SerieBoundary. Listar todas las Series por organigrama con cod: {}", codOrg);
        List<AdmSerie> lista = serieControl.listSeriePorOrganigramaByCod(codOrg);
        List<SerieINT> resultado = new ArrayList<>();

        for (AdmSerie admSerie :lista) {
            resultado.add(serieControl.transformSerie(admSerie));
        }

        return resultado;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<SerieINT> listSeriePorOrganigramaByCodTrd(String codOrg) throws SystemException {
        LOGGER.info("SerieBoundary. Listar todas las Series por organigrama con cod: {}", codOrg);
        List<AdmSerie> lista = serieControl.listSeriePorOrganigramaByCodTrd(codOrg);
        List<SerieINT> resultado = new ArrayList<>();

        for (AdmSerie admSerie :lista) {
            resultado.add(serieControl.transformSerie(admSerie));
        }

        return resultado;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FiltroSerSubINT> filtrarSerieSubserie(Long idOrg, BigInteger idSerie, BigInteger idSubserie, BigInteger idTrd) throws SystemException {
        LOGGER.info("SerieBoundary. Filtrar serie y subserie por id, organigrama y trd: {}", idOrg, idSerie, idSubserie, idTrd);
        return serieControl.filtrarSerieSubserie(idOrg, idSerie, idSubserie, idTrd);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FiltroSerSubINT> filtrarSerieSubserieByCod(String codOrg, BigInteger idSerie, BigInteger idSubserie, BigInteger idTrd) throws SystemException {
        LOGGER.info("SerieBoundary. Filtrar serie y subserie por cod, organigrama y trd: {}", codOrg, idSerie, idSubserie, idTrd);
        return serieControl.filtrarSerieSubserieByCod(codOrg, idSerie, idSubserie, idTrd);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<TipologiaDocINT> listarTipologia (String codSerie, String codSubserie){
        LOGGER.info("SerieBoundary. Listar tipología por serie y subserie: {} ", codSerie, codSubserie);
        return serieControl.listTipologiaDoc(codSerie, codSubserie);
    }
}
