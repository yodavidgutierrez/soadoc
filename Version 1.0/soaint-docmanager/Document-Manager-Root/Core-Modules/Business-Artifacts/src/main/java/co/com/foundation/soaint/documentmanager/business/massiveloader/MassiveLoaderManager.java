package co.com.foundation.soaint.documentmanager.business.massiveloader;

import co.com.foundation.soaint.documentmanager.business.massiveloader.interfaces.MassiveLoaderManagerProxy;
import co.com.foundation.soaint.documentmanager.persistence.entity.CmCargaMasiva;
import co.com.foundation.soaint.documentmanager.persistence.entity.CmRegistroCargaMasiva;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@BusinessBoundary
public class MassiveLoaderManager implements MassiveLoaderManagerProxy{

    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(MassiveLoaderManager.class.getName());

    @PersistenceContext
    private EntityManager em;

    public MassiveLoaderManager() {
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<CmCargaMasiva> listarCargasMasivas() throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("CmCargaMasiva.findAll", CmCargaMasiva.class).getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }

    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<CmRegistroCargaMasiva> listarRegistrosDeCargaMasiva(Long id) throws SystemException, BusinessException {

        try {
            return em.createNamedQuery("CmRegistroCargaMasiva.findAll", CmRegistroCargaMasiva.class).setParameter("ID_CARGA", id).getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
