package co.com.soaint.ecm.integration.service.rs;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.RecordTransfer;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.foundation.canonical.ecm.DisposicionFinalDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Created by amartinez on 24/01/2018.
 */

@Log4j2
@Service
@Path("/record")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecordIntegratioServicesClientRest {
    
    @Autowired
    @Qualifier(value = "recordServices")
    private IRecordServices record;

    @Autowired
    private RecordTransfer recordTransfer;

    @Context
    private UriInfo uriInfo;

    /**
     * Constructor de la clase
     */

    public RecordIntegratioServicesClientRest() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * Crear estructura en el Record
     *
     * @param estructuraTrdDTOS Estructura a crear
     * @return Mensaje de respuesta
     */
    @POST
    @Path("/crearEstructuraRecord/")
    public MensajeRespuesta crearEstructuraContent(List<EstructuraTrdDTO> estructuraTrdDTOS) throws SystemException {
        log.info("processing rest request - Crear Estructura Record");
        try {
            return record.crearEstructuraEcm(estructuraTrdDTOS);
        } catch (RuntimeException e) {
            log.error("Error servicio creando estructura ", e);
            throw e;
        }
    }

    /**
     * Metodo para cerrar una unidad documental
     *
     * @param unidadDocumentalDTO  Obj Unidad Documental
     * @return MensajeRespuesta
     */
    @PUT
    @Path("/abrirCerrarReactivarUnidadDocumentalECM/")
    public MensajeRespuesta gestionarUnidadDocumentalECM(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO) {
        log.info("Ejecutando metodo MensajeRespuesta gestionarUnidadDocumentalECM(UnidadDocumentalDTO idUnidadDocumental)");
        try {
            return record.gestionarUnidadDocumentalECM(unidadDocumentalDTO);
        } catch (Exception e) {
            log.error("Error en operacion - cerrarUnidadDocumentalECM ", e);
            return getSmsErrorResponse(e);
        }
    }

    /**
     * Metodo para cerrar una o varias unidades documentales
     *
     * @param unidadDocumentalDTOS   Lista Unidades Documentales para cerrar
     * @return MensajeRespuesta
     */
    @PUT
    @Path("/abrirCerrarReactivarUnidadesDocumentalesECM/")
    public MensajeRespuesta gestionarUnidadesDocumentalesECM(@RequestBody List<UnidadDocumentalDTO> unidadDocumentalDTOS) {
        log.info("Ejecutando metodo MensajeRespuesta cerrarUnidadesDocumentalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS)");
        try {
            return record.gestionarUnidadesDocumentalesECM(unidadDocumentalDTOS);
        } catch (Exception e) {
            log.error("Error en operacion - cerrarUnidadesDocumentalesECM ", e);
            return getSmsErrorResponse(e);
        }
    }

    /* ************************************
     * Transferencias Documentales Inicio
     * ************************************/

    /**
     * Operacion para devolver Las unidades documentales a Transferir
     *
     * @return MensajeRespuesta
     */
    @GET
    @Path("/listar-unidades-documentales-transferir")
    public MensajeRespuesta listarUnidadesDocumentalesTransferenciaPrimariaECM() {
        log.info("processing rest request - Obtener Unidades Documentales a Transferir ECM");
        try {
            final String tipoTransferencia = uriInfo.getQueryParameters().getFirst("tipoTransferencia");
            final String dependencyCode = uriInfo.getQueryParameters().getFirst("depCode");
            return recordTransfer.listarUnidadesDocumentalesTransferencia(tipoTransferencia, dependencyCode);
        } catch (Exception e) {
            log.error("Error en operacion - listarUnidadesDocumentalesTransferencia ", e);
            return getSmsErrorResponse(e);
        }
    }

    /**
     * Metodo para cerrar una o varias unidades documentales
     *
     * @param unidadDocumentalDTOS   Lista Unidades Documentales para aprobar/rechazar
     * @return MensajeRespuesta
     */
    @PUT
    @Path("/aprobar-rechazar-transferencias/{tipoTransferencia}")
    public MensajeRespuesta aprobarRechazarTransferenciasECM(@RequestBody List<UnidadDocumentalDTO> unidadDocumentalDTOS,
                                                             @PathParam("tipoTransferencia") String tipoTransferencia) {
        log.info("Ejecutando metodo MensajeRespuesta aprobarRechazarTransferenciasECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS)");
        try {
            return recordTransfer.aprobarRechazarTransferencias(unidadDocumentalDTOS, tipoTransferencia);
        } catch (Exception e) {
            log.error("Error en operacion - aprobarRechazarTransferenciasECM {}", e.getMessage());
            return getSmsErrorResponse(e);
        }
    }

    @GET
    @Path("/listar-unidades-documentales-verificar")
    public MensajeRespuesta listarUnidadesDocumentalesVerificarECM() {
        log.info("processing rest request - Verificar Unidades Documentales a Transferir ECM");
        try {
            final String dependencyCode = uriInfo.getQueryParameters().getFirst("depCode");
            final String numTransfer = uriInfo.getQueryParameters().getFirst("numTransfer");
            return recordTransfer.listarUnidadesDocumentalesVerificar(dependencyCode, numTransfer);
        } catch (Exception e) {
            log.error("Error en operacion - verificarUnidadesDocumentalesATransferirECM ", e);
            return getSmsErrorResponse(e);
        }
    }

    /**
     * Metodo para confirmar una o varias unidades documentales
     *
     * @param unidadDocumentalDTOS   Lista Unidades Documentales para confirmar
     * @return MensajeRespuesta
     */
    @PUT
    @Path("/confirmar-unidades-documentales-transferidas/")
    public MensajeRespuesta confirmarUnidadesDocumentalesTransferidasECM(@RequestBody List<UnidadDocumentalDTO> unidadDocumentalDTOS) {
        log.info("Ejecutando metodo MensajeRespuesta confirmarUnidadesDocumentalesTransferidas(List<UnidadDocumentalDTO>{})", unidadDocumentalDTOS);
        try {
            return recordTransfer.confirmarUnidadesDocumentalesTransferidas(unidadDocumentalDTOS);
        } catch (Exception e) {
            log.error("Error en operacion - confirmarUnidadesDocumentalesTransferidasECM ", e);
            return getSmsErrorResponse(e);
        }
    }

    /**
     * Metodo que devuelve la lista de unidades documentales confirmadas
     * @return MensajeRespuesta
     */
    @GET
    @Path("/listar-unidades-documentales-confirmadas/")
    public MensajeRespuesta listarUnidadesDocumentalesConfirmadasECM() {
        log.info("Ejecutando metodo MensajeRespuesta listarUnidadesDocumentalesConfirmadasECM()");
        try {
            final String dependencyCode = uriInfo.getQueryParameters().getFirst("depCode");
            final String numTransfer = uriInfo.getQueryParameters().getFirst("numTransfer");
            return recordTransfer.listarUnidadesDocumentalesConfirmadas(dependencyCode, numTransfer);
        } catch (Exception e) {
            log.error("Error en operacion - listarUnidadesDocumentalesConfirmadasECM ", e);
            return getSmsErrorResponse(e);
        }
    }

    /* ************************************
     * Disposicion Final Inicio
     * ************************************/

    /**
     * Crear carpeta en el Record
     *
     * @param disposicionFinal Obj con el DTO Unidad Documental y l listado de las disposiciones
     * @return Mensaje de respuesta
     */
    @POST
    @Path("/listar-unidades-documentales-disposicion/{depCode}")
    public MensajeRespuesta listarUdDisposicionFinalRecord(@RequestBody DisposicionFinalDTO disposicionFinal,
                                                           @PathParam("depCode") String dependencyCode) {
        log.info("processing rest request - Listar las unidades documentales que hay culminado su tiempo de retencion");
        try {
            return record.listarUdDisposicionFinal(disposicionFinal, dependencyCode);
        } catch (Exception e) {
            log.error("Error en operacion - listarUdDisposicionFinal {}", e.getMessage());
            return getSmsErrorResponse(e);
        }
    }

    /**
     * Metodo para cerrar una o varias unidades documentales
     *
     * @param unidadDocumentalDTOS   Lista Unidades Documentales para aprobar/rechazar
     * @return MensajeRespuesta
     */
    @PUT
    @Path("/aprobar-rechazar-disposiciones-finales/")
    public MensajeRespuesta aprobarRechazarDisposicionesFinalesECM(@RequestBody List<UnidadDocumentalDTO> unidadDocumentalDTOS) {
        log.info("Ejecutando metodo MensajeRespuesta aprobarRechazarDisposicionesFinalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS)");
        try {
            return record.aprobarRechazarDisposicionesFinales(unidadDocumentalDTOS);
        } catch (Exception e) {
            log.error("Error en operacion - aprobarDisposicionesFinalesECM ", e);
            return getSmsErrorResponse(e);
        }
    }

    private MensajeRespuesta getSmsErrorResponse(@NotNull Exception e) {
        MensajeRespuesta rs = new MensajeRespuesta();
        rs.setCodMensaje(ConstantesECM.ERROR_COD_MENSAJE);
        rs.setMensaje(e.getMessage());
        return rs;
    }
}