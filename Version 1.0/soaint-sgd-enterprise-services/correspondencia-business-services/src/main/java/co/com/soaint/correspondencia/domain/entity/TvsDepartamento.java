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
import java.util.List;

/**
 * @author jrodriguez
 */
@Getter
@Setter
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TVS_DEPARTAMENTO")
@NamedQueries({
        @NamedQuery(name = "TvsDepartamento.findAll", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO" +
                "(t.ideDepar, t.nombreDepar, t.codDepar) " +
                "FROM TvsDepartamento t " +
                "WHERE TRIM(t.auditColumns.estado) = TRIM(:ESTADO)"),
        @NamedQuery(name = "TvsDepartamento.findAllByCodPaisAndEstado", query = "SELECT  NEW co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO" +
                "(t.ideDepar, t.nombreDepar, t.codDepar) " +
                "FROM TvsDepartamento t " +
                "INNER JOIN t.pais p " +
                "WHERE TRIM(p.codPais) = TRIM(:COD_PAIS) AND TRIM(t.auditColumns.estado) = TRIM(:ESTADO)"),
        @NamedQuery(name = "TvsDepartamento.existeDepartamentoByCodDep", query = "SELECT  NEW co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO" +
                "(t.ideDepar, t.nombreDepar, t.codDepar) " +
                "FROM TvsDepartamento t " +
                "INNER JOIN t.tvsMunicipioList m " +
                "WHERE TRIM(t.codDepar) = TRIM(:COD_DEP)"),
        @NamedQuery(name = "TvsDepartamento.findByCodMunic", query = "SELECT  NEW co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO" +
                "(t.ideDepar, t.nombreDepar, t.codDepar) " +
                "FROM TvsDepartamento t " +
                "INNER JOIN t.tvsMunicipioList m " +
                "WHERE TRIM(m.codMunic) = TRIM(:COD_MUNIC)"),
        @NamedQuery(name = "TvsDepartamento.findByCodDepart", query = "SELECT t.nombreDepar " +
                "FROM TvsDepartamento t " +
                "WHERE TRIM(t.codDepar) = TRIM(:COD_DEPART)"),
        @NamedQuery(name = "TvsDepartamento.findByCodDep", query = "SELECT  NEW co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO" +
                "(t.ideDepar, t.nombreDepar, t.codDepar) " +
                "FROM TvsDepartamento t " +
                "WHERE TRIM(t.codDepar) = TRIM(:COD_DEP)")})
public class TvsDepartamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_DEPAR")
    private BigInteger ideDepar;
    @Column(name = "NOMBRE_DEPAR")
    private String nombreDepar;
    @Column(name = "COD_DEPAR")
    private String codDepar;

    @JoinColumn(name = "IDE_PAIS", referencedColumnName = "IDE_PAIS")
    @ManyToOne
    private TvsPais pais;

    @OneToMany(mappedBy = "departamento", orphanRemoval = true)
    private List<TvsMunicipio> tvsMunicipioList;

    @Embedded
    private AuditColumns auditColumns;

}
