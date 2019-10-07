
package co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para serieDTO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="serieDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://co.com.soaint.sgd.correspondencia.service}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serieDTO", propOrder = {
    "codigoSerie",
    "nombreSerie"
})
public class SerieDTO
    extends BaseDTO
{

    protected String codigoSerie;
    protected String nombreSerie;

    /**
     * Obtiene el valor de la propiedad codigoSerie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoSerie() {
        return codigoSerie;
    }

    /**
     * Define el valor de la propiedad codigoSerie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoSerie(String value) {
        this.codigoSerie = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreSerie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreSerie() {
        return nombreSerie;
    }

    /**
     * Define el valor de la propiedad nombreSerie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreSerie(String value) {
        this.nombreSerie = value;
    }

}
