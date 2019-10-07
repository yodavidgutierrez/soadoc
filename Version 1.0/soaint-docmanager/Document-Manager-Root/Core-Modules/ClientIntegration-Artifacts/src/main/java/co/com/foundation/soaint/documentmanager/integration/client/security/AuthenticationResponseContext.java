
package co.com.foundation.soaint.documentmanager.integration.client.security;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para authenticationResponseContext complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="authenticationResponseContext">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="principalContext" type="{http://www.soaint.com/services/security-cartridge/1.0.0}principalContext" minOccurs="0"/>
 *         &lt;element name="successful" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "authenticationResponseContext", propOrder = {
    "principalContext",
    "successful"
})
public class AuthenticationResponseContext {

    protected PrincipalContext principalContext;
    protected boolean successful;

    /**
     * Obtiene el valor de la propiedad principalContext.
     * 
     * @return
     *     possible object is
     *     {@link PrincipalContext }
     *     
     */
    public PrincipalContext getPrincipalContext() {
        return principalContext;
    }

    /**
     * Define el valor de la propiedad principalContext.
     * 
     * @param value
     *     allowed object is
     *     {@link PrincipalContext }
     *     
     */
    public void setPrincipalContext(PrincipalContext value) {
        this.principalContext = value;
    }

    /**
     * Obtiene el valor de la propiedad successful.
     * 
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Define el valor de la propiedad successful.
     * 
     */
    public void setSuccessful(boolean value) {
        this.successful = value;
    }

}
