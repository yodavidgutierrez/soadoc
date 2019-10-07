package co.com.foundation.soaint.documentmanager.business.configuracion;

import co.com.foundation.soaint.documentmanager.business.configuracion.interfaces.MotivosCreacionManagerProxy;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmMotCreacion;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.common.MessageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by jrodriguez on 13/09/2016.
 */
@BusinessBoundary
public class MotivosCreacionManager implements MotivosCreacionManagerProxy {

    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(MotivosCreacionManager.class.getName());

    @PersistenceContext
    private EntityManager em;

    public MotivosCreacionManager() {

    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmMotCreacion> findAllMotivosCreacion() throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmMotCreacion.findAll", AdmMotCreacion.class).getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }

    }

    public void createMotivosDoc(AdmMotCreacion admMotCreacion) throws SystemException, BusinessException {
        try {
            em.persist(admMotCreacion);
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void updateMotivosDoc(AdmMotCreacion admMotCreacion) throws SystemException, BusinessException {
        try {
            em.merge(admMotCreacion);
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean motivosDocExistByName(String nomMotCreacion, BigInteger id) throws SystemException, BusinessException {
        try {
            long count = em.createNamedQuery("AdmMotCreacion.countByNomMotCreacion", Long.class)
                    .setParameter("NOM_MOTIVOC", nomMotCreacion)
                    .setParameter("ID", id)
                    .getSingleResult();
            return count < 1;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

}
