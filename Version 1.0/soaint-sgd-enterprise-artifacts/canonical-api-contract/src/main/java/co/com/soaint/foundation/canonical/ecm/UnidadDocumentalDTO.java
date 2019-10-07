package co.com.soaint.foundation.canonical.ecm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:22-Feb-2018
 * Author: dotero
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(includeFieldNames = false, of = "nombreUnidadDocumental")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/unidad-documental/1.0.0")
public class UnidadDocumentalDTO extends SerieDTO {

    private static final long serialVersionUID = 1L;

    protected String accion;
    protected Boolean inactivo;
    protected String ubicacionTopografica;
    protected Calendar fechaCierre;
    protected Calendar fechaCreacion;
    protected String id;
    protected String faseArchivo; //archivo central, archivo gestion
    protected Calendar fechaExtremaInicial;
    protected String soporte;
    protected String codigoUnidadDocumental;
    protected String nombreUnidadDocumental;
    protected String descriptor2;
    protected String descriptor1;
    protected String estado; // aprobado/rechazado/verificado
    protected String tipoTransferencia; //primaria/secundaria
    protected String consecutivoTransferencia;
    protected String disposicion; //tipos disposiciones
    protected String autor;
    protected Calendar fechaExtremaFinal;
    protected LocalDateTime fechaArchivoRetencion;
    protected Boolean cerrada;

    //heredadas
    protected String codigoSubSerie;
    protected String nombreSubSerie;

    /*protected String codigoSerie;
    protected String nombreSerie;*/

    protected String codigoDependencia;
    protected String nombreDependencia;

    protected String codigoSede;
    protected String nombreSede;

    protected String observaciones;

    //Agregacion
    protected List<DocumentoDTO> listaDocumentos;

    @Builder(toBuilder = true, builderMethodName = "newInstance")
    public UnidadDocumentalDTO(String codigoBase, String nombreBase, String accion, Boolean inactivo,
                               String ubicacionTopografica, Calendar fechaCierre, String id, String faseArchivo,
                               Calendar fechaExtremaInicial, String soporte, String codigoUnidadDocumental,
                               String nombreUnidadDocumental, String descriptor2, String descriptor1,
                               String estado, String disposicion, Calendar fechaExtremaFinal, Boolean cerrada,
                               String codigoSubSerie, String nombreSubSerie, String codigoSerie, String nombreSerie,
                               String codigoDependencia, String nombreDependencia, String codigoSede, String nombreSede,
                               String observaciones, List<DocumentoDTO> listaDocumentos) {
        super(codigoBase, nombreBase, codigoSerie, nombreSerie);
        this.accion = accion;
        this.inactivo = inactivo;
        this.ubicacionTopografica = ubicacionTopografica;
        this.fechaCierre = fechaCierre;
        this.id = id;
        this.faseArchivo = faseArchivo;
        this.fechaExtremaInicial = fechaExtremaInicial;
        this.soporte = soporte;
        this.codigoUnidadDocumental = codigoUnidadDocumental;
        this.nombreUnidadDocumental = nombreUnidadDocumental;
        this.descriptor2 = descriptor2;
        this.descriptor1 = descriptor1;
        this.estado = estado;
        this.disposicion = disposicion;
        this.fechaExtremaFinal = fechaExtremaFinal;
        this.cerrada = cerrada;
        this.codigoSubSerie = codigoSubSerie;
        this.nombreSubSerie = nombreSubSerie;
        /*this.codigoSerie = codigoSerie;
        this.nombreSerie = nombreSerie;*/
        this.codigoDependencia = codigoDependencia;
        this.nombreDependencia = nombreDependencia;
        this.codigoSede = codigoSede;
        this.nombreSede = nombreSede;
        this.observaciones = observaciones;
        this.listaDocumentos = listaDocumentos;
    }
}