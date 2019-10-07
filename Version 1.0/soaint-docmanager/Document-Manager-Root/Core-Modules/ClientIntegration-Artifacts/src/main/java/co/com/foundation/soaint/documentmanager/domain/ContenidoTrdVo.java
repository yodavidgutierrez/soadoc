package co.com.foundation.soaint.documentmanager.domain;

/**
 * Created by jrodriguez on 8/12/2016.
 */
public class ContenidoTrdVo {

    private String codSerie;
    private String codSubSerie;
    private int diposicionFinal;
    private String idOrgAdm;
    private String idOrgOfc;
    private String nomSerie;
    private String nomSubSerie;
    private String procedimiento;
    private Long retArchivoCentral;
    private Long retArchivoGestion;
    private String codSede;
    private boolean radicadora;
    private String grupoSeguridad;

    public ContenidoTrdVo(){}

    public boolean isRadicadora() {
        return radicadora;
    }

    public void setRadicadora(boolean radicadora) {
        this.radicadora = radicadora;
    }

    public String getGrupoSeguridad() {
        return grupoSeguridad;
    }

    public void setGrupoSeguridad(String grupoSeguridad) {
        this.grupoSeguridad = grupoSeguridad;
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

    public int getDiposicionFinal() {
        return diposicionFinal;
    }

    public void setDiposicionFinal(int diposicionFinal) {
        this.diposicionFinal = diposicionFinal;
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

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public Long getRetArchivoCentral() {
        return retArchivoCentral;
    }

    public void setRetArchivoCentral(Long retArchivoCentral) {
        this.retArchivoCentral = retArchivoCentral;
    }

    public Long getRetArchivoGestion() {
        return retArchivoGestion;
    }

    public void setRetArchivoGestion(Long retArchivoGestion) {
        this.retArchivoGestion = retArchivoGestion;
    }

    public String getCodSede() {return codSede;}

    public void setCodSede(String codSede) {this.codSede = codSede;}
}
