/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jrodriguez
 */
@Getter
@Setter
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = {"ideFunci", "tvsOrgaAdminXFunciPkList"})
@Table(name = "FUNCIONARIOS")
@NamedQueries({
        @NamedQuery(name = "Funcionarios.findAll", query = "SELECT f FROM Funcionarios f"),
        @NamedQuery(name = "Funcionarios.findByLoginNameAndEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO " +
                "(f.ideFunci, f.codTipDocIdent, f.nroIdentificacion, f.nomFuncionario, f.valApellido1, f.valApellido2, " +
                "f.corrElectronico, f.loginName, f.auditColumns.estado) " +
                "FROM Funcionarios f " +
                "WHERE TRIM(f.loginName) = TRIM(:LOGIN_NAME) AND TRIM(f.auditColumns.estado) = TRIM(:ESTADO)"),
        @NamedQuery(name = "Funcionarios.findByLoginNamList", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO " +
                "(f.ideFunci, f.codTipDocIdent, f.nroIdentificacion, f.nomFuncionario, f.valApellido1, f.valApellido2, " +
                "f.corrElectronico, f.loginName, f.auditColumns.estado, f.cargo, f.firmaPolitica, f.esJefe) " +
                "FROM Funcionarios f " +
                "WHERE TRIM(f.loginName) IN :LOGIN_NAMES"),
        @NamedQuery(name = "Funcionarios.findByLoginName", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO " +
                "(f.ideFunci, f.codTipDocIdent, f.nroIdentificacion, f.nomFuncionario, f.valApellido1, f.valApellido2, " +
                "f.corrElectronico, f.loginName, f.auditColumns.estado, f.cargo, f.firmaPolitica, f.esJefe) " +
                "FROM Funcionarios f " +
                "WHERE TRIM(f.loginName) = TRIM(:LOGIN_NAME)"),
        @NamedQuery(name = "Funcionarios.findFirmaPoliticaByLoginName", query = "SELECT f.firmaPolitica " +
                "FROM Funcionarios f " +
                "WHERE TRIM(f.loginName) = TRIM(:LOGIN_NAME)"),
        @NamedQuery(name = "Funcionarios.findByRol", query = "SELECT  f " +
                "FROM Funcionarios f inner join f.corRolesList rol " +
                "WHERE TRIM(rol.nombre) = TRIM(:ROL)"),
        @NamedQuery(name = "Funcionarios.findByNroIdentificacion", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO " +
                "(f.ideFunci, f.codTipDocIdent, f.nroIdentificacion, f.nomFuncionario, f.valApellido1, f.valApellido2, " +
                "f.corrElectronico, f.loginName, f.auditColumns.estado) " +
                "FROM Funcionarios f " +
                "WHERE TRIM(f.nroIdentificacion) = TRIM(:NRO_IDENTIFICACION)"),
        @NamedQuery(name = "Funcionarios.findByIdeFunci", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO " +
                "(f.ideFunci, f.codTipDocIdent, f.nroIdentificacion, f.nomFuncionario, f.valApellido1, f.valApellido2, " +
                "f.corrElectronico, f.loginName, f.auditColumns.estado) " +
                "FROM Funcionarios f " +
                "WHERE f.ideFunci = :IDE_FUNCI"),
        @NamedQuery(name = "Funcionarios.consultarCredencialesByIdeFunci", query = "SELECT f.credenciales " +
                "FROM Funcionarios f " +
                "WHERE f.ideFunci = :IDE_FUNCI"),
        @NamedQuery(name = "Funcionarios.existFuncionarioByIdeFunci", query = "SELECT count(f)" +
                "FROM Funcionarios f " +
                "WHERE f.ideFunci = :IDE_FUNCI"),
        @NamedQuery(name = "Funcionarios.consultarLoginNameByIdeFunci", query = "SELECT f.loginName " +
                "FROM Funcionarios f " +
                "WHERE f.ideFunci = :IDE_FUNCI"),
        @NamedQuery(name = "Funcionarios.consultarEmailByIdeFunci", query = "SELECT f.corrElectronico " +
                "FROM Funcionarios f " +
                "WHERE f.ideFunci = :IDE_FUNCI"),
        @NamedQuery(name = "Funcionarios.findAllByCodOrgaAdmiAndEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO " +
                "(f.ideFunci, f.codTipDocIdent, f.nroIdentificacion, f.nomFuncionario, f.valApellido1, f.valApellido2, " +
                "f.corrElectronico, f.loginName, f.auditColumns.estado) " +
                "FROM Funcionarios f " +

                "INNER JOIN f.tvsOrgaAdminXFunciPkList o " +
                "WHERE TRIM(f.auditColumns.estado) = TRIM(:ESTADO) AND o.codOrgaAdmi = :COD_ORGA_ADMI"),
        @NamedQuery(name = "Funcionarios.findAllByCodOrgaAdmi", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO " +
                "(f.ideFunci, f.codTipDocIdent, f.nroIdentificacion, f.nomFuncionario, f.valApellido1, f.valApellido2, " +
                "f.corrElectronico, f.loginName, f.auditColumns.estado) " +
                "FROM Funcionarios f " +
                "INNER JOIN TvsOrgaAdminXFunciPk o ON f.ideFunci = o.funcionarios.ideFunci " +
                "WHERE o.codOrgaAdmi = :COD_ORGA_ADMI"),
        @NamedQuery(name = "Funcionarios.filter", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO " +
                "(f.ideFunci, f.codTipDocIdent, f.nroIdentificacion, f.nomFuncionario, f.valApellido1, f.valApellido2, " +
                "f.corrElectronico, f.loginName, f.auditColumns.estado, f.cargo, f.firmaPolitica, f.esJefe) " +
                "FROM Funcionarios f " +
                "WHERE (:COD_TIP_DOC_IDENT is null OR f.codTipDocIdent = :COD_TIP_DOC_IDENT) AND (:NRO_IDENTIFICACION is null OR f.nroIdentificacion = :NRO_IDENTIFICACION) " +
                "AND (:NOM_FUNCIONARIO is null OR f.nomFuncionario = :NOM_FUNCIONARIO) AND (:VAL_APELLIDO1 is null OR f.valApellido1 = :VAL_APELLIDO1) " +
                "AND (:VAL_APELLIDO2 is null OR f.valApellido2 = :VAL_APELLIDO2) AND (:CORR_ELECTRONICO is null OR f.corrElectronico = :CORR_ELECTRONICO) " +
                "AND (:LOGIN_NAME is null OR f.loginName = :LOGIN_NAME) AND (:ESTADO is null OR f.auditColumns.estado = :ESTADO) "),
        @NamedQuery(name = "Funcionarios.update", query = "UPDATE Funcionarios f " +
                "SET f.codTipDocIdent = :COD_TIP_DOC_IDENT, f.nroIdentificacion = :NRO_IDENTIFICACION, " +
                "f.nomFuncionario = :NOM_FUNCIONARIO, f.valApellido1 = :VAL_APELLIDO1, " +
                "f.valApellido2 = :VAL_APELLIDO2, f.corrElectronico = :CORR_ELECTRONICO, " +
                "f.credenciales = :CREDENCIALES, f.auditColumns.estado = :ESTADO, " +
                "f.auditColumns.fecCambio = :FECHA_CAMBIO, f.cargo = :CARGO " +
                "WHERE f.ideFunci = :IDE_FUNCI")})
@javax.persistence.TableGenerator(name = "FUNCIONARIOS_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "FUNCIONARIOS_SEQ", allocationSize = 1)
public class Funcionarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "FUNCIONARIOS_GENERATOR")
    @Column(name = "IDE_FUNCI")
    private BigInteger ideFunci;
    @Basic
    @Column(name = "COD_TIP_DOC_IDENT")
    private String codTipDocIdent;
    @Basic
    @Column(name = "NRO_IDENTIFICACION")
    private String nroIdentificacion;
    @Basic
    @Column(name = "NOM_FUNCIONARIO")
    private String nomFuncionario;
    @Basic
    @Column(name = "VAL_APELLIDO1")
    private String valApellido1;
    @Column(name = "VAL_APELLIDO2")
    private String valApellido2;
    @Column(name = "CORR_ELECTRONICO")
    private String corrElectronico;
    @Basic
    @Column(name = "LOGIN_NAME")
    private String loginName;
    @Basic
    @Column(name = "CARGO")
    private String cargo;
    @Basic
    @Column(name = "FIRMA_POLITICA")
    private String firmaPolitica;
    @Basic
    @Column(name = "JEFE")
    private String esJefe;
    @Embedded
    private AuditColumns auditColumns;
    @Column(name = "CREDENCIALES")
    private String credenciales;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionarios", orphanRemoval = true)
    private List<TvsOrgaAdminXFunciPk> tvsOrgaAdminXFunciPkList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionarios", orphanRemoval = true)
    private List<CorRol> corRolesList = new ArrayList<>();

    /*public void addRoles(CorRol corRol) {
        if (corRol == null) {
            return;
        }
        if (null == corRolesList) {
            corRolesList = new ArrayList<>();
        }
        corRolesList.add(corRol);
        corRol.setFuncionarios(this);
    }*/
}
