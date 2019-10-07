package co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion.interfaces;

import co.com.foundation.soaint.documentmanager.domain.DataTabRetDispVO;

import co.com.foundation.soaint.documentmanager.domain.TRDTableVO;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmTabRetDoc;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;

public interface TabRetencionDiposicionManagerProxy {

    void createTabRetencionDisposicion(AdmTabRetDoc admTabRetDoc) throws SystemException, BusinessException;

    void updateTabRetencionDisposicion(AdmTabRetDoc admTabRetDoc) throws SystemException, BusinessException;

    AdmTabRetDoc searchByUniAdminAndOfcProdAndIdSerieAndSubSerie (DataTabRetDispVO data) throws SystemException, BusinessException;

    TRDTableVO trdConfByOfcProdList(String idOfcProd)throws SystemException, BusinessException;

}
