package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.DatosContactoControl;
import co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.List;

@BusinessBoundary
public class GestionarDatosContacto {

    private final DatosContactoControl control;

    @Autowired
    public GestionarDatosContacto(DatosContactoControl control) {
        this.control = control;
    }

    public List<DatosContactoDTO> consultarDatosContactoByIdAgente(String idAgente) throws SystemException {
        BigInteger id = new BigInteger(idAgente);
        return control.consultarDatosContactoByIdAgente(id);
    }

    public DatosContactoDTO consultarDatosContactoPrincipalByIdAgente(String idAgente) throws SystemException {
        BigInteger id = new BigInteger(idAgente);
        return control.consultarDatosContactoPrincipalByIdAgente(id);
    }

    public List<DatosContactoDTO> consultarDatosContactoByNroIdentidad(String nroIdentidad, String tipoAgente, String tipoDoc) throws BusinessException {
        return control.consultarDatosContactoByNroIdentidad(nroIdentidad, tipoAgente, tipoDoc);
    }
}
