package co.com.foundation.soaint.documentmanager.domain;

import java.math.BigInteger;

/**
 * Created by jrodriguez on 8/12/2016.
 */
public class DataTrdEcmVO {

    private String unidadAdministrativa;
    private String oficinaProductora;
    protected String codSerie;
    protected String codSubSerie;
    protected String nomSerie;
    protected String nomSubSerie;
    private Long archivoGestion;
    private Long archivoCentral;
    private int diposicion;
    private String procedimientos;
    private boolean radicadora;
    private String grupoSeguridad;

    public DataTrdEcmVO(){}

    public String getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public boolean isRadicadora() {
        return radicadora;
    }

    public void setRadicadora(boolean radicadora) {
        this.radicadora = radicadora;
    }

    public void setUnidadAdministrativa(String unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getGrupoSeguridad() {
        return grupoSeguridad;
    }

    public void setGrupoSeguridad(String grupoSeguridad) {
        this.grupoSeguridad = grupoSeguridad;
    }

    public String getOficinaProductora() {
        return oficinaProductora;
    }

    public void setOficinaProductora(String oficinaProductora) {
        this.oficinaProductora = oficinaProductora;
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

    public Long getArchivoGestion() {
        return archivoGestion;
    }

    public void setArchivoGestion(Long archivoGestion) {
        this.archivoGestion = archivoGestion;
    }

    public Long getArchivoCentral() {
        return archivoCentral;
    }

    public void setArchivoCentral(Long archivoCentral) {
        this.archivoCentral = archivoCentral;
    }

    public int getDiposicion() {
        return diposicion;
    }

    public void setDiposicion(int diposicion) {
        this.diposicion = diposicion;
    }

    public String getProcedimientos() {
        return procedimientos;
    }

    public void setProcedimientos(String procedimientos) {
        this.procedimientos = procedimientos;
    }

    @Override
    public String toString() {
        return "DataTrdEcmVO{" +
                "unidadAdministrativa='" + unidadAdministrativa + '\'' +
                ", oficinaProductora='" + oficinaProductora + '\'' +
                ", codSerie='" + codSerie + '\'' +
                ", codSubSerie='" + codSubSerie + '\'' +
                ", nomSerie='" + nomSerie + '\'' +
                ", nomSubSerie='" + nomSubSerie + '\'' +
                ", archivoGestion=" + archivoGestion +
                ", archivoCentral=" + archivoCentral +
                ", diposicion=" + diposicion +
                ", procedimientos='" + procedimientos + '\'' +
                '}';
    }
}
