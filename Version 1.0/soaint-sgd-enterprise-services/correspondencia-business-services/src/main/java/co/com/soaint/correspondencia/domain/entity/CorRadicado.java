package co.com.soaint.correspondencia.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COR_RADICADO")
@NamedQueries({
        @NamedQuery(name = "CorRadicado.findMaxConsecutivo", query = "SELECT MAX(cr.consecutivo) FROM CorRadicado cr where cr.radicadoPadre = :RADICADO_PADRE "),
        @NamedQuery(name = "CorRadicado.findMaxRadicadoPadre", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.RadicadoDTO" +
                "(cr.radicadoPadre, cr.consecutivo) " +
                "FROM CorRadicado cr WHERE cr.radicadoPadre = (SELECT MAX(cor.radicadoPadre) FROM CorRadicado cor where cor.radicadoPadre < 900000)"),
        @NamedQuery(name = "CorRadicado.findRadicadosByRadPadreAndDocIdent", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.RadicadoDTO" +
                "(ca.ideAgente, ca.codTipDocIdent, ca.nroDocuIdentidad, ca.nombre, cr.radicadoPadre, cr.consecutivo, ppd.asunto, ca.razonSocial, ca.nit, ca.codEnCalidad, ca.codTipoPers, ppd.ideEcm) " +
                "FROM CorRadicado cr INNER JOIN CorCorrespondencia cc ON cr.id = cc.corRadicado.id " +
                "INNER JOIN PpdDocumento ppd ON ppd.corCorrespondencia.ideDocumento = cc.ideDocumento " +
                "INNER JOIN cc.corAgenteList ca " +
                "WHERE cr.radicadoPadre = :RADICADO_PADRE and cc.nroGuia = :NO_GUIA and ca.nombre = :NOMBRE and (:NRO_DOCU_IDENTIDAD is null or ca.nit like :NRO_DOCU_IDENTIDAD or ca.nroDocuIdentidad LIKE :NRO_DOCU_IDENTIDAD) " +
                "and cr.consecutivo = :CONSECUTIVO "),
        @NamedQuery(name = "CorRadicado.findRadicadosByRadPadre", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.RadicadoDTO" +
                "(ca.ideAgente, ca.codTipDocIdent, ca.nroDocuIdentidad, ca.nombre, cc.corRadicado.radicadoPadre, cc.corRadicado.consecutivo, ppd.asunto, ca.razonSocial, ca.nit, ca.codEnCalidad, ca.codTipoPers, ppd.ideEcm) " +
                "FROM CorCorrespondencia cc " +
                "INNER JOIN PpdDocumento ppd ON ppd.corCorrespondencia.ideDocumento = cc.ideDocumento " +
                "INNER JOIN cc.corAgenteList ca " +
                "WHERE cc.corRadicado.radicadoPadre = :RADICADO_PADRE and cc.corRadicado.consecutivo = :CONSECUTIVO and ca.codTipoRemite = 'EXT'"),
        @NamedQuery(name = "CorRadicado.findRadicadosByDocIdent", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.RadicadoDTO" +
                "(ca.ideAgente, ca.codTipDocIdent, ca.nroDocuIdentidad, ca.nombre, cr.radicadoPadre, cr.consecutivo, ppd.asunto, ca.razonSocial, ca.nit, ca.codEnCalidad, ca.codTipoPers, ppd.ideEcm) " +
                "FROM CorRadicado cr INNER JOIN CorCorrespondencia cc ON cr.id = cc.corRadicado.id " +
                //"JOIN ca.corCorrespondencia cc ON ca.corCorrespondencia.ideDocumento = cc.ideDocumento " +
                "INNER JOIN PpdDocumento ppd ON ppd.corCorrespondencia.ideDocumento = cc.ideDocumento " +
                "INNER JOIN cc.corAgenteList ca " +
                "WHERE ca.nroDocuIdentidad LIKE :NRO_DOCU_IDENTIDAD and ca.codTipDocIdent = :TIP_DOC and cr.consecutivo = :CONSECUTIVO and ca.codTipoRemite = 'EXT'"),
        @NamedQuery(name = "CorRadicado.findNombre", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.RadicadoDTO" +
                "(ca.ideAgente, ca.codTipDocIdent, ca.nroDocuIdentidad, ca.nombre, cr.radicadoPadre, cr.consecutivo, ppd.asunto, ca.razonSocial, ca.nit, ca.codEnCalidad, ca.codTipoPers, ppd.ideEcm) " +
                "FROM CorRadicado cr INNER JOIN CorCorrespondencia cc ON cr.id = cc.corRadicado.id " +
                //"JOIN ca.corCorrespondencia cc ON ca.corCorrespondencia.ideDocumento = cc.ideDocumento " +
                "INNER JOIN PpdDocumento ppd ON ppd.corCorrespondencia.ideDocumento = cc.ideDocumento " +
                "INNER JOIN cc.corAgenteList ca " +
                "WHERE  lower(ca.nombre)  LIKE lower( :NOMBRE) and cr.consecutivo = :CONSECUTIVO and ca.codTipoRemite = 'EXT'"),
        @NamedQuery(name = "CorRadicado.findNoGuia", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.RadicadoDTO" +
                "(ca.ideAgente, ca.codTipDocIdent, ca.nroDocuIdentidad, ca.nombre, cr.radicadoPadre, cr.consecutivo, ppd.asunto, ca.razonSocial, ca.nit, ca.codEnCalidad, ca.codTipoPers, ppd.ideEcm) " +
                "FROM CorRadicado cr INNER JOIN CorCorrespondencia cc ON cr.id = cc.corRadicado.id " +
                //"JOIN ca.corCorrespondencia cc ON ca.corCorrespondencia.ideDocumento = cc.ideDocumento " +
                "INNER JOIN PpdDocumento ppd ON ppd.corCorrespondencia.ideDocumento = cc.ideDocumento " +
                "INNER JOIN cc.corAgenteList ca " +
                "WHERE cc.nroGuia LIKE :NO_GUIA and cr.consecutivo = :CONSECUTIVO and ca.codTipoRemite = 'EXT'"),
        @NamedQuery(name = "CorRadicado.findAnnoRadicados", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.RadicadoDTO" +
                "(ca.ideAgente, ca.codTipDocIdent, ca.nroDocuIdentidad, ca.nombre, cc.corRadicado.radicadoPadre, cc.corRadicado.consecutivo, ppd.asunto, ca.razonSocial, ca.nit, ca.codEnCalidad, ca.codTipoPers, ppd.ideEcm) " +
                "FROM CorCorrespondencia cc " +
                "INNER JOIN PpdDocumento ppd ON ppd.corCorrespondencia.ideDocumento = cc.ideDocumento " +
                "INNER JOIN cc.corAgenteList ca " +
                "WHERE cc.corRadicado.fechaRadicacion >= :ANNO_RADICADO and cc.corRadicado.consecutivo = :CONSECUTIVO and ca.codTipoRemite = 'EXT'"),
        @NamedQuery(name = "CorRadicado.findFechRadByNumRad", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.RadicadoDTO(cr.fechaRadicacion)" +
                "FROM CorRadicado cr WHERE cr.nroRadicado LIKE :NUM_RAD"),
        @NamedQuery(name = "CorRadicado.findRadicado", query = "SELECT cr " +
                "FROM CorRadicado cr WHERE cr.nroRadicado LIKE :NUM_RAD"),
        @NamedQuery(name = "CorRadicado.findRadicadoPadre", query = "SELECT cr " +
                "FROM CorRadicado cr WHERE cr.radicadoPadre = :NUM_RAD"),
})
@javax.persistence.TableGenerator(name = "COR_RADICADO_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_RADICADO_SEQ", allocationSize = 1)
public class CorRadicado implements Serializable {

    private static long serialVersionUID = 1888324L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_RADICADO_GENERATOR")
    @Column(name = "ID")
    private BigInteger id;

    @Column(name = "RADICADO_PADRE")
    @Basic
    private BigInteger radicadoPadre;
    @Basic
    @Column(name = "CONSECUTIVO")
    private BigInteger consecutivo;

    @Column(name = "FECHA_RADICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRadicacion;

    @Basic
    @Column(name = "NRO_RADICADO")
    private String nroRadicado;

    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne
    private CorAgente corAgente;

    @PrePersist
    protected void onCreate() {
        if(fechaRadicacion == null)
        fechaRadicacion = new Date();
    }
}
