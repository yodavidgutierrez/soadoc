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
@Table(name = "COR_AGENTE")
@Log4j2
@NamedQueries({
        @NamedQuery(name = "CorAgente.findAll", query = "SELECT c FROM CorAgente c"),
        @NamedQuery(name = "CorAgente.findByNroIdentificacion", query = "SELECT c FROM CorAgente c WHERE c.nroDocuIdentidad = :NRO_IDENTIDAD"),
        @NamedQuery(name = "CorAgente.findByIdeDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "WHERE cor.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorAgente.findByIdeDocumentoObj", query = "SELECT c " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "WHERE cor.ideDocumento = :IDE_DOCUMENTO and c.codTipoRemite = 'INT' and c.indOriginal = 'TP-DESP' "),
        @NamedQuery(name = "CorAgente.findAgentByIdeDocumentoAndTipoRemitente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "WHERE cor.ideDocumento = :IDE_DOCUMENTO and c.codTipoRemite =:TIPO_REM "),
        @NamedQuery(name = "CorAgente.findAgentByIdeDocumentoAndTipoAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "WHERE cor.ideDocumento = :IDE_DOCUMENTO and c.codTipAgent = :TIPO_AGEN "),
        @NamedQuery(name = "CorAgente.findByNroIdentificacionAndTipPers", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c " +
                "WHERE c.nroDocuIdentidad = :NRO_IDENTIDAD AND c.codTipoPers = 'TP-PERPN'"),
        @NamedQuery(name = "CorAgente.findByNITAndTipPers", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c " +
                "WHERE c.nit = :NIT AND c.codTipoPers = 'TP-PERPJ'"),
        @NamedQuery(name = "CorAgente.findByNroRadicado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "WHERE cor.corRadicado.nroRadicado = :NRO_RADICADO AND c.codTipAgent = 'TP-AGEE'"),
        @NamedQuery(name = "CorAgente.findByIdeAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "WHERE c.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "CorAgente.findByIdeDocumentoAndCodDependenciaAndCodEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "WHERE (:COD_ESTADO IS NULL OR c.codEstado = :COD_ESTADO) AND c.codDependencia = :COD_DEPENDENCIA AND c.codTipAgent = :COD_TIP_AGENT " +
                "AND cor.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorAgente.findDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal, dau.numRedirecciones, dau.numDevoluciones) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "INNER JOIN c.dctAsigUltimoList dau " +
                "WHERE (:COD_ESTADO IS NULL OR c.codEstado = :COD_ESTADO) AND  c.codDependencia like :COD_DEPENDENCIA AND c.codTipoRemite = :COD_TIP_REM " +
                "AND cor.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorAgente.findDestinatariosByIdeDocumentoAndCodDependencia", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal, dau.numRedirecciones, dau.numDevoluciones) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "INNER JOIN c.dctAsigUltimoList dau " +
                "WHERE c.codDependencia = :COD_DEPENDENCIA AND c.codTipAgent = :COD_TIP_AGENT " +
                "AND cor.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorAgente.findDestinatariosByIdeDocumentoAndCodTipoAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal, dau.numRedirecciones, dau.numDevoluciones) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "INNER JOIN c.dctAsigUltimoList dau " +
                "WHERE c.codTipAgent = :COD_TIP_AGENT " +
                "AND cor.ideDocumento = :IDE_DOCUMENTO"),

        @NamedQuery(name = "CorAgente.listarAgentesPorIdDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "WHERE cor.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorAgente.ObtenerAgenteTPAGEIPorIdDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "WHERE cor.ideDocumento = :IDE_DOCUMENTO and c.codTipAgent = 'TP-AGEI' "),
        @NamedQuery(name = "CorAgente.findDestinatariosByIdeDocumentoAndCodTipoAgenteMail", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "WHERE c.codTipAgent = :COD_TIP_AGENT " +
                "AND cor.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorAgente.findRemitentesByIdeDocumentoAndCodTipoAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor " +
                "WHERE c.codTipAgent = :COD_TIP_AGENT " +
                "AND cor.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorAgente.findAgentePrincipalByIdeDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.ideFunci, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c " +
                "INNER JOIN c.corCorrespondenciaList cor inner join c.tvsDatosContactoList dc " +
                "WHERE  dc.principal = 'P' " +
                "AND cor.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorAgente.countByIdeAgente", query = "SELECT COUNT(c) " +
                "FROM CorAgente c " +
                "WHERE c.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "CorAgente.updateAsignacion", query = "UPDATE CorAgente c " +
                "SET c.fecAsignacion = :FECHA_ASIGNACION, c.codEstado = :COD_ESTADO " +
                "WHERE c.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "CorAgente.redireccionarCorrespondencia", query = "UPDATE CorAgente c " +
                "SET c.codSede = :COD_SEDE, c.codDependencia = :COD_DEPENDENCIA, " +
                "c.codEstado = :COD_ESTADO, c.estadoDistribucion = :ESTADO_DISTRIBUCION " +
                "WHERE c.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "CorAgente.updateEstado", query = "UPDATE CorAgente c " +
                "SET c.codEstado = :COD_ESTADO " +
                "WHERE c.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "CorAgente.updateEstadoDistribucion", query = "UPDATE CorAgente c " +
                "SET c.estadoDistribucion = :ESTADO_DISTRIBUCION " +
                "WHERE c.ideAgente = :IDE_AGENTE")})
@javax.persistence.TableGenerator(name = "COR_AGENTE_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_AGENTE_SEQ", allocationSize = 1)
public class CorAgente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_AGENTE_GENERATOR")
    @Column(name = "IDE_AGENTE")
    private BigInteger ideAgente;

    @Column(name = "COD_TIPO_REMITE")
    private String codTipoRemite;

    @Column(name = "COD_TIPO_PERS")
    private String codTipoPers;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "RAZON_SOCIAL")
    private String razonSocial;

    @Column(name = "NIT")
    private String nit;

    @Column(name = "COD_CORTESIA")
    private String codCortesia;

    @Column(name = "COD_EN_CALIDAD")
    private String codEnCalidad;

    @Column(name = "IDE_FUNCI")
    private BigInteger ideFunci;

    @Column(name = "COD_TIP_DOC_IDENT")
    private String codTipDocIdent;

    @Column(name = "NRO_DOCU_IDENTIDAD")
    private String nroDocuIdentidad;

    @Column(name = "COD_SEDE")
    private String codSede;

    @Column(name = "COD_DEPENDENCIA")
    private String codDependencia;

    @Column(name = "COD_ESTADO")
    private String codEstado;

    @Column(name = "FEC_ASIGNACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecAsignacion;

    @Column(name = "COD_TIP_AGENT")
    private String codTipAgent;

    @Column(name = "IND_ORIGINAL")
    private String indOriginal;

    @Column(name = "ESTADO_DISTRIBUCION")
    private String estadoDistribucion;

    @Column(name = "FEC_CREACION", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;

    @ManyToMany(mappedBy = "corAgenteList", cascade = CascadeType.ALL)
    private Set<CorCorrespondencia> corCorrespondenciaList = new HashSet<>();

    @OneToMany(mappedBy = "corAgente", orphanRemoval = true)
    private List<DctAsignacion> dctAsignacionList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corAgente", orphanRemoval = true)
    private List<CorPlanAgen> corPlanAgenList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corAgente", orphanRemoval = true)
    private List<TvsDatosContacto> tvsDatosContactoList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corAgente", orphanRemoval = true)
    private List<DctAsigUltimo> dctAsigUltimoList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corAgente", orphanRemoval = true)
    private List<CorRadicado> corRadicadoList = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fecCreacion = new Date();
    }

    public void addDatosContacto(TvsDatosContacto tvsDatosContacto) {
        if (null == tvsDatosContactoList) {
            tvsDatosContactoList = new ArrayList<>();
        }
        tvsDatosContactoList.add(tvsDatosContactoList.size(), tvsDatosContacto);
        tvsDatosContacto.setCorAgente(this);
    }

    public void addAsignacion(DctAsignacion dctAsignacion) {
        if (null == dctAsignacionList) {
            dctAsignacionList = new ArrayList<>();
        }
        dctAsignacionList.add(dctAsignacionList.size(), dctAsignacion);
        dctAsignacion.setCorAgente(this);
    }

    public void addAsignacionUltimo(DctAsigUltimo dctAsigUltimo) {
        if (null == dctAsigUltimoList) {
            dctAsigUltimoList = new ArrayList<>();
        }
        dctAsigUltimoList.add(dctAsigUltimoList.size(), dctAsigUltimo);
        dctAsigUltimo.setCorAgente(this);
    }

    public void addCorRadicado(CorRadicado corRadicado) {
        if (null == corRadicadoList) {
            corRadicadoList = new ArrayList<>();
        }
        corRadicadoList.add(corRadicadoList.size(), corRadicado);
        corRadicado.setCorAgente(this);
    }
}