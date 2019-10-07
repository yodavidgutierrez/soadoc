package co.com.foundation.soaint.documentmanager.web.domain;

import co.com.foundation.soaint.documentmanager.web.infrastructure.util.constants.EstadoCaracteristicaEnum;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by jrodriguez on 11/09/2016.
 */
public class SerieVO implements Serializable {

    private BigInteger ideSerie;
    @NotNull
    @NotEmpty
    @Pattern(regexp="\\d+",message = "Debe ser un número")
    private String codSerie;
    @NotNull
    @NotEmpty
    private String nomSerie;
    @NotNull
    @NotEmpty
    private String actAdministrativo;
    @NotNull
    private BigInteger idMotivo;
    @NotNull
    @NotEmpty
    private String notAlcance;
    @NotNull
    @NotEmpty
    private String fueBibliografica;
    @NotNull
    @NotEmpty
    private String estSerie;
    @NotNull
    @NotEmpty
    @Pattern(regexp="\\d+",message = "Debe ser un número")
    private String carTecnica;
    @NotNull
    @NotEmpty
    @Pattern(regexp="\\d+",message = "Debe ser un número")
    private String carLegal;
    @NotNull
    @NotEmpty
    @Pattern(regexp="\\d+",message = "Debe ser un número")
    private String carAdministrativa;
    @NotNull
    @NotEmpty
    @Pattern(regexp ="\\d+",message ="Debe ser un número")
    private String carContable;
    @NotNull
    @NotEmpty
    @Pattern(regexp ="\\d+",message ="Debe ser un número")
    private String carJuridica;
    @NotNull
    @NotEmpty
    @Pattern(regexp ="\\d+",message ="Debe ser un número")
    private String conPublica;
    @NotNull
    @NotEmpty
    @Pattern(regexp ="\\d+",message ="Debe ser un número")
    private String conClasificada;
    @NotNull
    @NotEmpty
    @Pattern(regexp ="\\d+",message ="Debe ser un número")
    private String conReservada;

    private String carConcat;

    private String conConcat;

    private String nombreMotCreacion;

    private int estSerieValue;

    private String indUnidadCor;

    public SerieVO() {
    }
    
    public SerieVO(String ideSerie) {
        this.ideSerie = BigInteger.valueOf(Long.parseLong(ideSerie));
    }

    public SerieVO(String codSerie, String nomSerie, BigInteger ideSerie) {
        this.codSerie = codSerie;
        this.nomSerie = nomSerie;
        this.ideSerie = ideSerie;
    }
    public SerieVO(String codSerie, String nomSerie, String actAdministrativo,BigInteger idMotivo, String nombreMotCreacion,
                   String notAlcance, String fueBibliografica, String estSerie, String carTecnica, String carLegal,
                   String carAdministrativa,String carContable, String carJuridica, String conPublica, String conClasificada,
                   String conReservada, BigInteger ideSerie, int estSerieValue, String indUnidadCor) {

        this.actAdministrativo = actAdministrativo;
        this.carAdministrativa = carAdministrativa;
        this.carLegal = carLegal;
        this.carTecnica = carTecnica;
        this.carContable = carContable;
        this.carJuridica = carJuridica;
        this.conPublica = conPublica;
        this.conClasificada = conClasificada;
        this.conReservada = conReservada;
        this.codSerie = codSerie;
        this.estSerie = estSerie;
        this.fueBibliografica = fueBibliografica;
        this.nomSerie = nomSerie;
        this.notAlcance = notAlcance;
        this.idMotivo = idMotivo;
        this.nombreMotCreacion = nombreMotCreacion;
        this.ideSerie = ideSerie;
        this.estSerieValue =estSerieValue;
        this.indUnidadCor = indUnidadCor;
        concatCaracteristicas();
        concatConfidencialidad();
    }

    public void concatCaracteristicas (){

        carConcat = "";

        if (carAdministrativa == "on") {
            carConcat = "A";
        }
        if (carContable == "on"){
            carConcat += carConcat.length() == 0 ? "C" : ", C";
        }
        if (carJuridica == "on"){
            carConcat += carConcat.length() == 0 ? "J" : ", J";
        }
        if (carLegal == "on"){
            carConcat += carConcat.length() == 0 ? "L" : ", L";
        }
        if (carTecnica == "on"){
            carConcat += carConcat.length() == 0 ? "T" : ", T";
        }



    }

    public void concatConfidencialidad (){

        conConcat = "";

        if (conClasificada == "on"){
            conConcat = "C";
        }
        if (conPublica == "on"){
            conConcat += conConcat.length() == 0 ? "P" : ", P";

        }
        if (conReservada == "on"){
            conConcat += conConcat.length() == 0 ? "R" : ", R";
        }

    }




    public BigInteger getIdeSerie() {
        return ideSerie;
    }

    public String getActAdministrativo() {
        return actAdministrativo;
    }

    public String getCarLegalValue() {return carLegal;}

    public Long getCarLegal() {
        return StringUtils.equals(carLegal, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getCarAdministrativaValue() {return carAdministrativa;}


    public Long getCarAdministrativa() {
        return StringUtils.equals(carAdministrativa, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getCarTecnicaValue() {return carTecnica;}

    public Long getCarTecnica() {
        return StringUtils.equals(carTecnica, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getCarContableValue() {return carContable;}

    public Long getCarContable(){
        return StringUtils.equals(carContable, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getCarJuridicaValue() {return carJuridica;}

    public Long getCarJuridica(){
        return StringUtils.equals(carJuridica, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getConPublicaValue() {return conPublica;}

    public Long getConPublica(){
        return StringUtils.equals(conPublica, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getConClasificadaValue() {return conClasificada;}

    public Long getConClasificada(){
        return StringUtils.equals(conClasificada, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getConReservadaValue() {return conReservada;}

    public Long getConReservada(){
        return StringUtils.equals(conReservada, EstadoCaracteristicaEnum.ON.getName())
                ? EstadoCaracteristicaEnum.ON.getId() : EstadoCaracteristicaEnum.OFF.getId();
    }

    public String getCarConcat() {
        return carConcat;
    }

    public String getConConcat() {
        return conConcat;
    }

    public int getEstSerieValue() {return estSerieValue;}

    public String getCodSerie() {
        return codSerie;
    }

    public String getEstSerie() {
        return estSerie;
    }

    public String getFueBibliografica() {
        return fueBibliografica;
    }

    public String getNomSerie() {
        return nomSerie;
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

    public String getIndUnidadCor() { return indUnidadCor; }

    @Override
    public String toString() {
        return "SerieVO{"
                + "ideSerie=" + ideSerie
                + ", actAdministrativo='" + actAdministrativo + '\''
                + ", carAdministrativa=" + carAdministrativa
                + ", carLegal=" + carLegal
                + ", carTecnica=" + carTecnica
                + ", carContable=" + carContable
                + ", carJuridica=" + carJuridica
                + ", conPublica=" + conPublica
                + ", conClasificada=" + conClasificada
                + ", conReservada=" + conReservada
                + ", codSerie='" + codSerie + '\''
                + ", estSerie=" + estSerie
                + ", fueBibliografica='" + fueBibliografica + '\''
                + ", nomSerie='" + nomSerie + '\''
                + ", notAlcance='" + notAlcance + '\''
                + ", idMotivo=" + idMotivo
                + ", nombreMotCreacion='" + nombreMotCreacion + '\''
                + ", carConcat=" + carConcat
                + ", conConcat=" + conConcat
                + ", indUnidadCor=" + indUnidadCor
                + '}';
    }
}
