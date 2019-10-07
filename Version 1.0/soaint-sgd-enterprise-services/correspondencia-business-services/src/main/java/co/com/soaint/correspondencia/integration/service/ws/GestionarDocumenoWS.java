package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarDocumento;
import co.com.soaint.foundation.canonical.correspondencia.DocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.ObservacionesDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.math.BigInteger;

/**
 * Created by esanchez on 8/1/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarDocumenoWS {

    @Autowired
    GestionarDocumento boundary;

    /**
     * Constructor
     */
    public GestionarDocumenoWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param documentoDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "actualizarReferenciaECM", operationName = "actualizarReferenciaECM")
    public void actualizarReferenciaECM(@WebParam(name = "documento") final DocumentoDTO documentoDTO) throws BusinessException, SystemException {
        boundary.actualizarReferenciaECM(documentoDTO);
    }

    /**
     * @param ppdTrazDocumentoDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "registrarObservacion", operationName = "registrarObservacion")
    public void registrarObservacion(PpdTrazDocumentoDTO ppdTrazDocumentoDTO) throws BusinessException, SystemException {
        boundary.generarTrazaDocumento(ppdTrazDocumentoDTO);
    }

    /**
     * @param ideDocumento
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "listarObservacionesDocumento", operationName = "listarObservacionesDocumento")
    public ObservacionesDocumentoDTO listarObservacionesDocumento(@WebParam(name = "ide-documento") final BigInteger ideDocumento) throws BusinessException, SystemException {
        return boundary.listarObservacionesDocumento(ideDocumento);
    }

    /**
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "consultarDocumentoByNroRadicado", operationName = "consultarDocumentoByNroRadicado")
    public PpdDocumentoDTO consultarDocumentoByNroRadicado(@WebParam(name = "nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        return boundary.consultarDocumentoByNroRadicado(nroRadicado);
    }
}
