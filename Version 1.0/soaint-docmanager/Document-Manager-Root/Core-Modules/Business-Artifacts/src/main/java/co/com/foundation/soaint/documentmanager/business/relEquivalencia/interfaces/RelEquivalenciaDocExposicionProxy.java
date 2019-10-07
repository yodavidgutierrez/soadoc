package co.com.foundation.soaint.documentmanager.business.relEquivalencia.interfaces;

import co.com.foundation.soaint.documentmanager.domain.RelEquiItemVO;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;

import java.util.List;

/**
 * Created by ADMIN on 12/12/2016.
 */
public interface RelEquivalenciaDocExposicionProxy {
    List<RelEquiItemVO> listUniAdminOrigen(String numVersionOrg) throws BusinessException, SystemException;
    List<RelEquiItemVO> listOfiProdByCodUniAdmOrigen(String codUniAdmin) throws BusinessException, SystemException;
    List<RelEquiItemVO> listSeriesInOrigenByCodUniAdmAndCodOfPro(String codUniAdmin, String codOfiProd) throws BusinessException, SystemException;
    List<RelEquiItemVO> listSubSeriesInOrigenByCodUniAdmAndCodOfProAndIdSer(String codUniAdmin, String codOfiProd, String codSerieOr) throws BusinessException, SystemException;
    List<RelEquiItemVO> listUniAdminDestino(String codUniAdminOr, String codOfiProdOr, String idSerieOr, String idSubSerieOr) throws BusinessException, SystemException;
    List<RelEquiItemVO> listOfiProdByCodUniAdminDestino(String codUniAdminDe, String codOfiProdOr, String idSerieOr, String idSubSerieOr, String codOfiProdDes) throws BusinessException, SystemException;
    List<RelEquiItemVO> listSeriesInDestinoByCodUniAdmAndCodOfPro(String codUniAdminDe, String codOfiProdOr, String idSerieOr, String idSubSerieOr, String codOfiProdDe) throws BusinessException, SystemException;
    List<RelEquiItemVO> listSubSeriesInDestinoByCodUniAdmAndCodOfProAndIdSer(String codUniAdminDe, String codOfiProdDe, String idSerieDe, String codSubserie, String codSerieDe) throws BusinessException, SystemException;

}
