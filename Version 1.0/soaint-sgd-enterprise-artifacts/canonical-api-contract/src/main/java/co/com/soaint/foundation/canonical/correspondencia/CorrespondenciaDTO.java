package co.com.soaint.foundation.canonical.correspondencia;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:4-May-2017
 * Author: jrodriguez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/correspondencia/1.0.0")
@ToString
public class CorrespondenciaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigInteger ideDocumento;
    private String descripcion;
    private String tiempoRespuesta;
    private String codUnidadTiempo;
    private String codMedioRecepcion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "America/Bogota")
    private Date fecRadicado;
    private String nroRadicado;
    private String radicadoPadre;
    private Date fecDocumento;
    private String codTipoDoc;
    private String codTipoCmc;
    private String ideInstancia;
    private String reqDistFisica;
    private String reqDistElectronica;
    private String codFuncRadica;
    private String codSede;
    private String codDependencia;
    private String reqDigita;
    private String adjuntarDocumento;
    private String codEmpMsj;
    private String nroGuia;
    private String idEcm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "America/Bogota")
    private Date fecVenGestion;
    private String codEstado;
    private String inicioConteo;
    private String codClaseEnvio;
    private String codModalidadEnvio;
    private String abreviatura;
    private Date fechaGuia;
    private String proveedor;
    private String peso;
    private String valorEnvio;
    private BigInteger distribuido;
    private Boolean contingencia;

    private Date fecEntrega;
    private Date fecEnvio;
    private String estadoEntrega;
    private String observacionesEntrega;

    public CorrespondenciaDTO(Date fecEntrega, String estadoEntrega, String observacionesEntrega) {
        this.fecEntrega = fecEntrega;
        this.estadoEntrega = estadoEntrega;
        this.observacionesEntrega = observacionesEntrega;
    }

    public CorrespondenciaDTO(String nroRadicado, String codSede, String codDependencia) {
        this.nroRadicado = nroRadicado;
        this.codSede = codSede;
        this.codDependencia = codDependencia;
    }
    public CorrespondenciaDTO(BigInteger ideDocumento, String descripcion, String tiempoRespuesta,
                              String codUnidadTiempo, String codMedioRecepcion, Date fecRadicado, String nroRadicado,
                              Date fecDocumento, String codTipoDoc, String codTipoCmc, String reqDistFisica,
                              String ideInstancia, String codFuncRadica, String codSede, String codDependencia,
                              String reqDigita, String nroGuia, String codEmpMsj, Date fecVenGestion, String codEstado) {
        this.ideDocumento = ideDocumento;
        this.descripcion = descripcion;
        this.tiempoRespuesta = tiempoRespuesta;
        this.codUnidadTiempo = codUnidadTiempo;
        this.codMedioRecepcion = codMedioRecepcion;
        this.fecRadicado = fecRadicado;
        this.nroRadicado = nroRadicado;
        this.fecDocumento = fecDocumento;
        this.codTipoDoc = codTipoDoc;
        this.codTipoCmc = codTipoCmc;
        this.reqDistFisica = reqDistFisica;
        this.ideInstancia = ideInstancia;
        this.codFuncRadica = codFuncRadica;
        this.codSede = codSede;
        this.codDependencia = codDependencia;
        this.reqDigita = reqDigita;
        this.nroGuia = nroGuia;
        this.codEmpMsj = codEmpMsj;
        this.fecVenGestion = fecVenGestion;
        this.codEstado = codEstado;
    }

    public CorrespondenciaDTO(BigInteger ideDocumento, String descripcion, String tiempoRespuesta,
                              String codUnidadTiempo, String codMedioRecepcion, Date fecRadicado, String nroRadicado,
                              String codTipoCmc, String reqDistFisica,
                              String ideInstancia, String codFuncRadica, String codSede, String codDependencia,
                              String reqDigita, String nroGuia, String codEmpMsj, Date fecVenGestion, String codEstado, String adjuntarDocumento){
        this.ideDocumento = ideDocumento;
        this.descripcion = descripcion;
        this.tiempoRespuesta = tiempoRespuesta;
        this.codUnidadTiempo = codUnidadTiempo;
        this.codMedioRecepcion = codMedioRecepcion;
        this.fecRadicado = fecRadicado;
        this.nroRadicado = nroRadicado;
        this.codTipoCmc = codTipoCmc;
        this.reqDistFisica = reqDistFisica;
        this.ideInstancia = ideInstancia;
        this.codFuncRadica = codFuncRadica;
        this.codSede = codSede;
        this.codDependencia = codDependencia;
        this.reqDigita = reqDigita;
        this.adjuntarDocumento = adjuntarDocumento;
        this.nroGuia = nroGuia;
        this.codEmpMsj = codEmpMsj;
        this.fecVenGestion = fecVenGestion;
        this.codEstado = codEstado;
    }

    public CorrespondenciaDTO(BigInteger ideDocumento, String descripcion, String tiempoRespuesta,
                              String codUnidadTiempo, String codMedioRecepcion, Date fecRadicado, String nroRadicado,
                              String codTipoCmc, String reqDistFisica,
                              String ideInstancia, String codFuncRadica, String codSede, String codDependencia,
                              String reqDigita, String nroGuia, String codEmpMsj, Date fecVenGestion, String codEstado){
        this.ideDocumento = ideDocumento;
        this.descripcion = descripcion;
        this.tiempoRespuesta = tiempoRespuesta;
        this.codUnidadTiempo = codUnidadTiempo;
        this.codMedioRecepcion = codMedioRecepcion;
        this.fecRadicado = fecRadicado;
        this.nroRadicado = nroRadicado;
        this.codTipoCmc = codTipoCmc;
        this.reqDistFisica = reqDistFisica;
        this.ideInstancia = ideInstancia;
        this.codFuncRadica = codFuncRadica;
        this.codSede = codSede;
        this.codDependencia = codDependencia;
        this.reqDigita = reqDigita;
        this.nroGuia = nroGuia;
        this.codEmpMsj = codEmpMsj;
        this.fecVenGestion = fecVenGestion;
        this.codEstado = codEstado;
    }

    public CorrespondenciaDTO(BigInteger ideDocumento, String descripcion, String tiempoRespuesta,
                              String codUnidadTiempo, String codMedioRecepcion, Date fecRadicado, String nroRadicado,
                              String codTipoCmc, String reqDistFisica,
                              String ideInstancia, String codFuncRadica, String codSede, String codDependencia,
                              String reqDigita, String nroGuia, String codEmpMsj, Date fecVenGestion, String codEstado, String codClaseEnvio, String codModalidadEnvio){
        this.ideDocumento = ideDocumento;
        this.descripcion = descripcion;
        this.tiempoRespuesta = tiempoRespuesta;
        this.codUnidadTiempo = codUnidadTiempo;
        this.codMedioRecepcion = codMedioRecepcion;
        this.fecRadicado = fecRadicado;
        this.nroRadicado = nroRadicado;
        this.codTipoCmc = codTipoCmc;
        this.reqDistFisica = reqDistFisica;
        this.ideInstancia = ideInstancia;
        this.codFuncRadica = codFuncRadica;
        this.codSede = codSede;
        this.codDependencia = codDependencia;
        this.reqDigita = reqDigita;
        this.nroGuia = nroGuia;
        this.codEmpMsj = codEmpMsj;
        this.fecVenGestion = fecVenGestion;
        this.codEstado = codEstado;
        this.codClaseEnvio = codClaseEnvio;
        this.codModalidadEnvio = codModalidadEnvio;
    }
}
