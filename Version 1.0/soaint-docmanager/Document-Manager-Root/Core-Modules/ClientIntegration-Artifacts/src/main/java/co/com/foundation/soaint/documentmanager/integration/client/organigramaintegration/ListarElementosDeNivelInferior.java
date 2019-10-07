
package co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para listarElementosDeNivelInferior complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="listarElementosDeNivelInferior">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id_padre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listarElementosDeNivelInferior", propOrder = {
    "idPadre"
})
public class ListarElementosDeNivelInferior {

    @XmlElement(name = "id_padre")
    protected String idPadre;

    /**
     * Obtiene el valor de la propiedad idPadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPadre() {
        return idPadre;
    }

    /**
     * Define el valor de la propiedad idPadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPadre(String value) {
        this.idPadre = value;
    }

}
