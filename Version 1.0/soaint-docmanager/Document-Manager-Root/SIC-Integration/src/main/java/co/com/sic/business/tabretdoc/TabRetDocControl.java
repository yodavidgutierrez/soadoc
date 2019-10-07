package co.com.sic.business.tabretdoc;

import co.com.foundation.soaint.documentmanager.integration.FiltroSerSubINT;
import co.com.foundation.soaint.documentmanager.integration.TabRetDocINT;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmTabRetDoc;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TabRetDocControl {
    private static final Logger LOGGER = LogManager.getLogger(TabRetDocControl.class);
    private static final String TAB_CONTROL = "TabRetDocControl";
    private static final String GENERIC_ERROR = "system.generic.error";

    @PersistenceContext
    private EntityManager em;

    @Transactional(propagation = Propagation.REQUIRED)
    public AdmTabRetDoc findById(BigInteger idTab) throws SystemException {
        try {
            LOGGER.info("TabRetDocControl. Consultar tabla de retencion por id: {}", idTab);
            return em.createNamedQuery("AdmTabRetDoc.findById", AdmTabRetDoc.class)
                    .setParameter("ID_TAB", idTab)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return new AdmTabRetDoc();
        } catch (Exception ex) {
            LOGGER.error(TAB_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdmTabRetDoc> getByIdSerieSubserie(BigInteger idSerie, BigInteger idSubserie) throws SystemException {
        try {
            LOGGER.info("TabRetDocControl. Consultar tabla de retencion por id: {}", idSerie, idSubserie);
            return em.createNamedQuery("AdmTabRetDoc.findBySerieSubserie", AdmTabRetDoc.class)
                    .setParameter("ID_SER", idSerie.equals(new BigInteger("0")) ? null : idSerie)
                    .setParameter("ID_SUB", idSubserie.equals(new BigInteger("0")) ? null : idSubserie)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error(TAB_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public BigInteger getByCodOrgSerieSubserie(String codOrg, String codSerie, String codSubserie, Long idOrg) throws SystemException {
        try {
            LOGGER.info("TabRetDocControl. Consultar tabla de retencion por codigo de organigrama, serie y subserie: {}", codOrg, codSerie, codSubserie, idOrg);

            StringBuilder query = new StringBuilder();
            query.append("SELECT MAX(tr.ideTabRetDoc) ");
            query.append("FROM TvsOrganigramaAdministrativo og ");
            query.append("INNER JOIN AdmCcd cc ON (cc.ideOfcProd = og.codOrg AND og.valVersion = 'TOP') ");
            query.append("INNER JOIN AdmTabRetDoc tr ON (tr.ideSerie.ideSerie = cc.ideSerie.ideSerie ");
            query.append("AND tr.ideOfcProd = cc.ideOfcProd) ");
            query.append("INNER JOIN AdmSerie se ON tr.ideSerie.ideSerie = se.ideSerie ");
            if (!codSubserie.equals("")) {
                query.append("INNER JOIN AdmSubserie su ON tr.ideSubserie.ideSubserie = su.ideSubserie ");
            }
            query.append("WHERE og.codOrg = :COD_ORG AND se.codSerie = :COD_SER");
            if (!codSubserie.equals("")) {
                query.append(" AND (:COD_SUB = '' OR su.codSubserie = :COD_SUB)");
            }
            if (idOrg != null) {
                query.append(" AND og.ideOrgaAdmin = :IDE_ORG");
            }

            TypedQuery<BigInteger> q = em.createQuery(query.toString(), BigInteger.class);
            q.setParameter("COD_ORG", codOrg);
            q.setParameter("COD_SER", codSerie);
            if (!codSubserie.equals("")) {
                q.setParameter("COD_SUB", codSubserie);
            }
            if (idOrg != null) {
                q.setParameter("IDE_ORG", idOrg);
            }

            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            LOGGER.error(TAB_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String getVersionTrdPorCodOrg(String codOrg) throws SystemException {
        try {
            LOGGER.info("TabRetDocControl. Obtener version TRD por codigo de organigrama: {}", codOrg);
            return em.createNamedQuery("AdmVersionTrd.consultVersionPorCodigoOrganigrama", BigInteger.class)
                    .setParameter("COD_ORG", codOrg)
                    .getSingleResult()
                    .toString();
        } catch (NoResultException ex) {
            return "0";
        } catch (Exception ex) {
            LOGGER.error(TAB_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public BigInteger getIdDisposicionFinal(String codDF) throws SystemException {
        try {
            LOGGER.info("TabRetDocControl. Obtener id de disposicion final dado el codigo: {}", codDF);
            return em.createNamedQuery("AdmDisFinal.getIdByCod", BigInteger.class)
                    .setParameter("CODIGO", codDF)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return new BigInteger("0");
        } catch (Exception ex) {
            LOGGER.error(TAB_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public FiltroSerSubINT getDisposicionFinal(BigInteger idDispoFinal) throws SystemException {
        try {
            LOGGER.info("TabRetDocControl. Obtener disposicion final dado el id: {}", idDispoFinal);
            return em.createNamedQuery("AdmDisFinal.getDispoFinalById", FiltroSerSubINT.class)
                    .setParameter("IDE", idDispoFinal)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return new FiltroSerSubINT();
        } catch (Exception ex) {
            LOGGER.error(TAB_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public TabRetDocINT transform(AdmTabRetDoc admTabRetDoc) {

        TabRetDocINT tabRetDocINT = new TabRetDocINT();

        tabRetDocINT.setAcTrd(admTabRetDoc.getAcTrd());
        tabRetDocINT.setAgTrd(admTabRetDoc.getAgTrd());
        tabRetDocINT.setCodOrg(admTabRetDoc.getIdeOfcProd() == null ? "" : admTabRetDoc.getIdeOfcProd());
        tabRetDocINT.setEstTabRetDoc(admTabRetDoc.getEstTabRetDoc());
        tabRetDocINT.setIdDisFinal(admTabRetDoc.getAdmDisFinal() == null ? null : admTabRetDoc.getAdmDisFinal().getIdeDisFinal());
        tabRetDocINT.setIdeSerie(admTabRetDoc.getIdeSerie() == null ? null : admTabRetDoc.getIdeSerie().getIdeSerie());
        tabRetDocINT.setIdeSubserie(admTabRetDoc.getIdeSubserie() == null ? null : admTabRetDoc.getIdeSubserie().getIdeSubserie());
        tabRetDocINT.setIdeUniAmt(admTabRetDoc.getIdeUniAmt() == null ? "" : admTabRetDoc.getIdeUniAmt());
        tabRetDocINT.setIdeUuid(admTabRetDoc.getIdeUuid() == null ? "" : admTabRetDoc.getIdeUuid());
        tabRetDocINT.setIdeTabRetDoc(admTabRetDoc.getIdeTabRetDoc());
        tabRetDocINT.setNivEscritura(admTabRetDoc.getNivEscritura());
        tabRetDocINT.setNivLectura(admTabRetDoc.getNivLectura());
        tabRetDocINT.setNomDisFinal(admTabRetDoc.getAdmDisFinal() == null ? "" : admTabRetDoc.getAdmDisFinal().getNomDisFinal());
        tabRetDocINT.setProTrd(admTabRetDoc.getProTrd());

        return tabRetDocINT;
    }
}
