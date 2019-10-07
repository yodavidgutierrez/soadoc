package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.TvsOrgaAdminXFunciPkControl;
import co.com.soaint.foundation.canonical.correspondencia.TvsOrgaAdminXFunciPkDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 22-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessBoundary
public class GestionarTvsOrgaAdminXFunciPk {
    // [fields] -----------------------------------

    @Autowired
    TvsOrgaAdminXFunciPkControl control;

    // ----------------------

    public boolean insertarFuncionarioConCodigoDependencia(TvsOrgaAdminXFunciPkDTO tvsOrga) throws BusinessException, SystemException {
        return control.insertarFuncionarioConCodigoDependencia(tvsOrga);

    }

}
