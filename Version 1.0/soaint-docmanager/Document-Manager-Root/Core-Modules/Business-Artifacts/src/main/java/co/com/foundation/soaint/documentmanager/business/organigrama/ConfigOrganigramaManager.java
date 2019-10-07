package co.com.foundation.soaint.documentmanager.business.organigrama;

import co.com.foundation.soaint.documentmanager.business.organigrama.interfaces.ConfigOrganigramaManagerProxy;
import co.com.foundation.soaint.documentmanager.domain.OrganigramaItemVO;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.documentmanager.persistence.entity.TvsConfigOrgAdministrativo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrodriguez on 30/10/2016.
 */

@BusinessBoundary
public class ConfigOrganigramaManager implements ConfigOrganigramaManagerProxy {

    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(ConfigOrganigramaManager.class.getName());

    @PersistenceContext
    private EntityManager em;

    public ConfigOrganigramaManager() {
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemVO> consultarOrganigrama()  throws SystemException, BusinessException {
        List<OrganigramaItemVO> list = new ArrayList<>();
       try{
        OrganigramaItemVO raiz = em.createNamedQuery("TvsConfigOrgAdministrativo.consultarElementoRayz", OrganigramaItemVO.class)
        ///        .setHint("org.hibernate.cacheable", true)
                .getSingleResult();

            list = listarElementosDeNivelInferior(raiz.getIdeOrgaAdmin());
            list.add(raiz);

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
        return list;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemVO> listarElementosDeNivelInferior(final Long ideOrgaAdmin) {

        List<OrganigramaItemVO> data = em.createNamedQuery("TvsConfigOrgAdministrativo.consultarDescendientesDirectos", OrganigramaItemVO.class)
                .setParameter("ID_PADRE", ideOrgaAdmin)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();

        consultarElementosRecursivamente(new ArrayList<>(data), data);

        return data;
    }

    public void consultarElementosRecursivamente(final List<OrganigramaItemVO> data, final List<OrganigramaItemVO> storage) {

        for (OrganigramaItemVO item : data) {

            List<OrganigramaItemVO> hijos = em.createNamedQuery("TvsConfigOrgAdministrativo.consultarDescendientesDirectos", OrganigramaItemVO.class)
                    .setParameter("ID_PADRE", item.getIdeOrgaAdmin())
                    .setHint("org.hibernate.cacheable", true)
                    .getResultList();

                    storage.addAll(hijos);
                    consultarElementosRecursivamente(new ArrayList<>(hijos), storage);

        }

    }

    @Override
    public void updateOrganigrama(TvsConfigOrgAdministrativo organigrama) throws SystemException, BusinessException {
        try {
            if (organigramaExistByNameAndDifferentId(organigrama.getNomOrg(), organigrama.getIdeOrgaAdmin())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("organigrama.organigrama_exits_by_name")
                        .buildBusinessException();
            }

            em.merge(organigrama);
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

    @Override
    public void createOrganigrama(TvsConfigOrgAdministrativo organigrama) throws SystemException, BusinessException {
        try {
            em.persist(organigrama);
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }

    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemVO> listarElementosOrganigrama() throws SystemException, BusinessException {

        List<OrganigramaItemVO> listElementos;

        OrganigramaItemVO padre = em.createNamedQuery("TvsConfigOrgAdministrativo.consultarElementoRayz", OrganigramaItemVO.class)
                .setHint("org.hibernate.cacheable", true)
                .getSingleResult();

        listElementos = em.createNamedQuery("TvsConfigOrgAdministrativo.consultarElemetosOrganigrama", OrganigramaItemVO.class)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();

        listElementos.add(padre);

        return listElementos;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean organigramaExistByCode(String codOrg) throws SystemException, BusinessException {
        try {
            long count = em.createNamedQuery("TvsConfigOrgAdministrativo.countByCodOrga", Long.class)
                    .setParameter("COD_ORGA", codOrg)
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
    public boolean organigramaExistByName(String nomOrg) throws SystemException, BusinessException {
        try {
            long count = em.createNamedQuery("TvsConfigOrgAdministrativo.countByNomOrga", Long.class)
                    .setParameter("NOM_ORGA", nomOrg)
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




    @Override
    public void cleanOrganigrama() throws  BusinessException, SystemException {
        try {
            em.createNamedQuery("TvsConfigOrgAdministrativo.cleanOrganigrama").executeUpdate();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean organigramaExistByNameAndDifferentId(String nomOrg, Long id) throws SystemException, BusinessException {
        try {

            long count = em.createNamedQuery("TvsConfigOrgAdministrativo.countByNomOrgaAndDifferentId", Long.class)
                    .setParameter("NOM_ORGA", nomOrg)
                    .setParameter("ID", id)
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
