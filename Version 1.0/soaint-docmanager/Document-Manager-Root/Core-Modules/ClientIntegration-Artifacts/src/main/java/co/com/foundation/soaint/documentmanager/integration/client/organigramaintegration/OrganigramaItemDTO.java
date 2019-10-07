
package co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para organigramaItemDTO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="organigramaItemDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="abreviatura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codOrg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoOrganigramaPadre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descOrg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idOrgaAdminPadre" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="ideOrgaAdmin" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="nivel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nivelPadre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nomOrg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nomPadre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="radicadora" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="refPadre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "organigramaItemDTO", propOrder = {
    "abreviatura",
    "codOrg",
    "codigoOrganigramaPadre",
    "descOrg",
    "estado",
    "idOrgaAdminPadre",
    "ideOrgaAdmin",
    "nivel",
    "nivelPadre",
    "nomOrg",
    "nomPadre",
    "radicadora",
    "refPadre"
})
public class OrganigramaItemDTO {

    protected String abreviatura;
    protected String codOrg;
    protected String codigoOrganigramaPadre;
    protected String descOrg;
    protected String estado;
    protected BigInteger idOrgaAdminPadre;
    protected BigInteger ideOrgaAdmin;
    protected String nivel;
    protected String nivelPadre;
    protected String nomOrg;
    protected String nomPadre;
    protected boolean radicadora;
    protected String refPadre;

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
     * Obtiene el valor de la propiedad codigoOrganigramaPadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOrganigramaPadre() {
        return codigoOrganigramaPadre;
    }

    /**
     * Define el valor de la propiedad codigoOrganigramaPadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOrganigramaPadre(String value) {
        this.codigoOrganigramaPadre = value;
    }

    /**
     * Obtiene el valor de la propiedad descOrg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescOrg() {
        return descOrg;
    }

    /**
     * Define el valor de la propiedad descOrg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescOrg(String value) {
        this.descOrg = value;
    }

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstado(String value) {
        this.estado = value;
    }

    /**
     * Obtiene el valor de la propiedad idOrgaAdminPadre.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIdOrgaAdminPadre() {
        return idOrgaAdminPadre;
    }

    /**
     * Define el valor de la propiedad idOrgaAdminPadre.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIdOrgaAdminPadre(BigInteger value) {
        this.idOrgaAdminPadre = value;
    }

    /**
     * Obtiene el valor de la propiedad ideOrgaAdmin.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIdeOrgaAdmin() {
        return ideOrgaAdmin;
    }

    /**
     * Define el valor de la propiedad ideOrgaAdmin.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIdeOrgaAdmin(BigInteger value) {
        this.ideOrgaAdmin = value;
    }

    /**
     * Obtiene el valor de la propiedad nivel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNivel() {
        return nivel;
    }

    /**
     * Define el valor de la propiedad nivel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNivel(String value) {
        this.nivel = value;
    }

    /**
     * Obtiene el valor de la propiedad nivelPadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNivelPadre() {
        return nivelPadre;
    }

    /**
     * Define el valor de la propiedad nivelPadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNivelPadre(String value) {
        this.nivelPadre = value;
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
     * Obtiene el valor de la propiedad nomPadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomPadre() {
        return nomPadre;
    }

    /**
     * Define el valor de la propiedad nomPadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomPadre(String value) {
        this.nomPadre = value;
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
     * Obtiene el valor de la propiedad refPadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefPadre() {
        return refPadre;
    }

    /**
     * Define el valor de la propiedad refPadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefPadre(String value) {
        this.refPadre = value;
    }

}
