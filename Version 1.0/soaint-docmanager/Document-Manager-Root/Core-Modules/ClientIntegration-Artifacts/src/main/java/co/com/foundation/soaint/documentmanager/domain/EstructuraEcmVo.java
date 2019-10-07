package co.com.foundation.soaint.documentmanager.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrodriguez on 8/12/2016.
 */
public class EstructuraEcmVo {

    private List<ContenidoTrdVo> contenidoDependenciaList;
    private List<OrganigramaVo> organigramaItemList;

    public EstructuraEcmVo() {
        contenidoDependenciaList =new ArrayList<>();
        organigramaItemList =new ArrayList<>();
    }

    public List<OrganigramaVo> getOrganigramaItemList() {
        return organigramaItemList;
    }

    public void setOrganigramaItemList(List<OrganigramaVo> organigramaItemList) {
        this.organigramaItemList = organigramaItemList;
    }

    public List<ContenidoTrdVo> getContenidoDependenciaList() {
        return contenidoDependenciaList;
    }

    public void setContenidoDependenciaList(List<ContenidoTrdVo> contenidoDependenciaList) {
        this.contenidoDependenciaList = contenidoDependenciaList;
    }
}
