package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarAgente;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.DevolucionDTO;
import co.com.soaint.foundation.canonical.correspondencia.RedireccionDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by esanchez on 7/14/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarAgenteWS {

    @Autowired
    GestionarAgente boundary;

    /**
     * Constructor
     */
    public GestionarAgenteWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param agenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "actualizarEstadoAgente", operationName = "actualizarEstadoAgente")
    public void actualizarEstadoAgente(@WebParam(name = "agente") AgenteDTO agenteDTO) throws BusinessException, SystemException {
        boundary.actualizarEstadoAgente(agenteDTO);
    }

    /**
     * @param redireccion
     * @throws SystemException
     */
    @WebMethod(action = "redireccionarCorrespondencia", operationName = "redireccionarCorrespondencia")
    public void redireccionarCorrespondencia(@WebParam(name = "redireccion") final RedireccionDTO redireccion) throws SystemException {
        boundary.redireccionarCorrespondencia(redireccion);
    }

    /**
     * @param devolucion
     * @throws SystemException
     */
    @WebMethod(action = "devolverCorrespondencia", operationName = "devolverCorrespondencia")
    public void devolverCorrespondencia(@WebParam(name = "devolucion") final DevolucionDTO devolucion) throws SystemException {
        boundary.devolverCorrespondencia(devolucion);
    }
}
