package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.RecordStructureTree;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.StructureAlfresco;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service("recordStructureContent")
public class RecordStructureTreeImpl extends RecordStructureImpl
        implements RecordStructureTree {

    @Autowired
    @Qualifier("contentStructure")
    private StructureAlfresco contentStructure;

    @Override
    public MensajeRespuesta crearEstructuraDeContent() throws SystemException {
        final Folder baseFolderContent = contentStructure.getBaseFolder();
        Folder baseFolderRecord;
        try {
            baseFolderRecord = getBaseFolder();
        } catch (SystemException ex) {





            baseFolderRecord = null;
        }

        crearEstructuraDeContent(baseFolderContent, baseFolderRecord);
        return null;
    }

    private void crearEstructuraDeContent(Folder folderContent, Folder folderRecord) {

    }

    private Map<String, Object> getPropertyMapFromFolderContent(Folder folderContent) {

        final Map<String, Object> propertyMap = new HashMap<>();

        propertyMap.put(PropertyIds.NAME, folderContent.getName());

        final String objectTypeId = folderContent.getType().getId();

        switch (objectTypeId) {
            case ConstantesECM.CMCOR_UB:
                propertyMap.put(ConstantesECM.RMC_X_FONDO, folderContent.getPropertyValue(ConstantesECM.CMCOR_UB_CODIGO));
                return null;
            case ConstantesECM.CMCOR_DEP:
                propertyMap.put(ConstantesECM.RMC_X_FONDO, folderContent.getPropertyValue(ConstantesECM.CMCOR_UB_CODIGO));
                propertyMap.put(ConstantesECM.RMC_X_SECCION, folderContent.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));
                break;

        }

        propertyMap.put(ConstantesECM.RMC_X_CONSECUTIVO_TP_CATEGORIA, "0");
        propertyMap.put(ConstantesECM.RMC_X_CONSECUTIVO_TS_CATEGORIA, "0");

        propertyMap.put(ConstantesECM.RMC_X_ORG_ACTIVO, true);

        return propertyMap;
    }
}
