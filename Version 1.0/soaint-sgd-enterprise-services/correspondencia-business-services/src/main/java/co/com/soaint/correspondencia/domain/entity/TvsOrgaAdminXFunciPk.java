package co.com.soaint.correspondencia.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by esanchez on 7/19/2017.
 */
@Getter
@Setter
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TVS_ORGA_ADMIN_X_FUNCI_PK")
@NamedQueries({
        @NamedQuery(name = "TvsOrgaAdminXFunciPk.findAll", query = "SELECT funci FROM TvsOrgaAdminXFunciPk funci"),
        @NamedQuery(name = "TvsOrgaAdminXFunciPk.findCodOrgaAdmiByIdeFunci", query = "SELECT t.codOrgaAdmi " +
                "FROM TvsOrgaAdminXFunciPk t " +
                "WHERE t.funcionarios.ideFunci = :IDE_FUNCI"),
        @NamedQuery(name = "TvsOrgaAdminXFunciPk.findFuncByCodOrgaAdmi", query = "SELECT t.funcionarios.nomFuncionario " +
                "FROM TvsOrgaAdminXFunciPk t " +
                "WHERE t.codOrgaAdmi = :COD_ORG"),
        @NamedQuery(name = "TvsOrgaAdminXFunciPk.deleteByIdeFunciCodDep", query = "DELETE " +
                "FROM TvsOrgaAdminXFunciPk t " +
                "WHERE t.funcionarios.ideFunci =:ID_FUNCIONARIO and t.codOrgaAdmi = :COD_ORG"),
        @NamedQuery(name = "TvsOrgaAdminXFunciPk.deleteAll", query = "DELETE " +
                "FROM TvsOrgaAdminXFunciPk t " +
                "WHERE t.funcionarios.ideFunci =:ID_FUNCIONARIO"),
        @NamedQuery(name = "TvsOrgaAdminXFunciPk.updateByIdeFunciCodDep", query = "UPDATE " +
                "TvsOrgaAdminXFunciPk t SET t.codOrgaAdmi = :COD_ORG " +
                "WHERE t.funcionarios.ideFunci =:ID_FUNCIONARIO and t.codOrgaAdmi = :COD_ORG")
})

@javax.persistence.TableGenerator(name = "TVS_ORGA_ADMIN_X_FUNCI_PK", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "TVS_ORGA_ADMIN_X_FUNCI_PK_SEQ", allocationSize = 1)
public class TvsOrgaAdminXFunciPk implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_ORGA_ADMIN_FUNCI")
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TVS_ORGA_ADMIN_X_FUNCI_PK")
    private BigInteger idOrganigramaXFunci;

    @Column(name = "COD_ORGA_ADMI")
    @Basic
    private String codOrgaAdmi;

    @Embedded
    private AuditColumns auditColumns;

    @JoinColumn(name = "IDE_FUNCI", referencedColumnName = "IDE_FUNCI")
    @ManyToOne
    private Funcionarios funcionarios;
}
