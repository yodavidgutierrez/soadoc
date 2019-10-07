package co.com.foundation.soaint.documentmanager.business.ecm;

import co.com.foundation.soaint.documentmanager.adapter.EcmClientAdapter;
import co.com.foundation.soaint.documentmanager.adapter.OrganigramaClientAdapter;
import co.com.foundation.soaint.documentmanager.business.organigrama.interfaces.OrganigramaManagerProxy;
import co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion.interfaces.VersionesTabRetDocManagerProxy;
import co.com.foundation.soaint.documentmanager.domain.*;

import co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.EstructuraTrdDTO;
import co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration.OrganigramaDTO;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmVersionTrd;
import co.com.foundation.soaint.documentmanager.persistence.entity.TvsOrganigramaAdministrativo;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrodriguez on 8/12/2016.
 */
@BusinessBoundary
public class EcmTrdManager {


    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(EcmTrdManager.class.getName());

    @Autowired
    private VersionesTabRetDocManagerProxy version;

    @Autowired
    private OrganigramaManagerProxy organigrama;

    @Autowired
    private EcmClientAdapter adapter;

    @Autowired
    private OrganigramaClientAdapter adapterOrg;

    public EcmTrdManager() {
    }

    public boolean generarEstructuraECM() throws SystemException, BusinessException {

        List<EstructuraEcmVo> estructuraTrdList = new ArrayList<>();

        List<TvsOrganigramaAdministrativo> tvsOrganigramaList = organigrama.findAllOfcProdTrdECM();

        List<EstructuraTrdDTO> organigramaItem = new ArrayList<>();

        for (TvsOrganigramaAdministrativo item : tvsOrganigramaList) {

            EstructuraEcmVo estructura = new EstructuraEcmVo();

            EstructuraTrdDTO estructuraTrdDTO = new EstructuraTrdDTO();

            for (OrganigramaItemVO elementos : organigrama.listarElementosDeNivelSuperior(item.getIdeOrgaAdmin())) {

                OrganigramaVo organigrama = new OrganigramaVo();
                organigrama.setIdeOrgaAdmin(elementos.getIdeOrgaAdmin());
                organigrama.setCodOrg(elementos.getCodOrg());
                organigrama.setNomOrg(elementos.getNomOrg());
                organigrama.setTipo(elementos.getNivel().equals(0) ? "P" : "H");
                organigrama.setRadicadora(elementos.getIndCorrespondencia().equals("1"));

                OrganigramaDTO organigramaDTO = new OrganigramaDTO();
                organigramaDTO.setIdeOrgaAdmin(elementos.getIdeOrgaAdmin());
                organigramaDTO.setCodOrg(elementos.getCodOrg());
                organigramaDTO.setNomOrg(elementos.getNomOrg());
                organigramaDTO.setTipo(elementos.getNivel().equals(0) ? "P" : "H");
                organigramaDTO.setAbreviatura(elementos.getAbrevOrg());
                organigramaDTO.setCodigoBase(elementos.getNivel().toString());
                organigramaDTO.setRadicadora(elementos.getIndCorrespondencia().equals("1"));

                estructuraTrdDTO.getOrganigramaItemList().add(organigramaDTO);

                estructura.getOrganigramaItemList().add(organigrama);
            }

            organigramaItem.add(estructuraTrdDTO);

            AdmVersionTrd admVersionTrd = version.consulVersionOfcProdTop(item.getCodOrg());
            List<DataTrdEcmVO> dataTrdEcmVOList = version.dataTrdByOfcProdListEcm(item.getCodOrg(), admVersionTrd.getIdeVersion());


            for (DataTrdEcmVO trdEcmVO : dataTrdEcmVOList) {

                ContenidoTrdVo contenido = new ContenidoTrdVo();
                contenido.setIdOrgAdm(trdEcmVO.getUnidadAdministrativa());
                contenido.setIdOrgOfc(trdEcmVO.getOficinaProductora());
                contenido.setCodSerie(trdEcmVO.getCodSerie());
                contenido.setNomSerie(trdEcmVO.getNomSerie());
                contenido.setCodSubSerie(trdEcmVO.getCodSubSerie());
                contenido.setNomSubSerie(trdEcmVO.getNomSubSerie());
                contenido.setRetArchivoGestion(trdEcmVO.getArchivoGestion());
                contenido.setRetArchivoCentral(trdEcmVO.getArchivoCentral());
                contenido.setProcedimiento(trdEcmVO.getProcedimientos());
                contenido.setDiposicionFinal(trdEcmVO.getDiposicion());
                contenido.setRadicadora(trdEcmVO.isRadicadora());
                contenido.setGrupoSeguridad(trdEcmVO.getGrupoSeguridad());
                estructura.getContenidoDependenciaList().add(contenido);

            }

            estructuraTrdList.add(estructura);


        }
        boolean resultado = adapter.generarEstructuraECM(estructuraTrdList);
        adapterOrg.generarOrganigrama(organigramaItem);
        return resultado;
    }


}
