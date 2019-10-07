
package co.com.foundation.soaint.documentmanager.integration.client.ecm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para contenidoDependenciaTrdVO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="contenidoDependenciaTrdVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codSubSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="diposicionFinal" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idOrgAdm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idOrgOfc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nomSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nomSubSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="procedimiento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="retArchivoCentral" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="retArchivoGestion" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contenidoDependenciaTrdVO", propOrder = {
    "codSerie",
    "codSubSerie",
    "diposicionFinal",
    "idOrgAdm",
    "idOrgOfc",
    "nomSerie",
    "nomSubSerie",
    "procedimiento",
    "retArchivoCentral",
    "retArchivoGestion"
})
public class ContenidoDependenciaTrdVO {

    protected String codSerie;
    protected String codSubSerie;
    protected int diposicionFinal;
    protected String idOrgAdm;
    protected String idOrgOfc;
    protected String nomSerie;
    protected String nomSubSerie;
    protected String procedimiento;
    protected Long retArchivoCentral;
    protected Long retArchivoGestion;

    /**
     * Obtiene el valor de la propiedad codSerie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSerie() {
        return codSerie;
    }

    /**
     * Define el valor de la propiedad codSerie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSerie(String value) {
        this.codSerie = value;
    }

    /**
     * Obtiene el valor de la propiedad codSubSerie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSubSerie() {
        return codSubSerie;
    }

    /**
     * Define el valor de la propiedad codSubSerie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSubSerie(String value) {
        this.codSubSerie = value;
    }

    /**
     * Obtiene el valor de la propiedad diposicionFinal.
     * 
     */
    public int getDiposicionFinal() {
        return diposicionFinal;
    }

    /**
     * Define el valor de la propiedad diposicionFinal.
     * 
     */
    public void setDiposicionFinal(int value) {
        this.diposicionFinal = value;
    }

    /**
     * Obtiene el valor de la propiedad idOrgAdm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOrgAdm() {
        return idOrgAdm;
    }

    /**
     * Define el valor de la propiedad idOrgAdm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOrgAdm(String value) {
        this.idOrgAdm = value;
    }

    /**
     * Obtiene el valor de la propiedad idOrgOfc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOrgOfc() {
        return idOrgOfc;
    }

    /**
     * Define el valor de la propiedad idOrgOfc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOrgOfc(String value) {
        this.idOrgOfc = value;
    }

    /**
     * Obtiene el valor de la propiedad nomSerie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomSerie() {
        return nomSerie;
    }

    /**
     * Define el valor de la propiedad nomSerie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomSerie(String value) {
        this.nomSerie = value;
    }

    /**
     * Obtiene el valor de la propiedad nomSubSerie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomSubSerie() {
        return nomSubSerie;
    }

    /**
     * Define el valor de la propiedad nomSubSerie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomSubSerie(String value) {
        this.nomSubSerie = value;
    }

    /**
     * Obtiene el valor de la propiedad procedimiento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcedimiento() {
        return procedimiento;
    }

    /**
     * Define el valor de la propiedad procedimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcedimiento(String value) {
        this.procedimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad retArchivoCentral.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRetArchivoCentral() {
        return retArchivoCentral;
    }

    /**
     * Define el valor de la propiedad retArchivoCentral.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRetArchivoCentral(Long value) {
        this.retArchivoCentral = value;
    }

    /**
     * Obtiene el valor de la propiedad retArchivoGestion.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRetArchivoGestion() {
        return retArchivoGestion;
    }

    /**
     * Define el valor de la propiedad retArchivoGestion.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRetArchivoGestion(Long value) {
        this.retArchivoGestion = value;
    }

}
