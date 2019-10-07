package co.com.soaint.foundation.canonical.ecm;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:11-Ene-2018
 * Author: dasiel
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "nombreDocumento")
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-agente/1.0.0")
@ToString(of = "nombreDocumento", includeFieldNames = false)
public class DocumentoDTO implements Serializable {

    protected static final long serialVersionUID = 1L;

    @FormParam("idDocumento")
    @PartType(MediaType.TEXT_PLAIN)
    protected String idDocumento;
    @FormParam("tipologiaDocumental")
    @PartType(MediaType.TEXT_PLAIN)
    protected String tipologiaDocumental;
    @FormParam("nroRadicado")
    @PartType(MediaType.TEXT_PLAIN)
    protected String nroRadicado;
    protected Date fechaRadicacion;
    @FormParam("destinatario")
    @PartType(MediaType.TEXT_PLAIN)
    protected String destinatario;
    @FormParam("nombreRemitente")
    @PartType(MediaType.TEXT_PLAIN)
    protected String nombreRemitente;
    @FormParam("sede")
    @PartType(MediaType.TEXT_PLAIN)
    protected String sede;
    @FormParam("codigoSede")
    @PartType(MediaType.TEXT_PLAIN)
    protected String codigoSede;
    @FormParam("dependencia")
    @PartType(MediaType.TEXT_PLAIN)
    protected String dependencia;
    @FormParam("codigoDependencia")
    @PartType(MediaType.TEXT_PLAIN)
    protected String codigoDependencia;
    @FormParam("serie")
    @PartType(MediaType.TEXT_PLAIN)
    protected String serie;
    @FormParam("subSerie")
    @PartType(MediaType.TEXT_PLAIN)
    protected String subSerie;
    @FormParam("idUnidadDocumental")
    @PartType(MediaType.TEXT_PLAIN)
    protected String idUnidadDocumental;
    @FormParam("nombreUnidadDocumental")
    @PartType(MediaType.TEXT_PLAIN)
    protected String nombreUnidadDocumental;
    @FormParam("nombreDocumento")
    @PartType(MediaType.TEXT_PLAIN)
    protected String nombreDocumento;
    @FormParam("idDocumentoPadre")
    @PartType(MediaType.TEXT_PLAIN)
    protected String idDocumentoPadre;
    protected Date fechaCreacion;
    @FormParam("tipoDocumento")
    @PartType(MediaType.TEXT_PLAIN)
    protected String tipoDocumento;//application/pdf, text/html
    protected String tamano;
    @FormParam("tipoPadreAdjunto")
    @PartType(MediaType.TEXT_PLAIN)
    protected String tipoPadreAdjunto;//xTipo = principal o anexo
    protected String versionLabel;
    @FormParam("documento")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    protected byte[] documento;
    protected String[] nroRadicadoReferido;
    protected Boolean labelRequired;
    protected String nombreProceso;
    @FormParam("docAutor")
    @PartType(MediaType.TEXT_PLAIN)
    protected String docAutor;
    @FormParam("estado")
    @PartType(MediaType.TEXT_PLAIN)
    protected String estado;
    protected boolean anulado;
    @FormParam("reintentar")
    @PartType(MediaType.TEXT_PLAIN)
    protected String reintentar;

    @FormParam("tramite")
    @PartType(MediaType.TEXT_PLAIN)
    protected String tramite;
    @FormParam("evento")
    @PartType(MediaType.TEXT_PLAIN)
    protected String evento;
    @FormParam("actuacion")
    @PartType(MediaType.TEXT_PLAIN)
    protected String actuacion;
    @FormParam("politicaFirma")
    @PartType(MediaType.TEXT_PLAIN)
    protected List<String> politicaFirma;
    protected String origen;

    protected List<DocumentoDTO> historialVersiones = new ArrayList<>();

    public DocumentoDTO(String idDocumento, String tipologiaDocumental, String nroRadicado, Date fechaRadicacion, String destinatario, String nombreRemitente, String sede, String codigoSede, String dependencia, String codigoDependencia, String serie, String subSerie, String idUnidadDocumental, String nombreUnidadDocumental, String nombreDocumento, String idDocumentoPadre, Date fechaCreacion, String tipoDocumento, String tamano, String tipoPadreAdjunto, String versionLabel, byte[] documento, String[] nroRadicadoReferido, Boolean labelRequired, String nombreProceso, String docAutor, String estado, boolean anulado, String tramite, String evento, String actuacion, List<String> politicaFirma, List<DocumentoDTO> historialVersiones) {
        this.idDocumento = idDocumento;
        this.tipologiaDocumental = tipologiaDocumental;
        this.nroRadicado = nroRadicado;
        this.fechaRadicacion = fechaRadicacion;
        this.destinatario = destinatario;
        this.nombreRemitente = nombreRemitente;
        this.sede = sede;
        this.codigoSede = codigoSede;
        this.dependencia = dependencia;
        this.codigoDependencia = codigoDependencia;
        this.serie = serie;
        this.subSerie = subSerie;
        this.idUnidadDocumental = idUnidadDocumental;
        this.nombreUnidadDocumental = nombreUnidadDocumental;
        this.nombreDocumento = nombreDocumento;
        this.idDocumentoPadre = idDocumentoPadre;
        this.fechaCreacion = fechaCreacion;
        this.tipoDocumento = tipoDocumento;
        this.tamano = tamano;
        this.tipoPadreAdjunto = tipoPadreAdjunto;
        this.versionLabel = versionLabel;
        this.documento = documento;
        this.nroRadicadoReferido = nroRadicadoReferido;
        this.labelRequired = labelRequired;
        this.nombreProceso = nombreProceso;
        this.docAutor = docAutor;
        this.estado = estado;
        this.anulado = anulado;
        this.tramite = tramite;
        this.evento = evento;
        this.actuacion = actuacion;
        this.politicaFirma = politicaFirma;
        this.historialVersiones = historialVersiones;
    }
    public DocumentoDTO(String idDocumento, String tipologiaDocumental, String nroRadicado, Date fechaRadicacion, String destinatario, String nombreRemitente, String sede, String codigoSede, String dependencia, String codigoDependencia, String serie, String subSerie, String idUnidadDocumental, String nombreUnidadDocumental, String nombreDocumento, String idDocumentoPadre, Date fechaCreacion, String tipoDocumento, String tamano, String tipoPadreAdjunto, String versionLabel, byte[] documento, String[] nroRadicadoReferido, Boolean labelRequired, String nombreProceso, String docAutor, String estado, boolean anulado, String tramite, String evento, String actuacion, List<String> politicaFirma, List<DocumentoDTO> historialVersiones, String origen) {
        this.idDocumento = idDocumento;
        this.tipologiaDocumental = tipologiaDocumental;
        this.nroRadicado = nroRadicado;
        this.fechaRadicacion = fechaRadicacion;
        this.destinatario = destinatario;
        this.nombreRemitente = nombreRemitente;
        this.sede = sede;
        this.codigoSede = codigoSede;
        this.dependencia = dependencia;
        this.codigoDependencia = codigoDependencia;
        this.serie = serie;
        this.subSerie = subSerie;
        this.idUnidadDocumental = idUnidadDocumental;
        this.nombreUnidadDocumental = nombreUnidadDocumental;
        this.nombreDocumento = nombreDocumento;
        this.idDocumentoPadre = idDocumentoPadre;
        this.fechaCreacion = fechaCreacion;
        this.tipoDocumento = tipoDocumento;
        this.tamano = tamano;
        this.tipoPadreAdjunto = tipoPadreAdjunto;
        this.versionLabel = versionLabel;
        this.documento = documento;
        this.nroRadicadoReferido = nroRadicadoReferido;
        this.labelRequired = labelRequired;
        this.nombreProceso = nombreProceso;
        this.docAutor = docAutor;
        this.estado = estado;
        this.anulado = anulado;
        this.tramite = tramite;
        this.evento = evento;
        this.actuacion = actuacion;
        this.politicaFirma = politicaFirma;
        this.historialVersiones = historialVersiones;
        this.origen = origen;
    }

    public void setIdDocumento(String idDocumento) {
        if (!StringUtils.isEmpty(idDocumento)) {
            final int index;
            idDocumento = (index = idDocumento.indexOf(';')) != -1
                    ? idDocumento.substring(0, index) : idDocumento;
        }
        this.idDocumento = idDocumento;
    }
}
