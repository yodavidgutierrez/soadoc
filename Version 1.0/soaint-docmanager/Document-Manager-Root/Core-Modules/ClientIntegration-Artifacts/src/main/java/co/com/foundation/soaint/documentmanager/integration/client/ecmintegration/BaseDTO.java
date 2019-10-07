
package co.com.foundation.soaint.documentmanager.integration.client.ecmintegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para baseDTO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="baseDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoBase" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreBase" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "baseDTO", propOrder = {
    "codigoBase",
    "nombreBase"
})
@XmlSeeAlso({
    SedeDTO.class,
    SubSerieDTO.class,
    SerieDTO.class,
    OrganigramaDTO.class
})
public class BaseDTO {

    protected String codigoBase;
    protected String nombreBase;

    /**
     * Obtiene el valor de la propiedad codigoBase.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoBase() {
        return codigoBase;
    }

    /**
     * Define el valor de la propiedad codigoBase.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoBase(String value) {
        this.codigoBase = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreBase.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreBase() {
        return nombreBase;
    }

    /**
     * Define el valor de la propiedad nombreBase.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreBase(String value) {
        this.nombreBase = value;
    }

}
