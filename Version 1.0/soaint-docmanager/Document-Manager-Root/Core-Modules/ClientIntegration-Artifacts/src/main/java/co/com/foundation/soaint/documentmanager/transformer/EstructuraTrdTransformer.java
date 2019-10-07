package co.com.foundation.soaint.documentmanager.transformer;

import co.com.foundation.soaint.documentmanager.domain.ContenidoTrdVo;
import co.com.foundation.soaint.documentmanager.domain.EstructuraEcmVo;
import co.com.foundation.soaint.documentmanager.domain.OrganigramaVo;
import co.com.foundation.soaint.documentmanager.integration.client.ecmintegration.ContenidoDependenciaTrdDTO;
import co.com.foundation.soaint.documentmanager.integration.client.ecmintegration.EstructuraTrdDTO;
import co.com.foundation.soaint.documentmanager.integration.client.ecmintegration.OrganigramaDTO;
import co.com.foundation.soaint.infrastructure.transformer.Transformer;
import org.springframework.stereotype.Component;

/**
 * Created by jrodriguez on 8/12/2016.
 */
@Component
public class EstructuraTrdTransformer implements Transformer<EstructuraEcmVo, EstructuraTrdDTO> {


    public EstructuraTrdDTO transform(EstructuraEcmVo input) {

        EstructuraTrdDTO trdVO  =new EstructuraTrdDTO();

        for(OrganigramaVo item :input.getOrganigramaItemList()){
            OrganigramaDTO organigramaVO =new OrganigramaDTO();
            organigramaVO.setIdeOrgaAdmin(item.getIdeOrgaAdmin());
            organigramaVO.setCodOrg(item.getCodOrg());
            organigramaVO.setNomOrg(item.getNomOrg());
            organigramaVO.setTipo(item.getTipo());
            organigramaVO.setRadicadora(item.isRadicadora());
            trdVO.getOrganigramaItemList().add(organigramaVO);
        }

        for (ContenidoTrdVo contenidoTrd :input.getContenidoDependenciaList()) {
            ContenidoDependenciaTrdDTO contenido = new ContenidoDependenciaTrdDTO();
            contenido.setIdOrgAdm(contenidoTrd.getIdOrgAdm());
            contenido.setIdOrgOfc(contenidoTrd.getIdOrgOfc());
            contenido.setCodSerie(contenidoTrd.getCodSerie());
            contenido.setNomSerie(contenidoTrd.getNomSerie());
            contenido.setCodSubSerie(contenidoTrd.getCodSubSerie());
            contenido.setNomSubSerie(contenidoTrd.getNomSubSerie());
            contenido.setRetArchivoGestion(contenidoTrd.getRetArchivoGestion());
            contenido.setRetArchivoCentral(contenidoTrd.getRetArchivoCentral());
            contenido.setProcedimiento(contenidoTrd.getProcedimiento());
            contenido.setDiposicionFinal(contenidoTrd.getDiposicionFinal());
            contenido.setRadicadora(contenidoTrd.isRadicadora());
            contenido.setGrupoSeguridad(contenidoTrd.getGrupoSeguridad());
            trdVO.getContenidoDependenciaList().add(contenido);
        }
        return trdVO;
    }

}
