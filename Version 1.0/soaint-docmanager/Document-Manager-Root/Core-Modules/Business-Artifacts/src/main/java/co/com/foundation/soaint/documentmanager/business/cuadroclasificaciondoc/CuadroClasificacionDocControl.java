package co.com.foundation.soaint.documentmanager.business.cuadroclasificaciondoc;

import co.com.foundation.soaint.documentmanager.persistence.entity.TableGenerator;
import co.com.foundation.soaint.documentmanager.persistence.entity.constants.VersionEnum;
import co.com.foundation.soaint.infrastructure.annotations.BusinessControl;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.BigInteger;

@BusinessControl
public class CuadroClasificacionDocControl {

    private static final String GET_CURRENT_VERSION = "SELECT SEQ_VALUE FROM TABLE_GENERATOR WHERE SEQ_NAME = 'VERSION_CCD_SEQ'";
    private static final String UPDATE_CURRENT_VERSION = "UPDATE TABLE_GENERATOR SET SEQ_VALUE = ?1 WHERE SEQ_NAME = 'VERSION_CCD_SEQ'";


    @PersistenceContext
    private EntityManager em;

    // -------------------------------
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public int consultarVersionActualDeCcd() {
        BigInteger version = (BigInteger) em.createNativeQuery(GET_CURRENT_VERSION).getSingleResult();
        return version.intValue();
    }


    // -------------------------------
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cambiarVersionCcd(int version, TableGenerator tableGenerator) {
        tableGenerator.setSeqValue(new Long(version));
        em.merge(tableGenerator);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TableGenerator obtenerTableGeneratorVersionCCD() {
        TableGenerator tableGenerator = em.createNamedQuery("TableGenerator.findBySeqName", TableGenerator.class)
                .setParameter("NAME", "VERSION_CCD_SEQ").getSingleResult();

        return tableGenerator;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void ajustarVersionCcd(int version) {
        em.createNamedQuery("AdmCcd.updateVersion")
                .setParameter("VAL_VERSION", "V." + version)
                .setParameter("VERSION", VersionEnum.TOP.name())
                .executeUpdate();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public BigInteger consultarMaximaVersionOrgCcd(){
        int result = (int) em.createNamedQuery("AdmCcd.maxVersionOrgByCdd").getSingleResult();
        if(result == 0){
            return BigInteger.ZERO;
        }else {
            return new BigInteger(String.valueOf(result));
        }
    }


}
