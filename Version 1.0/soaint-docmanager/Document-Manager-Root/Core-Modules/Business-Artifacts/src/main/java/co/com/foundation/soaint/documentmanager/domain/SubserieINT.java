package co.com.foundation.soaint.documentmanager.domain;

import java.math.BigInteger;

/**
 * Created by erodriguez on 24/10/2018.
 */

public class SubserieINT {

    private BigInteger ideSubserie;
    private String actAdministrativo;
    private Long carAdministrativa;
    private Long carLegal;
    private Long carTecnica;
    private String codSubserie;
    private int estSubserie;
    private String fueBibliografica;
    private String ideUuid;
    private Integer nivEscritura;
    private Integer nivLectura;
    private String nomSubserie;
    private String notAlcance;
    private Long carJuridica;
    private Long carContable;
    private Long conPublica;
    private Long conClasificada;
    private Long conReservada;
    private BigInteger ideMotCreacion;
    private String nombreMotCreacion;
    private BigInteger ideSerie;

    public SubserieINT(BigInteger ideSubserie, String actAdministrativo, Long carAdministrativa, Long carLegal,
                       Long carTecnica, String codSubserie, int estSubserie, String fueBibliografica, String ideUuid,
                       Integer nivEscritura, Integer nivLectura, String nomSubserie, String notAlcance, Long carJuridica,
                       Long carContable, Long conPublica, Long conClasificada, Long conReservada, BigInteger ideMotCreacion,
                       String nombreMotCreacion, BigInteger ideSerie) {
        this.ideSubserie = ideSubserie;
        this.actAdministrativo = actAdministrativo;
        this.carAdministrativa = carAdministrativa;
        this.carLegal = carLegal;
        this.carTecnica = carTecnica;
        this.codSubserie = codSubserie;
        this.estSubserie = estSubserie;
        this.fueBibliografica = fueBibliografica;
        this.ideUuid = ideUuid;
        this.nivEscritura = nivEscritura;
        this.nivLectura = nivLectura;
        this.nomSubserie = nomSubserie;
        this.notAlcance = notAlcance;
        this.carJuridica = carJuridica;
        this.carContable = carContable;
        this.conPublica = conPublica;
        this.conClasificada = conClasificada;
        this.conReservada = conReservada;
        this.ideMotCreacion = ideMotCreacion;
        this.nombreMotCreacion = nombreMotCreacion;
        this.ideSerie = ideSerie;
    }

    public SubserieINT() {
    }

    public BigInteger getIdeSubserie() {
        return ideSubserie;
    }

    public void setIdeSubserie(BigInteger ideSubserie) {
        this.ideSubserie = ideSubserie;
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

    public String getCodSubserie() {
        return codSubserie;
    }

    public void setCodSubserie(String codSubserie) {
        this.codSubserie = codSubserie;
    }

    public int getEstSubserie() {
        return estSubserie;
    }

    public void setEstSubserie(int estSubserie) {
        this.estSubserie = estSubserie;
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

    public String getNomSubserie() {
        return nomSubserie;
    }

    public void setNomSubserie(String nomSubserie) {
        this.nomSubserie = nomSubserie;
    }

    public String getNotAlcance() {
        return notAlcance;
    }

    public void setNotAlcance(String notAlcance) {
        this.notAlcance = notAlcance;
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

    public BigInteger getIdeSerie() {
        return ideSerie;
    }

    public void setIdeSerie(BigInteger ideSerie) {
        this.ideSerie = ideSerie;
    }
}
