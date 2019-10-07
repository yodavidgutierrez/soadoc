package co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion;

import co.com.foundation.soaint.documentmanager.domain.DataVersionTrdVO;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.entity.EntityAdmTabRetDocBuilder;
import co.com.foundation.soaint.documentmanager.persistence.entity.*;
import co.com.foundation.soaint.infrastructure.annotations.BusinessControl;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by jrodriguez on 24/11/2016.
 */

@BusinessControl
public class VesionesTablaRetencionDocControl {

    @PersistenceContext
    private EntityManager em;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public BigInteger consultarVersionActualDeTrdPorOfcProd(String idUniAmd, String idOfcProd) throws SystemException, BusinessException {
        BigInteger version;
        try {
            version = (BigInteger) em.createNamedQuery("AdmVersionTrd.consulVersionActualPorOfcProd")
                    .setParameter("ID_UNI_AMT", idUniAmd)
                    .setParameter("ID_OFC_PROD", idOfcProd)
                    .getSingleResult();
            if (version == null) {
                version = BigInteger.ZERO;
            }
            return version;
        } catch (NoResultException n) {
            return new BigInteger("0");
        } catch (Throwable ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void updateVersionTrd(String idUniAmd, String idOfcProd, BigInteger nunVersion, String valVersion,
                                 String nombreComite, String numActa, Date fechaActa) throws SystemException, BusinessException {
        try {
            em.createNamedQuery("AdmVersionTrd.updateVersion")
                    .setParameter("ID_UNI_AMT", idUniAmd)
                    .setParameter("ID_OFC_PROD", idOfcProd)
                    .setParameter("NUM_VERSION", nunVersion)
                    .setParameter("VAL_VERSION", valVersion)
                    .executeUpdate();
        } catch (Throwable ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    public List<DataVersionTrdVO> dataVersionTrd(String idUniAmd, String idOfcProd) throws SystemException, BusinessException {
        try {

            List<DataVersionTrdVO> data;

            data = em.createNamedQuery("AdmTabRetDoc.dataVersionTrdPorOfcProdSeSub", DataVersionTrdVO.class)
                    .setParameter("ID_UNI_AMT", idUniAmd)
                    .setParameter("ID_OFC_PROD", idOfcProd)
                    .getResultList();

            data.addAll(em.createNamedQuery("AdmTabRetDoc.dataVersionTrdPorOfcProdSe", DataVersionTrdVO.class)
                    .setParameter("ID_UNI_AMT", idUniAmd)
                    .setParameter("ID_OFC_PROD", idOfcProd)
                    .getResultList());
            return data;


        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("trd.versionamiento.no_data")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Throwable ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }

    }

    public AdmVersionTrd publicarVersionTRD(String idUniAmd, String idOfcProd, BigInteger version, String nombreComite,
                                            String numActa, Date fechaActa) {

        AdmVersionTrd admVersionTrd = new AdmVersionTrd();
        admVersionTrd.setIdeUniAmt(idUniAmd);
        admVersionTrd.setIdeOfcProd(idOfcProd);
        admVersionTrd.setFecVersion(new Date());
        admVersionTrd.setNumVersion(version);
        admVersionTrd.setValVersion("TOP");
        admVersionTrd.setNombreComite(nombreComite);
        admVersionTrd.setNumActa(numActa);
        admVersionTrd.setFechaActa(fechaActa);
        em.persist(admVersionTrd);
        em.flush();
        return admVersionTrd;
    }

    public void createTabRetDocOrg(DataVersionTrdVO data, BigInteger version, AdmSerie serie, AdmSubserie subserie) {

        AdmTabRetDoc tabRetDoc = EntityAdmTabRetDocBuilder.newInstance()
                .withIdeTabRetDoc(data.getIdeTabRetDoc())
                .build();

        AdmTabRetDocOrg tabRetDocOrg = new AdmTabRetDocOrg();
        tabRetDocOrg.setIdeUniAmt(data.getCodOrgUniAmd());
        tabRetDocOrg.setIdeOfcProd(data.getCodOrgOfcProd());
        tabRetDocOrg.setIdeTabRetDoc(tabRetDoc);
        tabRetDocOrg.setAgTrd(data.getAgTrd());
        tabRetDocOrg.setAcTrd(data.getAcTrd());
        tabRetDocOrg.setFecCreacion(new Date());
        tabRetDocOrg.setProTrd(data.getProTrd());
        tabRetDocOrg.setIdeDisFinal(data.getIdeDisFinal());
        tabRetDocOrg.setNombreSerie(serie.getNomSerie());
        tabRetDocOrg.setNombreSubserie(subserie.getNomSubserie());
        tabRetDocOrg.setNumVersion(version);
        em.persist(tabRetDocOrg);
    }

}
