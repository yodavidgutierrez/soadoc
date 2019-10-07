package co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion.interfaces;

import co.com.foundation.soaint.documentmanager.domain.DataReportTrdVO;
import co.com.foundation.soaint.documentmanager.domain.DataTrdEcmVO;
import co.com.foundation.soaint.documentmanager.domain.TRDTableVO;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmVersionTrd;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by jrodriguez on 24/11/2016.
 */
public interface VersionesTabRetDocManagerProxy {

    void publicarTablaRetencionDocumental(String idUniAmd , String idOfcProd, String nombreComite, String numActa, String fechaActa) throws SystemException,BusinessException;

    List<AdmVersionTrd> findAllVersionPorOfcProd(String idOfcProd) throws SystemException, BusinessException;

    TRDTableVO versionTrdByOfcProdList(String idOfcProd, BigInteger idVersion, String valVersionOrg) throws SystemException, BusinessException;

    DataReportTrdVO dataTrdByOfcProdList(String idOfcProd, BigInteger idVersion, String valVersionOrg) throws SystemException, BusinessException;

    AdmVersionTrd consulVersionOfcProdTop(String idOfcProd)throws SystemException, BusinessException;

    List<DataTrdEcmVO> dataTrdByOfcProdListEcm(String idOfcProd, BigInteger idVersion) throws SystemException, BusinessException;
}
