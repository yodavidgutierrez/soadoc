package co.com.sic.business.serie;

import co.com.foundation.soaint.documentmanager.domain.SerieINT;
import co.com.foundation.soaint.documentmanager.domain.SubserieINT;
import co.com.foundation.soaint.documentmanager.integration.FiltroSerSubINT;
import co.com.foundation.soaint.documentmanager.integration.ResponseSubserieINT;
import co.com.foundation.soaint.documentmanager.integration.TipologiaDocINT;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSerie;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSubserie;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.apache.commons.lang.StringUtils;
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

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SerieControl {
    private static final Logger LOGGER = LogManager.getLogger(SerieControl.class);
    private static final String SERIE_CONTROL = "SerieControl";
    private static final String GENERIC_ERROR = "system.generic.error";

    @PersistenceContext
    private EntityManager em;

    @Transactional(propagation = Propagation.REQUIRED)
    public AdmSerie findSerieById(BigInteger idSerie) throws SystemException {
        try {
            LOGGER.info("SerieControl. Consultar serie por id: {}", idSerie);
            return em.createNamedQuery("AdminSerie.searchSerieById", AdmSerie.class)
                    .setParameter("ID_SERIE", idSerie)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return new AdmSerie();
        } catch (Exception ex) {
            LOGGER.error(SERIE_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdmSerie> listSerie() throws SystemException {
        try {
            LOGGER.info("SerieControl. Listar todas las series.");
            return em.createNamedQuery("AdmSerie.findAll", AdmSerie.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error(SERIE_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AdmSubserie findsubserieById(BigInteger idSubserie) throws SystemException {
        try {
            LOGGER.info("SerieControl. Consultar Subserie por id: {}", idSubserie);
            return em.createNamedQuery("AdmSubserie.searchSubserieById", AdmSubserie.class)
                    .setParameter("ID_SUBSERIE", idSubserie)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return new AdmSubserie();
        } catch (Exception ex) {
            LOGGER.error(SERIE_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ResponseSubserieINT> listarSubserieByCodOrg(String codOrg) throws SystemException {
        try {
            LOGGER.info("SerieControl. Consultar Subserie por cod Organigrama: {}", codOrg);
            return em.createNamedQuery("AdmSubserie.searchSubserieByCodOrg", ResponseSubserieINT.class)
                    .setParameter("COD_ORG", codOrg)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error(SERIE_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdmSubserie> listSubserie() throws SystemException {
        try {
            LOGGER.info("SerieControl. Listar todas las Subseries.");
            return em.createNamedQuery("AdmSubserie.findAll", AdmSubserie.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error(SERIE_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdmSubserie> listSubseriePorSerie(BigInteger idSerie) throws SystemException {
        try {
            LOGGER.info("SerieControl. Listar todas las Subseries por la serie: {}", idSerie);
            return em.createNamedQuery("AdmSubserie.listByIdSerie", AdmSubserie.class)
                    .setParameter("ID_SERIE", idSerie)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error(SERIE_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdmSubserie> listSubseriePorSerieTrd(BigInteger idSerie, String codOrg) throws SystemException {
        try {
            LOGGER.info("SerieControl. Listar todas las Subseries por la serie: {}", idSerie);
            return em.createNamedQuery("AdmSubserie.listByIdSerieTrd", AdmSubserie.class)
                    .setParameter("ID_SERIE", idSerie)
                    .setParameter("COD_ORG", codOrg)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error(SERIE_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdmSerie> listSeriePorOrganigrama(Long ideOrg) throws SystemException {
        try {
            LOGGER.info("SerieControl. Listar todas las Series por organigrama: {}", ideOrg);
            return em.createNamedQuery("AdmSerie.listByIdOrg", AdmSerie.class)
                    .setParameter("IDE_ORG", ideOrg)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error(SERIE_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdmSerie> listSeriePorOrganigramaByCod(String codOrg) throws SystemException {
        try {
            LOGGER.info("SerieControl. Listar todas las Series por organigrama con cod: {}", codOrg);
            return em.createNamedQuery("AdmSerie.listByCodOrg", AdmSerie.class)
                    .setParameter("COD_ORG", codOrg)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error(SERIE_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdmSerie> listSeriePorOrganigramaByCodTrd(String codOrg) throws SystemException {
        try {
            LOGGER.info("SerieControl. Listar todas las Series por organigrama con cod: {}", codOrg);
            return em.createNamedQuery("AdmSerie.listByCodOrgTrd", AdmSerie.class)
                    .setParameter("COD_ORG", codOrg)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error(SERIE_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<FiltroSerSubINT> filtrarSerieSubserie(Long idOrg, BigInteger idSerie, BigInteger idSubserie, BigInteger idTrd) throws SystemException {
        LOGGER.info("SerieControl. Filtrar serie y subserie por id, organigrama y trd: {}", idOrg, idSerie, idSubserie, idTrd);

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT NEW co.com.foundation.soaint.documentmanager.integration.FiltroSerSubINT(")
                .append("s.nomSerie, s.codSerie, ss.nomSubserie, ss.codSubserie) ")
                .append("FROM AdmTabRetDoc t ")
                .append("LEFT JOIN t.ideSerie s ")
                .append("LEFT JOIN t.ideSubserie ss ");

        if (!idOrg.equals(0L)) {
            sql.append("LEFT JOIN TvsOrganigramaAdministrativo o ON o.codOrg = t.ideOfcProd AND o.valVersion = 'TOP' ");
        }

        if (!idTrd.equals(new BigInteger("0")) || !idSerie.equals(new BigInteger("0")) ||
                !idSubserie.equals(new BigInteger("0")) || !idOrg.equals(0L)) {
            sql.append("WHERE ");
        }

        String tmp = "";

        if (!idTrd.equals(new BigInteger("0"))) {
            sql.append(tmp + " t.ideTabRetDoc = :ID_TRD ");
            tmp = "AND";
        }
        if (!idSerie.equals(new BigInteger("0"))) {
            sql.append(tmp + " s.ideSerie = :ID_SERIE ");
            tmp = "AND";
        }
        if (!idSubserie.equals(new BigInteger("0"))) {
            sql.append(tmp + " ss.ideSubserie = :ID_SUBSERIE ");
            tmp = "AND";
        }
        if (!idOrg.equals(0L)) {
            sql.append(tmp + " o.ideOrgaAdmin = :ID_ORG ");
        }

        try {
            TypedQuery<FiltroSerSubINT> query = em.createQuery(sql.toString(), FiltroSerSubINT.class);

            if (!idTrd.equals(new BigInteger("0"))) {
                query.setParameter("ID_TRD", idTrd);
            }
            if (!idSerie.equals(new BigInteger("0"))) {
                query.setParameter("ID_SERIE", idSerie);
            }
            if (!idSubserie.equals(new BigInteger("0"))) {
                query.setParameter("ID_SUBSERIE", idSubserie);
            }
            if (!idOrg.equals(0L)) {
                query.setParameter("ID_ORG", idOrg);
            }

            return query.getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error(SERIE_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<FiltroSerSubINT> filtrarSerieSubserieByCod(String codOrg, BigInteger idSerie, BigInteger idSubserie, BigInteger idTrd) throws SystemException {
        LOGGER.info("SerieControl. Filtrar serie y subserie por cod, organigrama y trd: {}", codOrg, idSerie, idSubserie, idTrd);

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT NEW co.com.foundation.soaint.documentmanager.integration.FiltroSerSubINT(")
                .append("s.nomSerie, s.codSerie, ss.nomSubserie, ss.codSubserie) ")
                .append("FROM AdmTabRetDoc t ")
                .append("LEFT JOIN t.ideSerie s ")
                .append("LEFT JOIN t.ideSubserie ss ");

        if (!codOrg.isEmpty()) {
            sql.append("LEFT JOIN TvsOrganigramaAdministrativo o ON o.codOrg = t.ideOfcProd AND o.valVersion = 'TOP' ");
        }

        if (!idTrd.equals(new BigInteger("0")) || !idSerie.equals(new BigInteger("0")) ||
                !idSubserie.equals(new BigInteger("0")) || !codOrg.isEmpty()) {
            sql.append("WHERE ");
        }

        String tmp = "";

        if (!idTrd.equals(new BigInteger("0"))) {
            sql.append(tmp + " t.ideTabRetDoc = :ID_TRD ");
            tmp = "AND";
        }
        if (!idSerie.equals(new BigInteger("0"))) {
            sql.append(tmp + " s.ideSerie = :ID_SERIE ");
            tmp = "AND";
        }
        if (!idSubserie.equals(new BigInteger("0"))) {
            sql.append(tmp + " ss.ideSubserie = :ID_SUBSERIE ");
            tmp = "AND";
        }
        if (!codOrg.isEmpty()) {
            sql.append(tmp + " o.codOrg = :COD_ORG ");
        }

        try {
            TypedQuery<FiltroSerSubINT> query = em.createQuery(sql.toString(), FiltroSerSubINT.class);

            if (!idTrd.equals(new BigInteger("0"))) {
                query.setParameter("ID_TRD", idTrd);
            }
            if (!idSerie.equals(new BigInteger("0"))) {
                query.setParameter("ID_SERIE", idSerie);
            }
            if (!idSubserie.equals(new BigInteger("0"))) {
                query.setParameter("ID_SUBSERIE", idSubserie);
            }
            if (!codOrg.isEmpty()) {
                query.setParameter("COD_ORG", codOrg);
            }

            return query.getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error(SERIE_CONTROL, ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(GENERIC_ERROR)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public SerieINT transformSerie(AdmSerie admSerie) {

        SerieINT serieINT = new SerieINT();

        serieINT.setActAdministrativo(admSerie.getActAdministrativo());
        serieINT.setCarAdministrativa(admSerie.getCarAdministrativa());
        serieINT.setCarContable(admSerie.getCarContable());
        serieINT.setCarJuridica(admSerie.getCarJuridica());
        serieINT.setCarLegal(admSerie.getCarLegal());
        serieINT.setCarTecnica(admSerie.getCarTecnica());
        serieINT.setCodSerie(admSerie.getCodSerie());
        serieINT.setConClasificada(admSerie.getConClasificada());
        serieINT.setConPublica(admSerie.getConPublica());
        serieINT.setConReservada(admSerie.getConReservada());
        serieINT.setEstSerie(admSerie.getEstSerie());
        serieINT.setFueBibliografica(admSerie.getFueBibliografica());
        serieINT.setIdeMotCreacion(admSerie.getIdeMotCreacion().getIdeMotCreacion());
        serieINT.setIdeSerie(admSerie.getIdeSerie());
        serieINT.setIdeUuid(admSerie.getIdeUuid());
        serieINT.setNivEscritura(admSerie.getNivEscritura());
        serieINT.setNivLectura(admSerie.getNivLectura());
        serieINT.setNombreMotCreacion(admSerie.getIdeMotCreacion().getNomMotCreacion());
        serieINT.setNomSerie(admSerie.getNomSerie());
        serieINT.setNotAlcance(admSerie.getNotAlcance());

        return serieINT;
    }

    public SubserieINT transformSubserie(AdmSubserie admSubserie) {

        SubserieINT subserieINT = new SubserieINT();

        subserieINT.setActAdministrativo(admSubserie.getActAdministrativo());
        subserieINT.setCarAdministrativa(admSubserie.getCarAdministrativa());
        subserieINT.setCarContable(admSubserie.getCarContable());
        subserieINT.setCarJuridica(admSubserie.getCarJuridica());
        subserieINT.setCarLegal(admSubserie.getCarLegal());
        subserieINT.setCarTecnica(admSubserie.getCarTecnica());
        subserieINT.setCodSubserie(admSubserie.getCodSubserie());
        subserieINT.setConClasificada(admSubserie.getConClasificada());
        subserieINT.setConPublica(admSubserie.getConPublica());
        subserieINT.setConReservada(admSubserie.getConReservada());
        subserieINT.setEstSubserie(admSubserie.getEstSubserie());
        subserieINT.setFueBibliografica(admSubserie.getFueBibliografica());
        subserieINT.setIdeMotCreacion(admSubserie.getIdeMotCreacion().getIdeMotCreacion());
        subserieINT.setIdeSerie(admSubserie.getIdeSerie().getIdeSerie());
        subserieINT.setIdeSubserie(admSubserie.getIdeSubserie());
        subserieINT.setIdeUuid(admSubserie.getIdeUuid());
        subserieINT.setNivEscritura(admSubserie.getNivEscritura());
        subserieINT.setNivLectura(admSubserie.getNivLectura());
        subserieINT.setNombreMotCreacion(admSubserie.getIdeMotCreacion().getNomMotCreacion());
        subserieINT.setNomSubserie(admSubserie.getNomSubserie());
        subserieINT.setNotAlcance(admSubserie.getNotAlcance());

        return subserieINT;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<TipologiaDocINT> listTipologiaDoc(String codSerie, String codSubserie) {
        LOGGER.info("SerieControl. Filtrar serie y subserie por id, organigrama y trd: {}", codSerie, codSubserie);

        StringBuilder sql = new StringBuilder();

        if (!codSubserie.isEmpty()) {
            sql.append("SELECT NEW co.com.foundation.soaint.documentmanager.integration.TipologiaDocINT(sstd.ideTpgDoc.ideTpgDoc, sstd.ideTpgDoc.nomTpgDoc) ")
                    .append("FROM AdmSerSubserTpg sstd ")
                    .append("LEFT JOIN sstd.ideSerie s ")
                    .append("LEFT JOIN sstd.ideSubserie sub ")
                    .append("WHERE s.codSerie = '").append(codSerie).append("' ")
                    .append("AND sub.codSubserie = '").append(codSubserie).append("' ");
        }else{
            sql.append("SELECT DISTINCT NEW co.com.foundation.soaint.documentmanager.integration.TipologiaDocINT(sstd.ideTpgDoc.ideTpgDoc, sstd.ideTpgDoc.nomTpgDoc) ")
                    .append("FROM AdmSerSubserTpg sstd ")
                    .append("LEFT JOIN sstd.ideSerie s ")
                    .append("LEFT JOIN sstd.ideSubserie sub ")
                    .append("WHERE s.codSerie = '").append(codSerie).append("' ");
        }

        sql.append("ORDER BY sstd.ideTpgDoc.ideTpgDoc ");
        return em.createQuery(sql.toString(), TipologiaDocINT.class).getResultList();
    }
}
