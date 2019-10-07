package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsConstantes;
import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.foundation.canonical.correspondencia.ConstantesDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 03-Ago-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessControl
@Log4j2
public class ConstantesControl {

    // [fields] -----------------------------------

    @PersistenceContext
    private EntityManager em;


    // ----------------------

    /**
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByEstado(String estado) throws SystemException {
        try {
            return em.createNamedQuery("TvsConstantes.findAll", ConstanteDTO.class)
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param codigo
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByCodigoAndEstado(String codigo, String estado) throws SystemException {
        try {
            return em.createNamedQuery("TvsConstantes.findAllByCodigoAndEstado", ConstanteDTO.class)
                    .setParameter("CODIGO", codigo)
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param codPadre
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByCodPadreAndEstado(String codPadre, String estado) throws SystemException {
        try {
            return em.createNamedQuery("TvsConstantes.findAllByCodPadreAndEstado", ConstanteDTO.class)
                    .setParameter("COD_PADRE", codPadre)
                    .setParameter("ESTADO", estado)
                    .getResultList();
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
    public ConstantesDTO listarConstantesByCodigo(String[] codigos) throws SystemException {
        List<ConstanteDTO> constanteDTOList = new ArrayList<>();
        try {
            constanteDTOList = em.createNamedQuery("TvsConstantes.findAllByCodigo", ConstanteDTO.class)
                    .setParameter("CODIGOS", Arrays.asList(codigos))
                    .getResultList();
            return ConstantesDTO.newInstance().constantes(constanteDTOList).build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional
    public Boolean adicionarConstante(ConstanteDTO constante) {
        try {
            TvsConstantes tvsConstantes = TvsConstantes.newInstance()
                    .codigo(constante.getCodigo())
                    .codPadre(constante.getCodPadre())
                    .estado(constante.getEstado())
                    .nombre(constante.getNombre())
                    .build();
            em.persist(tvsConstantes);
            return true;
        } catch (Exception ex) {
            log.error("Business Control - a ocurrido un error creando la tabla TvsConstantes", ex);
            return false;
        }
    }

    @Transactional
    public Boolean actualizarConstante(ConstanteDTO constante) {
        try {
            if (!ObjectUtils.isEmpty(constante.getIdeConst()) && !ObjectUtils.isEmpty(constante.getNombre()) && !ObjectUtils.isEmpty(constante.getEstado())) {
                em.createNamedQuery("TvsConstantes.update")
                        .setParameter("NOMBRE", constante.getNombre())
                        .setParameter("ESTADO", constante.getEstado())
                        .setParameter("ID", constante.getIdeConst())
                        .executeUpdate();
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            log.error("Business Control - a ocurrido un error actualizando la tabla TvsConstantes", ex);
            return false;
        }
    }

    @Transactional
    public Boolean eliminarConstante(BigInteger idConstante) {
        try {


            TvsConstantes tvsConstante = em.find(TvsConstantes.class, idConstante);
            em.remove(tvsConstante);
            return true;
        } catch (Exception ex) {
            log.error("Business Control - a ocurrido un error eliminando en la tabla TvsConstantes", ex);
            return false;
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ConstanteDTO consultarConstanteByCodigo(String codigo) throws BusinessException, SystemException {
        try {
            log.info("request consultarConstanteByCodigo {}", codigo);
            List<ConstanteDTO> codigo1 = em.createNamedQuery("TvsConstantes.findByCodigo", ConstanteDTO.class)
                    .setParameter("CODIGO", codigo)
                    .getResultList();
            if(ObjectUtils.isEmpty(codigo1)){
                throw new SystemException("No existe constante por ese código "+codigo);
            }
            return codigo1.get(0);
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("constante.constante_not_exist_by_codigo")
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
    public String consultarNombreConstanteByCodigo(String codigo) {
        if (!StringUtils.isEmpty(codigo)) {
            try {
                final List<String> resultList = em.createNamedQuery("TvsConstantes.update", String.class)
                        .setParameter("CODIGO", codigo)
                        .getResultList();
                return !resultList.isEmpty() ? resultList.get(0) : null;
            } catch (Exception e) {
                log.error("Business Control - a system error has occurred {}", e);
            }
        }
        return null;
    }

    public List<ConstanteDTO> buscarHijos(String codigo, String nombre) {

        if (!StringUtils.isEmpty(codigo)) {
            List<String> codigos = Arrays.asList(codigo.split(","));
            log.info("############## request codigos ###############" + codigos + "$$$$$$$$$$$" + nombre);
            List<ConstanteDTO> constantes = em.createNamedQuery("TvsConstantes.findAllByCodigoPadre", ConstanteDTO.class)
                    .setParameter("CODIGOS", codigos)
                    .setParameter("NOMBRE", "%" + nombre + "%")
                    .getResultList();
            log.info("############## respuesta constantes ###############" + constantes);
            if (!ObjectUtils.isEmpty(constantes)) {
                return constantes;
            } else {
                return new ArrayList<ConstanteDTO>();
            }
        }
        return new ArrayList<ConstanteDTO>();
    }
}
