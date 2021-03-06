
package co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "SystemException", targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class SystemException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private SystemException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public SystemException_Exception(String message, SystemException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public SystemException_Exception(String message, SystemException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.SystemException
     */
    public SystemException getFaultInfo() {
        return faultInfo;
    }

}
