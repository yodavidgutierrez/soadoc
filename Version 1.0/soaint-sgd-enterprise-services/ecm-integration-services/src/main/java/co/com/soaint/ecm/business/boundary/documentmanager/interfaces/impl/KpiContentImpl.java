package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.KpiContent;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.StructureAlfresco;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Getter
@Service("kpiContent")
public class KpiContentImpl implements KpiContent {


    private final String CORRESPONDENCIA_ENDPOINT = SystemParameters
            .getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT_CORRESPONDENCIA);

    private final String PATH_KPI_11 = "/kpi-web-api/processKpi11";

    @Autowired
    @Qualifier("contentStructure")
    private StructureAlfresco structureAlfresco;

    @Override
    public void processKpi11(List<DocumentoDTO> documentosArchivados, String dependencyCode) throws SystemException {
        try {
            //final List<DocumentoDTO> documentosPorArchivar = utilities.getDocumentosPorArchivar(session, dependencyCode);
            //final List<DocumentoDTO> documentosArchivados = utilities.obtenerDocumentosArchivados(session, dependencyCode);
            //final List<DocumentoDTO> documentosProducidos = utilities.getDocumentosProducidos(session, dependencyCode);
            final Map<String, String> responseMap = new HashMap<>();

            /*int producedDocumentsAmount = documentosProducidos.size();

            for (DocumentoDTO documentoDTO :
                    documentosArchivados) {
                String nombreProceso = documentoDTO.getNombreProceso();
                if (!StringUtils.isEmpty(nombreProceso) && ConstantesECM.PRODUCCION_DOCUMENTAL.equalsIgnoreCase(nombreProceso)) {
                    producedDocumentsAmount++;
                }
            }*/

            Folder folderBy = structureAlfresco.getFolderBy(ConstantesECM.CLASE_DEPENDENCIA, ConstantesECM.CMCOR_DEP_CODIGO, dependencyCode);
            String dependencyName = folderBy.getName();

            responseMap.put("dependencyCode", dependencyCode);
            responseMap.put("dependencyName", dependencyName);
            //responseMap.put("producedDocumentsAmount", producedDocumentsAmount + "");
            responseMap.put("filedDocuments", documentosArchivados.size() + "");
            //responseMap.put("unfiledDocuments", documentosPorArchivar.size() + "");
            responseMap.put("unfiledDocuments", "0");

            final WebTarget wt = ClientBuilder.newClient().target(CORRESPONDENCIA_ENDPOINT);
            final Response response = wt
                    .path(PATH_KPI_11)
                    .request()
                    .post(Entity.json(responseMap));
            final int responseStatus = response.getStatus();
            if (responseStatus != HttpURLConnection.HTTP_CREATED && responseStatus != HttpURLConnection.HTTP_OK) {
                throw new SystemException("An error has occurred in processKpi11 statusInfo: ´" + response.getStatusInfo().getReasonPhrase() + "´");
            }
        } catch (Exception ex) {
            log.error("An error has occurred in processKpi11 statusInfo: {}", ex.getMessage());
            throw new SystemException("An error has occurred in processKpi11 statusInfo: ´" + ex.getMessage() + "´");
        }
    }
}
