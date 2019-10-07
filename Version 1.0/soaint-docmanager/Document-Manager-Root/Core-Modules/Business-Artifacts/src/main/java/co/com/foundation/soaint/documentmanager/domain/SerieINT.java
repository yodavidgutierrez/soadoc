package co.com.foundation.soaint.documentmanager.domain;

import java.math.BigInteger;

/**
 * Created by erodriguez on 24/10/2018.
 */

public class SerieINT {

    private BigInteger ideSerie;
    private String actAdministrativo;
    private Long carAdministrativa;
    private Long carLegal;
    private Long carTecnica;
    private Long carJuridica;
    private Long carContable;
    private Long conPublica;
    private Long conClasificada;
    private Long conReservada;
    private String codSerie;
    private int estSerie;
    private String fueBibliografica;
    private String ideUuid;
    private Integer nivEscritura;
    private Integer nivLectura;
    private String nomSerie;
    private String notAlcance;
    private BigInteger ideMotCreacion;
    private String nombreMotCreacion;

    public SerieINT(BigInteger ideSerie, String actAdministrativo, Long carAdministrativa, Long carLegal, Long carTecnica,
                    Long carJuridica, Long carContable, Long conPublica, Long conClasificada, Long conReservada,
                    String codSerie, int estSerie, String fueBibliografica, String ideUuid, Integer nivEscritura,
                    Integer nivLectura, String nomSerie, String notAlcance, BigInteger ideMotCreacion, String nombreMotCreacion) {
        this.ideSerie = ideSerie;
        this.actAdministrativo = actAdministrativo;
        this.carAdministrativa = carAdministrativa;
        this.carLegal = carLegal;
        this.carTecnica = carTecnica;
        this.carJuridica = carJuridica;
        this.carContable = carContable;
        this.conPublica = conPublica;
        this.conClasificada = conClasificada;
        this.conReservada = conReservada;
        this.codSerie = codSerie;
        this.estSerie = estSerie;
        this.fueBibliografica = fueBibliografica;
        this.ideUuid = ideUuid;
        this.nivEscritura = nivEscritura;
        this.nivLectura = nivLectura;
        this.nomSerie = nomSerie;
        this.notAlcance = notAlcance;
        this.ideMotCreacion = ideMotCreacion;
        this.nombreMotCreacion = nombreMotCreacion;
    }

    public SerieINT() {
    }

    public BigInteger getIdeSerie() {
        return ideSerie;
    }

    public void setIdeSerie(BigInteger ideSerie) {
        this.ideSerie = ideSerie;
    }

    public String getActAdministrativo() {
        return actAdministrativo;
    }

    public void setActAdministrativo(String actAdministrativo) {
        this.actAdministrativo = actAdministrativo;
    }

    public Long getCarAdministrativa() {
        return carAdministrativa;
    }

    public void setCarAdministrativa(Long carAdministrativa) {
        this.carAdministrativa = carAdministrativa;
    }

    public Long getCarLegal() {
        return carLegal;
    }

    public void setCarLegal(Long carLegal) {
        this.carLegal = carLegal;
    }

    public Long getCarTecnica() {
        return carTecnica;
    }

    public void setCarTecnica(Long carTecnica) {
        this.carTecnica = carTecnica;
    }

    public Long getCarJuridica() {
        return carJuridica;
    }

    public void setCarJuridica(Long carJuridica) {
        this.carJuridica = carJuridica;
    }

    public Long getCarContable() {
        return carContable;
    }

    public void setCarContable(Long carContable) {
        this.carContable = carContable;
    }

    public Long getConPublica() {
        return conPublica;
    }

    public void setConPublica(Long conPublica) {
        this.conPublica = conPublica;
    }

    public Long getConClasificada() {
        return conClasificada;
    }

    public void setConClasificada(Long conClasificada) {
        this.conClasificada = conClasificada;
    }

    public Long getConReservada() {
        return conReservada;
    }

    public void setConReservada(Long conReservada) {
        this.conReservada = conReservada;
    }

    public String getCodSerie() {
        return codSerie;
    }

    public void setCodSerie(String codSerie) {
        this.codSerie = codSerie;
    }

    public int getEstSerie() {
        return estSerie;
    }

    public void setEstSerie(int estSerie) {
        this.estSerie = estSerie;
    }

    public String getFueBibliografica() {
        return fueBibliografica;
    }

    public void setFueBibliografica(String fueBibliografica) {
        this.fueBibliografica = fueBibliografica;
    }

    public String getIdeUuid() {
        return ideUuid;
    }

    public void setIdeUuid(String ideUuid) {
        this.ideUuid = ideUuid;
    }

    public Integer getNivEscritura() {
        return nivEscritura;
    }

    public void setNivEscritura(Integer nivEscritura) {
        this.nivEscritura = nivEscritura;
    }

    public Integer getNivLectura() {
        return nivLectura;
    }

    public void setNivLectura(Integer nivLectura) {
        this.nivLectura = nivLectura;
    }

    public String getNomSerie() {
        return nomSerie;
    }

    public void setNomSerie(String nomSerie) {
        this.nomSerie = nomSerie;
    }

    public String getNotAlcance() {
        return notAlcance;
    }

    public void setNotAlcance(String notAlcance) {
        this.notAlcance = notAlcance;
    }

    public BigInteger getIdeMotCreacion() {
        return ideMotCreacion;
    }

    public void setIdeMotCreacion(BigInteger ideMotCreacion) {
        this.ideMotCreacion = ideMotCreacion;
    }

    public String getNombreMotCreacion() {
        return nombreMotCreacion;
    }

    public void setNombreMotCreacion(String nombreMotCreacion) {
        this.nombreMotCreacion = nombreMotCreacion;
    }
}
