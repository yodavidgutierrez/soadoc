package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Configuracion;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.Connection;
import co.com.soaint.ecm.domain.entity.AccionUsuario;
import co.com.soaint.ecm.domain.entity.SecurityGroupType;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.foundation.canonical.ecm.ContenidoDependenciaTrdDTO;
import co.com.soaint.foundation.canonical.ecm.OrganigramaDTO;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Log4j2
@Service("contentStructure")
public class ContentStructureImpl extends AbstractStructure {

    private static final Long serialVersionUID = -5164634613054646L;

    @Autowired
    @Qualifier("connection")
    private Connection connection;

    @Autowired
    @Qualifier("configuracion")
    private Configuracion configuracion;

    @PostConstruct
    public void init() {
        setConnection(connection);
    }

    @Override
    public Folder getDepFolderByCode(String depCode) throws SystemException {
        final Session session = getSession();
        final String ecmObject = ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_DEPENDENCIA);
        final String query = "SELECT " + PropertyIds.OBJECT_ID + " FROM " + ecmObject +
                " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + depCode + "'" +
                " AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + ecmObject + "'";
        final Iterator<QueryResult> iterator = session.query(query, false).iterator();
        if (iterator.hasNext()) {
            final String objectId = iterator.next().getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            return (Folder) session.getObject(objectId);
        }
        throw new SystemException("Dependency Folder not found in Content with Code " + depCode);
    }

    @Override
    public Folder getBaseFolder() throws SystemException {
        final String ecmType = ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_BASE);
        final Session session = getSession();
        final String query = "SELECT " + PropertyIds.OBJECT_ID + " FROM " + ecmType +
                " WHERE " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + ecmType + "'";
        try {
            final Iterator<QueryResult> iterator = session.createQueryStatement(query).query().iterator();
            if (iterator.hasNext()) {
                final String objectId = iterator.next().getPropertyValueById(PropertyIds.OBJECT_ID);
                return (Folder) session.getObject(objectId);
            }
            throw new SystemException("La estructura no contiene Unidad Base Content");
        } catch (Exception ex) {
            log.error("Error: {}", ex.getMessage());
            throw new SystemException("Error: " + ex.getMessage());
        }
    }

    @Override
    public Folder findUdByIdAndDepCode(String idUd) throws SystemException {
        if (StringUtils.isEmpty(idUd)) {
            throw new SystemException("No se ha especificado el Id de la Unidad Documental");
        }

        final Session session = getSession();
        final String query = "SELECT " + PropertyIds.OBJECT_ID + " FROM cmcor:CM_Unidad_Documental" +
                " WHERE " + ConstantesECM.CMCOR_UD_ID + " = '" + idUd + "'";
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        final Iterator<QueryResult> iterator = queryResults.iterator();

        if (queryResults.getPageNumItems() > 1) {
            final StringBuilder builder = new StringBuilder();
            while (iterator.hasNext()) {
                final String objectId = iterator.next().getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                final Folder folder = (Folder) session.getObject(objectId);
                builder.append("UD NAME: ").append(folder.getName()).append(", UD PARENT: ").append(folder.getFolderParent().getName());
            }
            throw new SystemException("Existe mas de una UD con el mismo ID: \n" + builder.toString());
        }

        if (iterator.hasNext()) {
            final String objectId = iterator.next().getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            return (Folder) session.getObject(objectId);
        }
        return null;
    }

    @Override
    public List<Folder> findAllUDListBy(UnidadDocumentalDTO dto) throws SystemException {
        String query = "SELECT " + PropertyIds.OBJECT_ID + " FROM cmcor:CM_Unidad_Documental" +
                " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + dto.getCodigoDependencia() + "'";
        if (!StringUtils.isEmpty(dto.getCodigoSerie()) && !"".equals(dto.getCodigoSerie())) {
            query += " AND " + ConstantesECM.CMCOR_SER_CODIGO + " = '" + dto.getCodigoSerie() + "'";
        }
        if (!StringUtils.isEmpty(dto.getCodigoSubSerie()) && !"".equals(dto.getCodigoSubSerie())) {
            query += " AND " + ConstantesECM.CMCOR_SS_CODIGO + " = '" + dto.getCodigoSubSerie() + "'";
        }

        final List<Folder> udList = super.findAllUDListBy(dto);
        final Session session = getSession();

        for (QueryResult queryResult : session.query(query, false)) {
            final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            final Folder folder = (Folder) session.getObject(objectId);
            updateUdSecurityGroup(folder);
            if (validProperties(folder, dto)) {
                udList.add(udList.size(), folder);
            }
        }
        return udList;
    }

    @Override
    void changeNode(Folder folder, OrganigramaDTO organigrama) throws SystemException {
        super.changeNode(folder, organigrama);
        if (folder.getType().getId().endsWith("CM_Unidad_Base")) {
            return;
        }
        final Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put(ConstantesECM.CMCOR_DEP_ORG_ACTIVO, true);
        if (organigrama.isRadicadora()) {
            propertyMap.put(ConstantesECM.CMCOR_DEP_RADICADORA, true);
        }
        folder.updateProperties(propertyMap);
    }

    @Override
    void crearDependencias(List<OrganigramaDTO> organigramaList, Map<String, Folder> folderMap) throws SystemException {
        Folder folderFather = null;
        final Map<String, Object> propertyMap = new HashMap<>();
        for (OrganigramaDTO organigrama : organigramaList) {

            final String codOrg = organigrama.getCodOrg().trim();
            final boolean esUnidadBase = "P".equalsIgnoreCase(organigrama.getTipo());

            if (!folderMap.containsKey(codOrg)) {
                Folder organigramaFolder = getOrganigrama(organigrama, esUnidadBase);
                if (organigramaFolder == null) {

                    propertyMap.put(PropertyIds.NAME, organigrama.getNomOrg());
                    propertyMap.put(ConstantesECM.CMCOR_UB_CODIGO, codOrg);
                    propertyMap.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(ConstantesECM.CLASE_BASE));
                    propertyMap.put(PropertyIds.OBJECT_TYPE_ID, "F:" + ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_BASE));

                    if (!esUnidadBase) {
                        propertyMap.put(ConstantesECM.CMCOR_DEP_CODIGO, codOrg);
                        if (folderFather != null)
                            propertyMap.put(ConstantesECM.CMCOR_UB_CODIGO, folderFather.getPropertyValue(ConstantesECM.CMCOR_UB_CODIGO));
                        propertyMap.put(ConstantesECM.CMCOR_DEP_RADICADORA, organigrama.isRadicadora());
                        propertyMap.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(ConstantesECM.CLASE_DEPENDENCIA));
                        propertyMap.put(PropertyIds.OBJECT_TYPE_ID, "F:" + ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_DEPENDENCIA));
                        organigramaFolder = crearNodo(propertyMap, folderFather);
                    } else {
                        organigramaFolder = crearRootCategory(propertyMap);
                    }
                } else {
                    changeNode(organigramaFolder, organigrama);
                }
                folderMap.put(codOrg, organigramaFolder);
            }
            folderFather = folderMap.get(codOrg);
            propertyMap.clear();
        }
    }

    @Override
    void resetAllEcmDependencies() throws SystemException {
        final Session session = getSession();
        boolean errors = false;
        try {
            final String query = "SELECT " + PropertyIds.OBJECT_ID + " FROM cmcor:CM_Unidad_Administrativa" +
                    " WHERE " + PropertyIds.OBJECT_TYPE_ID + " <> 'F:cmcor:CM_Unidad_Documental'" +
                    " AND (" + ConstantesECM.CMCOR_DEP_RADICADORA + " = true" +
                    " OR " + ConstantesECM.CMCOR_DEP_ORG_ACTIVO + " = true)";

            final ItemIterable<QueryResult> queryResults = session.query(query, false);
            final Map<String, Object> propertyMap = new HashMap<>();

            propertyMap.put(ConstantesECM.CMCOR_DEP_RADICADORA, false);
            propertyMap.put(ConstantesECM.CMCOR_DEP_ORG_ACTIVO, false);

            queryResults.forEach(queryResult -> {
                final String objectId = queryResult.getPropertyValueById(PropertyIds.OBJECT_ID);
                final CmisObject cmisObject = session.getObject(objectId);
                log.info("Modifying folder {} --> {}", cmisObject.getName(), propertyMap);
                cmisObject.updateProperties(propertyMap);
            });
        } catch (Exception ex) {
            errors = true;
            log.error("Error Reseting retention folders {}", ex.getMessage());
            throw new SystemException("Error Reseting retention folders " + ex.getMessage());
        } finally {
            log.info("Method resetRadicationFolders has ended with {}", (errors ? "errors" : "no errors"));
            System.out.println("Method resetRadicationFolders has ended with " + (errors ? "errors" : "no errors"));
        }
    }

    @Override
    boolean renameFolder(Folder folder, String newName) {
        final String folderName = folder.getName();
        final String ecmFolderType = folder.getType().getId();
        if (ecmFolderType.endsWith("CM_Unidad_Base") || ecmFolderType.endsWith("CM_Unidad_Administrativa")) {
            if (!StringUtils.endsWithIgnoreCase(folderName, newName)) {
                rename(folder, newName);
                return true;
            }
            return false;
        }
        return super.renameFolder(folder, newName);
    }

    @Override
    Folder getOrganigrama(OrganigramaDTO organigrama, boolean esUnidadBase) throws SystemException {
        try {
            final Session session = getSession();
            final String objectClass = ConstantesECM.CMCOR + (esUnidadBase ?
                    configuracion.getPropiedad(ConstantesECM.CLASE_BASE) : configuracion.getPropiedad(ConstantesECM.CLASE_DEPENDENCIA));
            final String property = esUnidadBase ? ConstantesECM.CMCOR_UB_CODIGO : ConstantesECM.CMCOR_DEP_CODIGO;

            final String queryPrincipal = "SELECT " + PropertyIds.OBJECT_ID + " " +
                    "FROM " + objectClass + " where " + property + " = '" + organigrama.getCodOrg() + "' " +
                    "AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + objectClass + "'";

            final ItemIterable<QueryResult> queryResults = session.query(queryPrincipal, false);
            final Iterator<QueryResult> iterator = queryResults.iterator();

            if (iterator.hasNext()) {
                final QueryResult next = iterator.next();
                String objectId = next.getPropertyValueById(PropertyIds.OBJECT_ID);
                return (Folder) session.getObject(objectId);
            }
            return null;
        } catch (Exception ex) {
            log.error("An error has occurred {}", ex);
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }

    @Override
    Folder crearRootCategory(Map<String, Object> propertyMap) throws SystemException {
        final Session session = getSession();
        return session.getRootFolder().createFolder(propertyMap);
    }

    @Override
    Folder crearSerie(Folder folderFather, ContenidoDependenciaTrdDTO trdDTO) throws SystemException {
        final Map<String, Object> propertyMap = getPropertyMap(folderFather, trdDTO, true);
        return crearNodo(propertyMap, folderFather);
    }

    @Override
    Folder crearSubSerie(Folder folderFather, ContenidoDependenciaTrdDTO trdDTO) throws SystemException {
        final Map<String, Object> propertyMap = getPropertyMap(folderFather, trdDTO, false);
        return crearNodo(propertyMap, folderFather);
    }

    @Override
    Folder getSerieSubSerie(ContenidoDependenciaTrdDTO trdDTO, boolean esSerie) throws SystemException {

        final Session session = getSession();
        final String ssCode = trdDTO.getCodSubSerie();
        final String ecmClass = esSerie ? ConstantesECM.CLASE_SERIE : ConstantesECM.CLASE_SUBSERIE;
        final String objectClass = ConstantesECM.CMCOR + configuracion.getPropiedad(ecmClass);

        String query = "SELECT " + PropertyIds.OBJECT_ID + " FROM " + objectClass + " " +
                "WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + trdDTO.getIdOrgOfc() + "' " +
                "AND " + ConstantesECM.CMCOR_SER_CODIGO + " = '" + trdDTO.getCodSerie() + "' " +
                "AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + objectClass + "'";
        if (!esSerie && !StringUtils.isEmpty(ssCode)) {
            query += " AND " + ConstantesECM.CMCOR_SS_CODIGO + " = '" + ssCode + "'";
        }

        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        final Iterator<QueryResult> iterator = queryResults.iterator();

        if (iterator.hasNext()) {
            final String objectId = iterator.next().getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            return (Folder) session.getObject(objectId);
        }
        return null;
    }

    @Override
    Folder crearNodo(Map<String, Object> propertyMap, Folder folderFather) throws SystemException {
        try {
            final String currentTypeId = (String) propertyMap.get(PropertyIds.OBJECT_TYPE_ID);
            final String fatherTypeId = folderFather.getType().getId();
            propertyMap.put(ConstantesECM.CMCOR_DEP_CODIGO_PADRE, folderFather.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO_PADRE));
            if (currentTypeId.endsWith("CM_Unidad_Administrativa")) {
                if (fatherTypeId.endsWith(configuracion.getPropiedad(ConstantesECM.CLASE_BASE))) {
                    propertyMap.put(ConstantesECM.CMCOR_DEP_CODIGO_PADRE, folderFather.getPropertyValue(ConstantesECM.CMCOR_UB_CODIGO));
                } else {
                    propertyMap.put(ConstantesECM.CMCOR_DEP_CODIGO_PADRE, folderFather.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));
                }
            }
            return folderFather.createFolder(propertyMap);
        } catch (Exception ex) {
            log.error("An error has occurred: {}", ex);
            throw new SystemException(ex.getMessage());
        }
    }

    @Override
    Map<String, Object> getPropertyMap(Folder folderFather, ContenidoDependenciaTrdDTO trdDTO, boolean esSerie) throws SystemException {

        final Map<String, Object> propertyMap = super.getPropertyMap(folderFather, trdDTO, esSerie);
        log.info("Disposition: {}" + propertyMap.remove(DISP_VALUE));

        final String depCode = folderFather.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);
        final SecurityGroupType groupType = SecurityGroupType.getSecurityBy(trdDTO.getGrupoSeguridad());

        propertyMap.put(ConstantesECM.CMCOR_SER_CODIGO, trdDTO.getCodSerie());
        propertyMap.put(ConstantesECM.CMCOR_DEP_ORG_ACTIVO, true);
        propertyMap.put(ConstantesECM.CMCOR_DEP_RADICADORA, trdDTO.isRadicadora());
        propertyMap.put(ConstantesECM.CMCOR_DEP_CODIGO, depCode);
        propertyMap.put(ConstantesECM.CMCOR_UB_CODIGO, folderFather.getPropertyValue(ConstantesECM.CMCOR_UB_CODIGO));

        trdDTO.setGrupoSeguridad(groupType == null ? trdDTO.getGrupoSeguridad() : groupType.getSecurityName());
        propertyMap.put(ConstantesECM.CMCOR_SER_SECURITY_GROUP, trdDTO.getGrupoSeguridad());
        propertyMap.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(ConstantesECM.CLASE_SERIE));
        propertyMap.put(PropertyIds.OBJECT_TYPE_ID, "F:" + ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_SERIE));

        if (!esSerie) {
            propertyMap.put(ConstantesECM.CMCOR_SS_CODIGO, trdDTO.getCodSubSerie());
            propertyMap.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(ConstantesECM.CLASE_SUBSERIE));
            propertyMap.put(PropertyIds.OBJECT_TYPE_ID, "F:" + ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_SUBSERIE));
        }
        return propertyMap;
    }

    @Override
    void changedNode(Folder folder, ContenidoDependenciaTrdDTO trdDTO, boolean esSerie) throws SystemException {

        final Map<String, Object> propertyMap = new HashMap<>();
        final String groupString = trdDTO.getGrupoSeguridad();
        final SecurityGroupType groupType = SecurityGroupType.getSecurityBy(groupString);

        propertyMap.put(ConstantesECM.CMCOR_SER_SECURITY_GROUP, groupType == null ? groupString : groupType.getSecurityName());
        propertyMap.put(ConstantesECM.CMCOR_DEP_RADICADORA, trdDTO.isRadicadora());
        propertyMap.put(ConstantesECM.CMCOR_DEP_ORG_ACTIVO, true);

        folder.updateProperties(propertyMap);
        super.changedNode(folder, trdDTO, esSerie);
    }

    @Override
    boolean isSerieLeaf(Folder folder) {
        final ItemIterable<CmisObject> children = folder.getChildren();
        for (CmisObject cmisObject : children) {
            final String ecmType = cmisObject.getType().getId();
            if (ecmType.endsWith("CM_Subserie")
                    && (Boolean) cmisObject.getPropertyValue(ConstantesECM.CMCOR_DEP_ORG_ACTIVO)) {
                return false;
            }
        }
        return true;
    }

    @Override
    boolean validProperties(Folder folder, UnidadDocumentalDTO dto) {
        if (folder.getType().getId().endsWith("CM_Unidad_Documental")) {

            final Boolean isClosedDto = dto.getCerrada();
            final boolean isClosed = folder.getPropertyValue(ConstantesECM.CMCOR_UD_CERRADA);

            if (isClosedDto != null && isClosed == isClosedDto) {
                return true;
            }

            final String id = dto.getId();
            final String desc1 = dto.getDescriptor1();
            final String desc2 = dto.getDescriptor2();
            final String name = dto.getNombreUnidadDocumental();

            final String ecmId = folder.getPropertyValue(ConstantesECM.CMCOR_UD_ID);
            final String ecmDes1 = folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_1);
            final String ecmDes2 = folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_2);
            final String ecmName = folder.getName();

            if (!StringUtils.isEmpty(id) && !StringUtils.containsIgnoreCase(ecmId, id) ||
                    !StringUtils.isEmpty(desc1) && !StringUtils.containsIgnoreCase(ecmDes1, desc1) ||
                    !StringUtils.isEmpty(desc2) && !StringUtils.containsIgnoreCase(ecmDes2, desc2) ||
                    !StringUtils.isEmpty(name) && !StringUtils.containsIgnoreCase(ecmName, name)) {
                return false;
            }

            final AccionUsuario accionUsuario = AccionUsuario.getActionBy(dto.getAccion());
            final boolean isInactive = folder.getPropertyValue(ConstantesECM.CMCOR_UD_INACTIVO);
            final Calendar calendar = folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_INICIAL);

            final boolean isToAdd = !isClosed && !isInactive;

            if (accionUsuario == null && isToAdd) {
                return true;
            } else if (calendar != null) {
                if (accionUsuario == AccionUsuario.ABRIR || accionUsuario == AccionUsuario.REACTIVAR) {
                    final Calendar endDate = folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_FINAL);
                    final Calendar closingDate = folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_CIERRE);
                    return isClosed && isInactive && endDate != null && closingDate != null;
                }
                return isToAdd;
            }
        }
        return false;
    }
}
