package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.correspondencia.domain.entity.TvsDatosContacto;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.*;
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
import org.springframework.util.StringUtils;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
public class AgenteControl extends GenericControl<CorAgente> {

    private static final Long serialVersionUID = 7878L;

    @Autowired
    AsignacionControl asignacionControl;

    @Autowired
    CorrespondenciaControl correspondenciaControl;

    @Autowired
    PpdTrazDocumentoControl ppdTrazDocumentoControl;

    @Autowired
    DatosContactoControl datosContactoControl;

    @Autowired
    ConstantesControl constanteControl;

    @Autowired
    private OrganigramaAdministrativoControl organigramaAdministrativoControl;

    @Autowired
    private FuncionariosControl funcionariosControl;

    @Value("${radicado.max.num.redirecciones}")
    private int numMaxRedirecciones;

    @Value("${radicado.req.dist.fisica}")
    private String reqDistFisica;

    public AgenteControl() {
        super(CorAgente.class);
    }

    /**
     * @param ideDocumento
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteDTO> listarRemitentesByIdeDocumento(BigInteger ideDocumento) throws SystemException {
        try {
            log.info("idDocumento request listarRemitentesByIdeDocumento --------------------------------------{}",ideDocumento);
            if (ObjectUtils.isEmpty(ideDocumento)) {
                return new ArrayList<>();
            }
                List<AgenteDTO> resultList = em.createNamedQuery("CorAgente.findRemitentesByIdeDocumentoAndCodTipoAgente", AgenteDTO.class)
                        .setParameter("COD_TIP_AGENT", TipoAgenteEnum.REMITENTE.getCodigo())
                        .setParameter("IDE_DOCUMENTO", ideDocumento)
                        .getResultList();
            log.info("response listarRemitentesByIdeDocumento --------------------------------------{}",resultList);
            if (ObjectUtils.isEmpty(resultList)) {
                return new ArrayList<>();
            }

            return resultList;


        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error -- " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AgenteDTO listarAgentePrincipalByIdeDocumento(BigInteger ideDocumento) throws SystemException {
        try {
            log.info("idDocumento request listarRemitentesByIdeDocumento --------------------------------------{}",ideDocumento);
            if (StringUtils.isEmpty(ideDocumento)) {
                return AgenteDTO.newInstance().build();
            }
            List<AgenteDTO> resultList = em.createNamedQuery("CorAgente.findAgentePrincipalByIdeDocumento", AgenteDTO.class)
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
                    .getResultList();
            log.info("response listarRemitentesByIdeDocumento --------------------------------------{}",resultList);
            if (ObjectUtils.isEmpty(resultList)) {
                return AgenteDTO.newInstance().build();
            }

            return resultList.get(0);


        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error -- " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param ideDocumento
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteFullDTO> listarRemitentesFullByIdeDocumento(BigInteger ideDocumento) throws SystemException {
        try {
            List<AgenteDTO> agenteDTOS = listarRemitentesByIdeDocumento(ideDocumento);
            return agenteListTransformToFull(agenteDTOS);

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
     * @param codDependencia
     * @param codEstado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteDTO> listarDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado(BigInteger ideDocumento,
                                                                                          String codDependencia,
                                                                                          String codEstado) throws SystemException {
        log.info("####################### idDocumento:" + ideDocumento);
        log.info("####################### codDependencia:" + codDependencia);
        log.info("####################### codEstado:" + codEstado);
        try {

            List<AgenteDTO> agentes = em.createNamedQuery("CorAgente.findDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado", AgenteDTO.class)
                    .setParameter("COD_ESTADO", codEstado)
                    .setParameter("COD_DEPENDENCIA", StringUtils.isEmpty(codDependencia) ? "%%" : "%" + codDependencia + "%")
                    .setParameter("COD_TIP_REM", TipoRemitenteEnum.INTERNO.getCodigo())
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
                    .getResultList();

            log.info("$$$$$$$$$$$$$$$$$ gentes:" + agentes);
            return agentes;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteDTO> listarDestinatariosByIdeDocumentoAndCodDependencia(BigInteger ideDocumento,
                                                                              String codDependencia) throws SystemException {
        try {
            log.info("request idDocumento listarDestinatariosByIdeDocumentoAndCodDependencia ------------------{}", ideDocumento);
            log.info("request codDependencia listarDestinatariosByIdeDocumentoAndCodDependencia ------------------{}", codDependencia);
            List<AgenteDTO> resultList = em.createNamedQuery("CorAgente.findDestinatariosByIdeDocumentoAndCodDependencia", AgenteDTO.class)
                    .setParameter("COD_DEPENDENCIA", codDependencia)
                    .setParameter("COD_TIP_AGENT", TipoAgenteEnum.DESTINATARIO.getCodigo())
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
                    .getResultList();
            log.info("destinatarios -----------------------------------  {}", resultList);
            if(!ObjectUtils.isEmpty(resultList)){
                return resultList;
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

    /**
     * @param ideDocumento
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteDTO> listarDestinatariosByIdeDocumento(BigInteger ideDocumento) throws SystemException {
        try {
            return em.createNamedQuery("CorAgente.findDestinatariosByIdeDocumentoAndCodTipoAgente", AgenteDTO.class)
                    .setParameter("COD_TIP_AGENT", TipoAgenteEnum.DESTINATARIO.getCodigo())
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
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
     * @param ideDocumento
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteDTO> listarDestinatariosByIdeDocumentoMail(BigInteger ideDocumento) throws SystemException {
        try {
            return em.createNamedQuery("CorAgente.findDestinatariosByIdeDocumentoAndCodTipoAgenteMail", AgenteDTO.class)
                    .setParameter("COD_TIP_AGENT", TipoAgenteEnum.DESTINATARIO.getCodigo())
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error -- " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public List<AgenteDTO> listarAgentesPorIdDocumento(BigInteger ideDocumento) throws SystemException {
        try {
            return em.createNamedQuery("CorAgente.listarAgentesPorIdDocumento", AgenteDTO.class)
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error -- " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    public AgenteDTO ObtenerAgenteTPAGEIPorIdDocumento(BigInteger ideDocumento) throws SystemException {
        try {
            List<AgenteDTO> agentes = em.createNamedQuery("CorAgente.ObtenerAgenteTPAGEIPorIdDocumento", AgenteDTO.class)
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
                    .getResultList();
            if(ObjectUtils.isEmpty(agentes)){
                return new AgenteDTO();
            }
            return agentes.get(0);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error -- " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param ideAgente
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AgenteDTO consultarAgenteByIdeAgente(BigInteger ideAgente) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("CorAgente.findByIdeAgente", AgenteDTO.class)
                    .setParameter("IDE_AGENTE", ideAgente)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("agente.agente_not_exist_by_ideAgente")
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


    public AgenteDTO consultarAgenteByNroOrNit(String nroIdentificacion, String tipoPersona) throws SystemException {
        try {
            if ("TP-PERPN".equalsIgnoreCase(tipoPersona) && null != nroIdentificacion && !"".equals(nroIdentificacion.trim())) {
                final List<AgenteDTO> resultList = em.createNamedQuery("CorAgente.findByNroIdentificacionAndTipPers", AgenteDTO.class)
                        .setParameter("NRO_IDENTIDAD", nroIdentificacion.trim())
                        .getResultList();
                return resultList.isEmpty() ? AgenteDTO.newInstance().build() : resultList.get(0);
            }
            if ("TP-PERPJ".equalsIgnoreCase(tipoPersona) && null != nroIdentificacion && !"".equals(nroIdentificacion.trim())) {
                final List<AgenteDTO> resultList = em.createNamedQuery("CorAgente.findByNITAndTipPers", AgenteDTO.class)
                        .setParameter("NIT", nroIdentificacion.trim())
                        .getResultList();
                return resultList.isEmpty() ? AgenteDTO.newInstance().build() : resultList.get(0);
            }
            return AgenteDTO.newInstance().build();

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error --> " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    /**
     * @param agenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarEstadoAgente(AgenteDTO agenteDTO) throws BusinessException, SystemException {
        try {
            if (!verificarByIdeAgente(agenteDTO.getIdeAgente())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("agente.agente_not_exist_by_ideAgente")
                        .buildBusinessException();
            }

            log.info("@@@@@@@@ esto es lo que llega {}, {}", agenteDTO.getCodEstado(), agenteDTO.getIdeAgente());
            em.createNamedQuery("CorAgente.updateEstado")
                    .setParameter("COD_ESTADO", agenteDTO.getCodEstado())
                    .setParameter("IDE_AGENTE", agenteDTO.getIdeAgente())
                    .executeUpdate();


            if (agenteDTO.getCodEstado().equals(EstadoAgenteEnum.SIN_ASIGNAR.getCodigo())) {
                CorrespondenciaDTO correspondencia = correspondenciaControl.consultarCorrespondenciaByIdeAgente(agenteDTO.getIdeAgente());
                correspondencia.setCodEstado(EstadoCorrespondenciaEnum.SIN_ASIGNAR.getCodigo());
                correspondenciaControl.actualizarEstadoCorrespondencia(correspondencia);
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
     * @param redireccion
     * @throws SystemException
     */
    public void redireccionarCorrespondencia(RedireccionDTO redireccion) throws SystemException {
        try {
            for (AgenteDTO agente : redireccion.getAgentes()) {
                CorrespondenciaDTO correspondencia = correspondenciaControl.consultarCorrespondenciaByIdeAgente(agente.getIdeAgente());
                String estadoDistribucionFisica = reqDistFisica.equals(correspondencia.getReqDistFisica()) ? EstadoDistribucionFisicaEnum.SIN_DISTRIBUIR.getCodigo() : null;

                CorAgente corAgente = em.find(CorAgente.class, agente.getIdeAgente());
                corAgente.setCodDependencia(agente.getCodDependencia());
                corAgente.setCodSede(agente.getCodSede());
                corAgente.setCodEstado("SA");
                corAgente.setEstadoDistribucion(estadoDistribucionFisica);
                em.merge(corAgente);
                /*
                em.createNamedQuery("CorAgente.redireccionarCorrespondencia")
                        .setParameter("COD_SEDE", agente.getCodSede())
                        .setParameter("COD_DEPENDENCIA", agente.getCodDependencia())
                        .setParameter("IDE_AGENTE", agente.getIdeAgente())
                        .setParameter("COD_ESTADO", EstadoAgenteEnum.SIN_ASIGNAR.getCodigo())
                        .setParameter("ESTADO_DISTRIBUCION", estadoDistribucionFisica)
                        .executeUpdate();*/

                //-----------------Asignacion--------------------------

                asignacionControl.actualizarAsignacion(agente.getIdeAgente(),
                        agente.getCodDependencia(), redireccion.getTraza().getIdeFunci());

                //-----------------------------------------------------

                correspondencia.setCodEstado(EstadoCorrespondenciaEnum.SIN_ASIGNAR.getCodigo());
                correspondenciaControl.actualizarEstadoCorrespondencia(correspondencia);

                ppdTrazDocumentoControl.generarTrazaDocumento(PpdTrazDocumentoDTO.newInstance()
                        .observacion(redireccion.getTraza().getObservacion())
                        .ideFunci(redireccion.getTraza().getIdeFunci())
                        .codEstado(redireccion.getTraza().getCodEstado())
                        .ideDocumento(correspondencia.getIdeDocumento())
                        .codOrgaAdmin(redireccion.getTraza().getCodOrgaAdmin())
                        .build());

            }
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param devolucion
     * @throws SystemException
     */
    public void devolverCorrespondencia(DevolucionDTO devolucion) throws SystemException {
        try {
            for (ItemDevolucionDTO itemDevolucion : devolucion.getItemsDevolucion()) {
                CorrespondenciaDTO correspondencia = correspondenciaControl.consultarCorrespondenciaByIdeAgente(itemDevolucion.getAgente().getIdeAgente());
                actualizarEstadoAgente(AgenteDTO.newInstance()
                        .ideAgente(itemDevolucion.getAgente().getIdeAgente())
                        .codEstado(EstadoAgenteEnum.DEVUELTO.getCodigo())
                        .build());
                log.info("######## estado devuelto: {}, idagente: {}", EstadoAgenteEnum.DEVUELTO.getCodigo(), itemDevolucion.getAgente().getIdeAgente());

                if(!ObjectUtils.isEmpty(correspondencia) && !ObjectUtils.isEmpty(correspondencia.getIdeDocumento())){
                    CorCorrespondencia correspondenciaObj = em.find(CorCorrespondencia.class, correspondencia.getIdeDocumento());
                    correspondenciaObj.setCodEstado("DV");
                    em.merge(correspondenciaObj);
                }
                //-----------------Asignacion--------------------------

                ppdTrazDocumentoControl.generarTrazaDocumento(devolucion.getTraza());
                asignacionControl.actualizarAsignacionFromDevolucion(itemDevolucion.getAgente().getIdeAgente(),
                        correspondencia.getIdeDocumento(), devolucion.getTraza().getIdeFunci(), itemDevolucion.getCausalDevolucion().toString(),
                        devolucion.getTraza().getObservacion());

                //-----------------------------------------------------
            }
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
    public Boolean verificarByIdeAgente(BigInteger ideAgente) {
        long cantidad = em.createNamedQuery("CorAgente.countByIdeAgente", Long.class)
                .setParameter("IDE_AGENTE", ideAgente)
                .getSingleResult();
        return cantidad > 0;
    }

    /**
     * @param corAgente
     * @param datosContactoDTOList
     */
    void asignarDatosContacto(CorAgente corAgente, List<DatosContactoDTO> datosContactoDTOList) throws SystemException {
        corAgente.setTvsDatosContactoList(new ArrayList<>());
        for (DatosContactoDTO datosContactoDTO : datosContactoDTOList) {
            corAgente.getTvsDatosContactoList().add(datosContactoControl.transform(datosContactoDTO));
        }
    }

    /**
     * @param idDocumento
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteDTO> consltarAgentesByCorrespondencia(BigInteger idDocumento) throws SystemException {
        List<AgenteDTO> agenteDTOList = new ArrayList<>(listarRemitentesByIdeDocumento(idDocumento));
        agenteDTOList.addAll(listarDestinatariosByIdeDocumento(idDocumento));
        return agenteDTOList;
    }

    /**
     * @param agenteDTO
     * @return
     */
    AgenteFullDTO agenteTransformToFull(AgenteDTO agenteDTO) throws SystemException {
        try {
            if (!ObjectUtils.isEmpty(agenteDTO)) {
                final EstadoAgenteEnum agenteEnum = EstadoAgenteEnum.getEstadoAgenteBy(agenteDTO.getCodEstado());
                final TipoAgenteEnum tipoAgenteEnum = TipoAgenteEnum.getTipoAgenteBy(agenteDTO.getCodTipoRemite());
                final TipoRemitenteEnum remitenteEnum = TipoRemitenteEnum.getTipoRemitenteBy(agenteDTO.getCodTipoRemite());
                return AgenteFullDTO.newInstance()
                        .codCortesia(agenteDTO.getCodCortesia())
                        .descCortesia(constanteControl.consultarNombreConstanteByCodigo(agenteDTO.getCodCortesia()))
                        .codDependencia(agenteDTO.getCodDependencia())
                        .descDependencia(organigramaAdministrativoControl.consultarNombreElementoByCodOrg(agenteDTO.getCodDependencia()))
                        .codEnCalidad(agenteDTO.getCodEnCalidad())
                        .descEnCalidad(constanteControl.consultarNombreConstanteByCodigo(agenteDTO.getCodEnCalidad()))
                        .codEstado(agenteDTO.getCodEstado())
                        .descEstado(null == agenteEnum ? null : agenteEnum.getNombre())
                        .codSede(agenteDTO.getCodSede())
                        .descSede(organigramaAdministrativoControl.consultarNombreElementoByCodOrg(agenteDTO.getCodSede()))
                        .codTipAgent(agenteDTO.getCodTipAgent())
                        .descTipAgent(null == tipoAgenteEnum ? null : tipoAgenteEnum.getNombre())
                        .codTipDocIdent(agenteDTO.getCodTipDocIdent())
                        .descTipDocIdent(constanteControl.consultarNombreConstanteByCodigo(agenteDTO.getCodTipDocIdent()))
                        .codTipoPers(agenteDTO.getCodTipoPers())
                        .descTipoPers(constanteControl.consultarNombreConstanteByCodigo(agenteDTO.getCodTipoPers()))
                        .codTipoRemite(agenteDTO.getCodTipoRemite())
                        .descTipoRemite(null == remitenteEnum ? null : remitenteEnum.getNombre())
                        .fecAsignacion(agenteDTO.getFecAsignacion())
                        .ideAgente(agenteDTO.getIdeAgente())
                        .indOriginal(agenteDTO.getIndOriginal())
                        .nit(agenteDTO.getNit())
                        .nombre(agenteDTO.getNombre())
                        .nroDocuIdentidad(agenteDTO.getNroDocuIdentidad())
                        .numDevoluciones(agenteDTO.getNumDevoluciones())
                        .numRedirecciones(agenteDTO.getNumRedirecciones())
                        .razonSocial(agenteDTO.getRazonSocial())
                        .datosContactoList(datosContactoControl.datosContactoListTransformToFull(agenteDTO.getDatosContactoList()))
                        .build();
            }
            return null;
        } catch (Exception e) {
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param agenteList
     * @return
     */
    List<AgenteFullDTO> agenteListTransformToFull(List<AgenteDTO> agenteList) throws SystemException {
        try {
            List<AgenteFullDTO> agenteFullDTOList = new ArrayList<>();
            for (AgenteDTO agenteDTO : agenteList) {
                agenteFullDTOList.add(agenteTransformToFull(agenteDTO));
            }

            return agenteFullDTOList;

        } catch (Exception e) {
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param idDocumento
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteFullDTO> consultarAgentesFullByCorrespondencia(BigInteger idDocumento) throws SystemException {

        List<AgenteDTO> agenteDTOList = new ArrayList<>(listarRemitentesByIdeDocumento(idDocumento));
        agenteDTOList.addAll(listarDestinatariosByIdeDocumentoMail(idDocumento));
        return agenteListTransformToFull(agenteDTOList);
    }


    /**
     * @param agente
     * @return
     */
    void actualizarIdeFunciAgenteInterno(CorAgente agente) throws SystemException {

        if (TipoRemitenteEnum.INTERNO.getCodigo().equalsIgnoreCase(agente.getCodTipoRemite())) {
            List<FuncionarioDTO> funcionarios = funcionariosControl.listarFuncionariosByCodDependencia(agente.getCodDependencia());
            BigInteger ideFunci = (funcionarios.size() > 0) ? funcionarios.get(0).getIdeFunci() : null;
            agente.setIdeFunci(ideFunci);
        }
//        return agente;
    }

    /**
     * @param agentes
     * @param datosContactoList
     * @param rDistFisica
     * @return
     */
    List<CorAgente> conformarAgentes(List<AgenteDTO> agentes, List<DatosContactoDTO> datosContactoList, String rDistFisica) throws SystemException {
        List<CorAgente> corAgentes = new ArrayList<>();

        for (AgenteDTO agenteDTO : agentes) {
            CorAgente corAgente = corAgenteTransform(agenteDTO);
            this.actualizarIdeFunciAgenteInterno(corAgente);

            if (TipoAgenteEnum.REMITENTE.getCodigo().equalsIgnoreCase(agenteDTO.getCodTipAgent()) && TipoRemitenteEnum.EXTERNO.getCodigo().equalsIgnoreCase(agenteDTO.getCodTipoRemite()) && datosContactoList != null)
                asignarDatosContacto(corAgente, datosContactoList);

            if (TipoAgenteEnum.DESTINATARIO.getCodigo().equalsIgnoreCase(agenteDTO.getCodTipAgent())) {
                corAgente.setCodEstado(EstadoAgenteEnum.SIN_ASIGNAR.getCodigo());
                corAgente.setEstadoDistribucion(reqDistFisica.equals(rDistFisica) ? EstadoDistribucionFisicaEnum.SIN_DISTRIBUIR.getCodigo() : null);
            }
            corAgentes.add(corAgente);
        }
        return corAgentes;
    }

    /**
     * @param agentes
     * @param rDistFisica
     * @return
     */
    List<CorAgente> conformarAgentesSalida(List<AgenteDTO> agentes, String rDistFisica) throws SystemException {
        List<CorAgente> corAgentes = new ArrayList<>();

        for (AgenteDTO agenteDTO : agentes) {
            CorAgente corAgente = corAgenteTransform(agenteDTO);
            this.actualizarIdeFunciAgenteInterno(corAgente);

            if (TipoAgenteEnum.REMITENTE.getCodigo().equals(agenteDTO.getCodTipAgent())) {
                corAgente.setCodEstado(EstadoAgenteEnum.SIN_ASIGNAR.getCodigo());
                corAgente.setEstadoDistribucion(reqDistFisica.equals(rDistFisica) ? EstadoDistribucionFisicaEnum.SIN_DISTRIBUIR.getCodigo() : null);
            }
            corAgentes.add(corAgente);
        }
        return corAgentes;
    }

    /**
     * @param ideAgente
     * @param estadoDistribucion
     * @throws SystemException
     */
    void actualizarEstadoDistribucion(BigInteger ideAgente, String estadoDistribucion) throws SystemException {
        try {
            em.createNamedQuery("CorAgente.updateEstadoDistribucion")
                    .setParameter("ESTADO_DISTRIBUCION", estadoDistribucion)
                    .setParameter("IDE_AGENTE", ideAgente)
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
     * @param agenteDTO
     * @return
     */
    CorAgente corAgenteTransform(AgenteDTO agenteDTO) {
        final List<DatosContactoDTO> datosContactoDTOList = agenteDTO.getDatosContactoList();
        return CorAgente.newInstance()
                .ideAgente(agenteDTO.getIdeAgente())
                .codTipoRemite(agenteDTO.getCodTipoRemite())
                .codTipoPers(agenteDTO.getCodTipoPers())
                .nombre(agenteDTO.getNombre())
                .razonSocial(agenteDTO.getRazonSocial())
                .nit(agenteDTO.getNit())
                .codCortesia(agenteDTO.getCodCortesia())
                .codEnCalidad(agenteDTO.getCodEnCalidad())
                .ideFunci(agenteDTO.getIdeFunci())
                .codTipDocIdent(agenteDTO.getCodTipDocIdent())
                .nroDocuIdentidad(agenteDTO.getNroDocuIdentidad())
                .codSede(agenteDTO.getCodSede())
                .codDependencia(agenteDTO.getCodDependencia())
                .codEstado(agenteDTO.getCodEstado())
                .estadoDistribucion("SD")
                .fecAsignacion(agenteDTO.getFecAsignacion())
                .codTipAgent(agenteDTO.getCodTipAgent())
                .indOriginal(agenteDTO.getIndOriginal())
                .tvsDatosContactoList(null == datosContactoDTOList ? new ArrayList<>() : datosContactoDTOList.stream()
                        .map(datosContactoDTO -> datosContactoControl.transform(datosContactoDTO)).collect(Collectors.toList()))
                .corCorrespondenciaList(new HashSet<>())
                .build();
    }

    /**
     * @param destinatarioDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public String actualizarDestinatario(DestinatarioDTO destinatarioDTO) throws SystemException {
        try {
            if (!verificarByIdeAgente(destinatarioDTO.getAgenteDestinatario().getIdeAgente()))
                return "0";
            CorAgente destinatario = em.getReference(CorAgente.class, destinatarioDTO.getAgenteDestinatario().getIdeAgente());
            destinatario.setCodDependencia(destinatarioDTO.getAgenteDestinatario().getCodDependencia());
            destinatario.setCodSede(destinatarioDTO.getAgenteDestinatario().getCodSede());

            asignacionControl.actualizarAsignacion(destinatario.getIdeAgente(),
                    destinatario.getCodDependencia(), destinatarioDTO.getIdeFuncionarioCreaModifica());

            em.flush();

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
     * @param remitenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public String actualizarRemitente(RemitenteDTO remitenteDTO) throws SystemException {
        try {
            if (!verificarByIdeAgente(remitenteDTO.getAgenteRemitente().getIdeAgente()))
                return "0";

            CorAgente remitente = em.getReference(CorAgente.class, remitenteDTO.getAgenteRemitente().getIdeAgente());
            log.info("corAgenteRemitente------------------------------------------------{}",remitente);
            remitente.setCodDependencia(remitenteDTO.getAgenteRemitente().getCodDependencia());
            remitente.setCodSede(remitenteDTO.getAgenteRemitente().getCodSede());
            remitente.setCodTipoPers(remitenteDTO.getAgenteRemitente().getCodTipoPers());
            remitente.setCodTipDocIdent(remitenteDTO.getAgenteRemitente().getCodTipDocIdent());
            remitente.setNroDocuIdentidad(remitenteDTO.getAgenteRemitente().getNroDocuIdentidad());
            remitente.setCodCortesia(remitenteDTO.getAgenteRemitente().getCodCortesia());
            remitente.setNombre(remitenteDTO.getAgenteRemitente().getNombre());
            remitente.setCodTipoRemite(remitenteDTO.getAgenteRemitente().getCodTipoRemite());
            remitente.setRazonSocial(remitenteDTO.getAgenteRemitente().getRazonSocial());
            remitente.setNit(remitenteDTO.getAgenteRemitente().getNit());
            remitente.setCodEnCalidad(remitenteDTO.getAgenteRemitente().getCodEnCalidad());
            remitente.setCodEstado(remitenteDTO.getAgenteRemitente().getCodEstado());
            remitente.setCodTipAgent(remitenteDTO.getAgenteRemitente().getCodTipAgent());
            remitente.setIndOriginal(remitenteDTO.getAgenteRemitente().getIndOriginal());

            List<TvsDatosContacto> datosObj = em.createNamedQuery("TvsDatosContacto.findAllbyIdAgenteObj", TvsDatosContacto.class)
                    .setParameter("IDE_AGENTE", remitente.getIdeAgente())
                    .getResultList();
            
          log.info("datos de cntactos del remitente--------------------------------{}",remitente.getTvsDatosContactoList());
            if(!ObjectUtils.isEmpty(datosObj)) {
                for (TvsDatosContacto contacto : datosObj) {
                    remitente.getTvsDatosContactoList().remove(contacto);
                }
            }
            log.info("remitenteDTO.getDatosContactoList------------------------------------------------{}",remitenteDTO.getDatosContactoList());
            for (DatosContactoDTO datosContactoDTO : remitenteDTO.getDatosContactoList()) {
                log.info("----------------------dentro del ciclo----------------------");
                    TvsDatosContacto datosContacto = TvsDatosContacto.newInstance()
                            .nroViaGeneradora(datosContactoDTO.getNroViaGeneradora())
                            .nroPlaca(datosContactoDTO.getNroPlaca())
                            .codTipoVia(datosContactoDTO.getCodTipoVia())
                            .codPrefijoCuadrant(datosContactoDTO.getCodPrefijoCuadrant())
                            .codPostal(datosContactoDTO.getCodPostal())
                            .direccion(datosContactoDTO.getDireccion())
                            .celular(datosContactoDTO.getCelular())
                            .telFijo(datosContactoDTO.getTelFijo())
                            .extension(datosContactoDTO.getExtension())
                            .corrElectronico(datosContactoDTO.getCorrElectronico())
                            .codPais(datosContactoDTO.getCodPais())
                            .codDepartamento(datosContactoDTO.getCodDepartamento())
                            .codMunicipio(datosContactoDTO.getCodMunicipio())
                            .provEstado(datosContactoDTO.getProvEstado())
                            .principal(datosContactoDTO.getPrincipal())
                            .ciudad(datosContactoDTO.getCiudad())
                            .corAgente(remitente)
                            .build();
                    datosContacto.setCorAgente(remitente);
                  //  em.persist(datosContacto);

                    remitente.getTvsDatosContactoList().add(datosContacto);
                    em.merge(remitente);
                }
            em.flush();

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
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AgenteDTO consultarRemitenteByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        try {
            AgenteDTO agenteDTO = em.createNamedQuery("CorAgente.findByNroRadicado", AgenteDTO.class)
                    .setParameter("NRO_RADICADO", nroRadicado)
                    .getSingleResult();
            agenteDTO.setDatosContactoList(datosContactoControl.consultarDatosContactoByIdAgente(agenteDTO.getIdeAgente()));
            return agenteDTO;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("agente.agente_not_exist_by_nro_radicado")
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
}
