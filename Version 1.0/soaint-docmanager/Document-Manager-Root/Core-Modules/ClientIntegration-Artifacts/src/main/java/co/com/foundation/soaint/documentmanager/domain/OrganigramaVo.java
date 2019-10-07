package co.com.foundation.soaint.documentmanager.domain;

/**
 * Created by jrodriguez on 8/12/2016.
 */
public class OrganigramaVo {

    private Long ideOrgaAdmin;
    private String codOrg;
    private String nomOrg;
    private String tipo;
    private boolean radicadora;

    public OrganigramaVo() {
    }

    public boolean isRadicadora() {
        return radicadora;
    }

    public void setRadicadora(boolean radicadora) {
        this.radicadora = radicadora;
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
