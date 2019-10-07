package co.com.foundation.soaint.documentmanager.business.cuadroclasificaciondoc.interfaces;

import co.com.foundation.soaint.documentmanager.domain.CcdItemVO;
import co.com.foundation.soaint.documentmanager.domain.DataCcdVO;
import co.com.foundation.soaint.documentmanager.domain.DataTabRetDispVO;
import co.com.foundation.soaint.documentmanager.domain.RelEquiItemVO;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSerie;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmTabRetDoc;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmCcd;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmConfigCcd;

import java.math.BigInteger;

import java.util.Date;
import java.util.List;

/**
 * Created by jrodriguez on 20/10/2016.
 */
public interface CuadroClasificacionDocManagerProxy {

    //Ccd Config
    List<AdmConfigCcd> findAllCcdConfig() throws BusinessException, SystemException;

    void createCuadroClasiDocConfig(AdmConfigCcd ccd) throws SystemException, BusinessException;

    void updateCuadroClasiDocConfig(Long ideCcd, Integer estCcd) throws SystemException, BusinessException;

    void createCuadroClasiDoc(AdmCcd ccd) throws SystemException, BusinessException;

    void publicarCcd(String nombreComite, String numActa, Date fechaActa) throws SystemException,BusinessException;

    boolean existByTabRedDoc(BigInteger ideSerie, BigInteger ideSubserie, String ideOfcProd, String ideUniAmt) throws SystemException, BusinessException;

    boolean existCcd(BigInteger ideSerie, BigInteger ideSubserie, String ideOfcProd, String ideUniAmt) throws SystemException, BusinessException;

    List<AdmCcd> listarVersionCcd() throws SystemException,BusinessException;

    List<RelEquiItemVO> listarVersionCcdNum() throws SystemException,BusinessException;

    List<AdmCcd> findAllAdmCcdByValVersion(DataCcdVO data) throws BusinessException, SystemException;

    AdmConfigCcd searchByUniAdminAndOfcProdAndIdSerieAndSubSerie(BigInteger ideSerie, BigInteger ideSubserie, String ideOfcProd, String ideUniAmt) throws SystemException, BusinessException;

    List<CcdItemVO> listarUniAdminExistInCcd(String version)throws BusinessException, SystemException;

    List<CcdItemVO> listarUniAdminExistInCcdDes()throws BusinessException, SystemException;

    List<CcdItemVO> listarOfiProductoraExistInCcdDe(String ideOrgaAdmin) throws BusinessException, SystemException;

    List<CcdItemVO> listarOfiProductoraExistInCcd(String ideOrgaAdmin, String version) throws BusinessException, SystemException;

    List<AdmCcd>  listarSeriesExistInCcd(String ideUniAmt, String ideOfcProd, String version)throws BusinessException, SystemException;

    List<AdmCcd>  listarSeriesExistInCcdDe(String ideUniAmt, String ideOfcProd)throws BusinessException, SystemException;

    List<AdmCcd>  listarSubSeriesExistInCcdDe(String ideUniAmt, String ideOfcProd, BigInteger idSerie)throws BusinessException, SystemException;

    List<AdmCcd>  listarSubSeriesExistInCcd(String ideUniAmt, String ideOfcProd, BigInteger idSerie,String version)throws BusinessException, SystemException;

    BigInteger getNumVersionOrgByValVersion(String valVersion) throws BusinessException, SystemException;

}
