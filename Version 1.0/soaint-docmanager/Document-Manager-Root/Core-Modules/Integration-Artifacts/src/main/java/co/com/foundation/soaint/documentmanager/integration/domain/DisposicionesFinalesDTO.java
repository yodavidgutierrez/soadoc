package co.com.foundation.soaint.documentmanager.integration.domain;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jrodriguez on 29/01/2017.
 */
public class DisposicionesFinalesDTO {

    private BigInteger ideDisFinal;
    private String nomDisFinal;
    private String desDisFinal;
    private String staDisFinal;
    private Date fecCambio;
    private Date fecCreacion;

    public  DisposicionesFinalesDTO(){}

    public BigInteger getIdeDisFinal() {
        return ideDisFinal;
    }

    public void setIdeDisFinal(BigInteger ideDisFinal) {
        this.ideDisFinal = ideDisFinal;
    }

    public String getNomDisFinal() {
        return nomDisFinal;
    }

    public void setNomDisFinal(String nomDisFinal) {
        this.nomDisFinal = nomDisFinal;
    }

    public String getDesDisFinal() {
        return desDisFinal;
    }

    public void setDesDisFinal(String desDisFinal) {
        this.desDisFinal = desDisFinal;
    }

    public String getStaDisFinal() {
        return staDisFinal;
    }

    public void setStaDisFinal(String staDisFinal) {
        this.staDisFinal = staDisFinal;
    }

    public Date getFecCambio() {
        return fecCambio;
    }

    public void setFecCambio(Date fecCambio) {
        this.fecCambio = fecCambio;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }
}
