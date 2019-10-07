package co.com.foundation.soaint.documentmanager.business.configuracion;

import co.com.foundation.soaint.documentmanager.business.configuracion.interfaces.SoporteManagerProxy;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSoporte;
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
import java.util.List;

/**
 * Created by jrodriguez on 22/09/2016.
 */
@BusinessBoundary
public class SoporteManager implements SoporteManagerProxy {


    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(SoporteManager.class.getName());

    @PersistenceContext
    private EntityManager em;

    public SoporteManager(){}


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmSoporte> findAllSoportes() throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmSoporte.findAll", AdmSoporte.class).getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }

    }

}
