package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.AuditColumns;
import co.com.soaint.correspondencia.domain.entity.CorRol;
import co.com.soaint.correspondencia.domain.entity.Funcionarios;
import co.com.soaint.correspondencia.domain.entity.TvsOrgaAdminXFunciPk;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.*;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 03-Ago-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class FuncionariosControl extends GenericControl<Funcionarios> {

    // [fields] -----------------------------------

    @Autowired
    DependenciaControl dependenciaControl;

    public FuncionariosControl() {
        super(Funcionarios.class);
    }
    // ----------------------

    /**
     * @param loginName
     * @param estado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionarioDTO listarFuncionarioByLoginNameAndEstado(String loginName, String estado) throws BusinessException, SystemException {
        try {
            FuncionarioDTO funcionario = em.createNamedQuery("Funcionarios.findByLoginNameAndEstado", FuncionarioDTO.class)
                    .setParameter("LOGIN_NAME", loginName)
                    .setParameter("ESTADO", estado)
                    .getSingleResult();
            funcionario.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionario.getIdeFunci()));
            return funcionario;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("funcionarios.funcionario_not_exist_by_loginName_and_estado")
                    .withRootException(n)
                    .buildBusinessException();
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
     * @param codEstado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionariosDTO listarFuncionariosByCodDependenciaAndCodEstado(String codDependencia, String codEstado) throws SystemException {
        //List<FuncionarioDTO> funcionarioDTOList = new ArrayList<>();
        try {
            List<FuncionarioDTO> resultList = em.createNamedQuery("Funcionarios.findAllByCodOrgaAdmiAndEstado", FuncionarioDTO.class)
                    .setParameter("COD_ORGA_ADMI", codDependencia)
                    .setParameter("ESTADO", codEstado)
                    .getResultList();
                    /*.forEach(funcionarioDTO -> {
                        funcionarioDTO.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionarioDTO.getIdeFunci()));
                        funcionarioDTOList.add(funcionarioDTO);
                    });*/

            return FuncionariosDTO.newInstance().funcionarios(resultList).build();
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
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FuncionarioDTO> listarFuncionariosByCodDependencia(String codDependencia) throws SystemException {
        List<FuncionarioDTO> funcionarioDTOList = new ArrayList<>();
        try {
            em.createNamedQuery("Funcionarios.findAllByCodOrgaAdmi", FuncionarioDTO.class)
                    .setParameter("COD_ORGA_ADMI", codDependencia)
                    .getResultList()
                    .forEach(funcionarioDTO -> {
                        funcionarioDTO.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionarioDTO.getIdeFunci()));
                        funcionarioDTOList.add(funcionarioDTO);
                    });

            return funcionarioDTOList;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param loginName
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionarioDTO listarFuncionarioByLoginName(String loginName) throws SystemException {
        try {
            List<FuncionarioDTO> funcionarioDTOS = em.createNamedQuery("Funcionarios.findByLoginName", FuncionarioDTO.class)
                    .setParameter("LOGIN_NAME", loginName)
                    .getResultList();
            if (!funcionarioDTOS.isEmpty()) {
                final FuncionarioDTO funcionario = funcionarioDTOS.get(0);
                funcionario.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionario.getIdeFunci()));
                return funcionario;
            }
            return null;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("Error ocurrido " + ex)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String  firmaPoliticaFuncionarioByLoginName(String loginName) throws SystemException {
        try {
            String funcionarioDTOS = em.createNamedQuery("Funcionarios.findFirmaPoliticaByLoginName", String.class)
                    .setParameter("LOGIN_NAME", loginName)
                    .getSingleResult();
            if (!funcionarioDTOS.isEmpty()) {
                return funcionarioDTOS;
            }
            return "4004";
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("Error ocurrido " + ex)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FuncionarioDTO> listarFuncionarioByRol(String rol) throws SystemException {
        try {
            List<FuncionarioDTO> funcionarioDTOS = em.createNamedQuery("Funcionarios.findByRol", FuncionarioDTO.class)
                    .setParameter("ROL", rol)
                    .getResultList();
            if (!funcionarioDTOS.isEmpty()) {
                for (FuncionarioDTO funcionarioDTO : funcionarioDTOS) {
                    final FuncionarioDTO funcionario = funcionarioDTOS.get(0);
                    funcionario.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionario.getIdeFunci()));
                }

                return funcionarioDTOS;
            }
            return new ArrayList<>();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("Error ocurrido " + ex)
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param loginNames
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionariosDTO listarFuncionariosByLoginNameList(String[] loginNames) throws SystemException {
        try {
            FuncionariosDTO funcionarios = FuncionariosDTO.newInstance().funcionarios(em.createNamedQuery("Funcionarios.findByLoginNamList", FuncionarioDTO.class)
                    .setParameter("LOGIN_NAMES", Arrays.asList(loginNames))
                    .getResultList())
                    .build();

            for (FuncionarioDTO funcionarioDTO : funcionarios.getFuncionarios()) {
                funcionarioDTO.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionarioDTO.getIdeFunci()));
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
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean existFuncionarioByIdeFunci(BigInteger ideFunci) throws SystemException {
        try {
            return em.createNamedQuery("Funcionarios.existFuncionarioByIdeFunci", Long.class)
                    .setParameter("IDE_FUNCI", ideFunci)
                    .getSingleResult() > 0;
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
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionarioDTO consultarFuncionarioByIdeFunci(BigInteger ideFunci) throws SystemException {
        log.info("processing rest request - ideFunci: " + ideFunci);
        try {
            if (null == ideFunci)
                return null;
            List<FuncionarioDTO> resultList = em.createNamedQuery("Funcionarios.findByIdeFunci", FuncionarioDTO.class)
                    .setParameter("IDE_FUNCI", ideFunci)
                    .getResultList();
            if (!resultList.isEmpty()) {
                final FuncionarioDTO funcionario = resultList.get(0);
                funcionario.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionario.getIdeFunci()));
                log.info("processing rest response - funcionario: " + funcionario);
                return funcionario;
            }
            return null;

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param nroIdentificacion
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FuncionarioDTO> consultarFuncionarioByNroIdentificacion(String nroIdentificacion) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("Funcionarios.findByNroIdentificacion", FuncionarioDTO.class)
                    .setParameter("NRO_IDENTIFICACION", nroIdentificacion)
                    .getResultList();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("funcionarios.funcionario_not_exist_by_ideFunci")
                    .withRootException(n)
                    .buildBusinessException();
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
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String consultarCredencialesByIdeFunci(BigInteger ideFunci) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("Funcionarios.consultarCredencialesByIdeFunci", String.class)
                    .setParameter("IDE_FUNCI", ideFunci)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("funcionarios.funcionario_not_exist_by_ideFunci")
                    .withRootException(n)
                    .buildBusinessException();
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
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String consultarLoginNameByIdeFunci(BigInteger ideFunci) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("Funcionarios.consultarLoginNameByIdeFunci", String.class)
                    .setParameter("IDE_FUNCI", ideFunci)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("funcionarios.funcionario_not_exist_by_ideFunci")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String consultarEmailByIdeFunci(BigInteger ideFunci) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("Funcionarios.consultarEmailByIdeFunci", String.class)
                    .setParameter("IDE_FUNCI", ideFunci)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("funcionarios.funcionario_not_exist_by_ideFunci")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param funcionarioDTO
     * @throws SystemException
     */
    public void crearFuncionario(FuncionarioDTO funcionarioDTO) throws SystemException {
        try {
            Funcionarios funcionario = funcionarioTransform(funcionarioDTO);
            if(!ObjectUtils.isEmpty(funcionarioDTO.getDependencias())|| funcionarioDTO.getDependencias() != null){
                funcionario.setTvsOrgaAdminXFunciPkList(new ArrayList<>());
                final List<DependenciaDTO> funcionarioDTODependencias = funcionarioDTO.getDependencias();
                for (DependenciaDTO dependenciaDTO : funcionarioDTODependencias) {
                    TvsOrgaAdminXFunciPk tvsOrgaAdminXFunciPk = new TvsOrgaAdminXFunciPk();
                    tvsOrgaAdminXFunciPk.setCodOrgaAdmi(dependenciaDTO.getCodDependencia());
                    tvsOrgaAdminXFunciPk.setFuncionarios(funcionario);
                    funcionario.getTvsOrgaAdminXFunciPkList().add(tvsOrgaAdminXFunciPk);
                }
            }
            AuditColumns audit = AuditColumns.newInstance()
                    .fecCreacion(GregorianCalendar.getInstance().getTime())
                    .estado(StringUtils.isEmpty(funcionarioDTO.getEstado()) ? "A" : funcionarioDTO.getEstado())
                    .build();
            funcionario.setAuditColumns(audit);
            em.persist(funcionario);
            em.flush();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param funciDto
     * @return
     * @throws SystemException
     */
    public String actualizarFuncionario(FuncionarioDTO funciDto) {
        try {
            if (ObjectUtils.isEmpty(funciDto)) {
                return "No se procesa la informacion FuncionarioDTO = null";
            }
            final BigInteger idFunciInteger = funciDto.getIdeFunci();
            final Funcionarios funciEntity = em.find(Funcionarios.class, idFunciInteger);

            if (ObjectUtils.isEmpty(funciEntity)) {
                return "No existe funciDto con id = '" + idFunciInteger + "'";
            }

            //final String encodePassword = encodePassword(funciDto.getLoginName(), funciDto.getPassword());
            //funciEntity.setCredenciales(encodePassword);

            funciEntity.setCodTipDocIdent(StringUtils.isEmpty(funciDto.getCodTipDocIdent()) ? funciEntity.getCodTipDocIdent() : funciDto.getCodTipDocIdent());
            funciEntity.setNroIdentificacion(StringUtils.isEmpty(funciDto.getNroIdentificacion()) ? funciEntity.getNroIdentificacion() : funciDto.getNroIdentificacion());
            funciEntity.setNomFuncionario(StringUtils.isEmpty(funciDto.getNomFuncionario()) ? funciEntity.getNomFuncionario() : funciDto.getNomFuncionario());
            funciEntity.setValApellido1(StringUtils.isEmpty(funciDto.getValApellido1()) ? funciEntity.getValApellido1() : funciDto.getValApellido1());
            funciEntity.setValApellido2(StringUtils.isEmpty(funciDto.getValApellido2()) ? funciEntity.getValApellido2() : funciDto.getValApellido2());
            funciEntity.setCorrElectronico(StringUtils.isEmpty(funciDto.getCorrElectronico()) ? funciEntity.getCorrElectronico() : funciDto.getCorrElectronico());
            AuditColumns auditColumns = AuditColumns.newInstance()
                    .estado(StringUtils.isEmpty(funciDto.getEstado()) ? funciEntity.getAuditColumns().getEstado() : funciDto.getEstado())
                    .fecCambio(new Date())
                    .build();
            funciEntity.setAuditColumns(auditColumns);
            //funciEntity.setFirmaPolitica(StringUtils.isEmpty(funciDto.getFirmaPolitica()) ? funciEntity.getFirmaPolitica() : funciDto.getFirmaPolitica());
            funciEntity.setFirmaPolitica(funciDto.getFirmaPolitica());
            funciEntity.setEsJefe(StringUtils.isEmpty(funciDto.getEsJefe()) ? funciEntity.getEsJefe() : funciDto.getEsJefe());
            funciEntity.setCargo(StringUtils.isEmpty(funciDto.getCargo()) ? funciEntity.getCargo() : funciDto.getCargo());
            if (!ObjectUtils.isEmpty(funciDto.getRoles())) {
                List<CorRol> roles = new ArrayList<>();
                for (RolDTO rol : funciDto.getRoles()) {
                    CorRol corRol = CorRol.newInstance()
                            .nombre(rol.getRol())
                            .fecCreacion(new Date())
                            .build();
                    roles.add(corRol);
                    corRol.setFuncionarios(funciEntity);
                }
                funciEntity.getCorRolesList().removeAll(funciEntity.getCorRolesList());
                funciEntity.getCorRolesList().clear();

                funciEntity.getCorRolesList().addAll(roles);
            }

           /* em.createNamedQuery("Funcionarios.update")
                    .setParameter("IDE_FUNCI", idFunciInteger)
                    .setParameter("COD_TIP_DOC_IDENT", StringUtils.isEmpty(funciDto.getCodTipDocIdent()) ? funciEntity.getCodTipDocIdent() : funciDto.getCodTipDocIdent())
                    .setParameter("NRO_IDENTIFICACION", StringUtils.isEmpty(funciDto.getNroIdentificacion()) ? funciEntity.getNroIdentificacion() : funciDto.getNroIdentificacion())
                    .setParameter("NOM_FUNCIONARIO", StringUtils.isEmpty(funciDto.getNomFuncionario()) ? funciEntity.getNomFuncionario() : funciDto.getNomFuncionario())
                    .setParameter("VAL_APELLIDO1", StringUtils.isEmpty(funciDto.getValApellido1()) ? funciEntity.getValApellido1() : funciDto.getValApellido1())
                    .setParameter("VAL_APELLIDO2", StringUtils.isEmpty(funciDto.getValApellido2()) ? funciEntity.getValApellido2() : funciDto.getValApellido2())
                    .setParameter("CORR_ELECTRONICO", StringUtils.isEmpty(funciDto.getCorrElectronico()) ? funciEntity.getCorrElectronico() : funciDto.getCorrElectronico())
                    .setParameter("ESTADO", StringUtils.isEmpty(funciDto.getEstado()) ? funciEntity.getAuditColumns().getEstado() : funciDto.getEstado())
                    .setParameter("FECHA_CAMBIO", GregorianCalendar.getInstance().getTime())
                    .setParameter("CREDENCIALES", StringUtils.isEmpty(encodePassword) ? funciEntity.getCredenciales() : encodePassword)
                    .setParameter("CARGO", StringUtils.isEmpty(funciDto.getCargo()) ? funciEntity.getCargo() : funciDto.getCargo())
                    .executeUpdate();*/
            em.merge(funciEntity);
            return "1";
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            return ex.getMessage();
        }
    }

    public boolean actualizarRolesFuncionario(FuncionarioDTO funcionario) throws SystemException {
        try {
            if (ObjectUtils.isEmpty(funcionario)) {
                return false;
            }
            if (ObjectUtils.isEmpty(funcionario.getRoles()) || ObjectUtils.isEmpty(funcionario.getIdeFunci())) {
                return false;
            }
            final Funcionarios funciEntity = em.find(Funcionarios.class, funcionario.getIdeFunci());

            if (ObjectUtils.isEmpty(funciEntity)) {
                return false;
            }

            List<CorRol> corRols = new ArrayList<>();
            for (RolDTO rol : funcionario.getRoles()) {
                CorRol corRol = CorRol.newInstance()
                        .nombre(rol.getRol())
                        .fecCreacion(new Date())
                        .build();
                corRols.add(corRol);
                corRol.setFuncionarios(funciEntity);
            }
            funciEntity.getCorRolesList().removeAll(funciEntity.getCorRolesList());
            funciEntity.getCorRolesList().clear();
            log.info("Estos son los roles que quedaron");
            funciEntity.getCorRolesList().addAll(corRols);

            em.merge(funciEntity);
            return true;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw new SystemException(ex.getMessage());
        }
    }

    /**
     * @param funcionario
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionariosDTO buscarFuncionario(FuncionarioDTO funcionario) throws SystemException {
        List<FuncionarioDTO> funcionarioDTOList = new ArrayList<>();
        try {
            em.createNamedQuery("Funcionarios.filter", FuncionarioDTO.class)
                    .setParameter("COD_TIP_DOC_IDENT", funcionario.getCodTipDocIdent())
                    .setParameter("NRO_IDENTIFICACION", funcionario.getNroIdentificacion())
                    .setParameter("NOM_FUNCIONARIO", funcionario.getNomFuncionario())
                    .setParameter("VAL_APELLIDO1", funcionario.getValApellido1())
                    .setParameter("VAL_APELLIDO2", funcionario.getValApellido2())
                    .setParameter("CORR_ELECTRONICO", funcionario.getCorrElectronico())
                    .setParameter("LOGIN_NAME", funcionario.getLoginName())
                    .setParameter("ESTADO", funcionario.getEstado())
                    .getResultList()
                    .stream()
                    .forEach(funcionarioDTO -> {
                        funcionarioDTO.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionarioDTO.getIdeFunci()));
                        funcionarioDTOList.add(funcionarioDTO);
                    });

            return FuncionariosDTO.newInstance().funcionarios(funcionarioDTOList).build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param funcionarioDTO
     * @return
     */
    public Funcionarios funcionarioTransform(FuncionarioDTO funcionarioDTO) {
        AuditColumns auditColumns = new AuditColumns();
        auditColumns.setEstado(funcionarioDTO.getEstado());
        auditColumns.setCodUsuarioCrea(funcionarioDTO.getUsuarioCrea());
        List<CorRol> corRoles = new ArrayList<>();
        if(funcionarioDTO.getRoles() != null) {
            for (RolDTO rol : funcionarioDTO.getRoles()) {
                CorRol corRol = CorRol.newInstance()
                        .nombre(rol.getRol())
                        .fecCreacion(new Date())
                        .build();
                corRoles.add(corRol);
            }
        }
        return Funcionarios.newInstance()
                .codTipDocIdent(funcionarioDTO.getCodTipDocIdent())
                .nroIdentificacion(funcionarioDTO.getNroIdentificacion())
                .nomFuncionario(funcionarioDTO.getNomFuncionario())
                .valApellido1(funcionarioDTO.getValApellido1())
                .valApellido2(funcionarioDTO.getValApellido2())
                .corrElectronico(funcionarioDTO.getCorrElectronico())
                .loginName(funcionarioDTO.getLoginName())
                .firmaPolitica(funcionarioDTO.getFirmaPolitica())
                .esJefe(funcionarioDTO.getEsJefe())
                .corRolesList(corRoles)
                .auditColumns(AuditColumns.newInstance()
                        .estado(StringUtils.isEmpty(funcionarioDTO.getEstado()) ? "A" : funcionarioDTO.getEstado())
                        .fecCreacion(GregorianCalendar.getInstance().getTime())
                        .build())
                .credenciales(encodePassword(funcionarioDTO.getLoginName(), funcionarioDTO.getPassword()))
                .build();
    }

    private String encodePassword(String username, String password) {
        return (null != username && null != password) ?
                java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes()) : null;
    }

}
