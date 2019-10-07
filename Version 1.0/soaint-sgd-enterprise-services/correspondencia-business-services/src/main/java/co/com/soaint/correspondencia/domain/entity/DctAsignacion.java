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
@Table(name = "DCT_ASIGNACION")
@NamedQueries({
        @NamedQuery(name = "DctAsignacion.findAll", query = "SELECT d FROM DctAsignacion d"),
        @NamedQuery(name = "DctAsignacion.findObjByIdDocumento", query = "SELECT d FROM DctAsignacion d where d.corCorrespondencia.ideDocumento =:ID_DOCUMENTO "),
        @NamedQuery(name = "DctAsignacion.findByIdeAsignacion", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.DctAsignacionDTO " +
                "(d.ideAsignacion, d.fecAsignacion, d.ideFunci, d.codDependencia, d.codTipAsignacion, d.observaciones, d.codTipCausal, d.codTipProceso)" +
                "FROM DctAsignacion d " +
                "WHERE d.ideAsignacion = :IDE_ASIGNACION"),
        @NamedQuery(name = "DctAsignacion.findByIdeAgente", query = "SELECT distinct NEW co.com.soaint.foundation.canonical.correspondencia.DctAsignacionDTO " +
                "(d.ideAsignacion, d.fecAsignacion, d.ideFunci, d.codDependencia, d.codTipAsignacion, d.observaciones, d.codTipCausal, d.codTipProceso)" +
                "FROM DctAsignacion d " +
                "INNER JOIN d.corAgente ca " +
                "WHERE ca.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "DctAsignacion.findObservacionByIdDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO " +
                "( d.fecAsignacion, d.ideFunci, d.codDependencia, d.observaciones)" +
                "FROM DctAsignacion d " +
                "INNER JOIN d.corCorrespondencia c " +
                "WHERE c.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "DctAsignacion.findByIdeAsigUltimo", query = "SELECT NEW co.com.soaint.correspondencia.domain.entity.DctAsignacion " +
                "(d.ideAsignacion, d.fecAsignacion, d.ideFunci, d.codDependencia, d.codTipAsignacion, d.observaciones, " +
                "d.codTipCausal, d.ideUsuarioCreo, d.fecCreo, d.codTipProceso)" +
                "FROM DctAsignacion d " +
                "INNER JOIN d.dctAsigUltimoList dau " +
                "WHERE dau.ideAsigUltimo = :IDE_ASIG_ULTIMO"),
        @NamedQuery(name = "DctAsignacion.asignarToFuncionario", query = "UPDATE DctAsignacion d " +
                "SET d.ideFunci = :IDE_FUNCI " +
                "WHERE d.ideAsignacion = :IDE_ASIGNACION")})
@javax.persistence.TableGenerator(name = "DCT_ASIGNACION_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "DCT_ASIGNACION_SEQ", allocationSize = 1)
public class DctAsignacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DCT_ASIGNACION_GENERATOR")
    @Column(name = "IDE_ASIGNACION")
    private BigInteger ideAsignacion;
    @Basic
    @Column(name = "FEC_ASIGNACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecAsignacion;
    @Column(name = "IDE_FUNCI")
    private BigInteger ideFunci;
    @Column(name = "COD_DEPENDENCIA")
    private String codDependencia;
    @Basic
    @Column(name = "COD_TIP_ASIGNACION")
    private String codTipAsignacion;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Column(name = "COD_TIP_CAUSAL")
    private String codTipCausal;
    @Basic
    @Column(name = "IDE_USUARIO_CREO")
    private BigInteger ideUsuarioCreo;
    @Basic
    @Column(name = "IDE_USUARIO_CAMBIO")
    private BigInteger ideUsuarioCambio;
    @Basic
    @Column(name = "FEC_CREO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreo;
    @Basic
    @Column(name = "FEC_CAMBIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCambio;
    @Column(name = "NIV_LECTURA")
    private BigInteger nivelLectura;
    @Column(name = "NIV_ESCRITURA")
    private BigInteger nivelEscritura;
    @Column(name = "COD_TIP_PROCESO")
    private String codTipProceso;
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecVencimiento;
    @Column(name = "ID_INSTANCIA")
    private String idInstancia;
    @Column(name = "NUM_REDIRECCIONES")
    private BigInteger numeroRedirecciones;
    @Column(name = "NUM_DEVOLUCIONES")
    private BigInteger numeroDevoluciones;

    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne
    private CorAgente corAgente;

    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne
    private CorCorrespondencia corCorrespondencia;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dctAsignacion")
    private List<DctAsigUltimo> dctAsigUltimoList;

    public DctAsignacion(BigInteger ideAsignacion, Date fecAsignacion, BigInteger ideFunci, String codDependencia,
                         String codTipAsignacion, String observaciones, String codTipCausal, BigInteger ideUsuarioCreo,
                         Date fecCreo, String codTipProceso) {
        this.ideAsignacion = ideAsignacion;
        this.fecAsignacion = fecAsignacion;
        this.ideFunci = ideFunci;
        this.codDependencia = codDependencia;
        this.codTipAsignacion = codTipAsignacion;
        this.observaciones = observaciones;
        this.codTipCausal = codTipCausal;
        this.ideUsuarioCreo = ideUsuarioCreo;
        this.fecCreo = fecCreo;
        this.codTipProceso = codTipProceso;
    }

    @PrePersist
    protected void onCreate() {
        fecCreo = new Date();
    }

}
