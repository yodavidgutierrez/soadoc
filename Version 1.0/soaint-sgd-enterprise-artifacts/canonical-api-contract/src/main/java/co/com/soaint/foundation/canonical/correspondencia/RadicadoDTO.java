package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:2-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-referido/1.0.0")
@ToString
public class RadicadoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger ideAgente;
    private String tipoDocumento;
    private String numeroIdentificacion;
    private String nombre;
    private String numeroRadicado;
    private String asunto;
    private String razonSocial;
    private String NIT;
    private String codigoCalidad;
    private String codTipoPers;
    private BigInteger consecutivo;
    private Date fechaRadicacion;
    private String idECM;

    public RadicadoDTO(Date fechaRadicacion) {
        this.fechaRadicacion = fechaRadicacion;
    }

    public RadicadoDTO(BigInteger numeroRadicado, BigInteger consecutivo) {
        this.numeroRadicado = numeroRadicado.toString();
        this.consecutivo = consecutivo;
    }

    public RadicadoDTO(BigInteger ideAgente, String tipoDocumento, String numeroIdentificacion, String nombre, String numeroRadicado, String asunto, String razonSocial, String NIT, String codigoCalidad, String codTipoPers, String idECM) {
        this.tipoDocumento = tipoDocumento;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombre = nombre;
        this.numeroRadicado = numeroRadicado;
        this.asunto = asunto;
        this.razonSocial = razonSocial;
        this.NIT = NIT;
        this.codigoCalidad = codigoCalidad;
        this.codTipoPers = codTipoPers;
        this.ideAgente = ideAgente;
        this.idECM = idECM;
    }

    public RadicadoDTO(BigInteger ideAgente, String tipoDocumento, String numeroIdentificacion, String nombre,
                       BigInteger radicadoPadre, BigInteger consecutivo, String asunto, String razonSocial, String NIT,
                       String codigoCalidad, String codTipoPers, String idECM) {
        this.tipoDocumento = tipoDocumento;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombre = nombre;
        this.numeroRadicado = radicadoPadre.toString().concat("-").concat(consecutivo.toString());
        this.asunto = asunto;
        this.razonSocial = razonSocial;
        this.NIT = NIT;
        this.codigoCalidad = codigoCalidad;
        this.codTipoPers = codTipoPers;
        this.ideAgente = ideAgente;
        this.idECM = idECM;
    }
}
