package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsOrganigramaAdministrativo;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.ModeloConsultaDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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
public class ConsultaDocumentosControl {

    // [fields] -----------------------------------

    @PersistenceContext
    private EntityManager em;

    @Autowired
    CorrespondenciaControl correspondenciaControl;

    @Autowired
    FuncionariosControl funcionariosControl;


    @Transactional
    public List<ModeloConsultaDocumentoDTO> consultarCorrespondencias(String userLogin, Date startDate, Date endDate, String tipoComunicacion, String nroRadicado, String nroIdentificacion, String depOrigen, String depDestino,
                                                                      String nroGuia, String asunto, String solicitante, String destinatario, String tipoDocumento, String tipologiaDocumental) throws SystemException {

        try {
            log.info("--------------------- consultar documentos Start ------------------------");
            log.info("user logged \t----- \t{}", userLogin);
            log.info("Fecha Inicial \t----- \t{}", startDate);
            log.info("Fecha Final \t----- \t{}", endDate);
            log.info("tipo de comunicacion  \t----- \t{}", tipoComunicacion);
            log.info("numero de radicado  \t----- \t{}", nroRadicado);
            log.info("numero de identificación \t----- \t{}", nroIdentificacion);
            log.info("dependencia de origen \t----- \t{}", depOrigen);
            log.info("dependencia de destino \t----- \t{}", depDestino);
            log.info("numero guia  \t----- \t{}", nroGuia);
            log.info("asunto \t----- \t{}", asunto);
            log.info("Solicitante  \t----- \t{}", solicitante);
            log.info("Destinatario  \t----- \t{}", destinatario);
            log.info("Tipo Documento  \t----- \t{}", tipoDocumento);
            log.info("Tipologia Documental \t----- \t{}", tipologiaDocumental);
            log.info("--------------------- consultar documentos End ------------------------");

            final LocalDateTime startDateTime = toLocalDateTime(startDate);
            final LocalDateTime endDateTime = toLocalDateTime(endDate);

            log.info("startDateTime: " + startDate);
            log.info("endDateTime: " + endDateTime);

            final List<String> depCodes = new ArrayList<>();
            if (!StringUtils.isEmpty(userLogin)) {
                final FuncionarioDTO funcionarioDTO = funcionariosControl.listarFuncionarioByLoginName(userLogin);
                if (null != funcionarioDTO) {
                    depCodes.addAll(funcionarioDTO.getDependencias()
                            .parallelStream()
                            .map(DependenciaDTO::getCodDependencia)
                            .collect(Collectors.toList()));
                }
            }

            log.info("Dep Codes: {}", depCodes);

            final List<ModeloConsultaDocumentoDTO> resultList = em.createNamedQuery("CorCorrespondencia.consultaDocumentos", ModeloConsultaDocumentoDTO.class)
                    /*.setParameter("START_DATE", startDate)
                    .setParameter("END_DATE", endDate)*/
                    .setParameter("TIPO_COM", (tipoComunicacion == null || "".equals(tipoComunicacion.trim())) ? null : tipoComunicacion)
                    .setParameter("NRO_RADICADO", (nroRadicado == null || "".equals(nroRadicado.trim())) ? null : "%" + nroRadicado + "%")
                    .setParameter("NRO_IDENT", (nroIdentificacion == null || "".equals(nroIdentificacion.trim())) ? null : "%" + nroIdentificacion + "%")
                    .setParameter("DEP_ORIGEN", (depOrigen == null || "".equals(depOrigen.trim())) ? null : depOrigen)
                    .setParameter("DEP_DEST", (depDestino == null || "".equals(depDestino.trim())) ? null : depDestino)
                    .setParameter("NRO_GUIA", (nroGuia == null || "".equals(nroGuia.trim())) ? null : nroGuia)
                    .setParameter("SOLICITANTE", (solicitante == null || "".equals(solicitante.trim())) ? null : "%" + solicitante.toLowerCase() + "%")
                    .setParameter("ASUNTO", (asunto == null || "".equals(asunto.trim())) ? null : "%" + asunto.trim().toLowerCase() + "%")
                    .setParameter("TIPO_DOC", (tipoDocumento == null || "".equals(tipoDocumento.trim())) ? null : tipoDocumento)
                    .setParameter("TIPOLOGIA", (tipologiaDocumental == null || "".equals(tipologiaDocumental.trim())) ? null : tipologiaDocumental)
                    .getResultList().parallelStream()
                    .filter(dto -> depCodes.isEmpty() || depCodes.parallelStream().anyMatch(s -> StringUtils.equalsIgnoreCase(s, dto.getCodigoDependencia())))
                    .filter(dto -> {
                        final LocalDateTime dateTimeDb = toLocalDateTime(dto.getFechaRadicacion());
                        log.info("startDateTime: {}, endDateTime: {}, dateTimeDb: {}", startDateTime, endDateTime, dateTimeDb);
                        if (null != dateTimeDb) {
                            if (null != startDateTime && null != endDateTime) {
                                return (dateTimeDb.toLocalDate().isEqual(startDateTime.toLocalDate()) || dateTimeDb.toLocalDate().isAfter(startDateTime.toLocalDate()))
                                        && (dateTimeDb.toLocalDate().isEqual(endDateTime.toLocalDate()) || dateTimeDb.toLocalDate().isBefore(endDateTime.toLocalDate()));
                            }
                            if (null != startDateTime) {
                                return dateTimeDb.toLocalDate().isEqual(startDateTime.toLocalDate()) || dateTimeDb.toLocalDate().isAfter(startDateTime.toLocalDate());
                            }
                            if (null != endDateTime) {
                                return dateTimeDb.toLocalDate().isEqual(endDateTime.toLocalDate()) || dateTimeDb.toLocalDate().isBefore(endDateTime.toLocalDate());
                            }
                        }
                        return true;
                    }).collect(Collectors.toList());

            if (!ObjectUtils.isEmpty(resultList)) {
                final List<ModeloConsultaDocumentoDTO> dtoList = completarAgenteDestinatario(resultList);
                if (null != destinatario && !"".equals(destinatario.trim())) {
                    return dtoList.parallelStream()
                            .filter(dto -> StringUtils.containsIgnoreCase(dto.getDestinatario(), destinatario.trim()))
                            .collect(Collectors.toList());
                }
                return dtoList;
            }
            return new ArrayList<>();

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    private List<ModeloConsultaDocumentoDTO> completarAgenteDestinatario(List<ModeloConsultaDocumentoDTO> documentos) {

        log.info("-----------request metodo completarAgenteDestinatario {}, {}", documentos);
        //  if ("EE".equals(tipoCom)) {
        for (ModeloConsultaDocumentoDTO documento : documentos) {
            if ("EE".equals(documento.getTipoCMC())) {
                List<AgenteDTO> remitente = em.createNamedQuery("CorAgente.findAgentByIdeDocumentoAndTipoRemitente", AgenteDTO.class)
                        .setParameter("IDE_DOCUMENTO", documento.getIdDocDb())
                        .setParameter("TIPO_REM", "EXT")
                        .getResultList();
                if (!ObjectUtils.isEmpty(remitente)) {
                    documento.setNombreRemitente(remitente.get(0).getNombre());
                }
                List<AgenteDTO> destinatarios = em.createNamedQuery("CorAgente.findAgentByIdeDocumentoAndTipoRemitente", AgenteDTO.class)
                        .setParameter("IDE_DOCUMENTO", documento.getIdDocDb())
                        .setParameter("TIPO_REM", "INT")
                        .getResultList();
                if (!ObjectUtils.isEmpty(destinatarios)) {
                    final StringBuilder destinatariosFull = new StringBuilder();
                    boolean hasMoreElements = false;
                    for (AgenteDTO agenteDTO : destinatarios) {
                        String depCode = getDepCodeAndAbbr(agenteDTO.getCodDependencia());
                        destinatariosFull.append(hasMoreElements ? " / " : "").append(depCode);
                        hasMoreElements = true;
                    }
                    documento.setDestinatario(destinatariosFull.toString());
                }
            }
            if ("SE".equals(documento.getTipoCMC())) {
                List<AgenteDTO> remitente = em.createNamedQuery("CorAgente.findAgentByIdeDocumentoAndTipoRemitente", AgenteDTO.class)
                        .setParameter("IDE_DOCUMENTO", documento.getIdDocDb())
                        .setParameter("TIPO_REM", "INT")
                        .getResultList();
                if (!ObjectUtils.isEmpty(remitente)) {
                    documento.setNombreRemitente(getDepCodeAndAbbr(remitente.get(0).getCodDependencia()));
                }
                List<AgenteDTO> destinatarios = em.createNamedQuery("CorAgente.findAgentByIdeDocumentoAndTipoRemitente", AgenteDTO.class)
                        .setParameter("IDE_DOCUMENTO", documento.getIdDocDb())
                        .setParameter("TIPO_REM", "EXT")
                        .getResultList();
                if (!ObjectUtils.isEmpty(destinatarios)) {
                    StringBuilder destinatariosFull = new StringBuilder();
                    boolean hasMoreElements = false;
                    for (AgenteDTO agente : destinatarios) {
                        destinatariosFull.append(hasMoreElements ? " / " : "").append(agente.getNombre());
                        hasMoreElements = true;
                    }
                    documento.setDestinatario(destinatariosFull.toString());
                }
            }
            if ("SI".equals(documento.getTipoCMC())) {
                log.info("TCM = SI");
                List<AgenteDTO> remitente = em.createNamedQuery("CorAgente.findAgentByIdeDocumentoAndTipoAgente", AgenteDTO.class)
                        .setParameter("IDE_DOCUMENTO", documento.getIdDocDb())
                        .setParameter("TIPO_AGEN", "TP-AGEE")
                        .getResultList();
                log.info("ID_DOC: {}, Response Rem List: {}", documento.getIdDocDb(), remitente.size());
                if (!ObjectUtils.isEmpty(remitente)) {
                    documento.setNombreRemitente(getDepCodeAndAbbr(remitente.get(0).getCodDependencia()));
                }
                List<AgenteDTO> destinatarios = em.createNamedQuery("CorAgente.findAgentByIdeDocumentoAndTipoAgente", AgenteDTO.class)
                        .setParameter("IDE_DOCUMENTO", documento.getIdDocDb())
                        .setParameter("TIPO_AGEN", "TP-AGEI")
                        .getResultList();
                log.info("ID_DOC: {}, Response Dest List: {}", documento.getIdDocDb(), destinatarios.size());
                if (!ObjectUtils.isEmpty(destinatarios)) {
                    StringBuilder destinatariosFull = new StringBuilder();
                    boolean hasMoreElements = false;
                    for (AgenteDTO agente : destinatarios) {
                        destinatariosFull
                                .append(hasMoreElements ? " / " : "")
                                .append(getDepCodeAndAbbr(agente.getCodDependencia()));
                        hasMoreElements = true;
                    }
                    documento.setDestinatario(destinatariosFull.toString());
                }
            }
        }
    /* //   }
      //  if ("SE".equals(tipoCom)) {
            for (ModeloConsultaDocumentoDTO documento : documentos) {
                List<AgenteDTO> remitente = em.createNamedQuery("CorAgente.findAgentByIdeDocumentoAndTipoRemitente", AgenteDTO.class)
                        .setParameter("IDE_DOCUMENTO", documento.getIdDocumento())
                        .setParameter("TIPO_REM", "INT")
                        .getResultList();
                if (!ObjectUtils.isEmpty(remitente)) {
                    documento.setRemitente(remitente.get(0).getCodDependencia());
                }
                List<AgenteDTO> destinatarios = em.createNamedQuery("CorAgente.findAgentByIdeDocumentoAndTipoRemitente", AgenteDTO.class)
                        .setParameter("IDE_DOCUMENTO", documento.getIdDocumento())
                        .setParameter("TIPO_REM", "EXT")
                        .getResultList();
                if (!ObjectUtils.isEmpty(destinatarios)) {
                    StringBuilder destinatariosFull = new StringBuilder();
                    for (AgenteDTO agente : destinatarios) {
                        destinatariosFull.append(agente.getNombre()).append(", ");
                    }
                    documento.setDestinatario(destinatariosFull.toString());
                }
            }
       // }
       // if ("SI".equals(tipoCom)) {
            for (ModeloConsultaDocumentoDTO documento : documentos) {
                List<AgenteDTO> remitente = em.createNamedQuery("CorAgente.findAgentByIdeDocumentoAndTipoAgente", AgenteDTO.class)
                        .setParameter("IDE_DOCUMENTO", documento.getIdDocumento())
                        .setParameter("TIPO_AGEN", "TP-AGEE")
                        .getResultList();
                if (!ObjectUtils.isEmpty(remitente)) {
                    documento.setRemitente(remitente.get(0).getCodDependencia());
                }
                List<AgenteDTO> destinatarios = em.createNamedQuery("CorAgente.findAgentByIdeDocumentoAndTipoAgente", AgenteDTO.class)
                        .setParameter("IDE_DOCUMENTO", documento.getIdDocumento())
                        .setParameter("TIPO_AGEN", "TP-AGEE")
                        .getResultList();
                if (!ObjectUtils.isEmpty(destinatarios)) {
                    StringBuilder destinatariosFull = new StringBuilder();
                    for (AgenteDTO agente : destinatarios) {
                        destinatariosFull.append(agente.getCodDependencia()).append(", ");
                    }
                    documento.setDestinatario(destinatariosFull.toString());
                }
            }
      //  }*/
        log.info("Estos son los documentos---------------------------------------------{}", documentos);
        return documentos.stream().peek(modeloConsultaDTO -> {
            String estado = modeloConsultaDTO.getEstado();
            EstadoAgenteEnum agenteEnum = EstadoAgenteEnum.getEstadoAgenteBy(estado);
            if (agenteEnum != null) {
                modeloConsultaDTO.setEstado(agenteEnum.getNombre());
            }
        }).collect(Collectors.toList());
    }

    private LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        final Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        final TimeZone tz = calendar.getTimeZone();
        final ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }

    private String getDepCodeAndAbbr(String depCode) {
        if (!StringUtils.isEmpty(depCode)) {
            final List<TvsOrganigramaAdministrativo> resultList = em
                    .createNamedQuery("TvsOrganigramaAdministrativo.consultarOrganigramaCodigo", TvsOrganigramaAdministrativo.class)
                    .setParameter("COD_ORG", depCode)
                    .getResultList();
            if (!resultList.isEmpty()) {
                final TvsOrganigramaAdministrativo organigramaAdministrativo = resultList.get(0);
                if (!StringUtils.isEmpty(organigramaAdministrativo.getAbreviatura())) {
                    depCode += "-" + organigramaAdministrativo.getAbreviatura();
                }
            }
        }
        return depCode;
    }
}
