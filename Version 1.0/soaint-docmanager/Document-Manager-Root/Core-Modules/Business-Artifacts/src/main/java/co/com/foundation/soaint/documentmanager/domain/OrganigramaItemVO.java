package co.com.foundation.soaint.documentmanager.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;

public class OrganigramaItemVO {

    @JsonProperty("id")
    private Long ideOrgaAdmin;
    @JsonProperty("code")
    private String codOrg;
    @JsonProperty("text")
    private String nomCodOrg;
    private Long idOrgaAdminPadre;
    @JsonProperty("parent")
    private String refPadre;
    @JsonProperty("textParent")
    private String nomPadre;
    @JsonProperty("status")
    private String estado;
    @JsonProperty("level")
    private Integer nivel;
    @JsonProperty("desc")
    private String descOrg;
    @JsonProperty("levelParent")
    private Integer nivelPadre;
    @JsonProperty("codeParent")
    private String codOrgPadre;
    @JsonProperty("abrevOrg")
    private String abrevOrg;
    @JsonProperty("nomOrg")
    private String nomOrg;
    @JsonProperty("indCorrespondencia")
    private String indCorrespondencia;


    public OrganigramaItemVO(Long ideOrgaAdmin, String codOrg, String nomOrg, String estado, String abrevOrg, String indCorrespondencia) {
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.estado = estado;
        this.abrevOrg = abrevOrg;
        this.nomCodOrg = codOrg + " " + nomOrg;
        this.indCorrespondencia = indCorrespondencia;
        refPadre = "#";
    }

    public OrganigramaItemVO(Long ideOrgaAdmin, String codOrg, String nomOrg, String estado, Integer nivel, String abrevOrg, String indCorrespondencia) {
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.estado = estado;
        refPadre = "#";
        this.nivel =nivel;
        this.abrevOrg = abrevOrg;
        this.nomCodOrg = codOrg + " " + nomOrg;
        this.indCorrespondencia = indCorrespondencia;
    }

    public OrganigramaItemVO(Long ideOrgaAdmin, String codOrg, String nomOrg, String estado, String descOrg, Integer nivel, String abrevOrg, String indCorrespondencia) {
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.estado = estado;
        this.descOrg = descOrg;
        this.nivel = nivel;
        this.abrevOrg = abrevOrg;
        this.nomCodOrg = codOrg + " " + nomOrg;
        this.indCorrespondencia = indCorrespondencia;
        refPadre = "#";

    }

    public OrganigramaItemVO(Long ideOrgaAdmin, String codOrg, String nomOrg, Long idOrgaAdminPadre, String estado,
                             String descOrg, Integer nivel, String abrevOrg, String indCorrespondencia) {
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.ideOrgaAdmin = idOrgaAdminPadre;
        this.estado = estado;
        this.descOrg = descOrg;
        this.nivel = nivel;
        this.nomCodOrg = codOrg + " " + nomOrg;
        refPadre = "#";
        this.indCorrespondencia = indCorrespondencia;
        this.abrevOrg = abrevOrg;
    }

    public OrganigramaItemVO(Long ideOrgaAdmin, String codOrg, String nomOrg, Long idOrgaAdminPadre, String nomPadre,
                             String estado, Integer nivel, String descOrg, Integer nivelPadre, String abrevOrg, String indCorrespondencia) {
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.idOrgaAdminPadre = idOrgaAdminPadre;
        refPadre = getIdOrgaAdminPadre().toString();
        this.nomPadre = nomPadre;
        this.estado = estado;
        this.nivel = nivel;
        this.descOrg = descOrg;
        this.nivelPadre = nivelPadre;
        this.abrevOrg = abrevOrg;
        this.nomCodOrg = codOrg + " " + nomOrg;
        this.indCorrespondencia = indCorrespondencia;
    }

    public OrganigramaItemVO(Long ideOrgaAdmin, String codOrg, String nomOrg, Long idOrgaAdminPadre, String nomPadre,
                             String estado, Integer nivel, String descOrg, Integer nivelPadre, String codOrgPadre, String abrevOrg, String indCorrespondencia) {
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.idOrgaAdminPadre = idOrgaAdminPadre;
        refPadre = getIdOrgaAdminPadre().toString();
        this.nomPadre = nomPadre;
        this.estado = estado;
        this.nivel = nivel;
        this.descOrg = descOrg;
        this.nivelPadre = nivelPadre;
        this.codOrgPadre = codOrgPadre;
        this.abrevOrg = abrevOrg;
        this.nomCodOrg = codOrg + " " + nomOrg;
        this.indCorrespondencia = indCorrespondencia;
    }

    public String getIndCorrespondencia() {
        return indCorrespondencia;
    }

    public Long getIdeOrgaAdmin() {
        return ideOrgaAdmin;
    }

    public String getCodOrg() {
        return codOrg;
    }

    public String getNomOrg() {
        return nomOrg;
    }

    public Long getIdOrgaAdminPadre() {
        return idOrgaAdminPadre;
    }

    public String getRefPadre() {
        return refPadre;
    }

    public String getNomPadre() {
        return nomPadre;
    }

    public String getEstado() {
        return estado;
    }

    public Integer getNivel() {
        return nivel;
    }

    public String getDescOrg() {
        return descOrg;
    }

    public Integer getNivelPadre() {
        return nivelPadre;
    }

    public String getAbrevOrg() {
        return abrevOrg;
    }


    public void setIdeOrgaAdmin(Long ideOrgaAdmin) {
        this.ideOrgaAdmin = ideOrgaAdmin;
    }

    public void setCodOrg(String codOrg) {
        this.codOrg = codOrg;
    }

    public void setNomOrg(String nomOrg) {
        this.nomOrg = nomOrg;
    }

    public void setIdOrgaAdminPadre(Long idOrgaAdminPadre) {
        this.idOrgaAdminPadre = idOrgaAdminPadre;
    }

    public void setRefPadre(String refPadre) {
        this.refPadre = refPadre;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setNomPadre(String nomPadre) {
        this.nomPadre = nomPadre;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public void setDescOrg(String descOrg) {
        this.descOrg = descOrg;
    }

    public void setNivelPadre(Integer nivelPadre) {
        this.nivelPadre = nivelPadre;
    }

    public void setAbrevOrg(String abrevOrg) {
        this.abrevOrg = abrevOrg;
    }

    public String getNomCodOrg() {
        return nomCodOrg;
    }

    public void setNomCodOrg(String nomCodOrg) {
        this.nomCodOrg = nomCodOrg;
    }

    public String getCodOrgPadre() {
        return codOrgPadre;
    }

    public void setCodOrgPadre(String codOrgPadre) {
        this.codOrgPadre = codOrgPadre;
    }

    @Override
    public String toString() {
        return "OrganigramaItemVO{" +
                "ideOrgaAdmin=" + ideOrgaAdmin +
                ", codOrg='" + codOrg + '\'' +
                ", nomOrg='" + nomOrg + '\'' +
                ", idOrgaAdminPadre=" + idOrgaAdminPadre +
                ", refPadre='" + refPadre + '\'' +
                ", nomPadre='" + nomPadre + '\'' +
                ", estado='" + estado + '\'' +
                ", nivel=" + nivel +
                ", descOrg='" + descOrg + '\'' +
                ", nivelPadre=" + nivelPadre +
                ", abrevOrg=" + abrevOrg +
                ", nomCodOrg=" + nomCodOrg +
                ", indCorrespondencia=" + indCorrespondencia +
                '}';
    }

    // Comparator
    public static class CompNivel implements Comparator<OrganigramaItemVO> {

        @Override
        public int compare(OrganigramaItemVO o1, OrganigramaItemVO o2) {
            return o1.getIdeOrgaAdmin().intValue() - o2.getIdeOrgaAdmin().intValue();
        }
    }
}
