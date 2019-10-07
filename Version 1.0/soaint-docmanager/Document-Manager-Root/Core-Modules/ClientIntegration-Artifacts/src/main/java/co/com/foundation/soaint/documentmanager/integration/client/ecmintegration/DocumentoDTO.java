
package co.com.foundation.soaint.documentmanager.integration.client.ecmintegration;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para documentoDTO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="documentoDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoDependencia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dependencia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="documento" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="fechaCreacion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaRadicacion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="idDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idDocumentoPadre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idUnidadDocumental" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="labelRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="nombreDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreProceso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreRemitente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreUnidadDocumental" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nroRadicado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nroRadicadoReferido" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tamano" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoPadreAdjunto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipologiaDocumental" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="versionLabel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documentoDTO", propOrder = {
    "codigoDependencia",
    "codigoSede",
    "dependencia",
    "documento",
    "fechaCreacion",
    "fechaRadicacion",
    "idDocumento",
    "idDocumentoPadre",
    "idUnidadDocumental",
    "labelRequired",
    "nombreDocumento",
    "nombreProceso",
    "nombreRemitente",
    "nombreUnidadDocumental",
    "nroRadicado",
    "nroRadicadoReferido",
    "sede",
    "serie",
    "subSerie",
    "tamano",
    "tipoDocumento",
    "tipoPadreAdjunto",
    "tipologiaDocumental",
    "versionLabel"
})
public class DocumentoDTO {

    protected String codigoDependencia;
    protected String codigoSede;
    protected String dependencia;
    protected byte[] documento;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaCreacion;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaRadicacion;
    protected String idDocumento;
    protected String idDocumentoPadre;
    protected String idUnidadDocumental;
    protected Boolean labelRequired;
    protected String nombreDocumento;
    protected String nombreProceso;
    protected String nombreRemitente;
    protected String nombreUnidadDocumental;
    protected String nroRadicado;
    @XmlElement(nillable = true)
    protected List<String> nroRadicadoReferido;
    protected String sede;
    protected String serie;
    protected String subSerie;
    protected String tamano;
    protected String tipoDocumento;
    protected String tipoPadreAdjunto;
    protected String tipologiaDocumental;
    protected String versionLabel;

    /**
     * Obtiene el valor de la propiedad codigoDependencia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoDependencia() {
        return codigoDependencia;
    }

    /**
     * Define el valor de la propiedad codigoDependencia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoDependencia(String value) {
        this.codigoDependencia = value;
    }

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
     * Obtiene el valor de la propiedad dependencia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDependencia() {
        return dependencia;
    }

    /**
     * Define el valor de la propiedad dependencia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDependencia(String value) {
        this.dependencia = value;
    }

    /**
     * Obtiene el valor de la propiedad documento.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDocumento() {
        return documento;
    }

    /**
     * Define el valor de la propiedad documento.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDocumento(byte[] value) {
        this.documento = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaCreacion.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Define el valor de la propiedad fechaCreacion.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaCreacion(XMLGregorianCalendar value) {
        this.fechaCreacion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaRadicacion.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaRadicacion() {
        return fechaRadicacion;
    }

    /**
     * Define el valor de la propiedad fechaRadicacion.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaRadicacion(XMLGregorianCalendar value) {
        this.fechaRadicacion = value;
    }

    /**
     * Obtiene el valor de la propiedad idDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocumento() {
        return idDocumento;
    }

    /**
     * Define el valor de la propiedad idDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocumento(String value) {
        this.idDocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad idDocumentoPadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocumentoPadre() {
        return idDocumentoPadre;
    }

    /**
     * Define el valor de la propiedad idDocumentoPadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocumentoPadre(String value) {
        this.idDocumentoPadre = value;
    }

    /**
     * Obtiene el valor de la propiedad idUnidadDocumental.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdUnidadDocumental() {
        return idUnidadDocumental;
    }

    /**
     * Define el valor de la propiedad idUnidadDocumental.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdUnidadDocumental(String value) {
        this.idUnidadDocumental = value;
    }

    /**
     * Obtiene el valor de la propiedad labelRequired.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLabelRequired() {
        return labelRequired;
    }

    /**
     * Define el valor de la propiedad labelRequired.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLabelRequired(Boolean value) {
        this.labelRequired = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreDocumento() {
        return nombreDocumento;
    }

    /**
     * Define el valor de la propiedad nombreDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreDocumento(String value) {
        this.nombreDocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreProceso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreProceso() {
        return nombreProceso;
    }

    /**
     * Define el valor de la propiedad nombreProceso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreProceso(String value) {
        this.nombreProceso = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreRemitente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreRemitente() {
        return nombreRemitente;
    }

    /**
     * Define el valor de la propiedad nombreRemitente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreRemitente(String value) {
        this.nombreRemitente = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreUnidadDocumental.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreUnidadDocumental() {
        return nombreUnidadDocumental;
    }

    /**
     * Define el valor de la propiedad nombreUnidadDocumental.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreUnidadDocumental(String value) {
        this.nombreUnidadDocumental = value;
    }

    /**
     * Obtiene el valor de la propiedad nroRadicado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNroRadicado() {
        return nroRadicado;
    }

    /**
     * Define el valor de la propiedad nroRadicado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNroRadicado(String value) {
        this.nroRadicado = value;
    }

    /**
     * Gets the value of the nroRadicadoReferido property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nroRadicadoReferido property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNroRadicadoReferido().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNroRadicadoReferido() {
        if (nroRadicadoReferido == null) {
            nroRadicadoReferido = new ArrayList<String>();
        }
        return this.nroRadicadoReferido;
    }

    /**
     * Obtiene el valor de la propiedad sede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSede() {
        return sede;
    }

    /**
     * Define el valor de la propiedad sede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSede(String value) {
        this.sede = value;
    }

    /**
     * Obtiene el valor de la propiedad serie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerie() {
        return serie;
    }

    /**
     * Define el valor de la propiedad serie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerie(String value) {
        this.serie = value;
    }

    /**
     * Obtiene el valor de la propiedad subSerie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubSerie() {
        return subSerie;
    }

    /**
     * Define el valor de la propiedad subSerie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubSerie(String value) {
        this.subSerie = value;
    }

    /**
     * Obtiene el valor de la propiedad tamano.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTamano() {
        return tamano;
    }

    /**
     * Define el valor de la propiedad tamano.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTamano(String value) {
        this.tamano = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Define el valor de la propiedad tipoDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumento(String value) {
        this.tipoDocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoPadreAdjunto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoPadreAdjunto() {
        return tipoPadreAdjunto;
    }

    /**
     * Define el valor de la propiedad tipoPadreAdjunto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoPadreAdjunto(String value) {
        this.tipoPadreAdjunto = value;
    }

    /**
     * Obtiene el valor de la propiedad tipologiaDocumental.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipologiaDocumental() {
        return tipologiaDocumental;
    }

    /**
     * Define el valor de la propiedad tipologiaDocumental.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipologiaDocumental(String value) {
        this.tipologiaDocumental = value;
    }

    /**
     * Obtiene el valor de la propiedad versionLabel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionLabel() {
        return versionLabel;
    }

    /**
     * Define el valor de la propiedad versionLabel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionLabel(String value) {
        this.versionLabel = value;
    }

}
