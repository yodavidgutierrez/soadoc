package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.Utils.DateUtils;
import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.correspondencia.domain.entity.DctAsigUltimo;
import co.com.soaint.correspondencia.domain.entity.DctAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
public class AsignacionControl {

    // [fields] -----------------------------------

    @PersistenceContext
    private EntityManager em;

    @Autowired
    DctAsignacionControl dctAsignacionControl;

    @Autowired
    DctAsigUltimoControl dctAsigUltimoControl;

    @Autowired
    CorrespondenciaControl correspondenciaControl;

    @Autowired
    FuncionariosControl funcionariosControl;

    @Autowired
    AgenteControl agenteControl;

    @Value("${radicado.cant.horas.para.activar.alerta.vencimiento}")
    private int cantHorasParaActivarAlertaVencimiento;

    // ----------------------

    /**
     * @param asignacionTramite
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional
    public AsignacionesDTO asignarCorrespondencia(AsignacionTramiteDTO asignacionTramite) throws BusinessException, SystemException {

        AsignacionesDTO asignacionesDTOResult = AsignacionesDTO.newInstance()
                .asignaciones(new ArrayList<>())
                .build();
        try {
            for (AsignacionDTO asignacionDTO : asignacionTramite.getAsignaciones().getAsignaciones()) {
                Date fecha = DateUtils.SetTimeZone("yyyy-MM-dd HH:mm:ss.SSS", new Date(), TimeZone.getTimeZone("America/Bogota"));
                CorCorrespondencia correspondencia = em.createNamedQuery("CorCorrespondencia.findByNroRadicadoObj", CorCorrespondencia.class)
                        .setParameter("NRO_RADICADO", asignacionDTO.getNroRadicado())
                        .getSingleResult();

                CorAgente agente = em.createNamedQuery("CorAgente.findByIdeDocumentoObj", CorAgente.class)
                        .setParameter("IDE_DOCUMENTO", correspondencia.getIdeDocumento())
                        .getSingleResult();
                agente.setIdeFunci(asignacionDTO.getIdeFunci());
                em.merge(agente);

                List<DctAsignacionDTO> dctAsignacion = em.createNamedQuery("DctAsignacion.findByIdeAgente", DctAsignacionDTO.class)
                        .setParameter("IDE_AGENTE", asignacionDTO.getIdeAgente())
                        .getResultList();
                if(!ObjectUtils.isEmpty(dctAsignacion) && ObjectUtils.isEmpty(dctAsignacion.get(0).getIdeAsignacion())) {
                    em.createNamedQuery("DctAsignacion.asignarToFuncionario")
                            .setParameter("IDE_FUNCI", asignacionDTO.getIdeFunci())
                            .setParameter("IDE_ASIGNACION", dctAsignacion.get(0).getIdeAsignacion())
                            .executeUpdate();
                }
                agenteControl.actualizarEstadoAgente(AgenteDTO.newInstance()
                        .ideAgente(asignacionDTO.getIdeAgente())
                        .codEstado(EstadoAgenteEnum.ASIGNADO.getCodigo())
                        .build());
                CorrespondenciaDTO correspondenciaDTO = CorrespondenciaDTO.newInstance()
                        .codEstado("AS")
                        .nroRadicado(asignacionDTO.getNroRadicado())
                        .build();
                if("EE".equalsIgnoreCase(correspondencia.getCodTipoCmc())){
                    correspondenciaControl.actualizarEstadoCorrespondencia(correspondenciaDTO);
                }

                List<AsignacionDTO> resultList = em.createNamedQuery("DctAsigUltimo.findByIdeAgenteAndCodEstado", AsignacionDTO.class)
                        .setParameter("IDE_AGENTE", asignacionDTO.getIdeAgente())
                        .setParameter("COD_ESTADO", EstadoAgenteEnum.ASIGNADO.getCodigo())
                        .getResultList();
                AsignacionDTO asignacionDTOResult;
                if (!ObjectUtils.isEmpty(resultList)){
                    asignacionDTOResult = resultList.get(0);
                    asignacionDTOResult.setLoginName(asignacionDTO.getLoginName());
                asignacionDTOResult.setAlertaVencimiento(calcularAlertaVencimiento(
                        correspondenciaControl.consultarFechaVencimientoByIdeDocumento(asignacionDTO.getIdeDocumento()),
                        fecha)
                );
                asignacionesDTOResult.getAsignaciones().add(asignacionDTOResult);

                }
            }
            return asignacionesDTOResult;
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
     * @param asignacion
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarIdInstancia(AsignacionDTO asignacion) throws BusinessException, SystemException {
        try {
            DctAsigUltimo dctAsigUltimo = em.createNamedQuery("DctAsigUltimo.findByIdeAsignacion", DctAsigUltimo.class)
                    .setParameter("IDE_ASIGNACION", asignacion.getIdeAsignacion())
                    .getSingleResult();
            em.createNamedQuery("DctAsigUltimo.updateIdInstancia")
                    .setParameter("IDE_ASIG_ULTIMO", dctAsigUltimo.getIdeAsigUltimo())
                    .setParameter("ID_INSTANCIA", asignacion.getIdInstancia())
                    .executeUpdate();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("asignacion.asignacion_not_exist_by_ideAsignacion")
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
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AsignacionesDTO listarAsignacionesByFuncionarioAndNroRadicado(BigInteger ideFunci, String nroRadicado) throws BusinessException, SystemException {
        try {
            List<AsignacionDTO> asignacionDTOList = em.createNamedQuery("DctAsigUltimo.findByIdeFunciAndNroRadicado", AsignacionDTO.class)
                    .setParameter("IDE_FUNCI", ideFunci)
                    .setParameter("NRO_RADICADO", nroRadicado == null ? null : "%" + nroRadicado + "%")
                    .getResultList();
            if (asignacionDTOList.isEmpty()) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("asignacion.not_exist_by_idefuncionario_and_nroradicado")
                        .buildBusinessException();
            }
            return AsignacionesDTO.newInstance().asignaciones(asignacionDTOList).build();
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
     * @param asignacion
     * @throws SystemException
     */
    public void actualizarTipoProceso(AsignacionDTO asignacion) throws SystemException {
        try {
            em.createNamedQuery("DctAsigUltimo.updateTipoProceso")
                    .setParameter("COD_TIPO_PROCESO", asignacion.getCodTipProceso())
                    .setParameter("IDE_ASIG_ULTIMO", asignacion.getIdeAsigUltimo())
                    .executeUpdate();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param ideAgente
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncAsigDTO consultarAsignacionReasignarByIdeAgente(BigInteger ideAgente) throws SystemException {
        try {
            AsignacionDTO asignacion = em.createNamedQuery("DctAsigUltimo.consultarByIdeAgente", AsignacionDTO.class)
                    .setParameter("IDE_AGENTE", ideAgente)
                    .getSingleResult();
            asignacion.setLoginName(funcionariosControl.consultarLoginNameByIdeFunci(asignacion.getIdeFunci()));
            return FuncAsigDTO.newInstance()
                    .asignacion(asignacion)
                    .credenciales(funcionariosControl.consultarCredencialesByIdeFunci(asignacion.getIdeFunci()))
                    .build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    private String calcularAlertaVencimiento(Date fechaVencimientoTramite, Date fechaAsignacion) {
        int diferenciaMinutos = (int) ChronoUnit.MINUTES.between(convertToLocalDateTime(fechaAsignacion), convertToLocalDateTime(fechaVencimientoTramite));
        //int cantidadHoras = cantHorasParaActivarAlertaVencimiento * 60;
        /*if(diferenciaMinutos < cantidadHoras)
            diferenciaMinutos = cantidadHoras - diferenciaMinutos;
        else{
            diferenciaMinutos -= cantidadHoras;
        }*/
        return diferenciaMinutos > 0 ? String.valueOf(diferenciaMinutos / 60).concat("h").concat(String.valueOf(diferenciaMinutos % 60)).concat("m") : "730h0m";
    }

    private LocalDateTime convertToLocalDateTime(Date fecha) {
        return LocalDateTime.ofInstant(fecha.toInstant(), ZoneId.systemDefault());
    }

    /**
     * @param agentes
     * @param ideDocumento
     * @param codTipoAsignacion
     * @param ideFuncionario
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    private AsignacionesDTO conformarAsignaciones(List<AgenteDTO> agentes, BigInteger ideDocumento, String codTipoAsignacion,
                                                  BigInteger ideFuncionario) {
        AsignacionesDTO asignaciones = AsignacionesDTO.newInstance()
                .asignaciones(new ArrayList<>())
                .build();
        agentes.forEach(agente ->
                asignaciones.getAsignaciones().add(transfromAgenteToAsignacion(agente, ideDocumento, codTipoAsignacion, ideFuncionario))
        );
        return asignaciones;
    }

    private AsignacionDTO transfromAgenteToAsignacion(AgenteDTO agente, BigInteger ideDocumento, String codTipoAsignacion, BigInteger ideFuncionario) {
        return AsignacionDTO.newInstance()
                .ideAgente(agente.getIdeAgente())
                .codDependencia(agente.getCodDependencia())
                .ideDocumento(ideDocumento)
                .codTipAsignacion(codTipoAsignacion)
                .ideFunci(ideFuncionario)
                .build();
    }

    /**
     * @param nroRadicado
     * @throws BusinessException
     * @throws SystemException
     */
    public void asignarDocumentoByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        CorrespondenciaDTO correspondencia = correspondenciaControl.consultarCorrespondenciaByNroRadicado(nroRadicado, false);
        List<AgenteDTO> destinatarios = agenteControl.listarDestinatariosByIdeDocumento(correspondencia.getIdeDocumento());
        AsignacionesDTO asignaciones = conformarAsignaciones(destinatarios, correspondencia.getIdeDocumento(), "CTA",
                new BigInteger(correspondencia.getCodFuncRadica()));
        asignarCorrespondencia(AsignacionTramiteDTO.newInstance()
                .asignaciones(asignaciones)
                .build());
    }

    /**
     * @param ideAgente
     * @param codDependencia
     * @param ideFunci
     * @throws SystemException
     */
    void actualizarAsignacion(BigInteger ideAgente, String codDependencia, BigInteger ideFunci) throws SystemException {
        try {
            DctAsigUltimo asignacionUltimo = getAsignacionUltimoByIdeAgente(ideAgente);

            CorAgente agente = em.find(CorAgente.class, ideAgente);

            DctAsignacion asignacion = DctAsignacion.newInstance()
                    .ideUsuarioCreo(ideFunci)
                    .ideFunci(ideFunci)
                    .codDependencia(codDependencia)
                    .codTipAsignacion("CTA")
                    .fecAsignacion(new Date())
                    .corAgente(agente)
                    .build();

            asignacionUltimo.setNumRedirecciones(asignacionUltimo.getNumRedirecciones() + 1);
            asignacionUltimo.setIdeUsuarioCambio(ideFunci);
            asignacionUltimo.setFecCambio(new Date());
            asignacionUltimo.setDctAsignacion(asignacion);
            asignacionUltimo.setCorAgente(agente);

            em.persist(asignacion);
            em.merge(asignacionUltimo);
            em.flush();
            log.error("Actualizo la asignacion correctamente");

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param ideAgente
     * @param ideDocumento
     * @param ideFunci
     * @param causalDevolucion
     * @param observaciones
     * @throws SystemException
     */
    void actualizarAsignacionFromDevolucion(BigInteger ideAgente, BigInteger ideDocumento, BigInteger ideFunci,
                                            String causalDevolucion, String observaciones) throws SystemException {
        try {
            DctAsigUltimo asignacionUltimo = getAsignacionUltimoByIdeAgente(ideAgente);
            CorCorrespondencia correspondencia = ideAgente == null ? null : em.find(CorCorrespondencia.class, ideDocumento);

            CorAgente agente = em.find(CorAgente.class, ideAgente);
           if (!ObjectUtils.isEmpty(asignacionUltimo) && !ObjectUtils.isEmpty(asignacionUltimo.getIdeAsigUltimo())) {
               List<DctAsignacion> asignacionList = em.createNamedQuery("DctAsignacion.findByIdeAsigUltimo", DctAsignacion.class)
                       .setParameter("IDE_ASIG_ULTIMO", asignacionUltimo.getIdeAsigUltimo())
                       .getResultList();

               if(!ObjectUtils.isEmpty(asignacionList)) {
                   DctAsignacion asignacion = asignacionList.get(0);
                   asignacion.setCorAgente(agente);
                   asignacion.setCorCorrespondencia(correspondencia);
                   asignacion.setCodTipCausal(causalDevolucion);
                   asignacion.setObservaciones(observaciones);
                   em.merge(asignacion);
                   asignacionUltimo.setDctAsignacion(asignacion);
               }
            asignacionUltimo.setNumDevoluciones(asignacionUltimo.getNumDevoluciones() + 1);
            asignacionUltimo.setIdeUsuarioCambio(ideFunci);
            asignacionUltimo.setFecCambio(new Date());

            asignacionUltimo.setCorCorrespondencia(correspondencia);
            asignacionUltimo.setCorAgente(agente);


            em.merge(asignacionUltimo);
            em.flush();
           }
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public DctAsigUltimo getAsignacionUltimoByIdeAgente(BigInteger ideAgente) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("DctAsigUltimo.findByIdeAgente", DctAsigUltimo.class)
                    .setParameter("IDE_AGENTE", ideAgente)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            return new DctAsigUltimo();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
