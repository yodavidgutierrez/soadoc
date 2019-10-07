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
@Getter
@Setter
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DCT_ASIG_ULTIMO")
@NamedQueries({
        @NamedQuery(name = "DctAsigUltimo.findAll", query = "SELECT d FROM DctAsigUltimo d"),
        @NamedQuery(name = "DctAsigUltimo.findByIdeAgente", query = "SELECT NEW co.com.soaint.correspondencia.domain.entity.DctAsigUltimo " +
                "(d.ideAsigUltimo, d.ideUsuarioCreo, d.fecCreo, d.nivLectura, " +
                "d.nivEscritura, d.fechaVencimiento, d.idInstancia, d.codTipProceso, " +
                "d.numRedirecciones, d.numDevoluciones) " +
                "FROM DctAsigUltimo d WHERE d.corAgente.ideAgente = :IDE_AGENTE "),
        @NamedQuery(name = "DctAsigUltimo.findByIdeAsignacion", query = "SELECT NEW co.com.soaint.correspondencia.domain.entity.DctAsigUltimo " +
                "(d.ideAsigUltimo, d.ideUsuarioCreo, d.fecCreo, d.nivLectura, " +
                "d.nivEscritura, d.fechaVencimiento, d.idInstancia, d.codTipProceso, " +
                "d.numRedirecciones, d.numDevoluciones) " +
                "FROM DctAsigUltimo d WHERE d.dctAsignacion.ideAsignacion = :IDE_ASIGNACION "),
        @NamedQuery(name = "DctAsigUltimo.findByIdeFunciAndNroRadicado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO " +
                "(a.ideAsignacion, a.fecAsignacion, a.ideFunci, a.codDependencia, a.codTipAsignacion, a.observaciones, a.codTipCausal, a.codTipProceso, " +
                "d.ideAsigUltimo, d.nivLectura, d.nivEscritura, d.fechaVencimiento, d.idInstancia, " +
                "g.ideAgente, " +
                "c.ideDocumento, c.corRadicado.nroRadicado, c.corRadicado.fechaRadicacion) " +
                "FROM DctAsigUltimo d " +
                "INNER JOIN d.dctAsignacion a " +
                "INNER JOIN d.corAgente g " +
                "INNER JOIN d.corCorrespondencia c " +
                "WHERE a.ideFunci = :IDE_FUNCI AND d.idInstancia IS NULL AND (:NRO_RADICADO IS NULL OR c.corRadicado.nroRadicado LIKE :NRO_RADICADO)"),
        @NamedQuery(name = "DctAsigUltimo.findByIdeAgenteAndCodEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO " +
                "(a.ideAsignacion, a.fecAsignacion, a.ideFunci, a.codDependencia, a.codTipAsignacion, a.observaciones, a.codTipCausal, a.codTipProceso, " +
                "d.ideAsigUltimo, d.nivLectura, d.nivEscritura, d.fechaVencimiento, d.idInstancia, " +
                "g.ideAgente, " +
                "c.ideDocumento, c.corRadicado.nroRadicado, c.corRadicado.fechaRadicacion) " +
                "FROM DctAsigUltimo d " +
                "INNER JOIN d.dctAsignacion a " +
                "INNER JOIN d.corAgente g " +
                "INNER JOIN d.corCorrespondencia c " +
                "WHERE g.ideAgente = :IDE_AGENTE AND g.codEstado = :COD_ESTADO "),
        @NamedQuery(name = "DctAsigUltimo.consultarByIdeAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO " +
                "(a.ideAsignacion, a.fecAsignacion, a.ideFunci, a.codDependencia, a.codTipAsignacion, a.observaciones, a.codTipCausal, a.codTipProceso, " +
                "d.ideAsigUltimo, d.nivLectura, d.nivEscritura, d.fechaVencimiento, d.idInstancia, " +
                "g.ideAgente, " +
                "c.ideDocumento, c.corRadicado.nroRadicado, c.corRadicado.fechaRadicacion) " +
                "FROM DctAsigUltimo d " +
                "INNER JOIN d.dctAsignacion a " +
                "INNER JOIN d.corAgente g " +
                "INNER JOIN d.corCorrespondencia c " +
                "WHERE g.ideAgente = :IDE_AGENTE "),
        @NamedQuery(name = "DctAsigUltimo.updateIdInstancia", query = "UPDATE DctAsigUltimo d " +
                "SET d.idInstancia = :ID_INSTANCIA " +
                "WHERE d.ideAsigUltimo = :IDE_ASIG_ULTIMO"),
        @NamedQuery(name = "DctAsigUltimo.updateTipoProceso", query = "UPDATE DctAsigUltimo d " +
                "SET d.codTipProceso = :COD_TIPO_PROCESO " +
                "WHERE d.ideAsigUltimo = :IDE_ASIG_ULTIMO"),
        @NamedQuery(name = "DctAsigUltimo.updateNumRedirecciones", query = "UPDATE DctAsigUltimo d " +
                "SET d.numRedirecciones = d.numRedirecciones + 1 " +
                "WHERE d.ideAsigUltimo = :IDE_ASIG_ULTIMO"),
        @NamedQuery(name = "DctAsigUltimo.updateNumDevoluciones", query = "UPDATE DctAsigUltimo d " +
                "SET d.numDevoluciones = d.numDevoluciones + 1 " +
                "WHERE d.ideAsigUltimo = :IDE_ASIG_ULTIMO")})
@javax.persistence.TableGenerator(name = "DCT_ASIG_ULTIMO_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "DCT_ASIG_ULTIMO_SEQ", allocationSize = 1)
public class DctAsigUltimo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DCT_ASIG_ULTIMO_GENERATOR")
    @Column(name = "IDE_ASIG_ULTIMO")
    private BigInteger ideAsigUltimo;
    @Basic
    @Column(name = "IDE_USUARIO_CREO")
    private BigInteger ideUsuarioCreo;
    @Basic
    @Column(name = "FEC_CREO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreo;
    @Basic
    @Column(name = "IDE_USUARIO_CAMBIO")
    private BigInteger ideUsuarioCambio;
    @Basic
    @Column(name = "FEC_CAMBIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCambio;
    @Column(name = "NIV_LECTURA")
    private Short nivLectura;
    @Column(name = "NIV_ESCRITURA")
    private Short nivEscritura;
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
    @Column(name = "ID_INSTANCIA")
    private String idInstancia;
    @Column(name = "COD_TIP_PROCESO")
    private String codTipProceso;
    @Column(name = "NUM_REDIRECCIONES")
    private Long numRedirecciones;
    @Column(name = "NUM_DEVOLUCIONES")
    private Long numDevoluciones;
    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne
    private CorAgente corAgente;

    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne
    private CorCorrespondencia corCorrespondencia;

    @JoinColumn(name = "IDE_ASIGNACION", referencedColumnName = "IDE_ASIGNACION")
    @ManyToOne
    private DctAsignacion dctAsignacion;

    /**
     * @param ideAsigUltimo
     * @param ideUsuarioCreo
     * @param fecCreo
     * @param nivLectura
     * @param nivEscritura
     * @param fechaVencimiento
     * @param idInstancia
     * @param codTipProceso
     * @param numRedirecciones
     * @param numDevoluciones
     */
    public DctAsigUltimo(BigInteger ideAsigUltimo, BigInteger ideUsuarioCreo, Date fecCreo, Short nivLectura,
                         Short nivEscritura, Date fechaVencimiento, String idInstancia, String codTipProceso,
                         Long numRedirecciones, Long numDevoluciones) {
        this.ideAsigUltimo = ideAsigUltimo;
        this.ideUsuarioCreo = ideUsuarioCreo;
        this.fecCreo = fecCreo;
        this.nivLectura = nivLectura;
        this.nivEscritura = nivEscritura;
        this.fechaVencimiento = fechaVencimiento;
        this.idInstancia = idInstancia;
        this.codTipProceso = codTipProceso;
        this.numRedirecciones = numRedirecciones;
        this.numDevoluciones = numDevoluciones;

    }

    @PrePersist
    protected void onCreate() {
        fecCreo = new Date();
        fecCambio = fecCreo;
        numRedirecciones = 0L;
        numDevoluciones = 0L;
    }

    @PreUpdate
    protected void onUpdate() {
        fecCambio = new Date();
    }

}
