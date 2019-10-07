package co.com.foundation.soaint.documentmanager.business.organigrama;

import co.com.foundation.soaint.documentmanager.business.organigrama.interfaces.OrganigramaManagerProxy;
import co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion.interfaces.VersionesTabRetDocManagerProxy;
import co.com.foundation.soaint.documentmanager.domain.*;
import co.com.foundation.soaint.documentmanager.infrastructure.util.EstadoOrganigramaEnum;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmVersionTrd;
import co.com.foundation.soaint.documentmanager.persistence.entity.TvsConfigOrgAdministrativo;
import co.com.foundation.soaint.documentmanager.persistence.entity.TvsOrganigramaAdministrativo;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@BusinessBoundary
public class OrganigramaManager implements OrganigramaManagerProxy {

    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(OrganigramaManager.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrganigramaControl control;

    @Autowired
    private VersionesTabRetDocManagerProxy version;

    @Autowired
    private OrganigramaManagerProxy organigrama;


    public OrganigramaManager() {
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized void publicarOrganigrama() throws SystemException, BusinessException {

        control.ajustarVersionOrganigrama();

        TvsConfigOrgAdministrativo raiz = em.createNamedQuery("TvsConfigOrgAdministrativo.consultarElementoRayzConEntity", TvsConfigOrgAdministrativo.class)
                .setHint("org.hibernate.cacheable", true)
                .getSingleResult();

        TvsOrganigramaAdministrativo rootParent = control.publicarItemOrganigrama(raiz, null);

        List<TvsConfigOrgAdministrativo> hijos = em.createNamedQuery("TvsConfigOrgAdministrativo.consultarDescendientesDirectosConEntity", TvsConfigOrgAdministrativo.class)
                .setParameter("ID_PADRE", raiz.getIdeOrgaAdmin())
                .setHint("org.hibernate.cacheable", true)
                .getResultList();

        control.publicarItemOrganigramaRecursivamente(hijos, rootParent);
    }

    // -------------------------

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemVO> listarElementosDeSegundoNivel() {

        OrganigramaItemVO raiz = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayz", OrganigramaItemVO.class)
                .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getName())
                .setHint("org.hibernate.cacheable", true)
                .getSingleResult();

        List<OrganigramaItemVO> lista = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemVO.class)
               .setParameter("ID_PADRE", raiz.getIdeOrgaAdmin())
                .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getName())
                .setHint("org.hibernate.cacheable", true)
                .getResultList();

        List<OrganigramaItemVO> resultado = new ArrayList<>();
        consultarElementosRecursivamenteSinHojas(lista, resultado);

        return resultado;
    }

    // -------------------------

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemVO> listarElementosDeNivelInferior(final Long ideOrgaAdmin) {
        List<OrganigramaItemVO> data = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemVO.class)
                .setParameter("ID_PADRE", ideOrgaAdmin)
                .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getName())
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
        //consultarElementosRecursivamente(new ArrayList<>(data), data);
        return data;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemVO> listarElementosDeNivelInferiorPorVersion(final Long ideOrgaAdmin, String valVersion) {
        List<OrganigramaItemVO> data = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectosVersion", OrganigramaItemVO.class)
                .setParameter("ID_PADRE", ideOrgaAdmin)
                .setParameter("VAL_VERSION", valVersion)
                .getResultList();
        consultarElementosRecursivamenteVersion(new ArrayList<>(data), data, valVersion);
        return data;
    }

    // -------------------------


    public void consultarElementosRecursivamente(final List<OrganigramaItemVO> data, final List<OrganigramaItemVO> storage) {

        for (OrganigramaItemVO item : data) {
            List<OrganigramaItemVO> hijos = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemVO.class)
                    .setParameter("ID_PADRE", item.getIdeOrgaAdmin())
                    .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getName())
                    .setHint("org.hibernate.cacheable", true)
                    .getResultList();
            storage.addAll(hijos);
            consultarElementosRecursivamente(new ArrayList<>(hijos), storage);
        }

    }

    public void consultarElementosRecursivamenteSinHojas(final List<OrganigramaItemVO> data, final List<OrganigramaItemVO> storage) {

        List<OrganigramaItemVO> next = new ArrayList<>();

        for (OrganigramaItemVO item : data) {
            List<OrganigramaItemVO> hijos = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemVO.class)
                    .setParameter("ID_PADRE", item.getIdeOrgaAdmin())
                    .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getName())
                    .setHint("org.hibernate.cacheable", true)
                    .getResultList();
            if (!CollectionUtils.isEmpty(hijos)){
                storage.add(item);
                next.addAll(hijos);

            }
        }

        if (!CollectionUtils.isEmpty(next)) {
            consultarElementosRecursivamenteSinHojas(next, storage);
        }
    }


    public void consultarElementosRecursivamenteVersion(final List<OrganigramaItemVO> data, final List<OrganigramaItemVO> storage, String valVersion) {

        for (OrganigramaItemVO item : data) {
            List<OrganigramaItemVO> hijos = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectosVersion", OrganigramaItemVO.class)
                    .setParameter("ID_PADRE", item.getIdeOrgaAdmin())
                    .setParameter("VAL_VERSION", valVersion)
                    .getResultList();
            storage.addAll(hijos);
            consultarElementosRecursivamenteVersion(new ArrayList<>(hijos), storage, valVersion);
        }

    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<TvsOrganigramaAdministrativo> listarVersionOrganigrama() throws SystemException, BusinessException {

        List<TvsOrganigramaAdministrativo> versionesOrganigrama = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarVersionesOrganigrama", TvsOrganigramaAdministrativo.class).getResultList();
        return versionesOrganigrama;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemVO> consultarOrganigramaPorVersion(String valVersion) throws SystemException, BusinessException {

        OrganigramaItemVO raiz = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayzVersion", OrganigramaItemVO.class)
                .setParameter("VAL_VERSION", valVersion)
                .getSingleResult();

        List<OrganigramaItemVO> list = listarElementosDeNivelInferiorPorVersion(raiz.getIdeOrgaAdmin(), valVersion);
        list.add(raiz);
        return list;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemVO> listarElementosDeNivelSuperior(final Long ideOrgaAdmin) {
        List<OrganigramaItemVO> items = new ArrayList<>();

        TvsOrganigramaAdministrativo hijo = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoPorId", TvsOrganigramaAdministrativo.class)
                .setParameter("ID", ideOrgaAdmin)
                .setHint("org.hibernate.cacheable", true)
                .getSingleResult();

        items.add(new OrganigramaItemVO(hijo.getIdeOrgaAdmin(), hijo.getCodOrg(), hijo.getNomOrg(), hijo.getIndEsActivo(), hijo.getCodNivel(), hijo.getAbrevOrg(), hijo.getIdeUuid()));

        while (hijo.getIdeOrgaAdminPadre() != null) {
            hijo = hijo.getIdeOrgaAdminPadre();
            items.add(new OrganigramaItemVO(hijo.getIdeOrgaAdmin(), hijo.getCodOrg(), hijo.getNomOrg(), hijo.getIndEsActivo(), hijo.getCodNivel(), hijo.getAbrevOrg(), hijo.getIdeUuid()));
            //  if (hijo.getIdeOrgaAdminPadre() != null) {
            //      items.add(new OrganigramaItemVO(hijo.getIdeOrgaAdmin(), hijo.getCodOrg(), hijo.getNomOrg(), hijo.getIndEsActivo()));
            //  }
        }
        Collections.sort(items, new OrganigramaItemVO.CompNivel());
        return items;

    }


    @Override
    public List<OrganigramaItemVO> consultarOrganigramaTop() throws SystemException, BusinessException {

        List<OrganigramaItemVO> list = new ArrayList<>();
        OrganigramaItemVO raiz = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayz", OrganigramaItemVO.class)
                .setParameter("STATUS", EstadoOrganigramaEnum.ACTIVO.getName())
                .setHint("org.hibernate.cacheable", true)
                .getSingleResult();
        list.add(raiz);
        list.addAll(listarElementosDeNivelInferior(raiz.getIdeOrgaAdmin()));
        return list;
    }

    public OrganigramaItemVO consultarElemetosOrganigramaPorNombre(String codigoOrganigrama) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElemetosOrganigramaPorNombre", OrganigramaItemVO.class)
                    .setParameter("CODIGO_ORGA", codigoOrganigrama)
                    .getSingleResult();
        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("organigrama.organigrama.no_data")
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

    public List<TvsOrganigramaAdministrativo> findAllUniAmdTrd() throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("TvsOrganigramaAdministrativo.findAllUniAmdTrd", TvsOrganigramaAdministrativo.class)
                    .getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    public List<TvsOrganigramaAdministrativo> findAllUniAmdCddOrg(String versionCdd) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("TvsOrganigramaAdministrativo.findAllUniAmdCddOrg", TvsOrganigramaAdministrativo.class)
                    .setParameter("VERSION_CCD", versionCdd)
                    .getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public List<TvsOrganigramaAdministrativo> findAllOfcProdTrd(String ideUniAmt) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("TvsOrganigramaAdministrativo.findAllOfcProdTrd", TvsOrganigramaAdministrativo.class)
                    .setParameter("ID_UNI_AMT", ideUniAmt)
                    .getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public List<TvsOrganigramaAdministrativo> findAllOfcProdCcdOrg(String versionCdd, String ideUniAmt) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("TvsOrganigramaAdministrativo.findAllOfcProdCcdOrg", TvsOrganigramaAdministrativo.class)
                    .setParameter("VERSION_CCD", versionCdd)
                    .setParameter("ID_UNI_AMT", ideUniAmt)
                    .getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public List<TvsOrganigramaAdministrativo> findAllOfcProdTrdECM() throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("TvsOrganigramaAdministrativo.findAllOfcProdTrdECM", TvsOrganigramaAdministrativo.class)
                    .getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public List<TvsOrganigramaAdministrativo> findAllUniAmdTrdOrg(String valVersionOrg) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("TvsOrganigramaAdministrativo.findAllUniAmdTrdOrg", TvsOrganigramaAdministrativo.class)
                    .setParameter("VAL_VERSION_ORG", valVersionOrg)
                    .getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    public List<TvsOrganigramaAdministrativo> findAllOfcProdTrdOrg(String ideUniAmt, String codVersionOrg) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("TvsOrganigramaAdministrativo.findAllOfcProdTrdOrg", TvsOrganigramaAdministrativo.class)
                    .setParameter("VAL_VERSION_ORG", codVersionOrg)
                    .setParameter("ID_UNI_AMT", ideUniAmt)
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
    public List<EstructuraEcmVo> obtenerEstructuraDocumental() throws SystemException, BusinessException {
        List<EstructuraEcmVo> estructuraTrdList = new ArrayList<>();

        List<TvsOrganigramaAdministrativo> tvsOrganigramaList = organigrama.findAllOfcProdTrdECM();

        for (TvsOrganigramaAdministrativo item : tvsOrganigramaList) {

            EstructuraEcmVo estructura = new EstructuraEcmVo();

            for (OrganigramaItemVO elementos : organigrama.listarElementosDeNivelSuperior(item.getIdeOrgaAdmin())) {

                OrganigramaVo organigrama = new OrganigramaVo();
                organigrama.setIdeOrgaAdmin(elementos.getIdeOrgaAdmin());
                organigrama.setCodOrg(elementos.getCodOrg());
                organigrama.setNomOrg(elementos.getNomOrg());
                organigrama.setTipo(elementos.getNivel().equals(0) ? "P" : "H");

                estructura.getOrganigramaItemList().add(organigrama);
            }

            AdmVersionTrd admVersionTrd = version.consulVersionOfcProdTop(item.getCodOrg());
            List<DataTrdEcmVO> dataTrdEcmVOList = version.dataTrdByOfcProdListEcm(item.getCodOrg(), admVersionTrd.getIdeVersion());


            for (DataTrdEcmVO trdEcmVO : dataTrdEcmVOList) {

                ContenidoTrdVo contenido = new ContenidoTrdVo();
                contenido.setIdOrgAdm(trdEcmVO.getUnidadAdministrativa());
                contenido.setIdOrgOfc(trdEcmVO.getOficinaProductora());
                contenido.setCodSerie(trdEcmVO.getCodSerie());
                contenido.setNomSerie(trdEcmVO.getNomSerie());
                contenido.setCodSubSerie(trdEcmVO.getCodSubSerie());
                contenido.setNomSubSerie(trdEcmVO.getNomSubSerie());
                contenido.setRetArchivoGestion(trdEcmVO.getArchivoGestion());
                contenido.setRetArchivoCentral(trdEcmVO.getArchivoCentral());
                contenido.setProcedimiento(trdEcmVO.getProcedimientos());
                contenido.setDiposicionFinal(trdEcmVO.getDiposicion());
                estructura.getContenidoDependenciaList().add(contenido);
            }

            estructuraTrdList.add(estructura);


        }
        return estructuraTrdList;
    }
}
