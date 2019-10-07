package co.com.foundation.soaint.documentmanager.business.configuracion;

import co.com.foundation.soaint.documentmanager.business.configuracion.interfaces.DisposicionFinalManagerProxy;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmDisFinal;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by jrodriguez on 29/09/2016.
 */
@BusinessBoundary
public class DisposicionFinalManager implements DisposicionFinalManagerProxy{

    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(DisposicionFinalManager.class.getName());

    @PersistenceContext
    private EntityManager em;

    public DisposicionFinalManager() {
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmDisFinal> findAllDisposicionFinal() throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmDisFinal.findAll", AdmDisFinal.class).getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }

    }

    public void createDisposicionFinal(AdmDisFinal admDisFinal) throws SystemException, BusinessException {
        try {
            em.persist(admDisFinal);
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void updateDisposicionFinal(AdmDisFinal admDisFinal) throws SystemException, BusinessException {
        try {
            em.merge(admDisFinal);
        } catch (Exception ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildBusinessException();

        }
    }
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean DisposicionesExistByName(String nomDisFinal, BigInteger id) throws SystemException {
        try {
            long count = em.createNamedQuery("AdmDisFinal.countByNomMotDisposicion", Long.class)
                    .setParameter("NOM_DIS_FINAL", nomDisFinal)
                    .setParameter("ID", id)
                    .getSingleResult();
            return count < 1;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean DisposicionesExistByDesc(String descDisFinal, BigInteger id) throws SystemException {
        try {
            long count = em.createNamedQuery("AdmDisFinal.countByDescMotDisposicion", Long.class)
                    .setParameter("DES_DIS_FINAL", descDisFinal)
                    .setParameter("ID", id)
                    .getSingleResult();
            return count < 1;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
