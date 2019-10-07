
package co.com.foundation.soaint.documentmanager.integration.client.ecmintegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para sedeDTO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="sedeDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://co.com.soaint.ecm.integration.service.ws}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sedeDTO", propOrder = {
    "codigoSede",
    "nombreSede"
})
public class SedeDTO
    extends BaseDTO
{

    protected String codigoSede;
    protected String nombreSede;

    /**
     * Obtiene el valor de la propiedad codigoSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoSede() {
        return codigoSede;
    }

    /**
     * Define el valor de la propiedad codigoSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoSede(String value) {
        this.codigoSede = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreSede() {
        return nombreSede;
    }

    /**
     * Define el valor de la propiedad nombreSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreSede(String value) {
        this.nombreSede = value;
    }

}
