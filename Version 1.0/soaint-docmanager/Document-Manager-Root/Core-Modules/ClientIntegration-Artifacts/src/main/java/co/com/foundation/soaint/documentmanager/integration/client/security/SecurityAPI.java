
package co.com.foundation.soaint.documentmanager.integration.client.security;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SecurityAPI", targetNamespace = "http://www.soaint.com/services/security-cartridge/1.0.0")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SecurityAPI {


    /**
     * 
     * @param password
     * @param login
     * @return
     *     returns co.com.foundation.soaint.documentmanager.integration.client.security.AuthenticationResponseContext
     * @throws SystemException_Exception
     */
    @WebMethod(action = "verifyCredentials")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "verifyCredentials", targetNamespace = "http://www.soaint.com/services/security-cartridge/1.0.0", className = "co.com.foundation.soaint.documentmanager.integration.client.security.VerifyCredentials")
    @ResponseWrapper(localName = "verifyCredentialsResponse", targetNamespace = "http://www.soaint.com/services/security-cartridge/1.0.0", className = "co.com.foundation.soaint.documentmanager.integration.client.security.VerifyCredentialsResponse")
    public AuthenticationResponseContext verifyCredentials(
        @WebParam(name = "login", targetNamespace = "")
        String login,
        @WebParam(name = "password", targetNamespace = "")
        String password)
        throws SystemException_Exception
    ;

}