package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarRadicado;
import co.com.soaint.foundation.canonical.correspondencia.RadicadoDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by esanchez on 9/6/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarRadicadoWS {

    @Autowired
    GestionarRadicado boundary;

    /**
     * Constructor
     */
    public GestionarRadicadoWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


    /**
     * @param nroRadicado
     * @param nroIdentidad
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "listarRadicados", operationName = "listarRadicados")
    public List<RadicadoDTO> listarRadicados(@WebParam(name = "nro-radicado") final String nroRadicado,
                                             @WebParam(name = "nro-identidad") final String nroIdentidad,
                                             @WebParam(name = "noGuia") final String noGuia,
                                             @WebParam(name = "nombre") final String nombre,
                                             @WebParam(name = "anno") final String anno,
                                             @WebParam(name = "tipoDocumento") final String tipoDocumento) throws SystemException {
        return boundary.listarRadicados(nroRadicado, nroIdentidad, noGuia, nombre, anno, tipoDocumento);
    }

}
