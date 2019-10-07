package co.com.foundation.soaint.documentmanager.business.relEquivalencia;

import co.com.foundation.soaint.documentmanager.business.relEquivalencia.interfaces.RelEquivalenciaDocManagerProxy;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmRelEqDestino;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmRelEqOrigen;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by ADMIN on 02/12/2016.
 */
@BusinessBoundary
public class RelEquivalenciaDocManager implements RelEquivalenciaDocManagerProxy {

    @PersistenceContext
    private EntityManager em;

    public RelEquivalenciaDocManager() {
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AdmRelEqOrigen> findAllRelEqui() throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("AdmRelEqOrigen.findAllRelEqui", AdmRelEqOrigen.class)
                    .getResultList();
        } catch (Throwable ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public AdmRelEqOrigen createRelEquivaleniaOrigen(AdmRelEqOrigen relEqOrige) throws SystemException, BusinessException {
        AdmRelEqOrigen ideRelOrigen;
        try {
            ideRelOrigen = em.merge(relEqOrige);
        } catch (Throwable ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
        return ideRelOrigen;
    }

    public void createRelEquivaleniaDestino(AdmRelEqDestino relEqDestino, BigInteger ideRelOrigen) throws SystemException, BusinessException {
        try {
            relEqDestino.setAdmRelEqOrigen(new AdmRelEqOrigen(ideRelOrigen));
            em.persist(relEqDestino);

        } catch (Throwable ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    public void removeRelEquivalencia(BigInteger ideRelOrigen) throws SystemException, BusinessException {
        try {

            //Borrar Destino
            em.createNamedQuery("AdmRelEqDestino.deleteRelEquiOrigenByIdOrigen")
                    .setParameter("ID_REL_ORIGEN", ideRelOrigen)
                    .executeUpdate();

            //Borrar Origen
            em.createNamedQuery("AdmRelEqOrigen.deleteRelEquiOrigenById")
                    .setParameter("ID_REL_ORIGEN", ideRelOrigen)
                    .executeUpdate();

        } catch (Throwable ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    public boolean validateExistRelEqui(String ideUniAmtOr, String ideOfcProdOr, BigInteger ideSerieOr, BigInteger ideSubserieOr, String ideUniAmtDe, String ideOfcProdDe, BigInteger ideSerieDe, BigInteger ideSubserieDe) throws SystemException, BusinessException {

        StringBuilder builderBor = new StringBuilder();
        builderBor.append("SELECT COUNT(*)FROM AdmRelEqDestino d "
                + " INNER JOIN AdmRelEqOrigen o ON o.ideUniAmt = '"+ ideUniAmtOr+"' AND o.ideOfcProd = '"+ ideOfcProdOr+"' " );

        if (ideSerieOr != null) {
            builderBor.append(" AND o.admSerie.ideSerie = "+ideSerieOr + " ");
        }
        if (ideSubserieOr != null) {
            builderBor.append(" AND o.admSubserie.ideSubserie = "+ideSubserieOr +" ");
        }

        builderBor.append("WHERE d.ideUniAmt = '" + ideUniAmtDe + "' AND " +
                 "d.ideOfcProd =  '"+ ideOfcProdDe +"' " );

        if (ideSerieDe != null) {
            builderBor.append(" AND d.admSerie.ideSerie = "+ideSerieDe + " ");
        }
        if (ideSubserieDe != null) {
            builderBor.append(" AND d.admSubserie.ideSubserie = "+ideSubserieDe +" ");
        }

        TypedQuery<Long> query = em.createQuery(builderBor.toString(), Long.class);

        long resultado = query.getSingleResult();
        return resultado > 0;
    }


}
