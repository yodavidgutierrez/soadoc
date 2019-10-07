package co.com.soaint.correspondencia.business.control;

import co.com.foundation.cartridge.email.model.Attachment;
import co.com.foundation.cartridge.email.model.MailRequestDTO;
import co.com.foundation.cartridge.email.proxy.MailServiceProxy;
import co.com.soaint.correspondencia.domain.entity.*;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.*;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.NoResultException;
import javax.persistence.TemporalType;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 13-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class CargaMasivaControl extends GenericControl<CorCorrespondencia> {

    // [fields] -----------------------------------

    @Autowired
    private AgenteControl agenteControl;

    @Autowired
    private ConstantesControl constanteControl;

    @Autowired
    private FuncionariosControl funcionarioControl;

    @Autowired
    private OrganigramaAdministrativoControl organigramaAdministrativoControl;

    @Autowired
    private PpdDocumentoControl ppdDocumentoControl;

    @Autowired
    private AnexoControl anexoControl;

    @Autowired
    private DatosContactoControl datosContactoControl;

    @Autowired
    private CorrespondenciaControl correspondenciaControl;

    @Autowired
    private ReferidoControl referidoControl;

    @Autowired
    private DserialControl dserialControl;

    @Autowired
    private RadicadoControl radicadoControl;

    @Value("${radicado.rango.reservado}")
    private String[] rangoReservado;

    @Value("${radicado.horario.laboral}")
    private String[] horarioLaboral;

    @Value("${radicado.unidad.tiempo.horas}")
    private String unidadTiempoHoras;

    @Value("${radicado.unidad.tiempo.dias}")
    private String unidadTiempoDias;

    @Value("${radicado.dia.siguiente.habil}")
    private String diaSiguienteHabil;

    @Value("${radicado.dias.festivos}")
    private String[] diasFestivos;

    @Value("${radicado.req.dist.fisica}")
    private String reqDistFisica;

    private static final String separator = "--";

    public CargaMasivaControl() {
        super(CorCorrespondencia.class);
    }

    // ----------------------


    @Transactional
    public ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO) throws SystemException {

        return radicarCorrespondencia(comunicacionOficialDTO, ("EE".equalsIgnoreCase(comunicacionOficialDTO.getCorrespondencia().getCodTipoCmc()) ? TipoRadicacionEnum.ENTRADA : TipoRadicacionEnum.SALIDA));
    }



    private ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO, TipoRadicacionEnum tipoRadicacionEnum) throws SystemException {
        final Date fecha = new Date();
        final CorrespondenciaDTO correspondenciaDTO = comunicacionOficialDTO.getCorrespondencia();
        final List<PpdDocumentoDTO> ppdDocumentoList = comunicacionOficialDTO.getPpdDocumentoList();
        final List<AgenteDTO> agenteDTOList = comunicacionOficialDTO.getAgenteList();

        log.info("correspondenciaDTO {}", correspondenciaDTO);
        log.info("ppdDocumentoList {}", ppdDocumentoList);
        log.info("agenteDTOList {}", agenteDTOList);

        try {
            List<CorCorrespondencia> correspondencias = em.createNamedQuery("CorCorrespondencia.findByNroRadicadoObj", CorCorrespondencia.class)
                    .setParameter("NRO_RADICADO", correspondenciaDTO.getNroRadicado())
                    .getResultList();

            if (!ObjectUtils.isEmpty(correspondencias) ){
                List<AgenteDTO> agenteDTOS = new ArrayList<>();
                log.info("Existe la correspondencia---------******--------*****{}",correspondencias.get(0));
                for (AgenteDTO agentes:agenteDTOList) {
                    if("TP-AGEI".equalsIgnoreCase(agentes.getCodTipAgent()) && "TP-DESC".equalsIgnoreCase(agentes.getIndOriginal())){
                        agenteDTOS.add(agentes);
                    }
                }

                if (!ObjectUtils.isEmpty(agenteDTOS)){
                    log.info("La lista de agentes---------******--------*****--------------{}",agenteDTOS);
                    asignarAgenteCorrespondencia(agenteDTOS, correspondencias.get(0), tipoRadicacionEnum);

                }
                return ComunicacionOficialDTO.newInstance()
                        .correspondencia(correspondenciaControl.getCorrespondenciaDTO(correspondenciaDTO.getNroRadicado()))
                        .build();
            }


            if (correspondenciaDTO.getFecRadicado() == null) {
                correspondenciaDTO.setFecRadicado(fecha);
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(correspondenciaDTO.getFecRadicado());
            int anno = cal.get(Calendar.YEAR);

            CorCorrespondencia correspondencia = corCorrespondenciaTransform(correspondenciaDTO);
            correspondencia.setCodEstado(EstadoCorrespondenciaEnum.REGISTRADO.getCodigo());
            correspondencia.setFecVenGestion(correspondenciaControl.calcularFechaVencimientoGestion(correspondenciaDTO));
            correspondencia.setCorAgenteList(new HashSet<>());
            correspondencia.setContingencia(true);

            PpdDocumento ppdDocumento = ppdDocumentoControl.ppdDocumentoTransform(ppdDocumentoList.get(0));


            correspondencia.addPpdDocumento(ppdDocumento);

            log.info("MOstrando Correspondencia ---------------------------------------------------------------- {}", correspondencia);

            final String nroRadicado = correspondenciaDTO.getNroRadicado();

            log.info("Show Correspondencia {}", correspondencia);


           /* CorRadicado corRadicado = CorRadicado.newInstance()
                    .nroRadicado( comunicacionOficialDTO.getCorrespondencia().getNroRadicado())
                    .consecutivo(BigInteger.ZERO)
                    .radicadoPadre(new BigInteger(radicadoPartes[1]))
                    .build();
            correspondencia.setCorRadicado(corRadicado);*/
             CorRadicado corRadicado = correspondencia.getCorRadicado();
             try {
                 asignarCorRadicado(correspondencia, nroRadicado, correspondenciaDTO.getFecRadicado());
             }catch (Exception ex){
                 throw new SystemException("El radicado ya existe en BD"+ex.getMessage());
             }

            log.info("Show Correspondencia {}", correspondencia);

            log.info("Iniciar metodo  -------------------------- asignarAgenteCorrespondencia");
            asignarAgenteCorrespondencia(agenteDTOList, correspondencia, tipoRadicacionEnum);

            log.info("Sale del metodo  -------------------------- asignarAgenteCorrespondencia");

            em.flush();
            log.info("Se ha dado Flush -------------------------- Exitoso");




            log.info("Correspondencia - radicacion exitosa nro-radicado -> " + corRadicado.getNroRadicado());


            return ComunicacionOficialDTO.newInstance()
                    .correspondencia(correspondenciaControl.getCorrespondenciaDTO(corRadicado.getNroRadicado()))
                    .build();

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex.getMessage());
            throw new SystemException("system.generic.error Method radicarCorrespondencia " + ex.getMessage());
        }
    }

    private void asignarAgenteCorrespondencia(List<AgenteDTO> agenteDTOList,CorCorrespondencia correspondencia, TipoRadicacionEnum tipoRadicacionEnum) throws SystemException {

        log.info("correspondencia -------------------------------------------- {}", correspondencia);
        log.info("tipoRadicacionEnum------------------------------------------ {}", tipoRadicacionEnum);
        log.info("agenteDTOList----------------------------------------------- {}", agenteDTOList);
        try {


            int i = 0;
            final List<TvsDatosContacto> tvsDatosContactos = new ArrayList<>();
             String nombreInterno="";
             String nroIdenfificacionInterno="";
            for (AgenteDTO agenteDTO : agenteDTOList) {
                if ("TP-DESP".equalsIgnoreCase(agenteDTO.getIndOriginal())
                        && TipoRemitenteEnum.EXTERNO.getCodigo().equalsIgnoreCase(agenteDTO.getCodTipoRemite()))
                {

                 /*   tvsDatosContactos.addAll(agenteDTO.getDatosContactoList().stream()
                            .map(datosContactoDTO ->
                                    datosContactoControl.transform(datosContactoDTO))
                            .collect(Collectors.toList()));*/

                    if("TP-PERPN".equalsIgnoreCase(agenteDTO.getCodTipoPers())){
                        nombreInterno = agenteDTO.getNombre();
                        nroIdenfificacionInterno = agenteDTO.getNroDocuIdentidad();
                    }
                    if("TP-PERPJ".equalsIgnoreCase(agenteDTO.getCodTipoPers())) {
                        nombreInterno = agenteDTO.getRazonSocial();
                        nroIdenfificacionInterno = agenteDTO.getNit();
                    }
                    break;
                }
            }

            log.info("List<AgenteDTO> agenteDTOList----------------------------------------------- {}",agenteDTOList.size());
            for (AgenteDTO agenteDTO : agenteDTOList) {
                CorAgente corAgente = agenteControl.corAgenteTransform(agenteDTO);
                if(!"EXT".equalsIgnoreCase(corAgente.getCodTipoRemite())){
                    corAgente.setCodTipoRemite("INT");
                }
                log.info("CorAgente-------------------------------------------------------------- {}",corAgente);
               // datosContactoControl.asignarDatosContacto(corAgente);
                log.info("Lista DC -------------------------------------------------------------- {}",corAgente.getTvsDatosContactoList().size());
                log.info("Lista DC-------------------------------------------------------------- {}",corAgente.getTvsDatosContactoList());
                final BigInteger idAgente = corAgente.getIdeAgente();
                log.info("Iteracion ------------------------------ {}", ++i);
                if (null == idAgente) {
                    log.info("IDAGENTE == null ------------------------------ {}");
                    //agenteControl.actualizarIdeFunciAgenteInterno(corAgente);
                   // corAgente.setIdeFunci(agenteDTO.getIdeFunci());
                    corAgente.setEstadoDistribucion(EstadoDistribucionFisicaEnum.SIN_DISTRIBUIR.getCodigo());
                    corAgente.setIdeFunci(new BigInteger(correspondencia.getCodFuncRadica()));
                    correspondencia.addCorAgente(corAgente);
                    if
                    (TipoRemitenteEnum.EXTERNO.getCodigo().equalsIgnoreCase(agenteDTO.getCodTipoRemite()))
                    {

                        em.persist(correspondencia);
                        log.info("Se ha persistido la correspondencia");
                        continue;
                    }
                  //  tvsDatosContactos.forEach(corAgente::addDatosContacto);

                    corAgente.setCodEstado(EstadoAgenteEnum.SIN_ASIGNAR.getCodigo());
                    corAgente.setNombre(nombreInterno);
                    corAgente.setNroDocuIdentidad(nroIdenfificacionInterno);

                    /*corAgente.setEstadoDistribucion(reqDistFisica.equals(correspondencia.getReqDistFisica())
                            ? EstadoDistribucionFisicaEnum.SIN_DISTRIBUIR.getCodigo() : null);*/

                    if (tipoRadicacionEnum == TipoRadicacionEnum.ENTRADA) {
                        final DctAsignacion dctAsignacion =
                                DctAsignacion.newInstance()
                                        .ideUsuarioCreo(new
                                                BigInteger(correspondencia.getCodFuncRadica()))
                                        .codDependencia(corAgente.getCodDependencia())
                                        .codTipAsignacion("CTA")
                                        .fecAsignacion(new Date())
                                        .build();
                        em.persist(dctAsignacion);
                        final DctAsigUltimo dctAsigUltimo =
                                DctAsigUltimo.newInstance()
                                        .ideUsuarioCreo(new
                                                BigInteger(correspondencia.getCodFuncRadica()))
                                        .ideUsuarioCambio(new
                                                BigInteger(correspondencia.getCodFuncRadica()))
                                        .dctAsignacion(dctAsignacion)
                                        .build();

                        corAgente.addAsignacion(dctAsignacion);
                        corAgente.addAsignacionUltimo(dctAsigUltimo);
                        corAgente.addCorRadicado(correspondencia.getCorRadicado());

                        correspondencia.addDctAsignacion(dctAsignacion);
                        correspondencia.addDctAsigUltimo(dctAsigUltimo);
                    }
                } else {
                    corAgente = em.find(CorAgente.class, idAgente);
                    correspondencia.addCorAgente(corAgente);
                }
                em.persist(correspondencia);
            }
        } catch (Exception ex) {
            log.error("An error occurred on asignarAgenteCorrespondencia Message: {}", ex.getMessage());
            throw new SystemException(ex.getMessage());
        }
    }


    private void asignarCorRadicado(CorCorrespondencia correspondencia, String nroRadicado, Date fechaRadicacion) throws SystemException {
        log.info("fechaRadicacion---------------------------------------------------------------------{}",fechaRadicacion);
        String[] radicadoPartes = nroRadicado.split("-");
        List<CorRadicado> radicados = em.createNamedQuery("CorRadicado.findRadicadoPadre", CorRadicado.class)
                .setParameter("NUM_RAD", new BigInteger(radicadoPartes[1]))
                .getResultList();
        if(!ObjectUtils.isEmpty(radicados)){
           throw new SystemException("El radicado ya existe en BD");
        }
        final CorRadicado corRadicado = correspondencia.getCorRadicado();
        corRadicado.setConsecutivo(BigInteger.ZERO);
        corRadicado.setFechaRadicacion(fechaRadicacion);
        corRadicado.setRadicadoPadre(new BigInteger(radicadoPartes[1]));
        corRadicado.setNroRadicado(nroRadicado);
        log.info("Radicado Response ------------------{}", corRadicado);
    }



    private CorCorrespondencia corCorrespondenciaTransform(CorrespondenciaDTO correspondenciaDTO) {
        final CorRadicado corRadicado = CorRadicado.newInstance()
                .nroRadicado(correspondenciaDTO.getNroRadicado())
                .fechaRadicacion(correspondenciaDTO.getFecRadicado())
                .build();
        return CorCorrespondencia.newInstance()
                .descripcion(correspondenciaDTO.getDescripcion())
                .tiempoRespuesta(correspondenciaDTO.getTiempoRespuesta())
                .codUnidadTiempo(correspondenciaDTO.getCodUnidadTiempo())
                .ideDocumento(correspondenciaDTO.getIdeDocumento())
                .codMedioRecepcion(correspondenciaDTO.getCodMedioRecepcion())
                .corRadicado(corRadicado)
                .codTipoCmc(correspondenciaDTO.getCodTipoCmc())
                .ideInstancia(correspondenciaDTO.getIdeInstancia())
                .reqDistFisica("1".equalsIgnoreCase(correspondenciaDTO.getReqDistFisica()) ? "0" : "1")
                .codFuncRadica(correspondenciaDTO.getCodFuncRadica())
                .codSede(correspondenciaDTO.getCodSede())
                .codDependencia(correspondenciaDTO.getCodDependencia())
                .reqDigita(correspondenciaDTO.getReqDigita())
                .adjuntarDocumento(correspondenciaDTO.getAdjuntarDocumento())
                .codEmpMsj(correspondenciaDTO.getCodEmpMsj())
                .nroGuia(correspondenciaDTO.getNroGuia())
                .fecVenGestion(correspondenciaDTO.getFecVenGestion())
                .codEstado(correspondenciaDTO.getCodEstado())
                .codClaseEnvio(correspondenciaDTO.getCodClaseEnvio())
                .codModalidadEnvio(correspondenciaDTO.getCodModalidadEnvio())
                .corAgenteList(new HashSet<>())
                //.dctAsignacionList(new ArrayList<>())
                .ppdDocumentoList(new ArrayList<>())
                .corReferidoList(new ArrayList<>())
                .build();
    }




}
