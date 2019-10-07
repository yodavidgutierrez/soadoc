package co.com.foundation.soaint.documentmanager.business.configuracion.interfaces;

import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmMotCreacion;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by jrodriguez on 11/10/2016.
 */
public interface MotivosCreacionManagerProxy {

    List<AdmMotCreacion> findAllMotivosCreacion() throws SystemException, BusinessException;

    void createMotivosDoc(AdmMotCreacion admMotCreacion) throws SystemException, BusinessException;

    void updateMotivosDoc(AdmMotCreacion admMotCreacion) throws SystemException, BusinessException;

    boolean motivosDocExistByName(String nomMotCreacion, BigInteger id) throws SystemException, BusinessException;

}
