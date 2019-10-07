package co.com.foundation.soaint.documentmanager.integration.domain;

/**
 * Created by jrodriguez on 8/12/2016.
 */
public class OrganigramaDTO {

    private String codOrg;
    private Long ideOrgaAdmin;
    private String nomOrg;
    private String tipo;

    public OrganigramaDTO() {
    }

    public OrganigramaDTO(String codOrg, Long ideOrgaAdmin, String nomOrg, String tipo) {
        this.codOrg = codOrg;
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.nomOrg = nomOrg;
        this.tipo = tipo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
