package co.com.foundation.soaint.documentmanager.integration.transformer;


import co.com.foundation.soaint.documentmanager.domain.EstructuraEcmVo;
import co.com.foundation.soaint.documentmanager.integration.domain.ContenidoTrdDTO;
import co.com.foundation.soaint.documentmanager.integration.domain.EstructuraEcmDTO;
import co.com.foundation.soaint.documentmanager.integration.domain.OrganigramaDTO;

import co.com.foundation.soaint.infrastructure.transformer.Transformer;
import org.springframework.stereotype.Component;


@Component
public class EstructuraEcmTransformer implements Transformer<EstructuraEcmVo, EstructuraEcmDTO> {


    public EstructuraEcmDTO transform(EstructuraEcmVo estructuraEcmVo) {
        EstructuraEcmDTO ecmDTO = new EstructuraEcmDTO();
        for (int i = 0; i < estructuraEcmVo.getContenidoDependenciaList().size(); i++) {
            ecmDTO.getContenidoDependenciaList().add(i,
                    new ContenidoTrdDTO( estructuraEcmVo.getContenidoDependenciaList().get(i).getCodSerie(),
                             estructuraEcmVo.getContenidoDependenciaList().get(i).getCodSubSerie(),
                             estructuraEcmVo.getContenidoDependenciaList().get(i).getIdOrgAdm(),
                             estructuraEcmVo.getContenidoDependenciaList().get(i).getIdOrgOfc(),
                             estructuraEcmVo.getContenidoDependenciaList().get(i).getNomSerie(),
                             estructuraEcmVo.getContenidoDependenciaList().get(i).getNomSubSerie()));
        }
        for(int i = 0; i < estructuraEcmVo.getOrganigramaItemList().size(); i++){
            ecmDTO.getOrganigramaItemList().add(i,
                    new OrganigramaDTO(estructuraEcmVo.getOrganigramaItemList().get(i).getCodOrg(),
                            estructuraEcmVo.getOrganigramaItemList().get(i).getIdeOrgaAdmin(),
                            estructuraEcmVo.getOrganigramaItemList().get(i).getNomOrg(),
                            estructuraEcmVo.getOrganigramaItemList().get(i).getTipo()));
        }
        return ecmDTO;
    }
}
