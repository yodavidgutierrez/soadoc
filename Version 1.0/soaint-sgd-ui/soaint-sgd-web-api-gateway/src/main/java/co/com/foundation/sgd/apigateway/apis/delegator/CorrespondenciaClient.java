package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.Futures;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EstadosEnum;
import co.com.soaint.foundation.canonical.correspondencia.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@ApiDelegator
@Log4j2
public class CorrespondenciaClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private String droolsEndpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_DROOLS_SERVICE_ENDPOINT_URL);

    private String droolsAuthToken = SystemParameters.getParameter(SystemParameters.BACKAPI_DROOLS_SERVICE_TOKEN);


    @Autowired
    private ProcesoClient procesoClient;

    @Autowired
    @Qualifier("dfrThreadPoolTaskExecutor")
    private Executor executor;

    @Value("${comunicacion.salida.externa}")
    String COMUNICACION_SALIDA_EXTERNA;

    @Value("${comunicacion.salida.interna}")
    String COMUNICACION_SALIDA_INTERNA ;

    public CorrespondenciaClient() {
        super();
    }

    public Response radicar(ComunicacionOficialDTO comunicacionOficialDTO) {
        log.info("Correspondencia - [trafic] - radicar Correspondencia with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia")
                .request()
                .post(Entity.json(comunicacionOficialDTO));
    }

    public Response radicarSalida(ComunicacionOficialRemiteDTO comunicacionOficialDTO) {
        log.info("Correspondencia - [trafic] - radicar Correspondencia Salida with endpoint: " + endpoint);

         String path ="radicar-salida";
        final String tipoComunicacion =  comunicacionOficialDTO.getCorrespondencia().getCodTipoCmc();

       if(tipoComunicacion.equals(COMUNICACION_SALIDA_INTERNA))
           path = "radicar-interna";

        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/"+ path)
                .request()
                .post(Entity.json(comunicacionOficialDTO));
    }

    public Response listarComunicaciones(String fechaIni, String fechaFin, String codDependencia, String codEstado, String nroRadicado, Long idFuncionario) {
        log.info("Correspondencia - [trafic] - radicar Correspondencia with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia")
                .queryParam("fecha_ini", fechaIni)
                .queryParam("fecha_fin", fechaFin)
                .queryParam("cod_dependencia", codDependencia)
                .queryParam("cod_estado", codEstado)
                .queryParam("nro_radicado", nroRadicado)
                .queryParam("id_Funcionario", idFuncionario)
                .request()
                .get();
    }

    public Response listarComunicacionesSalidaDistibucionFisica(String tipoComunicacion,String fechaIni, String fechaFin, String codDependencia, String modEnvio, String claseEnvio, String codTipoDoc, String nroRadicado) {
        log.info("Correspondencia - [trafic] - listar comunicacion salida para distribucion fisica with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/listar-comunicacion-salida-distribucion-fisica/"+ tipoComunicacion)
                .queryParam("fecha_ini", fechaIni)
                .queryParam("fecha_fin", fechaFin)
                .queryParam("cod_dependencia", codDependencia)
                .queryParam("mod_correo", modEnvio)
                .queryParam("clase_envio", claseEnvio)
                .queryParam("cod_tipo_doc", codTipoDoc)
                .queryParam("nro_radicado", nroRadicado)
                .request()
                .get();
    }
    public Response listarComunicacionesDistibucionFisicaSalida(String fechaIni, String fechaFin, String codDependencia,String nroRadicado) {
        log.info("Correspondencia - [trafic] - listar comunicacion salida para distribucion fisica with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/listar-comunicacion-salida-distribucion-fisica-distribucion")
                .queryParam("fecha_ini", fechaIni)
                .queryParam("fecha_fin", fechaFin)
                .queryParam("cod_dependencia", codDependencia)
                .queryParam("numeroRadicado", nroRadicado)
                .request()
                .get();
    }


    public Response listarObservaciones(BigInteger idCorrespondencia) {
        log.info("Correspondencia - [trafic] - radicar Correspondencia with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/documento-web-api/documento/listar-observaciones/" + idCorrespondencia)
                .request()
                .get();
    }

    public Response obtenerCorrespondenciaPorNroRadicado(String nroRadicado) {
        log.info("Correspondencia - [trafic] - obtenet Correspondencia por nro de radicado with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/" + nroRadicado)
                .request()
                .get();
    }

    public Response obtenerCorrespondenciaFullPorNroRadicado(String nroRadicado) {
        log.info("Correspondencia - [trafic] - obtenet Correspondencia por nro de radicado with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/full" + nroRadicado)
                .request()
                .get();
    }

    public Response asignarComunicaciones(AsignacionTramiteDTO asignacionTramiteDTO) {
        log.info("Correspondencia - [trafic] - asignar Comunicaciones with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/asignacion-web-api/asignacion/asignar-funcionario")
                .request()
                .post(Entity.json(asignacionTramiteDTO));
    }

    public Response InitTaskByAsignaciones(AsignacionesDTO asignacionesDTO)throws ExecutionException, InterruptedException{

        List<EstadosEnum> estados = new ArrayList();
        estados.add(EstadosEnum.LISTO);

        final List<CompletableFuture<Response>>  completableFutureArrayList =  new ArrayList<>();

        final List<Response> responses = new ArrayList<>();

        asignacionesDTO.getAsignaciones().forEach(asignacionDTO -> {
            EntradaProcesoDTO entradaProceso = new EntradaProcesoDTO();
            entradaProceso.setIdProceso("proceso.recibir-gestionar-doc");
            entradaProceso.setIdDespliegue("co.com.soaint.sgd.process:proceso-recibir-gestionar-doc:1.0.4-SNAPSHOT");
            entradaProceso.setEstados(estados);
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("idAsignacion", asignacionDTO.getIdeAsignacion().toString());
            parametros.put("idAgente", asignacionDTO.getIdeAgente().toString());
            parametros.put("usuario", asignacionDTO.getLoginName());
            parametros.put("idDocumento", asignacionDTO.getIdeDocumento().toString());
            parametros.put("numeroRadicado", asignacionDTO.getNroRadicado());
            parametros.put("fechaRadicacion", asignacionDTO.getFecRadicado());
            parametros.put("codDependencia", asignacionDTO.getCodDependencia());
            if (asignacionDTO.getAlertaVencimiento() != null)
                parametros.put("fechaVencimiento", asignacionDTO.getAlertaVencimiento());
            entradaProceso.setParametros(parametros);

            completableFutureArrayList.add(Futures
                    .toCompletable(procesoClient.iniciarTerceroAsync(entradaProceso, new InvocationCallback<Response>() {
                        @Override
                        public void completed(Response response) {

                            responses.add(response);
                        }
                        @Override
                        public void failed(Throwable throwable) {

                            log.info(throwable.getMessage());

                        }
                    }),executor));
        });


        CompletableFuture.allOf(completableFutureArrayList.toArray(new CompletableFuture[completableFutureArrayList.size()])).get();

        for( Response r : responses){

            if(r.getStatus() > Response.Status.BAD_REQUEST.getStatusCode())
                return r;
        }

        return Response.ok().build();

    }


    public Response obtenerFuncionarInfoParaReasignar(BigInteger idAgente) {
        log.info("Asignacion - [trafic] - obtener Hash del Funcionario with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/asignacion-web-api/asignacion/re-asignacion/" + idAgente)
                .request()
                .get();
    }

    public Response redireccionarComunicaciones(RedireccionDTO redireccionDTO) {
        log.info("Correspondencia - [trafic] - redireccionar Comunicaciones with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/agente-web-api/agente/redireccionar")
                .request()
                .put(Entity.json(redireccionDTO));
    }

    public Response devolverComunicaciones(DevolucionDTO devolucionDTO) {
        log.info("Correspondencia - [trafic] - redireccionar Comunicaciones with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/agente-web-api/agente/devolver")
                .request()
                .put(Entity.json(devolucionDTO));
    }

    public Response obtenerContactoDestinatarioExterna(String idRadicado) {
        log.info("Correspondencia - [trafic] - obtener - Contacto- DestinatarioExterna with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/agente-web-api/agente/remitente/" + idRadicado).request().get();
    }

    public Response metricasTiempoDrools(String payload) {
        log.info("Correspondencia - [trafic] -metricas de Tiempo por Tipologia Regla: " + droolsEndpoint);

        WebTarget wt = ClientBuilder.newClient().target(droolsEndpoint);
        return wt.path("/regla")
                .request()
                .header("Authorization", "Basic " + droolsAuthToken)
                .header("X-KIE-ContentType", "json")
                .header("Content-Type", "application/json")
                .post(Entity.json(payload));
    }

    public Response verificarRedireccionesDrools(String payload) {
        log.info("Correspondencia - [trafic] - verificar redirecciones Regla: " + droolsEndpoint);
        log.error("DROOLS TOKEN: Basic " + droolsAuthToken);

        WebTarget wt = ClientBuilder.newClient().target(droolsEndpoint);
        return wt.path("/redireccion")
                .request()
                .header("Authorization", "Basic " + droolsAuthToken)
                .header("X-KIE-ContentType", "json")
                .header("Content-Type", "application/json")
                .post(Entity.json(payload));
    }

    public Response registrarObservacion(PpdTrazDocumentoDTO observacion) {
        log.info("Correspondencia - [trafic] -registrar observacion: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/documento-web-api/documento/registrar-observacion")
                .request()
                .post(Entity.json(observacion));
    }

    public Response obtnerContantesPorCodigo(String codigos) {
        log.info("Correspondencia - [trafic] -registrar observacion: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        WebTarget target = wt.path("/constantes-web-api/constantes").queryParam("codigos", codigos);

        return target.request().get();
    }

    public Response listarDistribucion(String fechaIni, String fechaFin, String codDependencia, String codTipoDoc, String nroRadicado) {
        log.info("Correspondencia - [trafic] - listar distribucion: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        WebTarget target = wt.path("/correspondencia-web-api/correspondencia/listar-distribucion")
                .queryParam("fecha_ini", fechaIni)
                .queryParam("fecha_fin", fechaFin)
                .queryParam("cod_dependencia", codDependencia)
                .queryParam("cod_tipologia_documental", codTipoDoc)
                .queryParam("nro_radicado", nroRadicado);
        return target.request().get();
    }

    public Response listarPlanillas(String nroPlanilla) {
        log.info("Correspondencia - [trafic] - listar planillas: " + nroPlanilla);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        WebTarget target = wt.path("/planillas-web-api/planillas/" + nroPlanilla);

        return target.request().get();
    }

    public Response listarPlanillasSalida(String nroPlanilla) {
        log.info("Correspondencia - [trafic] - listar planillas: " + nroPlanilla);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        WebTarget target = wt.path("/planillas-web-api/planillas-salida/" + nroPlanilla);

        return target.request().get();
    }

    public Response generarPlantilla(PlanillaDTO planilla) {
        log.info("Correspondencia - [trafic] - generar planilla: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        log.info("****** DTO que llega  planilla *****"+ planilla);

        return wt.path("/planillas-web-api/planillas")
                .request()
                .post(Entity.json(planilla));
    }

    Response cargarPlantilla(PlanillaDTO planilla) {
        log.info("Correspondencia - [trafic] - generar planilla: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/planillas-web-api/planillas")
                .request()
                .put(Entity.json(planilla));
    }

    public Response exportarPlanilla(String nroPlanilla, String formato) {
        log.info("Correspondencia - [trafic] - exportar planilla: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/planillas-web-api/planillas/" + nroPlanilla + "/" + formato)
                .request()
                .get();
    }

    public Response exportarPlanillaSalida(String tipoComunicacion,String nroPlanilla, String formato) {
        log.info("Correspondencia - [trafic] - exportar planilla: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/planillas-web-api/planillas/salida/" + nroPlanilla + "/" + formato+"/"+tipoComunicacion)
                .request()
                .get();
    }

    public Response restablecerCorrespondenciaEntrada(String idproceso, String idtarea) {
        log.info("Correspondencia - [trafic] - Invocando Servicio Remoto SalvarCorrespondenciaEntrada: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/tarea-web-api/tarea/" + idproceso + "/" + idtarea)
                .request().get();
    }

    public Response salvarCorrespondenciaEntrada(TareaDTO tarea) {
        log.info("Correspondencia - [trafic] - generar planilla: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/tarea-web-api/tarea")
                .request()
                .post(Entity.json(tarea));
    }

    public Response actualizarComunicacion(ComunicacionOficialDTO comunicacionOficialDTO) {
        log.info("Comunicacion - [trafic] - comunicacion with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/actualizar-comunicacion")
                .request()
                .put(Entity.json(comunicacionOficialDTO));
    }


    public Response listarAnexos(String nroRadicado) {
        log.info("Comunicacion - [trafic] - listar anexos: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("correspondencia-web-api/anexo-web-api/anexo" + "/" + nroRadicado)
                .request()
                .get();
    }

    /**
     * ------------------------------  Metodos de Solicitud de Creacion de Unidad Dcoumental --------------------------
     */
    public Response crearSolicitudUnidadDocuemntal(SolicitudesUnidadDocumentalDTO solicitudes) {

        log.info("Delegator: crear Solicitud de unidad documentales - [trafic] - Modificar Unidades Documentales");

        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/crear-solicitud-um")
                .request()
                .post(Entity.json(solicitudes));
    }

    public Response listarSolicitudUnidadDocumentalNoTramitadas(String codSede, String codDependencia, String idSolicitante, String fechaSolicitud) {

        log.info("Delegator: listar Solicitud de unidad documentales - [trafic] - Modificar Unidades Documentales");

        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        return wt.path("/correspondencia-web-api/correspondencia/obtener-solicitud-um-solicitante-sin-tramitar")
                .queryParam("cod_sede", codSede)
                .queryParam("cod_dependencia", codDependencia)
                .queryParam("id_solicitante", idSolicitante)
                .queryParam("fecha_in", fechaSolicitud)
                .request()
                .get();
    }

     public Response actualizarInstancia(CorrespondenciaDTO correspondencia) {
        log.info("Delegator: actualizar instancia  - [trafic] - devoluciones");

        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/actualizar-ide-instancia")
                .request()
                .put(Entity.json(correspondencia));
    }

    public Response getDatosContactosByIdAgente(String idAgente) {

        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        return wt.path("/datos-contacto-web-api/datoscontacto/" + idAgente)
                .request()
                .get();
    }

    public Response getDatosContactosByIdentificacion(String nroIdentificacion, String tipoPersona, String tipoDocumentoIdentiifcacion) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/datos-contacto-web-api/datoscontacto-nro/" + nroIdentificacion + "/" + tipoPersona + "/"+tipoDocumentoIdentiifcacion)
                .request()
                .get();
    }

    public Response getRadicadoPadres(String nroRadicado, String nroIdentidad, String nombre, String noGuia, String anno, String tipoDocumento) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/radicado-web-api/radicado")
                .queryParam("nro-radicado", nroRadicado)
                .queryParam("nro-identidad", nroIdentidad)
                .queryParam("nombre", nombre)
                .queryParam("noGuia", noGuia)
                .queryParam("anno", anno)
                .queryParam("tipoDocumento",tipoDocumento)
                .request()
                .get();

    }

    public Response actualizarDatosEntrega(List<CorrespondenciaDTO> correspondenciaDTOS){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/actualizar-entrega")
                .request()
                .put(Entity.json(correspondenciaDTOS));
    }

   public Future<Response> actualizarDatosEntregaAsync(List<CorrespondenciaDTO> correspondenciaDTOS){

       WebTarget wt = ClientBuilder.newClient().target(endpoint);
     return   wt.path("/correspondencia-web-api/correspondencia/actualizar-entrega")
               .request()
                .async()
                .put(Entity.json(correspondenciaDTOS), new InvocationCallback<Response>() {
                    @Override
                    public void completed(Response response) {

                        if(response.getStatus() < Response.Status.BAD_REQUEST.getStatusCode()){
                            log.info("Actulizacion de estados de entrega exitosa");
                        }
                        else{
                            log.info("error :"+ response.readEntity(String.class));
                        }
                    }

                    @Override
                    public void failed(Throwable throwable) {
                         log.info("error :"+ throwable.getMessage());
                    }
                });

   }

   public Response actualizarPlanilla(PlanillaDTO planillaDTO){

        Response response = cargarPlantilla(planillaDTO);

        if(response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return response;

        List<CorrespondenciaDTO> correspondenciaDTOList = new ArrayList<>();

        for(PlanAgenDTO agenDTO : planillaDTO.getPAgentes().getPAgente()){

            CorrespondenciaDTO correspondenciaDTO = new CorrespondenciaDTO();

            correspondenciaDTO.setNroRadicado(agenDTO.getNroRadicado());
            correspondenciaDTO.setEstadoEntrega(agenDTO.getEstado());
            correspondenciaDTO.setFecEntrega(agenDTO.getFecCarguePla());
            correspondenciaDTO.setObservacionesEntrega(agenDTO.getObservaciones());

            correspondenciaDTOList.add(correspondenciaDTO);
        }

        log.info("correspondencias a actualizar :" + correspondenciaDTOList);
         Future<Response> future =actualizarDatosEntregaAsync(correspondenciaDTOList);

        synchronized (future){
            future.notify();
        }

        return response;
   }

    public Response confirmarReciboFisico(List<CorrespondenciaDTO> correspondenciaDTOList){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/actualizar-envio/true")
                .request()
                .put(Entity.json(correspondenciaDTOList));
    }

   public Response actualizarDatosEnvio(List<CorrespondenciaDTO> correspondenciaDTOList){

       WebTarget wt = ClientBuilder.newClient().target(endpoint);
       return wt.path("/correspondencia-web-api/correspondencia/actualizar-envio/false")
               .request()
               .put(Entity.json(correspondenciaDTOList));
   }

    public Future<Response> actualizarDatosEnvioAsync(List<CorrespondenciaDTO> correspondenciaDTOList){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/actualizar-envio/false")
                .request()
                .async()
                .put(Entity.json(correspondenciaDTOList));
    }

    public Response actualizarPlanillaGenerada(PlanillaDTO planillaDTO){

        log.info("*******-- actualizarPlanillaGenerada --: ::: "+ planillaDTO);
        Response response = generarPlantilla(planillaDTO);

        List<CorrespondenciaDTO> correspondenciaDTOList = new ArrayList<>();

        for( PlanAgenDTO agenteDTO : planillaDTO.getPAgentes().getPAgente()){
            CorrespondenciaDTO correspondenciaDTO = new CorrespondenciaDTO();

            correspondenciaDTO.setNroRadicado(agenteDTO.getNroRadicado());
            correspondenciaDTO.setPeso(agenteDTO.getVarPeso());
            correspondenciaDTO.setNroGuia(agenteDTO.getVarNumeroGuia());
            correspondenciaDTO.setValorEnvio(agenteDTO.getVarValor());

            correspondenciaDTOList.add(correspondenciaDTO);
        }

        log.info("correspondencias a actualizar :" + correspondenciaDTOList);
        Future<Response> future =actualizarDatosEnvioAsync(correspondenciaDTOList);

        synchronized (future){
            future.notify();
        }

        return  response;

    }

  public Response actualizarInstanciaGestionaDevoluciones(CorrespondenciaDTO correspondenciaDTO){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/actualizar-ide-instancia-devolucion")
                .request()
                .put(Entity.json(correspondenciaDTO));
    }

    /** --------------------- Fin -------------------------------*/
}
