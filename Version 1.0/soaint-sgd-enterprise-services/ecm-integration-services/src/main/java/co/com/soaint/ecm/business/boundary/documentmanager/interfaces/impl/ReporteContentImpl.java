package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.KpiContent;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ReporteContent;
import co.com.soaint.ecm.domain.entity.AccionUsuario;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Getter
@Service("reporteContent")
public class ReporteContentImpl implements ReporteContent {

    private static final Long serialVersionUID = 84654654612L;
    @Autowired
    private ContentControl contentControl;
    @Autowired
    private KpiContent kpiContent;

    private final String CORRESPONDENCIA_ENDPOINT = SystemParameters
            .getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT_CORRESPONDENCIA);

    private final String PATH_REP_7 = "/reporte-web-api/processRep7";
    private final String PATH_REP_8 = "/reporte-web-api/processRep8";
    private final String PATH_REP_9 = "/reporte-web-api/processRep9";

    @Override
    public void processReporte7(String dependencyCode) {
        Thread thread = new Thread(() -> {
            try {
                final List<DocumentoDTO> documentosArchivados = contentControl.obtenerDocumentosArchivadosList(dependencyCode);
                kpiContent.processKpi11(documentosArchivados, dependencyCode);
                final WebTarget wt = ClientBuilder.newClient().target(CORRESPONDENCIA_ENDPOINT);
                final Response response = wt
                        .path(PATH_REP_7)
                        .request()
                        .post(Entity.json(documentosArchivados));
                final int responseStatus = response.getStatus();
                if (responseStatus != HttpURLConnection.HTTP_CREATED && responseStatus != HttpURLConnection.HTTP_OK) {
                    throw new SystemException("An error has occurred in processKpi11 statusInfo: ´" + response.getStatusInfo().getReasonPhrase() + "´");
                }
            } catch (Exception ex) {
                log.error("An error has occurred in processKpi11 statusInfo: {}", ex.getMessage());
//                throw new SystemException("An error has occurred in processKpi11 statusInfo: ´" + ex.getMessage() + "´");
            }
        });
        thread.start();
    }

    @Override
    public void processReporte8(String dependencyCode) {
        Thread thread = new Thread(() -> {
            try {
                final UnidadDocumentalDTO documentalDTO = new UnidadDocumentalDTO();
                documentalDTO.setCerrada(false);
                documentalDTO.setCodigoDependencia(dependencyCode);
                List<UnidadDocumentalDTO> documentalDTOS = contentControl.listarUnidadesDocumentales(documentalDTO);
                documentalDTO.setCerrada(true);
                documentalDTOS.addAll(contentControl.listarUnidadesDocumentales(documentalDTO));
                List<UnidadDocumentalDTO> collect = documentalDTOS.stream().peek(unidadDocumentalDTO -> {
                    final String accion = unidadDocumentalDTO.getAccion();
                    final boolean closed = unidadDocumentalDTO.getCerrada();

                    if (closed) {
                        unidadDocumentalDTO.setAccion(AccionUsuario.CERRAR.getState());
                    } else if (StringUtils.isEmpty(accion) || accion.equalsIgnoreCase("abrir")) {
                        unidadDocumentalDTO.setAccion(AccionUsuario.ABRIR.getState());
                    }
                }).collect(Collectors.toList());
                final WebTarget wt = ClientBuilder.newClient().target(CORRESPONDENCIA_ENDPOINT);
                final Response response = wt
                        .path(PATH_REP_8)
                        .request()
                        .post(Entity.json(collect));
                final int responseStatus = response.getStatus();
                if (responseStatus != HttpURLConnection.HTTP_CREATED && responseStatus != HttpURLConnection.HTTP_OK) {
                    throw new SystemException("An error has occurred in processKpi11 statusInfo: ´" + response.getStatusInfo().getReasonPhrase() + "´");
                }
            } catch (Exception ex) {
                log.error("An Error has occurred {}", ex.getMessage());
//                throw new SystemException("An Error has occurred " + ex.getMessage());
            }
        });
        thread.start();
    }

    @Override
    public void processReporte9(String dependencyCode) {
        log.info("Implement {}", PATH_REP_9);
    }
}
