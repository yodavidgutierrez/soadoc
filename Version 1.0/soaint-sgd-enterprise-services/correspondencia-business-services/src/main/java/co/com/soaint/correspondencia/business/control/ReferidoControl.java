package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorReferido;
import co.com.soaint.foundation.canonical.correspondencia.ReferidoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 13-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class ReferidoControl {

    @PersistenceContext
    private EntityManager em;

    /**
     * @param idDocumento
     * @return
     * @throws SystemException
     */
    public List<ReferidoDTO> consultarReferidosByCorrespondencia(BigInteger idDocumento) throws SystemException {
        try {
            return em.createNamedQuery("CorReferido.findByIdeDocumento", ReferidoDTO.class)
                    .setParameter("IDE_DOCUMENTO", idDocumento)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param referidoDTO
     * @return
     */
    public CorReferido corReferidoTransform(ReferidoDTO referidoDTO) {
        return CorReferido.newInstance()
                .nroRadRef(referidoDTO.getNroRadRef())
                .build();
    }

    /**
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    public String consultarNroRadicadoCorrespondenciaReferida(String nroRadicado) throws SystemException {
        List<String> strings = consultarNrosRadicadoCorrespondenciaReferida(nroRadicado);
        return ObjectUtils.isEmpty(strings) ? null : strings.get(0);
    }

    /**
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    public List<String> consultarNrosRadicadoCorrespondenciaReferida(String nroRadicado) throws SystemException {
        try {
            return em.createNamedQuery("CorReferido.findByNrosRadicadoCorrespodenciaReferida", String.class)
                    .setParameter("NRO_RAD", "%" + (StringUtils.isEmpty(nroRadicado) ? "" : nroRadicado.trim()) + "%")
                    .getResultList();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex.getMessage());
            throw new SystemException(ex.getMessage());
        }
    }
}
