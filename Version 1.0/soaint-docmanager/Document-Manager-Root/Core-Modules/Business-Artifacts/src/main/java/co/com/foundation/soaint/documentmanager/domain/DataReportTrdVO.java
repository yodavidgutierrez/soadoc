package co.com.foundation.soaint.documentmanager.domain;

import java.util.ArrayList;

/**
 * Created by jrodriguez on 3/12/2016.
 */
public class DataReportTrdVO {

    private String fechaVersion;
    private String version;
    private String unidadAdministrativa;
    private String oficinaProductora;
    private String nomComite;
    private String numActa;
    private String fechaActa;
    private ArrayList<DataTableTrdVO> rows;

    public DataReportTrdVO(){
        this.rows = new ArrayList<>();
        this.fechaVersion = "";
        this.version = "";
        this.unidadAdministrativa = "";
        this.oficinaProductora = "";
        this.nomComite = "";
        this.numActa = "";
        this.fechaActa = "";
    }

    public DataReportTrdVO(String fechaVersion, String version, String unidadAdministrativa,
                           String oficinaProductora, String nomComite, String numActa, String fechaActa,
                           ArrayList<DataTableTrdVO> rows) {
        super();
        this.fechaVersion = fechaVersion;
        this.version = version;
        this.unidadAdministrativa = unidadAdministrativa;
        this.oficinaProductora = oficinaProductora;
        this.nomComite = nomComite;
        this.numActa = numActa;
        this.fechaActa = fechaActa;
        this.rows = rows;
    }

    public String getFechaVersion() {
        return fechaVersion;
    }

    public void setFechaVersion(String fechaVersion) {
        this.fechaVersion = fechaVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(String unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getOficinaProductora() {
        return oficinaProductora;
    }

    public void setOficinaProductora(String oficinaProductora) {
        this.oficinaProductora = oficinaProductora;
    }

    public ArrayList<DataTableTrdVO> getRows() {
        return rows;
    }

    public void setRows(ArrayList<DataTableTrdVO> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "DataReportTrdVO{" +
                "fechaVersion='" + fechaVersion + '\'' +
                ", version='" + version + '\'' +
                ", unidadAdministrativa='" + unidadAdministrativa + '\'' +
                ", oficinaProductora='" + oficinaProductora + '\'' +
                ", nomComite='" + nomComite + '\'' +
                ", numActa='" + numActa + '\'' +
                ", fechaActa='" + fechaActa + '\'' +
                ", rows=" + rows +
                '}';
    }

    public String getNomComite() {
        return nomComite;
    }

    public void setNomComite(String nomComite) {
        this.nomComite = nomComite;
    }

    public String getNumActa() {
        return numActa;
    }

    public void setNumActa(String numActa) {
        this.numActa = numActa;
    }

    public String getFechaActa() {
        return fechaActa;
    }

    public void setFechaActa(String fechaActa) {
        this.fechaActa = fechaActa;
    }
}
