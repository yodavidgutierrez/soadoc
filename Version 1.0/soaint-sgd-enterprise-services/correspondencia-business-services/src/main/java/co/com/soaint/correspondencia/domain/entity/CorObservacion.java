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
import java.util.Date;


/**
 * @author jrodriguez
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COR_OBSERVACION")
@NamedQueries({
        @NamedQuery(name = "CorObservacion.findAllbyIdInstance", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.ObservacionDTO " +
                "(c.observacion, c.usuario,c.idInstancia,c.fechaCeracion, c.idTarea)  FROM CorObservacion c " +
                "where c.idInstancia =:ID_INSTANCIA "),
        @NamedQuery(name = "CorObservacion.deleteAllbyIdInstance", query = "DELETE FROM CorObservacion c " +
                "where c.idInstancia =:ID_INSTANCIA"),

})
@TableGenerator(name = "COR_OBSERVACION_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_OBSERVACION_SEQ", allocationSize = 1)
public class CorObservacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_OBSERVACION_GENERATOR")
    @Column(name = "IDE_OBSERVACION")
    private BigInteger ideObservacion;

    @Column(name = "OBSERVACION")
    private String observacion;

    @Column(name = "USUARIO")
    private String usuario;

    @Column(name = "ID_INSTANCIA")
    private String idInstancia;

    @Column(name = "ID_TAREA")
    private String idTarea;

    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCeracion;

}
