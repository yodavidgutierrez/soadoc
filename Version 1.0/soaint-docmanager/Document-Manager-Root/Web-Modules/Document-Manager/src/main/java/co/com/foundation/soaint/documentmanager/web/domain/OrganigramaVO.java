package co.com.foundation.soaint.documentmanager.web.domain;

import java.io.Serializable;

/**
 * Created by jrodriguez on 30/10/2016.
 */
public class OrganigramaVO implements Serializable {


    private Long ideOrgaAdmin;
    private String codOrg;
    private String nomOrg;
    private String descOrg;
    private String indEsActivo;
    private Integer codNivel;
    private String valVersion;
    private String ideOrgaAdminPadre;
    private String nomOrgPadre;
    private int estadoValue;
    private String botonSelected;
    private String abrevOrg;
    private String indUnidadCor;

    public OrganigramaVO() {
    }

    public OrganigramaVO(Long ideOrgaAdmin, String codOrg, String nomOrg, String descOrg, String indEsActivo,
                         Integer codNivel, String ideOrgaAdminPadre, String valVersion, String nomOrgPadre,
                         int estadoValue,String botonSelected, String abrevOrg, String indUnidadCor) {
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.descOrg = descOrg;
        this.indEsActivo = indEsActivo;
        this.codNivel = codNivel;
        this.ideOrgaAdminPadre = ideOrgaAdminPadre;
        this.valVersion = valVersion;
        this.nomOrgPadre = nomOrgPadre;
        this.estadoValue = estadoValue;
        this.abrevOrg = abrevOrg;
        this.botonSelected = botonSelected;
        this.indUnidadCor = indUnidadCor;
    }

    public String getIndUnidadCor() { return indUnidadCor; }

    public Long getIdeOrgaAdmin() {
        return ideOrgaAdmin;
    }

    public String getNomOrg() {
        return nomOrg;
    }

    public String getCodOrg() {
        return codOrg;
    }

    public String getDescOrg() {
        return descOrg;
    }

    public String getIndEsActivo() {
        return indEsActivo;
    }

    public Integer getCodNivel() {
        return codNivel;
    }

    public String getValVersion() {
        return valVersion;
    }

    public String getIdeOrgaAdminPadre() {
        return ideOrgaAdminPadre;
    }

    public String getNomOrgPadre() {
        return nomOrgPadre;
    }

    public int getEstadoValue() {
        return estadoValue;
    }

    public String getBotonSelected() {
        return botonSelected;
    }

    public void setBotonSelected(String botonSelected) {
        this.botonSelected = botonSelected;
    }

    public String getAbrevOrg() {return abrevOrg;}

    public void setAbrevOrg(String abrevOrg) {
        this.abrevOrg = abrevOrg;
    }

    @Override
    public String toString() {
        return "OrganigramaVO{" +
                "ideOrgaAdmin=" + ideOrgaAdmin +
                ", codOrg='" + codOrg + '\'' +
                ", nomOrg='" + nomOrg + '\'' +
                ", descOrg='" + descOrg + '\'' +
                ", indEsActivo='" + indEsActivo + '\'' +
                ", codNivel=" + codNivel +
                ", valVersion='" + valVersion + '\'' +
                ", ideOrgaAdminPadre='" + ideOrgaAdminPadre + '\'' +
                ", nomOrgPadre='" + nomOrgPadre + '\'' +
                ", estadoValue=" + estadoValue +
                ", botonSelected='" + botonSelected + '\'' +
                ", abrevOrg='" + abrevOrg + '\'' +
                ", indUnidadCor='" + indUnidadCor + '\'' +
                '}';
    }


}
