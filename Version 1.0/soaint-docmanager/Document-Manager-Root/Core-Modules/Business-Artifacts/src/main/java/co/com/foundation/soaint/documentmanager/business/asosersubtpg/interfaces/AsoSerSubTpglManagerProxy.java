/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.foundation.soaint.documentmanager.business.asosersubtpg.interfaces;

import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSerSubserTpg;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmTpgDoc;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface AsoSerSubTpglManagerProxy {
    
    List<AdmSerSubserTpg> findAllAsoc() throws SystemException, BusinessException;
    
    List<AdmSerSubserTpg> findAllAsocGroup() throws SystemException, BusinessException;
    
    void createAsoc(AdmSerSubserTpg asociacion) throws SystemException, BusinessException;
   
    List<AdmSerSubserTpg> findAsocBySerAndSubSer(BigInteger idSerie, BigInteger idSubSerie) throws SystemException, BusinessException;
    
    List<AdmSerSubserTpg> findAsocBySerAndSubServ(BigInteger idSerie) throws SystemException, BusinessException;
  
    List<AdmTpgDoc> findTipBySerAndSubSer(BigInteger idSerie, BigInteger idSubSerie) throws SystemException, BusinessException;

    List<AdmTpgDoc> findTipBySer(BigInteger idSerie) throws  SystemException,BusinessException;

    void removeAsocById(BigDecimal idAsoc) throws SystemException, BusinessException;

    void removeAsocBySerie(BigInteger idSerie) throws SystemException, BusinessException;

    void removeAsocBySubSerie(BigInteger idSubSerie) throws SystemException, BusinessException;

    boolean serieOrSubserieExistById(BigInteger idSerie, BigInteger idSubserie) throws SystemException, BusinessException;
    
}
