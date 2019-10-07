package co.com.soaint.foundation.canonical.correspondencia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-agente/1.0.0")
@ToString
public class FuncionarioDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private BigInteger ideFunci;
    private String codTipDocIdent;
    private String nroIdentificacion;
    @JsonProperty("nombre")
    private String nomFuncionario;
    private String valApellido1;
    private String valApellido2;
    private String corrElectronico;
    private String loginName;
    private String estado;
    private List<DependenciaDTO> dependencias;
    private List<RolDTO> roles;
    private String password;
    private String usuarioCrea;
    private String cargo;
    private String firmaPolitica;
    private String esJefe;

    public FuncionarioDTO(BigInteger ideFunci, String codTipDocIdent, String nroIdentificacion, String nomFuncionario,
                            String valApellido1, String valApellido2, String corrElectronico,
                            String loginName, String estado, String cargo) {
        this.ideFunci = ideFunci;
        this.codTipDocIdent = codTipDocIdent;
        this.nroIdentificacion = nroIdentificacion;
        this.nomFuncionario = nomFuncionario;
        this.valApellido1 = valApellido1;
        this.valApellido2 = valApellido2;
        this.corrElectronico = corrElectronico;
        this.loginName = loginName;
        this.estado = estado;
        this.cargo = cargo;

    }
    public FuncionarioDTO(BigInteger ideFunci, String codTipDocIdent, String nroIdentificacion, String nomFuncionario,
                          String valApellido1, String valApellido2, String corrElectronico,
                          String loginName, String estado, String cargo, String firmaPolitica, String esJefe) {
        this.ideFunci = ideFunci;
        this.codTipDocIdent = codTipDocIdent;
        this.nroIdentificacion = nroIdentificacion;
        this.nomFuncionario = nomFuncionario;
        this.valApellido1 = valApellido1;
        this.valApellido2 = valApellido2;
        this.corrElectronico = corrElectronico;
        this.loginName = loginName;
        this.estado = estado;
        this.cargo = cargo;
        this.firmaPolitica = firmaPolitica;
        this.esJefe = esJefe;

    }
    public FuncionarioDTO(BigInteger ideFunci, String codTipDocIdent, String nroIdentificacion, String nomFuncionario,
                          String valApellido1, String valApellido2, String corrElectronico,
                          String loginName, String estado) {
        this(ideFunci, codTipDocIdent, nroIdentificacion, nomFuncionario, valApellido1, valApellido2, corrElectronico, loginName, estado, null);
    }
}
