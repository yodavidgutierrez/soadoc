package co.com.foundation.soaint.documentmanager.integration.soap;


import co.com.foundation.soaint.documentmanager.business.configuracion.interfaces.DisposicionFinalManagerProxy;
import co.com.foundation.soaint.documentmanager.business.organigrama.interfaces.OrganigramaManagerProxy;
import co.com.foundation.soaint.documentmanager.domain.EstructuraEcmVo;
import co.com.foundation.soaint.documentmanager.domain.OrganigramaItemVO;
import co.com.foundation.soaint.documentmanager.integration.client.ecm.EstructuraTrdVO;
import co.com.foundation.soaint.documentmanager.integration.domain.DisposicionesFinalesDTO;
import co.com.foundation.soaint.documentmanager.integration.domain.EstructuraEcmDTO;
import co.com.foundation.soaint.documentmanager.integration.domain.OrganigramaItemDTO;
import co.com.foundation.soaint.documentmanager.integration.utilities.Utilities;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmDisFinal;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.infrastructure.transformer.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrodriguez on 27/01/2017.
 */
@WebService(targetNamespace = "http://co.com.foundation.soaint.documentmanager.ws/services")
public class OrganigramaEndpoint {

    @Autowired
    private OrganigramaManagerProxy proxy;

    @Autowired
    private DisposicionFinalManagerProxy proxyDisposicion;


    @Autowired
    @Qualifier("estructuraEcmTransformer")
    private Transformer<EstructuraEcmVo, EstructuraEcmDTO> transformerEcm;

    @Autowired
    @Qualifier("organigramaItemTransformer")
    private Transformer<OrganigramaItemVO, OrganigramaItemDTO> transformer;

    @Autowired
    @Qualifier("disposicionFinalTransformer")
    private Transformer<AdmDisFinal, DisposicionesFinalesDTO> transformerDisp;

    public OrganigramaEndpoint() {
    }

    @WebMethod(operationName = "listUnidadesAdministrativas", action = "listUnidadesAdministrativas")
    public List<OrganigramaItemDTO> listarElementosDeSegundoNivel() throws BusinessException, SystemException {
        List<OrganigramaItemDTO> listOrganigramaDTO = new ArrayList<>();

        proxy.listarElementosDeSegundoNivel().stream().forEach((item -> {
            listOrganigramaDTO.add(transformer.transform(item));
        }));

        return listOrganigramaDTO;
    }

    @WebMethod(operationName = "listOficinasProductorasPorCodUniAdmin", action = "listOficinasProductorasPorCodUniAdmin")
    public List<OrganigramaItemDTO> listarElementosDeNivelInferior(@WebParam(name = "ideOrgaAdmin") Long ideOrgaAdmin) throws BusinessException, SystemException {
        List<OrganigramaItemDTO> listOrganigramaDTO = new ArrayList<>();

        proxy.listarElementosDeNivelInferior(ideOrgaAdmin).stream().forEach((item -> {
            listOrganigramaDTO.add(transformer.transform(item));
        }));

        return listOrganigramaDTO;
    }

    @WebMethod(operationName = "listDisposicionesFinales", action = "listDisposicionesFinales")
    public List<DisposicionesFinalesDTO> findAllDisposicionFinal() throws SystemException, BusinessException {
        List<DisposicionesFinalesDTO> listDisposicionesFinalesDTO = new ArrayList<>();

        proxyDisposicion.findAllDisposicionFinal().stream().forEach((item -> {
            listDisposicionesFinalesDTO.add(transformerDisp.transform(item));
        }));

        return listDisposicionesFinalesDTO;
    }

    @WebMethod(operationName = "estructuraDocumental", action = "estructuraDocumental")
    public List<EstructuraEcmDTO> estructuraDocumental() throws SystemException, BusinessException {
        List<EstructuraEcmDTO> listEcmDTO = new ArrayList<>();
        proxy.obtenerEstructuraDocumental().stream().forEach(item -> {
            listEcmDTO.add(transformerEcm.transform(item));
        });
        /*Ordenamiento*/
        for (EstructuraEcmDTO estructuraEcmDTO : listEcmDTO) {
            Utilities.ordenarListaOrganigrama(estructuraEcmDTO.getOrganigramaItemList());
        }
        return listEcmDTO;
    }


}
