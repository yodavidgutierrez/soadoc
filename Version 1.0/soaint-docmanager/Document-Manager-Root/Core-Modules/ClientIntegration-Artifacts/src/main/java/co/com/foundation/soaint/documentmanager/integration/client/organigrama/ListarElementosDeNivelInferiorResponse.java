
package co.com.foundation.soaint.documentmanager.integration.client.organigrama;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para listarElementosDeNivelInferiorResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="listarElementosDeNivelInferiorResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://co.com.soaint.sgd.correspondencia.service}organigramaAdministrativoDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listarElementosDeNivelInferiorResponse", propOrder = {
    "_return"
})
public class ListarElementosDeNivelInferiorResponse {

    @XmlElement(name = "return")
    protected OrganigramaAdministrativoDTO _return;

    /**
     * Obtiene el valor de la propiedad return.
     * 
     * @return
     *     possible object is
     *     {@link OrganigramaAdministrativoDTO }
     *     
     */
    public OrganigramaAdministrativoDTO getReturn() {
        return _return;
    }

    /**
     * Define el valor de la propiedad return.
     * 
     * @param value
     *     allowed object is
     *     {@link OrganigramaAdministrativoDTO }
     *     
     */
    public void setReturn(OrganigramaAdministrativoDTO value) {
        this._return = value;
    }

}
