package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarOrganigramaAdministrativo;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaAdministrativoDTO;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by esanchez on 6/22/2017.
 */
@Service("gestionarOrganigramaAdministrativoWS")
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service",
        serviceName = "GestionarOrganigramaAdministrativoWS", name = "GestionarOrganigramaAdministrativoWS")
@Log4j2
public class GestionarOrganigramaAdministrativoWS {

    @Autowired
    GestionarOrganigramaAdministrativo boundary;

    /**
     * Constructor
     */
    public GestionarOrganigramaAdministrativoWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "consultarOrganigrama", operationName = "consultarOrganigrama")
    public OrganigramaAdministrativoDTO consultarOrganigrama() throws BusinessException, SystemException {
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.consultarOrganigrama()).build();
    }

    /**
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarDescendientesPadres", operationName = "listarDescendientesPadres")
    public OrganigramaAdministrativoDTO listarDescendientesPadres() throws SystemException {
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.listarDescendientesPadres()).build();
    }

    /**
     * @param idPadre
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarElementosDeNivelInferior", operationName = "listarElementosDeNivelInferior")
    public OrganigramaAdministrativoDTO listarElementosDeNivelInferior(@WebParam(name = "id_padre") final String idPadre) throws SystemException {
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.listarElementosDeNivelInferior(new BigInteger(idPadre))).build();
    }

    /**
     * @param idHijo
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "consultarPadreDeSegundoNivel", operationName = "consultarPadreDeSegundoNivel")
    public OrganigramaItemDTO consultarPadreDeSegundoNivel(@WebParam(name = "id_hijo") final String idHijo) throws BusinessException, SystemException {
        return boundary.consultarPadreDeSegundoNivel(new BigInteger(idHijo));
    }

    @WebMethod(action = "crearOrganigrama", operationName = "crearOrganigrama")
    public boolean insertarOrganigrama(@WebParam(name = "organigramaItem") OrganigramaAdministrativoDTO organigramaItemDTO) {
        log.info("Creating database structure {}", organigramaItemDTO);
        return true;
        //return boundary.insertarOrganigrama(organigramaItemDTO);
    }

    @WebMethod(action = "generarEstructura", operationName = "generarEstructura")
    public boolean generarEstructuraDatabase(@WebParam(name = "organigramaItem") List<EstructuraTrdDTO> structure) {
        log.info("processing rest request - Generar estructura de organigrama Base de Datos");
        try {
            return boundary.generarEstructuraDatabase(structure);
        } catch (Exception e) {
            log.error("Error servicio creando estructura --> {}", e.getMessage());
            return false;
        }
    }
}
