/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

/**
 * @author jrodriguez
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COR_ROL")
//, uniqueConstraints = {@UniqueConstraint(columnNames = "NOMBRE")}
@Log4j2
@NamedQueries({
        @NamedQuery(name = "CorRol.findAll", query = "SELECT distinct c FROM CorRol c"),
        @NamedQuery(name = "CorRol.findAllByIdFunci", query = "SELECT distinct c FROM CorRol c inner join Funcionarios funci on c.funcionarios.ideFunci = funci.ideFunci " +
                "where funci.ideFunci =:ID_FUNCI"),
        @NamedQuery(name = "CorRol.findAllByLoginName", query = "SELECT distinct c FROM CorRol c inner join Funcionarios funci on c.funcionarios.ideFunci = funci.ideFunci " +
                "where funci.loginName =:LOGIN_NAME"),
})
@TableGenerator(name = "COR_ROL_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_ROL_SEQ", allocationSize = 1)
public class CorRol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_ROL_GENERATOR")
    @Column(name = "IDE_ROL")
    private BigInteger ideRol;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "FEC_CREACION", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;

    @JoinColumn(name = "IDE_FUNCI",referencedColumnName = "IDE_FUNCI")
    @ManyToOne
    private Funcionarios funcionarios;

    @PrePersist
    protected void onCreate() {
        fecCreacion = new Date();
    }

}