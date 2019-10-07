package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.DctAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 11-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
public class DctAsignacionControl extends GenericControl<DctAsignacion> {


    public DctAsignacionControl() {
        super(DctAsignacion.class);
    }

    public DctAsignacion dctAsignacionTransform(AsignacionDTO asignacionDTO) {
        return DctAsignacion.newInstance()
                .ideAsignacion(asignacionDTO.getIdeAsignacion())
                .fecAsignacion(asignacionDTO.getFecAsignacion())
                .ideFunci(asignacionDTO.getIdeFunci())
                .codDependencia(asignacionDTO.getCodDependencia())
                .codTipAsignacion(asignacionDTO.getCodTipAsignacion())
                .observaciones(asignacionDTO.getObservaciones())
                .codTipCausal(asignacionDTO.getCodTipCausal())
                .codTipProceso(asignacionDTO.getCodTipProceso())
                .build();
    }

    public boolean existeAsignacion(BigInteger idDocumento){
        List<DctAsignacion> asignaciones = em.createNamedQuery("DctAsignacion.findObjByIdDocumento", DctAsignacion.class)
                .setParameter("ID_DOCUMENTO", idDocumento)
                .getResultList();
        if(ObjectUtils.isEmpty(asignaciones))
            return false;
        return true;
    }

}
