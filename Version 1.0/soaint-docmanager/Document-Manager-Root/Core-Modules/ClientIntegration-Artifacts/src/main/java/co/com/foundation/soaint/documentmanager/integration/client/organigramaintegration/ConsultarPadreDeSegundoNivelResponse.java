
package co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para consultarPadreDeSegundoNivelResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="consultarPadreDeSegundoNivelResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://co.com.soaint.sgd.correspondencia.service}organigramaItemDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarPadreDeSegundoNivelResponse", propOrder = {
    "_return"
})
public class ConsultarPadreDeSegundoNivelResponse {

    @XmlElement(name = "return")
    protected OrganigramaItemDTO _return;

    /**
     * Obtiene el valor de la propiedad return.
     * 
     * @return
     *     possible object is
     *     {@link OrganigramaItemDTO }
     *     
     */
    public OrganigramaItemDTO getReturn() {
        return _return;
    }

    /**
     * Define el valor de la propiedad return.
     * 
     * @param value
     *     allowed object is
     *     {@link OrganigramaItemDTO }
     *     
     */
    public void setReturn(OrganigramaItemDTO value) {
        this._return = value;
    }

}
