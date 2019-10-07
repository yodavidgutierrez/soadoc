package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.OrganigramaAdministrativoControl;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaAdministrativoDTO;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 22-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessBoundary
public class GestionarOrganigramaAdministrativo {
    // [fields] -----------------------------------

    @Autowired
    OrganigramaAdministrativoControl control;

    // ----------------------

    /**
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> listarDescendientesPadres() throws SystemException {
        return control.listarDescendientesPadres();
    }

    /**
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> consultarOrganigrama() throws BusinessException, SystemException {
        return control.consultarOrganigrama();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> listarElementosDeNivelInferior(BigInteger ideOrgaAdmin) throws SystemException {
        //return control.listarElementosDeNivelInferior(ideOrgaAdmin);
        return new ArrayList<>();
    }

    /**
     * @param data
     * @param codOrganigramaPadre
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> listarElementosDeNivelInferior(List<OrganigramaItemDTO> data, String codOrganigramaPadre) throws SystemException {
        return control.listarElementosDeNivelInferior(data, codOrganigramaPadre);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> listarElementosCheck(List<OrganigramaItemDTO> data, String codOrganigramaPadre) throws SystemException {
        return control.listarElementosCheck(data, codOrganigramaPadre);
    }

    /**
     * @param ideOrgaAdmin
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OrganigramaItemDTO consultarPadreDeSegundoNivel(BigInteger ideOrgaAdmin) throws BusinessException, SystemException {
        return control.listarPadreDeSegundoNivel(String.valueOf(ideOrgaAdmin));
    }

    public boolean insertarOrganigrama(OrganigramaAdministrativoDTO organigramaItemDTO) {
        return control.insertarOrganigrama(organigramaItemDTO);
    }

    public boolean generarEstructuraDatabase(List<EstructuraTrdDTO> structure) throws SystemException {
        return control.generarEstructuraDatabase(structure);
    }
}
