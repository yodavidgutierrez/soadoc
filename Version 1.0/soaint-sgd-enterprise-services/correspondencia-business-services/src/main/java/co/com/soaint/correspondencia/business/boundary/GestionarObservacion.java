package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.ObservacionControl;
import co.com.soaint.foundation.canonical.correspondencia.ObservacionDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@BusinessBoundary
public class GestionarObservacion {

    private final ObservacionControl control;

    @Autowired
    public GestionarObservacion(ObservacionControl control) {
        this.control = control;
    }

    public List<ObservacionDTO> listarObservacionesByIdInstancia(String idInstancia) {
        return control.listarObservacionesByIdInstancia(idInstancia);
    }

    public boolean insertarObservaciones(List<ObservacionDTO> observaciones) {
        return control.insertarObservaciones(observaciones);
    }
}
