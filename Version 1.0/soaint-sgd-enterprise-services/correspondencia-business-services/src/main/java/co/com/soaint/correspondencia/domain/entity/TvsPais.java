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
@Table(name = "TVS_PAIS")
@NamedQueries({
        @NamedQuery(name = "TvsPais.findAll", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.PaisDTO" +
                "(t.idePais, t.nombrePais, t.codPais) " +
                "FROM TvsPais t " +
                "WHERE TRIM(t.auditColumns.estado) = TRIM(:ESTADO)"),
        @NamedQuery(name = "TvsPais.findByNombrePaisAndEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.PaisDTO" +
                "(t.idePais, t.nombrePais, t.codPais) " +
                "FROM TvsPais t " +
                "WHERE TRIM(t.nombrePais) LIKE :NOMBRE_PAIS AND TRIM(t.auditColumns.estado) = TRIM(:ESTADO) " +
                "ORDER BY t.nombrePais"),
        @NamedQuery(name = "TvsPais.findByCodDepar", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.PaisDTO" +
                "(t.idePais, t.nombrePais, t.codPais) " +
                "FROM TvsPais t " +
                "INNER JOIN t.tvsDepartamentoList d " +
                "WHERE TRIM(d.codDepar) = TRIM(:COD_DEPAR) " +
                "ORDER BY t.nombrePais"),
        @NamedQuery(name = "TvsPais.findPaisByCodigo", query = "SELECT (t.nombrePais)" +
                "FROM TvsPais t " +
                "WHERE t.codPais = TRIM(:COD_PAIS)"),
        @NamedQuery(name = "TvsPais.findByCod", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.PaisDTO" +
                "(t.idePais, t.nombrePais, t.codPais) " +
                "FROM TvsPais t " +
                "WHERE TRIM(t.codPais) = TRIM(:COD_PAIS) ")})
@javax.persistence.TableGenerator(name = "TVS_PAIS_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "TVS_PAIS_SEQ", allocationSize = 1)
public class TvsPais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TVS_PAIS_GENERATOR")
    @Column(name = "IDE_PAIS")
    private BigInteger idePais;

    @Column(name = "NOMBRE_PAIS")
    private String nombrePais;

    @Basic
    @Column(name = "COD_PAIS")
    private String codPais;

    @Embedded
    private AuditColumns auditColumns;

    @OneToMany(mappedBy = "pais", orphanRemoval = true)
    private List<TvsDepartamento> tvsDepartamentoList = new ArrayList<>();

}
