package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.ecm.domain.entity.CommunicationType;
import co.com.soaint.ecm.domain.entity.DocumentType;
import co.com.soaint.ecm.domain.entity.SecurityGroupType;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public interface StructureAlfresco extends Serializable {

    MensajeRespuesta crearEstructura(List<EstructuraTrdDTO> trdDTOS) throws SystemException;

    default Folder getFolderBy(final String classType, final String propertyName,
                               final String value) throws SystemException {
        String query = "SELECT * FROM";
        final String ecmError;
        switch (classType) {
            case ConstantesECM.CLASE_BASE:
                query += " cmcor:CM_Unidad_Base WHERE " + propertyName + " = '" + value + "'" +
                        "  AND cmis:objectTypeId = 'F:cmcor:CM_Unidad_Base'";
                ecmError = "No existe Carpeta base con codigo " + value + " en el ECM";
                break;
            case ConstantesECM.CLASE_SEDE:
            case ConstantesECM.CLASE_DEPENDENCIA:
                query += " cmcor:CM_Unidad_Administrativa WHERE " + propertyName + " = '" + value + "'" +
                        "  AND cmis:objectTypeId = 'F:cmcor:CM_Unidad_Administrativa'";
                ecmError = "No existe Dependencia con codigo " + value + " en el ECM";
                break;
            case ConstantesECM.CLASE_SERIE:
                query += " cmcor:CM_Serie WHERE " + propertyName + " = '" + value + "'" +
                        "  AND cmis:objectTypeId = 'F:cmcor:CM_Serie'";
                ecmError = "No existe Serie con codigo " + value + " en el ECM";
                break;
            case ConstantesECM.CLASE_SUBSERIE:
                query += " cmcor:CM_Subserie WHERE " + propertyName + " = '" + value + "'" +
                        "  AND cmis:objectTypeId = 'F:cmcor:CM_Subserie'";
                ecmError = "No existe Subserie con codigo " + value + " en el ECM";
                break;
            default:
                throw new SystemException("Ecm Class Not found");
        }
        final Session session = getSession();
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        final Iterator<QueryResult> iterator = queryResults.iterator();
        if (iterator.hasNext()) {
            final QueryResult next = iterator.next();
            final String objectId = next.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            final CmisObject cmisObject = session.getObject(session.createObjectId(objectId));
            return (Folder) cmisObject;
        }
        throw new SystemException(ecmError);
    }

    Folder getDepFolderByCode(String depCode) throws SystemException;

    Folder getBaseFolder() throws SystemException;

    Folder findUdByIdAndDepCode(String idUd) throws SystemException;

    List<Folder> findAllUDListBy(UnidadDocumentalDTO dto) throws SystemException;

    Session getSession() throws SystemException;

    default List<Folder> findAllforQueryModule(String depCode, String sCode, String ssCode, List<SecurityGroupType> groupTypeList) throws SystemException {
        final Session session = getSession();
        String query = "SELECT " + PropertyIds.OBJECT_ID + " FROM cmcor:CM_Unidad_Documental";
        boolean where = false;
        if (!StringUtils.isEmpty(depCode)) {
            query += " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + depCode + "'";
            where = true;
        }
        if (!StringUtils.isEmpty(sCode) && !"".equals(sCode)) {
            query += (where ? " AND ": " WHERE ") + ConstantesECM.CMCOR_SER_CODIGO + " = '" + sCode + "'";
            where = true;
        }
        if (!StringUtils.isEmpty(ssCode) && !"".equals(ssCode)) {
            query += (where ? " AND ": " WHERE ") + ConstantesECM.CMCOR_SS_CODIGO + " = '" + ssCode + "'";
        }
        final List<Folder> folderList = new ArrayList<>();
        for (QueryResult queryResult : session.query(query, false)) {
            final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            final Folder folder = (Folder) session.getObject(objectId);
            updateUdSecurityGroup(folder);
            final String role = folder.getPropertyValue(ConstantesECM.CMCOR_SER_SECURITY_GROUP);
            final SecurityGroupType securityGroupType = SecurityGroupType.getSecurityBy(role);
            if (groupTypeList.contains(securityGroupType)) {
                folderList.add(folderList.size(), folder);
            }
        }
        return folderList;
    }

    default List<Folder> findAllforQueryModule(List<String> depCodes, List<SecurityGroupType> groupTypeList) throws SystemException {
        final List<Folder> udFolders = new ArrayList<>();
        for (String depCode : depCodes) {
            udFolders.addAll(findAllforQueryModule(depCode, null, null, groupTypeList)
                    .parallelStream().filter(folder -> !folder.getName().startsWith(CommunicationType.PD.getCommunicationName()))
                    .collect(Collectors.toList()));
        }
        return udFolders;
    }

    default List<Folder> findAllUDListBy(String depCode, String sCode, String ssCode) throws SystemException {
        final UnidadDocumentalDTO dto = new UnidadDocumentalDTO();
        dto.setCodigoDependencia(depCode);
        dto.setCodigoSubSerie(ssCode);
        dto.setCodigoSerie(sCode);
        return findAllUDListBy(dto);
    }

    default List<Document> findSavedDocumentsFrom(String depCode) throws SystemException {
        final List<Document> responseList = new ArrayList<>();
        final List<Folder> udList = findAllUDListBy(depCode, null, null);
        udList.stream().filter(folder -> !StringUtils.isEmpty(folder.getPropertyValue(ConstantesECM.CMCOR_UD_ID)))
                .forEach(folder -> {
                    for (CmisObject child : folder.getChildren()) {
                        final String ecmType = child.getType().getId();
                        if (ecmType.startsWith("D:cm")) {
                            responseList.add(responseList.size(), (Document) child);
                        }
                    }
                });
        return responseList;
    }

    default void updateUdSecurityGroup(Folder udFolder) throws SystemException {
        if (udFolder == null)
            return;
        final String ecmUdType = udFolder.getType().getId();
        if (!ecmUdType.endsWith("CM_Unidad_Documental")) {
            throw new SystemException("Folder: " + udFolder.getName() + " no es una unidad documental");
        }

        final Folder folderParent = udFolder.getFolderParent();
        final String ecmType = folderParent.getType().getId();
        final Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put(ConstantesECM.CMCOR_SER_SECURITY_GROUP, SecurityGroupType.PUBLIC.getSecurityName());

        if (ecmType.endsWith("CM_Serie") || ecmType.endsWith("CM_Subserie")) {
            final String parentRole = folderParent.getPropertyValue(ConstantesECM.CMCOR_SER_SECURITY_GROUP);
            final SecurityGroupType groupTypeParent = SecurityGroupType.getSecurityBy(parentRole);
            if (groupTypeParent != null) {
                final String udRole = udFolder.getPropertyValue(ConstantesECM.CMCOR_SER_SECURITY_GROUP);
                final SecurityGroupType groupTypeUd = SecurityGroupType.getSecurityBy(udRole);
                if (groupTypeParent != groupTypeUd) {
                    propertyMap.put(ConstantesECM.CMCOR_SER_SECURITY_GROUP, groupTypeParent.getSecurityName());
                    udFolder.updateProperties(propertyMap);
                    udFolder.refresh();
                }
            }
        } else {
            udFolder.updateProperties(propertyMap);
            udFolder.refresh();
        }
    }

    default List<Folder> getUdListBySecurityType(SecurityGroupType securityGroupType) throws SystemException {
        final List<Folder> udList = new ArrayList<>();
        if (securityGroupType == null) {
            return udList;
        }
        final Session session = getSession();
        final String ecmType = "cmcor:CM_Unidad_Documental";
        final String query = "SELECT " + PropertyIds.OBJECT_ID + " FROM " + ecmType +
                " WHERE " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + ecmType + "'" +
                " AND " + PropertyIds.NAME + " NOT LIKE '" + CommunicationType.PD.getCommunicationName() + "%'" +
                " AND " + PropertyIds.NAME + " <> '" + CommunicationType.PA.getCommunicationName() + "'" +
                " AND (" + ConstantesECM.CMCOR_SER_SECURITY_GROUP + " = '" + securityGroupType.getSecurityName() + "'" +
                " OR " + ConstantesECM.CMCOR_SER_SECURITY_GROUP + " IS NULL" +
                " OR " + ConstantesECM.CMCOR_SER_SECURITY_GROUP + " = '')";
        for (QueryResult queryResult : session.query(query, false)) {
            final String objectId = queryResult.getPropertyValueById(PropertyIds.OBJECT_ID);
            final Folder folder = (Folder) session.getObject(objectId);
            updateUdSecurityGroup(folder);
            final String udRole = folder.getPropertyValue(ConstantesECM.CMCOR_SER_SECURITY_GROUP);
            final SecurityGroupType groupType = SecurityGroupType.getSecurityBy(udRole);
            if (groupType == securityGroupType) {
                udList.add(udList.size(), folder);
            }
        }
        return udList;
    }

    default Document findDocumentByNroRad(String nroRadicado) throws SystemException {
        if (StringUtils.isEmpty(nroRadicado)) {
            return null;
        }
        final Session session = getSession();
        final String query = "SELECT " + PropertyIds.OBJECT_ID + " FROM cmcor:CM_DocumentoPersonalizado" +
                " WHERE " + ConstantesECM.CMCOR_DOC_RADICADO + " LIKE '%" + nroRadicado + "'" +
                " AND " + ConstantesECM.CMCOR_TIPO_DOCUMENTO + " = '" + DocumentType.MAIN.getType() + "'";
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        final Iterator<QueryResult> iterator = queryResults.iterator();

        if (iterator.hasNext()) {
            final String objectId = iterator.next().getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            return (Document) session.getObject(objectId);
        }
        return null;
    }
}