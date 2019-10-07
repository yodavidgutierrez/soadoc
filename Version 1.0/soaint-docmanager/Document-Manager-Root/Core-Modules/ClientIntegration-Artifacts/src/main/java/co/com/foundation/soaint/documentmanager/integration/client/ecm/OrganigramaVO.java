
package co.com.foundation.soaint.documentmanager.integration.client.ecm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para organigramaVO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="organigramaVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codOrg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ideOrgaAdmin" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="nomOrg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "organigramaVO", propOrder = {
    "codOrg",
    "ideOrgaAdmin",
    "nomOrg",
    "tipo"
})
public class OrganigramaVO {

    protected String codOrg;
    protected Long ideOrgaAdmin;
    protected String nomOrg;
    protected String tipo;

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
