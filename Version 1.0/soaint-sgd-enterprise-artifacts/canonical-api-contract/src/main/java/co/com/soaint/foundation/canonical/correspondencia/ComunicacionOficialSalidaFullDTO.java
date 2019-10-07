package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:2-Jun-2017
 * Author: jrodriguez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-agente/1.0.0")
@ToString
public class ComunicacionOficialSalidaFullDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger idAgente;
    private BigInteger idDocumento;
    private String nroRadicado;
    private Date fechaRadicacion;
    private String dependencia;
    private String nombre;
    private String tipoDocumento;
    private String nroIdentificacion;
    private String pais;
    private String departamento;
    private String municipio;
    private String ciudad;
    private String direccion;
    private BigInteger folios;
    private BigInteger anexos;
    private String peso;
    private String nroGuia;
    private String valorEnvio;
    private String asunto;
    List<AnexoFullDTO> listaAnexos;
    private String tipoComunicacion;

    public ComunicacionOficialSalidaFullDTO(String nroRadicado, Date fechaRadicacion, String dependencia, String nombre, String tipoDocumento, String nroIdentificacion, String pais, String departamento, String municipio, String ciudad, String direccion, BigInteger folios, BigInteger anexos) {
        this.nroRadicado = nroRadicado;
        this.fechaRadicacion = fechaRadicacion;
        this.dependencia = dependencia;
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.nroIdentificacion = nroIdentificacion;
        this.pais = pais;
        this.departamento = departamento;
        this.municipio = municipio;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.folios = folios;
        this.anexos = anexos;
    }
}
