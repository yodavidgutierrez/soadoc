package co.com.foundation.soaint.documentmanager.integration.domain;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;

@XmlRootElement
public class SerieDTO {

    private BigInteger ideSerie;
    private String actAdministrativo;
    private String carAdministrativa;
    private String carLegal;
    private String carTecnica;
    private String codSerie;
    private String estSerie;
    private String fueBibliografica;
    private String nomSerie;
    private String notAlcance;
    private BigInteger idMotivo;
    private String nombreMotCreacion;
    private int estSerieValue;

    public SerieDTO() {
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

    public String getCarAdministrativa() {
        return carAdministrativa;
    }

    public void setCarAdministrativa(String carAdministrativa) {
        this.carAdministrativa = carAdministrativa;
    }

    public String getCarLegal() {
        return carLegal;
    }

    public void setCarLegal(String carLegal) {
        this.carLegal = carLegal;
    }

    public String getCarTecnica() {
        return carTecnica;
    }

    public void setCarTecnica(String carTecnica) {
        this.carTecnica = carTecnica;
    }

    public String getCodSerie() {
        return codSerie;
    }

    public void setCodSerie(String codSerie) {
        this.codSerie = codSerie;
    }

    public String getEstSerie() {
        return estSerie;
    }

    public void setEstSerie(String estSerie) {
        this.estSerie = estSerie;
    }

    public String getFueBibliografica() {
        return fueBibliografica;
    }

    public void setFueBibliografica(String fueBibliografica) {
        this.fueBibliografica = fueBibliografica;
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

    public BigInteger getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(BigInteger idMotivo) {
        this.idMotivo = idMotivo;
    }

    public String getNombreMotCreacion() {
        return nombreMotCreacion;
    }

    public void setNombreMotCreacion(String nombreMotCreacion) {
        this.nombreMotCreacion = nombreMotCreacion;
    }

    public int getEstSerieValue() {
        return estSerieValue;
    }

    public void setEstSerieValue(int estSerieValue) {
        this.estSerieValue = estSerieValue;
    }
}
