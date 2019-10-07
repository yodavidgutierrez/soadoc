
package co.com.foundation.soaint.documentmanager.integration.client.ecmintegration;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para contenidoDependenciaTrdDTO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="contenidoDependenciaTrdDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codSubSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="diposicionFinal" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="grupoSeguridad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idOrgAdm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idOrgOfc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="listaDependencia" type="{http://co.com.soaint.ecm.integration.service.ws}organigramaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listaSede" type="{http://co.com.soaint.ecm.integration.service.ws}sedeDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listaSerie" type="{http://co.com.soaint.ecm.integration.service.ws}serieDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listaSubSerie" type="{http://co.com.soaint.ecm.integration.service.ws}subSerieDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="nomSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nomSubSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="procedimiento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="radicadora" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
@XmlType(name = "contenidoDependenciaTrdDTO", propOrder = {
    "codSede",
    "codSerie",
    "codSubSerie",
    "diposicionFinal",
    "grupoSeguridad",
    "idOrgAdm",
    "idOrgOfc",
    "listaDependencia",
    "listaSede",
    "listaSerie",
    "listaSubSerie",
    "nomSerie",
    "nomSubSerie",
    "procedimiento",
    "radicadora",
    "retArchivoCentral",
    "retArchivoGestion"
})
public class ContenidoDependenciaTrdDTO {

    protected String codSede;
    protected String codSerie;
    protected String codSubSerie;
    protected int diposicionFinal;
    protected String grupoSeguridad;
    protected String idOrgAdm;
    protected String idOrgOfc;
    @XmlElement(nillable = true)
    protected List<OrganigramaDTO> listaDependencia;
    @XmlElement(nillable = true)
    protected List<SedeDTO> listaSede;
    @XmlElement(nillable = true)
    protected List<SerieDTO> listaSerie;
    @XmlElement(nillable = true)
    protected List<SubSerieDTO> listaSubSerie;
    protected String nomSerie;
    protected String nomSubSerie;
    protected String procedimiento;
    protected boolean radicadora;
    protected Long retArchivoCentral;
    protected Long retArchivoGestion;

    /**
     * Obtiene el valor de la propiedad codSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSede() {
        return codSede;
    }

    /**
     * Define el valor de la propiedad codSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSede(String value) {
        this.codSede = value;
    }

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
     * Obtiene el valor de la propiedad grupoSeguridad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrupoSeguridad() {
        return grupoSeguridad;
    }

    /**
     * Define el valor de la propiedad grupoSeguridad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrupoSeguridad(String value) {
        this.grupoSeguridad = value;
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
     * Gets the value of the listaDependencia property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaDependencia property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaDependencia().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrganigramaDTO }
     * 
     * 
     */
    public List<OrganigramaDTO> getListaDependencia() {
        if (listaDependencia == null) {
            listaDependencia = new ArrayList<OrganigramaDTO>();
        }
        return this.listaDependencia;
    }

    /**
     * Gets the value of the listaSede property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaSede property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaSede().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SedeDTO }
     * 
     * 
     */
    public List<SedeDTO> getListaSede() {
        if (listaSede == null) {
            listaSede = new ArrayList<SedeDTO>();
        }
        return this.listaSede;
    }

    /**
     * Gets the value of the listaSerie property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaSerie property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaSerie().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SerieDTO }
     * 
     * 
     */
    public List<SerieDTO> getListaSerie() {
        if (listaSerie == null) {
            listaSerie = new ArrayList<SerieDTO>();
        }
        return this.listaSerie;
    }

    /**
     * Gets the value of the listaSubSerie property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaSubSerie property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaSubSerie().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubSerieDTO }
     * 
     * 
     */
    public List<SubSerieDTO> getListaSubSerie() {
        if (listaSubSerie == null) {
            listaSubSerie = new ArrayList<SubSerieDTO>();
        }
        return this.listaSubSerie;
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
