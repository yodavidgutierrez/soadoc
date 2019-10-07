package co.com.foundation.soaint.documentmanager.integration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;

/**
 * Created by jrodriguez on 27/01/2017.
 */
public class OrganigramaItemDTO {

    private Long ideOrgaAdmin;
    private String codOrg;
    private String nomOrg;
    private Long idOrgaAdminPadre;
    private String refPadre;
    private String nomPadre;
    private String estado;
    private Integer nivel;
    private String descOrg;
    private Integer nivelPadre;

    public OrganigramaItemDTO(){

    }
    public Long getIdeOrgaAdmin() {
        return ideOrgaAdmin;
    }

    public void setIdeOrgaAdmin(Long ideOrgaAdmin) {
        this.ideOrgaAdmin = ideOrgaAdmin;
    }

    public String getCodOrg() {
        return codOrg;
    }

    public void setCodOrg(String codOrg) {
        this.codOrg = codOrg;
    }

    public String getNomOrg() {
        return nomOrg;
    }

    public void setNomOrg(String nomOrg) {
        this.nomOrg = nomOrg;
    }

    public Long getIdOrgaAdminPadre() {
        return idOrgaAdminPadre;
    }

    public void setIdOrgaAdminPadre(Long idOrgaAdminPadre) {
        this.idOrgaAdminPadre = idOrgaAdminPadre;
    }

    public String getRefPadre() {
        return refPadre;
    }

    public void setRefPadre(String refPadre) {
        this.refPadre = refPadre;
    }

    public String getNomPadre() {
        return nomPadre;
    }

    public void setNomPadre(String nomPadre) {
        this.nomPadre = nomPadre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getDescOrg() {
        return descOrg;
    }

    public void setDescOrg(String descOrg) {
        this.descOrg = descOrg;
    }

    public Integer getNivelPadre() {
        return nivelPadre;
    }

    public void setNivelPadre(Integer nivelPadre) {
        this.nivelPadre = nivelPadre;
    }
}
