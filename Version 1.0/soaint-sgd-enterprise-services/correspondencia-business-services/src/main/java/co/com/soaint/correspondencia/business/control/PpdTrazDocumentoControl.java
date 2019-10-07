package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.Funcionarios;
import co.com.soaint.correspondencia.domain.entity.PpdDocumento;
import co.com.soaint.correspondencia.domain.entity.PpdTrazDocumento;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.ObservacionesDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class PpdTrazDocumentoControl {

    // [fields] -----------------------------------

    @PersistenceContext
    private EntityManager em;

    @Autowired
    PpdDocumentoControl ppdDocumentoControl;

    @Autowired
    DependenciaControl dependenciaControl;

    // ----------------------

    /**
     * @param ppdTrazDocumentoDTO
     * @return
     */
    public PpdTrazDocumento ppdTrazDocumentoTransform(PpdTrazDocumentoDTO ppdTrazDocumentoDTO) {
        final BigInteger idFunci = ppdTrazDocumentoDTO.getIdeFunci();
        Funcionarios funcionarios = null;
        if (!ObjectUtils.isEmpty(idFunci)) {
            funcionarios = em.find(Funcionarios.class, idFunci);
        }
        return PpdTrazDocumento.newInstance()
                .observacion(ppdTrazDocumentoDTO.getObservacion())
                .funcionario(funcionarios)
                .codEstado(ppdTrazDocumentoDTO.getCodEstado())
                .ideDocumento(ppdTrazDocumentoDTO.getIdeDocumento())
                .codOrgaAdmin(ppdTrazDocumentoDTO.getCodOrgaAdmin())
                .build();
    }

    /**
     * @param ppdTrazDocumentoDTO
     */

    public void generarTrazaDocumento(PpdTrazDocumentoDTO ppdTrazDocumentoDTO) throws SystemException, BusinessException {
        try {
            PpdTrazDocumento ppdTrazDocumento = ppdTrazDocumentoTransform(ppdTrazDocumentoDTO);

            BigInteger idePpdDocumento = ppdDocumentoControl.consultarIdePpdDocumentoByIdeDocumento(ppdTrazDocumentoDTO.getIdeDocumento());

            ppdTrazDocumento.setPpdDocumento(PpdDocumento.newInstance()
                    .idePpdDocumento(idePpdDocumento)
                    .build());
            ppdTrazDocumento.setFecTrazDocumento(new Date());
            em.persist(ppdTrazDocumento);
            em.flush();
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param ideDocumento
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public ObservacionesDocumentoDTO listarTrazasDocumento(BigInteger ideDocumento) throws BusinessException, SystemException {
        try {
            List<PpdTrazDocumentoDTO> observaciones = em.createNamedQuery("PpdTrazDocumento.findAllByIdeDocumento", PpdTrazDocumentoDTO.class)
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
                    .getResultList();
            if (!ObjectUtils.isEmpty(observaciones))
                for (int i = 0; i < observaciones.size(); i++) {
                    DependenciaDTO dependencia = dependenciaControl.listarDependenciaByCodigo(observaciones.get(i).getCodOrgaAdmin());
                    observaciones.get(i).setNomDependencia(dependencia.getNomDependencia());
                }
            return ObservacionesDocumentoDTO.newInstance().observaciones(observaciones).build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error: Api listar trazas de documento en Dependencia Control")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
