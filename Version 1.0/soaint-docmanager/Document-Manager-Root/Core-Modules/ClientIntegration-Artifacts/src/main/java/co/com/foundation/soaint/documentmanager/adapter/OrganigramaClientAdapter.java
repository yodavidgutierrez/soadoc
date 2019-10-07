package co.com.foundation.soaint.documentmanager.adapter;

import co.com.foundation.soaint.documentmanager.integration.client.organigrama.GestionarOrganigramaAdministrativoWS;
import co.com.foundation.soaint.documentmanager.integration.client.organigrama.OrganigramaAdministrativoDTO;
import co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.EstructuraTrdDTO;
import co.com.foundation.soaint.infrastructure.annotations.InfrastructureService;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by erodriguez on 16/11/2018.
 */
@InfrastructureService
public class OrganigramaClientAdapter {
    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(OrganigramaClientAdapter.class.getName());

    @Autowired
    private GestionarOrganigramaAdministrativoWS ecm;

    @Autowired
    private co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.GestionarOrganigramaAdministrativoWS client;

    public OrganigramaClientAdapter() {
    }

    public boolean exportarOrganigrama(OrganigramaAdministrativoDTO organigramaAdministrativoDTO) throws BusinessException {
        try {
            boolean response = ecm.crearOrganigrama(organigramaAdministrativoDTO);
            return response;
        } catch (Exception e) {
            LOGGER.error("generic error calling ecm api ", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .buildBusinessException();
        }


    }

    public boolean generarOrganigrama(List<EstructuraTrdDTO> organigramaItem) throws BusinessException {
        try {
            boolean response = client.generarEstructura(organigramaItem);
            return response;
        } catch (Exception e) {
            LOGGER.error("generic error calling ecm api ", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .buildBusinessException();
        }


    }


}
