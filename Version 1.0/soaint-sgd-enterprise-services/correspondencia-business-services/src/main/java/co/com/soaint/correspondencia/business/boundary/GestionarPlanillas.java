package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.PlanillasControl;
import co.com.soaint.foundation.canonical.correspondencia.PlanillaDTO;
import co.com.soaint.foundation.canonical.correspondencia.PlanillaSalidaDTO;
import co.com.soaint.foundation.canonical.correspondencia.ReportDTO;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoPlanillaEnum;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 14-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
public class GestionarPlanillas {

    // [fields] -----------------------------------

    @Autowired
    PlanillasControl control;

    // ----------------------

    /**
     * @param planilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public PlanillaDTO generarPlanilla(PlanillaDTO planilla) throws SystemException {
        return control.generarPlanilla(planilla);
    }

    /**
     * @param planilla
     * @throws SystemException
     */
    public void cargarPlanilla(PlanillaDTO planilla) throws SystemException {
        control.cargarPlanilla(planilla);
    }

    /**
     * @param nroPlanilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public PlanillaDTO listarPlanillasByNroPlanilla(String nroPlanilla) throws SystemException {
        return control.listarPlanillasByNroPlanilla(nroPlanilla, EstadoPlanillaEnum.DISTRIBUCION.getCodigo());
    }

    /**
     * @param nroPlanilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public PlanillaSalidaDTO listarPlanillasSalidaByNroPlanilla(String nroPlanilla) throws BusinessException, SystemException {
        return control.listarPlanillasSalidaByNroPlanilla(nroPlanilla, EstadoPlanillaEnum.DISTRIBUCION.getCodigo());
    }

    /**
     * @param nroPlanilla
     * @param formato
     * @return
     * @throws SystemException
     */
    public ReportDTO exportarPlanilla(String nroPlanilla, String formato) throws SystemException {
        return control.exportarPlanilla(nroPlanilla, formato);
    }

    public ReportDTO exportarPlanillaSalida(String nroPlanilla, String formato, String tipoComunicacion) throws SystemException {
        return control.exportarPlanillaSalida(nroPlanilla, formato, tipoComunicacion);
    }
}
