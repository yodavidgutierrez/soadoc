package co.com.foundation.soaint.documentmanager.business.tipologiadocumental;

import co.com.foundation.soaint.documentmanager.business.tipologiadocumental.interfaces.TipologiasDocManagerProxy;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.common.MessageUtil;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmTpgDoc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by jrodriguez on 22/09/2016.
 */
@BusinessBoundary
public class TipologiasDocManager implements TipologiasDocManagerProxy {

    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(TipologiasDocManager.class.getName());

    @PersistenceContext
    private EntityManager em;

    public TipologiasDocManager() {
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmTpgDoc> findAllTipologiasDoc() throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmTpgDoc.findAll", AdmTpgDoc.class).getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AdmTpgDoc findById(BigInteger idTipo) throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmTpgDoc.findById", AdmTpgDoc.class)
                    .setParameter("IDTIPO", idTipo)
                    .getSingleResult();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
    
    public void createTpgDoc(AdmTpgDoc tpgDoc) throws SystemException, BusinessException {
        try {
            if (tpcDocExistByName(tpgDoc.getNomTpgDoc())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("tipologias.tpgdoc_exits_by_name")
                        .buildBusinessException();
            }
            if (!existIdSoporte(tpgDoc.getIdeSoporte().getIdeSoporte())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("soporte.soporte_not_exists")
                        .buildBusinessException();
            }
            if(tpgDoc.getEstTpgDoc() == 0){
                throw ExceptionBuilder.newBuilder()
                        .withMessage("tipologias.tpgdoc_estado")
                        .buildBusinessException();
            }
            if(tpgDoc.getCarTecnica() == 0 &&
                    tpgDoc.getCarLegal() == 0 &&
                    tpgDoc.getCarAdministrativa() == 0 &&
                    tpgDoc.getCarContable() == 0 &&
                    tpgDoc.getCarJuridico() == 0){
                throw ExceptionBuilder.newBuilder()
                        .withMessage("tipologias.tpgdoc_caracteristicas")
                        .buildBusinessException();
            }
            em.persist(tpgDoc);
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void updateTpgDoc(AdmTpgDoc tpgDoc) throws SystemException, BusinessException {
        try {
            em.merge(tpgDoc);
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    public void removeTpgDoc(BigInteger idTpgDoc) throws SystemException, BusinessException {
        try {
            em.createNamedQuery("AdmSerSubserTpg.deleteByIdTpgDoc")
                    .setParameter("ID_TPGDOC", idTpgDoc)
                    .executeUpdate();

            em.createNamedQuery("AdmTpgDoc.deleteByIdTpgDoc")
                    .setParameter("ID_TPGDOC", idTpgDoc)
                    .executeUpdate();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * Validar nombre no repetido en base de datos*
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean tpcDocExistByName(String nomTpgDoc) throws SystemException, BusinessException {
        try {
            long count = em.createNamedQuery("AdmTpgDoc.countByNomTpgDoc", Long.class)
                    .setParameter("NOM_TPG_DOC", nomTpgDoc)
                    .getSingleResult();
            return count > 0;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean existIdSoporte(BigInteger ideSoporte) throws SystemException, BusinessException {
        try {
            long count = em.createNamedQuery("AdmSoporte.countByIdSoporte", Long.class)
                    .setParameter("IDE_SOPORTE", ideSoporte)
                    .getSingleResult();
            return count > 0;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmTpgDoc> findByEstado(int estado) throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmTpgDoc.findByEstado", AdmTpgDoc.class)
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean tipoDocExistByIdInCcd(BigInteger ideSubserie) throws SystemException, BusinessException {
        try {
            List<Long> count = em.createNamedQuery("AdmTpgDoc.countTiposDocExistByIdInCcd", Long.class)
                    .setParameter("ID_TPG_DOC", ideSubserie)
                    .getResultList();
            int cont = 0;
            for(Long  l : count){
                cont ++;
            }
            return cont > 0;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }





}
