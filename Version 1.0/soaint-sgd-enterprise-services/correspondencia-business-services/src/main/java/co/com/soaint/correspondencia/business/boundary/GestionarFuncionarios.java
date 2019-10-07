package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.FuncionariosControl;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessBoundary
public class GestionarFuncionarios {
    // [fields] -----------------------------------

    @Autowired
    FuncionariosControl control;
    // ----------------------

    /**
     * @param loginName
     * @param estado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public FuncionarioDTO listarFuncionarioByLoginNameAndEstado(String loginName, String estado) throws BusinessException, SystemException {
        return control.listarFuncionarioByLoginNameAndEstado(loginName, estado);
    }

    /**
     * @param loginNames
     * @return
     * @throws SystemException
     */
    public FuncionariosDTO listarFuncionariosByLoginNameList(String[] loginNames) throws SystemException {
        return control.listarFuncionariosByLoginNameList(loginNames);
    }

    /**
     * @param codDependencia
     * @param codEstado
     * @return
     * @throws SystemException
     */
    public FuncionariosDTO listarFuncionariosByCodDependenciaAndCodEstado(String codDependencia, String codEstado) throws SystemException {
        return control.listarFuncionariosByCodDependenciaAndCodEstado(codDependencia, codEstado);
    }

    /**
     * @param loginName
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public FuncionarioDTO listarFuncionarioByLoginName(String loginName) throws SystemException {
        return control.listarFuncionarioByLoginName(loginName);
    }

    public List<FuncionarioDTO> listarFuncionarioByRol(String rol) throws SystemException {
        return control.listarFuncionarioByRol(rol);
    }

    /**
     * @param funcionarioDTO
     * @throws SystemException
     */
    public void crearFuncionario(FuncionarioDTO funcionarioDTO) throws SystemException {
        control.crearFuncionario(funcionarioDTO);
    }

    public List<FuncionarioDTO> consultarFuncionarioByNroIdentificacion(String nroIdentificacion) throws SystemException, BusinessException {
        return control.consultarFuncionarioByNroIdentificacion(nroIdentificacion);
    }

    /**
     * @param funcionarioDTO
     */
    public String actualizarFuncionario(FuncionarioDTO funcionarioDTO) {
        return control.actualizarFuncionario(funcionarioDTO);
    }

    public Boolean actualizarRolesFuncionario(FuncionarioDTO funcionario) throws SystemException {
        return control.actualizarRolesFuncionario(funcionario);
    }

    /**
     * @param funcionarioDTO
     * @return
     * @throws SystemException
     */
    public FuncionariosDTO buscarFuncionario(FuncionarioDTO funcionarioDTO) throws SystemException {
        return control.buscarFuncionario(funcionarioDTO);
    }


    /**
     * @param ideFunci
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public String consultarLoginNameByIdeFunci(BigInteger ideFunci) throws BusinessException, SystemException {
        return control.consultarLoginNameByIdeFunci(ideFunci);
    }

    /**
     * @param ideFunci
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public FuncionarioDTO consultarFuncionarioByIdeFunci(BigInteger ideFunci) throws BusinessException, SystemException {
        return control.consultarFuncionarioByIdeFunci(ideFunci);
    }

    /**
     *
     * @param login
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public String firmaPoliticaFuncionarioByLoginName (String login) throws  BusinessException, SystemException {
        return control.firmaPoliticaFuncionarioByLoginName(login);
    }
}
