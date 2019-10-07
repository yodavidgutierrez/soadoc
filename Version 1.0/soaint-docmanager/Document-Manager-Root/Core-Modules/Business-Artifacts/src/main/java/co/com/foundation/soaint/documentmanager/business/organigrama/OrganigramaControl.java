package co.com.foundation.soaint.documentmanager.business.organigrama;

import co.com.foundation.soaint.documentmanager.persistence.entity.TableGenerator;
import co.com.foundation.soaint.infrastructure.annotations.BusinessControl;
import co.com.foundation.soaint.documentmanager.persistence.entity.TvsConfigOrgAdministrativo;
import co.com.foundation.soaint.documentmanager.persistence.entity.TvsOrganigramaAdministrativo;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@BusinessControl
public class OrganigramaControl {

    private static final String GET_CURRENT_VERSION = "SELECT SEQ_VALUE FROM TABLE_GENERATOR WHERE SEQ_NAME = 'VERSION_ORGANIGRAMA_SEQ'";
    private static final String UPDATE_CURRENT_VERSION = "UPDATE TABLE_GENERATOR SET SEQ_VALUE = ?1 WHERE SEQ_NAME = 'VERSION_ORGANIGRAMA_SEQ'";


    @PersistenceContext
    private EntityManager em;

    // -------------------------------

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public int consultarVersionActualDeOrgranigrama(){
        /*BigInteger version = (BigInteger) em.createNativeQuery(GET_CURRENT_VERSION).getSingleResult();*/
        TableGenerator tableGenerator = em.createNamedQuery("TableGenerator.findBySeqName", TableGenerator.class)
                .setParameter("NAME", "VERSION_ORGANIGRAMA_SEQ").getSingleResult();
        return tableGenerator.getSeqValue().intValue();
    }


    // -------------------------------

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cambiarVersionOrganigrama( int version ) throws SystemException {
        try {
            em.createNamedQuery("TableGenerator.updateOrgVersion")
                    .setParameter("VAL_VERSION", new Long(version))
                    .executeUpdate();
            em.flush();
        }catch (Exception ex){
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    // -------------------------------

    @Transactional(propagation = Propagation.REQUIRED)
    public TvsOrganigramaAdministrativo publicarItemOrganigrama( TvsConfigOrgAdministrativo config,
                                                                 TvsOrganigramaAdministrativo parent ) throws SystemException {
        try{
        TvsOrganigramaAdministrativo entity = new TvsOrganigramaAdministrativo();

        entity.setCodNivel( config.getCodNivel() );
        entity.setCodOrg( config.getCodOrg() );
        entity.setDescOrg( config.getDescOrg() );
        entity.setNomOrg( config.getNomOrg() );
        entity.setIndEsActivo( config.getIndEsActivo() );
        entity.setIdeOrgaAdminPadre( parent );
        entity.setFecCreacion(new Date());
        entity.setValVersion("TOP");
        entity.setAbrevOrg(config.getAbrevOrg());
        entity.setIdeUuid(config.getIdeUuid());

        em.persist(entity);
        return entity;
        } catch(Exception ex){
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    // -------------------------

    @Transactional(propagation = Propagation.REQUIRED)
    public void publicarItemOrganigramaRecursivamente(final List<TvsConfigOrgAdministrativo> data,
                                                      final TvsOrganigramaAdministrativo parent) throws SystemException {

        for (TvsConfigOrgAdministrativo item : data)
        {
            TvsOrganigramaAdministrativo itemParent = publicarItemOrganigrama(item,parent);
            List<TvsConfigOrgAdministrativo> hijos = em.createNamedQuery("TvsConfigOrgAdministrativo.consultarDescendientesDirectosConEntity", TvsConfigOrgAdministrativo.class)
                    .setParameter("ID_PADRE", item.getIdeOrgaAdmin())
                    .setHint("org.hibernate.cacheable", true)
                    .getResultList();

            publicarItemOrganigramaRecursivamente(hijos,itemParent);
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void ajustarVersionOrganigrama() throws SystemException{

        int version = consultarVersionActualDeOrgranigrama();

        em.createNamedQuery("TvsOrganigramaAdministrativo.updateVersion")
                .setParameter("VAL_VERSION","V."+version)
                .executeUpdate();

        TableGenerator tableGenerator = em.createNamedQuery("TableGenerator.findBySeqName", TableGenerator.class)
                .setParameter("NAME", "VERSION_ORGANIGRAMA_SEQ").getSingleResult();

        tableGenerator.setSeqValue(version+1L);
        em.merge(tableGenerator);

        //cambiarVersionOrganigrama(version+1);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public int ajustarVersionOrganigramaCcd(int versionOrgCddMax, int version){


        if(versionOrgCddMax != version ){
            em.createNamedQuery("AdmCcd.updateVersionOrg")
                    .setParameter("VAL_VERSION", "V." + (version - 1))
                    .setParameter("NUM_VERSION",new BigInteger(String.valueOf((version-1))))
                    .executeUpdate();
        }
        return version;
    }
}
