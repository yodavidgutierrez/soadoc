package co.com.soaint.ecm.integration.service.ws;

import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.SystemException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.Serializable;
import java.util.List;

@WebService(targetNamespace = "http://co.com.soaint.ecm.integration.service.ws")
public interface GenerarEstructuraWS extends Serializable {

    @WebMethod(operationName = "createStructureECM", action = "createStructureECM")
    MensajeRespuesta crearEstructuraEcm(@WebParam(name = "estructura") List<EstructuraTrdDTO> structure) throws SystemException;
}
