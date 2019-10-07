
package co.com.foundation.soaint.documentmanager.integration.client.ecmintegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para organigramaDTO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="organigramaDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://co.com.soaint.ecm.integration.service.ws}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="abreviatura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codOrg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ideOrgaAdmin" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="nomOrg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="radicadora" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "organigramaDTO", propOrder = {
    "abreviatura",
    "codOrg",
    "ideOrgaAdmin",
    "nomOrg",
    "radicadora",
    "tipo"
})
public class OrganigramaDTO
    extends BaseDTO
{

    protected String abreviatura;
    protected String codOrg;
    protected Long ideOrgaAdmin;
    protected String nomOrg;
    protected boolean radicadora;
    protected String tipo;

    /**
     * Obtiene el valor de la propiedad abreviatura.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbreviatura() {
        return abreviatura;
    }

    /**
     * Define el valor de la propiedad abreviatura.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbreviatura(String value) {
        this.abreviatura = value;
    }

    /**
     * Obtiene el valor de la propiedad codOrg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodOrg() {
        return codOrg;
    }

    /**
     * Define el valor de la propiedad codOrg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodOrg(String value) {
        this.codOrg = value;
    }

    /**
     * Obtiene el valor de la propiedad ideOrgaAdmin.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdeOrgaAdmin() {
        return ideOrgaAdmin;
    }

    /**
     * Define el valor de la propiedad ideOrgaAdmin.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdeOrgaAdmin(Long value) {
        this.ideOrgaAdmin = value;
    }

    /**
     * Obtiene el valor de la propiedad nomOrg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomOrg() {
        return nomOrg;
    }

    /**
     * Define el valor de la propiedad nomOrg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomOrg(String value) {
        this.nomOrg = value;
    }

    /**
     * Obtiene el valor de la propiedad radicadora.
     * 
     */
    public boolean isRadicadora() {
        return radicadora;
    }

    /**
     * Define el valor de la propiedad radicadora.
     * 
     */
    public void setRadicadora(boolean value) {
        this.radicadora = value;
    }

    /**
     * Obtiene el valor de la propiedad tipo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Define el valor de la propiedad tipo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

}
