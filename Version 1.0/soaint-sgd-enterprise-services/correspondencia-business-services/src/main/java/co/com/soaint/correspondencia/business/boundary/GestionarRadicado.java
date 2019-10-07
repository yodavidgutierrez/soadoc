package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.RadicadoControl;
import co.com.soaint.foundation.canonical.correspondencia.RadicadoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@BusinessBoundary
public class GestionarRadicado {

    private final RadicadoControl control;

    @Autowired
    public GestionarRadicado(RadicadoControl control) {
        this.control = control;
    }

    public List<RadicadoDTO> listarRadicados(String nroRadicado, String nroIdentidad, String noGuia, String nombre, String anno, String tipoDocumento) {
        return control.listarRadicados(nroRadicado, nroIdentidad, noGuia, nombre, anno, tipoDocumento);
    }

    public RadicadoDTO getFechaRadicacion(String nroRadicado) throws SystemException {
        return control.getFechaRadicacion(nroRadicado);
    }

    public Boolean existeRadicado(String nroRadicado) throws SystemException {
        return control.existeRadicado(nroRadicado);
    }

}
