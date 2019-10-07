package co.com.foundation.soaint.documentmanager.business.organigrama.interfaces;


import co.com.foundation.soaint.documentmanager.domain.EstructuraEcmVo;
import co.com.foundation.soaint.documentmanager.domain.OrganigramaItemVO;
import co.com.foundation.soaint.documentmanager.persistence.entity.TvsOrganigramaAdministrativo;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;

import java.util.List;

public interface OrganigramaManagerProxy {

    void publicarOrganigrama() throws SystemException,BusinessException;

    List<OrganigramaItemVO> listarElementosDeSegundoNivel() throws SystemException, BusinessException;

    List<OrganigramaItemVO> listarElementosDeNivelInferior(final Long ideOrgaAdmin) throws SystemException, BusinessException;

    void consultarElementosRecursivamente(final List<OrganigramaItemVO> data, final List<OrganigramaItemVO> storage) throws SystemException, BusinessException;

    List<OrganigramaItemVO> listarElementosDeNivelSuperior(final Long ideOrgaAdmin);

    List<OrganigramaItemVO> consultarOrganigramaTop() throws SystemException, BusinessException;

    OrganigramaItemVO consultarElemetosOrganigramaPorNombre(String codigoOrganigrama) throws BusinessException, SystemException;

    List<TvsOrganigramaAdministrativo> findAllUniAmdTrd() throws BusinessException, SystemException;

    List<TvsOrganigramaAdministrativo> findAllOfcProdTrd(String ideUniAmt) throws BusinessException, SystemException;

    List<TvsOrganigramaAdministrativo> listarVersionOrganigrama() throws SystemException,BusinessException;

    List<OrganigramaItemVO> consultarOrganigramaPorVersion(String valVersion) throws SystemException,BusinessException;

    void consultarElementosRecursivamenteVersion(final List<OrganigramaItemVO> data, final List<OrganigramaItemVO> storage, String valVersion);

    List<OrganigramaItemVO> listarElementosDeNivelInferiorPorVersion(final Long ideOrgaAdmin, String valVersion);

    List<TvsOrganigramaAdministrativo> findAllOfcProdTrdECM() throws BusinessException, SystemException;

    List<TvsOrganigramaAdministrativo> findAllUniAmdCddOrg(String versionCdd) throws BusinessException, SystemException;

    List<TvsOrganigramaAdministrativo> findAllOfcProdCcdOrg(String versionCdd, String ideUniAmt) throws BusinessException, SystemException;

    List<TvsOrganigramaAdministrativo> findAllUniAmdTrdOrg(String valVersionOrg) throws BusinessException, SystemException;

    List<TvsOrganigramaAdministrativo> findAllOfcProdTrdOrg(String ideUniAmt, String codVersionOrg) throws BusinessException, SystemException;

    List<EstructuraEcmVo> obtenerEstructuraDocumental() throws SystemException, BusinessException;
}
