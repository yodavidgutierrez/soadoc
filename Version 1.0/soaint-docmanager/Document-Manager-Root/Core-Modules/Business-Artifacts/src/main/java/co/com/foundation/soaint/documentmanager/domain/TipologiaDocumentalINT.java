package co.com.foundation.soaint.documentmanager.domain;

/**
 * Created by erodriguez on 24/10/2018.
 */

public class TipologiaDocumentalINT {

    private Long ideTpgDoc;
    private Long carAdministrativa;
    private Long carLegal;
    private Long carTecnica;
    private Long carJuridico;
    private Long carContable;
    private String codTpgDoc;
    private int estTpgDoc;
    private String ideUuid;
    private Integer nivEscritura;
    private Integer nivLectura;
    private String nomTpgDoc;
    private String notAlcance;
    private String fueBibliografica;
    private Long ideSoporte;
    private String nombreSoporte;
    private Long ideTraDocumental;
    private Long nombreTraDocumental;

    public TipologiaDocumentalINT(Long ideTpgDoc, Long carAdministrativa, Long carLegal, Long carTecnica,
                                  Long carJuridico, Long carContable, String codTpgDoc, int estTpgDoc, String ideUuid,
                                  Integer nivEscritura, Integer nivLectura, String nomTpgDoc, String notAlcance,
                                  String fueBibliografica, Long ideSoporte, String nombreSoporte, Long ideTraDocumental,
                                  Long nombreTraDocumental) {
        this.ideTpgDoc = ideTpgDoc;
        this.carAdministrativa = carAdministrativa;
        this.carLegal = carLegal;
        this.carTecnica = carTecnica;
        this.carJuridico = carJuridico;
        this.carContable = carContable;
        this.codTpgDoc = codTpgDoc;
        this.estTpgDoc = estTpgDoc;
        this.ideUuid = ideUuid;
        this.nivEscritura = nivEscritura;
        this.nivLectura = nivLectura;
        this.nomTpgDoc = nomTpgDoc;
        this.notAlcance = notAlcance;
        this.fueBibliografica = fueBibliografica;
        this.ideSoporte = ideSoporte;
        this.nombreSoporte = nombreSoporte;
        this.ideTraDocumental = ideTraDocumental;
        this.nombreTraDocumental = nombreTraDocumental;
    }

    public TipologiaDocumentalINT() {
    }

    public Long getIdeTpgDoc() {
        return ideTpgDoc;
    }

    public void setIdeTpgDoc(Long ideTpgDoc) {
        this.ideTpgDoc = ideTpgDoc;
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

    public Long getCarJuridico() {
        return carJuridico;
    }

    public void setCarJuridico(Long carJuridico) {
        this.carJuridico = carJuridico;
    }

    public Long getCarContable() {
        return carContable;
    }

    public void setCarContable(Long carContable) {
        this.carContable = carContable;
    }

    public String getCodTpgDoc() {
        return codTpgDoc;
    }

    public void setCodTpgDoc(String codTpgDoc) {
        this.codTpgDoc = codTpgDoc;
    }

    public int getEstTpgDoc() {
        return estTpgDoc;
    }

    public void setEstTpgDoc(int estTpgDoc) {
        this.estTpgDoc = estTpgDoc;
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

    public String getNomTpgDoc() {
        return nomTpgDoc;
    }

    public void setNomTpgDoc(String nomTpgDoc) {
        this.nomTpgDoc = nomTpgDoc;
    }

    public String getNotAlcance() {
        return notAlcance;
    }

    public void setNotAlcance(String notAlcance) {
        this.notAlcance = notAlcance;
    }

    public String getFueBibliografica() {
        return fueBibliografica;
    }

    public void setFueBibliografica(String fueBibliografica) {
        this.fueBibliografica = fueBibliografica;
    }

    public Long getIdeSoporte() {
        return ideSoporte;
    }

    public void setIdeSoporte(Long ideSoporte) {
        this.ideSoporte = ideSoporte;
    }

    public String getNombreSoporte() {
        return nombreSoporte;
    }

    public void setNombreSoporte(String nombreSoporte) {
        this.nombreSoporte = nombreSoporte;
    }

    public Long getIdeTraDocumental() {
        return ideTraDocumental;
    }

    public void setIdeTraDocumental(Long ideTraDocumental) {
        this.ideTraDocumental = ideTraDocumental;
    }

    public Long getNombreTraDocumental() {
        return nombreTraDocumental;
    }

    public void setNombreTraDocumental(Long nombreTraDocumental) {
        this.nombreTraDocumental = nombreTraDocumental;
    }
}
