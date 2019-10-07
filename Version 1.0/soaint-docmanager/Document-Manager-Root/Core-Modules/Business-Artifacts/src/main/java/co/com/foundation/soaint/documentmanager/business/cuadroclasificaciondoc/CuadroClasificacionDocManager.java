/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.foundation.soaint.documentmanager.business.cuadroclasificaciondoc;

import co.com.foundation.soaint.documentmanager.business.cuadroclasificaciondoc.interfaces.CuadroClasificacionDocManagerProxy;
import co.com.foundation.soaint.documentmanager.business.organigrama.OrganigramaControl;
import co.com.foundation.soaint.documentmanager.domain.CcdItemVO;
import co.com.foundation.soaint.documentmanager.domain.DataCcdVO;
import co.com.foundation.soaint.documentmanager.domain.DataCcdVersionVO;
import co.com.foundation.soaint.documentmanager.domain.RelEquiItemVO;
import co.com.foundation.soaint.documentmanager.infrastructure.util.EstadoOrganigramaEnum;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmCcd;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmConfigCcd;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSerie;
import co.com.foundation.soaint.documentmanager.persistence.entity.TableGenerator;
import co.com.foundation.soaint.documentmanager.persistence.entity.constants.VersionEnum;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dsanchez
 */
@BusinessBoundary
public class CuadroClasificacionDocManager implements CuadroClasificacionDocManagerProxy {

    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(CuadroClasificacionDocManager.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CuadroClasificacionDocControl control;

    @Autowired
    private OrganigramaControl controlOrganigrama;

    public CuadroClasificacionDocManager() {
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmConfigCcd> findAllCcdConfig() throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("AdmConfigCcd.findAll", AdmConfigCcd.class)
                    .getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void createCuadroClasiDocConfig(AdmConfigCcd ccdConfig) throws SystemException, BusinessException {
        try {
            em.persist(ccdConfig);
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void createCuadroClasiDoc(AdmCcd ccd) throws SystemException, BusinessException {
        try {
            em.persist(ccd);
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void updateCuadroClasiDocConfig(Long ideCcd, Integer estCcd) throws SystemException, BusinessException {
        try {

            em.createNamedQuery("AdmConfigCcd.updateCuadroClasiDocConfig")
                    .setParameter("DATE", new Date())
                    .setParameter("ESTADO", estCcd)
                    .setParameter("IDE_CCD", ideCcd)
                    .executeUpdate();

        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean existByTabRedDoc(BigInteger ideSerie, BigInteger ideSubserie, String ideOfcProd, String ideUniAmt) throws SystemException, BusinessException {
        try {
            if (ideSubserie != null) {
                long count = em.createNamedQuery("AdmTabRetDoc.countByTabRecDocBySerieAndSubSerie", Long.class)
                        .setParameter("ID_SERIE", ideSerie)
                        .setParameter("ID_SUBSERIE", ideSubserie)
                        .setParameter("ID_UNIAMT", ideUniAmt)
                        .setParameter("ID_OFIPRO", ideOfcProd)
                        .getSingleResult();
                return count > 0;
            } else {
                long count = em.createNamedQuery("AdmTabRetDoc.countByTabRecDocBySerie", Long.class)
                        .setParameter("ID_SERIE", ideSerie)
                        .setParameter("ID_UNIAMT", ideUniAmt)
                        .setParameter("ID_OFIPRO", ideOfcProd)
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean existCcd(BigInteger ideSerie, BigInteger ideSubserie, String ideOfcProd, String ideUniAmt) throws SystemException, BusinessException {
        try {
            if (ideSubserie != null) {
                long count = em.createNamedQuery("AdmConfigCcd.countByExistCcdBySerieAndSubserie", Long.class)
                        .setParameter("ID_SERIE", ideSerie)
                        .setParameter("ID_SUBSERIE", ideSubserie)
                        .setParameter("ID_UNIAMT", ideUniAmt)
                        .setParameter("ID_OFIPRO", ideOfcProd)
                        .getSingleResult();
                return count > 0;
            } else {
                long count = em.createNamedQuery("AdmConfigCcd.countByExistCcdBySerie", Long.class)
                        .setParameter("ID_SERIE", ideSerie)
                        .setParameter("ID_UNIAMT", ideUniAmt)
                        .setParameter("ID_OFIPRO", ideOfcProd)
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AdmConfigCcd searchByUniAdminAndOfcProdAndIdSerieAndSubSerie(BigInteger ideSerie, BigInteger ideSubserie, String ideOfcProd, String ideUniAmt) throws SystemException, BusinessException {
        try {
            if (ideSubserie != null) {
                return em.createNamedQuery("AdmConfigCcd.searchByUniAdminAndOfcProdAndIdSerieAndSubSerie", AdmConfigCcd.class)
                        .setParameter("ID_SERIE", ideSerie)
                        .setParameter("ID_SUBSERIE", ideSubserie)
                        .setParameter("ID_UNIAMT", ideUniAmt)
                        .setParameter("ID_OFIPRO", ideOfcProd)
                        .getSingleResult();

            } else {
                return em.createNamedQuery("AdmConfigCcd.searchByUniAdminAndOfcProdAndIdSerie", AdmConfigCcd.class)
                        .setParameter("ID_SERIE", ideSerie)
                        .setParameter("ID_UNIAMT", ideUniAmt)
                        .setParameter("ID_OFIPRO", ideOfcProd)
                        .getSingleResult();


            }

        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("retencion.documental.no_data")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized void publicarCcd(String nombreComite, String numActa, Date fechaActa) throws SystemException, BusinessException {

        int versionOrgCddMax = control.consultarMaximaVersionOrgCcd().intValue();
        int version = control.consultarVersionActualDeCcd();
        control.ajustarVersionCcd(version);
        TableGenerator tableGenerator = control.obtenerTableGeneratorVersionCCD();
        control.cambiarVersionCcd(version + 1, tableGenerator);
        version = controlOrganigrama.consultarVersionActualDeOrgranigrama();
        int versionOrg = controlOrganigrama.ajustarVersionOrganigramaCcd(versionOrgCddMax, version);


        for (AdmConfigCcd ccdConfig : findAllCcdConfig()) {
            AdmCcd ccd = new AdmCcd();
            if(ccdConfig.getEstCcd() == 1) {
                ccd.setIdeCcd(null);
                ccd.setFecCambio(ccdConfig.getFecCambio());
                ccd.setFecCreacion(ccdConfig.getFecCreacion());
                ccd.setIdeUsuarioCambio(ccdConfig.getIdeUsuarioCambio());
                ccd.setIdeOfcProd(ccdConfig.getIdeOfcProdCodOrganigrama());
                ccd.setIdeSerie(ccdConfig.getIdeSerie());

                if (ccdConfig.getIdeSubserie().getIdeSubserie() != null) {
                    ccd.setIdeSubserie(ccdConfig.getIdeSubserie());
                }
                ccd.setIdeUniAmt(ccdConfig.getIdeUnidadAdministrativa());
                ccd.setEstCcd(ccdConfig.getEstCcd());
                ccd.setValVersion(VersionEnum.TOP.name());
                ccd.setValVersionOrg(VersionEnum.TOP.name());
                ccd.setNumVersionOrg(new BigInteger(String.valueOf(versionOrg)));
                ccd.setNombreComite(nombreComite);
                ccd.setNumActa(numActa);
                ccd.setFechaActa(fechaActa);
                createCuadroClasiDoc(ccd);
            }
        }

    }

    public List<AdmCcd> listarVersionCcd() throws SystemException, BusinessException {

        List<AdmCcd> admCcds = em.createNamedQuery("AdmCcd.versionCcd", AdmCcd.class).getResultList();
        return new ArrayList<>(admCcds);
    }

    public List<RelEquiItemVO> listarVersionCcdNum() throws SystemException, BusinessException {
        List<RelEquiItemVO> admCcds = em.createNamedQuery("AdmCcd.versionCcdNum", RelEquiItemVO.class).getResultList();
        return new ArrayList<>(admCcds);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmCcd> findAllAdmCcdByValVersion(DataCcdVO data) throws BusinessException, SystemException {

        StringBuilder builderBor = new StringBuilder();

        builderBor.append("SELECT NEW co.com.foundation.soaint.documentmanager.persistence.entity.AdmCcd"
                + " (c.ideCcd, ua.ideOrgaAdmin, op.ideOrgaAdmin, s.ideSerie, ss.ideSubserie,"
                + " ua.nomOrg, ua.codOrg, op.nomOrg, op.codOrg, s.codSerie, s.nomSerie, ss.codSubserie, "
                + " ss.nomSubserie, c.estCcd, c.fecCambio, c.fecCreacion)"
                + " FROM  AdmCcd c "
                + " INNER JOIN c.ideSerie s "
                + " LEFT OUTER JOIN c.ideSubserie ss "
                + " INNER JOIN TvsOrganigramaAdministrativo ua on ua.codOrg = c.ideUniAmt "
                + " INNER JOIN TvsOrganigramaAdministrativo op on op.codOrg = c.ideOfcProd "
                + " WHERE c.valVersion  = :VAL_VERSION  AND ua.valVersion =c.valVersionOrg AND op.valVersion =c.valVersionOrg ");


        if (!data.getIdUniAmt().equals("")) {
            builderBor.append("AND ua.codOrg =:ID_UNI_ADMIN ");
        }

        if (!data.getIdOfcProd().equals("")) {
            builderBor.append("AND op.codOrg =:ID_OFC_PROD ");
        }

        TypedQuery<AdmCcd> query = em.createQuery(builderBor.toString(), AdmCcd.class);

        if (data.getVersion() != "") {
            query.setParameter("VAL_VERSION", data.getVersion());
        } else {
            query.setParameter("VAL_VERSION", VersionEnum.TOP.name());
        }

        if (!data.getIdUniAmt().equals("")) {
            query.setParameter("ID_UNI_ADMIN", data.getIdUniAmt());
        }

        if (!data.getIdOfcProd().equals("")) {
            query.setParameter("ID_OFC_PROD", data.getIdOfcProd());
        }

        List<AdmCcd> resultado = query.getResultList();
        return resultado;

    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<CcdItemVO> listarUniAdminExistInCcd(String version) throws BusinessException, SystemException {
        if(!version.equals("TOP")){
            String aux = version;
            version = "V."+aux;
        }
        return em.createNamedQuery("AdmCcd.consultarUnidadAdministrativaExistByCcd", CcdItemVO.class)
                .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getId())
                .setParameter("VERSION",version )
                .setHint("org.hibernate.cacheable", true)
                .getResultList();

    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<CcdItemVO> listarUniAdminExistInCcdDes() throws BusinessException, SystemException {

        return em.createNamedQuery("AdmCcd.consultarUnidadAdministrativaExistByCcdDes", CcdItemVO.class)
                .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getId())
                .setHint("org.hibernate.cacheable", true)
                .getResultList();

    }



    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<CcdItemVO> listarOfiProductoraExistInCcd(String codUniAdmin, String version) throws BusinessException, SystemException {
        if(!version.equals("TOP")){
            String aux = version;
            version = "V."+aux;
        }
        return em.createNamedQuery("AdmCcd.consultarOficinaProdExistByCcd", CcdItemVO.class)
                .setParameter("COD_UNI_ADMIN", codUniAdmin)
                .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getId())
                .setParameter("VERSION", version)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();

    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<CcdItemVO> listarOfiProductoraExistInCcdDe(String codUniAdmin) throws BusinessException, SystemException {

        return em.createNamedQuery("AdmCcd.consultarOficinaProdExistByCcdDe", CcdItemVO.class)
                .setParameter("COD_UNI_ADMIN", codUniAdmin)
                .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getId())
                .setHint("org.hibernate.cacheable", true)
                .getResultList();

    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmCcd> listarSeriesExistInCcd(String ideUniAmt, String ideOfcProd, String version) throws BusinessException, SystemException {
        if(!version.equals("TOP")){
            String aux = version;
            version = "V."+aux;
        }
        return em.createNamedQuery("AdmCcd.serieExistByCcd", AdmCcd.class)
                .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getId())
                .setParameter("IDEUNIADMIN", ideUniAmt)
                .setParameter("IDEOFIPROD", ideOfcProd)
                .setParameter("VERSION", version)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmCcd> listarSeriesExistInCcdDe(String ideUniAmt, String ideOfcProd) throws BusinessException, SystemException {

        return em.createNamedQuery("AdmCcd.serieExistByCcdDe", AdmCcd.class)
                .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getId())
                .setParameter("IDEUNIADMIN", ideUniAmt)
                .setParameter("IDEOFIPROD", ideOfcProd)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmCcd> listarSubSeriesExistInCcd(String ideUniAmt, String ideOfcProd, BigInteger idSerie, String version) throws BusinessException, SystemException {
        if(!version.equals("TOP")){
            String aux = version;
            version = "V."+aux;
        }
        return em.createNamedQuery("AdmCcd.subSerieExistByCcd", AdmCcd.class)
                .setParameter("IDEUNIADMIN", ideUniAmt)
                .setParameter("IDEOFIPROD", ideOfcProd)
                .setParameter("SERIE", idSerie)
                .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getId())
                .setParameter("VERSION", version)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmCcd> listarSubSeriesExistInCcdDe(String ideUniAmt, String ideOfcProd, BigInteger idSerie) throws BusinessException, SystemException {

        return em.createNamedQuery("AdmCcd.subSerieExistByCcdDe", AdmCcd.class)
                .setParameter("IDEUNIADMIN", ideUniAmt)
                .setParameter("IDEOFIPROD", ideOfcProd)
                .setParameter("SERIE", idSerie)
                .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getId())
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public BigInteger getNumVersionOrgByValVersion(String valVersion) throws BusinessException, SystemException {
        DataCcdVersionVO data = em.createNamedQuery("AdmCcd.findVersionOrgByValVersion", DataCcdVersionVO.class)
                .setParameter("VAL_VERSION", valVersion)
                .getSingleResult();
        return data.getNumVersionOrg();
    }

}
