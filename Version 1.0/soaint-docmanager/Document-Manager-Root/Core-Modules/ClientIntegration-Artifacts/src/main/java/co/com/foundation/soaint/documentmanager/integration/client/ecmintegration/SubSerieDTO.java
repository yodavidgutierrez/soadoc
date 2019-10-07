
package co.com.foundation.soaint.documentmanager.integration.client.ecmintegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para subSerieDTO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="subSerieDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://co.com.soaint.ecm.integration.service.ws}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoSubSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreSubSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subSerieDTO", propOrder = {
    "codigoSubSerie",
    "nombreSubSerie"
})
public class SubSerieDTO
    extends BaseDTO
{

    protected String codigoSubSerie;
    protected String nombreSubSerie;

    /**
     * Obtiene el valor de la propiedad codigoSubSerie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoSubSerie() {
        return codigoSubSerie;
    }

    /**
     * Define el valor de la propiedad codigoSubSerie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoSubSerie(String value) {
        this.codigoSubSerie = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreSubSerie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreSubSerie() {
        return nombreSubSerie;
    }

    /**
     * Define el valor de la propiedad nombreSubSerie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreSubSerie(String value) {
        this.nombreSubSerie = value;
    }

}
