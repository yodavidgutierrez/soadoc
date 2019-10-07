package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsSolicitudUnidadDocumental;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudesUnidadDocumentalDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 17-Oct-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class SolicitudUnidadDocumentalControl {
    @PersistenceContext
    private EntityManager em;

    /**
     * @param solicitudesUnidadDocumentalDTO
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    @Transactional
    public Boolean crearSolicitudUnidadDocumental(SolicitudesUnidadDocumentalDTO solicitudesUnidadDocumentalDTO) throws SystemException {
        log.info("processing rest request - crearSolicitudUnidadDocumental");
        try {
            for (SolicitudUnidadDocumentalDTO s : solicitudesUnidadDocumentalDTO.getSolicitudesUnidadDocumentalDTOS()) {
                this.insertarSolicitudUnidadDocumental(s);
            }
            em.flush();
            return true;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param solicitudUnidadDocumental
     * @return
     * @throws SystemException
     */
    @Transactional
    public void insertarSolicitudUnidadDocumental(SolicitudUnidadDocumentalDTO solicitudUnidadDocumental) throws SystemException {
        log.info("processing rest request - crearSolicitudUnidadDocumental");
        try {
            solicitudUnidadDocumental.setFechaHora(new Date());
            TvsSolicitudUnidadDocumental unidadDocumental = this.tvsSolicitudUnidadDocumentalTransform(solicitudUnidadDocumental);
            em.persist(unidadDocumental);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw new SystemException("Error insertando Solicitud de Unidad Documental " + ex.getMessage());
        }
    }

    /**
     * @param solicitudUnidadDocumental
     * @return
     * @throws SystemException
     */
    public TvsSolicitudUnidadDocumental tvsSolicitudUnidadDocumentalTransform(SolicitudUnidadDocumentalDTO solicitudUnidadDocumental) throws SystemException {
        try {
            return TvsSolicitudUnidadDocumental.newInstance()
                    .id(solicitudUnidadDocumental.getId())
                    .nombreUD(solicitudUnidadDocumental.getNombreUnidadDocumental())
                    .accion(solicitudUnidadDocumental.getAccion())
                    .codDependencia(solicitudUnidadDocumental.getCodigoDependencia())
                    .codSede(solicitudUnidadDocumental.getCodigoSede())
                    .codSerie(solicitudUnidadDocumental.getCodigoSerie())
                    .nombreSerie(solicitudUnidadDocumental.getNombreSerie())
                    .codSubserie(solicitudUnidadDocumental.getCodigoSubSerie())
                    .nombreSubSerie(solicitudUnidadDocumental.getNombreSubSerie())
                    .descriptor1(solicitudUnidadDocumental.getDescriptor1())
                    .descriptor2(solicitudUnidadDocumental.getDescriptor2())
                    .estado(solicitudUnidadDocumental.getEstado())
                    .fecHora(solicitudUnidadDocumental.getFechaHora())
                    .idConstante(solicitudUnidadDocumental.getIdConstante())
                    .idSolicitante(solicitudUnidadDocumental.getIdSolicitante())
                    .nro(solicitudUnidadDocumental.getNro())
                    .observaciones(solicitudUnidadDocumental.getObservaciones())
                    .motivo(solicitudUnidadDocumental.getMotivo())
                    .build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw new SystemException("Error convirtiendo Solicitud de Unidad Documental " + ex.getMessage());
        }
    }

    /**
     * @param fechaIni
     * @param fechaFin
     * @param codSede
     * @param codDependencia
     * @return
     * @throws SystemException
     */
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(Date fechaIni, Date fechaFin, String codSede, String codDependencia) throws SystemException {
        try {
            log.info("Se entra al metodo obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo");

            if (fechaIni != null && fechaFin != null) {
                if (fechaIni.getTime() > fechaFin.getTime() || fechaIni.getTime() == fechaFin.getTime())
                    throw ExceptionBuilder.newBuilder()
                            .withMessage("La fecha final no puede ser igual o menor que la fecha inicial.")
                            .buildBusinessException();
            }

            Timestamp fecIn = fechaIni == null ? null : new Timestamp(fechaIni.getTime());
            Timestamp fecFin = fechaFin == null ? null : new Timestamp(fechaFin.getTime());

            List<SolicitudUnidadDocumentalDTO> solicitudUnidadDocumentalDTOList = em.createNamedQuery("TvsSolicitudUM.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo", SolicitudUnidadDocumentalDTO.class)
                    .setParameter("FECHA_INI", fecIn)
                    .setParameter("FECHA_FIN", fecFin)
                    .setParameter("COD_SEDE", codSede)
                    .setParameter("COD_DEP", codDependencia)
                    .getResultList();

            return SolicitudesUnidadDocumentalDTO.newInstance().solicitudesUnidadDocumentalDTOS(solicitudUnidadDocumentalDTOList).build();

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw new SystemException("BD query Error obteniendo las solicitudes " + ex.getMessage());
        }
    }

    /**
     * @param ideSolicitante
     * @param codSede
     * @param codDependencia
     * @return
     * @throws SystemException
     */
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante(String ideSolicitante, String codSede, String codDependencia) throws SystemException {
        try {
            log.info("Se entra al metodo obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante");

            if (StringUtils.isEmpty(ideSolicitante) || StringUtils.isEmpty(codSede) || StringUtils.isEmpty(codDependencia)) {
                throw new SystemException("No se procesan valores nulos en la peticion");
            }

            List<SolicitudUnidadDocumentalDTO> solicitudUnidadDocumentalDTOList = em.createNamedQuery("TvsSolicitudUM.obtenerSolicitudUnidadDocumentalSedeDependenciaSolicitante", SolicitudUnidadDocumentalDTO.class)
                    /*.setParameter("FECHA_INI", fechaIni, TemporalType.TIMESTAMP)
                    .setParameter("FECHA_FIN", fechaFin, TemporalType.TIMESTAMP)*/
                    .setParameter("ID_SOL", ideSolicitante.trim())
                    .setParameter("COD_SEDE", codSede.trim())
                    .setParameter("COD_DEP", codDependencia.trim())
                    .getResultList();

            return SolicitudesUnidadDocumentalDTO.newInstance().solicitudesUnidadDocumentalDTOS(solicitudUnidadDocumentalDTOList).build();

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw new SystemException("BD query Error obteniendo las solicitudes " + ex.getMessage());
        }
    }

    /**
     * @param ideSolicitante
     * @param codSede
     * @param codDependencia
     * @return
     * @throws SystemException
     */
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependencialSolicitanteSinTramitar(String ideSolicitante, String codSede, String codDependencia) throws SystemException {
        try {
            log.info("Se entra al metodo obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante");

            if (StringUtils.isEmpty(ideSolicitante) || StringUtils.isEmpty(codSede) || StringUtils.isEmpty(codDependencia)) {
                throw new SystemException("Existen valores nulos para atender la peticion");
            }

            List<SolicitudUnidadDocumentalDTO> solicitudUnidadDocumentalDTOList = em.createNamedQuery("TvsSolicitudUM.obtenerSolicitudUnidadDocumentalSedeDependenciaSolicitanteSinTramitar", SolicitudUnidadDocumentalDTO.class)
                    /*.setParameter("FECHA_INI", fechaSolicitud, TemporalType.TIMESTAMP)
                    .setParameter("FECHA_FIN", fechaFin, TemporalType.TIMESTAMP)*/
                    .setParameter("ID_SOL", ideSolicitante.trim())
                    .setParameter("COD_SEDE", codSede.trim())
                    .setParameter("COD_DEP", codDependencia.trim())
                    .getResultList();

            return SolicitudesUnidadDocumentalDTO.newInstance().solicitudesUnidadDocumentalDTOS(solicitudUnidadDocumentalDTOList).build();

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("BD query Error obteniendo las solicitudes " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param idSolicitud
     * @return
     */
    public Boolean verificarByIdeSolicitud(BigInteger idSolicitud) throws SystemException {
        try {
            long cantidad = em.createNamedQuery("TvsSolicitudUM.countByIdSolicitud", Long.class)
                    .setParameter("IDE_SOL", idSolicitud)
                    .getSingleResult();
            return cantidad > 0;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw new SystemException("Business Control - a system error has occurred " + ex.getMessage());
        }
    }

    /**
     * @param IdSolicitud
     * @return
     */
    public Boolean verificarByIdNombreUMSolicitud(BigInteger IdSolicitud, String nombreUD) throws SystemException {
        try {
            long cantidad = em.createNamedQuery("TvsSolicitudUM.countByIdNombreUDSolicitud", Long.class)
                    .setParameter("IDE_SOL", IdSolicitud)
                    .setParameter("NOM_UD", nombreUD)
                    .getSingleResult();
            return cantidad > 0;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw new SystemException("Business Control - a system error has occurred " + ex);
        }
    }

    public SolicitudUnidadDocumentalDTO actualizarSolicitudUnidadDocumental(SolicitudUnidadDocumentalDTO solicitudUnidadDocumentalDTO) throws BusinessException, SystemException {
        log.info("processing rest request - actualizarSolicitudUnidadDocumental");

        if (!verificarByIdeSolicitud(solicitudUnidadDocumentalDTO.getIdSolicitud())) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("solicitud.solicitud_not_exist_by_id")
                    .buildBusinessException();
        }
        try {
            em.createNamedQuery("TvsSolicitudUM.actualizarSolicitudUnidadDocumental")
                    .setParameter("IDE_SOL", solicitudUnidadDocumentalDTO.getIdSolicitud())
                    .setParameter("ID", solicitudUnidadDocumentalDTO.getId())
                    .setParameter("ID_CONST", solicitudUnidadDocumentalDTO.getIdConstante())
                    .setParameter("FECH", solicitudUnidadDocumentalDTO.getFechaHora())
                    .setParameter("NOMBREUD", solicitudUnidadDocumentalDTO.getNombreUnidadDocumental())
                    .setParameter("DESCP1", solicitudUnidadDocumentalDTO.getDescriptor1())
                    .setParameter("DESCP2", solicitudUnidadDocumentalDTO.getDescriptor2())
                    .setParameter("NRO", solicitudUnidadDocumentalDTO.getNro())
                    .setParameter("COD_SER", solicitudUnidadDocumentalDTO.getCodigoSerie())
                    .setParameter("COD_SUBS", solicitudUnidadDocumentalDTO.getCodigoSubSerie())
                    .setParameter("COD_SED", solicitudUnidadDocumentalDTO.getCodigoSede())
                    .setParameter("COD_DEP", solicitudUnidadDocumentalDTO.getCodigoDependencia())
                    .setParameter("ID_SOL", solicitudUnidadDocumentalDTO.getIdSolicitante())
                    .setParameter("EST", solicitudUnidadDocumentalDTO.getEstado())
                    .setParameter("ACC", solicitudUnidadDocumentalDTO.getAccion())
                    .setParameter("OBS", solicitudUnidadDocumentalDTO.getObservaciones())
                    .setParameter("MOT", solicitudUnidadDocumentalDTO.getMotivo())
                    .executeUpdate();

            return solicitudUnidadDocumentalDTO;
        } catch (NullPointerException ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("solicitud.solicitud_is_null")
                    .withRootException(ex)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("Business Control - a system error has occurred " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @return
     */
    public List<SolicitudUnidadDocumentalDTO> listarSolicitudes() throws SystemException {
        try {
            return em.createNamedQuery("TvsSolicitudUM.findAll", SolicitudUnidadDocumentalDTO.class)
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
     * @return
     */
    public Long obtenerCantidadSolicitudes() throws SystemException {
        try {
            return em.createNamedQuery("TvsSolicitudUM.countAll", Long.class)
                    .getSingleResult();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
