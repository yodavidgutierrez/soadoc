package co.com.foundation.soaint.documentmanager.business.configuracion;

import co.com.foundation.soaint.documentmanager.business.configuracion.interfaces.TradicionDocManagerProxy;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmTradDoc;
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
public class TradicionDocManager  implements TradicionDocManagerProxy{

    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(TradicionDocManager.class.getName());

    @PersistenceContext
    private EntityManager em;

    public TradicionDocManager() {
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmTradDoc> findAllTradicionDoc() throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmTradDoc.findAll", AdmTradDoc.class).getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }

    }


}
