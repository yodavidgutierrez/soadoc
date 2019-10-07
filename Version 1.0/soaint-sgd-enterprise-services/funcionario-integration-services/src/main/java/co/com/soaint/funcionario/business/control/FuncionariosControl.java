package co.com.soaint.funcionario.business.control;

import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.apis.delegator.funcionarios.FuncionariosWebApiClient;
import co.com.soaint.funcionario.apis.delegator.roles.RolesWebApiClient;
import co.com.soaint.funcionario.apis.delegator.security.SecurityApiClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 28-Ago-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class FuncionariosControl {

    @Autowired
    FuncionariosWebApiClient funcionariosWebApiClient;

    @Autowired
    RolesWebApiClient rolesWebApiClient;

    @Autowired
    SecurityApiClient securityApiClient;

    /**
     * @param credenciales
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public FuncionarioDTO verificarCredenciales(CredencialesDTO credenciales) throws BusinessException, SystemException {

      FuncionarioDTO funcionario;
        try {
            FuncionarioDTO usuario = securityApiClient.verificarCredenciales(credenciales);
            funcionario = funcionariosWebApiClient.listarFuncionarioByLoginName(usuario.getLoginName());
            log.info("usuario que devuelve---------------------------------{}", usuario);
            if (!ObjectUtils.isEmpty(funcionario)) {
                //funcionario.setRoles(usuario.getRoles());
                    funcionario.setRoles(rolesWebApiClient.listarRolesByLoginName(usuario.getLoginName()));

                log.info("funcionario que devuelve---------------------------------{}", funcionario);
                return funcionario;
            } else {
                FuncionarioDTO funcionarioDTO = FuncionarioDTO.newInstance()
                        .loginName(credenciales.getLoginName())
                        .password(credenciales.getPassword())
                      //  .roles(usuario.getRoles()) sio existe el funcionario no debe de tener roles
                        .corrElectronico(usuario.getCorrElectronico())
                        .dependencias(usuario.getDependencias())
                        .codTipDocIdent(StringUtils.isEmpty(usuario.getCodTipDocIdent()) ? "CTDI" : usuario.getCodTipDocIdent())
                        .estado(StringUtils.isEmpty(usuario.getEstado()) ? "A" : usuario.getEstado())
                        .nomFuncionario(StringUtils.isEmpty(usuario.getNomFuncionario()) ? credenciales.getLoginName() : usuario.getNomFuncionario())
                        .nroIdentificacion(usuario.getNroIdentificacion())
                        .usuarioCrea(usuario.getUsuarioCrea())
                        .valApellido1(usuario.getValApellido1())
                        .valApellido2(usuario.getValApellido2())
                        .ideFunci(usuario.getIdeFunci())
                        .cargo(usuario.getCargo())
                        .build();
                funcionariosWebApiClient.crearFuncionario(funcionarioDTO);
                log.info("funcionarioDTO que devuelve---------------------------------{}", funcionarioDTO);
                return funcionarioDTO;
            }
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param codDependencia
     * @param rol
     * @param codEstado
     * @return
     * @throws SystemException
     */
    public FuncionariosDTO listarFuncionariosByDependenciaAndRolAndEstado(String codDependencia, String rol, String codEstado) throws BusinessException, SystemException {
        List<FuncionarioDTO> usuariosRol = listarUsuariosByRol(rol);
        List<FuncionarioDTO> funcionariosDependencia = listarFuncionariosByCodDependenciaAndEstado(codDependencia, codEstado);
        List<FuncionarioDTO> resultado = new ArrayList<>();
        if (!usuariosRol.isEmpty() && !funcionariosDependencia.isEmpty()) {
            funcionariosDependencia.stream().forEach(funcionarioDTO -> {
                List<RolDTO> roles = obtenerRolesFromList(usuariosRol, funcionarioDTO.getLoginName());
                if (roles != null) {
                    funcionarioDTO.setRoles(roles);
                    resultado.add(funcionarioDTO);
                }
            });
        }
        return FuncionariosDTO
                .newInstance()
                .funcionarios(resultado)
                .build();

      /*  List<RolDTO> roles =new ArrayList<>();
        RolDTO rol1 = RolDTO.newInstance()
                .rol("bpmadmin")
                .build();
        RolDTO rol2 = RolDTO.newInstance()
                .rol("bpmmanager")
                .build();
        RolDTO rol3 = RolDTO.newInstance()
                .rol("bpmradicador")
                .build();
        RolDTO rol4 = RolDTO.newInstance()
                .rol("Radicador")
                .build();
        RolDTO rol5 = RolDTO.newInstance()
                .rol("Proyector")
                .build();
        RolDTO rol6 = RolDTO.newInstance()
                .rol("Receptor")
                .build();
        RolDTO rol7 = RolDTO.newInstance()
                .rol("Archivista")
                .build();
        RolDTO rol8 = RolDTO.newInstance()
                .rol("Gestor_devoluciones")
                .build();
        RolDTO rol9 = RolDTO.newInstance()
                .rol("Aprobador")
                .build();
        RolDTO rol10 = RolDTO.newInstance()
                .rol("Revisor")
                .build();
        RolDTO rol11 = RolDTO.newInstance()
                .rol("Auxiliar_correspondencia")
                .build();
        RolDTO rol12 = RolDTO.newInstance()
                .rol("bpmadmin")
                .build();
        RolDTO rol13 = RolDTO.newInstance()
                .rol("bpmanalyst")
                .build();
        RolDTO rol14 = RolDTO.newInstance()
                .rol("bpmdeveloper")
                .build();
        RolDTO rol15 = RolDTO.newInstance()
                .rol("bpmmanager")
                .build();
        RolDTO rol16 = RolDTO.newInstance()
                .rol("bpmradicador")
                .build();
        RolDTO rol17 = RolDTO.newInstance()
                .rol("bpmuser")
                .build();
        RolDTO rol18 = RolDTO.newInstance()
                .rol("Asignador")
                .build();
       roles.add(rol1);
        roles.add(rol2);
        roles.add(rol3);
        roles.add(rol4);
        roles.add(rol5);
        roles.add(rol6);
        roles.add(rol7);
        roles.add(rol8);
        roles.add(rol9);
        roles.add(rol11);
        roles.add(rol10);
        roles.add(rol12);
        roles.add(rol13);
        roles.add(rol14);
        roles.add(rol15);
        roles.add(rol16);
        roles.add(rol17);
        roles.add(rol18);

        FuncionarioDTO funcionarioDTO = FuncionarioDTO.newInstance()
                .ideFunci(new BigInteger("10"))
                .nomFuncionario("pruebasoaint1")
                .codTipDocIdent("CTDI")
                .estado("A")
                .roles(roles)
                .loginName("pruebasoaint1")
                .usuarioCrea("pruebasoaint1")
                .build();
        FuncionarioDTO funcionarioDTO3 = FuncionarioDTO.newInstance()
                .ideFunci(new BigInteger("13"))
                .nomFuncionario("pruebasoaint3")
                .codTipDocIdent("CTDI")
                .estado("A")
                .roles(roles)
                .loginName("pruebasoaint3")
                .usuarioCrea("pruebasoaint3")
                .build();
        FuncionarioDTO funcionarioDTO4 = FuncionarioDTO.newInstance()
                .ideFunci(new BigInteger("14"))
                .nomFuncionario("pruebasoaint4")
                .codTipDocIdent("CTDI")
                .estado("A")
                .roles(roles)
                .loginName("pruebasoaint4")
                .usuarioCrea("pruebasoaint4")
                .build();
        FuncionarioDTO funcionarioDTO1 = FuncionarioDTO.newInstance()
                .ideFunci(new BigInteger("11"))
                .nomFuncionario("pruebasoaint2")
                .codTipDocIdent("CTDI")
                .usuarioCrea("pruebasoaint2")
                .loginName("pruebasoaint2")
                .roles(roles)
                .estado("A")

                .build();
        List<FuncionarioDTO> lista =  new ArrayList<>();
        lista.add(funcionarioDTO);
        lista.add(funcionarioDTO1);
        lista.add(funcionarioDTO3);
        lista.add(funcionarioDTO4);
        log.info("Business Control - a business error has occurred {}", lista);
        return FuncionariosDTO.newInstance().funcionarios(lista).build();*/

    }

    private List<RolDTO> obtenerRolesFromList(List<FuncionarioDTO> funcionarios, String loginName) {
        List<RolDTO> roles = null;
        FuncionarioDTO funcionario = funcionarios.stream().filter(x -> x.getLoginName().equals(loginName)).findFirst().orElse(null);
        if (funcionario != null)
            roles = funcionario.getRoles();
        return roles;
    }

    /**
     * @param codDependencia
     * @param codEstado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public List<FuncionarioDTO> listarFuncionariosByCodDependenciaAndEstado(String codDependencia, String codEstado) throws BusinessException, SystemException {
        try {
            List<FuncionarioDTO> funcionariosDepenendencia = funcionariosWebApiClient.listarFuncionariosByDependenciaAndEstado(codDependencia, codEstado).getFuncionarios();
            for (FuncionarioDTO funcionarioActual : funcionariosDepenendencia) {
               // List<RolDTO> rolDTOS = securityApiClient.obtenerRolesUsuario(funcionarioActual.getLoginName());
                List<RolDTO> rolDTOS = rolesWebApiClient.listarRolesByLoginName(funcionarioActual.getLoginName());
                log.info("processing rest request - funcionarios " + rolDTOS.size());
              /*  List<RolDTO> roles =new ArrayList<>();
                RolDTO rol8 = RolDTO.newInstance()
                        .rol("Gestor_Devoluciones")
                        .build();
                RolDTO rol18 = RolDTO.newInstance()
                        .rol("Asignador")
                        .build();
                roles.add(rol8);
                roles.add(rol18);
                rolDTOS.addAll(roles);*/
                log.info("------------------los roles del funcionario {}" + rolDTOS);
                funcionarioActual.setRoles(rolDTOS);
            }
            if (funcionariosDepenendencia.isEmpty())
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.funcionario_not_exist_by_codDependencia")
                        .buildBusinessException();
            return funcionariosDepenendencia;
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param rol
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public List<FuncionarioDTO> listarUsuariosByRol(String rol) throws BusinessException, SystemException {
        try {
           // List<FuncionarioDTO> usuariosRol = securityApiClient.listarUsusriosByRol(rol);
            List<FuncionarioDTO> usuariosRol = funcionariosWebApiClient.listarFuncionariosByRol(rol);
            if (ObjectUtils.isEmpty(usuariosRol))
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.funcionario_not_exist_by_rol")
                        .buildBusinessException();
            return usuariosRol;
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param funcionario
     * @throws SystemException
     */
    public String crearFuncionario(FuncionarioDTO funcionario) throws SystemException {
        try {
            securityApiClient.crearFuncionario(funcionario);
            return funcionariosWebApiClient.crearFuncionario(funcionario);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param funcionario
     * @throws SystemException
     */
    public String actualizarFuncionario(FuncionarioDTO funcionario) throws SystemException {
        try {
            if (funcionario.getPassword().isEmpty()) {
                funcionario.setPassword(null);
            }

            securityApiClient.actualizarFuncionario(funcionario);
            return funcionariosWebApiClient.actualizarFuncionario(funcionario);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param idFuncionario
     * @throws SystemException
     */
    public String eliminarFuncionario(BigInteger idFuncionario) throws SystemException {
        try {
            //securityApiClient.crearFuncionario();
            //funcionariosWebApiClient.crearFuncionario(funcionario);
            //TODO Implementacion
            return "1";
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @throws SystemException
     */
    public List<RolDTO> obtenerRoles() throws SystemException {
        try {
            List<RolDTO> rolesDTO =new ArrayList<>();
     /*       List<RolDTO> roles =new ArrayList<>();
            RolDTO rol8 = RolDTO.newInstance()
                    .rol("Gestor_Devoluciones")
                    .build();
            RolDTO rol18 = RolDTO.newInstance()
                    .rol("Asignador")
                    .build();
            roles.add(rol8);
            roles.add(rol18);
           //rolesDTO = securityApiClient.obtenerRoles();
            rolesDTO.addAll(roles);*/
            rolesDTO = rolesWebApiClient.listarRoles();
            log.info("estos son los roles {}", rolesDTO);
            return rolesDTO;
        } catch (SystemException e) {
            log.error("Business Control - a business error has occurred {}", e);
            throw e;
        }
    }

    /**
     * @param funcionarioDTO
     * @return
     * @throws SystemException
     */
    public FuncionariosDTO buscarFuncionario(FuncionarioDTO funcionarioDTO) throws SystemException {
        try {
            FuncionariosDTO funcionarios = funcionariosWebApiClient.buscarFuncionario(funcionarioDTO);
            for (FuncionarioDTO funcionarioActual : funcionarios.getFuncionarios()) {
                funcionarioActual.setRoles(new ArrayList<>());
                List<RolDTO> rolesDTO = rolesWebApiClient.listarRolesByLoginName(funcionarioActual.getLoginName());

                funcionarioActual.setRoles(rolesDTO);
            }
            return funcionarios;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param ideFunci
     * @return
     * @throws SystemException
     */
    public FuncionarioDTO consultarFuncionarioByIdeFunci(BigInteger ideFunci) throws SystemException {
        try {
            log.info("-------------------------------- esto devuelve el api consultarFuncionarioByIdeFunci {}",funcionariosWebApiClient.consultarFuncionarioByIdeFunci(ideFunci));
            return funcionariosWebApiClient.consultarFuncionarioByIdeFunci(ideFunci);

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param loginNames
     * @return
     * @throws SystemException
     */
    public FuncionariosDTO listarFuncionariosByLoginNameList(String loginNames) throws SystemException {
        try {
            FuncionariosDTO funcionarios = funcionariosWebApiClient.listarFuncionariosByLoginNameList(loginNames);
            for (FuncionarioDTO funcionario : funcionarios.getFuncionarios()) {
                funcionario.setRoles(rolesWebApiClient.listarRolesByLoginName(funcionario.getLoginName()));
            }
            return funcionarios;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public String buscarFirmaLogin(String login) {
        try {
            log.info("-------------------------------- buscarFirmaLogin {}");
            return funcionariosWebApiClient.buscarFirmaLogin(login);

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            return "";
        }
    }
}
