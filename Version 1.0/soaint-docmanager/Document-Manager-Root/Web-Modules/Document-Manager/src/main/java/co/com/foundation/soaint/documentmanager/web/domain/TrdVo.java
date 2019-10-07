package co.com.foundation.soaint.documentmanager.web.domain;

import java.util.Date;

/**
 * Created by jrodriguez on 26/11/2016.
 */
public class TrdVo {

    private String codUniAmt;
    private String codOfcProd;
    private String nombreComite;
    private String numActa;
    private String fechaActa;

    public TrdVo(){}

    public TrdVo(String codUniAmt, String codOfcProd) {
        this.codUniAmt = codUniAmt;
        this.codOfcProd = codOfcProd;
    }

    public String getCodOfcProd() {
        return codOfcProd;
    }

    public void setCodOfcProd(String codOfcProd) {
        this.codOfcProd = codOfcProd;
    }

    public String getCodUniAmt() {
        return codUniAmt;
    }

    public void setCodUniAmt(String codUniAmt) {
        this.codUniAmt = codUniAmt;
    }

    public String getNombreComite() {
        return nombreComite;
    }

    public void setNombreComite(String nombreComite) {
        this.nombreComite = nombreComite;
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

    @Override
    public String toString() {
        return "TrdVo{" +
                "codUniAmt='" + codUniAmt + '\'' +
                ", codOfcProd='" + codOfcProd + '\'' +
                ", nombreComite='" + nombreComite + '\'' +
                ", numActa='" + numActa + '\'' +
                ", fechaActa='" + fechaActa + '\'' +
                '}';
    }
}


