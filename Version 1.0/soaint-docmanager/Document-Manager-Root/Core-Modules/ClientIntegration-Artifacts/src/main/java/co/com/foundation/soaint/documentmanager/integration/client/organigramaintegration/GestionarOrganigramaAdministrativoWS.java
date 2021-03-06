
package co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "GestionarOrganigramaAdministrativoWS", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface GestionarOrganigramaAdministrativoWS {


    /**
     * 
     * @return
     *     returns co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.OrganigramaAdministrativoDTO
     * @throws SystemException_Exception
     * @throws BusinessException_Exception
     */
    @WebMethod(action = "consultarOrganigrama")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "consultarOrganigrama", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service", className = "co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.ConsultarOrganigrama")
    @ResponseWrapper(localName = "consultarOrganigramaResponse", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service", className = "co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.ConsultarOrganigramaResponse")
    @Action(input = "consultarOrganigrama", output = "http://co.com.soaint.sgd.correspondencia.service/GestionarOrganigramaAdministrativoWS/consultarOrganigramaResponse", fault = {
        @FaultAction(className = BusinessException_Exception.class, value = "http://co.com.soaint.sgd.correspondencia.service/GestionarOrganigramaAdministrativoWS/consultarOrganigrama/Fault/BusinessException"),
        @FaultAction(className = SystemException_Exception.class, value = "http://co.com.soaint.sgd.correspondencia.service/GestionarOrganigramaAdministrativoWS/consultarOrganigrama/Fault/SystemException")
    })
    public OrganigramaAdministrativoDTO consultarOrganigrama()
        throws BusinessException_Exception, SystemException_Exception
    ;

    /**
     * 
     * @param idHijo
     * @return
     *     returns co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.OrganigramaItemDTO
     * @throws SystemException_Exception
     * @throws BusinessException_Exception
     */
    @WebMethod(action = "consultarPadreDeSegundoNivel")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "consultarPadreDeSegundoNivel", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service", className = "co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.ConsultarPadreDeSegundoNivel")
    @ResponseWrapper(localName = "consultarPadreDeSegundoNivelResponse", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service", className = "co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.ConsultarPadreDeSegundoNivelResponse")
    @Action(input = "consultarPadreDeSegundoNivel", output = "http://co.com.soaint.sgd.correspondencia.service/GestionarOrganigramaAdministrativoWS/consultarPadreDeSegundoNivelResponse", fault = {
        @FaultAction(className = BusinessException_Exception.class, value = "http://co.com.soaint.sgd.correspondencia.service/GestionarOrganigramaAdministrativoWS/consultarPadreDeSegundoNivel/Fault/BusinessException"),
        @FaultAction(className = SystemException_Exception.class, value = "http://co.com.soaint.sgd.correspondencia.service/GestionarOrganigramaAdministrativoWS/consultarPadreDeSegundoNivel/Fault/SystemException")
    })
    public OrganigramaItemDTO consultarPadreDeSegundoNivel(
        @WebParam(name = "id_hijo", targetNamespace = "")
        String idHijo)
        throws BusinessException_Exception, SystemException_Exception
    ;

    /**
     * 
     * @param organigramaItem
     * @return
     *     returns boolean
     */
    @WebMethod(action = "crearOrganigrama")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "crearOrganigrama", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service", className = "co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.CrearOrganigrama")
    @ResponseWrapper(localName = "crearOrganigramaResponse", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service", className = "co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.CrearOrganigramaResponse")
    @Action(input = "crearOrganigrama", output = "http://co.com.soaint.sgd.correspondencia.service/GestionarOrganigramaAdministrativoWS/crearOrganigramaResponse")
    public boolean crearOrganigrama(
        @WebParam(name = "organigramaItem", targetNamespace = "")
        OrganigramaAdministrativoDTO organigramaItem);

    /**
     * 
     * @param organigramaItem
     * @return
     *     returns boolean
     */
    @WebMethod(action = "generarEstructura")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "generarEstructura", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service", className = "co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.GenerarEstructura")
    @ResponseWrapper(localName = "generarEstructuraResponse", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service", className = "co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.GenerarEstructuraResponse")
    @Action(input = "generarEstructura", output = "http://co.com.soaint.sgd.correspondencia.service/GestionarOrganigramaAdministrativoWS/generarEstructuraResponse")
    public boolean generarEstructura(
        @WebParam(name = "organigramaItem", targetNamespace = "")
        List<EstructuraTrdDTO> organigramaItem);

    /**
     * 
     * @param idPadre
     * @return
     *     returns co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.OrganigramaAdministrativoDTO
     * @throws SystemException_Exception
     */
    @WebMethod(action = "listarElementosDeNivelInferior")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "listarElementosDeNivelInferior", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service", className = "co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.ListarElementosDeNivelInferior")
    @ResponseWrapper(localName = "listarElementosDeNivelInferiorResponse", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service", className = "co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.ListarElementosDeNivelInferiorResponse")
    @Action(input = "listarElementosDeNivelInferior", output = "http://co.com.soaint.sgd.correspondencia.service/GestionarOrganigramaAdministrativoWS/listarElementosDeNivelInferiorResponse", fault = {
        @FaultAction(className = SystemException_Exception.class, value = "http://co.com.soaint.sgd.correspondencia.service/GestionarOrganigramaAdministrativoWS/listarElementosDeNivelInferior/Fault/SystemException")
    })
    public OrganigramaAdministrativoDTO listarElementosDeNivelInferior(
        @WebParam(name = "id_padre", targetNamespace = "")
        String idPadre)
        throws SystemException_Exception
    ;

    /**
     * 
     * @return
     *     returns co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.OrganigramaAdministrativoDTO
     * @throws SystemException_Exception
     */
    @WebMethod(action = "listarDescendientesPadres")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "listarDescendientesPadres", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service", className = "co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.ListarDescendientesPadres")
    @ResponseWrapper(localName = "listarDescendientesPadresResponse", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service", className = "co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.ListarDescendientesPadresResponse")
    @Action(input = "listarDescendientesPadres", output = "http://co.com.soaint.sgd.correspondencia.service/GestionarOrganigramaAdministrativoWS/listarDescendientesPadresResponse", fault = {
        @FaultAction(className = SystemException_Exception.class, value = "http://co.com.soaint.sgd.correspondencia.service/GestionarOrganigramaAdministrativoWS/listarDescendientesPadres/Fault/SystemException")
    })
    public OrganigramaAdministrativoDTO listarDescendientesPadres()
        throws SystemException_Exception
    ;

}
