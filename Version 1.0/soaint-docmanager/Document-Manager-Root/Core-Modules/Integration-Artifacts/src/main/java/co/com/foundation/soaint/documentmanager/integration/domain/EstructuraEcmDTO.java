package co.com.foundation.soaint.documentmanager.integration.domain;


import java.util.ArrayList;
import java.util.List;

public class EstructuraEcmDTO {
    private List<ContenidoTrdDTO> contenidoDependenciaList;
    private List<OrganigramaDTO> organigramaItemList;

    public EstructuraEcmDTO() {
        contenidoDependenciaList =new ArrayList<>();
        organigramaItemList =new ArrayList<>();
    }

    public List<OrganigramaDTO> getOrganigramaItemList() {
        return organigramaItemList;
    }

    public void setOrganigramaItemList(List<OrganigramaDTO> organigramaItemList) {
        this.organigramaItemList = organigramaItemList;
    }

    public List<ContenidoTrdDTO> getContenidoDependenciaList() {
        return contenidoDependenciaList;
    }

    public void setContenidoDependenciaList(List<ContenidoTrdDTO> contenidoDependenciaList) {
        this.contenidoDependenciaList = contenidoDependenciaList;
    }
}
