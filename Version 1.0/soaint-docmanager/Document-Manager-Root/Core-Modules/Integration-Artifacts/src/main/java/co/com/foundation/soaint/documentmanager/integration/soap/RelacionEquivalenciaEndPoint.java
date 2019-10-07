package co.com.foundation.soaint.documentmanager.integration.soap;

import co.com.foundation.soaint.documentmanager.business.cuadroclasificaciondoc.interfaces.CuadroClasificacionDocManagerProxy;
import co.com.foundation.soaint.documentmanager.business.relEquivalencia.interfaces.RelEquivalenciaDocExposicionProxy;
import co.com.foundation.soaint.documentmanager.domain.RelEquiItemVO;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmCcd;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ADMIN on 12/12/2016.
 */
@WebService(targetNamespace = "http://co.com.foundation.soaint.documentmanager.ws/services")
public class RelacionEquivalenciaEndPoint {

    @Autowired
    private RelEquivalenciaDocExposicionProxy proxy;

    @Autowired
    private CuadroClasificacionDocManagerProxy ccd;



    public RelacionEquivalenciaEndPoint() {
    }



    @WebMethod(operationName = "getListUniAdminOrigen",action = "getListUniAdminOrigen")
    public List<RelEquiItemVO> listUniAdminOrigen(@WebParam(name = "numVersionOrg") String numVersionOrg) throws SystemException,BusinessException {
        return proxy.listUniAdminOrigen(numVersionOrg);

    }

    @WebMethod(operationName = "getListOfiProdOrigen",action = "getListOfiProdOrigen")
    public List<RelEquiItemVO> listOfiProdByCodUniAdmOrigen(@WebParam(name = "codUniAdmin") String codUniAdmin) throws BusinessException, SystemException{
        return proxy.listOfiProdByCodUniAdmOrigen(codUniAdmin);
    }

    @WebMethod(operationName = "getListSeriesOrigen",action = "getListSeriesOrigen")
    public List<RelEquiItemVO> listSeriesInOrigenByCodUniAdmAndCodOfPro(@WebParam(name = "codUniAdmin")String codUniAdmin, @WebParam(name = "codOfiProd") String codOfiProd) throws BusinessException, SystemException{
        return proxy.listSeriesInOrigenByCodUniAdmAndCodOfPro(codUniAdmin, codOfiProd);
    }

    @WebMethod(operationName = "getListSubSeriesOrigen",action = "getListSubSeriesOrigen")
    public List<RelEquiItemVO> listSubSeriesInOrigenByCodUniAdmAndCodOfProAndIdSer(@WebParam(name = "codUniAdmin")String codUniAdmin,@WebParam(name = "codOfiProd")String codOfiProd, @WebParam(name = "codSerieOr")String codSerieOr) throws BusinessException, SystemException{
        return proxy.listSubSeriesInOrigenByCodUniAdmAndCodOfProAndIdSer(codUniAdmin, codOfiProd,codSerieOr);
    }

    @WebMethod(operationName = "getListUniAdminDestino",action = "getListUniAdminDestino")
    public List<RelEquiItemVO> listUniAdminDestino(@WebParam(name = "codUniAdminOr")String codUniAdminOr, @WebParam(name = "codOfiProdOr")String codOfiProdOr, @WebParam(name = "codSerieOr")String codSerieOr, @WebParam(name = "codSubSerieOr")String codSubSerieOr) throws BusinessException, SystemException{
        return proxy.listUniAdminDestino(codUniAdminOr, codOfiProdOr, codSerieOr, codSubSerieOr);
    }

    @WebMethod(operationName = "getListOfiProdDestino",action = "getListOfiProdDestino")
    public List<RelEquiItemVO> listOfiProdByCodUniAdminDestino(@WebParam(name = "codUniAdminOr") String codUniAdminOr, @WebParam(name = "codOfiProdOr")String codOfiProdOr, @WebParam(name = "codSerieOr")String codSerieOr, @WebParam(name = "codSubSerieOr")String codSubSerieOr, @WebParam(name = "codUniAdminDe")String codUniAdminDe) throws BusinessException, SystemException{
        return proxy.listOfiProdByCodUniAdminDestino( codUniAdminOr,  codOfiProdOr,  codSerieOr,  codSubSerieOr, codUniAdminDe);
    }

    @WebMethod(operationName = "getListSeriesDestino",action = "getListSeriesDestino")
    public List<RelEquiItemVO> listSeriesInDestinoByCodUniAdmAndCodOfPro(@WebParam(name = "codUniAdmin")String codUniAdminDe,
                                                                         @WebParam(name = "codOfiProdOr")String codOfiProdOr,
                                                                         @WebParam(name = "codSerieOr")String codSerieOr,
                                                                         @WebParam(name = "codSubSerieOr")String codSubSerieOr,
                                                                         @WebParam(name = "codOfiProdDe")String codOfiProdDe) throws BusinessException, SystemException{
        return proxy.listSeriesInDestinoByCodUniAdmAndCodOfPro(codUniAdminDe,  codOfiProdOr,  codSerieOr,  codSubSerieOr, codOfiProdDe);
    }
    @WebMethod(operationName = "getListSubSeriesDestino",action = "getListSubSeriesDestino")
    public List<RelEquiItemVO> listSubSeriesInDestinoByCodUniAdmAndCodOfProAndIdSer(@WebParam(name = "codUniAdminDe")String codUniAdminDe,
                                                                                    @WebParam(name = "codOfiProdOr")String codOfiProdOr,
                                                                                    @WebParam(name = "codSerieOr")String codSerieOr,
                                                                                    @WebParam(name = "codSubserie")String codSubserie,
                                                                                    @WebParam(name = "codSerieDe")String codSerieDe) throws BusinessException, SystemException{
        return proxy.listSubSeriesInDestinoByCodUniAdmAndCodOfProAndIdSer( codUniAdminDe,  codOfiProdOr,  codSerieOr, codSubserie, codSerieDe);
    }

    @WebMethod(operationName = "getListCcd",action = "getListCcd")
    public List<RelEquiItemVO> listVersionCcd() throws BusinessException, SystemException{
        return ccd.listarVersionCcdNum();
    }




}
