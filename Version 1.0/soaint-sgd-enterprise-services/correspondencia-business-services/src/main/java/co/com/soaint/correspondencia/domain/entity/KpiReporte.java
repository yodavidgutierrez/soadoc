package co.com.soaint.correspondencia.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(name = "KpiReporte.findByUdIDAndDocNameAndDepCode",
                query = "SELECT kpi FROM KpiReporte kpi " +
                        "WHERE kpi.dataX01 = :REP_NUMBER AND kpi.dataX08 = :UD_ID " +
                        "AND kpi.dataX03 = :DOC_NAME AND kpi.dataX04 = :DEP_CODE"),
        @NamedQuery(name = "KpiReporte.findByUdIDAndAndDepCode",
                query = "SELECT kpi FROM KpiReporte kpi " +
                        "WHERE kpi.dataX01 = :REP_NUMBER AND kpi.dataX06 = :UD_ID " +
                        "AND kpi.dataX02 = :DEP_CODE"),
        @NamedQuery(name = "KpiReporte.findMaxId", query = "SELECT max(kpi.reporteId) FROM KpiReporte kpi")
})
@Table(name = "KPI_REPORTE")
public class KpiReporte implements Serializable, Cloneable {

    private static final Long serialVersionUID = 4563453453454L;

    @Id
    @Column(name = "REP_IDE", updatable = false, nullable = false)
    private BigInteger reporteId;

    @Basic
    @Column(name = "REP_NOMBRE", nullable = false)
    private String nombre;

    @Basic
    @Column(name = "REP_PROCESO", nullable = false)
    private String procesoNombre;

    @Basic
    @Column(name = "REP_FECHA_HORA", nullable = false)
    private Timestamp fechaHora;

    @Column(name = "REP_DATA_X01")
    private String dataX01;

    @Column(name = "REP_DATA_X02")
    private String dataX02;

    @Column(name = "REP_DATA_X03")
    private String dataX03;

    @Column(name = "REP_DATA_X04")
    private String dataX04;

    @Column(name = "REP_DATA_X05")
    private String dataX05;

    @Column(name = "REP_DATA_X06")
    private String dataX06;

    @Column(name = "REP_DATA_X07")
    private String dataX07;

    @Column(name = "REP_DATA_X08")
    private String dataX08;

    @Column(name = "REP_DATA_X09")
    private String dataX09;

    @Column(name = "REP_DATA_X10")
    private String dataX10;

    @Column(name = "REP_DATA_X11")
    private String dataX11;

    @Column(name = "REP_DATA_X12")
    private String dataX12;

    @Column(name = "REP_DATA_X13")
    private String dataX13;

    @Column(name = "REP_DATA_X14")
    private String dataX14;

    @Column(name = "REP_DATA_X15")
    private String dataX15;

    @Column(name = "REP_DATA_N01")
    private BigInteger dataN01;

    @Column(name = "REP_DATA_N02")
    private BigInteger dataN02;

    @Column(name = "REP_DATA_N03")
    private BigInteger dataN03;

    @Column(name = "REP_DATA_N04")
    private BigInteger dataN04;

    @Column(name = "REP_DATA_N05")
    private BigInteger dataN05;

    @Column(name = "REP_DATA_N06")
    private BigInteger dataN06;

    @Column(name = "REP_DATA_N07")
    private BigInteger dataN07;

    @Column(name = "REP_DATA_N08")
    private BigInteger dataN08;

    @Column(name = "REP_DATA_N09")
    private BigInteger dataN09;

    @Column(name = "REP_DATA_N10")
    private BigInteger dataN10;

    @Column(name = "REP_DATA_N11")
    private BigInteger dataN11;

    @Column(name = "REP_DATA_N12")
    private BigInteger dataN12;

    @Column(name = "REP_DATA_N13")
    private BigInteger dataN13;

    @Column(name = "REP_DATA_N14")
    private BigInteger dataN14;

    @Column(name = "REP_DATA_N15")
    private BigInteger dataN15;

    @Basic
    @Column(name = "REP_DATA_TS01")
    private Timestamp dataTS01;

    @Basic
    @Column(name = "REP_DATA_TS02")
    private Timestamp dataTS02;

    @Basic
    @Column(name = "REP_DATA_TS03")
    private Timestamp dataTS03;

    @Basic
    @Column(name = "REP_DATA_TS04")
    private Timestamp dataTS04;

    @Basic
    @Column(name = "REP_DATA_TS05")
    private Timestamp dataTS05;

    @Basic
    @Column(name = "REP_DATA_TS06")
    private Timestamp dataTS06;

    @Basic
    @Column(name = "REP_DATA_TS07")
    private Timestamp dataTS07;

    @Basic
    @Column(name = "REP_DATA_TS08")
    private Timestamp dataTS08;

    @Basic
    @Column(name = "REP_DATA_TS09")
    private Timestamp dataTS09;

    @Basic
    @Column(name = "REP_DATA_TS10")
    private Timestamp dataTS10;

    @Basic
    @Column(name = "REP_DATA_TS11")
    private Timestamp dataTS11;

    @Basic
    @Column(name = "REP_DATA_TS12")
    private Timestamp dataTS12;

    @Basic
    @Column(name = "REP_DATA_TS13")
    private Timestamp dataTS13;

    @Basic
    @Column(name = "REP_DATA_TS14")
    private Timestamp dataTS14;

    @Basic
    @Column(name = "REP_DATA_TS15")
    private Timestamp dataTS15;

    @PrePersist
    @PreUpdate
    protected void onTransactional() {
        fechaHora = Timestamp.valueOf(LocalDateTime.now());
    }

    @Override
    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }
}
