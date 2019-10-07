package co.com.foundation.soaint.documentmanager.integration.domain;

/**
 * Created by jrodriguez on 8/12/2016.
 */
public class ContenidoTrdDTO {

    private String codSerie;
    private String codSubSerie;
    private String idOrgAdm;
    private String idOrgOfc;
    private String nomSerie;
    private String nomSubSerie;


    public ContenidoTrdDTO(){}

    public ContenidoTrdDTO(String codSerie, String codSubSerie, String idOrgAdm, String idOrgOfc, String nomSerie, String nomSubSerie) {
        this.codSerie = codSerie;
        this.codSubSerie = codSubSerie;
        this.idOrgAdm = idOrgAdm;
        this.idOrgOfc = idOrgOfc;
        this.nomSerie = nomSerie;
        this.nomSubSerie = nomSubSerie;

    }

    public String getCodSerie() {
        return codSerie;
    }

    public void setCodSerie(String codSerie) {
        this.codSerie = codSerie;
    }

    public String getCodSubSerie() {
        return codSubSerie;
    }

    public void setCodSubSerie(String codSubSerie) {
        this.codSubSerie = codSubSerie;
    }

    public String getIdOrgAdm() {
        return idOrgAdm;
    }

    public void setIdOrgAdm(String idOrgAdm) {
        this.idOrgAdm = idOrgAdm;
    }

    public String getIdOrgOfc() {
        return idOrgOfc;
    }

    public void setIdOrgOfc(String idOrgOfc) {
        this.idOrgOfc = idOrgOfc;
    }

    public String getNomSerie() {
        return nomSerie;
    }

    public void setNomSerie(String nomSerie) {
        this.nomSerie = nomSerie;
    }

    public String getNomSubSerie() {
        return nomSubSerie;
    }

    public void setNomSubSerie(String nomSubSerie) {
        this.nomSubSerie = nomSubSerie;
    }


}
