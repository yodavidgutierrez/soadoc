package co.com.foundation.soaint.documentmanager.business.cuadroclasificaciondoc.interfaces;


import co.com.foundation.soaint.documentmanager.domain.CCDForVersionVO;
import co.com.foundation.soaint.documentmanager.domain.CCDVersionesVO;
import co.com.foundation.soaint.documentmanager.domain.DataReportCcdVO;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;

import java.util.List;

/**
 * Created by ADMIN on 30/11/2016.
 */
public interface VersionCuadroClasificacionDocManagerProxy {

    DataReportCcdVO versionCcdByOfcProdList(String valVersion) throws SystemException, BusinessException;
}
