package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsOrganigramaAdministrativo;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.DependenciasDTO;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 21-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class DependenciaControl {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    OrganigramaAdministrativoControl organigramaAdministrativoControl;

    /**
     * @param dependencia
     * @param sede
     * @return
     */
    public DependenciaDTO dependenciaDTOTransform(OrganigramaItemDTO dependencia, OrganigramaItemDTO sede) {
        return DependenciaDTO.newInstance()
                .ideDependencia(dependencia.getIdeOrgaAdmin())
                .codDependencia(dependencia.getCodOrg())
                .nomDependencia(dependencia.getNomOrg())
                .estado(dependencia.getEstado())
                .ideSede(sede.getIdeOrgaAdmin())
                .codSede(sede.getCodOrg())
                .nomSede(sede.getNomOrg())
                .radicadora(dependencia.isRadicadora())
                .build();
    }

    /**
     * @param ideFunci
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<DependenciaDTO> obtenerDependenciasByFuncionario(BigInteger ideFunci) {
        return em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDependenciasByIdFunci", DependenciaDTO.class)
                .setParameter("ID_FUNCI", ideFunci).getResultList();
    }

    /**
     * @param codOrg
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public DependenciaDTO listarDependenciaByCodigo(String codOrg) throws BusinessException, SystemException {
        try {
            OrganigramaItemDTO organigramaItemDTO = organigramaAdministrativoControl.consultarElementoByCodOrg(codOrg);
            if (!ObjectUtils.isEmpty(organigramaItemDTO)) {
                return dependenciaDTOTransform(organigramaItemDTO, organigramaAdministrativoControl.consultarPadreDeSegundoNivel(organigramaItemDTO.getCodOrg()));
            } else {
                return DependenciaDTO.newInstance().build();
            }
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("dependencia.dependencia_not_exist_by_codOrg")
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

    public DependenciaDTO listarDependenciaByCodigoObj(String codOrg) throws BusinessException, SystemException {
        try {
            OrganigramaItemDTO organigramaItemDTO = organigramaAdministrativoControl.consultarElementoByCodOrg(codOrg);
            if (!ObjectUtils.isEmpty(organigramaItemDTO)) {
                return dependenciaDTOTransform(organigramaItemDTO, organigramaAdministrativoControl.consultarPadreDeSegundoNivel(organigramaItemDTO.getCodOrg()));
            } else {
                return DependenciaDTO.newInstance().build();
            }
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("dependencia.dependencia_not_exist_by_codOrg")
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
     * @param codigos
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public DependenciasDTO listarDependenciasByCodigo(String[] codigos) throws SystemException {
        List<DependenciaDTO> dependencias = new ArrayList<>();
        try {
            organigramaAdministrativoControl.consultarElementosByCodOrg(codigos).stream().forEach(organigramaItemDTO ->
                    dependencias.add(dependenciaDTOTransform(organigramaItemDTO, organigramaAdministrativoControl.consultarPadreDeSegundoNivel(organigramaItemDTO.getCodOrg()))));
            return DependenciasDTO.newInstance().dependencias(dependencias).build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public DependenciasDTO listarDependencias() throws SystemException {
        List<DependenciaDTO> dependencias = new ArrayList<>();
        try {
            for (OrganigramaItemDTO sede : organigramaAdministrativoControl.listarDescendientesDirectosDeElementoRayz()) {
                for (OrganigramaItemDTO dependencia : organigramaAdministrativoControl.listarElementosDeNivelInferior(new ArrayList<>(), sede.getCodOrg())) {
                    dependencias.add(dependenciaDTOTransform(dependencia, sede));
                }
            }
            return DependenciasDTO.newInstance().dependencias(dependencias).build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public TvsOrganigramaAdministrativo consultarOrganigramaCodigo(String codOrg){
      return  organigramaAdministrativoControl.consultarOrganigramaCodigo(codOrg);
    }

    public OrganigramaItemDTO obtenerDependenciaRadicadora(){

        List<OrganigramaItemDTO> resultList = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDependenciaRadicadora", OrganigramaItemDTO.class)
                .getResultList();
        if (ObjectUtils.isEmpty(resultList)){
            return new OrganigramaItemDTO();
        }
        return resultList.get(0);
    }
}
