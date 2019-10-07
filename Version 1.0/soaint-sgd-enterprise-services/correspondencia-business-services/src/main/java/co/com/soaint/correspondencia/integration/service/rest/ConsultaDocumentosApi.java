package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarConsultaDocumentos;
import co.com.soaint.foundation.canonical.correspondencia.ModeloConsultaDocumentoDTO;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 11-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/consulta-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
@Api(value = "ConsultaDocumentosWebApi")
public class ConsultaDocumentosApi {

    @Autowired
    GestionarConsultaDocumentos boundary;

    /**
     * Constructor
     */
    public ConsultaDocumentosApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/consulta/")
    public List<ModeloConsultaDocumentoDTO> consultarDocumentos(@QueryParam("userLogin") final String userLogin,
                                                                @QueryParam("fecha_ini") final String startDateString,
                                                                @QueryParam("fecha_fin") final String endDateString,
                                                                @QueryParam("tipo_comunicacion") final String tipoComunicacion,
                                                                @QueryParam("nro_radicado") final String nroRadicado,
                                                                @QueryParam("nro_identificacion") final String nroIdentificacion,
                                                                @QueryParam("dep_origen") final String depOrigen,
                                                                @QueryParam("dep_destino") final String depDestino,
                                                                @QueryParam("nro_guia") final String nroGuia,
                                                                @QueryParam("asunto") final String asunto,
                                                                @QueryParam("solicitante") final String solicitante,
                                                                @QueryParam("destinatario") final String destinatario,
                                                                @QueryParam("tipoDocumento") final String tipoDocumento,
                                                                @QueryParam("tipologiaDocumental") final String tipologiaDocumental) {
        try {
            Date startDate = null;
            if (!StringUtils.isEmpty(startDateString)) {
                final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                startDate = df.parse(startDateString);
            }
            Date endDate = null;
            if (!StringUtils.isEmpty(endDateString)) {
                final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                endDate = df.parse(endDateString);
            }

            return boundary.consultarCorrespondencias(userLogin, startDate, endDate, tipoComunicacion, nroRadicado,
                    nroIdentificacion, depOrigen, depDestino, nroGuia, asunto, solicitante, destinatario, tipoDocumento, tipologiaDocumental);
        } catch (Exception e) {
            log.error(e);
            return new ArrayList<>();
        }
    }
}
