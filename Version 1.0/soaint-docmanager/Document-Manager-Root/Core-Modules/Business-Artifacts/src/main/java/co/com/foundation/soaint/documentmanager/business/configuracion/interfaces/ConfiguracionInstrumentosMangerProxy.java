package co.com.foundation.soaint.documentmanager.business.configuracion.interfaces;

import co.com.foundation.soaint.documentmanager.domain.ItemVO;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;

/**
 * Created by jrodriguez on 27/10/2016.
 */
public interface ConfiguracionInstrumentosMangerProxy {

    ItemVO consultarEstadoInstrumento(String instrumento) throws BusinessException, SystemException;

    String consultarEstadosIntrumentos(ItemVO itemInstrumento)throws BusinessException, SystemException;

    void setEstadoInstrumento(ItemVO item)throws BusinessException, SystemException;
}
