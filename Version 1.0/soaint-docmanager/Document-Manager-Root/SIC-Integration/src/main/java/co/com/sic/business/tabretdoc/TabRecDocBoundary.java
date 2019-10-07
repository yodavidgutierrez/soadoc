package co.com.sic.business.tabretdoc;

import co.com.foundation.soaint.documentmanager.integration.FiltroSerSubINT;
import co.com.foundation.soaint.documentmanager.integration.ResponseDTO;
import co.com.foundation.soaint.documentmanager.integration.TabRetDocINT;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmTabRetDoc;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
public class TabRecDocBoundary {
    private static final Logger LOGGER = LogManager.getLogger(TabRecDocBoundary.class);

    @Autowired
    private TabRetDocControl tabRetDocControl;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public TabRetDocINT findById(BigInteger idTab) throws SystemException {
        LOGGER.info("TabRecDocBoundary. Consultar tabla de retencion por id: {}", idTab);
        AdmTabRetDoc admTabRetDoc = tabRetDocControl.findById(idTab);
        return tabRetDocControl.transform(admTabRetDoc);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<TabRetDocINT> getByIdSerieSubserie(BigInteger idSerie, BigInteger idSubserie) throws SystemException {
        LOGGER.info("TabRecDocBoundary. Consultar tabla de retencion por serie y subserie: {}", idSerie, idSubserie);
        List<TabRetDocINT> lista = new ArrayList<>();
        for (AdmTabRetDoc admTabRetDoc : tabRetDocControl.getByIdSerieSubserie(idSerie, idSubserie)) {
            lista.add(tabRetDocControl.transform(admTabRetDoc));
        }
        return lista;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public TabRetDocINT getByCodOrgSerieSubserie(String codOrg, String codSerie, String codSubserie, Long idOrg) throws SystemException {
        LOGGER.info("TabRecDocBoundary. Consultar tabla de retencion por codigo de organigrama, serie y subserie: {}", codOrg, codSerie, codSubserie, idOrg);
        BigInteger id = tabRetDocControl.getByCodOrgSerieSubserie(codOrg, codSerie, codSubserie, idOrg);
        if (id == null) {
            return TabRetDocINT.builder().build();
        }
        return findById(id);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResponseDTO getVersionTrdPorCodOrg(String codOrg) throws SystemException {
        LOGGER.info("TabRecDocBoundary. Obtener version TRD por codigo de organigrama: {}", codOrg);
        return ResponseDTO.builder()
                .codigo("00")
                .mensaje("")
                .value(tabRetDocControl.getVersionTrdPorCodOrg(codOrg))
                .build();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResponseDTO getIdDisposicionFinal(String codDF) throws SystemException {
        LOGGER.info("TabRecDocBoundary. Obtener id de disposicion final dado el codigo: {}", codDF);
        return ResponseDTO.builder()
                .codigo("00")
                .mensaje("")
                .value(tabRetDocControl.getIdDisposicionFinal(codDF).toString())
                .build();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FiltroSerSubINT getDisposicionFinal(BigInteger idDispoFinal) throws SystemException {
        LOGGER.info("TabRecDocBoundary. Obtener id de disposicion final dado el codigo: {}", idDispoFinal);
        return tabRetDocControl.getDisposicionFinal(idDispoFinal);
    }
}
