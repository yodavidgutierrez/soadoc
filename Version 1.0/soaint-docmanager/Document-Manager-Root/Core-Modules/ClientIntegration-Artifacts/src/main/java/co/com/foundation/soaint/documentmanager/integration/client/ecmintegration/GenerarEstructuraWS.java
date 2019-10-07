
package co.com.foundation.soaint.documentmanager.integration.client.ecmintegration;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "GenerarEstructuraWS", targetNamespace = "http://co.com.soaint.ecm.integration.service.ws")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface GenerarEstructuraWS {


    /**
     * 
     * @param estructura
     * @return
     *     returns co.com.foundation.soaint.documentmanager.integration.client.ecmintegration.MensajeRespuesta
     */
    @WebMethod(action = "createStructureECM")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "createStructureECM", targetNamespace = "http://co.com.soaint.ecm.integration.service.ws", className = "co.com.foundation.soaint.documentmanager.integration.client.ecmintegration.CreateStructureECM")
    @ResponseWrapper(localName = "createStructureECMResponse", targetNamespace = "http://co.com.soaint.ecm.integration.service.ws", className = "co.com.foundation.soaint.documentmanager.integration.client.ecmintegration.CreateStructureECMResponse")
    @Action(input = "createStructureECM", output = "http://co.com.soaint.ecm.integration.service.ws/GenerarEstructuraWS/createStructureECMResponse")
    public MensajeRespuesta createStructureECM(
        @WebParam(name = "estructura", targetNamespace = "")
        List<EstructuraTrdDTO> estructura);

}