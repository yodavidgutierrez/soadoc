package co.com.foundation.soaint.documentmanager.business.subserie;

import co.com.foundation.soaint.documentmanager.business.subserie.interfaces.SubserieManagerProxy;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSerie;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSubserie;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.common.MessageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.math.BigInteger;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrodriguez on 16/09/2016.
 */
@BusinessBoundary
public class SubserieManager implements SubserieManagerProxy {

    private static Logger LOGGER = LogManager.getLogger(SubserieManager.class.getName());

    @PersistenceContext
    private EntityManager em;

    public SubserieManager() {

    }

    public void createSubseries(AdmSubserie subserie) throws SystemException, BusinessException {
        try {

            if (subSerieExistByName(subserie.getNomSubserie())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("subseries.subseries_exits_by_name")
                        .buildBusinessException();
            }
            if (!existByIdSerie(subserie.getIdeSerie().getIdeSerie())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("series.series_not_exits_by_cod")
                        .buildBusinessException();
            }
            if (codSubserieExistByIdSerie(subserie.getCodSubserie(), subserie.getIdeSerie().getIdeSerie())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("subseries.subseries_exist_ByIdeSerie")
                        .buildBusinessException();
            }

            em.persist(subserie);
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void createSubseriesByMassiveLoader(AdmSubserie subserie) throws SystemException, BusinessException {
        try {

            if (subSerieExistByName(subserie.getNomSubserie())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("subseries.subseries_exits_by_name")
                        .buildBusinessException();
            }
            if (!existByCodSerie(subserie.getIdeSerie().getIdeSerie().toString())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("series.series_not_exits_by_id")
                        .buildBusinessException();
            }
            if (existsCodSubserieExistByIdSerie(subserie.getCodSubserie(), subserie.getIdeSerie().getIdeSerie().toString())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("subseries.subseries_exist_ByIdeSerie")
                        .buildBusinessException();
            }
            if (subserie.getEstSubserie() == 0) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("subseries.subseries_estado")
                        .buildBusinessException();
            }
            if (subserie.getCarTecnica() == 0 &&
                    subserie.getCarAdministrativa() == 0 &&
                    subserie.getCarLegal() == 0) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("subseries.subseries_caracteristicas")
                        .buildBusinessException();
            }

            AdmSerie serie = searchIdSerieByCodSerie(subserie.getIdeSerie().getIdeSerie().toString());
            subserie.setIdeSerie(serie);

            em.persist(subserie);
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AdmSerie searchIdSerieByCodSerie(String codSerie) throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmSerie.searchIdSerieByCodSerie", AdmSerie.class)
                    .setParameter("COD_SERIE", codSerie)
                    .getSingleResult();


        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmSubserie> findAllSubseries() throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmSubserie.findAll", AdmSubserie.class).getResultList();
        }
        catch (Throwable ex) {

            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();

        }


    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AdmSubserie searchSubserieById(BigInteger idSubserie) throws SystemException, BusinessException {
        try {

            AdmSubserie admSubserie = em.createNamedQuery("AdmSubserie.searchSubserieById", AdmSubserie.class)
                    .setParameter("ID_SUBSERIE", idSubserie).getSingleResult();
            return admSubserie;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }

    }


    public void updateSubserie(AdmSubserie subserie) throws SystemException, BusinessException {
        try {
            AdmSubserie subserieExist = findSubserieExistByIdSerie(subserie.getCodSubserie(),subserie.getIdeSerie().getIdeSerie());
            if(subserieExist != null) {
                em.merge(subserie);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void removeSubserie(BigInteger ideSubserie) throws SystemException, BusinessException {
        try {
            //Borrar Cuadro de clasificacion documental
            em.createNamedQuery("AdmConfigCcd.deleteSubserieById")
                    .setParameter("ID_SUBSERIE", ideSubserie)
                    .executeUpdate();

            //Borrar tabla retencion documental
            em.createNamedQuery("AdmTabRetDoc.deleteSubserieById")
                    .setParameter("ID_SUBSERIE", ideSubserie)
                    .executeUpdate();

            //Borrar asociaciones
            em.createNamedQuery("AdmSerSubserTpg.deleteSubserieById")
                    .setParameter("ID_SUBSERIE", ideSubserie)
                    .executeUpdate();

            //Borrar subseries
            em.createNamedQuery("AdmSubserie.deleteSubserieById")
                    .setParameter("ID_SUBSERIE", ideSubserie)
                    .executeUpdate();

        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex.getMessage());
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean existByIdSerie(BigInteger ideSerie) throws SystemException, BusinessException {
        try {
            long count = em.createNamedQuery("AdmSerie.countByIdSerie", Long.class)
                    .setParameter("ID_SERIE", ideSerie)
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
    public boolean existByCodSerie(String codSerie) throws SystemException, BusinessException {
        try {
            long count = em.createNamedQuery("AdmSerie.countByCodSerie", Long.class)
                    .setParameter("COD_SERIE", codSerie)
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
    public boolean subSerieExistByName(String nomSubserie) throws SystemException, BusinessException {
        try {
            long count = em.createNamedQuery("AdmSubserie.countByNomSubSerie", Long.class)
                    .setParameter("NOM_SUBSERIE", nomSubserie)
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
    public List<AdmSubserie> searchSubseriesByIdSerie(BigInteger idSerie) throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmSubserie.searchSubserieByIdSerie", AdmSubserie.class)
                    .setParameter("ID_SERIE", idSerie)
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
    public List<AdmSubserie> findByEstado(int estado) throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmSubserie.findByEstado", AdmSubserie.class)
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean subserieExistByIdInCcd(BigInteger ideSubserie) throws SystemException, BusinessException {
        try {
            long count = em.createNamedQuery("AdmCcd.countSubserieExistByIdInCcd", Long.class)
                    .setParameter("ID_SUBSERIE", ideSubserie)
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
    public boolean codSubserieExistByIdSerie(String codSubserie, BigInteger ideSerie) throws SystemException, BusinessException {
        try {
            long count = em.createNamedQuery("AdmSubserie.countSubserieExistByIdSerie", Long.class)
                    .setParameter("COD_SUBSERIE", codSubserie)
                    .setParameter("ID_SERIE", ideSerie)
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
    public AdmSubserie findSubserieExistByIdSerie(String codSubserie, BigInteger ideSerie) throws SystemException, BusinessException {
        try {
            AdmSubserie subserie = em.createNamedQuery("AdmSubserie.findSubserieExistByIdSerie", AdmSubserie.class)
                    .setParameter("ID_SERIE", ideSerie)
                    .setParameter("COD_SUBSERIE",codSubserie)
                    .getSingleResult();
            return subserie;

        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }

    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean existsCodSubserieExistByIdSerie(String codSubserie, String ideSerie) throws SystemException, BusinessException {
        try {
            long count = em.createNamedQuery("AdmSubserie.countCodSubserieExistByIdSerie", Long.class)
                    .setParameter("COD_SUBSERIE", codSubserie)
                    .setParameter("ID_SERIE", ideSerie)
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


}