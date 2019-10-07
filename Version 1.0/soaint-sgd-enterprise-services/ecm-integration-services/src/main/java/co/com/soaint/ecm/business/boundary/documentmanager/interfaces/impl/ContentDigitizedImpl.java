package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentDigitized;
import co.com.soaint.ecm.domain.entity.CommunicationType;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaDigitalizarDTO;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Getter
@Service("contentDigitized")
public final class ContentDigitizedImpl implements ContentDigitized {

    private static final long serialVersionUID = 133L;

    private final String CORRESPONDENCIA_ENDPOINT = SystemParameters
            .getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT_CORRESPONDENCIA);

    private final String BPM_ENDPOINT = SystemParameters
            .getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT_BPM);

    private final String folderName;

    @Autowired
    @Qualifier("contentControlAlfresco")
    private ContentControl contentControl;


    public ContentDigitizedImpl(@Value("${digitized.folder.name}") String folderName) {
        this.folderName = folderName;
    }

    @Override
    public void processDigitalizedDocuments() throws SystemException {
        log.info("Buscando documentos fisicos digitalizados en la carpeta {}", folderName);
        final Folder digitalizedFolder = getDigitalizedFolder();
        ItemIterable<CmisObject> children = digitalizedFolder.getChildren();
        final Map<String, List<Document>> dtoMap = new HashMap<>();
        for (CmisObject cmisObject :
                children) {
            if (cmisObject instanceof Document && cmisObject.getType().getId().startsWith("D:cmcor:CM")) {
                Document document = (Document) cmisObject;
                final String nroRadicado = document.getPropertyValue(ConstantesECM.CMCOR_DOC_RADICADO);
                if (!StringUtils.isEmpty(nroRadicado)) {
                    if (!dtoMap.containsKey(nroRadicado)) {
                        dtoMap.put(nroRadicado, new ArrayList<>());
                    }
                    final List<Document> dtoList = dtoMap.get(nroRadicado);
                    dtoList.add(dtoList.size(), document);
                }
            }
        }
        for (String nroRadicado :
                dtoMap.keySet()) {
            executeProcess(dtoMap.get(nroRadicado));
        }
    }

    private void executeProcess(List<Document> documentList) throws SystemException {
        Document mainDocument = null;
        final List<Document> tmpDtoList = new ArrayList<>();
        for (Document document :
                documentList) {
            final String docType = document.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO);
            if (!StringUtils.isEmpty(docType)
                    && "principal".equals(docType.toLowerCase())) {
                mainDocument = document;
                continue;
            }
            tmpDtoList.add(tmpDtoList.size(), document);
        }
        if (null == mainDocument) {
            return;
        }
        DocumentoDTO principalDTO = contentControl.transformarDocumento(mainDocument, true);
        final String nroRadicado = principalDTO.getNroRadicado();
        final CorrespondenciaDTO correspondenciaDTO = getCorrespondenciaDTO(nroRadicado);
        final String dependencyCode = correspondenciaDTO.getCodDependencia();
        if (StringUtils.isEmpty(dependencyCode)) {
            throw new SystemException("El codigo dependencia de la correspondencia es nulo");
        }
        principalDTO.setCodigoDependencia(dependencyCode);
        principalDTO.setLabelRequired(false);
        final MensajeRespuesta mensajeRespuesta = contentControl
                .subirDocumentoPrincipalAdjunto(principalDTO, null);
        principalDTO = mensajeRespuesta.getDocumentoDTOList().get(0);
        final String idDocPrincipal = principalDTO.getIdDocumento();
        mainDocument.delete();
        for (Document document :
                tmpDtoList) {
            DocumentoDTO dto = contentControl.transformarDocumento(document, true);
            dto.setCodigoDependencia(dependencyCode);
            dto.setIdDocumentoPadre(idDocPrincipal);
            dto.setLabelRequired(false);
            contentControl
                    .subirDocumentoPrincipalAdjunto(dto, null);
            document.delete();
        }
        final String idInstancia = correspondenciaDTO.getIdeInstancia();
        if (StringUtils.isEmpty(idInstancia)) {
            throw new SystemException("El ID instancia de la correspondencia es nulo");
        }
        notifyBpmProcess(nroRadicado, idInstancia, idDocPrincipal);
    }

    private void notifyBpmProcess(String nroRadicado, String ideInstancia, String ideEcm) throws SystemException {

        final RespuestaDigitalizarDTO digitalizarDTO = getRespuestaDigitalizarDTO(ideInstancia);
        final Map<String, Object> parameters = new HashMap<>();
        final CommunicationType communicationType = CommunicationType.getSelectorBy(nroRadicado);

        parameters.put("nombreSennal", digitalizarDTO.getNombreSennal());
        if (null != communicationType && communicationType == CommunicationType.EE) {
            parameters.put("ideEcm", ideEcm);
        }

        final EntradaProcesoDTO procesoDTO = EntradaProcesoDTO.newInstance()
                .idDespliegue(digitalizarDTO.getIdDespliegue())
                .idProceso(digitalizarDTO.getIdProceso())
                .instanciaProceso(Long.parseLong(ideInstancia))
                .parametros(parameters)
                .build();

        final Response response = iniciarProceso(procesoDTO);
        if (response.getStatus() != 200) {
            throw new SystemException("Ocurrio un error al inicial el proceso con id instancia: " + ideInstancia
                    + " Response Status: " + response.getStatus());
        }
    }

    private CorrespondenciaDTO getCorrespondenciaDTO(String nroRadicado) throws SystemException {
        WebTarget wt = ClientBuilder.newClient().target(CORRESPONDENCIA_ENDPOINT);
        Response response = wt
                .path("/correspondencia-web-api/correspondencia/obtener-ide-instancia-dependencia-radicado/" + nroRadicado)
                .request()
                .get();
        if (response.getStatus() != 200) {
            throw new SystemException("Ocurrio un error al consumir" +
                    " servicio correspondencia, codigo status: " + response.getStatus());
        }
        return response.readEntity(CorrespondenciaDTO.class);
    }

    private RespuestaDigitalizarDTO getRespuestaDigitalizarDTO(String ideInstancia) throws SystemException {
        WebTarget wt = ClientBuilder.newClient().target(BPM_ENDPOINT);
        Response response = wt.path("/bpm/proceso/obtener/" + ideInstancia)
                .request()
                .get();
        if (response.getStatus() != 200) {
            throw new SystemException("Ocurrio un error al consumir" +
                    " servicio BPM, codigo status: " + response.getStatus());
        }
        return response.readEntity(RespuestaDigitalizarDTO.class);
    }

    private Response iniciarProceso(EntradaProcesoDTO entradaProcesoDTO) {
        WebTarget wt = ClientBuilder.newClient().target(BPM_ENDPOINT);
        return wt.path("/bpm/proceso/sennal")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    private Folder getDigitalizedFolder() throws SystemException {
        Folder digitalizedFolder = contentControl.getFolderFromRootByName(folderName);
        if (null == digitalizedFolder) {
            Session session = contentControl.getSession();
            final Map<String, Object> properties = new HashMap<>();
            properties.put(PropertyIds.NAME, folderName);
            properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
            properties.put(PropertyIds.DESCRIPTION, "Almacena los documentos fisicos digitalizados");
            digitalizedFolder = session.getRootFolder().createFolder(properties);
        }
        return digitalizedFolder;
    }
}