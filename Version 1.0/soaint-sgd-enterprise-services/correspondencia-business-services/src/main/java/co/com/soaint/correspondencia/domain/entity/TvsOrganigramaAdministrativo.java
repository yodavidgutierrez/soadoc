/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author jrodriguez
 */
@Getter
@Setter
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TVS_ORGANIGRAMA_ADMINISTRATIVO")
@NamedQueries({
        @NamedQuery(name = "TvsOrganigramaAdministrativo.findAll", query = "SELECT t FROM TvsOrganigramaAdministrativo t where t.orgActivo = true "),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarOrganigramaCodigo", query = "SELECT t FROM TvsOrganigramaAdministrativo t where t.codOrg = :COD_ORG "),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarElementoRayz", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.indEsActivo, t.descOrg, t.codNivel, t.radicadora) " +
                "FROM TvsOrganigramaAdministrativo t WHERE t.codOrganigramaPadre IS NULL and t.orgActivo = true "),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarDescendientesPadres", query = "SELECT DISTINCT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.indEsActivo, t.descOrg, t.codNivel, t.radicadora) " +
                "FROM TvsOrganigramaAdministrativo t INNER JOIN TvsOrganigramaAdministrativo t1 ON t.codOrg = t1.codOrganigramaPadre " +
                "WHERE t.codOrganigramaPadre IS NOT NULL and t.orgActivo = true "),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarDependenciaRadicadora", query = "SELECT DISTINCT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.indEsActivo, t.descOrg, t.codNivel, t.radicadora) " +
                "FROM TvsOrganigramaAdministrativo t " +
                "WHERE t.radicadora = true "),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.inactiveAllOrg", query = "UPDATE TvsOrganigramaAdministrativo t set t.orgActivo = false where t.orgActivo = true "),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarElementoByIdeOrgaAdmin", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.ideOrgaAdminPadre, t.indEsActivo, t.descOrg, t.codNivel, t.codOrganigramaPadre, t.radicadora) " +
                "FROM TvsOrganigramaAdministrativo t " +
                "WHERE t.ideOrgaAdmin = :IDE_ORGA_ADMIN and t.orgActivo = true "),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarElementoByCodOrgaAdmin", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.ideOrgaAdminPadre, t.nomOrg, t.indEsActivo, t.codNivel, t.descOrg, t.codNivel,t.codOrganigramaPadre, t.abreviatura, t.radicadora) " +
                "FROM TvsOrganigramaAdministrativo t " +
                "WHERE t.codOrg = :COD_PADRE and t.orgActivo = true "),
        @NamedQuery(name =  "TvsOrganigramaAdministrativo.consultarDescendientesDirectos", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.ideOrgaAdminPadre, t.nomOrg, t.indEsActivo, t.codNivel, t.descOrg, t.codNivel, t.codOrganigramaPadre, t.abreviatura, t.radicadora) " +
                "FROM TvsOrganigramaAdministrativo t " +
                "WHERE t.codOrganigramaPadre = :COD_ORG_PADRE and t.orgActivo = true"),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.obtenerDependenciaPadre", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.ideOrgaAdminPadre, t.indEsActivo, t.codNivel, t.descOrg, t.radicadora) " +
                "FROM TvsOrganigramaAdministrativo t " +
                "WHERE t.codOrganigramaPadre is null and t.orgActivo = true "),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarDependenciasByIdFunci", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO" +
                "(t1.ideOrgaAdmin, t1.codOrg, t1.nomOrg, t.ideOrgaAdmin, t.codOrg, t.nomOrg, f.auditColumns.estado, t1.radicadora) " +
                "FROM TvsOrganigramaAdministrativo t " +
                "INNER JOIN TvsOrganigramaAdministrativo t1 ON t.codOrg = t1.codOrganigramaPadre " +
                "INNER JOIN TvsOrgaAdminXFunciPk p ON p.codOrgaAdmi = t1.codOrg " +
                "INNER JOIN Funcionarios f ON f.ideFunci = p.funcionarios.ideFunci " +
                "WHERE f.ideFunci = :ID_FUNCI and t1.orgActivo = true "),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarElementosByCodOrgList", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.ideOrgaAdminPadre, t.nomOrg, t.indEsActivo, t.codNivel, t.descOrg, t.codNivel, t.codOrganigramaPadre, t.abreviatura, t.radicadora) " +
                "FROM TvsOrganigramaAdministrativo t " +
                "INNER JOIN TvsOrgaAdminXFunciPk t1 ON t.codOrg = t1.codOrgaAdmi " +
                "WHERE t.codOrg IN :COD_ORG_LIST and t.orgActivo = true "),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarElementoByCodOrg", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.ideOrgaAdminPadre, t1.nomOrg, t.indEsActivo, t.codNivel, t.descOrg, t1.codNivel,t1.codOrganigramaPadre, t.abreviatura, t.radicadora) " +
                "FROM TvsOrganigramaAdministrativo t " +
                "INNER JOIN TvsOrganigramaAdministrativo t1 ON t.codOrganigramaPadre = t1.codOrg " +
                "WHERE t.codOrg = :COD_ORG and t.orgActivo = true "),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarNombreElementoByCodOrg", query = "SELECT t.nomOrg " +
                "FROM TvsOrganigramaAdministrativo t WHERE t.codOrg = :COD_ORG and t.orgActivo = true "),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarElementosByCodigosOrg", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.ideOrgaAdminPadre, t1.nomOrg, t.indEsActivo, t.codNivel, t.descOrg, t1.codNivel, t1.codOrganigramaPadre, t.abreviatura, t.radicadora) " +
                "FROM TvsOrganigramaAdministrativo t " +
                "INNER JOIN TvsOrganigramaAdministrativo t1 ON t.codOrganigramaPadre = t1.codOrg " +
                "WHERE t.codOrg IN :CODIGOS_ORG and t.orgActivo = true "),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarByOrgCode", query = "SELECT t FROM TvsOrganigramaAdministrativo t WHERE t.codOrg = :COD_ORG"),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarDependenciasNivel2", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t1.ideOrgaAdmin, t1.codOrg, t1.nomOrg,t1.indEsActivo, t.radicadora) " +
                "FROM TvsOrganigramaAdministrativo t inner join TvsOrganigramaAdministrativo t1 on t.codOrg=t1.codOrganigramaPadre " +
                "WHERE t.codNivel like '2' and t.orgActivo = true ")})
@javax.persistence.TableGenerator(name = "ORGANIGRAMA_ADMIN", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "ORGANIGRAMA_ADMIN_SEQ", allocationSize = 1)
@Cacheable
public class TvsOrganigramaAdministrativo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ORGANIGRAMA_ADMIN")

    @Column(name = "IDE_ORGA_ADMIN")
    private BigInteger ideOrgaAdmin;

    @Basic
    @Column(name = "COD_ORG")
    private String codOrg;

    @Basic
    @Column(name = "NOM_ORG")
    private String nomOrg;

    @Basic
    @Column(name = "DESC_ORG")
    private String descOrg;

    @Basic
    @Column(name = "IND_ES_ACTIVO")
    private String indEsActivo;

    @Column(name = "IDE_DIRECCION")
    private BigInteger ideDireccion;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "IDE_PLAN_RESPONSABLE")
    private BigDecimal idePlanResponsable;

    @Column(name = "IDE_ORGA_ADMIN_PADRE")
    private BigInteger ideOrgaAdminPadre;

    @Column(name = "COD_NIVEL")
    private String codNivel;

    @Column(name = "COD_ABBR")
    private String abreviatura;

    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;

    @Column(name = "IDE_USUARIO_CREO")
    private BigInteger ideUsuarioCreo;

    @Basic
    @Column(name = "IDE_USUARIO_CAMBIO")
    private BigInteger ideUsuarioCambio;

    @Column(name = "FEC_CAMBIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCambio;

    @Column(name = "NIV_LECTURA")
    private Integer nivelLectura;

    @Column(name = "NIV_ESCRITURA")
    private Integer nivelEscritura;

    //@GeneratedValue
    @Column(name = "IDE_UUID")
    private String ideUuid;
    //private UUID ideUuid;

    @Column(name = "VAL_SISTEMA")
    private String valSistema;

    @Column(name = "VAL_VERSION")
    private String valVersion;

    @Column(name = "COD_ORGA_PADRE")
    private String codOrganigramaPadre;

    @Column(name = "RADICADORA", columnDefinition = "boolean default false", nullable = false)
    private Boolean radicadora = false;

    @Column(name = "ORG_ACTIVO", columnDefinition = "boolean default true", nullable = false)
    private Boolean orgActivo = true;

    @PrePersist
    public void prePersist() {
        if (getRadicadora() == null) {
            setRadicadora(false);
        }
        if (getOrgActivo() == null) {
            setOrgActivo(true);
        }
    }
}
