package co.com.foundation.soaint.documentmanager.adapter;

import co.com.foundation.soaint.documentmanager.domain.EstructuraEcmVo;
import co.com.foundation.soaint.documentmanager.integration.client.ecmintegration.EstructuraTrdDTO;
import co.com.foundation.soaint.documentmanager.integration.client.ecmintegration.GenerarEstructuraWS;
import co.com.foundation.soaint.documentmanager.integration.client.ecmintegration.MensajeRespuesta;
import co.com.foundation.soaint.infrastructure.annotations.InfrastructureService;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.transformer.Transformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrodriguez on 5/12/2016.
 */
@InfrastructureService
public class EcmClientAdapter {
    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(EcmClientAdapter.class.getName());

    @Autowired
    //private ECMIntegrationServices ecm;
    private GenerarEstructuraWS ecm;

    @Autowired
    @Qualifier("estructuraTrdTransformer")
    private Transformer<EstructuraEcmVo, EstructuraTrdDTO> transformer;

    public EcmClientAdapter() {
    }

    public boolean generarEstructuraECM(List<EstructuraEcmVo> estructuraEcm) throws BusinessException {
        List<EstructuraTrdDTO> estructuraTrdVOList = new ArrayList<>();
        try {
            estructuraEcm.stream().forEach((ecm) -> {
                estructuraTrdVOList.add(transformer.transform(ecm));
            });
            MensajeRespuesta response = ecm.createStructureECM(estructuraTrdVOList);
            return response.getCodMensaje().equals("0000");
        } catch (Exception e) {
            LOGGER.error("generic error calling ecm api ", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .buildBusinessException();
        }


    }


}
