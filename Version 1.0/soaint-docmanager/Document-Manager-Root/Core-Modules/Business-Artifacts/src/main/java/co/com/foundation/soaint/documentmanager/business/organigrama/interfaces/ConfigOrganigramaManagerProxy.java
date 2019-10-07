package co.com.foundation.soaint.documentmanager.business.organigrama.interfaces;

import co.com.foundation.soaint.documentmanager.domain.OrganigramaItemVO;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.documentmanager.persistence.entity.TvsConfigOrgAdministrativo;

import java.io.StringBufferInputStream;
import java.util.List;

/**
 * Created by jrodriguez on 30/10/2016.
 */
public interface ConfigOrganigramaManagerProxy {

    List<OrganigramaItemVO> consultarOrganigrama() throws SystemException, BusinessException;

    void updateOrganigrama(TvsConfigOrgAdministrativo organigrama) throws SystemException, BusinessException;

    void createOrganigrama(TvsConfigOrgAdministrativo organigrama) throws SystemException, BusinessException;

    List<OrganigramaItemVO> listarElementosOrganigrama() throws SystemException, BusinessException;

    boolean organigramaExistByCode(String codOrg) throws SystemException, BusinessException;

    boolean organigramaExistByName(String nomOrg) throws SystemException, BusinessException;



    void cleanOrganigrama() throws BusinessException, SystemException;

}
