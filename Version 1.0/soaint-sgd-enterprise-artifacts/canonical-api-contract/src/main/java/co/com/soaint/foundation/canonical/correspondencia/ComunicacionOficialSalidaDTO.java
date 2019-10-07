package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class ComunicacionOficialSalidaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger idAgente;
    private BigInteger idDocumento;
    private String codDependencia;
    private String nroRadicado;
    private Date fechaRadicacion;
    private String razonSocial;
    private String tipoDocumento;
    private String nit;
    private String pais;
    private String departamento;
    private String municipio;
    private String ciudad;
    private String direccion;
    private BigInteger folios;
    private BigInteger anexos;
    private String asunto;
    private String tipoComunicacion;
}
