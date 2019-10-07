package co.com.soaint.correspondencia.business.control;

import co.com.foundation.cartridge.email.model.Attachment;
import co.com.foundation.cartridge.email.model.MailRequestDTO;
import co.com.foundation.cartridge.email.proxy.MailServiceProxy;
import co.com.soaint.correspondencia.Utils.DateUtils;
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
import javax.persistence.TypedQuery;
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
public class CorrespondenciaControl extends GenericControl<CorCorrespondencia> {

    // [fields] -----------------------------------

    @Autowired
    private AgenteControl agenteControl;

    @Autowired
    private DctAsignacionControl dctAsignacionControl;

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
    private ReferidoControl referidoControl;

    @Autowired
    private DependenciaControl dependenciaControl;

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

    public CorrespondenciaControl() {
        super(CorCorrespondencia.class);
    }

    // ----------------------

    /**
     * @param nroRadicado
     * @param codSede
     * @param codTipoCmc
     * @param anno
     * @param consecutivo
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    private String procesarNroRadicado(String nroRadicado, String codSede, String codTipoCmc, String anno, String consecutivo) throws BusinessException, SystemException {
        String nRadicado = nroRadicado;
        try {
            if (StringUtils.isEmpty(nroRadicado))
                nRadicado = codSede
                        .concat(codTipoCmc)
                        .concat(anno)
                        .concat(consecutivo);
            else if (verificarByNroRadicado(nRadicado))
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_duplicated_nroRadicado")
                        .buildBusinessException();
            return nRadicado;
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred {}", e);
            throw e;
        }
    }

    /**
     * @param codSede
     * @param codTipoCmc
     * @param anno
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    private String procesarNroRadicadoSalida(String codSede, String codDependencia, String codFunci, String codTipoCmc, String anno) throws SystemException {
        try {
            String nRadicado;

            do {
                String consecutivo = dserialControl.consultarConsecutivoRadicadoByCodSedeAndCodCmcAndAnno(codSede, codTipoCmc, String.valueOf(anno));
                nRadicado = codSede
                        .concat(codTipoCmc)
                        .concat(anno)
                        .concat(consecutivo);

                dserialControl.updateConsecutivo(codSede, codDependencia,
                        codTipoCmc, String.valueOf(anno), consecutivo, codFunci);
            }
            while (verificarByNroRadicado(nRadicado));

            return nRadicado;
        } catch (SystemException e) {
            log.error("Business Control - a business error has occurred", e);
            throw e;
        }
    }

    /**
     * @param comunicacionOficialDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional
    public ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO) throws SystemException {
        return radicarCorrespondencia(comunicacionOficialDTO, TipoRadicacionEnum.ENTRADA);
    }

    @Transactional
    public ComunicacionOficialDTO radicarCorrespondenciaSalidaRemitenteReferidoADestinatario(ComunicacionOficialDTO comunicacionOficialDTO)
            throws SystemException {
        return radicarCorrespondencia(comunicacionOficialDTO, TipoRadicacionEnum.SALIDA);
    }

    @Transactional
    public ComunicacionOficialDTO radicarCorrespondenciaSalidaInternaRemitenteReferidoADestinatario(ComunicacionOficialDTO comunicacionOficialDTO)
            throws SystemException {
        return radicarCorrespondencia(comunicacionOficialDTO, TipoRadicacionEnum.INTERNA);
    }

    private ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO, TipoRadicacionEnum tipoRadicacionEnum) throws SystemException {
        final Date fecha = new Date();
        final CorrespondenciaDTO correspondenciaDTO = comunicacionOficialDTO.getCorrespondencia();
        final List<PpdDocumentoDTO> ppdDocumentoList = comunicacionOficialDTO.getPpdDocumentoList();
        final List<AnexoDTO> anexoList = comunicacionOficialDTO.getAnexoList();
        final List<ReferidoDTO> referidoList = comunicacionOficialDTO.getReferidoList();
        final List<AgenteDTO> agenteDTOList = comunicacionOficialDTO.getAgenteList();

        log.info("correspondenciaDTO {}", correspondenciaDTO);
        log.info("ppdDocumentoList {}", ppdDocumentoList);
        log.info("anexoList {}", anexoList);
        log.info("referidoList {}", referidoList);
        log.info("agenteDTOList {}", agenteDTOList);

        try {
            if (correspondenciaDTO.getFecRadicado() == null) {
                correspondenciaDTO.setFecRadicado(fecha);
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(correspondenciaDTO.getFecRadicado());
            int anno = cal.get(Calendar.YEAR);

            CorCorrespondencia correspondencia = corCorrespondenciaTransform(correspondenciaDTO);
            correspondencia.setCodEstado(EstadoCorrespondenciaEnum.REGISTRADO.getCodigo());
            correspondencia.setFecVenGestion(calcularFechaVencimientoGestion(correspondenciaDTO));
            correspondencia.setCorAgenteList(new HashSet<>());
            if (!ObjectUtils.isEmpty(correspondenciaDTO) && !StringUtils.isEmpty(correspondenciaDTO.getReqDistElectronica())) {
                correspondencia.setReqDistElectronica(correspondenciaDTO.getReqDistElectronica());
            }

            PpdDocumento ppdDocumento = ppdDocumentoControl.ppdDocumentoTransform(ppdDocumentoList.get(0));

            if (!ObjectUtils.isEmpty(anexoList)) {
                ppdDocumento.setCorAnexoList(new ArrayList<>());
                anexoList.forEach(anexoDTO -> {
                    CorAnexo corAnexo = anexoControl.corAnexoTransform(anexoDTO);
                    ppdDocumento.addCorAnexo(corAnexo);
                });
            }

            correspondencia.addPpdDocumento(ppdDocumento);

            log.info("MOstrando Correspondencia ---------------------------------------------------------------- {}", correspondencia);

            final String nroRadicado;
            if (tipoRadicacionEnum == TipoRadicacionEnum.ENTRADA) {
                log.info("Iniciando dserialControl.consultarConsecutivoRadicadoByCodSedeAndCodCmcAndAnno ");
                final String consecutivo = dserialControl.consultarConsecutivoRadicadoByCodSedeAndCodCmcAndAnno(correspondenciaDTO.getCodSede(),
                        correspondenciaDTO.getCodTipoCmc(), String.valueOf(anno));
                log.info("Resultado dserialControl.consultarConsecutivoRadicadoByCodSedeAndCodCmcAndAnno {}", consecutivo);
                log.info("Iniciando procesarNroRadicado(correspondenciaDTO.getNroRadicado(),\n" +
                        "                        correspondencia.getCodSede(),\n" +
                        "                        correspondencia.getCodTipoCmc(),\n" +
                        "                        String.valueOf(anno), consecutivo); ");
                nroRadicado = procesarNroRadicado(correspondenciaDTO.getNroRadicado(),
                        correspondencia.getCodSede(),
                        correspondencia.getCodTipoCmc(),
                        String.valueOf(anno), consecutivo);
                log.info("Resultado procesarNroRadicado {}", nroRadicado);
                log.info("Iniciando updateConsecutivo");
                dserialControl.updateConsecutivo(correspondencia.getCodSede(), correspondencia.getCodDependencia(),
                        correspondencia.getCodTipoCmc(), String.valueOf(anno), consecutivo, correspondencia.getCodFuncRadica());
            } else {
                nroRadicado = procesarNroRadicadoSalida(
                        correspondencia.getCodSede(),
                        correspondencia.getCodDependencia(),
                        correspondencia.getCodFuncRadica(),
                        correspondencia.getCodTipoCmc(),
                        String.valueOf(anno));
            }

            for (ReferidoDTO referidoDTO : referidoList) {
                final String nroRadReferido = referidoDTO.getNroRadRef();
                if (!StringUtils.isEmpty(nroRadReferido)) {
                    correspondencia.addCorReferido(CorReferido.newInstance().nroRadRef(nroRadReferido).build());
                }
            }

            log.info("Show Correspondencia {}", correspondencia);

            asignarCorRadicado(correspondencia, correspondenciaDTO.getRadicadoPadre(), nroRadicado);

            log.info("Show Correspondencia {}", correspondencia);

            log.info("Iniciar metodo  -------------------------- asignarAgenteCorrespondencia");
            asignarAgenteCorrespondencia(agenteDTOList, correspondencia, tipoRadicacionEnum);

            log.info("Sale del metodo  -------------------------- asignarAgenteCorrespondencia");

            em.flush();
            log.info("Se ha dado Flush -------------------------- Exitoso");

            CorRadicado corRadicado = correspondencia.getCorRadicado();

            log.info("Correspondencia - radicacion exitosa nro-radicado -> " + corRadicado.getNroRadicado());

            /*if (tipoRadicacionEnum == TipoRadicacionEnum.SALIDA) {
                String endpoint = System.getProperty("ecm-api-endpoint");
                final WebTarget wt = ClientBuilder.newClient().target(endpoint);
                correspondencia.getPpdDocumentoList().parallelStream().forEach(ppdDoc -> {
                    final String idEcm = ppdDoc.getIdeEcm();
                    log.info("Se modificara el documento con el NroRadicado = " + corRadicado.getNroRadicado() + " y con ID " + idEcm);
                    if (StringUtils.isEmpty(idEcm)) {
                        co.com.soaint.foundation.canonical.ecm.DocumentoDTO dto = co.com.soaint.foundation.canonical.ecm.DocumentoDTO.newInstance()
                                .nroRadicado(corRadicado.getNroRadicado())
                                .idDocumento(idEcm)
                                .build();
                        Response response = wt.path("/modificarMetadatosDocumentoECM/")
                                .request()
                                .put(Entity.json(dto));
                        String nuevoIdECM = (String) response.readEntity(MensajeRespuesta.class).getResponse().get("idECM");
                        if (null != nuevoIdECM) {
                            ppdDoc.setIdeEcm(nuevoIdECM);
                            log.info("Response del cambio de radicado " + response.toString());
                        }
                    }
                });
            }*/
            CorrespondenciaDTO cor = getCorrespondenciaDTO(corRadicado.getNroRadicado());
            log.info("FECHA RADICADO SALIDAAA --->" + cor.getFecRadicado());
            log.info("FECHA VENCIMIENTO SALIDAAA --->" + cor.getFecVenGestion());

            return ComunicacionOficialDTO.newInstance()
                    .correspondencia(cor)
                    .build();

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex.getMessage());
            throw new SystemException("system.generic.error Method radicarCorrespondencia " + ex.getMessage());
        }
    }

    private void asignarAgenteCorrespondencia(List<AgenteDTO> agenteDTOList, CorCorrespondencia correspondencia, TipoRadicacionEnum tipoRadicacionEnum) throws SystemException {

        try {
            int i = 0;
            final List<TvsDatosContacto> tvsDatosContactos = new ArrayList<>();
            String nombreInterno = "";
            String nroIdenfificacionInterno = "";
            /*for (AgenteDTO agenteDTO : agenteDTOList) {

            }*/
            log.info("List<AgenteDTO> agenteDTOList----------------------------------------------- {}", agenteDTOList.size());
            for (AgenteDTO agenteDTO : agenteDTOList) {

                if ("TP-DESP".equalsIgnoreCase(agenteDTO.getIndOriginal())
                        && TipoRemitenteEnum.EXTERNO.getCodigo().equalsIgnoreCase(agenteDTO.getCodTipoRemite())) {

                    tvsDatosContactos.addAll(agenteDTO.getDatosContactoList().stream()
                            .map(datosContactoDTO ->
                                    datosContactoControl.transform(datosContactoDTO))
                            .collect(Collectors.toList()));

                    if ("TP-PERPN".equalsIgnoreCase(agenteDTO.getCodTipoPers())) {
                        nombreInterno = agenteDTO.getNombre();
                        nroIdenfificacionInterno = agenteDTO.getNroDocuIdentidad();
                    }
                    if ("TP-PERPJ".equalsIgnoreCase(agenteDTO.getCodTipoPers())) {
                        nombreInterno = agenteDTO.getRazonSocial();
                        nroIdenfificacionInterno = agenteDTO.getNit();
                    }
                    //break;
                }

                CorAgente corAgente = agenteControl.corAgenteTransform(agenteDTO);
                log.info("CorAgente-------------------------------------------------------------- {}", corAgente);
                datosContactoControl.asignarDatosContacto(corAgente);
                log.info("Lista DC -------------------------------------------------------------- {}", corAgente.getTvsDatosContactoList().size());
                log.info("Lista DC-------------------------------------------------------------- {}", corAgente.getTvsDatosContactoList());
                final BigInteger idAgente = corAgente.getIdeAgente();
                log.info("Iteracion ------------------------------ {}", ++i);
                if (null == idAgente) {
                    log.info("IDAGENTE == null ------------------------------ {}");
                    //agenteControl.actualizarIdeFunciAgenteInterno(corAgente);
                    corAgente.setIdeFunci(agenteDTO.getIdeFunci());
                    correspondencia.addCorAgente(corAgente);
                    if
                    (TipoRemitenteEnum.EXTERNO.getCodigo().equalsIgnoreCase(agenteDTO.getCodTipoRemite())) {

                        corAgente.setEstadoDistribucion(EstadoDistribucionFisicaEnum.SIN_DISTRIBUIR.getCodigo());
                        em.persist(correspondencia);
                        log.info("Se ha persistido la correspondencia");
                        continue;
                    }
                    tvsDatosContactos.forEach(corAgente::addDatosContacto);

                    corAgente.setCodEstado(EstadoAgenteEnum.SIN_ASIGNAR.getCodigo());
                    corAgente.setNombre(nombreInterno);
                    corAgente.setNroDocuIdentidad(nroIdenfificacionInterno);

                    corAgente.setEstadoDistribucion(reqDistFisica.equals(correspondencia.getReqDistFisica())
                            ? EstadoDistribucionFisicaEnum.SIN_DISTRIBUIR.getCodigo() : null);

                    if ((tipoRadicacionEnum == TipoRadicacionEnum.ENTRADA || (tipoRadicacionEnum == TipoRadicacionEnum.INTERNA && "TP-AGEI".equals(corAgente.getCodTipAgent())))) {

                        final DctAsignacion dctAsignacion =
                                DctAsignacion.newInstance()
                                        .ideUsuarioCreo(new BigInteger(correspondencia.getCodFuncRadica()))
                                        .codDependencia(corAgente.getCodDependencia())
                                        .codTipAsignacion("CTA")
                                        .fecAsignacion(new Date())
                                        .ideFunci(new BigInteger(correspondencia.getCodFuncRadica()))
                                        .build();
                        em.persist(dctAsignacion);
                        final DctAsigUltimo dctAsigUltimo =
                                DctAsigUltimo.newInstance()
                                        .ideUsuarioCreo(new BigInteger(correspondencia.getCodFuncRadica()))
                                        .ideUsuarioCambio(new BigInteger(correspondencia.getCodFuncRadica()))
                                        .dctAsignacion(dctAsignacion)
                                        .build();

                        corAgente.addAsignacion(dctAsignacion);
                        corAgente.addAsignacionUltimo(dctAsigUltimo);
                        corAgente.addCorRadicado(correspondencia.getCorRadicado());

                        correspondencia.addDctAsignacion(dctAsignacion);
                        correspondencia.addDctAsigUltimo(dctAsigUltimo);
                    }
                } else {
                    CorAgente corAgentee = em.find(CorAgente.class, idAgente);
                    corAgentee.setIndOriginal(corAgente.getIndOriginal());
                    //corAgentee.setCodTipAgent(corAgente.getCodTipAgent());
                    em.merge(corAgentee);
                    correspondencia.addCorAgente(corAgentee);
                }
                em.persist(correspondencia);
            }
        } catch (Exception ex) {
            log.error("An error occurred on asignarAgenteCorrespondencia Message: {}", ex.getMessage());
            throw new SystemException(ex.getMessage());
        }
    }

    public CorrespondenciaDTO getCorrespondenciaDTO(String nroRadicado) throws SystemException {
        CorrespondenciaDTO correspondenciaDTO;
        correspondenciaDTO = consultarCorrespondenciaByNroRadicado(nroRadicado, false);
        final String orgCode = correspondenciaDTO.getCodDependencia();
        if (!StringUtils.isEmpty(orgCode)) {
            final OrganigramaItemDTO itemDTO = organigramaAdministrativoControl.consultarElementoByCodOrg(orgCode);
            correspondenciaDTO.setAbreviatura(itemDTO.getAbreviatura());
        }
        return correspondenciaDTO;
    }

    private void asignarCorRadicado(CorCorrespondencia correspondencia, String radicadoPadre, String nroRadicado) throws ParseException {
        log.info("NUMERO RADICADO PADRE ENTRANTE {}", radicadoPadre);
        final RadicadoDTO radicadoDTO = radicadoControl.crearNumeroRadicado(radicadoPadre);
        log.info("radicadoDTO repuesta {}", radicadoDTO);
        final String yearString = radicadoControl.getLastDigit(new BigInteger(String.valueOf(LocalDate.now().getYear())), 2);
        final String newRadicado = yearString + "-" + radicadoDTO.getNumeroRadicado()
                + "-" + radicadoDTO.getConsecutivo().toString();

        log.info("{}------------------{}", yearString, newRadicado);

        final CorRadicado corRadicado = correspondencia.getCorRadicado();
        Calendar cal = Calendar.getInstance();
        corRadicado.setConsecutivo(radicadoDTO.getConsecutivo());
        corRadicado.setFechaRadicacion(DateUtils.SetTimeZone("yyyy-MM-dd HH:mm:ss.SSS", cal.getTime(), TimeZone.getTimeZone("America/Bogota")));
        corRadicado.setRadicadoPadre(new BigInteger(radicadoDTO.getNumeroRadicado()));
        corRadicado.setNroRadicado(nroRadicado + separator + newRadicado);
        log.info("Radicado Response ------------------{}", corRadicado.getFechaRadicacion());
    }

    /**
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        try {
            CorrespondenciaDTO correspondenciaDTO = consultarCorrespondenciaByNroRadicado(nroRadicado, false);

            return consultarComunicacionOficialByCorrespondencia(correspondenciaDTO);
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred {}", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param correspondenciaDTO
     * @return
     */
    CorrespondenciaFullDTO correspondenciaTransformToFull(CorrespondenciaDTO correspondenciaDTO) throws SystemException {
        log.info("processing rest request - CorrespondenciaControl-correspondenciaTransformToFull");
        try {

            FuncionarioDTO funcionarioDTO = funcionarioControl.consultarFuncionarioByIdeFunci(new BigInteger(correspondenciaDTO.getCodFuncRadica()));
            String funcionario = !ObjectUtils.isEmpty(funcionarioDTO) ? (StringUtils.isEmpty(funcionarioDTO.getNomFuncionario()) ? "" : funcionarioDTO.getNomFuncionario() + " ") +
                    (StringUtils.isEmpty(funcionarioDTO.getValApellido1()) ? "" : funcionarioDTO.getValApellido1() + " ") +
                    (StringUtils.isEmpty(funcionarioDTO.getValApellido2()) ? "" : funcionarioDTO.getValApellido2()) : null;

            log.info("correspondenciaTransformToFull ModalidadEnvio " + correspondenciaDTO.getCodModalidadEnvio());
            log.info("correspondenciaTransformToFull ClaseEnvio" + correspondenciaDTO.getCodClaseEnvio());
            EstadoCorrespondenciaEnum state = EstadoCorrespondenciaEnum.getEstadoCorrespondenciaBy(correspondenciaDTO.getCodEstado());
            return CorrespondenciaFullDTO.newInstance()
                    .codModalidadEnvio(correspondenciaDTO.getCodModalidadEnvio())
                    .descModalidadEnvio(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodModalidadEnvio()))
                    .codClaseEnvio(correspondenciaDTO.getCodClaseEnvio())
                    .descClaseEnvio(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodClaseEnvio()))
                    .codDependencia(correspondenciaDTO.getCodDependencia())
                    .descDependencia(organigramaAdministrativoControl.consultarNombreElementoByCodOrg(correspondenciaDTO.getCodDependencia()))
                    .codEmpMsj(correspondenciaDTO.getCodEmpMsj())
                    .descEmpMsj(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodEmpMsj()))
                    .codEstado(correspondenciaDTO.getCodEstado())
                    .descEstado(state != null ? state.getNombre() : null)
                    .codFuncRadica(correspondenciaDTO.getCodFuncRadica())
                    .descFuncRadica(funcionario)
                    .codMedioRecepcion(correspondenciaDTO.getCodMedioRecepcion())
                    .descMedioRecepcion(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodMedioRecepcion()))
                    .codSede(correspondenciaDTO.getCodSede())
                    .descSede(organigramaAdministrativoControl.consultarNombreElementoByCodOrg(correspondenciaDTO.getCodSede()))
                    .codTipoCmc(correspondenciaDTO.getCodTipoCmc())
                    .descTipoCmc(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodTipoCmc()))
                    .codTipoDoc(correspondenciaDTO.getCodTipoDoc())
                    .descTipoDoc(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodTipoDoc()))
                    .codUnidadTiempo(correspondenciaDTO.getCodUnidadTiempo())
                    .descUnidadTiempo(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodUnidadTiempo()))
                    .fecDocumento(correspondenciaDTO.getFecDocumento())
                    .fecRadicado(correspondenciaDTO.getFecRadicado())
                    .fecVenGestion(correspondenciaDTO.getFecVenGestion())
                    .ideDocumento(correspondenciaDTO.getIdeDocumento())
                    .ideInstancia(correspondenciaDTO.getIdeInstancia())
                    .inicioConteo(correspondenciaDTO.getInicioConteo())
                    .nroGuia(correspondenciaDTO.getNroGuia())
                    .nroRadicado(correspondenciaDTO.getNroRadicado())
                    .reqDigita(correspondenciaDTO.getReqDigita())
                    .reqDistFisica(correspondenciaDTO.getReqDistFisica())
                    .tiempoRespuesta(correspondenciaDTO.getTiempoRespuesta())
                    .build();
        } catch (Exception e) {
            log.error("Business Control - a system error has occurred {}", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error -- " + e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ComunicacionOficialFullDTO listarFullCorrespondenciaByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        try {

            CorrespondenciaDTO correspondenciaDTO = consultarCorrespondenciaByNroRadicado(nroRadicado, false);
            log.info("CorrespondenciaDTO --------------------------------- {}", correspondenciaDTO);
            CorrespondenciaFullDTO correspondenciaFullDTO = correspondenciaTransformToFull(correspondenciaDTO);
            ComunicacionOficialFullDTO comunicacionOficialFullDTO = consultarComunicacionOficialFullByCorrespondencia(correspondenciaFullDTO);
            log.info("comunicacionOficialFullDTO --------------------------------- {}", comunicacionOficialFullDTO);
            if (ObjectUtils.isEmpty(comunicacionOficialFullDTO)) {
                return ComunicacionOficialFullDTO.newInstance().build();
            }
            return comunicacionOficialFullDTO;

        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred {}", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error -- " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param correspondenciaDTO
     * @throws SystemException
     */
    public void actualizarEstadoCorrespondencia(CorrespondenciaDTO correspondenciaDTO) throws SystemException {
        try {
            log.info("CorrespondenciaDTO --------------------------------- {}", correspondenciaDTO);
            final String nroRadicado = correspondenciaDTO.getNroRadicado();
            if (StringUtils.isEmpty(nroRadicado)) {
                throw new SystemException("No se ha recibido el numero de radicado value: " + nroRadicado);
            }
            final String codEstado = correspondenciaDTO.getCodEstado();
            if (StringUtils.isEmpty(codEstado)) {
                throw new SystemException("No se ha recibido el Codigo del estado value: " + codEstado);
            }
            List<CorCorrespondencia> resultList = em.createNamedQuery("CorCorrespondencia.findByNroRadicadoObj", CorCorrespondencia.class)
                    .setParameter("NRO_RADICADO", "%" + nroRadicado)
                    .getResultList();
            log.info("resullist CorCorrespondencia-----------------------------------", resultList);
            if (resultList.size() > 1) {
                throw new SystemException("correspondencia.correspondencia_not_exist_by_nroRadicado");
            }
            if (!resultList.isEmpty()) {
                CorCorrespondencia corCorrespondencia = resultList.get(0);
                List<DctAsignacion> asignacion = corCorrespondencia.getDctAsignacionList();
                if (!ObjectUtils.isEmpty(asignacion)) {
                    asignacion.get(0).setFecCambio(new Date());
                    log.info("La lista que va a guardar");
                    corCorrespondencia.setDctAsignacionList(asignacion);
                }
                List<PpdDocumento> ppdDocumentos = em.createNamedQuery("PpdDocumento.findByIdeDocumentoObj", PpdDocumento.class)
                        .setParameter("ID_DOC", corCorrespondencia.getIdeDocumento())
                        .getResultList();
                log.info("ppdDocumentos-------------------------------------------------", resultList);
                if (!ObjectUtils.isEmpty(ppdDocumentos) && !StringUtils.isEmpty(correspondenciaDTO.getIdEcm())) {
                    PpdDocumento documento = ppdDocumentos.get(0);
                    documento.setIdeEcm(correspondenciaDTO.getIdEcm());
                    em.merge(documento);
                }
                corCorrespondencia.setCodEstado(codEstado);
                if (!ObjectUtils.isEmpty(correspondenciaDTO) && !ObjectUtils.isEmpty(correspondenciaDTO.getContingencia())) {
                    corCorrespondencia.setContingencia(correspondenciaDTO.getContingencia());
                }
                em.merge(corCorrespondencia);
                em.flush();
                return;
            }
            log.info("Start Executing named query CorCorrespondencia.updateEstado");

        } catch (Exception ex) {
            log.error("system.generic.error --  {}", ex);
            throw new SystemException("system.generic.error -- " + ex.getMessage());
        }
    }


    public void actualizaroCorrespondenciaSalida(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        try {
            if (!verificarByNroRadicado(correspondenciaDTO.getNroRadicado())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                        .buildBusinessException();
            }
            if (!StringUtils.isEmpty(correspondenciaDTO.getNroRadicado()) && !StringUtils.isEmpty(correspondenciaDTO.getFechaGuia())
                    && !StringUtils.isEmpty(correspondenciaDTO.getProveedor()) && !StringUtils.isEmpty(correspondenciaDTO.getPeso())
                    && !StringUtils.isEmpty(correspondenciaDTO.getValorEnvio())) {
                em.createNamedQuery("CorCorrespondencia.updateRadicacionSalida")
                        .setParameter("NRO_RADICADO", correspondenciaDTO.getNroRadicado())
                        .setParameter("FECHA_GUIA", correspondenciaDTO.getFechaGuia())
                        .setParameter("PESO", correspondenciaDTO.getPeso())
                        .setParameter("PROVEEDOR", correspondenciaDTO.getProveedor())
                        .setParameter("VALOR_ENVIO", correspondenciaDTO.getValorEnvio())
                        .executeUpdate();

            }
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred {}", e);
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error -- " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param correspondenciaDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarIdeInstancia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        try {
            if (!verificarByNroRadicado(correspondenciaDTO.getNroRadicado())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                        .buildBusinessException();
            }
            CorrespondenciaDTO correspondenciaDTO1 = consultarCorrespondenciaByNroRadicado(correspondenciaDTO.getNroRadicado(), false);

            if (!ObjectUtils.isEmpty(correspondenciaDTO1)) {
                CorCorrespondencia correspondencia = em.find(CorCorrespondencia.class, correspondenciaDTO1.getIdeDocumento());
                correspondencia.setIdeInstancia(correspondenciaDTO.getIdeInstancia());
                em.merge(correspondencia);
            }

        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred {}", e);
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error -- " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public boolean actualizarIdeInstanciaDevolucion(CorrespondenciaDTO correspondenciaDTO) throws SystemException {
        try {

            log.info("este es el request --------------------------------{}", correspondenciaDTO);
            if (ObjectUtils.isEmpty(correspondenciaDTO)) {
                return false;
            }
            if (StringUtils.isEmpty(correspondenciaDTO.getNroRadicado()) || StringUtils.isEmpty(correspondenciaDTO.getIdeInstancia())) {
                return false;
            }
            CorrespondenciaDTO correspondenciaDTO1 = consultarCorrespondenciaByNroRadicado(correspondenciaDTO.getNroRadicado(), false);
            log.info("esta es la correspondencia que devuelve--------------------------------{}", correspondenciaDTO1);
            CorCorrespondencia correspondencia = em.find(CorCorrespondencia.class, correspondenciaDTO1.getIdeDocumento());
            correspondencia.setIdeInstanciaDevolucion(correspondenciaDTO.getIdeInstancia());
            em.merge(correspondencia);
            /* em.createNamedQuery("CorCorrespondencia.updateIdeInstanciaDevolucion")
                    .setParameter("NRO_RADICADO", correspondenciaDTO.getNroRadicado())
                    .setParameter("IDE_INSTANCIA", correspondenciaDTO.getIdeInstancia())
                    .executeUpdate();*/
            return true;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error -- " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param nroRadicado
     * @throws BusinessException
     * @throws SystemException
     */
    public String getIdeInstanciaPorRadicado(String nroRadicado) throws BusinessException, SystemException {
        try {
            if (!verificarByNroRadicado(nroRadicado)) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                        .buildBusinessException();
            }

            return em.createNamedQuery("CorCorrespondencia.getIdeInstanciaPorRadicado", String.class)
                    .setParameter("NRO_RADICADO", nroRadicado)
                    .getSingleResult();


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


    public AsignacionDTO obtenerObersacionByIdDocumento(BigInteger idDocumento) {


        List<AsignacionDTO> asignacion = em.createNamedQuery("DctAsignacion.findObservacionByIdDocumento", AsignacionDTO.class)
                .setParameter("IDE_DOCUMENTO", idDocumento)
                .getResultList();
        return ObjectUtils.isEmpty(asignacion) ? AsignacionDTO.newInstance().build() : asignacion.get(0);
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
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ComunicacionesOficialesDTO listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(Date fechaIni, Date fechaFin, String codDependencia, String codEstado, BigInteger idFuncionario, String nroRadicado) throws SystemException {
        log.info("FechaInicio: " + fechaIni.toString());
        log.info("FechaFin: " + fechaFin.toString());
        log.info("codDependencia: " + codDependencia);
        log.info("codEstado: " + codEstado);
        log.info("nroRadicado: " + nroRadicado);
        log.info("idFuncionario: " + idFuncionario);
        try {
            final EstadoCorrespondenciaEnum state = EstadoCorrespondenciaEnum.getEstadoCorrespondenciaBy(codEstado);
            if (null == state) {
                throw new SystemException("Invalid Correspondencia State with value null");
            }

            Calendar fecha = Calendar.getInstance();
            fecha.setTime(fechaFin);
            fecha.add(Calendar.DAY_OF_MONTH, 1);
            fecha.add(Calendar.HOUR, 23);
            fechaFin = fecha.getTime();

            log.info("##########$$$$$$$$$@@@@@@@@@@@@@@@@@@@-----------------------" + fechaIni + "##############" + fechaFin);
            List<CorrespondenciaDTO> correspondenciaDTOList;

            if (!ObjectUtils.isEmpty(idFuncionario)) {
                correspondenciaDTOList = em.createNamedQuery("CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicadoAndIdFunci", CorrespondenciaDTO.class)
                        .setParameter("FECHA_INI", fechaIni, TemporalType.DATE)
                        .setParameter("FECHA_FIN", fechaFin, TemporalType.DATE)
                        .setParameter("COD_ESTADO", codEstado)
                        .setParameter("COD_DEPENDENCIA", codDependencia)
                        .setParameter("COD_TIPO_REMITENTE", TipoRemitenteEnum.INTERNO.getCodigo())
                        .setParameter("NRO_RADICADO", StringUtils.isEmpty(nroRadicado) ? "%%" : "%" + nroRadicado + "%")
                        .setParameter("ID_FUNCI", idFuncionario)
                        .getResultList();
            } else {
                correspondenciaDTOList = em.createNamedQuery("CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado", CorrespondenciaDTO.class)
                        .setParameter("REGISTRADO", EstadoAgenteEnum.REGISTRADO.getCodigo())
                        .setParameter("FECHA_INI", fechaIni, TemporalType.DATE)
                        .setParameter("FECHA_FIN", fechaFin, TemporalType.DATE)
                        .setParameter("COD_ESTADO", codEstado)
                        .setParameter("COD_DEPENDENCIA", codDependencia)
                        .setParameter("COD_TIPO_REMITENTE", TipoRemitenteEnum.INTERNO.getCodigo())
                        .setParameter("NRO_RADICADO", StringUtils.isEmpty(nroRadicado) ? "%%" : "%" + nroRadicado + "%")
                        .getResultList();
            }
            log.info("##############" + correspondenciaDTOList);

            List<ComunicacionOficialDTO> comunicacionOficialDTOList = new ArrayList<>();
            List<AgenteDTO> agenteDTOList = new ArrayList<>();
            for (CorrespondenciaDTO correspondenciaDTO : correspondenciaDTOList) {
                log.info("esto es lo que llega idDocumento {}, codDependencia {}, estado {}", correspondenciaDTO.getIdeDocumento(), codDependencia, codEstado);
                // List<AgenteDTO> agenteDTOList = agenteControl.listarDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado(correspondenciaDTO.getIdeDocumento(), codDependencia, codEstado);

                AgenteDTO agenteDTO = agenteControl.ObtenerAgenteTPAGEIPorIdDocumento(correspondenciaDTO.getIdeDocumento());
                correspondenciaDTO.setCodDependencia(agenteDTO.getCodDependencia());
                agenteDTOList = agenteControl.listarAgentesPorIdDocumento(correspondenciaDTO.getIdeDocumento());

                ComunicacionOficialDTO comunicacionOficialDTO = ComunicacionOficialDTO.newInstance()
                        .correspondencia(correspondenciaDTO)
                        .agenteList(agenteDTOList)
                        .build();
                comunicacionOficialDTOList.add(comunicacionOficialDTO);
            }

            return ComunicacionesOficialesDTO.newInstance().comunicacionesOficiales(comunicacionOficialDTOList).build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error Error " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param fechaIni
     * @param fechaFin
     * @param codDependencia
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ComunicacionOficialSalidaFullDTO> listarComunicacionDeSalidaConDistribucionFisicaSinDistribuir(String fechaIni,
                                                                                                               String fechaFin,
                                                                                                               String codDependencia,
                                                                                                               String tipoDoc,
                                                                                                               String nroRadicado) throws BusinessException, SystemException {
        log.info("CorrespondenciaControl: listarComunicacionDeSalidaConDistribucionFisica");
        log.info("FechaInicio: " + fechaIni);
        log.info("FechaFin: " + fechaFin);
        log.info("codDependencia: " + codDependencia);
        log.info("claseEnvio: " + tipoDoc);
        log.info("nroRadicado: " + nroRadicado);

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicial = (fechaIni == null || fechaIni.isEmpty()) ? null : dateFormat.parse(fechaIni);
            Date fechaFinal = (fechaFin == null || fechaFin.isEmpty()) ? null : dateFormat.parse(fechaFin);

            if (fechaInicial != null && fechaFinal != null) {
                if (fechaInicial.getTime() > fechaFinal.getTime() || fechaInicial.getTime() == fechaFinal.getTime())
                    throw ExceptionBuilder.newBuilder()
                            .withMessage("La fecha final no puede ser igual o menor que la fecha inicial.")
                            .buildBusinessException();
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaFinal);
            cal.add(Calendar.DATE, 1);
            fechaFinal = cal.getTime();

            log.info("CorrespondenciaControl: listarComunicacionDeSalidaConDistribucionFisica");
            log.info("FechaInicio: " + fechaInicial);
            log.info("FechaFin: " + fechaFinal);
            log.info("codDependencia: " + codDependencia);
            log.info("tipoDoc: " + tipoDoc);
            log.info("nroRadicado: " + nroRadicado);


            List<ComunicacionOficialSalidaDTO> resultList = em.createNamedQuery("CorCorrespondencia.findByComunicacionsSalidaConDistribucionFisicaSinDistribucion", ComunicacionOficialSalidaDTO.class)
                    .setParameter("NRO_RADICADO", StringUtils.isEmpty(nroRadicado) ? "%%" : "%" + nroRadicado + "%")
                    .setParameter("COD_DEPENDENCIA", StringUtils.isEmpty(codDependencia) ? "%%" : codDependencia)
                    .setParameter("FECHA_FIN", fechaFinal)
                    .setParameter("FECHA_INI", fechaInicial)
                    .setParameter("TIPO_DOC", StringUtils.isEmpty(tipoDoc) ? "%%" : tipoDoc)
                    .getResultList();

            log.info("###########################################" + resultList);

            List<ComunicacionOficialSalidaFullDTO> resultados = new ArrayList<>();

            String pais = null;
            String departamento = null;
            String municipio = null;
            String destinatario = null;
            List<AnexoFullDTO> anexos = new ArrayList<>();
            for (ComunicacionOficialSalidaDTO item : resultList) {
                AgenteDTO agenteDTO = new AgenteDTO();
                agenteDTO = agenteControl.ObtenerAgenteTPAGEIPorIdDocumento(item.getIdDocumento());
                if (!StringUtils.isEmpty(item.getTipoComunicacion()) && "SI".equalsIgnoreCase(item.getTipoComunicacion())) {
                    destinatario = dependenciaControl.listarDependenciaByCodigoObj(agenteDTO.getCodDependencia()).getNomDependencia();
                }
                if (!StringUtils.isEmpty(item.getTipoComunicacion()) && "SE".equalsIgnoreCase(item.getTipoComunicacion())) {
                    destinatario = item.getRazonSocial();
                }
                if (!StringUtils.isEmpty(item.getNroRadicado())) {
                    anexos = anexoControl.listarAnexosByNroRadicado(item.getNroRadicado()).getAnexos();

                }
                if (!StringUtils.isEmpty(item.getPais())) {
                    List<String> paises = em.createNamedQuery("TvsPais.findPaisByCodigo", String.class)
                            .setParameter("COD_PAIS", item.getPais())
                            .getResultList();
                    pais = paises.get(0);
                    log.info("!!!!!!!!!!!!!!!!!!" + pais + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                if (!StringUtils.isEmpty(item.getDepartamento())) {
                    List<String> departamentos = em.createNamedQuery("TvsDepartamento.findByCodDepart", String.class)
                            .setParameter("COD_DEPART", item.getDepartamento())
                            .getResultList();
                    departamento = departamentos.get(0);
                    log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + departamento + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                }
                if (!StringUtils.isEmpty(item.getMunicipio())) {
                    List<String> municipios = em.createNamedQuery("TvsMunicipio.findByCodMunic", String.class)
                            .setParameter("COD_MUNI", item.getMunicipio())
                            .getResultList();
                    municipio = municipios.get(0);
                    log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + municipio + "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                }
                ComunicacionOficialSalidaFullDTO nuevo = ComunicacionOficialSalidaFullDTO.newInstance()
                        .idAgente(item.getIdAgente())
                        .idDocumento(item.getIdDocumento())
                        .nroRadicado(item.getNroRadicado())
                        .fechaRadicacion(item.getFechaRadicacion())
                        .dependencia(destinatario)
                        .nombre(item.getRazonSocial())
                        .tipoDocumento(item.getTipoDocumento())
                        .nroIdentificacion(item.getNit())
                        .pais(ObjectUtils.isEmpty(pais) ? " " : pais)
                        .departamento(ObjectUtils.isEmpty(departamento) ? " " : departamento)
                        .municipio(ObjectUtils.isEmpty(municipio) ? " " : municipio)
                        .ciudad(item.getCiudad())
                        .direccion(item.getDireccion())
                        .folios(item.getFolios())
                        .anexos(item.getAnexos())
                        .asunto(item.getAsunto())
                        .listaAnexos(anexos)
                        .tipoComunicacion(item.getTipoComunicacion())
                        .build();
                resultados.add(nuevo);
                log.info("resultado agregado---------------------------------{}", nuevo);
            }

            log.info("estos son los resultados---------------------------------{}", resultados);
            return ObjectUtils.isEmpty(resultados) ? new ArrayList<>() : resultados;
        } catch (ParseException pe) {
            log.error("Business Control - a system error has occurred {}", pe);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("parse.exception.error -- " + pe.getMessage())
                    .withRootException(pe)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error -- " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }

    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ComunicacionOficialSalidaFullDTO> listarComunicacionDeSalidaConDistribucionFisica(String fechaIni,
                                                                                                  String fechaFin,
                                                                                                  String modEnvio,
                                                                                                  String claseEnvio,
                                                                                                  String codDependencia,
                                                                                                  String tipoDoc,
                                                                                                  String nroRadicado,
                                                                                                  String tipoComunicacion) throws BusinessException, SystemException {
        log.info("CorrespondenciaControl: listarComunicacionDeSalidaConDistribucionFisica");
        log.info("FechaInicio: " + fechaIni);
        log.info("FechaFin: " + fechaFin);
        log.info("codDependencia: " + codDependencia);
        log.info("modEnvio: " + modEnvio);
        log.info("claseEnvio: " + claseEnvio);
        log.info("claseEnvio: " + tipoDoc);
        log.info("nroRadicado: " + nroRadicado);

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicial = (fechaIni == null || fechaIni.isEmpty()) ? null : dateFormat.parse(fechaIni);
            Date fechaFinal = (fechaFin == null || fechaFin.isEmpty()) ? null : dateFormat.parse(fechaFin);

            if (fechaInicial != null && fechaFinal != null) {
                if (fechaInicial.getTime() > fechaFinal.getTime())
                    throw ExceptionBuilder.newBuilder()
                            .withMessage("La fecha final no puede ser igual o menor que la fecha inicial.")
                            .buildBusinessException();
            }
            //|| fechaInicial.getTime() == fechaFinal.getTime()

            List<ComunicacionOficialFullDTO> comunicacionOficialFullDTOList = new ArrayList<>();

            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaFinal);
            cal.add(Calendar.DATE, 1);
            fechaFinal = cal.getTime();

            log.info("CorrespondenciaControl: listarComunicacionDeSalidaConDistribucionFisica");
            log.info("FechaInicio: " + fechaInicial);
            log.info("FechaFin: " + fechaFinal);
            log.info("codDependencia: " + codDependencia);
            log.info("modEnvio: " + modEnvio);
            log.info("claseEnvio: " + claseEnvio);
            log.info("tipoDoc: " + tipoDoc);
            log.info("nroRadicado: " + nroRadicado);
            log.info("tipoComunicacion: " + tipoComunicacion);


            List<ComunicacionOficialSalidaDTO> resultList = new ArrayList<>();
            if (!StringUtils.isEmpty(tipoComunicacion) && "SI".equals(tipoComunicacion)) {
                log.info("entro en la interna ----------------------------------------------------------------------- SI");
                resultList = em.createNamedQuery("CorCorrespondencia.findByComunicacionsSalidaInternaConDistribucionFisica", ComunicacionOficialSalidaDTO.class)
                        .setParameter("NRO_RADICADO", StringUtils.isEmpty(nroRadicado) ? "%%" : "%" + nroRadicado + "%")
                        .setParameter("COD_DEPENDENCIA", StringUtils.isEmpty(codDependencia) ? "%%" : codDependencia)
                        .setParameter("FECHA_FIN", fechaFinal, TemporalType.TIMESTAMP)
                        .setParameter("FECHA_INI", fechaInicial, TemporalType.TIMESTAMP)
                        .setParameter("TIPO_DOC", StringUtils.isEmpty(tipoDoc) ? "%%" : tipoDoc)
                        .setParameter("TIPO_COM", StringUtils.isEmpty(tipoComunicacion) ? "SI" : tipoComunicacion)
                        .getResultList();
            } else {
                log.info("entro en la externa ----------------------------------------------------------------------- SE");
                resultList = em.createNamedQuery("CorCorrespondencia.findByComunicacionsSalidaConDistribucionFisica", ComunicacionOficialSalidaDTO.class)
                        .setParameter("NRO_RADICADO", StringUtils.isEmpty(nroRadicado) ? "%%" : "%" + nroRadicado + "%")
                        .setParameter("COD_DEPENDENCIA", StringUtils.isEmpty(codDependencia) ? "%%" : codDependencia)
                        .setParameter("COD_MOD_ENVIO", StringUtils.isEmpty(modEnvio) ? "%%" : modEnvio)
                        .setParameter("COD_CLASE_ENVIO", StringUtils.isEmpty(claseEnvio) ? "%%" : claseEnvio)
                        .setParameter("FECHA_FIN", fechaFinal, TemporalType.TIMESTAMP)
                        .setParameter("FECHA_INI", fechaInicial, TemporalType.TIMESTAMP)
                        .setParameter("TIPO_DOC", StringUtils.isEmpty(tipoDoc) ? "%%" : tipoDoc)
                        .setParameter("TIPO_COM", StringUtils.isEmpty(tipoComunicacion) ? "SE" : tipoComunicacion)
                        .getResultList();
            }
            log.info("###########################################" + resultList);

            List<ComunicacionOficialSalidaFullDTO> resultados = new ArrayList<>();

            String pais = null;
            String departamento = null;
            String municipio = null;
            for (ComunicacionOficialSalidaDTO item : resultList) {
                if (!StringUtils.isEmpty(item.getPais())) {
                    List<String> paises = em.createNamedQuery("TvsPais.findPaisByCodigo", String.class)
                            .setParameter("COD_PAIS", item.getPais())
                            .getResultList();
                    pais = paises.get(0);
                    log.info("!!!!!!!!!!!!!!!!!!" + pais + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                if (!StringUtils.isEmpty(item.getDepartamento())) {
                    List<String> departamentos = em.createNamedQuery("TvsDepartamento.findByCodDepart", String.class)
                            .setParameter("COD_DEPART", item.getDepartamento())
                            .getResultList();
                    departamento = departamentos.get(0);
                    log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + departamento + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                }
                if (!StringUtils.isEmpty(item.getMunicipio())) {
                    List<String> municipios = em.createNamedQuery("TvsMunicipio.findByCodMunic", String.class)
                            .setParameter("COD_MUNI", item.getMunicipio())
                            .getResultList();
                    municipio = municipios.get(0);
                    log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + municipio + "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                }
                ComunicacionOficialSalidaFullDTO nuevo = ComunicacionOficialSalidaFullDTO.newInstance()
                        .idAgente(item.getIdAgente())
                        .idDocumento(item.getIdDocumento())
                        .nroRadicado(item.getNroRadicado())
                        .fechaRadicacion(item.getFechaRadicacion())
                        .dependencia(item.getCodDependencia())
                        .nombre(item.getRazonSocial())
                        .tipoDocumento(item.getTipoDocumento())
                        .nroIdentificacion(item.getNit())
                        .pais(ObjectUtils.isEmpty(pais) ? " " : pais)
                        .departamento(ObjectUtils.isEmpty(departamento) ? " " : departamento)
                        .municipio(ObjectUtils.isEmpty(municipio) ? " " : municipio)
                        .ciudad(item.getCiudad())
                        .direccion(item.getDireccion())
                        .folios(item.getFolios())
                        .anexos(item.getAnexos())
                        .asunto(item.getAsunto())
                        .build();
                resultados.add(nuevo);
            }


            return ObjectUtils.isEmpty(resultados) ? new ArrayList<>() : resultados;
        } catch (ParseException pe) {
            log.error("Business Control - a system error has occurred {}", pe);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("parse.exception.error -- " + pe.getMessage())
                    .withRootException(pe)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error -- " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }

    }

    /**
     * @param fechaIni
     * @param fechaFin
     * @param codDependencia
     * @param codTipoDoc
     * @param modEnvio
     * @param claseEnvio
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ComunicacionesOficialesDTO listarComunicacionDeSalidaConDistribucionFisicaEmplantillada(Date fechaIni,
                                                                                                   Date fechaFin,
                                                                                                   String modEnvio,
                                                                                                   String claseEnvio,
                                                                                                   String codDependencia,
                                                                                                   String codTipoDoc,
                                                                                                   String nroRadicado) throws BusinessException, SystemException {
        log.info("CorrespondenciaControl: listarComunicacionDeSalidaConDistribucionFisicaEmplantillada");
        log.info("FechaInicio: " + fechaIni);
        log.info("FechaFin: " + fechaFin);
        log.info("codDependencia: " + codDependencia);
        log.info("modEnvio: " + modEnvio);
        log.info("claseEnvio: " + claseEnvio);
        log.info("codTipoDoc: " + codTipoDoc);
        log.info("nroRadicado: " + nroRadicado);

        if (fechaIni != null && fechaFin != null) {
            if (fechaIni.getTime() > fechaFin.getTime() || fechaIni.getTime() == fechaFin.getTime())
                throw ExceptionBuilder.newBuilder()
                        .withMessage("La fecha final no puede ser igual o menor que la fecha inicial.")
                        .buildBusinessException();
        }
        try {
            List<CorrespondenciaDTO> correspondenciaDTOList = em.createNamedQuery("CorCorrespondencia.findByComunicacionsSalidaConDistribucionFisicaNroPlantillaNoAsociado", CorrespondenciaDTO.class)
                    .setParameter("REQ_DIST_FISICA", reqDistFisica)
                    .setParameter("TIPO_COM1", "SE")
                    .setParameter("TIPO_COM2", "SI")
                    .setParameter("COD_DEPENDENCIA", codDependencia)
                    .setParameter("CLASE_ENVIO", claseEnvio)
                    .setParameter("MOD_ENVIO", modEnvio)
                    .setParameter("ESTADO_DISTRIBUCION", EstadoDistribucionFisicaEnum.EMPLANILLADO.getCodigo())
                    .setParameter("TIPO_AGENTE", TipoAgenteEnum.DESTINATARIO.getCodigo())
                    .setParameter("FECHA_INI", fechaIni, TemporalType.DATE)
                    .setParameter("FECHA_FIN", fechaFin, TemporalType.DATE)
                    //.setParameter("COD_TIPO_DOC", codTipoDoc)
                    .setParameter("NRO_RADICADO", nroRadicado == null ? null : "%" + nroRadicado + "%")
                    .getResultList();

            List<ComunicacionOficialDTO> comunicacionOficialDTOList = new ArrayList<>();

            if (correspondenciaDTOList != null && !correspondenciaDTOList.isEmpty()) {
                for (CorrespondenciaDTO correspondenciaDTO : correspondenciaDTOList) {
                    List<AgenteDTO> agenteDTOList = agenteControl.listarDestinatariosByIdeDocumentoMail(correspondenciaDTO.getIdeDocumento());
                    ComunicacionOficialDTO comunicacionOficialDTO = ComunicacionOficialDTO.newInstance()
                            .correspondencia(correspondenciaDTO)
                            .agenteList(agenteDTOList)
                            .build();
                    comunicacionOficialDTOList.add(comunicacionOficialDTO);
                }
            }

            return ComunicacionesOficialesDTO.newInstance().comunicacionesOficiales(comunicacionOficialDTOList).build();

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
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
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ComunicacionesOficialesDTO listarCorrespondenciaSinDistribuir(Date fechaIni, Date fechaFin, String codDependencia, String codTipoDoc, String nroRadicado) throws BusinessException, SystemException {
        try {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(fechaFin);
            cal.add(Calendar.DATE, 1);
            final ComunicacionesOficialesDTO comunicacionesOficialesDTO = ComunicacionesOficialesDTO.newInstance().comunicacionesOficiales(new ArrayList<>()).build();
            final List<CorrespondenciaDTO> correspondenciaDTOList = em.createNamedQuery("CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodTipoDocAndNroRadicado", CorrespondenciaDTO.class)
                    .setParameter("FECHA_INI", fechaIni, TemporalType.DATE)
                    .setParameter("FECHA_FIN", cal.getTime(), TemporalType.DATE)
                    .setParameter("REQ_DIST_FISICA", reqDistFisica)
                    .setParameter("COD_DEPENDENCIA", codDependencia)
                    .setParameter("COD_TIP_AGENT", TipoAgenteEnum.DESTINATARIO.getCodigo())
                    .setParameter("ESTADO_DISTRIBUCION", EstadoDistribucionFisicaEnum.SIN_DISTRIBUIR.getCodigo())
                    .setParameter("COD_TIPO_DOC", codTipoDoc)
                    .setParameter("NRO_RADICADO", nroRadicado == null ? null : "%" + nroRadicado + "%")
                    .getResultList();

            if (correspondenciaDTOList.isEmpty()) {
                return comunicacionesOficialesDTO;
            }

            List<ComunicacionOficialDTO> comunicacionOficialDTOList = new ArrayList<>();

            log.info("comunicaciones que devuelve la query-----------------------------------------{}", correspondenciaDTOList);
            for (CorrespondenciaDTO correspondenciaDTO : correspondenciaDTOList) {
                List<AgenteDTO> agenteDTOList = agenteControl.listarDestinatariosByIdeDocumentoAndCodDependencia(correspondenciaDTO.getIdeDocumento(),
                        codDependencia);
                AgenteDTO agente = agenteControl.listarAgentePrincipalByIdeDocumento(correspondenciaDTO.getIdeDocumento());
                log.info("agente que devuelve el api listarAgentePrincipalByIdeDocumento -----------------------------------------{}", agente);
                if (!ObjectUtils.isEmpty(agente) && !ObjectUtils.isEmpty(agente.getIdeAgente())) {
                    DatosContactoDTO datosContactoDTO = datosContactoControl.consultarDatosContactoPrincipalByIdAgente(agente.getIdeAgente());
                    log.info("datos contacto que devuelve consultarDatosContactoPrincipalByIdAgente -----------------------------------------{}", datosContactoDTO);
                    if (!ObjectUtils.isEmpty(datosContactoDTO)) {
                        agente.getDatosContactoList().add(datosContactoDTO);
                    }
                    agenteDTOList.add(agente);
                }

                log.info("comunicaciones agentes-----------------------------------------{}", agenteDTOList);
                ComunicacionOficialDTO comunicacionOficialDTO = ComunicacionOficialDTO.newInstance()
                        .correspondencia(correspondenciaDTO)
                        .ppdDocumentoList(ppdDocumentoControl.consultarPpdDocumentosByCorrespondencia(correspondenciaDTO.getIdeDocumento()))
                        .agenteList(agenteDTOList)
                        .build();
                log.info("comunicación comunicacionOficialDTO -----------------------------------------{}", agenteDTOList);
                comunicacionOficialDTOList.add(comunicacionOficialDTO);
            }
            log.info("comunicaciones distribucion entrada-----------------------------------------{}", comunicacionOficialDTOList);
            comunicacionesOficialesDTO.getComunicacionesOficiales().addAll(comunicacionOficialDTOList);
            return comunicacionesOficialesDTO;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex.getMessage());
            throw new SystemException("Business Control - a system error has occurred " + ex.getMessage());
        }
    }

    /**
     * @param correspondenciaDTO
     * @return
     */
    public ComunicacionOficialDTO consultarComunicacionOficialByCorrespondencia(CorrespondenciaDTO correspondenciaDTO) throws SystemException {

        //List<AgenteDTO> agenteDTOList = agenteControl.consltarAgentesByCorrespondencia(correspondenciaDTO.getIdeDocumento());
        List<AgenteDTO> agenteDTOList = agenteControl.listarAgentesPorIdDocumento(correspondenciaDTO.getIdeDocumento());

        List<DatosContactoDTO> datosContactoDTOList = datosContactoControl.consultarDatosContactoByAgentes(agenteDTOList);

        List<PpdDocumentoDTO> ppdDocumentoDTOList = ppdDocumentoControl.consultarPpdDocumentosByCorrespondencia(correspondenciaDTO.getIdeDocumento());

        List<AnexoDTO> anexoList = anexoControl.consultarAnexosByPpdDocumentos(ppdDocumentoDTOList);

        List<ReferidoDTO> referidoList = referidoControl.consultarReferidosByCorrespondencia(correspondenciaDTO.getIdeDocumento());

        return ComunicacionOficialDTO.newInstance()
                .correspondencia(correspondenciaDTO)
                .agenteList(agenteDTOList)
                .ppdDocumentoList(ppdDocumentoDTOList)
                .anexoList(anexoList)
                .referidoList(referidoList)
                .datosContactoList(datosContactoDTOList)
                .build();
    }


    /**
     * @param correspondenciaFullDTO
     * @return
     */
    public ComunicacionOficialFullDTO consultarComunicacionOficialFullByCorrespondencia(CorrespondenciaFullDTO correspondenciaFullDTO) throws SystemException, BusinessException {

        List<AgenteFullDTO> agenteFullDTOList = agenteControl.consultarAgentesFullByCorrespondencia(correspondenciaFullDTO.getIdeDocumento());

        List<DatosContactoFullDTO> datosContactoDTOList = datosContactoControl.consultarDatosContactoFullByAgentes(agenteFullDTOList);

        List<PpdDocumentoDTO> ppdDocumentoDTOList = ppdDocumentoControl.consultarPpdDocumentosByCorrespondencia(correspondenciaFullDTO.getIdeDocumento());

        List<AnexoDTO> anexoList = anexoControl.consultarAnexosByPpdDocumentos(ppdDocumentoDTOList);

        List<ReferidoDTO> referidoList = referidoControl.consultarReferidosByCorrespondencia(correspondenciaFullDTO.getIdeDocumento());
//        log.info("processing rest request - referidoControl.consultarReferidosByCorrespondencia OK");

        ComunicacionOficialFullDTO comunicacionOficialFullDTO = ComunicacionOficialFullDTO.newInstance()
                .correspondencia(correspondenciaFullDTO)
                .agentes(agenteFullDTOList)
                .ppdDocumentos(ppdDocumentoControl.ppdDocumentoListTransformToFull(ppdDocumentoDTOList))
                .anexos(anexoControl.anexoListTransformToFull(anexoList))
                .referidos(referidoList)
                .datosContactos(datosContactoDTOList)
                .build();
        log.info("reponse consultarComunicacionOficialFullByCorrespondencia--------------------------------------{}", comunicacionOficialFullDTO);
        return comunicacionOficialFullDTO;
    }

    /**
     * @param correspondenciaDTO
     * @return
     */
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
                .reqDistFisica(correspondenciaDTO.getReqDistFisica())
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

    /**
     * @param nroRadicado
     * @return
     */
    public Boolean verificarByNroRadicado(String nroRadicado) throws SystemException {
        try {
            log.info("Executing named query 'CorCorrespondencia.countByNroRadicado'");
            long cantidad = em.createNamedQuery("CorCorrespondencia.countByNroRadicado", Long.class)
                    .setParameter("NRO_RADICADO", nroRadicado + "%")
                    .getSingleResult();
            log.info("End named query 'CorCorrespondencia.countByNroRadicado' Value: {}", cantidad);
            return cantidad > 0;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public List<CorrespondenciaDTO> radicadosContingencia() {
        // findCorrespondenciasContingencia
        List<CorrespondenciaDTO> resultList = em.createNamedQuery("CorCorrespondencia.findCorrespondenciasContingencia", CorrespondenciaDTO.class)
                .getResultList();
        if (ObjectUtils.isEmpty(resultList)) {
            return new ArrayList<>();
        }
        for (CorrespondenciaDTO cor : resultList) {
            TvsOrganigramaAdministrativo organigramaDependencia = organigramaAdministrativoControl.consultarOrganigramaCodigo(cor.getCodDependencia());
            TvsOrganigramaAdministrativo organigramaSede = organigramaAdministrativoControl.consultarOrganigramaCodigo(cor.getCodSede());
            cor.setCodDependencia(cor.getCodDependencia().concat("-").concat(organigramaDependencia.getNomOrg()).concat("-").concat(organigramaDependencia.getAbreviatura()));
            cor.setCodSede(cor.getCodSede().concat("-").concat(organigramaSede.getNomOrg()).concat("-").concat(organigramaSede.getAbreviatura()));
        }
        return resultList;
    }

    /**
     * @param correspondenciaDTO
     * @return
     */
    public Date calcularFechaVencimientoGestionViejo(CorrespondenciaDTO correspondenciaDTO) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(correspondenciaDTO.getFecRadicado());
        if (diaSiguienteHabil.equals(correspondenciaDTO.getInicioConteo()))
            calendario.setTime(calcularDiaHabilSiguiente(calendario.getTime()));

        String tiempoRespuesta = (correspondenciaDTO.getTiempoRespuesta() == null) ? "0" : correspondenciaDTO.getTiempoRespuesta();
        Long tiempoDuracionTramite = Long.parseLong(tiempoRespuesta);
        long cantHorasLaborales = horasHabilesDia(horarioLaboral[0], horarioLaboral[1]);
        if (unidadTiempoDias.equals(correspondenciaDTO.getCodUnidadTiempo()))
            tiempoDuracionTramite *= cantHorasLaborales;

        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
        while (tiempoDuracionTramite > 0) {

            long horasHabilesDia = horasHabilesDia(formatoHora.format(calendario.getTime()), horarioLaboral[1]);

            if (horasHabilesDia >= 0) {
                if (horasHabilesDia >= tiempoDuracionTramite)
                    calendario.add(Calendar.HOUR, tiempoDuracionTramite.intValue());
                tiempoDuracionTramite -= horasHabilesDia;
            }

            if (tiempoDuracionTramite > 0)
                calendario.setTime(calcularDiaHabilSiguiente(calendario.getTime()));
        }

        return calendario.getTime();
    }

    public Date calcularFechaVencimientoGestion(CorrespondenciaDTO correspondenciaDTO) throws ParseException {

        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(correspondenciaDTO.getFecRadicado());

        log.info("CALCULO FECHA DE VENCIMIENTO -----> " + correspondenciaDTO.getFecRadicado());

        long cantHorasLaborales = horasHabilesDia(horarioLaboral[0], horarioLaboral[1]);

        String tiempoRespuesta = (correspondenciaDTO.getTiempoRespuesta() == null) ? "0" : correspondenciaDTO.getTiempoRespuesta();
        long tiempoDuracionTramite = Long.parseLong(tiempoRespuesta);


        if (unidadTiempoDias.equals(correspondenciaDTO.getCodUnidadTiempo())) {
            if (diaSiguienteHabil.equals(correspondenciaDTO.getInicioConteo()))
                calendar.setTime(calcularDiaHabilSiguiente(calendar.getTime()));

            tiempoDuracionTramite *= cantHorasLaborales;

            while (tiempoDuracionTramite > 0) {

                long horasHabilesDia = horasHabilesDia(formatoHora.format(calendar.getTime()), horarioLaboral[1]);

                if (horasHabilesDia >= 0) {
                    if (horasHabilesDia >= tiempoDuracionTramite)
                        calendar.add(Calendar.HOUR_OF_DAY, (int) tiempoDuracionTramite);
                    tiempoDuracionTramite -= horasHabilesDia;
                }

                if (tiempoDuracionTramite > 0)
                    calendar.setTime(calcularDiaHabilSiguiente(calendar.getTime()));
            }
        }
        else if (unidadTiempoHoras.equals(correspondenciaDTO.getCodUnidadTiempo())) {

            while (tiempoDuracionTramite > 0) {

                long horasHabilesDia = horasHabilesDia(formatoHora.format(calendar.getTime()), horarioLaboral[1]);

                if (horasHabilesDia >= 0) {
                    if (horasHabilesDia >= tiempoDuracionTramite)
                        calendar.add(Calendar.HOUR_OF_DAY, (int) tiempoDuracionTramite);
                    tiempoDuracionTramite -= horasHabilesDia;
                }

                if (tiempoDuracionTramite > 0)
                    calendar.setTime(calcularDiaHabilSiguiente(calendar.getTime()));
            }
        }

        log.info("FECHA VENCIMIENTO CALCULADA FINAL  --> " + calendar.getTime());

        return calendar.getTime();
    }

    /**
     * @param horaInicio
     * @param horaFin
     * @return
     */
    private long horasHabilesDia(String horaInicio, String horaFin) {
        return ChronoUnit.HOURS.between(LocalTime.parse(horaInicio), LocalTime.parse(horaFin));
    }

    /**
     * @param fecha
     * @return
     */
    public Date calcularDiaHabilSiguiente(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        do {
            calendario.add(Calendar.DATE, 1);
            if (calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                calendario.add(Calendar.DATE, 2);
        }
        while (!esDiaHabil(calendario.getTime()));

        String[] tiempo = horarioLaboral[0].split(":");
        calendario.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tiempo[0]));
        calendario.set(Calendar.MINUTE, Integer.parseInt(tiempo[1]));
        return calendario.getTime();
    }

    /**
     * @param fecha
     * @return
     */
    public Boolean esDiaHabil(Date fecha) {
        return !(esDiaFestivo(fecha) || esFinSemana(fecha));
    }

    /**
     * @param fecha
     * @return
     */
    public Boolean esDiaFestivo(Date fecha) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        return Arrays.stream(diasFestivos).anyMatch(x -> x.equals(formatoFecha.format(fecha)));
    }

    /**
     * @param fecha
     * @return
     */
    public Boolean esFinSemana(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        return calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    /**
     * @param nroRadicado
     * @return
     */
    public CorrespondenciaDTO consultarCorrespondenciaByNroRadicado(String nroRadicado, boolean devolucion) throws SystemException {
        log.info("processing rest request - CorrespondenciaControl-consultarCorrespondenciaByNroRadicado {},{}", nroRadicado, devolucion);
        try {
            if (!devolucion) {
                List<CorrespondenciaDTO> correspondencias = em.createNamedQuery("CorCorrespondencia.findByNroRadicado", CorrespondenciaDTO.class)
                        .setParameter("NRO_RADICADO", "%" + nroRadicado + "%")
                        .getResultList();
                if (correspondencias.size() > 1 || correspondencias.isEmpty()) throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_by_nroRadicado_duplicated_or_empty")
                        .buildBusinessException();
                log.info("response ------------------------instancia {}", correspondencias.get(0));
                if (!ObjectUtils.isEmpty(correspondencias)) {
                    List<PpdDocumento> documentos = em.createNamedQuery("PpdDocumento.findByIdeDocumentoObj", PpdDocumento.class)
                            .setParameter("ID_DOC", correspondencias.get(0).getIdeDocumento())
                            .getResultList();
                    if (!ObjectUtils.isEmpty(documentos)) {
                        correspondencias.get(0).setCodTipoDoc(documentos.get(0).getCodTipoDoc());
                    }
                }
                return correspondencias.get(0);
            } else {
                List<CorrespondenciaDTO> correspondencias = em.createNamedQuery("CorCorrespondencia.findIfDevolucionByNroRadicado", CorrespondenciaDTO.class)
                        .setParameter("NRO_RADICADO", "%" + nroRadicado + "%")
                        .getResultList();
                if (!ObjectUtils.isEmpty(correspondencias)) {
                    List<PpdDocumento> documentos = em.createNamedQuery("PpdDocumento.findByIdeDocumentoObj", PpdDocumento.class)
                            .setParameter("ID_DOC", correspondencias.get(0).getIdeDocumento())
                            .getResultList();
                    if (!ObjectUtils.isEmpty(documentos)) {
                        correspondencias.get(0).setCodTipoDoc(documentos.get(0).getCodTipoDoc());
                    }
                }
                if (correspondencias.size() > 1 || correspondencias.isEmpty()) throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_by_nroRadicado_duplicated_or_empty")
                        .buildBusinessException();
                log.info("response ------------------------instanciaDevolucion {}", correspondencias.get(0));
                return correspondencias.get(0);
            }
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("An error has occurred " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public boolean actualizarCamposEntregaCorrespondencia(List<CorrespondenciaDTO> correspondenciaDTOList) throws SystemException {
        log.info("processing rest request - CorrespondenciaControl-actualizarCamposEntregaCorrespondencia {}", correspondenciaDTOList);

        try {

            for (CorrespondenciaDTO correspondencia : correspondenciaDTOList) {
                if (!StringUtils.isEmpty(correspondencia.getNroRadicado())) {
                    List<CorCorrespondencia> correspondencias = em.createNamedQuery("CorCorrespondencia.findByNroRadicadoObj", CorCorrespondencia.class)
                            .setParameter("NRO_RADICADO", correspondencia.getNroRadicado())
                            .getResultList();

                    if (!ObjectUtils.isEmpty(correspondencias)) {
                        CorCorrespondencia corObj = correspondencias.get(0);
                        corObj.setEstadoEntrega(correspondencia.getEstadoEntrega());
                        corObj.setFecEntrega(correspondencia.getFecEntrega());
                        corObj.setObservacionesEntrega(correspondencia.getObservacionesEntrega());
                        log.info("-------------------------------------------------------- persistiendo la correspondencia {}", corObj);
                        em.merge(corObj);
                    } else return false;
                } else return false;

            }
            return true;

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("An error has occurred " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    public boolean actualizarEnvioCorrespondencia(List<CorrespondenciaDTO> correspondenciaDTOList, boolean distribuido) throws SystemException {

        try {
            for (CorrespondenciaDTO correspondencia : correspondenciaDTOList) {
                if (!StringUtils.isEmpty(correspondencia.getNroRadicado())) {
                    List<CorCorrespondencia> correspondencias = em.createNamedQuery("CorCorrespondencia.findByNroRadicadoObj", CorCorrespondencia.class)
                            .setParameter("NRO_RADICADO", correspondencia.getNroRadicado())
                            .getResultList();
                    log.info("-----------------------Esto es lo que devuelve {}", correspondencias);
                    if (!ObjectUtils.isEmpty(correspondencias) && !distribuido) {
                        CorCorrespondencia corObj = correspondencias.get(0);
                        corObj.setFecEnvio(new Date());
                        corObj.setValorEnvio(correspondencia.getValorEnvio());
                        corObj.setPeso(correspondencia.getPeso());
                        corObj.setNroGuia(correspondencia.getNroGuia());
                        em.merge(corObj);
                        log.info("Esto es lo que guarda {}", corObj);
                    }
                    if (!ObjectUtils.isEmpty(correspondencias) && distribuido) {
                        CorCorrespondencia corObj = correspondencias.get(0);
                        corObj.setAceptado(correspondencia.getDistribuido());
                        em.merge(corObj);
                        log.info("Esto es lo que guarda {}", corObj);
                    } else return false;
                } else return false;

            }
            return true;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("An error has occurred update fecEntrega" + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param ideAgente
     * @return
     */
    public CorrespondenciaDTO consultarCorrespondenciaByIdeAgente(BigInteger ideAgente) throws BusinessException, SystemException {
        try {
            List<CorrespondenciaDTO> correspondencias = em.createNamedQuery("CorCorrespondencia.findByIdeAgente", CorrespondenciaDTO.class)
                    .setParameter("IDE_AGENTE", ideAgente)
                    .getResultList();
            if (ObjectUtils.isEmpty(correspondencias))
                return new CorrespondenciaDTO();
            return correspondencias.get(0);
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.correspondencia_not_exist_by_ideAgente")
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
     * @param ideDocumento
     * @return
     * @throws SystemException
     */
    public CorrespondenciaDTO consultarCorrespondenciaByIdeDocumento(BigInteger ideDocumento) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("CorCorrespondencia.findByIdeDocumento", CorrespondenciaDTO.class)
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.correspondencia_not_exist_by_ideDocumento")
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
     * @param ideDocumento
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public Date consultarFechaVencimientoByIdeDocumento(BigInteger ideDocumento) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("CorCorrespondencia.findFechaVenGestionByIdeDocumento", Date.class)
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.correspondencia_not_exist_by_ideDocumento")
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

    public String obtenerRadicadoFull(String radicado) throws SystemException {
        try {
            List<String> radicados = em.createNamedQuery("CorCorrespondencia.findRadicadoFull", String.class)
                    .setParameter("NRO_RADICADO", "%" + radicado + "%")
                    .getResultList();
            return radicados.isEmpty() ? "" : radicados.get(0);

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex.getMessage());
            throw new SystemException("system.generic.error -- " + ex.getMessage());
        }
    }

    private Boolean verificarAgenteEnListaDTO(CorAgente agente, List<AgenteDTO> corAgenteList) {

        return corAgenteList.stream()
                .map(AgenteDTO::getIdeAgente)
                .filter(Objects::nonNull)
                .anyMatch(ideAgente -> ideAgente.equals(agente.getIdeAgente()));
    }

    /**
     * @param comunicacionOficialDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */

    @Transactional
    public String actualizarComunicacion(ComunicacionOficialDTO comunicacionOficialDTO) throws SystemException {
        try {

            CorCorrespondencia correspondencia = em.find(CorCorrespondencia.class, comunicacionOficialDTO.getCorrespondencia().getIdeDocumento());

            if (!ObjectUtils.isEmpty(comunicacionOficialDTO) && !ObjectUtils.isEmpty(comunicacionOficialDTO.getCorrespondencia())) {
                correspondencia.setCodClaseEnvio(comunicacionOficialDTO.getCorrespondencia().getCodClaseEnvio());
                correspondencia.setCodModalidadEnvio(comunicacionOficialDTO.getCorrespondencia().getCodModalidadEnvio());
                correspondencia.setCodEstado(comunicacionOficialDTO.getCorrespondencia().getCodEstado());
                correspondencia.setDescripcion(comunicacionOficialDTO.getCorrespondencia().getDescripcion());
            }

            //------------------------------------------
            /*Set<CorAgente> agentes = correspondencia.getCorAgenteList();
             */
            List<AgenteDTO> agenteDTOList = comunicacionOficialDTO.getAgenteList();
            log.info("Lisado de agentes antes de posible modificacion ---------------------{}", agenteDTOList);
            /*List<CorAgente> agentesAEliminar = agentes.stream()
                    .filter(corAgente -> !this.verificarAgenteEnListaDTO(corAgente, agenteDTOList))
                    .peek(correspondencia::removeAsignacionByAgente)
                    .collect(Collectors.toList());

            agentes.removeAll(agentesAEliminar);

            log.info("Lisado de agentes despues de eliminacion agente " + agentes.toString());
            log.info("Lisado de agentes despues de eliminacion agente correspondencia" + agentes.toString());*/

            for (AgenteDTO agenteDTO : agenteDTOList) {

                if (agenteDTO.getIdeAgente() == null) { // si no existe
                    CorAgente agente;
                    agente = agenteControl.corAgenteTransform(agenteDTO);
                    // agenteControl.actualizarIdeFunciAgenteInterno(agente);
                    agente.setCodEstado(EstadoAgenteEnum.SIN_ASIGNAR.getCodigo());
                    correspondencia.getCorAgenteList().add(agente);
                    agente.getCorCorrespondenciaList().add(correspondencia);
                    //agentes.add(agente);
//                    agente.setEstadoDistribucion(reqDistFisica.equals(rDistFisica) ? EstadoDistribucionFisicaEnum.SIN_DISTRIBUIR.getCodigo() : null);
                    em.persist(agente);
                    agenteDTO.setIdeAgente(agente.getIdeAgente());
                    agenteDTO.setIdeFunci(agente.getIdeFunci());

                    //----------------------asignacion--------------
                    if (TipoAgenteEnum.DESTINATARIO.getCodigo().equals(agenteDTO.getCodTipAgent())) {
                        DctAsignacion dctAsignacion = DctAsignacion.newInstance()
                                //.corCorrespondencia(correspondencia)
                                .ideUsuarioCreo(new BigInteger(correspondencia.getCodFuncRadica()))
                                .codDependencia(agenteDTO.getCodDependencia())
                                .codTipAsignacion("CTA")
                                .fecAsignacion(new Date())
                                .corAgente(agente)
                                .build();
                        em.persist(dctAsignacion);

                        DctAsigUltimo dctAsigUltimo = DctAsigUltimo.newInstance()
                                .corAgente(agente)
                                //.corCorrespondencia(correspondencia)
                                .ideUsuarioCreo(new BigInteger(correspondencia.getCodFuncRadica()))
                                .ideUsuarioCambio(new BigInteger(correspondencia.getCodFuncRadica()))
                                .dctAsignacion(dctAsignacion)
                                .build();
                        em.persist(dctAsigUltimo);
                        //correspondencia.getDctAsigUltimoList().add(dctAsigUltimo);
                    }
                }  //------ si existe el agente en la correspondencia

                /*log.info("Lisado de agentes despues de posible modificacion" + agentes.toString());
                log.info("Lisado de agentes despues de posible modificacion desde correspondencia" + agentes.toString());*/


                if (TipoAgenteEnum.DESTINATARIO.getCodigo().equals(agenteDTO.getCodTipAgent())) {
                    log.error("Destinatario");
                    DestinatarioDTO destinatario = DestinatarioDTO.newInstance()
                            .agenteDestinatario(agenteDTO)
                            .ideFuncionarioCreaModifica(new BigInteger(comunicacionOficialDTO.getCorrespondencia().getCodFuncRadica()))
                            .build();
                    agenteControl.actualizarDestinatario(destinatario);
                } else {
                    log.error("Remitente");
                    RemitenteDTO remitente = RemitenteDTO.newInstance()
                            .ideFuncionarioCreaModifica(new BigInteger(comunicacionOficialDTO.getCorrespondencia().getCodFuncRadica()))
                            .agenteRemitente(agenteDTO)
                            .datosContactoList(agenteDTO.getDatosContactoList())
                            .build();
                    agenteControl.actualizarRemitente(remitente);
                }
                //-------------------------------------------------------
            }

            PpdDocumento ppdDocumento = em.find(PpdDocumento.class, comunicacionOficialDTO.getPpdDocumentoList().get(0).getIdePpdDocumento());
            if (!comunicacionOficialDTO.getPpdDocumentoList().get(0).getCodTipoDoc().equals(ppdDocumento.getCodTipoDoc())) {
                correspondencia.setFecVenGestion(calcularFechaVencimientoGestion(comunicacionOficialDTO.getCorrespondencia()));
                ppdDocumento.setCodTipoDoc(comunicacionOficialDTO.getPpdDocumentoList().get(0).getCodTipoDoc());
            }
            ppdDocumento.setAsunto(comunicacionOficialDTO.getPpdDocumentoList().get(0).getAsunto());

            if (comunicacionOficialDTO.getAnexoList() != null) {
                comunicacionOficialDTO.getAnexoList().forEach(anexoDTO -> {
                    if (em.find(CorAnexo.class, anexoDTO.getIdeAnexo()) == null) {
                        CorAnexo corAnexo = anexoControl.corAnexoTransform(anexoDTO);
                        corAnexo.setPpdDocumento(ppdDocumento);
                        ppdDocumento.getCorAnexoList().add(corAnexo);
                    }
                });
            }

            if (comunicacionOficialDTO.getReferidoList() != null) {
                log.info("Referidos ----------------------------------{}", comunicacionOficialDTO.getReferidoList());
                for (ReferidoDTO referido : comunicacionOficialDTO.getReferidoList()) {
                    CorReferido corReferido = new CorReferido();
                    if (!ObjectUtils.isEmpty(referido.getIdeReferido())) {
                        log.info("este es el id del referido ----------------------------------{}", referido.getIdeReferido());
                        CorReferido corReferido1 = em.find(CorReferido.class, referido.getIdeReferido());
                        if (ObjectUtils.isEmpty(corReferido1)) {
                            corReferido = referidoControl.corReferidoTransform(referido);
                            corReferido.setCorCorrespondencia(correspondencia);
                            correspondencia.addCorReferido(corReferido);
                            log.info("Referido a persistir ----------------------------------{}", corReferido);
                            em.persist(corReferido);
                        }
                    } else {
                        corReferido = referidoControl.corReferidoTransform(referido);
                        corReferido.setCorCorrespondencia(correspondencia);
                        correspondencia.addCorReferido(corReferido);
                        log.info("Referido a persistir ----------------------------------{}", corReferido);
                        em.persist(corReferido);
                    }
                }
            }

            log.info("correspondencia a persistir --------------------------------------");
            em.persist(correspondencia);

            log.info("Actualizacion exitosa de la comunicacion");
            return "1";
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    private ArrayList<Attachment> obtenerDocumentosECMporNroRadicado(String nroRadicado) throws SystemException {

        String endpoint = System.getProperty("ecm-api-endpoint");
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        ArrayList<Attachment> attachmentsList = new ArrayList<>();

        co.com.soaint.foundation.canonical.ecm.DocumentoDTO dto = co.com.soaint.foundation.canonical.ecm.DocumentoDTO.newInstance().nroRadicado(nroRadicado).build();
        Response response = wt.path("/obtenerDocumentosAdjuntosECM/")
                .request()
                .post(Entity.json(dto));

        if (response.getStatus() == HttpStatus.OK.value()) {
            MensajeRespuesta mensajeRespuesta = response.readEntity(MensajeRespuesta.class);
            if (mensajeRespuesta.getCodMensaje().equals("0000")) {
                final List<co.com.soaint.foundation.canonical.ecm.DocumentoDTO> documentoDTOList = mensajeRespuesta.getDocumentoDTOList();
                log.info("processing rest request - documentoDTOList.size(): " + documentoDTOList.size());

                if (!ObjectUtils.isEmpty(documentoDTOList)) {
                    documentoDTOList.forEach(documento -> {
                        Attachment doc = new Attachment();
                        doc.setAttachments(documento.getDocumento());
                        doc.setContentTypeattachment(documento.getTipoDocumento());
                        doc.setNameAttachments(documento.getNombreDocumento());
                        attachmentsList.add(doc);
                        log.info("processing rest request - documento.getTipoDocumento(): " + documento.getTipoDocumento());
                        log.info("processing rest request - documento.getNombreDocumento(): " + documento.getNombreDocumento());
                    });
                }
            } else {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.error consultando servicio de negocio obtenerDocumentosAdjuntosECM")
                        .buildSystemException();
            }

        }
        return attachmentsList;
    }

    /**
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public Boolean sendMail(final String nroRadicado) throws BusinessException, SystemException {
        log.info("processing rest request - dentro enviar correo radicar correspondencia salida");

        final HashMap<String, String> parameters = new HashMap<>();
        String radicado = nroRadicado;
        int index;
        if ((index = radicado.lastIndexOf(separator)) >= 0) {
            radicado = radicado.substring(index + separator.length());
        }
        parameters.put("#RADICADO#", StringUtils.isEmpty(radicado) ? "" : radicado);
        parameters.put("#FECHA#", new Date().toString());
        MailRequestDTO request = new MailRequestDTO("PA001");
        log.info("processing rest request - template: " + request.getTemplate());

        CorrespondenciaDTO correspondenciaDTO = this.consultarCorrespondenciaByNroRadicado(nroRadicado, false);

        log.info("processing rest request - ideDocumento correspondencia: " + correspondenciaDTO.getIdeDocumento().toString());

        String asunto = "Respuesta " + radicado + " " + correspondenciaDTO.getFecRadicado() + ".";
        request.setSubject(asunto);
        log.info("processing rest request - asunto: " + request.getSubject());

        //------------------- Inicio Attachments ------------------------------------------------------//
        ArrayList<Attachment> attachmentsList = obtenerDocumentosECMporNroRadicado(nroRadicado);
        //------------------- Fin Attachments ------------------------------------------------------//
        request.setAttachmentsList(attachmentsList);

        final List<AgenteDTO> destinatariosList = agenteControl.listarDestinatariosByIdeDocumentoMail(correspondenciaDTO.getIdeDocumento());
        log.info("Destinatarios list" + destinatariosList.toString());

        if (ObjectUtils.isEmpty(destinatariosList)) throw ExceptionBuilder.newBuilder()
                .withMessage("No existen destinatarios para enviar correo.")
                .buildSystemException();

        final List<String> destinatarios = new ArrayList<>();

        destinatariosList.forEach(agenteDTO -> {

            if ("INT".equalsIgnoreCase(agenteDTO.getCodTipoRemite())) {
                try {
                    FuncionarioDTO funcionario = funcionarioControl.consultarFuncionarioByIdeFunci(agenteDTO.getIdeFunci());
                    log.info("Funcionario correspondencia" + funcionario.getCorrElectronico() + " " + funcionario.getNomFuncionario());
                    if (agenteDTO.getIndOriginal() != null) {
                        if ("TP-DESP".equals(agenteDTO.getIndOriginal())) {
                            if ("TP-PERA".equals(agenteDTO.getCodTipoPers())) parameters.put("#USER#", "");
                            else {
                                String nombre = (funcionario.getNomFuncionario() == null) ? "" : funcionario.getNomFuncionario();
                                log.info("processing rest request - agente: " + agenteDTO.getCodTipoPers() + " " + agenteDTO.getIndOriginal());
                                parameters.put("#USER#", nombre);
                            }
                        }
                    }

                    log.info("processing rest request - funcionarioDTO.getNomFuncionario(): " + funcionario.getNomFuncionario());
                    destinatarios.add(funcionario.getCorrElectronico());
                } catch (Exception ex) {
                    log.error("Business Control - a system error has occurred", ex);
                }
            } else {
                try {
                    if (agenteDTO.getIndOriginal() != null) {
                        if ("TP-DESP".equalsIgnoreCase(agenteDTO.getIndOriginal())) {
                            if ("TP-PERA".equalsIgnoreCase(agenteDTO.getCodTipoPers())) {
                                parameters.put("#USER#", "");
                            } else {
                                String nombre = (agenteDTO.getNombre() == null) ? "" : agenteDTO.getNombre();
                                log.info("processing rest request - agente: " + agenteDTO.getCodTipoPers() + " " + agenteDTO.getIndOriginal());
                                parameters.put("#USER#", nombre);
                            }
                        }
                        log.info("processing rest request - agenteDTO.getNombre(): " + agenteDTO.getNombre());
                    }

                    List<DatosContactoDTO> datosContacto = datosContactoControl.consultarDatosContactoByAgentesCorreo(agenteDTO);
                    parameters.put("#USER#", StringUtils.isEmpty(agenteDTO.getNombre()) ? "" : agenteDTO.getNombre());
                    log.info("processing rest request - datosContactoDTOS.size(): " + datosContacto.size());
                    for (DatosContactoDTO contactoDTO : datosContacto) {
                        destinatarios.add(contactoDTO.getCorrElectronico());
                    }
                } catch (Exception ex) {
                    log.error("Business Control - a system error has occurred", ex);
                }
            }
        });

        List<BigInteger> idFuncionarios = new ArrayList<>();
        for (AgenteDTO destinatario : destinatariosList) {
            if ("INT".equalsIgnoreCase(destinatario.getCodTipoRemite())) {
                idFuncionarios.add(destinatario.getIdeAgente());
            }
        }
        for (BigInteger id : idFuncionarios) {
            String correoDestinatario = funcionarioControl.consultarEmailByIdeFunci(id);
            if (!StringUtils.isEmpty(correoDestinatario)) {
                destinatarios.add(correoDestinatario);
            }
        }


        if (!parameters.containsKey("#USER#"))
            parameters.put("#USER#", "");
        //  destinatarios.add("correo@certificado.4-72.com.co");
        log.info("processing rest request - destinatarios: " + destinatarios.toString());

        if (ObjectUtils.isEmpty(destinatarios)) throw ExceptionBuilder.newBuilder()
                .withMessage("No existen destinatarios para enviar correo.")
                .buildSystemException();

        request.setTo(destinatarios.toArray(new String[0]));
        log.info("processing rest request - enviar correo radicar correspondencia" + Arrays.toString(request.getTo()));
        String s = this.organigramaAdministrativoControl.consultarNombreElementoByCodOrg(correspondenciaDTO.getCodDependencia());
        parameters.put("#ORG#", StringUtils.isEmpty(s) ? "" : s);

        request.setParameters(parameters);

        log.info("processing rest request - enviar correo radicar correspondencia" + request.getParameters());
        try {
            log.info("processing rest request - enviar correo radicar correspondencia- preparando enviar...");
            MailServiceProxy instance = MailServiceProxy.getInstance();
            return instance.sendEmail(request);
        } catch (Exception e) {
            log.info("processing rest request - error enviar correo radicar correspondencia" + e.getMessage());
            throw new BusinessException("system.error.correo.enviado");
        }
    }


    //***********************************************************//

    public Boolean notoficarTarea(String nroRadicado, AgenteDTO destinatario, AgenteDTO remitente, String nombreTarea) throws BusinessException, SystemException, ParseException {
        log.info("processing rest request - dentro enviar correo notificar tarea");

        HashMap<String, String> parameters = new HashMap<>();
        MailRequestDTO request = new MailRequestDTO("PA002");
        log.info("processing rest request - template: " + request.getTemplate());

        String fechaParseada = "" + LocalDate.now().getYear() + "-" + LocalDate.now().getMonth().getValue() + "-" + LocalDate.now().getDayOfMonth() + " " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond();
        String asunto = "Nueva Asignación: " + nombreTarea + " " + fechaParseada + ".";
        request.setSubject(asunto);
        log.info("processing rest request - asunto: " + request.getSubject());

        final List<String> destinatariosList = new ArrayList<>();

        log.info("processing rest request - : " + request.getSubject());
        FuncionarioDTO funcionario = funcionarioControl.consultarFuncionarioByIdeFunci(destinatario.getIdeFunci());
        String nombreDestinatario = StringUtils.isEmpty(funcionario.getNomFuncionario()) ? "" : funcionario.getNomFuncionario();
        log.info("############# destinatario #################" + funcionario);
        FuncionarioDTO funcionarioRem = funcionarioControl.consultarFuncionarioByIdeFunci(remitente.getIdeFunci());
        String nombreRemitente = StringUtils.isEmpty(funcionarioRem.getNomFuncionario()) ? "" : funcionarioRem.getNomFuncionario();
        log.info("$$$$$$$$$$$$$ remitente $$$$$$$$$$$$$" + funcionarioRem);
        String apellido1 = StringUtils.isEmpty(funcionarioRem.getValApellido1()) ? "" : funcionarioRem.getValApellido1();
        String apellido2 = StringUtils.isEmpty(funcionarioRem.getValApellido2()) ? "" : funcionarioRem.getValApellido2();
        parameters.put("#USER#", nombreDestinatario);
        destinatariosList.add(funcionario.getCorrElectronico());

        if (ObjectUtils.isEmpty(destinatariosList)) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("No existen destinatarios para enviar correo.")
                    .buildSystemException();
        }

        if (!parameters.containsKey("#USER#")) {
            parameters.put("#USER#", "");
        }

        log.info("processing rest request - destinatarios: " + destinatariosList.toString());
        request.setTo(destinatariosList.toArray(new String[0]));
        log.info("processing rest request - enviar correo notificar tarea");

      /*  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = new Date();
        Date fechaInicial = dateFormat.parse(fecha.toString());*/

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        log.info("*************** fecha parseada {}******************", fechaParseada);
        String dateSring = format.format(fecha);
        Date fechaInicial = format.parse(dateSring);
        log.info("fecha inicial " + fechaInicial);
        String nombrCompleto = nombreRemitente.concat(" ").concat(apellido1).concat(" ").concat(apellido2);
        parameters.put("#FECHA#", fechaParseada);
        parameters.put("#REMITENTE#", nombrCompleto);
        if (!StringUtils.isEmpty(nroRadicado) && nroRadicado.contains("--")) {
            String[] radicado = nroRadicado.split("--");
            nroRadicado = radicado[1];
        }
        log.info("hasta aqui llega");
        nroRadicado = StringUtils.isEmpty(nroRadicado) ? "sin número" : nroRadicado;
        log.info(" numero de radicado..." + nroRadicado);
        parameters.put("#RADICADO#", nroRadicado);

        request.setParameters(parameters);

        log.info("processing rest request - %%%%%%%% request &&&&&&&&&" + request.getParameters());
        try {
            log.info(" enviar correo notificar tarea- preparando enviar...");
            MailServiceProxy instance = MailServiceProxy.getInstance();
            return instance.sendEmail(request);
        } catch (Exception e) {
            log.info("processing rest request - error enviar correo radicar correspondencia" + e.getMessage());
            throw new BusinessException("system.error.correo.enviado");
        }
    }

    /**
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    public String consultarNroRadicadoCorrespondenciaReferida(String nroRadicado) throws SystemException {
        return referidoControl.consultarNroRadicadoCorrespondenciaReferida(nroRadicado);
    }
}
