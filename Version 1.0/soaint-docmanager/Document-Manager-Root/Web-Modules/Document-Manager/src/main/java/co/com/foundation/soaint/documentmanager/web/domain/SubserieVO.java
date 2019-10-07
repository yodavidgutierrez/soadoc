package co.com.foundation.soaint.documentmanager.web.domain;

import co.com.foundation.soaint.documentmanager.web.infrastructure.util.constants.EstadoCaracteristicaEnum;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrodriguez on 20/09/2016.
 */
public class SubserieVO implements Serializable {

    private BigInteger ideSubserie;
    @NotNull
    @NotEmpty
    private String actAdministrativo;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "\\d+", message = "Debe ser un número")
    private String carAdministrativa;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "\\d+", message = "Debe ser un número")
    private String carLegal;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "\\d+", message = "Debe ser un número")
    private String carTecnica;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "\\d+", message = "Debe ser un número")
    private String carJuridica;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "\\d+", message = "Debe ser un número")
    private String carContable;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "\\d+", message = "Debe ser un número")
    private String conPublica;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "\\d+", message = "Debe ser un número")
    private String conClasificada;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "\\d+", message = "Debe ser un número")
    private String conReservada;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "\\d+", message = "Debe ser un número")
    private String codSubserie;
    @NotNull
    @NotEmpty
    private String estSubserie;
    @NotNull
    @NotEmpty
    private String fueBibliografica;
    @NotNull
    @NotEmpty
    private String nomSubserie;
    @NotNull
    @NotEmpty
    private String notAlcance;
    @NotNull
    private BigInteger idMotivo;
    private String nombreMotCreacion;
    @NotNull
    private BigInteger idSerie;
    private String codSerie;
    private String nomSerie;
    private int estSubserieValue;


    private String carConcat;
    private String conConcat;

    public SubserieVO() {
    }

    public SubserieVO(String ideSubserie) {
        if (ideSubserie.equals("")) {
            this.ideSubserie = null;
        } else if (!ideSubserie.equals("null")) {
            this.ideSubserie = BigInteger.valueOf(Long.parseLong(ideSubserie));
        }

    }

    public SubserieVO(BigInteger ideSubserie, String codSerie, String codSubserie, String nomSubserie, String actAdministrativo,
                      BigInteger idMotivo, String nombreMotCreacion, String notAlcance, String fueBibliografica, String estSubserie,
                      String carTecnica, String carLegal, String carAdministrativa, String carJuridica, String carContable,
                      String conPublica, String conClasificada, String conReservada, BigInteger idSerie, String nomSerie, int estSubserieValue) {

        this.ideSubserie = ideSubserie;
        this.actAdministrativo = actAdministrativo;
        this.carAdministrativa = carAdministrativa;
        this.carLegal = carLegal;
        this.carTecnica = carTecnica;
        this.carJuridica = carJuridica;
        this.carContable = carContable;
        this.conPublica = conPublica;
        this.conClasificada = conClasificada;
        this.conReservada = conReservada;
        this.codSubserie = codSubserie;
        this.estSubserie = estSubserie;
        this.fueBibliografica = fueBibliografica;
        this.nomSubserie = nomSubserie == null ? "-N/A-" : nomSubserie;
        this.notAlcance = notAlcance;
        this.nombreMotCreacion = nombreMotCreacion;
        this.idMotivo = idMotivo;
        this.idSerie = idSerie;
        this.codSerie = codSerie;
        this.nomSerie = nomSerie;
        this.estSubserieValue = estSubserieValue;
        concatCaracteristicaSub();
        concatConfidencialdidadSub();
    }

    public SubserieVO(String codSerie, String codSubserie, String nomSubserie, String actAdministrativo,
                      BigInteger idMotivo, String nombreMotCreacion, String notAlcance, String fueBibliografica, String estSubserie,
                      String carTecnica, String carLegal, String carAdministrativa, String carJuridica, String carContable,
                      String conPublica, String conClasificada, String conReservada, BigInteger idSerie, String nomSerie, int estSubserieValue) {

        this.actAdministrativo = actAdministrativo;
        this.carAdministrativa = carAdministrativa;
        this.carLegal = carLegal;
        this.carTecnica = carTecnica;
        this.carJuridica = carJuridica;
        this.carContable = carContable;
        this.conPublica = conPublica;
        this.conClasificada = conClasificada;
        this.conReservada = conReservada;
        this.codSubserie = codSubserie;
        this.estSubserie = estSubserie;
        this.fueBibliografica = fueBibliografica;
        this.nomSubserie = nomSubserie;
        this.notAlcance = notAlcance;
        this.nombreMotCreacion = nombreMotCreacion;
        this.idMotivo = idMotivo;
        this.idSerie = idSerie;
        this.codSerie = codSerie;
        this.nomSerie = nomSerie;
        this.estSubserieValue = estSubserieValue;

    }

    public void concatCaracteristicaSub() {

        carConcat = "";
        List<String> car = new ArrayList<>();

        if (carAdministrativa == "on") {
            car.add("A");
        }
        if (carContable == "on") {
            car.add("C");
        }
        if (carJuridica == "on") {
            car.add("J");
        }
        if (carLegal == "on") {
            car.add("L");
        }
        if (carTecnica == "on") {
            car.add("T");
        }

        carConcat = String.join(", ", car);
    }

    public void concatConfidencialdidadSub() {

        conConcat = "";
        List<String> conf = new ArrayList<>();

        if (conClasificada == "on") {
            conf.add("C");
        }
        if (conPublica == "on") {
            conf.add("P");
        }
        if (conReservada == "on") {
            conf.add("R");
        }
        conConcat = String.join(", ", conf);
    }


    public BigInteger getIdeSubserie() {
        return ideSubserie;
    }

    public String getActAdministrativo() {
        return actAdministrativo;
    }

    public Long getCarAdministrativa() {
        return StringUtils.equals(carAdministrativa, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getCarAdministrativaValue() {
        return carAdministrativa;
    }

    public Long getCarLegal() {
        return StringUtils.equals(carLegal, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getCarLegalValue() {
        return carLegal;
    }

    public Long getCarTecnica() {
        return StringUtils.equals(carTecnica, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getCarTecnicaValue() {
        return carTecnica;
    }

    public Long getCarJuridica() {
        return StringUtils.equals(carJuridica, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getCarJuridicaValue() {
        return carJuridica;
    }

    public Long getCarContable() {
        return StringUtils.equals(carContable, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getCarContableValue() {
        return carContable;
    }

    public Long getConPublica() {
        return StringUtils.equals(conPublica, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getConPublicaValue() {
        return conPublica;
    }

    public Long getConClasificada() {
        return StringUtils.equals(conClasificada, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getConClasificadaValue() {
        return conClasificada;
    }

    public Long getConReservada() {
        return StringUtils.equals(conReservada, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getConReservadaValue() {
        return conReservada;
    }


    public String getCodSubserie() {
        return codSubserie;
    }

    public String getEstSubserie() {
        return estSubserie;
    }

    public String getNomSubserie() {
        return nomSubserie;
    }

    public String getFueBibliografica() {
        return fueBibliografica;
    }

    public String getNotAlcance() {
        return notAlcance;
    }

    public BigInteger getIdMotivo() {
        return idMotivo;
    }

    public String getNombreMotCreacion() {
        return nombreMotCreacion;
    }

    public BigInteger getIdSerie() {
        return idSerie;
    }

    public String getCodSerie() {
        return codSerie;
    }

    public String getNomSerie() {
        return nomSerie;
    }

    public int getEstSubserieValue() {
        return estSubserieValue;
    }

    public String getCarConcat() {
        return carConcat;
    }

    public String getConConcat() {
        return conConcat;
    }

    @Override
    public String toString() {
        return "SubserieVO{" +
                "ideSubserie=" + ideSubserie +
                ", actAdministrativo='" + actAdministrativo + '\'' +
                ", carAdministrativa='" + carAdministrativa + '\'' +
                ", carLegal='" + carLegal + '\'' +
                ", carTecnica='" + carTecnica + '\'' +
                ", carJuridica='" + carJuridica + '\'' +
                ", carContable='" + carContable + '\'' +
                ", conPublica='" + conPublica + '\'' +
                ", conClasificada='" + conClasificada + '\'' +
                ", conReservada='" + conReservada + '\'' +
                ", codSubserie='" + codSubserie + '\'' +
                ", estSubserie='" + estSubserie + '\'' +
                ", fueBibliografica='" + fueBibliografica + '\'' +
                ", nomSubserie='" + nomSubserie + '\'' +
                ", notAlcance='" + notAlcance + '\'' +
                ", idMotivo=" + idMotivo +
                ", nombreMotCreacion='" + nombreMotCreacion + '\'' +
                ", idSerie=" + idSerie +
                ", codSerie='" + codSerie + '\'' +
                ", nomSerie='" + nomSerie + '\'' +
                ", estSubserieValue=" + estSubserieValue +
                ", carConcat='" + carConcat + '\'' +
                ", conConcat='" + conConcat + '\'' +
                '}';
    }
}
