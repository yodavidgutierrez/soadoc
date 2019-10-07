package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.CorrespondenciaControl;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 31-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessBoundary
public class GestionarCorrespondencia {

    // [fields] -----------------------------------

    @Autowired
    CorrespondenciaControl control;

    @Autowired
    private GestionarSolicitudUnidadDocumental gestionarSolicitudUnidadDocumental;

    // ----------------------

    /**
     * @param comunicacionOficialDTO
     * @return
     * @throws SystemException
     */
    public ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO) throws SystemException {
        return control.radicarCorrespondencia(comunicacionOficialDTO);
    }

    public ComunicacionOficialDTO radicarCorrespondenciaSalidaInternaRemitenteReferidoADestinatario(ComunicacionOficialDTO comunicacionOficialDTO) throws SystemException {
        return control.radicarCorrespondenciaSalidaInternaRemitenteReferidoADestinatario(comunicacionOficialDTO);
    }

    /**
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        return control.listarCorrespondenciaByNroRadicado(nroRadicado);
    }

    /**
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public ComunicacionOficialFullDTO listarFullCorrespondenciaByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        return control.listarFullCorrespondenciaByNroRadicado(nroRadicado);
    }


    /**
     * @param correspondenciaDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarEstadoCorrespondencia(CorrespondenciaDTO correspondenciaDTO) throws SystemException {
        control.actualizarEstadoCorrespondencia(correspondenciaDTO);
    }

    public void actualizaroCorrespondenciaSalida(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        control.actualizaroCorrespondenciaSalida(correspondenciaDTO);
    }

    /**
     * @param correspondenciaDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarIdeInstancia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        control.actualizarIdeInstancia(correspondenciaDTO);
    }

    public boolean actualizarIdeInstanciaDevolucion(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
       return control.actualizarIdeInstanciaDevolucion(correspondenciaDTO);
    }

    public String getIdeInstanciaPorRadicado(String nroRadicado) throws SystemException, BusinessException {
        return control.getIdeInstanciaPorRadicado(nroRadicado);
    }

    public AsignacionDTO obtenerObersacionByIdDocumento(BigInteger idObservacion) throws SystemException, BusinessException {
        return control.obtenerObersacionByIdDocumento(idObservacion);
    }

    /**
     * @param nroRadicado
     * @throws BusinessException
     * @throws SystemException
     */
    public CorrespondenciaDTO getCorrespondenciaInstanciaPorRadicado(String nroRadicado, boolean devolucion) throws SystemException, BusinessException {
        return control.consultarCorrespondenciaByNroRadicado(nroRadicado, devolucion);
    }

    public boolean actualizarCamposEntregaCorrespondencia(List<CorrespondenciaDTO> correspondencias) throws SystemException {
        return control.actualizarCamposEntregaCorrespondencia(correspondencias);
    }

    public boolean actualizarEnvioCorrespondencia(List<CorrespondenciaDTO> correspondencias, boolean distribuido) throws SystemException {
        return control.actualizarEnvioCorrespondencia(correspondencias, distribuido);
    }

    /**
     * @param fechaIni
     * @param fechaFin
     * @param codDependencia
     * @param codEstado
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public ComunicacionesOficialesDTO listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(Date fechaIni, Date fechaFin, String codDependencia, String codEstado, BigInteger idFuncionario, String nroRadicado) throws SystemException {
        return control.listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(fechaIni, fechaFin, codDependencia, codEstado, idFuncionario, nroRadicado);
    }

    /**
     * @param fechaIni
     * @param fechaFin
     * @param codDependencia
     * @param codTipoDoc
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public ComunicacionesOficialesDTO listarCorrespondenciaSinDistribuir(Date fechaIni, Date fechaFin, String codDependencia, String codTipoDoc, String nroRadicado) throws BusinessException, SystemException {
        return control.listarCorrespondenciaSinDistribuir(fechaIni, fechaFin, codDependencia, codTipoDoc, nroRadicado);
    }

    /**
     * @param fechaIni
     * @param fechaFin
     * @param modEnvio
     * @param claseEnvio
     * @param codDependencia
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public List<ComunicacionOficialSalidaFullDTO> listarComunicacionDeSalidaConDistribucionFisica(String fechaIni,
                                                                                                    String fechaFin,
                                                                                                    String modEnvio,
                                                                                                    String claseEnvio,
                                                                                                    String codDependencia,
                                                                                                    String tipoDoc,
                                                                                                    String nroRadicado,
                                                                                                    String tipoComunicacion) throws BusinessException, SystemException {
        return control.listarComunicacionDeSalidaConDistribucionFisica(fechaIni, fechaFin, modEnvio, claseEnvio, codDependencia, tipoDoc, nroRadicado,tipoComunicacion);
    }

    public List<ComunicacionOficialSalidaFullDTO> listarComunicacionDeSalidaConDistribucionFisicaSinDistribuir(String fechaIni,
                                                                                                  String fechaFin,
                                                                                                  String codDependencia,
                                                                                                  String tipoDoc,
                                                                                                  String nroRadicado) throws BusinessException, SystemException {
        return control.listarComunicacionDeSalidaConDistribucionFisicaSinDistribuir(fechaIni, fechaFin, codDependencia, tipoDoc, nroRadicado);
    }

    /**
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    public Boolean verificarByNroRadicado(String nroRadicado) throws SystemException {
        return control.verificarByNroRadicado(nroRadicado);
    }

    public List<CorrespondenciaDTO> radicadosContingencia() throws SystemException {
        return control.radicadosContingencia();
    }

    /**
     * @param comunicacionOficialDTO
     * @return
     * @throws SystemException
     */
    public String actualizarComunicacion(ComunicacionOficialDTO comunicacionOficialDTO) throws SystemException {
        return control.actualizarComunicacion(comunicacionOficialDTO);
    }

//    /**
//     *
//     * @param comunicacionOficialDTO
//     * @return
//     * @throws BusinessException
//     * @throws SystemException
//     */
//    public ComunicacionOficialDTO radicarCorrespondenciaSalida(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
//        return control.radicarCorrespondenciaSalida(comunicacionOficialDTO);
//    }

    /**
     * @param comunicacionOficialDTO
     * @return
     * @throws SystemException
     */
    public ComunicacionOficialDTO radicarCorrespondenciaSalidaRemitenteReferidoADestinatario(ComunicacionOficialRemiteDTO comunicacionOficialDTO)
            throws SystemException {
        final Boolean esRemitenteReferidoDestinatario = comunicacionOficialDTO.getEsRemitenteReferidoDestinatario();
        final ComunicacionOficialDTO oficialDTO = ComunicacionOficialDTO.newInstance()
                .correspondencia(comunicacionOficialDTO.getCorrespondencia())
                .agenteList(comunicacionOficialDTO.getAgenteList())
                .ppdDocumentoList(comunicacionOficialDTO.getPpdDocumentoList())
                .anexoList(comunicacionOficialDTO.getAnexoList())
                .datosContactoList(comunicacionOficialDTO.getDatosContactoList())
                .referidoList(comunicacionOficialDTO.getReferidoList())
                .esRemitenteReferidoDestinatario(ObjectUtils.isEmpty(esRemitenteReferidoDestinatario) ? Boolean.FALSE : esRemitenteReferidoDestinatario)
                .build();
        return control.radicarCorrespondenciaSalidaRemitenteReferidoADestinatario(oficialDTO);
    }

    /**
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    public String obtenerRadicadoFull(String nroRadicado) throws SystemException {
        return control.obtenerRadicadoFull(nroRadicado);
    }

    public Boolean sendMail(String nroRadicado) throws BusinessException, SystemException {
        return control.sendMail(nroRadicado);
    }

    public Boolean notoficarTarea(NotificacionDTO notificacionDTO) throws SystemException, BusinessException, ParseException {
        return control.notoficarTarea(notificacionDTO.getNroRadicado(), notificacionDTO.getDestinatario(), notificacionDTO.getRemitente(), notificacionDTO.getNombreTarea());
    }

    /**
     * @param unidadDocumentalDTO * @throws BusinessException
     * @throws SystemException
     */
    public Boolean crearSolicitudUnidadDocumental(SolicitudesUnidadDocumentalDTO unidadDocumentalDTO) throws SystemException, BusinessException {
        return gestionarSolicitudUnidadDocumental.crearSolicitudUnidadDocumental(unidadDocumentalDTO);
    }

    /**
     * @param fechaIni
     * @param fechaFin
     * @param codDependencia
     * @param codSede
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(Date fechaIni, Date fechaFin, String codSede, String codDependencia) throws BusinessException, SystemException {
        return gestionarSolicitudUnidadDocumental.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(fechaIni, fechaFin, codSede, codDependencia);
    }

    /**
     * @param ideSolicitante
     * @param codDependencia
     * @param codSede
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante(String ideSolicitante, String codSede, String codDependencia) throws SystemException {
        return gestionarSolicitudUnidadDocumental.obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante(ideSolicitante, codSede, codDependencia);
    }

    /**
     * @param ideSolicitante
     * @param codDependencia
     * @param codSede
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependencialSolicitanteSinTramitar(String ideSolicitante, String codSede, String codDependencia) throws SystemException {
        return gestionarSolicitudUnidadDocumental.obtenerSolicitudUnidadDocumentalSedeDependencialSolicitanteSinTramitar(ideSolicitante, codSede, codDependencia);
    }

    /**
     * @param solicitudUnidadDocumentalDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public SolicitudUnidadDocumentalDTO actualizarSolicitudUnidadDocumental(SolicitudUnidadDocumentalDTO solicitudUnidadDocumentalDTO) throws BusinessException, SystemException {
        return gestionarSolicitudUnidadDocumental.actualizarSolicitudUnidadDocumental(solicitudUnidadDocumentalDTO);
    }

    /**
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    public String consultarNroRadicadoCorrespondenciaReferida(String nroRadicado) throws SystemException {
        return control.consultarNroRadicadoCorrespondenciaReferida(nroRadicado);
    }
}
