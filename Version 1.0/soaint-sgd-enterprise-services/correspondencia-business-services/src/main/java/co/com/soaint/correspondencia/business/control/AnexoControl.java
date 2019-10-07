package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAnexo;
import co.com.soaint.foundation.canonical.correspondencia.AnexoDTO;
import co.com.soaint.foundation.canonical.correspondencia.AnexoFullDTO;
import co.com.soaint.foundation.canonical.correspondencia.AnexosFullDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
public class AnexoControl extends GenericControl<CorAnexo> {

    @Autowired
    private ConstantesControl constantesControl;

    public AnexoControl() {
        super(CorAnexo.class);
    }

    /**
     * @param anexoDTO
     * @return
     */
    public AnexoFullDTO anexoTransformToFull(AnexoDTO anexoDTO) throws SystemException {
        try {
            return AnexoFullDTO.newInstance()
                    .ideAnexo(anexoDTO.getIdeAnexo())
                    .codAnexo(anexoDTO.getCodAnexo())
                    .descTipoAnexo(constantesControl.consultarNombreConstanteByCodigo(anexoDTO.getCodAnexo()))
                    .codTipoSoporte(anexoDTO.getCodTipoSoporte())
                    .descTipoSoporte(constantesControl.consultarNombreConstanteByCodigo(anexoDTO.getCodTipoSoporte()))
                    .build();
            //pendiente construir transform de lista de contactoFullDTO
        } catch (Exception e) {
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param anexoDTOList
     * @return
     */
    List<AnexoFullDTO> anexoListTransformToFull(List<AnexoDTO> anexoDTOList) throws SystemException {
        List<AnexoFullDTO> anexoFullDTOList = new ArrayList<>();
        try {

            for (AnexoDTO anexoDTO : anexoDTOList) {
                anexoFullDTOList.add(anexoTransformToFull(anexoDTO));
            }

            return anexoFullDTOList;
            //pendiente construir transform de lista de contactoFullDTO
        } catch (Exception e) {
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param ppdDocumentoDTOList
     * @return
     */
    List<AnexoDTO> consultarAnexosByPpdDocumentos(List<PpdDocumentoDTO> ppdDocumentoDTOList) throws SystemException {
        List<AnexoDTO> anexoList = new ArrayList<>();
        try {
            for (PpdDocumentoDTO ppdDocumentoDTO : ppdDocumentoDTOList) {
                anexoList.addAll(em.createNamedQuery("CorAnexo.findByIdePpdDocumento", AnexoDTO.class)
                        .setParameter("IDE_PPD_DOCUMENTO", ppdDocumentoDTO.getIdePpdDocumento())
                        .getResultList());
            }
            return anexoList;
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
    public AnexosFullDTO listarAnexosByNroRadicado(String nroRadicado) throws SystemException {
        try {
            return AnexosFullDTO.newInstance().anexos(em.createNamedQuery("CorAnexo.findAnexosByNroRadicado", AnexoFullDTO.class)
                    .setParameter("NRO_RADICADO", nroRadicado)
                    .getResultList()).build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional
    public List<AnexoDTO> actualizarAnexos(List<AnexoDTO> anexos) throws SystemException {
        try {
            for (AnexoDTO anexo : anexos) {
                em.createNamedQuery("CorAnexo.updateAnexo")
                        .setParameter("DESCRIPCION", anexo.getDescripcion())
                        .setParameter("ID_ANEXO", anexo.getIdeAnexo())
                        .executeUpdate();
            }
            return anexos;
        } catch (Exception ex) {
            log.error("Business Control - Error en servicio de negocio actualizarAnexos", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error " + ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }

    }

    /**
     * @param anexoDTO
     * @return
     */
    CorAnexo corAnexoTransform(AnexoDTO anexoDTO) {
        return CorAnexo.newInstance()
                .codAnexo(anexoDTO.getCodAnexo())
                .descripcion(anexoDTO.getDescripcion())
                .codTipoSoporte(anexoDTO.getCodTipoSoporte())
                .build();
    }
}
