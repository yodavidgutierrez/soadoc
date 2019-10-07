/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.foundation.soaint.documentmanager.business.asosersubtpg;

import co.com.foundation.soaint.documentmanager.business.asosersubtpg.interfaces.AsoSerSubTpglManagerProxy;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSerSubserTpg;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSerie;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSubserie;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmTpgDoc;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.common.MessageUtil;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ADMIN
 */
@BusinessBoundary
public class AsoSerSubTpgManager implements AsoSerSubTpglManagerProxy {

    private static Logger LOGGER = LogManager.getLogger(AsoSerSubTpgManager.class.getName());

    @PersistenceContext
    private EntityManager em;

    public AsoSerSubTpgManager() {
    }
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmSerSubserTpg> findAllAsoc() throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmSerSubserTpg.findAll", AdmSerSubserTpg.class).getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmSerSubserTpg> findAllAsocGroup() throws SystemException, BusinessException {
        try {
            List<AdmSerSubserTpg> listAsociacionesFinal = new ArrayList<>();
            List<AdmSerSubserTpg> listAsociaciones = em.createNamedQuery("AdmSerSubserTpg.findAllAsocGroup", AdmSerSubserTpg.class).getResultList();


            for (AdmSerSubserTpg admAsociacion : listAsociaciones) {
                AdmSerSubserTpg adm = new AdmSerSubserTpg();

                AdmSerie serie = new AdmSerie();
                serie.setIdeSerie(admAsociacion.getIdeSerie().getIdeSerie());
                serie.setNomSerie(admAsociacion.getIdeSerie().getNomSerie());
                serie.setCodSerie(admAsociacion.getIdeSerie().getCodSerie());

                AdmSubserie subSerie = new AdmSubserie();
                subSerie.setIdeSubserie(admAsociacion.getIdeSubserie().getIdeSubserie());
                subSerie.setNomSubserie(admAsociacion.getIdeSubserie().getNomSubserie());
                subSerie.setCodSubserie(admAsociacion.getIdeSubserie().getCodSubserie());

                AdmTpgDoc tipDoc = new AdmTpgDoc();
                tipDoc.setIdeTpgDoc(admAsociacion.getIdeTpgDoc().getIdeTpgDoc());
                tipDoc.setNomTpgDoc(admAsociacion.getIdeTpgDoc().getNomTpgDoc());

                adm.setIdeSerie(serie);
                adm.setIdeSubserie(subSerie);
                adm.setIdeTpgDoc(tipDoc);
                listAsociacionesFinal.add(adm);

            }
            return listAsociacionesFinal;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void createAsoc(AdmSerSubserTpg asociacion) throws SystemException, BusinessException {
        try {
            if(asociacion.getIdeSubserie() != null){
                if (asocExistByCodeBySerieAndSubserie(asociacion.getIdeSerie().getIdeSerie(),
                        asociacion.getIdeSubserie().getIdeSubserie(),
                        asociacion.getIdeTpgDoc().getIdeTpgDoc())) {
                    throw ExceptionBuilder.newBuilder()
                            .withMessage("generic.asociacion_exits_by_name")
                            .buildBusinessException();
                }
            }else{
                if (asocExistByCodeBySerie(asociacion.getIdeSerie().getIdeSerie(),
                        asociacion.getIdeTpgDoc().getIdeTpgDoc())) {
                    throw ExceptionBuilder.newBuilder()
                            .withMessage("generic.asociacion_exits_by_name")
                            .buildBusinessException();
                }
            }


            em.persist(asociacion);
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean asocExistByCodeBySerieAndSubserie(BigInteger idSerie, BigInteger idSubserie, BigInteger idTpg) throws SystemException, BusinessException {
        try {
            long count = em.createNamedQuery("AdmSerSubserTpg.countByCodAsocSerieAndSubserie", Long.class)
                    .setParameter("COD_SERIE", idSerie)
                    .setParameter("COD_SUBSERIE", idSubserie)
                    .setParameter("COD_TPG", idTpg)
                    .getSingleResult();
            return count > 0;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean asocExistByCodeBySerie(BigInteger idSerie, BigInteger idTpg) throws SystemException, BusinessException {
        try {
            long count = em.createNamedQuery("AdmSerSubserTpg.countByCodAsocSerie", Long.class)
                    .setParameter("COD_SERIE", idSerie)
                    .setParameter("COD_TPG", idTpg)
                    .getSingleResult();
            return count > 0;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmSerSubserTpg> findAsocBySerAndSubSer(BigInteger idSerie, BigInteger idSubSerie) throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmSerSubserTpg.findAsocBySerAndSubSer", AdmSerSubserTpg.class)
                    .setParameter("IDSERIE", idSerie)
                    .setParameter("IDSUBSERIE", idSubSerie)
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
    public List<AdmSerSubserTpg> findAsocBySerAndSubServ(BigInteger idSerie) throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmSerSubserTpg.findAsocBySerAndSubServ", AdmSerSubserTpg.class)
                    .setParameter("IDSERIE", idSerie)
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
    public List<AdmTpgDoc> findTipBySerAndSubSer(BigInteger idSerie, BigInteger idSubSerie) throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmSerSubserTpg.findTipBySerAndSubSer", AdmTpgDoc.class)
                    .setParameter("IDSERIE", idSerie)
                    .setParameter("IDSUBSERIE", idSubSerie)
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
    public List<AdmTpgDoc> findTipBySer(BigInteger idSerie) throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmSerSubserTpg.findTipBySer", AdmTpgDoc.class)
                    .setParameter("IDSERIE", idSerie)
                    .getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void removeAsocById(BigDecimal idAsoc) throws SystemException, BusinessException {
        try {
            em.createNamedQuery("AdmSerSubserTpg.deleteAsocById")
                    .setParameter("IDE_REL_SST", idAsoc)
                    .executeUpdate();

        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void removeAsocBySerie(BigInteger idSerie) throws SystemException, BusinessException {
        try {
            em.createNamedQuery("AdmSerSubserTpg.deleteSerieById")
                    .setParameter("ID_SERIE", idSerie)
                    .executeUpdate();

        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void removeAsocBySubSerie(BigInteger idSubSerie) throws SystemException, BusinessException {
        try {
            em.createNamedQuery("AdmSerSubserTpg.deleteSubserieById")
                    .setParameter("ID_SUBSERIE", idSubSerie)
                    .executeUpdate();

        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean serieOrSubserieExistById(BigInteger idSerie, BigInteger idSubserie) throws SystemException, BusinessException {
        try {
            if (idSerie != null && idSubserie != null && idSubserie.equals("0")) {
                long count = em.createNamedQuery("AdmSerSubserTpg.countBySerieAndSubserie", Long.class)
                        .setParameter("ID_SERIE", idSerie)
                        .setParameter("ID_SUBSERIE", idSubserie)
                        .getSingleResult();
                return count > 0;
            } else {
                long count = em.createNamedQuery("AdmSerSubserTpg.countBySerie", Long.class)
                        .setParameter("ID_SERIE", idSerie)
                        .getSingleResult();
                return count > 0;
            }
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
