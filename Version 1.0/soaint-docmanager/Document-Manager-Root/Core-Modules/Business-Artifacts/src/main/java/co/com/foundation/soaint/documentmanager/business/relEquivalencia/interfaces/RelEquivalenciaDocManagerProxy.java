package co.com.foundation.soaint.documentmanager.business.relEquivalencia.interfaces;

import co.com.foundation.soaint.documentmanager.persistence.entity.AdmRelEqDestino;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmRelEqOrigen;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by ADMIN on 02/12/2016.
 */
public interface RelEquivalenciaDocManagerProxy {

    List<AdmRelEqOrigen> findAllRelEqui() throws BusinessException, SystemException;

    AdmRelEqOrigen createRelEquivaleniaOrigen(AdmRelEqOrigen relEqOrige) throws SystemException, BusinessException;

    void createRelEquivaleniaDestino(AdmRelEqDestino relEqDestino, BigInteger ideRelOrigen) throws SystemException, BusinessException;

    void removeRelEquivalencia(BigInteger ideRelOrigen) throws SystemException, BusinessException;

    boolean validateExistRelEqui(String ideUniAmtOr, String ideOfcProdOr, BigInteger ideSerieOr, BigInteger ideSubserieOr,
                                 String ideUniAmtDe, String ideOfcProdDe, BigInteger ideSerieDe, BigInteger ideSubserieDe) throws SystemException, BusinessException;



}
