package co.com.foundation.soaint.documentmanager.business.subserie.interfaces;

import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSubserie;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by jrodriguez on 9/10/2016.
 */

public interface SubserieManagerProxy {

    void createSubseries(AdmSubserie subserie) throws SystemException, BusinessException;

    void createSubseriesByMassiveLoader(AdmSubserie subserie) throws SystemException, BusinessException;

    List<AdmSubserie> findAllSubseries() throws SystemException, BusinessException;

    AdmSubserie searchSubserieById(BigInteger idSubserie) throws SystemException, BusinessException;

    void updateSubserie(AdmSubserie subserie) throws SystemException, BusinessException;

    void removeSubserie(BigInteger ideSubserie) throws SystemException, BusinessException;

    boolean subSerieExistByName(String nomSubserie) throws SystemException, BusinessException;
    
    List<AdmSubserie> searchSubseriesByIdSerie(BigInteger idSerie) throws SystemException, BusinessException;

    List<AdmSubserie> findByEstado(int estado) throws SystemException, BusinessException;

    boolean subserieExistByIdInCcd (BigInteger ideSubserie) throws SystemException, BusinessException;
    
}
