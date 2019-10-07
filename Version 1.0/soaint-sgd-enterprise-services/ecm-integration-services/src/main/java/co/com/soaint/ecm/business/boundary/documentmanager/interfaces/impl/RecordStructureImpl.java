package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.Connection;
import co.com.soaint.ecm.domain.entity.DiposicionFinalEnum;
import co.com.soaint.ecm.domain.entity.EcmRecordObjectType;
import co.com.soaint.ecm.domain.entity.FinalDispositionType;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.*;

@Log4j2
@Service("recordStructure")
public class RecordStructureImpl extends AbstractStructure {

    private static final Long serialVersionUID = -5464634613054646L;

    @Value("${protocolo}")
    private String protocolo = "";
    @Value("${mensaje.error.sistema}")
    private String errorSistema = "";
    @Value("${mensaje.error.sistema.generico}")
    private String errorSistemaGenerico = "";
    @Value("${header.accept}")
    private String headerAccept = "";
    @Value("${header.authorization}")
    private String headerAuthorization = "";
    @Value("${header.value.application.type}")
    private String valueApplicationType = "";
    @Value("${header.value.authorization}")
    private String valueAuthorization = "";
    @Value("${mensaje.error.negocio.fallo}")
    private String errorNegocioFallo = "";
    @Value("${nodeType}")
    private String tipoNodo = "";
    @Value("${recordCategory}")
    private String recordCategoria = "";
    @Value("${tag.propiedades}")
    private String tagPropiedades = "";
    @Value("${recordCarpeta}")
    private String recordCarpeta = "";
    @Value("${id.sitio.record.manager}")
    private String idRecordManager = "";
    @Value("${api.service.alfresco}")
    private String apiServiceAlfresco = "";
    @Value("${tag.nombre}")
    private String nombre = "";
    @Value("${tag.descripcion}")
    private String descripcion = "";
    @Value("${tag.periodo}")
    private String periodo = "";
    @Value("${tag.localizacion}")
    private String localizacion = "";
    @Value("${tag.propiedad.periodo}")
    private String propiedadPeriodo = "";
    @Value("${valor.periodo}")
    private String valorPeriodo = "";
    @Value("${valor.mensaje.descripcion}")
    private String mensajeDescripcion = "";
    //@Value("${tokken.record}")
    private String encoding;

    @Autowired
    @Qualifier("connection")
    private Connection connection;

    @Autowired
    @Qualifier("recordServices")
    private RecordServices recordServices;

    @PostConstruct
    public void init() {
        setConnection(connection);
        encoding = SystemParameters.encodeAlfrescCredenciales();
    }

    @Override
    public MensajeRespuesta crearEstructura(List<EstructuraTrdDTO> structure) throws SystemException {
        final MensajeRespuesta respuesta = super.crearEstructura(structure);
        this.addRetentionSerieLeaf();
        return respuesta;
    }

    @Override
    public Folder getDepFolderByCode(String depCode) throws SystemException {
        final Session session = getSession();
        final String queryString = "SELECT " + PropertyIds.OBJECT_ID + " FROM rmc:rmarecordCategoryCustomProperties " +
                "WHERE " + ConstantesECM.RMC_X_SECCION + " = '" + depCode + "' " +
                "AND (" + ConstantesECM.RMC_X_COD_SERIE + " IS NULL OR " + ConstantesECM.RMC_X_COD_SERIE + " = '') " +
                "AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:rma:recordCategory'";
        final ItemIterable<QueryResult> queryResults = session.query(queryString, false);
        final Iterator<QueryResult> iterator = queryResults.iterator();
        if (iterator.hasNext()) {
            final String objectId = iterator.next().getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            return (Folder) session.getObject(objectId);
        }
        throw new SystemException("Dependency Folder not found in Record with Code " + depCode);
    }

    @Override
    public Folder getBaseFolder() throws SystemException {
        final Session session = getSession();
        final String queryPrincipal = "SELECT * FROM rmc:rmarecordCategoryCustomProperties" +
                " WHERE (" + ConstantesECM.RMC_X_FONDO + " IS NOT NULL OR " + ConstantesECM.RMC_X_FONDO + " <> '')" +
                " AND (" + ConstantesECM.RMC_X_SECCION + " IS NULL OR " + ConstantesECM.RMC_X_SECCION + " = '')" +
                " AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:rma:recordCategory'";
        try {
            final Iterator<QueryResult> iterator = session.createQueryStatement(queryPrincipal).query().iterator();
            if (iterator.hasNext()) {
                final String objectId = iterator.next().getPropertyValueById(PropertyIds.OBJECT_ID);
                return (Folder) session.getObject(objectId);
            }
            throw new SystemException("La estructura no contiene Unidad Base Record");
        } catch (Exception ex) {
            log.error("Error: {}", ex.getMessage());
            throw new SystemException("Error: " + ex.getMessage());
        }
    }

    @Override
    public Folder findUdByIdAndDepCode(String idUd) throws SystemException {
        return null;
    }

    @Override
    public List<Folder> findAllUDListBy(UnidadDocumentalDTO dto) throws SystemException {
        String query = "SELECT " + PropertyIds.OBJECT_ID + " FROM rmc:rmarecordCategoryCustomProperties" +
                " WHERE " + ConstantesECM.RMC_X_SECCION + " = '" + dto.getCodigoDependencia() + "'";
        if (!StringUtils.isEmpty(dto.getCodigoSerie()) && !"".equals(dto.getCodigoSerie())) {
            query += " AND " + ConstantesECM.RMC_X_COD_SERIE + " = '" + dto.getCodigoSerie() + "'";
        }
        if (!StringUtils.isEmpty(dto.getCodigoSubSerie()) && !"".equals(dto.getCodigoSubSerie())) {
            query += " AND " + ConstantesECM.RMC_X_COD_SUB_SERIE + " = '" + dto.getCodigoSubSerie() + "'";
        }

        final List<Folder> udList = super.findAllUDListBy(dto);
        final Session session = getSession();
        final ItemIterable<QueryResult> queryResults = session.query(query, false);

        for (QueryResult queryResult : queryResults) {
            final String objectIdCategory = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            final Folder folderCategory = (Folder) session.getObject(objectIdCategory);
            for (CmisObject child : folderCategory.getChildren()) {
                final String id = child.getType().getId();
                if (id.endsWith("rma:recordFolder")) {
                    final String objectIdFolder = child.getPropertyValue(PropertyIds.OBJECT_ID);
                    final Folder folder = (Folder) session.getObject(objectIdFolder);
                    if (validProperties(folder, dto)) {
                        udList.add(udList.size(), folder);
                    }
                }
            }


        }
        return udList;
    }

    @Override
    void changeNode(Folder folder, OrganigramaDTO organigrama) throws SystemException {
        super.changeNode(folder, organigrama);
        final Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put(ConstantesECM.RMC_X_ORG_ACTIVO, true);
        final Response response = recordServices.modifyRecord(folder.getId(), propertyMap, EcmRecordObjectType.RECORD_CATEGORY);
        log.info("Modifying record Category {} --> Status Code {}", folder.getName(), response.getStatus());
    }

    @Override
    void crearDependencias(List<OrganigramaDTO> organigramaList, Map<String, Folder> folderMap) throws SystemException {
        Folder folderFather = null;
        final Map<String, Object> propertyMap = new HashMap<>();
        for (OrganigramaDTO organigrama : organigramaList) {

            final boolean isRootCategory = "P".equalsIgnoreCase(organigrama.getTipo());
            final String codOrg = organigrama.getCodOrg().trim();

            if (!folderMap.containsKey(codOrg)) {
                Folder organigramaFolder = getOrganigrama(organigrama, isRootCategory);
                if (organigramaFolder == null) {

                    propertyMap.put(PropertyIds.NAME, organigrama.getNomOrg());
                    propertyMap.put(ConstantesECM.RMC_X_FONDO, codOrg);

                    propertyMap.put(ConstantesECM.RMC_X_CONSECUTIVO_TP_CATEGORIA, "0");
                    propertyMap.put(ConstantesECM.RMC_X_CONSECUTIVO_TS_CATEGORIA, "0");

                    propertyMap.put(ConstantesECM.RMC_X_ORG_ACTIVO, true);

                    if (!isRootCategory) {
                        propertyMap.put(ConstantesECM.RMC_X_SECCION, codOrg);
                        if (folderFather != null)
                            propertyMap.put(ConstantesECM.RMC_X_FONDO, folderFather.getPropertyValue(ConstantesECM.RMC_X_FONDO));
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
            final String query = "SELECT " + PropertyIds.OBJECT_ID + ", " + PropertyIds.NAME +
                    " FROM rmc:rmarecordCategoryCustomProperties" +
                    " WHERE " + ConstantesECM.RMC_X_ORG_ACTIVO + " = true";

            final ItemIterable<QueryResult> queryResults = session.query(query, false);
            final Map<String, Object> propertyMap = new HashMap<>();

            propertyMap.put(ConstantesECM.RMC_X_ORG_ACTIVO, false);

            queryResults.forEach(queryResult -> {
                final String objectId = queryResult.getPropertyValueById(PropertyIds.OBJECT_ID);
                final String objectName = queryResult.getPropertyValueById(PropertyIds.NAME);
                log.info("Modifying folder {} --> {}", objectName, propertyMap);
                final Response response = recordServices.modifyRecord(objectId, propertyMap, EcmRecordObjectType.RECORD_CATEGORY);
                if (response.getStatus() == HttpURLConnection.HTTP_OK) {
                    log.info("Modifying folder {} --> Success", objectName);
                    System.out.println("Modifying folder " + objectName + " Success");
                }
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
        final String serieCode = folder.getPropertyValue(ConstantesECM.RMC_X_COD_SERIE);
        if (StringUtils.isEmpty(serieCode)) {
            if (!StringUtils.endsWithIgnoreCase(folderName, newName)) {
                rename(folder, newName);
                return true;
            }
            return false;
        }
        return super.renameFolder(folder, newName);
    }

    @Override
    Folder getOrganigrama(OrganigramaDTO organigrama, boolean isRootCategory) throws SystemException {
        try {
            final Session session = getSession();
            final String property = isRootCategory ? ConstantesECM.RMC_X_FONDO : ConstantesECM.RMC_X_SECCION;

            String queryPrincipal = "SELECT * FROM rmc:rmarecordCategoryCustomProperties " +
                    "WHERE " + property + " = '" + organigrama.getCodOrg() + "' " +
                    "AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:rma:recordCategory'";

            queryPrincipal += property.equalsIgnoreCase(ConstantesECM.RMC_X_FONDO)
                    ? " AND (" + ConstantesECM.RMC_X_SECCION + " IS NULL OR " + ConstantesECM.RMC_X_SECCION + " = '')"
                    : " AND (" + ConstantesECM.RMC_X_COD_SERIE + " IS NULL OR " + ConstantesECM.RMC_X_COD_SERIE + " = '')";

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
    Folder getSerieSubSerie(ContenidoDependenciaTrdDTO trdDTO, boolean esSerie) throws SystemException {

        final Session session = getSession();

        String query = "SELECT " + PropertyIds.OBJECT_ID + " FROM rmc:rmarecordCategoryCustomProperties " +
                "WHERE " + ConstantesECM.RMC_X_SECCION + " = '" + trdDTO.getIdOrgOfc() + "' " +
                "AND " + ConstantesECM.RMC_X_COD_SERIE + " = '" + trdDTO.getCodSerie() + "' " +
                "AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:rma:recordCategory'";

        if (esSerie) {
            query += " AND (" + ConstantesECM.RMC_X_COD_SUB_SERIE + " IS NULL" +
                    " OR " + ConstantesECM.RMC_X_COD_SUB_SERIE + " = '')";
        } else if (!StringUtils.isEmpty(trdDTO.getCodSubSerie())) {
            query += " AND " + ConstantesECM.RMC_X_COD_SUB_SERIE + " = '" + trdDTO.getCodSubSerie() + "'";
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
    Folder crearRootCategory(Map<String, Object> propertyMap) throws SystemException {
        try {
            log.info("iniciar - Crear categoria padre: {}", propertyMap);
            final JSONObject entrada = getJsonObject(propertyMap);
            final WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter("record-endpoint"));
            final Response response = wt.path("/file-plans/" + obtenerIdFilePlan() + "/categories").request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType).post(Entity.json(entrada.toString()));
            if (response.getStatus() != 201) {
                throw new SystemException(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString());
            } else {
                String nodeId = obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
                log.info("Root Category Node Id {}", nodeId);
                return (Folder) getSession().getObject(nodeId);
            }
        } catch (Exception ex) {
            log.error("Error has occurred {}", ex);
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }

    @Override
    Folder crearNodo(Map<String, Object> propertyMap, Folder folderFather) throws SystemException {
        try {
            final JSONObject entrada = getJsonObject(propertyMap);
            log.info("iniciar - Crear categoria hija: {}", entrada.toString());
            final WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter("record-endpoint"));
            final Response response = wt.path("/record-categories/" + folderFather.getId() + "/children").request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType).post(Entity.json(entrada.toString()));
            if (response.getStatus() != 201) {
                throw new SystemException(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString());
            } else {
                String nodeId = obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
                return (Folder) getSession().getObject(nodeId);
            }
        } catch (Exception ex) {
            log.error("Error has occurred {}", ex);
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }

    @Override
    Map<String, Object> getPropertyMap(Folder folderFather, ContenidoDependenciaTrdDTO trdDTO, boolean esSerie) throws SystemException {

        final Map<String, Object> propertyMap = super.getPropertyMap(folderFather, trdDTO, esSerie);

        propertyMap.put(ConstantesECM.RMC_X_COD_SERIE, trdDTO.getCodSerie());
        propertyMap.put(ConstantesECM.RMC_X_ORG_ACTIVO, true);

        propertyMap.put(ConstantesECM.RMC_X_SECCION, folderFather.getPropertyValue(ConstantesECM.RMC_X_SECCION));
        propertyMap.put(ConstantesECM.RMC_X_FONDO, folderFather.getPropertyValue(ConstantesECM.RMC_X_FONDO));

        propertyMap.put(ConstantesECM.RMC_X_RET_ARCHIVO_CENTRAL, String.valueOf(trdDTO.getRetArchivoCentral()));
        propertyMap.put(ConstantesECM.RMC_X_RET_ARCHIVO_GESTION, String.valueOf(trdDTO.getRetArchivoGestion()));

        propertyMap.put(ConstantesECM.RMC_X_DISPOSICION_FINAL_CATEGORIA, propertyMap.remove(DISP_VALUE));

        return propertyMap;
    }

    @Override
    Folder crearSerie(Folder folderFather, ContenidoDependenciaTrdDTO trdDTO) throws SystemException {

        final Map<String, Object> propertyMap = getPropertyMap(folderFather, trdDTO, true);

        propertyMap.put(ConstantesECM.RMC_X_SERIE, propertyMap.get(PropertyIds.NAME));
        final FinalDispositionType dispCategory = (FinalDispositionType) propertyMap.get(ConstantesECM.RMC_X_DISPOSICION_FINAL_CATEGORIA);
        propertyMap.put(ConstantesECM.RMC_X_DISPOSICION_FINAL_CATEGORIA, dispCategory.getDispositionName());
        final Folder folder = crearNodo(propertyMap, folderFather);
        if (StringUtils.isEmpty(trdDTO.getCodSubSerie())) {
            propertyMap.put("aspectNames", "rma:scheduled");
            propertyMap.put(ConstantesECM.RMC_X_DISPOSICION_FINAL_CATEGORIA, dispCategory);
            createRetentionTime(propertyMap, folder.getId());
        }
        return folder;
    }

    @Override
    Folder crearSubSerie(Folder folderFather, ContenidoDependenciaTrdDTO trdDTO) throws SystemException {

        final Map<String, Object> propertyMap = getPropertyMap(folderFather, trdDTO, false);

        propertyMap.put(ConstantesECM.RMC_X_COD_SUB_SERIE, trdDTO.getCodSubSerie());
        propertyMap.put(ConstantesECM.RMC_X_SUBSERIE, propertyMap.get(PropertyIds.NAME));
        propertyMap.put(ConstantesECM.RMC_X_SERIE, folderFather.getName());
        propertyMap.put("aspectNames", "rma:scheduled");
        final FinalDispositionType dispCategory = (FinalDispositionType) propertyMap.get(ConstantesECM.RMC_X_DISPOSICION_FINAL_CATEGORIA);
        propertyMap.put(ConstantesECM.RMC_X_DISPOSICION_FINAL_CATEGORIA, dispCategory.getDispositionName());

        final Folder subSerieFolder = crearNodo(propertyMap, folderFather);
        propertyMap.put(ConstantesECM.RMC_X_DISPOSICION_FINAL_CATEGORIA, dispCategory);
        createRetentionTime(propertyMap, subSerieFolder.getId());
        return subSerieFolder;
    }

    @Override
    boolean isSerieLeaf(Folder folder) {
        final ItemIterable<CmisObject> children = folder.getChildren();
        for (CmisObject cmisObject : children) {
            final String ecmType = cmisObject.getType().getId();
            if (ecmType.endsWith("recordCategory")
                    && (Boolean) cmisObject.getPropertyValue(ConstantesECM.RMC_X_ORG_ACTIVO)
                    && !StringUtils.isEmpty(cmisObject.getPropertyValue(ConstantesECM.RMC_X_COD_SUB_SERIE))) {
                return false;
            }
        }
        return true;
    }

    @Override
    boolean validProperties(Folder folder, UnidadDocumentalDTO dto) {

        final String ecmId = folder.getPropertyValue(ConstantesECM.RMC_X_IDENTIFICADOR);
        final String ecmName = folder.getName();

        final String dtoName = dto.getNombreUnidadDocumental();
        final String dtoId = dto.getId();

        return (StringUtils.isEmpty(dtoName) || StringUtils.containsIgnoreCase(ecmName, dtoName)) &&
                (StringUtils.isEmpty(dtoId) || StringUtils.containsIgnoreCase(ecmId, dtoId));
    }

    void changedNode(Folder folder, ContenidoDependenciaTrdDTO trdDTO, boolean esSerie) throws SystemException {

        final Map<String, JSONObject> propertyMapRetention = new HashMap<>();
        final Map<String, Object> propertyMapMetadata = new HashMap<>();
        final List<String> retentionList = getRetentionIds(folder.getId());
        final String folderName = getSerieSubSerieName(trdDTO, esSerie);

        if (renameFolder(folder, folderName) && retentionList.size() >= 2) {
            propertyMapRetention.get(retentionList.get(0)).put(localizacion, folderName);
            propertyMapRetention.get(retentionList.get(1)).put(localizacion, folderName);
        }

        retentionList.forEach(s -> propertyMapRetention.put(s, new JSONObject()));

        final String retAgEcm = folder.getPropertyValue(ConstantesECM.RMC_X_RET_ARCHIVO_GESTION);
        final String retAcEcm = folder.getPropertyValue(ConstantesECM.RMC_X_RET_ARCHIVO_CENTRAL);
        final String dispEcm = folder.getPropertyValue(ConstantesECM.RMC_X_DISPOSICION_FINAL_CATEGORIA);
        final FinalDispositionType typeEcm = FinalDispositionType.getDispositionBy(dispEcm);

        final String retAgDto = String.valueOf(trdDTO.getRetArchivoGestion());
        final String retAcDto = String.valueOf(trdDTO.getRetArchivoCentral());
        final FinalDispositionType typeDto = FinalDispositionType.getDispositionBy(String.valueOf(trdDTO.getDiposicionFinal()));

        propertyMapMetadata.put(ConstantesECM.RMC_X_ORG_ACTIVO, true);

        boolean isChanged = false;
        if (!StringUtils.equalsIgnoreCase(retAgDto, retAgEcm) && !retentionList.isEmpty()) {
            isChanged = true;
            propertyMapMetadata.put(ConstantesECM.RMC_X_RET_ARCHIVO_GESTION, retAgDto);
            propertyMapRetention.get(retentionList.get(0)).put(periodo, valorPeriodo.concat(retAgDto));
            propertyMapRetention.get(retentionList.get(0)).put(descripcion, mensajeDescripcion.concat(" ").concat(retAgDto.concat(" años en Archivo Gestion")));
        }

        if (!StringUtils.equalsIgnoreCase(retAcDto, retAcEcm) && retentionList.size() >= 2) {
            isChanged = true;
            propertyMapMetadata.put(ConstantesECM.RMC_X_RET_ARCHIVO_CENTRAL, retAcDto);
            propertyMapRetention.get(retentionList.get(1)).put(periodo, valorPeriodo.concat(retAcDto));
            propertyMapRetention.get(retentionList.get(1)).put(descripcion, mensajeDescripcion.concat(" ").concat(retAcDto.concat(" años en Archivo Central")));
        }

        if (typeDto != typeEcm && retentionList.size() >= 2) {
            final DiposicionFinalEnum diposicionFinalEnum = DiposicionFinalEnum.obtenerClave(String.valueOf(trdDTO.getDiposicionFinal()));
            if (diposicionFinalEnum != null && typeDto != null) {
                isChanged = true;
                propertyMapRetention.get(retentionList.get(1)).put(nombre, diposicionFinalEnum.getNombre());
                propertyMapMetadata.put(ConstantesECM.RMC_X_DISPOSICION_FINAL_CATEGORIA, typeDto.getDispositionName());
            }
        }

        if (isChanged) {
            for (Map.Entry<String, JSONObject> entry : propertyMapRetention.entrySet()) {
                final JSONObject jsonObject = entry.getValue();
                if (jsonObject.length() > 0) {
                    modifyRetentionTime(jsonObject, folder.getId(), entry.getKey());
                }
            }
        }
        final Response response = recordServices.modifyRecord(folder.getId(), propertyMapMetadata, EcmRecordObjectType.RECORD_CATEGORY);
        log.info("Modifying Record Category Status Info: {}", response.getStatusInfo());
    }

    private String obtenerIdFilePlan() throws SystemException {
        log.info("iniciar - obtener id file plan");
        WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.API_CORE_ALFRESCO));
        Response response = wt.path("/sites/" + idRecordManager + "/containers").request().header(headerAuthorization, valueAuthorization + " " + encoding).header(headerAccept, valueApplicationType).get();
        if (response.getStatus() != 200) {
            throw new SystemException("Status: " + response.getStatus() + ", StatusInfo: " + response.getStatusInfo().getReasonPhrase());
        } else {
            return obtenerIdRuta(new JSONObject(response.readEntity(String.class)));
        }
    }

    private String obtenerIdRuta(JSONObject respuestaJson) {
        StringBuilder codigoIdBuilder = new StringBuilder();
        Set<String> stringSet = respuestaJson.keySet();
        stringSet.forEach(s -> {
            if ("list".equalsIgnoreCase(s)) {
                JSONObject valor = respuestaJson.getJSONObject(s);
                JSONArray listaNodosJson = valor.getJSONArray("entries");

                for (int i = 0; i < listaNodosJson.length(); ++i) {
                    JSONObject valorJson = (JSONObject) listaNodosJson.get(i);
                    String codeId = obtenerIdNodo(valorJson);
                    if (!StringUtils.isEmpty(codeId)) {
                        codigoIdBuilder.append(codeId);
                        break;
                    }
                }
            }
        });
        return codigoIdBuilder.toString();
    }

    private String obtenerIdNodo(JSONObject respuestaJson) {
        StringBuilder nodoIdBuilder = new StringBuilder();
        Set<String> stringSet = respuestaJson.keySet();
        stringSet.forEach(s -> {
            if ("entry".equalsIgnoreCase(s)) {
                JSONObject valor1 = respuestaJson.getJSONObject(s);
                if (valor1.getString("folderId").equalsIgnoreCase("documentLibrary")) {
                    nodoIdBuilder.append(valor1.getString("id"));
                }
            }
        });
        return nodoIdBuilder.toString();
    }

    private String obtenerIdPadre(JSONObject respuestaJson) {
        StringBuilder codigoIdBuilder = new StringBuilder();
        respuestaJson.keySet().forEach(s -> {
            if ("entry".equalsIgnoreCase(s)) {
                JSONObject valor = respuestaJson.getJSONObject(s);
                codigoIdBuilder.append(valor.getString("id"));
            }

        });
        return codigoIdBuilder.toString();
    }

    private void modifyRetentionTime(final JSONObject jsonObject, String idPadre, String idRetention) throws SystemException {
        try {
            final WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.API_SERVICE_ALFRESCO));
            final Response response = wt.path("/" + idPadre + "/dispositionschedule/dispositionactiondefinitions/" + idRetention)
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .put(Entity.json(jsonObject.toString()));
            if (response.getStatus() != 200) {
                log.error("" + errorNegocioFallo + response.getStatus() + response.getStatusInfo());
                throw new SystemException("" + errorNegocioFallo + response.getStatus() + response.getStatusInfo());
            }
        } catch (Exception e) {
            log.error("An error has occurred {}", e.getMessage());
            throw new SystemException("An error has occurred " + e.getMessage());
        } finally {
            log.info("fin - modificar tiempos de retencion");
        }
    }

    /**
     * Permite crear los tiempos de retencion asociado a series y subseries
     *
     * @param entrada objeto json con lo paramtros necesarios para crear las los tiempos
     * @param idPadre id del nodo al que se le aplicaran los tiempos de retencion
     */
    private void createPhaseRetention(Map<String, Object> entrada, String idPadre) throws SystemException {
        log.info("iniciar - Crear tiempo retencion: {}", entrada.toString());
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(nombre, entrada.get(nombre));
            jsonObject.put(descripcion, entrada.get(descripcion));
            jsonObject.put(periodo, entrada.get(periodo));
            jsonObject.put(localizacion, entrada.get(localizacion));
            jsonObject.put(propiedadPeriodo, entrada.get(propiedadPeriodo));
            /*//jsonObject.put(eventoCompletar, entrada.get(eventoCompletar));
            //            //JSONArray events = new JSONArray();
            //            //events.put(entrada.get("events"));
            //            //jsonObject.put(evento, events);*/

            final WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.API_SERVICE_ALFRESCO));
            final Response response = wt.path("/" + idPadre + "/dispositionschedule/dispositionactiondefinitions")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(jsonObject.toString()));
            if (response.getStatus() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("" + errorNegocioFallo + response.getStatus() + response.getStatusInfo())
                        .buildBusinessException();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - crear categoria hija ");
        }
    }

    private void createRetentionTime(Map<String, Object> propertyMap, String objectId) throws SystemException {

        final Object ag = propertyMap.get(ConstantesECM.RMC_X_RET_ARCHIVO_GESTION);
        final Object ac = propertyMap.get(ConstantesECM.RMC_X_RET_ARCHIVO_CENTRAL);
        final Object name = propertyMap.get(PropertyIds.NAME);
        final FinalDispositionType dispCategory = (FinalDispositionType) propertyMap.get(ConstantesECM.RMC_X_DISPOSICION_FINAL_CATEGORIA);

        final Map<String, Object> disposicion = new HashMap<>();
        disposicion.put(nombre, DiposicionFinalEnum.RETENER.getNombre());
        disposicion.put(descripcion, mensajeDescripcion.concat(" ").concat(String.valueOf(ag).concat(" año(s) en Archivo Gestion")));
        //disposicion.put(descripcion, "Prueba Gestion");
        //disposicion.put(periodo, "immediately");
        disposicion.put(periodo, valorPeriodo.concat(String.valueOf(ag)));
        disposicion.put(localizacion, name);
        disposicion.put(propiedadPeriodo, "cm:created");
        disposicion.put("automatic", true);
        createPhaseRetention(disposicion, objectId);

        disposicion.put(propiedadPeriodo, "rma:dispositionAsOf");
        //disposicion.put(descripcion, "Prueba Central");
        //disposicion.put(periodo, valorPeriodo.concat(String.valueOf(trdDTO.getRetArchivoGestion())));
        disposicion.put(descripcion, mensajeDescripcion.concat(" ").concat(String.valueOf(ac).concat(" año(s) en Archivo Central")));
        //createRetentionTime(disposicion, objectId);
        final DiposicionFinalEnum diposicionFinalEnum = DiposicionFinalEnum.obtenerClave(dispCategory.getDispositionKey());
        final String xFinalDisposicion = (diposicionFinalEnum != null) ? diposicionFinalEnum.getNombre() : "";
        disposicion.put(nombre, xFinalDisposicion);
        //disposicion.replace(periodo, valorPeriodo.concat(String.valueOf(archivoCentral)));
        disposicion.put(periodo, valorPeriodo.concat(String.valueOf(ac)));
        //disposicion.put(descripcion, trdDTO.getProcedimiento());
        createPhaseRetention(disposicion, objectId);
    }

    private JSONObject getJsonObject(Map<String, Object> propertyMap) {
        final JSONObject entrada = new JSONObject();
        if (propertyMap.containsKey("aspectNames")) {
            entrada.put("aspectNames", propertyMap.remove("aspectNames"));
        }
        entrada.put(nombre, propertyMap.remove(PropertyIds.NAME));
        entrada.put(tipoNodo, recordCategoria);
        entrada.put(tagPropiedades, propertyMap);
        return entrada;
    }

    private Response getRetentionTime(final String fatherId) {
        final WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.API_SERVICE_ALFRESCO));
        return wt.path("/" + fatherId + "/dispositionschedule")
                .request()
                .header(headerAuthorization, valueAuthorization + " " + encoding)
                .header(headerAccept, valueApplicationType)
                .get();
    }

    private List<String> getRetentionIds(final String fatherId) {
        final Response response = getRetentionTime(fatherId);
        List<String> retentionList = new ArrayList<>();
        if (response.getStatus() == 404) {
            return retentionList;
        }
        if (response.getStatus() == 200) {
            JSONObject object = new JSONObject(response.readEntity(String.class));
            object = object.getJSONObject("data");
            final JSONArray actions = object.getJSONArray("actions");
            actions.forEach(o -> {
                if (o instanceof JSONObject) {
                    retentionList.add(retentionList.size(), ((JSONObject) o).getString("id"));
                }
            });
        }
        return retentionList;
    }

    private Collection<? extends Folder> getRecordFoldersFromCategory(Folder folder) {
        List<Folder> folderList = new ArrayList<>();
        final ItemIterable<CmisObject> children = folder.getChildren();
        for (CmisObject cmisObject : children) {
            if (cmisObject.getType().getId().endsWith("recordFolder")) {
                final String idUd = cmisObject.getPropertyValue(ConstantesECM.RMC_X_IDENTIFICADOR);

                if (!StringUtils.isEmpty(idUd) && !idUd.trim().isEmpty())
                    folderList.add(folderList.size(), (Folder) cmisObject);
            }
        }
        return folderList;
    }

    private void addRetentionSerieLeaf() {
        new Thread(() -> {
            try {
                final Session session = getSession();
                final String query = "SELECT " + PropertyIds.OBJECT_ID +
                        " FROM rmc:rmarecordCategoryCustomProperties WHERE " + ConstantesECM.RMC_X_ORG_ACTIVO + " = true" +
                        " AND (" + ConstantesECM.RMC_X_COD_SERIE + " IS NOT NULL AND " + ConstantesECM.RMC_X_COD_SERIE + " <> '')" +
                        " AND (" + ConstantesECM.RMC_X_COD_SUB_SERIE + " IS NULL OR " + ConstantesECM.RMC_X_COD_SUB_SERIE + " = '')";
                for (QueryResult queryResult : session.query(query, false)) {

                    final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                    final Folder serieFolder = (Folder) session.getObject(objectId);
                    final List<String> retentionIds = getRetentionIds(objectId);
                    final FinalDispositionType type = FinalDispositionType
                            .getDispositionBy(serieFolder.getPropertyValue(ConstantesECM.RMC_X_DISPOSICION_FINAL_CATEGORIA));

                    if (retentionIds.isEmpty() && type != null && isSerieLeaf(serieFolder)) {

                        final Map<String, Object> propertyMap = new HashMap<>();

                        final String folderName = serieFolder.getName();
                        final String ag = serieFolder.getPropertyValue(ConstantesECM.RMC_X_RET_ARCHIVO_GESTION);
                        final String ac = serieFolder.getPropertyValue(ConstantesECM.RMC_X_RET_ARCHIVO_CENTRAL);

                        propertyMap.put(PropertyIds.NAME, folderName);
                        propertyMap.put(ConstantesECM.RMC_X_RET_ARCHIVO_GESTION, ag);
                        propertyMap.put(ConstantesECM.RMC_X_RET_ARCHIVO_CENTRAL, ac);
                        propertyMap.put(ConstantesECM.RMC_X_DISPOSICION_FINAL_CATEGORIA, type);

                        createRetentionTime(propertyMap, objectId);
                    }
                }
            } catch (Exception e) {
                log.error("Error: " + e.getMessage());
                System.out.println("Error: " + e.getMessage());
            }
        }).start();
    }
}
