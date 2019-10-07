package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.*;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EstadosEnum;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.canonical.jbpm.ProcessesDto;
import co.com.soaint.foundation.canonical.ui.ReasignarComunicacionDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/correspondencia-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class CorrespondenciaGatewayApi {

    private static final String CONTENT = "CorrespondenciaGatewayApi - [content] : ";
    @Autowired
    private CorrespondenciaClient client;

    @Autowired
    private ProcesoClient procesoClient;

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private InstrumentoClient instrumentoClient;

    @Value("${comunicacion.salida.externa}")
    String COMUNICACION_SALIDA_EXTERNA;

    @Value("${comunicacion.salida.interna}")
    String COMUNICACION_SALIDA_INTERNA ;

    public CorrespondenciaGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/listar-comunicaciones")
    @JWTTokenSecurity
    @RolesAllowed("Asignador")
    public Response listarComunicaciones(@QueryParam("fecha_ini") final String fechaIni,
                                         @QueryParam("fecha_fin") final String fechaFin,
                                         @QueryParam("cod_dependencia") final String codDependencia,
                                         @QueryParam("cod_estado") final String codEstado,
                                         @QueryParam("nro_radicado") final String nroRadicado,
                                         @QueryParam("id_funcionario") final Long idFuncionario
                                         ) {

        log.info("CorrespondenciaGatewayApi - [trafic] - listing Correspondencia");
        Response response = client.listarComunicaciones(fechaIni, fechaFin, codDependencia, codEstado, nroRadicado,idFuncionario);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);
        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/listar-comunicaciones-salida-externa-distribucion-fisica")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response listarComunicacionesDistribucionFisica(@QueryParam("fecha_ini") final String fechaIni,
                                                           @QueryParam("fecha_fin") final String fechaFin,
                                                           @QueryParam("mod_correo") final String modEnvio,
                                                           @QueryParam("clase_envio") final String claseEnvio,
                                                           @QueryParam("cod_dep") final String codDependencia,
                                                           @QueryParam("cod_tipo_doc") final String codTipoDoc,
                                                           @QueryParam("nro_radicado") final String nroRadicado) {

        log.info("CorrespondenciaGatewayApi - [trafic] - listing Correspondencia salida para distribución física");
        Response response = client.listarComunicacionesSalidaDistibucionFisica(COMUNICACION_SALIDA_EXTERNA,fechaIni, fechaFin, codDependencia, modEnvio, claseEnvio, codTipoDoc, nroRadicado);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);
        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/listar-comunicaciones-distribucion-fisica-salida")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response listarComunicacionesDistribucionFisica(@QueryParam("fecha_ini") final String fechaIni,
                                                           @QueryParam("fecha_fin") final String fechaFin,
                                                           @QueryParam("cod_dependencia") final String codDependencia,
                                                           @QueryParam("nro_radicado") final String nroRadicado) {

        log.info("CorrespondenciaGatewayApi - [trafic] - listing Correspondencia salida para distribución física");
        Response response = client.listarComunicacionesDistibucionFisicaSalida(fechaIni, fechaFin, codDependencia,nroRadicado);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/listar-comunicaciones-salida-distribucion-interna")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response listarComunicacionesDistribucionInterna(@QueryParam("fecha_ini") final String fechaIni,
                                                           @QueryParam("fecha_fin") final String fechaFin,
                                                           @QueryParam("cod_dep") final String codDependencia,
                                                           @QueryParam("tipologia") final String codTipoDoc,
                                                           @QueryParam("numeroRadicado") final String nroRadicado) {

        log.info("CorrespondenciaGatewayApi - [trafic] - listing Correspondencia salida para distribución física");
        Response response = client.listarComunicacionesSalidaDistibucionFisica(COMUNICACION_SALIDA_INTERNA,fechaIni, fechaFin, codDependencia,null,null,null, nroRadicado);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }


    @POST
    @Path("/radicar")
    @JWTTokenSecurity
    @RolesAllowed("Radicador")
    public Response radicarComunicacion(@RequestBody ComunicacionOficialDTO comunicacionOficial) {

        log.info("CorrespondenciaGatewayApi - [trafic] - radicar Correspondencia");
        Response response = client.radicar(comunicacionOficial);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/radicar_salida")
    @JWTTokenSecurity
    @RolesAllowed("Radicador")
    public Response radicarCofSalida(@RequestBody ComunicacionOficialRemiteDTO comunicacionOficial) {

        log.info("CorrespondenciaGatewayApi - [trafic] - radicar Correspondencia");

     return   radicarSalida(comunicacionOficial);
    }

    private Response radicarSalida(ComunicacionOficialRemiteDTO comunicacionOficial){

        Response response = client.radicarSalida(comunicacionOficial);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/radicar-documento-producido")
    @JWTTokenSecurity
    @RolesAllowed("Proyector")
    public Response radicarDocumentoProducido (@RequestBody ComunicacionOficialRemiteDTO comunicacionOficial) {

        return radicarSalida(comunicacionOficial);
    }

    @POST
    @Path("/asignar")
   /* @JWTTokenSecurity
    @RolesAllowed("Asignador")*/
    public Response asignarComunicaciones(AsignacionTramiteDTO asignacionTramiteDTO) {
        try {
            log.info("CorrespondenciaGatewayApi - [trafic] - assinging Comunicaciones");
            Response response = client.asignarComunicaciones(asignacionTramiteDTO);
            AsignacionesDTO responseObject = response.readEntity(AsignacionesDTO.class);

            log.info("asignaciones dto :" + responseObject);

            if (responseObject.getAsignaciones() != null){
                if(responseObject.getAsignaciones().size() > 0) {

                    List<EstadosEnum> estados = new ArrayList();
                    estados.add(EstadosEnum.LISTO);

                    Response responseAssignedTask = client.InitTaskByAsignaciones(responseObject);

                    if(responseAssignedTask.getStatus() > Response.Status.BAD_REQUEST.getStatusCode())
                        return Response.status(responseAssignedTask.getStatus()).entity(response.readEntity(String.class)).build();

                    asignacionTramiteDTO.getAsignaciones().getAsignaciones().forEach( asignacionDTO -> {

                        NotificacionDTO notificacionDTO = new NotificacionDTO();

                        notificacionDTO.setNroRadicado(asignacionDTO.getNroRadicado());
                        notificacionDTO.setNombreTarea("Recibir y Gestionar Documentos");

                        AgenteDTO destinatario = new AgenteDTO();

                        destinatario.setIdeFunci(asignacionDTO.getIdeFunci());
                        destinatario.setCodTipAgent(TipoAgenteEnum.DESTINATARIO.getCodigo());

                        AgenteDTO remitente = new AgenteDTO();

                        remitente.setIdeFunci(asignacionTramiteDTO.getTraza().getIdeFunci());

                        destinatario.setCodTipAgent(TipoAgenteEnum.DESTINATARIO.getCodigo());

                        notificacionDTO.setDestinatario( destinatario);

                        notificacionDTO.setRemitente(remitente);

                        notificationClient.notificate(notificacionDTO);

                    });
                }
            }

            log.info(CONTENT + responseObject);
            if (response.getStatus() != HttpStatus.OK.value()) {
                return Response.ok().entity(new ArrayList<>()).build();
            }
            return Response.status(response.getStatus()).entity(responseObject).build();
        }catch (Exception e){
             return  Response.serverError().entity(e.getMessage()).build();
        }
    }

    /*@POST
    @Path("/reasignar")
    @JWTTokenSecurity
    @RolesAllowed("Asignador")
    public Response reasignarComunicaciones(ReasignarComunicacionDTO reasignarComunicacionDTO) {
        log.info("CorrespondenciaGatewayApi - [trafic] - assinging Comunicaciones");


        for(AsignacionDTO asignacionDTO : reasignarComunicacionDTO.getAsignaciones().getAsignaciones() ) {

            EntradaProcesoDTO entradaProceso = new EntradaProcesoDTO();
            entradaProceso.setIdProceso("proceso.recibir-gestionar-doc");
            entradaProceso.setIdDespliegue("co.com.soaint.sgd.process:proceso-recibir-gestionar-doc:1.0.4-SNAPSHOT");

            Response asigResponse =client.obtenerFuncionarInfoParaReasignar(asignacionDTO.getIdeAgente());

            if(asigResponse.getStatus()>= Response.Status.BAD_REQUEST.getStatusCode())
                return Response.status(asigResponse.getStatus()).entity(asigResponse.readEntity(String.class)).build();

             FuncAsigDTO asigDTO = asigResponse.readEntity(FuncAsigDTO.class);
            log.info("Sacar el hash " + asigDTO);

            EntradaProcesoDTO entradaParaTarea = new EntradaProcesoDTO();

//          entradaParaTarea.setUsuario(asignacionDTO.getLoginName());

//            funcionariosControl.consultarCredencialesByIdeFunci(asignacion.getIdeFunci())

            entradaParaTarea.setPass(asigDTO.getCredenciales());
            entradaParaTarea.setInstanciaProceso(Long.parseLong(asigDTO.getAsignacion().getIdInstancia()));

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("usuario", asigDTO.getAsignacion().getLoginName());
            entradaParaTarea.setParametros(parametros);

//            List<EstadosEnum> estados = new ArrayList<>();
//            estados.add(EstadosEnum.LISTO);
//            estados.add(EstadosEnum.ENPROGRESO);
//            estados.add(EstadosEnum.COMPLETADO);
//            estados.add(EstadosEnum.RESERVADO);
//            entradaParaTarea.setEstados(estados);

            log.info("CorrespondenciaGatewayApi - [trafic] - buscando tareas por usuario y id proceso");
            //Response responseTasks = procesoClient.listarPorIdProceso(entradaParaTarea);

            Response responseTasks = procesoClient.listarPorUsuarioYPorIdProceso(entradaParaTarea);

            List<RespuestaTareaDTO> responseTareas = responseTasks.readEntity(new GenericType<List<RespuestaTareaDTO>>() {
            });
            entradaProceso.setPass(asigDTO.getCredenciales());
            log.info(responseTareas);

            if (responseTareas != null && !responseTareas.isEmpty()) {
                entradaProceso.setIdTarea(responseTareas.get(0).getIdTarea());
                //Map<String, Object> parametros = new HashMap<>();
                parametros.put("usuarioReasignar", asignacionDTO.getLoginName());
                entradaProceso.setParametros(parametros);
                this.procesoClient.reasignarTarea(entradaProceso);

                NotificacionDTO notificacionDTO = new NotificacionDTO();
                notificacionDTO.setNombreTarea("Reasignar Comunicación");
                notificacionDTO.setNroRadicado(asignacionDTO.getNroRadicado());

                AgenteDTO destinatario = new AgenteDTO();
                destinatario.setIdeFunci(asignacionDTO.getIdeFunci());
                destinatario.setCodTipAgent(TipoAgenteEnum.DESTINATARIO.getCodigo());
                notificacionDTO.setDestinatario(destinatario);


                AgenteDTO remitente = new AgenteDTO();
                remitente.setIdeFunci(reasignarComunicacionDTO.getIdFunc());
                remitente.setCodTipAgent(TipoAgenteEnum.DESTINATARIO.getCodigo());
                notificacionDTO.setRemitente(remitente);

                notificationClient.notificate(notificacionDTO);
            }
        }

        Response response = client.asignarComunicaciones(AsignacionTramiteDTO.newInstance().asignaciones(reasignarComunicacionDTO.getAsignaciones()).traza(PpdTrazDocumentoDTO.newInstance().ideFunci(reasignarComunicacionDTO.getIdFunc()).build()).build());

        AsignacionesDTO asignacionDTOResponse = response.readEntity(AsignacionesDTO.class);

        log.info(CONTENT + asignacionDTOResponse);

        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }

        return Response.status(response.getStatus()).entity(asignacionDTOResponse).build();
    }*/

    @POST
    @Path("/reasignar")
    @JWTTokenSecurity
    @RolesAllowed("Asignador")
    public Response reasignarComunicaciones(ReasignarComunicacionDTO reasignarComunicacionDTO) {
        log.info("CorrespondenciaGatewayApi - [trafic] - assinging Comunicaciones");


        for(AsignacionDTO asignacionDTO : reasignarComunicacionDTO.getAsignaciones().getAsignaciones() ) {

            Response asigResponse =client.obtenerFuncionarInfoParaReasignar(asignacionDTO.getIdeAgente());

            if(asigResponse.getStatus()>= Response.Status.BAD_REQUEST.getStatusCode())
                return Response.status(asigResponse.getStatus()).entity(asigResponse.readEntity(String.class)).build();

            FuncAsigDTO asigDTO = asigResponse.readEntity(FuncAsigDTO.class);
            log.info("Sacar el hash " + asigDTO);

            EntradaProcesoDTO entradaParaTarea = new EntradaProcesoDTO();
            entradaParaTarea.setPass(asigDTO.getCredenciales());
            entradaParaTarea.setInstanciaProceso(Long.parseLong(asigDTO.getAsignacion().getIdInstancia()));

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("usuario", asigDTO.getAsignacion().getLoginName());
            entradaParaTarea.setParametros(parametros);

            log.info("CorrespondenciaGatewayApi - [trafic] - buscando tareas por usuario y id proceso");

            Response responseTasks = procesoClient.listarPorUsuarioYPorIdProceso(entradaParaTarea);

            List<RespuestaTareaDTO> responseTareas = responseTasks.readEntity(new GenericType<List<RespuestaTareaDTO>>() {
            });

            log.info(responseTareas);

            if (responseTareas != null && !responseTareas.isEmpty()) {
                try {

                    byte[] decode = Base64Utils.decodeFromString(asigDTO.getCredenciales());
                    String credenciales = new String(decode);
                    String[] conversion = credenciales.split(":");
                    String password = conversion[1];

                    EntradaProcesoDTO requestAbortar = new EntradaProcesoDTO();
                    requestAbortar.setIdDespliegue(responseTareas.get(0).getIdDespliegue());
                    requestAbortar.setInstanciaProceso(responseTareas.get(0).getIdInstanciaProceso());
                    requestAbortar.setIdProceso(responseTareas.get(0).getIdProceso());
                    requestAbortar.setUsuario(asigDTO.getAsignacion().getLoginName());
                    requestAbortar.setPass(password);
                    Response responseAbortar = procesoClient.abortarTarea(requestAbortar);

                    if (responseAbortar.getStatus() == HttpStatus.OK.value()) {
                        RespuestaProcesoDTO res = responseAbortar.readEntity(RespuestaProcesoDTO.class);
                        log.info("RESPUESTA ---> " + res);

                    }
                }
                catch(Exception e){
                    return  Response.serverError().entity(e.getMessage()).build();
                }
            }
        }

        Response response = client.asignarComunicaciones(AsignacionTramiteDTO.newInstance().asignaciones(reasignarComunicacionDTO.getAsignaciones()).traza(PpdTrazDocumentoDTO.newInstance().ideFunci(reasignarComunicacionDTO.getIdFunc()).build()).build());

        AsignacionesDTO asignacionDTOResponse = response.readEntity(AsignacionesDTO.class);

        log.info(CONTENT + asignacionDTOResponse);

        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }else{
            try {
                Response responseAssignedTask = client.InitTaskByAsignaciones(asignacionDTOResponse);
                log.info("RESPUESTA1 ---> " + responseAssignedTask);


                /*NotificacionDTO notificacionDTO = new NotificacionDTO();
                notificacionDTO.setNombreTarea("Reasignar Comunicación");
                notificacionDTO.setNroRadicado(asignacionDTO.getNroRadicado());

                AgenteDTO destinatario = new AgenteDTO();
                destinatario.setIdeFunci(asignacionDTO.getIdeFunci());
                destinatario.setCodTipAgent(TipoAgenteEnum.DESTINATARIO.getCodigo());
                notificacionDTO.setDestinatario(destinatario);

                AgenteDTO remitente = new AgenteDTO();
                remitente.setIdeFunci(reasignarComunicacionDTO.getIdFunc());
                remitente.setCodTipAgent(TipoAgenteEnum.DESTINATARIO.getCodigo());
                notificacionDTO.setRemitente(remitente);

                notificationClient.notificate(notificacionDTO);*/
            }
            catch(Exception e){
                return  Response.serverError().entity(e.getMessage()).build();
            }
        }

        return Response.status(response.getStatus()).entity(asignacionDTOResponse).build();
    }

    @POST
    @Path("/redireccionar")
    @JWTTokenSecurity
    @RolesAllowed("Asignador")
    public Response redireccionarComunicaciones(RedireccionDTO redireccionDTO) {
        log.info("CorrespondenciaGatewayApi - [trafic] - redirect Comunicaciones");
        Response response = client.redireccionarComunicaciones(redireccionDTO);

        NotificacionDTO notificacionDTO = new NotificacionDTO();

        notificacionDTO.setNombreTarea("Asignación de Comunicaciones");

        AgenteDTO remitente = new AgenteDTO();

        remitente.setIdeFunci(redireccionDTO.getTraza().getIdeFunci());
        notificacionDTO.setRemitente(remitente);

        notificationClient.notificateByRolAndDependency(redireccionDTO.getTraza().getCodOrgaAdmin(),"Asignador", notificacionDTO);

        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

  /*  @POST
    @Path("/devolver")
    *//*@JWTTokenSecurity
    @RolesAllowed("Asignador")*//*
    public Response devolverComunicaciones(DevolucionDTO devolucion) {
        log.info("CorrespondenciaGatewayApi - [trafic] - devolver Comunicaciones - documentos trámites");
        return devolver(devolucion);
    }*/

    @POST
    @Path("/devolver/asignacion")
    @JWTTokenSecurity
    @RolesAllowed("Asignador")
    public Response devolverComunicacionesAsignacion(DevolucionDTO devolucion) {
        log.info("CorrespondenciaGatewayApi - [trafic] - devolver Comunicaciones - asignación");
     return devolver(devolucion);

    }

    @POST
    @Path("/devolver")
    public Response devolver(DevolucionDTO devolucion) {
        log.info("CorrespondenciaGatewayApi - [trafic] - devolver Comunicaciones");

        DependenciaDTO dependenciaDTO = instrumentoClient.obtenerDependenciaRadicadoraObject();

         if(dependenciaDTO == null)
            return Response.serverError().entity("No existe dependencia radicadora").build();

        Response response = client.devolverComunicaciones(devolucion);
        if (response.getStatus() == HttpStatus.NO_CONTENT.value() || response.getStatus() == HttpStatus.OK.value()) {

                devolucion.getItemsDevolucion().parallelStream().forEach((itemDevolucion -> {

                    Response  updateInstanceResponse =  this.client.actualizarInstancia(itemDevolucion.getCorrespondencia());

                    log.info("item devolución:" + itemDevolucion);

                    if(updateInstanceResponse.getStatus() < HttpStatus.BAD_REQUEST.value() ){

                        NotificacionDTO notificacionDTO = new NotificacionDTO();

                        notificacionDTO.setNroRadicado(itemDevolucion.getCorrespondencia().getNroRadicado());

                        AgenteDTO remitente = new AgenteDTO();

                        remitente.setIdeFunci(devolucion.getTraza().getIdeFunci());
                        notificacionDTO.setRemitente(remitente);

                        notificacionDTO.setNombreTarea("Gestionar Devoluciones");

                        notificationClient.notificateByRolAndDependency(dependenciaDTO.getCodDependencia(),"Gestor_devoluciones",notificacionDTO);

                    }
                    /*EntradaProcesoDTO entradaProceso = new EntradaProcesoDTO();
                    entradaProceso.setIdProceso("proceso.gestor-devoluciones");
                    entradaProceso.setIdDespliegue("co.com.soaint.sgd.process:proceso-gestor-devoluciones:1.0.0-SNAPSHOT");
                    entradaProceso.setUsuario("pruebasoaint2");
                    entradaProceso.setPass("Soaint2@");
                    Response response_instancia = null;
                    try {
                        response_instancia = this.procesoClient.iniciarProcesoGestorDevoluciones(itemDevolucion, entradaProceso,dependenciaDTO.getCodDependencia());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    /*if (response_instancia.getStatus() == HttpStatus.OK.value()) {
                        ProcessesDto entity = response_instancia.readEntity(ProcessesDto.class);
                        itemDevolucion.getCorrespondencia().setIdeInstancia(entity.getProcessId());
                      Response  updateInstanceResponse =  this.client.actualizarInstancia(itemDevolucion.getCorrespondencia());

                      log.info("item devolución:" + itemDevolucion);

                      if(updateInstanceResponse.getStatus() < HttpStatus.BAD_REQUEST.value() ){

                          NotificacionDTO notificacionDTO = new NotificacionDTO();

                          notificacionDTO.setNroRadicado(itemDevolucion.getCorrespondencia().getNroRadicado());

                          AgenteDTO remitente = new AgenteDTO();

                          remitente.setIdeFunci(devolucion.getTraza().getIdeFunci());
                          notificacionDTO.setRemitente(remitente);

                          notificacionDTO.setNombreTarea("Gestionar Devoluciones");

                          notificationClient.notificateByRolAndDependency(dependenciaDTO.getCodDependencia(),"Gestor_devoluciones",notificacionDTO);

                      }
                    }*/
                }));

        }
        String responseObject = StringUtils.isEmpty(response.readEntity(String.class)) ? response.readEntity(String.class) : "";
        return Response.status(HttpStatus.OK.value()).entity(responseObject).build();
    }

    @GET
    @Path("/metricasTiempo")
    @JWTTokenSecurity
    public Response metricasTiempo(@QueryParam("payload") String payload) {
        log.info("CorrespondenciaGatewayApi - [trafic] - redirect Comunicaciones");
        Response response = client.metricasTiempoDrools(payload);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/contactos-destinatario-externo/{nro_radicado}")
    @JWTTokenSecurity
    public Response obtenerContactoDestinatarioExterna(@PathParam("nro_radicado") String nroRadicado) {
        log.info("TareaGatewayApi - [trafic] - Obtener Contactos Destinatario Externo");
        Response response = client.obtenerContactoDestinatarioExterna(nroRadicado);
        String responseContent = response.readEntity(String.class);
        log.info("TareaGatewayApi - [content] : " + responseContent);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }


    @GET
    @Path("/obtener-comunicacion/{nro_radicado}")
    @JWTTokenSecurity
    public Response obtenerComunicacion(@PathParam("nro_radicado") String nroRadicado) {
        log.info("CorrespondenciaGatewayApi - [trafic] - redirect Comunicaciones");
        Response response = client.obtenerCorrespondenciaPorNroRadicado(nroRadicado);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/obtener-comunicacion-full/{nro_radicado}")
    @JWTTokenSecurity
    public Response obtenerComunicacionfull(@PathParam("nro_radicado") String nroRadicado) {
        log.info("CorrespondenciaGatewayApi - [trafic] - redirect Comunicaciones");
        Response response = client.obtenerCorrespondenciaFullPorNroRadicado(nroRadicado);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/obtenerObservaciones/{idCorrespondencia}")
    @JWTTokenSecurity
    public Response obtenerObservaciones(@PathParam("idCorrespondencia") BigInteger idCorrespondencia) {
        log.info("CorrespondenciaGatewayApi - [trafic] - listing Observaciones for document: " + idCorrespondencia);
        Response response = client.listarObservaciones(idCorrespondencia);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/registrarObservacion")
    @JWTTokenSecurity
    @RolesAllowed({"Asignador","Gestor_devoluciones"})
    public Response registrarObservacion(PpdTrazDocumentoDTO observacion) {
        log.info("CorrespondenciaGatewayApi - [trafic] - redirect Comunicaciones");
        Response response = client.registrarObservacion(observacion);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/constantes")
    @JWTTokenSecurity
    public Response constantes(@QueryParam("codigos") String codigos) {
        log.info("CorrespondenciaGatewayApi - [trafic] - obteniendo constantes por codigos: " + codigos);
        Response response = client.obtnerContantesPorCodigo(codigos);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/listar-distribucion")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response listarDistribucion(@QueryParam("fecha_ini") final String fechaIni,
                                       @QueryParam("fecha_fin") final String fechaFin,
                                       @QueryParam("cod_dependencia") final String codDependencia,
                                       @QueryParam("cod_tipologia_documental") final String codTipoDoc,
                                       @QueryParam("nro_radicado") final String nroRadicado) {
        log.info("CorrespondenciaGatewayApi - [trafic] - listando distribucion");
        Response response = client.listarDistribucion(fechaIni, fechaFin, codDependencia, codTipoDoc, nroRadicado);
        String responseObject = response.readEntity(String.class);
        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/listar-planillas")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response listarPlanillas(@QueryParam("fecha_ini") final String fechaIni,
                                    @QueryParam("fecha_fin") final String fechaFin,
                                    @QueryParam("cod_dependencia") final String codDependencia,
                                    @QueryParam("cod_tipologia_documental") final String codTipoDoc,
                                    @QueryParam("nro_planilla") final String nroPlanilla) {
        log.info("CorrespondenciaGatewayApi - [trafic] - listando planillas");
        Response response = client.listarPlanillas(nroPlanilla);
        String responseObject = response.readEntity(String.class);
        if (response.getStatus() != HttpStatus.OK.value()) {
            PlanillaDTO emptyPlanilla = new PlanillaDTO();
            PlanAgentesDTO planAgentesDTO = new PlanAgentesDTO();
            planAgentesDTO.setPAgente(new ArrayList<>());
            emptyPlanilla.setPAgentes(planAgentesDTO);
            return Response.status(HttpStatus.OK.value()).entity(emptyPlanilla).build();
        }
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/listar-planillas-salida")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response listarPlanillasSalida(@QueryParam("fecha_ini") final String fechaIni,
                                          @QueryParam("fecha_fin") final String fechaFin,
                                          @QueryParam("cod_dependencia") final String codDependencia,
                                          @QueryParam("cod_tipologia_documental") final String codTipoDoc,
                                          @QueryParam("nro_planilla") final String nroPlanilla) {
        log.info("CorrespondenciaGatewayApi - [trafic] - listando planillas");
        Response response = client.listarPlanillasSalida(nroPlanilla);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/generar-planilla")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response generarPlanilla(@RequestBody PlanillaDTO planilla) throws Exception {
        log.info("processing rest request - generar planilla distribucion");
        Response response = client.actualizarPlanillaGenerada(planilla);
        PlanillaDTO responseObject = response.readEntity(PlanillaDTO.class);

        EntradaProcesoDTO entradaProceso = new EntradaProcesoDTO();
        entradaProceso.setIdProceso("proceso.gestion-planillas");
        entradaProceso.setIdDespliegue("co.com.soaint.sgd.process:proceso-gestion-planillas:1.0.0-SNAPSHOT");
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("numPlanilla", responseObject.getNroPlanilla());
        parametros.put("codDependencia", planilla.getCodDependenciaOrigen());
        entradaProceso.setParametros(parametros);
        this.procesoClient.iniciarTercero(entradaProceso);

        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/cargar-plantilla")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response cargarPlanilla(@RequestBody PlanillaDTO planilla) {
        log.info("processing rest request - cargar planilla distribucion");
        Response response = client.actualizarPlanilla(planilla);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/generar-planilla-salida-externa")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response generarPlanillaSalidaExterna(@RequestBody PlanillaDTO planilla) throws Exception {
        log.info("processing rest request - generar planilla salida distribucion");
        Response response = client.actualizarPlanillaGenerada(planilla);
        PlanillaDTO responseObject = response.readEntity(PlanillaDTO.class);

        EntradaProcesoDTO entradaProceso = new EntradaProcesoDTO();
        entradaProceso.setIdProceso("proceso.gestion-planillas-salida");
        entradaProceso.setIdDespliegue("co.com.soaint.sgd.process:proceso.gestion-planillas-salida:1.0.0-SNAPSHOT");
        entradaProceso.setUsuario("arce");
        entradaProceso.setPass("arce");
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("numPlanilla", responseObject.getNroPlanilla());
        parametros.put("codDependencia", planilla.getCodDependenciaOrigen());
        entradaProceso.setParametros(parametros);
        this.procesoClient.iniciarTercero(entradaProceso);

        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/generar-planilla-salida-interna")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response generarPlanillaSalidaInterna(@RequestBody PlanillaDTO planilla) throws Exception {
        log.info("processing rest request - generar planilla salida distribucion");
        Response response = client.actualizarPlanillaGenerada(planilla);
        PlanillaDTO responseObject = response.readEntity(PlanillaDTO.class);

        EntradaProcesoDTO entradaProceso = new EntradaProcesoDTO();
        entradaProceso.setIdProceso("proceso.gestion-planillas-interna");
        entradaProceso.setIdDespliegue("co.com.soaint.sgd.process:proceso.gestion-planillas-interna:1.0.0-SNAPSHOT");
        entradaProceso.setUsuario("arce");
        entradaProceso.setPass("arce");
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("numPlanilla", responseObject.getNroPlanilla());
        parametros.put("codDependencia", planilla.getCodDependenciaOrigen());
        entradaProceso.setParametros(parametros);
        this.procesoClient.iniciarTercero(entradaProceso);

        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/exportar-plantilla/")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response exportarPlanilla(@QueryParam("nroPlanilla") final String nroPlanilla,
                                     @QueryParam("formato") final String formato) {
        log.info("processing rest request - exportar planilla distribucion");
        Response response = client.exportarPlanilla(nroPlanilla, formato);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/exportar-plantilla-salida-externa/")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response exportarPlanillaSalidaExterna(@QueryParam("nroPlanilla") final String nroPlanilla,
                                     @QueryParam("formato") final String formato) {
        log.info("processing rest request - exportar planilla distribucion");
        Response response = client.exportarPlanillaSalida(COMUNICACION_SALIDA_EXTERNA,nroPlanilla, formato);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/exportar-plantilla-salida-interna/")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response exportarPlanillaSalidaInterna(@QueryParam("nroPlanilla") final String nroPlanilla,
                                           @QueryParam("formato") final String formato) {
        log.info("processing rest request - exportar planilla distribucion");
        Response response = client.exportarPlanillaSalida(COMUNICACION_SALIDA_INTERNA,nroPlanilla, formato);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }


    @POST
    @Path("/salvar-correspondencia-entrada")
    @JWTTokenSecurity
    public Response salvarCorrespondenciaEntrada(TareaDTO tarea) {
        log.info("CorrespondenciaGatewayApi - [trafic] - Salvando Correspondencia Entrada");
        Response response = client.salvarCorrespondenciaEntrada(tarea);
        String responseObject = response.readEntity(String.class);
        if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @PUT
    @Path("/actualizar-comunicacion")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response actualizarComunicacion(@RequestBody ComunicacionOficialDTO comunicacionOficial) {
        log.info("CorrespondenciaGatewayApi - [trafic] - comunicacion");
        Response response = client.actualizarComunicacion(comunicacionOficial);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/restablecer_correspondencia_entrada/{proceso}/{tarea}")
    @JWTTokenSecurity
    public Response restablecerCorrespondenciaEntrada(@PathParam("proceso") final String idproceso, @PathParam("tarea") final String idtarea) {
        log.info("CorrespondenciaGatewayApi - [trafic] - Restableciendo Correspondencia Entrada");
        Response response = client.restablecerCorrespondenciaEntrada(idproceso, idtarea);
        String responseObject = response.readEntity(String.class);
        if (response.getStatus() == HttpStatus.NO_CONTENT.value() || response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseObject).build();
    }


    @GET
    @Path("/verificar-redirecciones")
    @JWTTokenSecurity
    @RolesAllowed({"Gestor_devoluciones","Asignador"})
    public Response verificarRedirecciones(@QueryParam("payload") String payload) {
        log.info("CorrespondenciaGatewayApi - [trafic] - verificar cantidad de redirecciones");
        Response response = client.verificarRedireccionesDrools(payload);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/listar-anexos/{nroRadicado}")
    @JWTTokenSecurity
    @RolesAllowed("Radicador")
    public Response listarAnexos(@PathParam("nroRadicado") String nroRadicado) {
        log.info(CONTENT + "  - listar anexos por nro radicado");
        Response response = client.listarAnexos(nroRadicado);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    /**
     * ---------------------------  Metodos de solicitud de creacion de unidad documental -----
     **/

    @POST
    @Path("/crear-solicitud-unidad-documental")
    @JWTTokenSecurity
    @RolesAllowed("Solicitante")

    public Response crearSolicitudUnidad(@RequestBody SolicitudesUnidadDocumentalDTO solicitudes) {

        log.info("UnidadDocumentalGatewayApi: Crear Solicitude de Unidaddes documentales");

        Response response = client.crearSolicitudUnidadDocuemntal(solicitudes);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/listar-solicitud-ud-no-tramitadas")
    @JWTTokenSecurity
    @RolesAllowed("Archivista")
    public Response listarSolicitudUnidadNoTramitadas(@QueryParam("codSede") final String codSede,
                                                      @QueryParam("codDependencia") final String codDependencia,
                                                      @QueryParam("idSolicitante") final String idSolicitante,
                                                      @QueryParam("fechaSolicitud") final String fechaFin) {

        log.info("UnidadDocumentalGatewayApi: Listar Solicitude de Unidaddes documentales");

        log.info("cod_sede: {}, cod_dependencia: {}, id_solicitante: {}, fecha_in: {}", codSede, codDependencia, idSolicitante, fechaFin);
        Response response = client.listarSolicitudUnidadDocumentalNoTramitadas(codSede, codDependencia, idSolicitante, fechaFin);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/obtener-datos-contacto-agente/{idAgente}")
    @JWTTokenSecurity
    @RolesAllowed("Radicador")
    public Response getDatosContactoByIdAgente(@PathParam("idAgente") String idAgente) {

        Response response = client.getDatosContactosByIdAgente(idAgente);

        List<DatosContactoDTO> datosContacto = response.readEntity(new GenericType<List<DatosContactoDTO>>() {
        });

        return Response.status(response.getStatus()).entity(datosContacto).build();
    }

    @GET
    @Path("/obtener-datos-contacto-identificacion/{nroIdentificacion}/{tipoPersona}/{tipoDocumentoIdentifcacion}")
    @JWTTokenSecurity
    @RolesAllowed("Radicador")
    public Response getDatosContactoByIdAgente(@PathParam("nroIdentificacion") String nroIdentificacion,
                                               @PathParam("tipoPersona") String tipoPersona,
                                               @PathParam("tipoDocumentoIdentifcacion") String tipoDocIdent) {
        final Response response = client.getDatosContactosByIdentificacion(nroIdentificacion, tipoPersona, tipoDocIdent);
        final List<DatosContactoDTO> datosContacto = response.readEntity(new GenericType<List<DatosContactoDTO>>() {
        });
        return Response.status(response.getStatus()).entity(datosContacto).build();
    }

    @GET
    @Path("/obtener-radicados-padres")
    @JWTTokenSecurity
    @RolesAllowed("Radicador")
    public Response getRadicadosPadres(@QueryParam("noRadicado") String nroRadicado, @QueryParam("numIdentificacion") String nroIdentidad, @QueryParam("nombre") String nombre, @QueryParam("noGuia") String noGuia, @QueryParam("anno") String anno, @QueryParam("tipoDocumento") String tipoDocumento) {

        Response response = client.getRadicadoPadres(nroRadicado, nroIdentidad, nombre, noGuia, anno, tipoDocumento);

        List<RadicadoDTO> radicados = response.readEntity(new GenericType<List<RadicadoDTO>>() {
        });

        return Response.status(response.getStatus()).entity(radicados).build();

    }

    @PUT
    @Path("/atualizar-instancia-gestion-devolucion")
    @JWTTokenSecurity
    @RolesAllowed("Gestor_devoluciones")
    public Response actualizarInstanciaGestionDevolucion(CorrespondenciaDTO correspondenciaDTO){

        log.info("Actualizando instancia de devolucion");

      Response response =  client.actualizarInstanciaGestionaDevoluciones(correspondenciaDTO);

      String responseContent = response.readEntity(String.class);

      return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @PUT
    @Path("/confirmar-recibo-fisico")
    @JWTTokenSecurity
    @RolesAllowed("Auxiliar_correspondencia")
    public Response confirmarReciboFisico(List<CorrespondenciaDTO> correspondenciaDTOList){

        log.info("Actualizando instancia de devolucion");

        Response response =  client.confirmarReciboFisico(correspondenciaDTOList);

        String responseContent = response.readEntity(String.class);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }


}
