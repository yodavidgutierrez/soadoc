package co.com.foundation.soaint.documentmanager.domain;

import java.util.Date;
import java.util.List;

public class TRDTableVO {

    private String unidadAdministrativa;
    private String oficinaProductora;
    private String nomComite;
    private String numActa;
    private String fechaActa;
    private List<DataTrdVO> rows;
    private boolean valida;

    public TRDTableVO(String unidadAdministrativa, String oficinaProductora, List<DataTrdVO> rows, boolean valida) {
        this.unidadAdministrativa = unidadAdministrativa;
        this.oficinaProductora = oficinaProductora;
        this.rows = rows;
        this.valida = valida;
    }

    public TRDTableVO(String unidadAdministrativa, String oficinaProductora, String nomComite, String numActa, String fechaActa,
                      List<DataTrdVO> rows, boolean valida) {
        this.unidadAdministrativa = unidadAdministrativa;
        this.oficinaProductora = oficinaProductora;
        this.rows = rows;
        this.valida = valida;
        this.nomComite = nomComite;
        this.numActa = numActa;
        this.fechaActa = fechaActa;
    }

    public boolean isValida() {
        return valida;
    }

    public String getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public String getOficinaProductora() {
        return oficinaProductora;
    }

    public String getNomComite() {
        return nomComite;
    }

    public String getNumActa() {
        return numActa;
    }

    public String getfechaActa() {
        return fechaActa;
    }

    public List<DataTrdVO> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return "TRDTableVO{" +
                "unidadAdministrativa='" + unidadAdministrativa + '\'' +
                ", oficinaProductora='" + oficinaProductora + '\'' +
                ", nomComite='" + nomComite + '\'' +
                ", numActa='" + numActa + '\'' +
                ", fechaActa='" + fechaActa + '\'' +
                ", rows=" + rows +
                '}';
    }
}
