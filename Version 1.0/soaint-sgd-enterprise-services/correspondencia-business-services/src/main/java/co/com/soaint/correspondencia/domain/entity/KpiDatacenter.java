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
        @NamedQuery(name = "KpiDatacenter.findByKpiNumberAndDateTime",
                query = "SELECT kpi FROM KpiDatacenter kpi " +
                        "WHERE kpi.dataX01 = :KPI_NUMBER " +
                        "AND kpi.fechaHora >= :START_DATE AND kpi.fechaHora <= :END_DATE"),
        @NamedQuery(name = "KpiDatacenter.findMaxId", query = "SELECT max(kpi.datacenterId) FROM KpiDatacenter kpi")
})
@Table(name = "KPI_DATACENTER")
public class KpiDatacenter implements Serializable, Cloneable {

    private static final Long serialVersionUID = 6546313213154L;

    @Id
    @Column(name = "KPI_DATACENTER_IDE", updatable = false, nullable = false)
    private BigInteger datacenterId;

    @Column(name = "KPI_NOMBRE")
    private String nombre;

    @Column(name = "KPI_PROCESO")
    private String procesoNombre;

    @Column(name = "KPI_TAREA")
    private String tareaNombre;

    @Column(name = "KPI_COD_USUARIO")
    private String codigoUsuario;

    @Basic
    @Column(name = "KPI_FECHA_HORA", nullable = false)
    private Timestamp fechaHora;

    @Column(name = "KPI_DATA_X01")
    private String dataX01;

    @Column(name = "KPI_DATA_X02")
    private String dataX02;

    @Column(name = "KPI_DATA_X03")
    private String dataX03;

    @Column(name = "KPI_DATA_X04")
    private String dataX04;

    @Column(name = "KPI_DATA_X05")
    private String dataX05;

    @Column(name = "KPI_DATA_X06")
    private String dataX06;

    @Column(name = "KPI_DATA_X07")
    private String dataX07;

    @Column(name = "KPI_DATA_X08")
    private String dataX08;

    @Column(name = "KPI_DATA_X09")
    private String dataX09;

    @Column(name = "KPI_DATA_X10")
    private String dataX10;

    @Column(name = "KPI_DATA_N01")
    private BigInteger dataN01;

    @Column(name = "KPI_DATA_N02")
    private BigInteger dataN02;

    @Column(name = "KPI_DATA_N03")
    private BigInteger dataN03;

    @Column(name = "KPI_DATA_N04")
    private BigInteger dataN04;

    @Column(name = "KPI_DATA_N05")
    private BigInteger dataN05;

    @Column(name = "KPI_DATA_N06")
    private BigInteger dataN06;

    @Column(name = "KPI_DATA_N07")
    private BigInteger dataN07;

    @Column(name = "KPI_DATA_N08")
    private BigInteger dataN08;

    @Column(name = "KPI_DATA_N09")
    private BigInteger dataN09;

    @Column(name = "KPI_DATA_N10")
    private BigInteger dataN10;

    @Basic
    @Column(name = "KPI_DATA_TS01")
    private Timestamp dataTS01;

    @Basic
    @Column(name = "KPI_DATA_TS02")
    private Timestamp dataTS02;

    @Basic
    @Column(name = "KPI_DATA_TS03")
    private Timestamp dataTS03;

    @Basic
    @Column(name = "KPI_DATA_TS04")
    private Timestamp dataTS04;

    @Basic
    @Column(name = "KPI_DATA_TS05")
    private Timestamp dataTS05;

    @Basic
    @Column(name = "KPI_DATA_TS06")
    private Timestamp dataTS06;

    @Basic
    @Column(name = "KPI_DATA_TS07")
    private Timestamp dataTS07;

    @Basic
    @Column(name = "KPI_DATA_TS08")
    private Timestamp dataTS08;

    @Basic
    @Column(name = "KPI_DATA_TS09")
    private Timestamp dataTS09;

    @Basic
    @Column(name = "KPI_DATA_TS10")
    private Timestamp dataTS10;

    @PrePersist
    protected void onCreate() {
        fechaHora = Timestamp.valueOf(LocalDateTime.now());
    }

    @PreUpdate
    protected void onUpdate() {
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
