package co.com.foundation.soaint.documentmanager.business.configuracion.interfaces;

import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmDisFinal;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by jrodriguez on 11/10/2016.
 */
public interface DisposicionFinalManagerProxy {

    List<AdmDisFinal> findAllDisposicionFinal() throws SystemException, BusinessException;

    void createDisposicionFinal(AdmDisFinal admDisFinal) throws SystemException, BusinessException;

    void updateDisposicionFinal(AdmDisFinal admDisFinal) throws SystemException, BusinessException;

    boolean DisposicionesExistByName(String nomDisFinal, BigInteger id) throws SystemException;

    boolean DisposicionesExistByDesc(String descDisFinal, BigInteger id) throws SystemException;

}
