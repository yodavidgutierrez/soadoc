package co.com.foundation.soaint.documentmanager.integration.transformer;

import co.com.foundation.soaint.documentmanager.domain.OrganigramaItemVO;
import co.com.foundation.soaint.documentmanager.integration.domain.OrganigramaItemDTO;
import co.com.foundation.soaint.infrastructure.transformer.Transformer;
import org.springframework.stereotype.Component;


/**
 * Created by jrodriguez on 27/01/2017.
 */
@Component
public class OrganigramaItemTransformer implements Transformer<OrganigramaItemVO, OrganigramaItemDTO> {


    public OrganigramaItemDTO transform(OrganigramaItemVO itemVO){

        OrganigramaItemDTO itemDTO =new OrganigramaItemDTO();
        itemDTO.setIdeOrgaAdmin(itemVO.getIdeOrgaAdmin());
        itemDTO.setCodOrg(itemVO.getCodOrg());
        itemDTO.setNomOrg(itemVO.getNomOrg());
        itemDTO.setIdOrgaAdminPadre(itemVO.getIdOrgaAdminPadre());
        itemDTO.setNomPadre(itemVO.getNomPadre());
        itemDTO.setEstado(itemVO.getEstado());
        itemDTO.setNivel(itemVO.getNivel());
        itemDTO.setDescOrg(itemVO.getDescOrg());
        itemDTO.setNivelPadre(itemVO.getNivelPadre());

        return  itemDTO;
    }

}
