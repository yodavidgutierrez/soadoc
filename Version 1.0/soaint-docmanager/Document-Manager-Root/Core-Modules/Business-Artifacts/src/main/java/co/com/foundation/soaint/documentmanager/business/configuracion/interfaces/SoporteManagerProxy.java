package co.com.foundation.soaint.documentmanager.business.configuracion.interfaces;

import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSoporte;

import java.util.List;

/**
 * Created by jrodriguez on 11/10/2016.
 */
public interface SoporteManagerProxy {

    List<AdmSoporte> findAllSoportes() throws SystemException, BusinessException;
}
