package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.MunicipioControl;
import co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 24-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessBoundary
public class GestionarMunicipio {

    // [fields] -----------------------------------

    @Autowired
    private MunicipioControl control;

    // ----------------------

    /**
     * @param codDepar
     * @param estado
     * @return
     * @throws SystemException
     */
    public List<MunicipioDTO> listarMunicipiosByCodDeparAndEstado(String codDepar, String estado) throws SystemException {
        return control.listarMunicipiosByCodDeparAndEstado(codDepar, estado);
    }

    /**
     * @param estado
     * @return
     * @throws SystemException
     */
    public List<MunicipioDTO> listarMunicipiosByEstado(String estado) throws SystemException {
        return control.listarMunicipiosByEstado(estado);
    }

    /**
     * @param codigos
     * @return
     * @throws SystemException
     */
    public List<MunicipioDTO> listarMunicipiosByCodidos(String[] codigos) throws SystemException {
        return control.listarMunicipiosByCodidos(codigos);
    }
}
