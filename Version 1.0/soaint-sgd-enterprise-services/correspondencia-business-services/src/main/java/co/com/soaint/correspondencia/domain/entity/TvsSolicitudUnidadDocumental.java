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
@Table(name = "TVS_SOLICITUD_UD")
@NamedQueries({
        @NamedQuery(name = "TvsSolicitudUM.findAll", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO" +
                " (t.ideSolicitud, t.id, t.idConstante, t.fecHora, t.nombreUD, t.descriptor1, t.descriptor2, t.nro, t.codSerie, t.codSubserie, t.codSede," +
                " t.codDependencia, t.idSolicitante, t.estado, t.accion, t.observaciones, t.motivo, t.nombreSerie, t.nombreSubSerie)" +
                " FROM TvsSolicitudUnidadDocumental t"),
        @NamedQuery(name = "TvsSolicitudUM.countAll", query = "SELECT COUNT(ts)" +
                " FROM TvsSolicitudUnidadDocumental ts"),
        @NamedQuery(name = "TvsSolicitudUM.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO " +
                "(t.ideSolicitud, t.id, t.idConstante, t.fecHora, t.nombreUD, t.descriptor1, t.descriptor2, t.nro, t.codSerie, t.codSubserie, t.codSede," +
                " t.codDependencia, t.idSolicitante, t.estado, t.accion, t.observaciones, t.motivo, t.nombreSerie, t.nombreSubSerie)" +
                "FROM TvsSolicitudUnidadDocumental t " +
                "WHERE (:COD_DEP IS NULL OR t.codDependencia = :COD_DEP) " +
                "AND (:COD_SEDE IS NULL OR t.codSede = :COD_SEDE) " +
                "AND ((:FECHA_INI IS NULL OR t.fecHora >= :FECHA_INI) AND (:FECHA_FIN IS NULL OR t.fecHora <= :FECHA_FIN))"),
        @NamedQuery(name = "TvsSolicitudUM.obtenerSolicitudUnidadDocumentalSedeDependenciaSolicitante", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO " +
                "(t.ideSolicitud, t.id, t.idConstante, t.fecHora, t.nombreUD, t.descriptor1, t.descriptor2, t.nro, t.codSerie, t.codSubserie, t.codSede," +
                " t.codDependencia, t.idSolicitante, t.estado, t.accion, t.observaciones, t.motivo, t.nombreSerie, t.nombreSubSerie)" +
                "FROM TvsSolicitudUnidadDocumental t " +
                "WHERE TRIM(t.codDependencia) = :COD_DEP AND TRIM(t.codSede) = :COD_SEDE AND TRIM(t.idSolicitante) = :ID_SOL AND t.accion IS NOT NULL"),
        @NamedQuery(name = "TvsSolicitudUM.obtenerSolicitudUnidadDocumentalSedeDependenciaSolicitanteSinTramitar", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO " +
                "(t.ideSolicitud, t.id, t.idConstante, t.fecHora, t.nombreUD, t.descriptor1, t.descriptor2, t.nro, t.codSerie, t.codSubserie, t.codSede," +
                " t.codDependencia, t.idSolicitante, t.estado, t.accion, t.observaciones, t.motivo, t.nombreSerie, t.nombreSubSerie)" +
                "FROM TvsSolicitudUnidadDocumental t " +
                "WHERE TRIM(t.codDependencia) = :COD_DEP AND TRIM(t.codSede) = :COD_SEDE " +
                "AND TRIM(t.idSolicitante) = :ID_SOL AND t.accion IS NULL"),
        //"AND ((:FECHA_INI IS NULL OR t.fecHora >= :FECHA_INI) AND (:FECHA_FIN IS NULL OR t.fecHora <= :FECHA_FIN))"),
        @NamedQuery(name = "TvsSolicitudUM.actualizarSolicitudUnidadDocumental", query = "UPDATE TvsSolicitudUnidadDocumental t " +
                "SET t.id = :ID, t.idConstante = :ID_CONST , t.fecHora = :FECH, t.nombreUD = :NOMBREUD, t.descriptor1 = :DESCP1, " +
                "t.descriptor2 = :DESCP2, t.nro = :NRO, t.codSerie = :COD_SER, t.codSubserie = :COD_SUBS, t.codSede = :COD_SED, t.codDependencia = :COD_DEP, " +
                "t.idSolicitante = :ID_SOL, t.estado = :EST , t.accion = :ACC , t.observaciones = :OBS, t.motivo= :MOT " +
                "WHERE t.ideSolicitud = :IDE_SOL"),
        @NamedQuery(name = "TvsSolicitudUM.countByIdSolicitud", query = "SELECT COUNT(t) " +
                "FROM TvsSolicitudUnidadDocumental t " +
                "WHERE t.ideSolicitud = :IDE_SOL"),
        @NamedQuery(name = "TvsSolicitudUM.countByNombreUDSolicitud", query = "SELECT COUNT(t) " +
                "FROM TvsSolicitudUnidadDocumental t " +
                "WHERE TRIM(t.nombreUD) = TRIM(:NOM_UD)"),
        @NamedQuery(name = "TvsSolicitudUM.countByIdNombreUDSolicitud", query = "SELECT COUNT(t) " +
                "FROM TvsSolicitudUnidadDocumental t " +
                "WHERE TRIM(t.nombreUD) = TRIM(:NOM_UD)" +
                "AND TRIM(t.ideSolicitud) = TRIM(:IDE_SOL)")
})
@javax.persistence.TableGenerator(name = "COR_SOLICITUD_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_SOLICITUD_SEQ", allocationSize = 1)
public class TvsSolicitudUnidadDocumental implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_SOLICITUD_GENERATOR")
    @Column(name = "ID")
    private BigInteger ideSolicitud;

    @Basic
    @Column(name = "ID_UM")
    private String id;

    @Column(name = "ID_CONSTANTE")
    private String idConstante;

    @Column(name = "FEC_HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecHora;

    @Basic
    @Column(name = "NOMBRE_UNIDAD_DOCUMENTAL")
    private String nombreUD;

    @Basic
    @Column(name = "DESCRIPTOR1")
    private String descriptor1;

    @Basic
    @Column(name = "DESCRIPTOR2")
    private String descriptor2;

    @Column(name = "NRO")
    private String nro;

    @Basic
    @Column(name = "CODIGO_SERIE")
    private String codSerie;

    @Basic
    @Column(name = "NOMBRE_SERIE")
    private String nombreSerie;

    @Basic
    @Column(name = "CODIGO_SUBSERIE")
    private String codSubserie;

    @Basic
    @Column(name = "NOMBRE_SUBSERIE")
    private String nombreSubSerie;

    @Basic
    @Column(name = "CODIGO_SEDE")
    private String codSede;

    @Column(name = "CODIGO_DEPENDENCIA")
    @Basic
    private String codDependencia;

    @Basic
    @Column(name = "ID_SOLICITANTE")
    private String idSolicitante;

    @Basic
    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "ACCION")
    private String accion;

    @Column(name = "OBSERVACIONES")
    private String observaciones;

    @Column(name = "MOTIVO")
    private String motivo;
}
