package co.com.sic.business.organigrama;

import co.com.foundation.soaint.documentmanager.integration.DependenciaINT;
import co.com.foundation.soaint.documentmanager.integration.OrganigramaINT;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class OrganigramaControl {
    private static final Logger LOGGER = LogManager.getLogger(OrganigramaControl.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional(propagation = Propagation.REQUIRED)
    public OrganigramaINT findById(Long idOrg) throws SystemException {
        try {
            LOGGER.info("OrganigramaControl. Consultar organigrama por id: {}", idOrg);
            return em.createNamedQuery("TvsOrganigramaAdministrativo.findById", OrganigramaINT.class)
                    .setParameter("ID_ORG", idOrg)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return new OrganigramaINT();
        } catch (Exception ex) {
            LOGGER.error("OrganigramaControl", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrganigramaINT findByCodigo(String codOrg) throws SystemException {
        try {
            LOGGER.info("OrganigramaControl. Consultar organigrama por id: {}", codOrg);
            return em.createNamedQuery("TvsOrganigramaAdministrativo.findByCod", OrganigramaINT.class)
                    .setParameter("COD_ORG", codOrg)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return new OrganigramaINT();
        } catch (Exception ex) {
            LOGGER.error("OrganigramaControl", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<DependenciaINT> listDependencias() throws SystemException {
        try {
            LOGGER.info("OrganigramaControl. Listar todas las dependencias.");
            return em.createNamedQuery("TvsOrganigramaAdministrativo.listDependencias", DependenciaINT.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error("OrganigramaControl", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrganigramaINT> getOrganigramasbyIdPadre(Long idOrg) throws SystemException {
        try {
            LOGGER.info("OrganigramaControl. Listando organigramas por id padre: {}", idOrg);
            return em.createNamedQuery("TvsOrganigramaAdministrativo.findByIdPadre", OrganigramaINT.class)
                    .setParameter("ID_PADRE", idOrg)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error("OrganigramaControl", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrganigramaINT> getOrganigramasbyCod(String codOrg) throws SystemException {
        try {
            LOGGER.info("OrganigramaControl. Listando organigramas por cod padre: {}", codOrg);
            return em.createNamedQuery("TvsOrganigramaAdministrativo.findByCodPadre", OrganigramaINT.class)
                    .setParameter("COD_ORG", codOrg)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error("OrganigramaControl", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrganigramaINT obtenerPrimerNivel() throws SystemException {
        try {
            LOGGER.info("OrganigramaControl. Consultar organigrama primer nivel.");
            return em.createNamedQuery("TvsOrganigramaAdministrativo.obtener_primer_nivel", OrganigramaINT.class)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return new OrganigramaINT();
        } catch (Exception ex) {
            LOGGER.error("OrganigramaControl", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrganigramaINT> obtenerHijos(Long idePadre) throws SystemException {
        try {
            LOGGER.info("OrganigramaControl. Consultar organigrama primer nivel.");
            return em.createNamedQuery("TvsOrganigramaAdministrativo.consultarHijos", OrganigramaINT.class)
                    .setParameter("ID_PADRE", idePadre)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.error("OrganigramaControl", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrganigramaINT obtenerPadrePorCodHijo(String codOrg) throws SystemException {
        try {
            LOGGER.info("OrganigramaControl. Consultar padre por codigo de organigrama.");
            return em.createNamedQuery("TvsOrganigramaAdministrativo.consultarPadrePorCodigoHijo", OrganigramaINT.class)
                    .setParameter("CODIGO", codOrg)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return OrganigramaINT.builder().build();
        } catch (Exception ex) {
            LOGGER.error("OrganigramaControl", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
