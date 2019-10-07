package co.com.foundation.soaint.documentmanager.business.tipologiadocumental.interfaces;

import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmTpgDoc;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by jrodriguez on 10/10/2016.
 */
public interface TipologiasDocManagerProxy {

    List<AdmTpgDoc> findAllTipologiasDoc() throws SystemException, BusinessException;

    void createTpgDoc(AdmTpgDoc tpgDoc) throws SystemException, BusinessException;

    void updateTpgDoc(AdmTpgDoc tpgDoc) throws SystemException, BusinessException;

    void removeTpgDoc(BigInteger idTpgDoc) throws SystemException, BusinessException;

    boolean tpcDocExistByName(String nomTpgDoc) throws SystemException, BusinessException;
    
    List<AdmTpgDoc> findByEstado(int estado) throws SystemException, BusinessException;
    
    AdmTpgDoc findById(BigInteger idTipo) throws SystemException, BusinessException;

    boolean existIdSoporte( BigInteger ideSoporte) throws SystemException, BusinessException;

    boolean tipoDocExistByIdInCcd (BigInteger idTipo) throws SystemException, BusinessException;

}
