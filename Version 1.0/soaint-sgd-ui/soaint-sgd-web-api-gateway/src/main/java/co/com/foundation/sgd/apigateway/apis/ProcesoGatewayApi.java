package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.FuncionarioClient;
import co.com.foundation.sgd.apigateway.apis.delegator.NotificationClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ProcesoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.foundation.sgd.dto.FullTareaDTO;
import co.com.foundation.sgd.dto.TareaDTO;
import co.com.foundation.sgd.utils.AdapterForNotificate;
import co.com.foundation.sgd.utils.AdapterTareaDTO;
import co.com.foundation.sgd.utils.ProcessRolSettings;
import co.com.soaint.foundation.canonical.bpm.*;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.canonical.correspondencia.NotificacionDTO;
import co.com.soaint.foundation.canonical.jbpm.FullTaskDto;
import co.com.soaint.foundation.canonical.jbpm.ParametrosDto;
import co.com.soaint.foundation.canonical.jbpm.ProcessesDto;
import co.com.soaint.foundation.canonical.jbpm.TaskDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Path("/proceso-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class ProcesoGatewayApi {

    private static final String CONTENT = "ProcesoGatewayApi - [content] : ";
    @Autowired
    private ProcesoClient procesoClient;

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private FuncionarioClient funcionarioClient;

    public ProcesoGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/{loginName}")
    /*@JWTTokenSecurity
    @PermitAll*/
    public Response list(@PathParam("loginName") String loginName, @Context HttpServletRequest request) {

        log.info("ProcesoGatewayApi - [trafic] - listing Procesos");
        Response response = procesoClient.list(request);

        if (response.getStatus() > Response.Status.BAD_REQUEST.getStatusCode())
            return Response.status(response.getStatus()).entity(response.readEntity(String.class)).build();

        List<ProcessesDto> responseContent = response.readEntity(new GenericType<List<ProcessesDto>>() {
        });

        List<RespuestaProcesoDTO> procesos = new ArrayList<>();
        for (ProcessesDto proces : responseContent) {
            procesos.add(RespuestaProcesoDTO.newInstance().codigoProceso(proces.getProcessId())
                    .estado("Falta el estado del proceso")
                    .nombreProceso(proces.getProcessName())
                    .idDespliegue(proces.getDeploymentId())
                    .build());
        }
        log.info(CONTENT + procesos);

        FuncionarioDTO requestFuncionario = new FuncionarioDTO();

        requestFuncionario.setLoginName(loginName);

        Response responseFuncionario = funcionarioClient.buscarFuncionario(requestFuncionario);

        if (responseFuncionario.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return Response.status(responseFuncionario.getStatus()).entity(responseFuncionario.readEntity(String.class)).build();


        FuncionarioDTO funcionario = responseFuncionario.readEntity(FuncionariosDTO.class).getFuncionarios().get(0);

        log.info("funcionario actual :" + funcionario);

        try {

            List<RespuestaProcesoDTO> procesosDemanda = procesos.stream()
                    .filter(
                            respuestaProcesoDTO -> ProcessRolSettings.GetProcesoPorDemanda().stream()
                                    .anyMatch(processRol -> processRol.getIniciarPorDemanda()
                                            && processRol.getIdProceso().equals(respuestaProcesoDTO.getCodigoProceso())
                                            && funcionario.getRoles().stream()
                                            .anyMatch(rolDTO -> rolDTO.getRol().equals(processRol.getRol()))
                            )).collect(Collectors.toList());

            return Response.ok().entity(procesosDemanda).build();
        } catch (Exception e) {

            log.info(e.toString());

            return Response.serverError().entity(e.toString()).build();
        }
    }

    /**
     * @param entrada
     * @return Response
     */
    @POST
    @Path("/iniciar")
    /*@JWTTokenSecurity
    @PermitAll*/
    public Response iniciarProceso(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - starting Process");
        Response response = procesoClient.iniciarManual(entrada);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/listar/estados-instancia")
    /*@JWTTokenSecurity*/
    public Response listTareasIdProceso(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - listing Precess");
        Response response = procesoClient.listarPorIdProceso(entrada);
        List<TaskDto> responseContentF = response.readEntity(new GenericType<List<TaskDto>>() {
        });

        List<RespuestaTareaDTO> responseContent = procesoClient.contructTaskResponse(responseContentF,entrada);

        List<TareaDTO> result = responseContent
                .stream()
                .map(AdapterTareaDTO::convertToTareaDTO)
                .collect(Collectors.toList());

        log.info(CONTENT + result);

        return Response.status(response.getStatus()).entity(result).build();
    }

    @POST
    @Path("/tareas/listar/estados")
    /*@JWTTokenSecurity
    @PermitAll*/
    public Response listTareas(EntradaProcesoDTO entrada) {
        try {
            log.info("ProcesoGatewayApi - [trafic] - listing Tasks");
            log.info("ProcesoGatewayApi - [codDependencia] - " + entrada.getParametros().get("codDependencia"));
            Response response = procesoClient.listarTareas(entrada);
            if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
                return Response.status(response.getStatus()).entity(response.readEntity(String.class)).build();

            List<TaskDto> tareas =  new ArrayList<>();
            FullTaskDto tareasPaginadas = response.readEntity(new GenericType<FullTaskDto>() {
            });
            tareas = tareasPaginadas.getTareas();
            List<RespuestaTareaDTO> responseContent = procesoClient.contructTaskResponse(tareas, entrada);
            log.info(CONTENT + responseContent);
            FuncionarioDTO funcionarioSearch = new FuncionarioDTO();
            funcionarioSearch.setLoginName(entrada.getUsuario());
            Response funcionarioResponse = funcionarioClient.buscarFuncionario(funcionarioSearch);

            FuncionariosDTO fun = funcionarioResponse.readEntity(FuncionariosDTO.class);

            if (fun == null)
                return Response.status(response.getStatus()).entity("[]").build();
            if (fun.getFuncionarios().size() == 0)
                return Response.status(response.getStatus()).entity("[]").build();

            FuncionarioDTO funcionarioDTO = fun.getFuncionarios().get(0);

            log.info("before");
          /*  List<TareaDTO> result = responseContent
                    .parallelStream()
                    .filter((tarea) -> (tarea.getCodigoDependencia().equals(entrada.getParametros().get("codDependencia")) || tarea.getNombre().equals("Verificar y Aprobar Transferencia"))
                            && funcionarioDTO.getRoles() != null && funcionarioDTO.getRoles().parallelStream().anyMatch(rolDTO -> rolDTO.getRol().equals(tarea.getRol()))
                    )
                    .map(AdapterTareaDTO::convertToTareaDTO)
                    .collect(Collectors.toList());*/

          List<TareaDTO> result = responseContent
                    .parallelStream()
                    .map(AdapterTareaDTO::convertToTareaDTO)
                    .collect(Collectors.toList());
            log.info("after");

            FullTareaDTO  tareasFull = FullTareaDTO.newBuilder()
                    .cantidad(tareasPaginadas.getCount())
                    .tareas(result)
                    .build();

            return Response.status(response.getStatus()).entity(tareasFull).build();

        } catch (Exception ex) {
            log.info(ex.toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build();
        }

    }

    @POST
    @Path("/tareas/listar/completadas")
    @JWTTokenSecurity
    @PermitAll
    public Response listTareasCompletadas(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - listing Tasks");
        Response response = procesoClient.listarTareasCompletadas(entrada);
        List<RespuestaTareaBamDTO> responseContent = response.readEntity(new GenericType<List<RespuestaTareaBamDTO>>() {
        });
        List<RespuestaTareaDTO> responseTasks = new ArrayList<>();
        responseContent.forEach((tarea) -> {
            RespuestaTareaDTO res = new RespuestaTareaDTO();
            res.setEstado(tarea.getStatus());
            res.setNombre(tarea.getTaskname());
            res.setIdTarea(new Long(tarea.getTaskid()));
            responseTasks.add(res);
        });
        log.info(CONTENT + responseTasks);

        return Response.status(response.getStatus()).entity(responseTasks).build();
    }

    @POST
    @Path("/tareas/reasignar")
    @JWTTokenSecurity
    @RolesAllowed("Administrador")
    public Response reasignarTarea(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - listing Tasks");
        Response response = procesoClient.reasignarTareaAOtro(entrada);
        RespuestaTareaBamDTO responseContent = response.readEntity(new GenericType<RespuestaTareaBamDTO>() {
        });

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/tareas/reasignarVarias")
   /* @JWTTokenSecurity
    @RolesAllowed("Administrador")*/
    public Response reasignarVariasTarea(List<EntradaProcesoDTO> entradas) throws ExecutionException, InterruptedException {

        try {
            log.info("ProcesoGatewayApi - [trafic] - listing Tasks");
            List<Response> responses = procesoClient.reasignarTareas(entradas);
            List<RespuestaTareaBamDTO> formatted = new ArrayList<>();

            int status = 0;

            for (Response response : responses) {

                status = response.getStatus();

                if (status >= 400)
                    return Response.status(status).entity(response).build();

            }


            return Response.status(status).entity("{\"success\":true}").build();
        } catch (Exception e) {

            return Response.status(500).entity(e.getMessage()).build();
        }
    }


    @POST
    @Path("/tareas/listar/usuario")
    @JWTTokenSecurity
    @RolesAllowed("Administrador")
    public Response listEstadisticasTareas(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - stadistics not in use tasks");
        Response response = procesoClient.listarEstadisticasTareas(entrada);
        List<RespuestaTareaBamDTO> responseContent = response.readEntity(new GenericType<List<RespuestaTareaBamDTO>>() {
        });

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/tareas/listarporusuario")
    /*@JWTTokenSecurity
    @RolesAllowed("Administrador")*/
    public Response listTaskByUser(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - stadistics not in use tasks");
        // Response response = procesoClient.listarTareasPorUsuario(entrada);
        //Response response = procesoClient.listarTareasPorEstadosTercero(entrada);
        Response response = procesoClient.listarTareas(entrada);
        FullTaskDto tareasPaginadas = null;
        try {
            if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
                return Response.status(response.getStatus()).entity(response.readEntity(String.class)).build();
            tareasPaginadas = response.readEntity(new GenericType<FullTaskDto>() {
            });
        } catch (Exception e) {
            log.error("Error consultando las tareas del usuario");
        }
        //return Response.status(response.getStatus()).entity(responseContent).build();
        return Response.status(response.getStatus()).entity(tareasPaginadas).build();
    }

    @POST
    @Path("/tareas/estadisticas/{status}")
    /*@JWTTokenSecurity
    @PermitAll*/
    public Response taskStatistic(EntradaProcesoDTO entrada, @PathParam("status") String status) {
        log.info("ProcesoGatewayApi - [trafic] - stadistics not in use tasks");
        return procesoClient.tareaEstadisticas(entrada, status);
    }

    @POST
    @Path("/tareas/iniciar")
   /* @JWTTokenSecurity
    @PermitAll*/
    public Response iniciarTarea(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - start Task");
        Response response = procesoClient.iniciarTarea(entrada);
        if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return Response.status(response.getStatus()).entity(response.readEntity(String.class)).build();


        List<TaskDto> responseContentF = response.readEntity(new GenericType<List<TaskDto>>() {
        });

        RespuestaTareaDTO responseContent = procesoClient.contructTaskResponse(responseContentF, entrada).get(0);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(AdapterTareaDTO.convertToTareaDTO(responseContent)).build();
    }

    @POST
    @Path("/tareas/reservar")
    /*@JWTTokenSecurity
    @PermitAll*/
    public Response reservarTarea(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - reserve Task");
        Response response = procesoClient.reservarTarea(entrada);
        String responseContent = response.readEntity(String.class);
        if (response.getStatus() == HttpStatus.OK.value()) {
            response = procesoClient.iniciarTarea(entrada);
            responseContent = response.readEntity(String.class);
        }

        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/tareas/completar")
    /*@JWTTokenSecurity
    @PermitAll*/
    public Response completarTarea(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - start Task");

        Response response = procesoClient.completarTarea(entrada);
        List<TaskDto> responseContent = response.readEntity(new GenericType<List<TaskDto>>() {
        });
        log.info(CONTENT + response);

        if (entrada.getParametros() != null) {

            if (entrada.getParametros().containsKey("notifications")) {

                List<HashMap<String, Object>> notifications = (List<HashMap<String, Object>>) entrada.getParametros().get("notifications");

                notificate(notifications);
            } else if (entrada.getParametros().containsKey("notifiable") || entrada.getParametros().containsKey("notifyToRol"))
                notificate(entrada);
        }

        TaskDto tarea = responseContent.get(0);

        return Response.status(response.getStatus()).entity(RespuestaTareaDTO.newInstance()
                .idTarea(Long.valueOf(tarea.getTaskId()))
                .estado(String.valueOf(EstadosEnum.COMPLETADO))
                .idProceso(tarea.getInstanceId()).build()).build();
    }

    private void notificate(EntradaProcesoDTO entrada) {

        if (entrada.getParametros() != null) {

            if (entrada.getParametros().containsKey("notifiable")) {

                NotificacionDTO notificacionDTO = getNotificationDTO(entrada);

                if (notificacionDTO != null) {
                    notificationClient.notificate(notificacionDTO);
                }
            } else if (entrada.getParametros().containsKey("notifyToRol")) {

                String rol = entrada.getParametros().get("notifyToRol").toString();
                NotificacionDTO notificacionDTO = getNotificationDTO(entrada);

                if (entrada.getParametros().containsKey("notifyToDependencia")) {

                    String codDependencia = entrada.getParametros().get("notifyToDependencia").toString();

                    if (rol != null && codDependencia != null && notificacionDTO != null)
                        notificationClient.notificateByRolAndDependency(codDependencia, rol, notificacionDTO);
                } else if (entrada.getParametros().containsKey("notifyToDependenciaList")) {

                    List dependenciaList = (ArrayList) entrada.getParametros().get("notifyToDependenciaList");


                    if (rol != null && !ObjectUtils.isEmpty(dependenciaList) && notificacionDTO != null) {

                        dependenciaList.parallelStream().forEach(dependencia -> {

                            notificationClient.notificateByRolAndDependency(dependencia.toString(), rol, notificacionDTO);
                        });
                    }
                } else if (entrada.getParametros().containsKey("onlyRol")) {

                    if (rol != null && notificacionDTO != null)
                        notificationClient.notificateToRol(rol, notificacionDTO);
                }
            }
        }
    }

    private void notificate(List<HashMap<String, Object>> notifications) {

        notifications.parallelStream().forEach(notification -> {
            if (notification.containsKey("notifyToRol")) {
                String rol = notification.get("notifyToRol").toString();
                NotificacionDTO notificacionDTO = getNotificationDTO(notification);

                if (notificacionDTO == null)
                    return;

                if (notification.containsKey("notifyToDependencia")) {

                    String codDependencia = notification.get("notifyToDependencia").toString();

                    if (rol != null && codDependencia != null)
                        notificationClient.notificateByRolAndDependency(codDependencia, rol, notificacionDTO);
                } else if (notification.containsKey("notifyToDependenciaList")) {

                    List dependenciaList = (ArrayList) notification.get("notifyToDependenciaList");


                    if (rol != null && !ObjectUtils.isEmpty(dependenciaList)) {

                        dependenciaList.parallelStream().forEach(dependencia -> {

                            notificationClient.notificateByRolAndDependency(dependencia.toString(), rol, notificacionDTO);
                        });
                    }
                } else if (notification.containsKey("onlyRol")) {

                    if (rol != null)
                        notificationClient.notificateToRol(rol, notificacionDTO);
                }
            } else {
                NotificacionDTO notificacionDTO = getNotificationDTO(notification);
                if (notificacionDTO != null) {
                    notificationClient.notificate(notificacionDTO);
                }
            }
        });


    }

    private NotificacionDTO getNotificationDTO(EntradaProcesoDTO entrada) {

        NotificacionDTO notificacionDTO = new NotificacionDTO();

        final Map<String, Object> parameters = entrada.getParametros();

        if (parameters == null)
            return null;

        if (!parameters.containsKey("notifiable") && !parameters.containsKey("notifyToRol"))
            return null;

        if (parameters.containsKey("nroRadicado"))
            notificacionDTO.setNroRadicado(parameters.get("nroRadicado").toString());

        if (parameters.containsKey("nombreTarea"))
            notificacionDTO.setNombreTarea(parameters.get("nombreTarea").toString());

        if (parameters.containsKey("destinatario"))
            notificacionDTO.setDestinatario(AdapterForNotificate.convertToAgenteDto((LinkedHashMap) parameters.get("destinatario")));

        if (parameters.containsKey("remitente"))
            notificacionDTO.setRemitente(AdapterForNotificate.convertToAgenteDto((LinkedHashMap) parameters.get("remitente")));

        return notificacionDTO;

    }

    private NotificacionDTO getNotificationDTO(HashMap<String, Object> parameters) {

        NotificacionDTO notificacionDTO = new NotificacionDTO();

        if (parameters == null)
            return null;

        if (parameters.containsKey("nroRadicado"))
            notificacionDTO.setNroRadicado(parameters.get("nroRadicado").toString());

        if (parameters.containsKey("nombreTarea"))
            notificacionDTO.setNombreTarea(parameters.get("nombreTarea").toString());

        if (parameters.containsKey("destinatario"))
            notificacionDTO.setDestinatario(AdapterForNotificate.convertToAgenteDto((LinkedHashMap) parameters.get("destinatario")));

        if (parameters.containsKey("remitente"))
            notificacionDTO.setRemitente(AdapterForNotificate.convertToAgenteDto((LinkedHashMap) parameters.get("remitente")));

        return notificacionDTO;

    }

    @POST
    @Path("/tareas/abortar")
    @JWTTokenSecurity
    @PermitAll
    public Response abortarTarea(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - start Task");
        Response response = procesoClient.abortarTarea(entrada);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/proceso/listar-instancias")
    @JWTTokenSecurity
    @RolesAllowed("Administrador")
    public Response listarIntanciasProceso(@RequestParam Integer page,
                                           @RequestParam Integer pageSize) {

        log.info("ProcesoGatewayApi - [trafic] - listing Process Instances");
        Response response = procesoClient.listarIntanciasProceso(page, pageSize);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/tareas/obtener-variables")
    /*@JWTTokenSecurity
    @PermitAll*/
    public Response obtenerVaraiblesTarea(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - get task variables");
        Response response = procesoClient.obtenerVariablesTarea(entrada);
        ParametrosDto responseContent = (ParametrosDto) response.getEntity();
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent.getValues()).build();
    }

    @POST
    @Path("/send-signal")
    /*@JWTTokenSecurity
    @PermitAll*/
    public Response sendSignal(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - send signal");
        Response response = procesoClient.sendSignal(entrada);
 /*       ParametrosDto responseContent = (ParametrosDto) response.getEntity();
        log.info(CONTENT + responseContent);*/

        return Response.status(response.getStatus()).build();
    }


}
