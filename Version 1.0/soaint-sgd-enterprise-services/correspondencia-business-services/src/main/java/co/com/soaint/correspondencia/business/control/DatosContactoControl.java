package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.TvsDatosContacto;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.AgenteFullDTO;
import co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO;
import co.com.soaint.foundation.canonical.correspondencia.DatosContactoFullDTO;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoRemitenteEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class DatosContactoControl extends GenericControl<TvsDatosContacto> {

    @Autowired
    private DepartamentoControl departamentoControl;

    @Autowired
    private MunicipioControl municipioControl;

    @Autowired
    private PaisControl paisControl;

    @Autowired
    private ConstantesControl constantesControl;

    public DatosContactoControl() {
        super(TvsDatosContacto.class);
    }

    /**
     * @param agenteDTOList
     * @return
     */
    List<DatosContactoDTO> consultarDatosContactoByAgentes(List<AgenteDTO> agenteDTOList) throws SystemException {
        List<DatosContactoDTO> datosContactoDTOList = new ArrayList<>();
        try {
            agenteDTOList.forEach(agenteDTO -> {
                if (TipoAgenteEnum.REMITENTE.getCodigo().equals(agenteDTO.getCodTipAgent()) && TipoRemitenteEnum.EXTERNO.getCodigo().equals(agenteDTO.getCodTipoRemite())) {
                    datosContactoDTOList.addAll(em.createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class)
                            .setParameter("IDE_AGENTE", agenteDTO.getIdeAgente())
                            .getResultList());
                }
            });
            return datosContactoDTOList;
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
    List<DatosContactoDTO> consultarDatosContactoByAgentesCorreo(AgenteDTO agenteDTO) throws SystemException {
        try {

            return new ArrayList<>(em.createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class)
                    .setParameter("IDE_AGENTE", agenteDTO.getIdeAgente())
                    .getResultList());
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param contactoDTO
     * @return
     */
    private DatosContactoFullDTO datosContactoTransformToFull(DatosContactoDTO contactoDTO) throws SystemException {
        try {
            return DatosContactoFullDTO.newInstance()
                    .celular(contactoDTO.getCelular())
                    .ciudad(contactoDTO.getCiudad())
                    .codDepartamento(contactoDTO.getCodDepartamento())
                    .departamento(departamentoControl.consultarDepartamentoByCod(contactoDTO.getCodDepartamento()))
                    .codMunicipio(contactoDTO.getCodMunicipio())
                    .municipio(municipioControl.consultarMunicipioByCodMunic(contactoDTO.getCodMunicipio()))
                    .codPais(contactoDTO.getCodPais())
                    .pais(paisControl.consultarPaisByCod(contactoDTO.getCodPais()))
                    .codPostal(contactoDTO.getCodPostal())
                    .codPrefijoCuadrant(contactoDTO.getCodPrefijoCuadrant())
                    .codTipoVia(contactoDTO.getCodTipoVia())
                    .corrElectronico(contactoDTO.getCorrElectronico())
                    .descTipoVia(constantesControl.consultarNombreConstanteByCodigo(contactoDTO.getCodTipoVia()))
                    .descPrefijoCuadrant(constantesControl.consultarNombreConstanteByCodigo(contactoDTO.getCodPrefijoCuadrant()))
                    .direccion(contactoDTO.getDireccion())
                    .extension(contactoDTO.getExtension())
                    .ideContacto(contactoDTO.getIdeContacto())
                    .nroPlaca(contactoDTO.getNroPlaca())
                    .nroViaGeneradora(contactoDTO.getNroViaGeneradora())
                    .principal(contactoDTO.getPrincipal())
                    .provEstado(contactoDTO.getProvEstado())
                    .telFijo(contactoDTO.getTelFijo())
                    .build();
        } catch (Exception e) {
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param datosContactoDTOList
     * @return
     */
    List<DatosContactoFullDTO> datosContactoListTransformToFull(List<DatosContactoDTO> datosContactoDTOList) throws SystemException {
        List<DatosContactoFullDTO> datosContactoFullDTOList = new ArrayList<>();
        try {
            for (DatosContactoDTO contactoDTO : datosContactoDTOList) {
                datosContactoFullDTOList.add(datosContactoTransformToFull(contactoDTO));
            }

            return datosContactoFullDTOList;
        } catch (Exception e) {
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param agenteDTOList
     * @return
     */
    List<DatosContactoFullDTO> consultarDatosContactoFullByAgentes(List<AgenteFullDTO> agenteDTOList) throws SystemException {
        List<DatosContactoDTO> datosContactoDTOList = new ArrayList<>();
        try {
            agenteDTOList.forEach(AgenteFullDTO -> {
                if (TipoAgenteEnum.REMITENTE.getCodigo().equals(AgenteFullDTO.getCodTipAgent()) && TipoRemitenteEnum.EXTERNO.getCodigo().equals(AgenteFullDTO.getCodTipoRemite())) {
                    datosContactoDTOList.addAll(em.createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class)
                            .setParameter("IDE_AGENTE", AgenteFullDTO.getIdeAgente())
                            .getResultList());
                }
            });
            return datosContactoListTransformToFull(datosContactoDTOList);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param idAgente
     * @return
     */
    public List<DatosContactoDTO> consultarDatosContactoByIdAgente(BigInteger idAgente) throws SystemException {
        try {
            return em.createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class)
                    .setParameter("IDE_AGENTE", idAgente)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error in consultarDatosContactoByIdAgente service " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public DatosContactoDTO consultarDatosContactoPrincipalByIdAgente(BigInteger idAgente) throws SystemException {
        try {
            log.info("ide agente ---------------------------------------{}",idAgente);
            List<DatosContactoDTO> datosContactoDTOS = em.createNamedQuery("TvsDatosContacto.findPrincipalByIdeAgente", DatosContactoDTO.class)
                    .setParameter("IDE_AGENTE", idAgente)
                    .getResultList();
            log.info("datos contacto qe devuelve---------------------------------------{}",datosContactoDTOS);
            if (ObjectUtils.isEmpty(datosContactoDTOS)){
                return DatosContactoDTO.newInstance().build();
            }
            return datosContactoDTOS.get(0);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error in consultarDatosContactoByIdAgente service " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public List<DatosContactoDTO> consultarDatosContactoByNroIdentidad(String nroIdentidad, String tipoAgente, String tipoDoc) throws BusinessException {
        try {
            if ("TP-PERPN".equalsIgnoreCase(tipoAgente) && null != nroIdentidad && !"".equals(nroIdentidad.trim())) {
                return em.createNamedQuery("TvsDatosContacto.findByNroIdentidadAndTipAgent", DatosContactoDTO.class)
                        .setParameter("NRO_IDENTIDAD", nroIdentidad.trim())
                        .setParameter("TIPO_DOC", tipoDoc)
                        .getResultList();
            }
            if ("TP-PERPJ".equalsIgnoreCase(tipoAgente) && null != nroIdentidad && !"".equals(nroIdentidad.trim())) {
                return em.createNamedQuery("TvsDatosContacto.findByNITAndTipAgent", DatosContactoDTO.class)
                        .setParameter("NIT", nroIdentidad.trim())
                        .setParameter("TIPO_DOC", tipoDoc)
                        .getResultList();
            }
            return new ArrayList<>();
        } catch (Exception ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error in consultarDatosContactoByNroIdentidad", ex.getMessage())
                    .buildBusinessException();
        }
    }

    /**
     * @param datosContactoDTO
     * @return TvsDatosContacto
     */
    TvsDatosContacto transform(DatosContactoDTO datosContactoDTO) {
        return TvsDatosContacto.newInstance()
                .ideContacto(datosContactoDTO.getIdeContacto())
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
                .tipoContacto(datosContactoDTO.getTipoContacto())
                .build();
    }

    @Transactional
    void asignarDatosContacto(CorAgente corAgente) throws SystemException {
        try {
            final List<TvsDatosContacto> contactoListEntrada = corAgente.getTvsDatosContactoList();
            log.info("Method asignarDatosContacto ---------------------------------- {} ---- {}", "contactoListEntrada", contactoListEntrada.size());
            final BigInteger idAgente = corAgente.getIdeAgente();
            if (null != idAgente) {
                CorAgente agenteDb = em.find(CorAgente.class, idAgente);
                if (null != agenteDb) {

                    agenteDb.getTvsDatosContactoList().clear();
                    contactoListEntrada.forEach(agenteDb::addDatosContacto);

                    final String nombreAgenteDto = corAgente.getNombre();
                    final String nombreAgenteEntity = agenteDb.getNombre();

                    if (!StringUtils.isEmpty(nombreAgenteDto) && !nombreAgenteDto.equalsIgnoreCase(nombreAgenteEntity)) {
                        agenteDb.setNombre(nombreAgenteDto);
                    }
                    em.flush();
                    return;
                }
            }
            corAgente.setTvsDatosContactoList(new ArrayList<>());
            contactoListEntrada.forEach(corAgente::addDatosContacto);
        } catch (Exception ex) {
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }
}
