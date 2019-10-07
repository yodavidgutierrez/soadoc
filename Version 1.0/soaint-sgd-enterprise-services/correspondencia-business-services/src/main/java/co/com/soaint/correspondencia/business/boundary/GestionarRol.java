package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.RolControl;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;


@NoArgsConstructor
@BusinessBoundary
public class GestionarRol {



    @Autowired
    private RolControl control;
    // ----------------------



    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<RolDTO> listarRoles() {
        return control.listarRoles();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<RolDTO> listarRolesByIdFuncionario(BigInteger idFuncionario) {
        return control.listarRolesByIdFuncionario(idFuncionario);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<RolDTO> listarRolesByLoginName(String loginName) {
        return control.listarRolesByLoginName(loginName);
    }
}
