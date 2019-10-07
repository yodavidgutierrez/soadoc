package co.com.soaint.bpm.services.integration.services.impl;

import co.com.soaint.bpm.services.domain.entity.TaskEntity;
import co.com.soaint.bpm.services.integration.services.IProcessServices;
import co.com.soaint.bpm.services.integration.services.ITaskServices;
import co.com.soaint.bpm.services.util.EngineConexion;
import co.com.soaint.bpm.services.util.Estados;
import co.com.soaint.bpm.services.util.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.*;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hornetq.utils.json.JSONArray;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;
import org.jbpm.workflow.core.node.Split;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Arce on 8/16/2017.
 */
@Service
@Log4j2
@PropertySource("classpath:tareas-roles.properties")
public class TasksService implements ITaskServices {

    private TaskService taskService;
    @Value("${jbpm.endpoint.url}")
    private String endpointJBPConsole = "";
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
    @Value("${formato.idioma}")
    private String formatoIdioma = "";
    @Value("${protocolo}")
    private String protocolo = "";
    @Value("${tarea.querys}")
    private String tareaQuerys = "";
    private HttpClient httpClient;
    private HttpPost postRequest;
    private HttpResponse response;
    HttpGet getRequest;
    private String estado = "";
    @PersistenceContext
    private EntityManager em;
    EngineConexion engine = EngineConexion.getInstance();
    Estados estadosOperaciones = new Estados();
    @Autowired
    IProcessServices procesoOperaciones;
    @Autowired
    private Environment env;


    private TasksService() {
    }

    /**
     * Permite iniciar una tarea asociada a un proceso
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos de la tarea iniciada
     * @throws MalformedURLException
     */
    @Override
    public RespuestaTareaDTO iniciarTarea(EntradaProcesoDTO entrada) throws SystemException {
        try {
            log.info("iniciar - tarea: {}", entrada);
            taskService = engine.obtenerEngine(entrada).getTaskService();
            taskService.start(entrada.getIdTarea(), entrada.getUsuario());
            Task task = taskService.getTaskById(entrada.getIdTarea());
            return RespuestaTareaDTO.newInstance()
                    .idTarea(entrada.getIdTarea())
                    .estado(String.valueOf(EstadosEnum.ENPROGRESO))
                    .nombre(task.getName())
                    .idProceso(entrada.getIdProceso())
                    .idDespliegue(entrada.getIdDespliegue())
                    .idParent(task.getTaskData().getParentId())
                    .idResponsable(task.getTaskData().getActualOwner().getId())
                    .idInstanciaProceso(task.getTaskData().getProcessInstanceId())
                    .tiempoExpiracion(task.getTaskData().getExpirationTime())
                    .tiempoActivacion(task.getTaskData().getActivationTime())
                    .fechaCreada(task.getTaskData().getCreatedOn())
                    .prioridad(task.getPriority())
                    .descripcion(task.getDescription())
                    .build();

        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - iniciar - tarea ");
        }

    }

    /**
     * Permite completar una tarea asociada a un proceso
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos de la tarea completada
     * @throws MalformedURLException
     */
    @Override
    public RespuestaTareaDTO completarTarea(EntradaProcesoDTO entrada) throws SystemException {
        try {
            log.info("iniciar - tarea: {}", entrada);
            taskService = engine.obtenerEngine(entrada).getTaskService();
            taskService.complete(entrada.getIdTarea(), entrada.getUsuario(), entrada.getParametros());
            return RespuestaTareaDTO.newInstance()
                    .idTarea(entrada.getIdTarea())
                    .estado(String.valueOf(EstadosEnum.COMPLETADO))
                    .idProceso(entrada.getIdProceso())
                    .build();
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - iniciar - tarea ");
        }

    }

    /**
     * Permite reservar una tarea asociada a un proceso
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos de la tarea reservada
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */
    @Override
    public RespuestaTareaDTO reservarTarea(EntradaProcesoDTO entrada) throws SystemException {
        String encoding = java.util.Base64.getEncoder().encodeToString(new String(entrada.getUsuario() + ":" + entrada.getPass()).getBytes());
        log.info("iniciar - reservar tarea: {}", entrada);
        try {
            URI uri = new URIBuilder(protocolo.concat(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT)))
                    .setPath("/jbpm-console/rest/task/" + entrada.getIdTarea() + "/claim")
                    .build();
            httpClient = HttpClientBuilder.create().build();
            postRequest = new HttpPost(uri);
            postRequest.addHeader(headerAuthorization, valueAuthorization + " " + encoding);
            postRequest.addHeader(headerAccept, valueApplicationType);
            response = httpClient.execute(postRequest);
            JSONObject respuestaJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            if (response.getStatusLine().getStatusCode() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatusLine().getStatusCode())
                        .buildBusinessException();
            } else {
                String estadoResp = respuestaJson.getString("status");

                if ("SUCCESS".equals(estadoResp)) {
                    estado = String.valueOf(EstadosEnum.ENPROGRESO);
                } else {
                    estado = String.valueOf(EstadosEnum.ERROR);
                }
            }
            return RespuestaTareaDTO.newInstance()
                    .idTarea(entrada.getIdTarea())
                    .estado(estado)
                    .idProceso(entrada.getIdProceso())
                    .idDespliegue(entrada.getIdDespliegue())
                    .build();
        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - reservar - tarea ");
        }


    }

    /**
     * Permite reasignar una tarea a otro usuario
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos de la tarea reservada
     * @throws MalformedURLException
     */
    @Override
    public RespuestaTareaDTO reasignarTarea(EntradaProcesoDTO entrada) throws SystemException {
        try {
            log.info("iniciar - reasignar tarea: {}", entrada);
            URI uri = new URIBuilder(protocolo.concat(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT)))
                    .setPath("/jbpm-console/rest/task/" + entrada.getIdTarea() + "/delegate")
                    .addParameter("targetEntityId", entrada.getParametros().get("usuarioReasignar").toString())
                    .build();
            httpClient = HttpClientBuilder.create().build();
            postRequest = new HttpPost(uri);
            postRequest.addHeader(headerAuthorization, valueAuthorization + " " + entrada.getPass());
            postRequest.addHeader(headerAccept, valueApplicationType);
            response = httpClient.execute(postRequest);
            JSONObject respuestaJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            if (response.getStatusLine().getStatusCode() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatusLine().getStatusCode())
                        .buildBusinessException();
            } else {
                String estadoResp = respuestaJson.getString("status");

                if ("SUCCESS".equals(estadoResp)) {
                    estado = String.valueOf(EstadosEnum.RESERVADO);
                } else {
                    estado = String.valueOf(EstadosEnum.ERROR);
                }
            }
            return RespuestaTareaDTO.newInstance()
                    .idTarea(entrada.getIdTarea())
                    .estado(estado)
                    .idProceso(entrada.getIdProceso())
                    .idDespliegue(entrada.getIdDespliegue())
                    .build();
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - reasignar - tarea ");
        }
    }

    /**
     * Permite listar las tareas por estados
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas que cumplen con los filtros de estado solicitdos
     * @throws MalformedURLException
     */
    @Override
    public List<RespuestaTareaDTO> listarTareasEstados(EntradaProcesoDTO entrada) throws SystemException {
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        try {
            log.info("iniciar - listar tareas estados: {}", entrada);
            taskService = engine.obtenerEngine(entrada).getTaskService();
            List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(entrada.getUsuario(), formatoIdioma);
            log.info("iniciar - listar taks--------------------------------------------------------------: {}", tasks.toString());
            String descripcion="";
            for (TaskSummary task : tasks) {
                log.info("cantidad de tareas-----------------{}",tasks.size());

                entrada.setInstanciaProceso(task.getProcessInstanceId());
                entrada.setIdDespliegue(task.getDeploymentId());
                JSONObject respuestaJson = new JSONObject(procesoOperaciones.listarVariablesProcesos(entrada));
                JSONObject valor = respuestaJson.getJSONObject("variables");
                //consumiendo servicio que devuelve el funcionario dado un id del funcionario
                if(task.getName().equalsIgnoreCase("Crear Unidad Documental")) {
                    WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT_CORRESPONDENCIA));
                    Response response = wt
                            .path("/funcionarios-web-api/funcionarios/by-id/" + valor.getString("idSolicitante"))
                            .request()
                            .get();
                    if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode()) {
                        throw new SystemException("Ocurrio un error al consumir" +
                                " servicio correspondencia, codigo status: " + response.getStatus());
                    }
                    FuncionarioDTO funcionario = response.readEntity(FuncionarioDTO.class);
                   descripcion=formatDescripcion(task.getDescription());
                    String nombreFuncionario ="";
                    String apellido1Funcionario = "";
                    String apellido2Funcionario = "";
                    if(!ObjectUtils.isEmpty(funcionario)) {
                        nombreFuncionario = (ObjectUtils.isEmpty(funcionario.getNomFuncionario()) ? "" : funcionario.getNomFuncionario());
                        apellido1Funcionario = (ObjectUtils.isEmpty(funcionario.getValApellido1()) ? "" : funcionario.getValApellido1());
                        apellido2Funcionario = (ObjectUtils.isEmpty(funcionario.getValApellido2()) ? "" : funcionario.getValApellido2());
                    }
                    RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                            .idTarea(task.getId())
                            .estado(estadosOperaciones.estadoRespuesta(task.getStatusId()))
                            .idProceso(task.getProcessId())
                            .idDespliegue(task.getDeploymentId())
                            .nombre(task.getName())
                            .prioridad(task.getPriority())
                            .idInstanciaProceso(task.getProcessInstanceId())
                            .fechaCreada(task.getCreatedOn())
                            .tiempoActivacion(task.getActivationTime())
                            .tiempoExpiracion(task.getExpirationTime())
                            .descripcion(descripcion+" "+ nombreFuncionario+ " "+apellido1Funcionario+" "+apellido2Funcionario+" "+DateFormat.getDateInstance().format(task.getCreatedOn()))
                            .codigoDependencia(valor.getString("codDependencia"))
                            .idCreador(valor.getString("idSolicitante"))
                            .rol(!StringUtils.isEmpty(task.getName()) ? env.getProperty(task.getName().replaceAll(" ", ".")) : "")
                            .build();
                    tareas.add(respuestaTarea);
                }
                else{
                    descripcion=formatDescripcion(task.getDescription());
                    RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                            .idTarea(task.getId())
                            .estado(estadosOperaciones.estadoRespuesta(task.getStatusId()))
                            .idProceso(task.getProcessId())
                            .idDespliegue(task.getDeploymentId())
                            .nombre(task.getName())
                           // .idCreador(valor.getString("idSolicitante"))
                            .idResponsable(task.getActualOwnerId())
                            .prioridad(task.getPriority())
                            .idInstanciaProceso(task.getProcessInstanceId())
                            .fechaCreada(task.getCreatedOn())
                            .tiempoActivacion(task.getActivationTime())
                            .tiempoExpiracion(task.getExpirationTime())
                            .descripcion(descripcion)
                            .codigoDependencia(valor.getString("codDependencia"))
                            .rol(!StringUtils.isEmpty(task.getName()) ? env.getProperty(task.getName().replaceAll(" ", ".")) : "")
                            .build();
                    tareas.add(respuestaTarea);
                }
            }
            log.info("lista de tareas por usuario y dependencia -----------------------------{}", tareas);
            return tareas;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar - tareas - estados ");
        }
    }

    /**
     * Permite listar las tareas asociadas a una instancia de procesos por sus estados
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas
     * @throws MalformedURLException
     */
    @Override
    public List<RespuestaTareaDTO> listarTareasEstadosInstanciaProceso(EntradaProcesoDTO entrada) throws SystemException {
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
        List<Status> estadosActivos = estadosOperaciones.estadosActivos(estadosEnviados);
        String descripcion="";
        try {
            log.info("iniciar - listar tareas estados instancias proceso: {}", entrada);
            taskService = engine.obtenerEngine(entrada).getTaskService();
            List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(entrada.getUsuario(), estadosActivos, formatoIdioma);
            for (TaskSummary task : tasks) {
                if (task.getProcessInstanceId().longValue() == entrada.getInstanciaProceso().longValue()) {
                    descripcion=formatDescripcion(task.getDescription());
                    RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                            .idTarea(task.getId())
                            .estado(estadosOperaciones.estadoRespuesta(task.getStatusId()))
                            .idProceso(task.getProcessId())
                            .idDespliegue(task.getDeploymentId())
                            .nombre(task.getName())
                            .prioridad(task.getPriority())
                            .idInstanciaProceso(task.getProcessInstanceId())
                            .fechaCreada(task.getCreatedOn())
                            .tiempoActivacion(task.getActivationTime())
                            .tiempoExpiracion(task.getExpirationTime())
                            .descripcion(descripcion)
                            .build();
                    tareas.add(respuestaTarea);
                }
            }
            return tareas;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar - tareas estados instandias proceso ");
        }
    }


    /**
     * Permite listar tareas asociados a usuarios por estados
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas
     * @throws MalformedURLException
     */

    //###################modificando para obtener el listado de tareas por los estados del usuario pasado como parámetro ###############################

    //  esta propiedad tiene que estar en el servidor donde el kieserver.war esta desplegado <system-properties> <property name="org.kie.server.bypass.auth.user" value="true"/>
    @Override
    public List<RespuestaTareaDTO> listarTareasEstadosPorUsuario(EntradaProcesoDTO entrada) throws SystemException {
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
        List<Status> estadosActivos = estadosOperaciones.estadosActivos(estadosEnviados);

        try {
            log.info("iniciar - listar tareas estados usuarios: {}", entrada);
            taskService = engine.obtenerEngine(entrada).getTaskService();
           String usuario =entrada.getParametros().get("usuario").toString();
           String descripcion="";
            List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(usuario, estadosActivos, formatoIdioma);
            for (TaskSummary task : tasks) {
                descripcion=formatDescripcion(task.getDescription());
                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(task.getId())
                        .estado(estadosOperaciones.estadoRespuesta(task.getStatusId()))
                        .idProceso(task.getProcessId())
                        .idDespliegue(task.getDeploymentId())
                        .nombre(task.getName())
                        .prioridad(task.getPriority())
                        .idInstanciaProceso(task.getProcessInstanceId())
                        .fechaCreada(task.getCreatedOn())
                        .tiempoActivacion(task.getActivationTime())
                        .tiempoExpiracion(task.getExpirationTime())
                        .descripcion(descripcion)
                        .rol(!StringUtils.isEmpty(task.getName()) ? env.getProperty(task.getName().replaceAll(" ", ".")) : "")
                        .build();
                tareas.add(respuestaTarea);
            }
            return tareas;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar tareas estados usuarios ");
        }
    }

    /**
     * Listar tareas por instancia de proceso
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas
     * @throws MalformedURLException
     */
    @Override

    public List<RespuestaTareaDTO> listarTareasPorInstanciaProceso(EntradaProcesoDTO entrada) throws SystemException {

        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
        List<Status> estadosActivos = estadosOperaciones.estadosActivos(estadosEnviados);
        try {
            log.info("iniciar - listar tareas instancias proceso: {}", entrada);
            taskService = engine.obtenerEngine(entrada).getTaskService();
            String descripcion="";
            List<TaskSummary> tasks = taskService.getTasksByStatusByProcessInstanceId(entrada.getInstanciaProceso(), estadosActivos, formatoIdioma);
            for (TaskSummary task : tasks) {
                descripcion=formatDescripcion(task.getDescription());
                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(task.getId())
                        .estado(estadosOperaciones.estadoRespuesta(task.getStatusId()))
                        .idProceso(task.getProcessId())
                        .idDespliegue(task.getDeploymentId())
                        .nombre(task.getName())
                        .prioridad(task.getPriority())
                        .idInstanciaProceso(task.getProcessInstanceId())
                        .fechaCreada(task.getCreatedOn())
                        .tiempoActivacion(task.getActivationTime())
                        .tiempoExpiracion(task.getExpirationTime())
                        .descripcion(descripcion)
                        .build();
                tareas.add(respuestaTarea);

            }
            return tareas;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar - tareas estados instancias proceso ");
        }
    }


    /**
     * Permite listar las tareas por estados
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas que cumplen con los filtros de estado solicitdos
     * @throws MalformedURLException
     */
    @Override
    public RespuestaTareaDTO listarTareasCompletadas(EntradaProcesoDTO entrada) throws SystemException {

        try {
            log.info("iniciar - listar tareas completadas por usuario: {}", entrada);

           List<RespuestaTareaBamDTO> tareas= em.createNamedQuery("BamTaskSummary.findTaskComplete", RespuestaTareaBamDTO.class)
                    .setParameter("ESTADO", Status.Completed.name())
                    .setParameter("USUARIO", entrada.getUsuario())
                    .getResultList();

            int cont=0;
            Calendar calendar = GregorianCalendar.getInstance();
            for (RespuestaTareaBamDTO tarea:tareas) {
                if(numeroDiasEntreDosFechas(tarea.getStartdate(),calendar.getTime())<=1){
                    cont++;
                }
            }
            RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                    .estado("COMPLETADO")
                    .idTarea(Long.valueOf(cont))
                    .idCreador(entrada.getUsuario())
                    .build();
            return respuestaTarea;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar tareas completadas por usuario ");
        }
    }

    /**
     * Permite listar las tareas por usuario
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas que cumplen con los filtros de estado solicitdos
     * @throws MalformedURLException
     */
    @Override
    public List<RespuestaTareaBamDTO> listarTareasPorUsuario(EntradaProcesoDTO entrada) throws SystemException {

        try {
            log.info("iniciar - listar tareas  por usuario: {}", entrada);

            return em.createNamedQuery("TaskEventEntity.findTaskByUser", RespuestaTareaBamDTO.class)
                    .setParameter("USUARIO", entrada.getParametros().get("usuario").toString())
                    .getResultList();

        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar tareas  por usuario ");
        }
    }

    /**
     * Listar tareas por instancia de proceso
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas
     * @throws MalformedURLException
     */
    @Override

    public List<RespuestaTareaDTO> listarTareasPorUsuarioAsignado(EntradaProcesoDTO entrada) throws SystemException {

        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        try {
            log.info("iniciar - listar tareas asignadas: {}", entrada);

            httpClient = HttpClientBuilder.create().build();

            URI uri = new URIBuilder(protocolo.concat(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT)).concat(tareaQuerys))
                    .addParameter("taskOwner", entrada.getParametros().get("usuario").toString())
                    .addParameter("processInstanceId", String.valueOf(entrada.getInstanciaProceso()))
                            .build();
            log.info("imprimiendo objeto uri----------------------------: {}", uri.toString());
            getRequest = new HttpGet(uri);
            getRequest.addHeader(headerAccept, valueApplicationType);
            getRequest.addHeader(headerAuthorization, valueAuthorization + " " + entrada.getPass());

            try {
                response = httpClient.execute(getRequest);
            }catch (Exception ex){
                throw new SystemException(ex.getMessage());
            }

            log.info("imprimiendo objeto repsonse----------------------------: {}", response.toString());
            JSONObject respuestaJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            if (response.getStatusLine().getStatusCode() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatusLine().getStatusCode())
                        .buildBusinessException();
            } else {
                JSONArray listaProcesosJson = respuestaJson.getJSONArray("taskSummaryList");
                for (int i = 0; i < listaProcesosJson.length(); i++) {
                JSONObject valor = (JSONObject) listaProcesosJson.get(i);
                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(valor.getLong("id"))
                        .estado(estadosOperaciones.estadoRespuesta(valor.getString("status")))
                        .nombre(valor.getString("name"))
                        .descripcion(valor.getString("description"))
                        .idDespliegue(valor.getString("deployment-id"))
                        .idInstanciaProceso(valor.getLong("process-instance-id"))
                        .idProceso(valor.getString("process-id"))
                        .build();
                tareas.add(respuestaTarea);
                }
            }
            return tareas;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar - tareas estados instancias proceso ");
        }
    }



    /**
     * cambiar el propietario de una tarea
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return los datos de la tarea a devolver
     * @throws MalformedURLException
     */
   @Override

    public RespuestaTareaDTO nominarTareaPorusuario(EntradaProcesoDTO entrada) throws SystemException {

        try {
            log.info("iniciar nominar tarea por usuario", entrada);
            taskService = engine.obtenerEngine(entrada).getTaskService();

           String usuarioReasignar = entrada.getParametros().get("usuarioReasignar").toString();
            taskService.delegate(entrada.getIdTarea(),entrada.getUsuario(),usuarioReasignar);
            String descripcion="";
            Task task = taskService.getTaskById(entrada.getIdTarea());
            descripcion=formatDescripcion(task.getDescription());
                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(entrada.getIdTarea())
                        .estado(estadosOperaciones.estadoRespuesta(task.getTaskData().getStatus().toString()))
                        .idProceso(task.getTaskData().getProcessId())
                        .idDespliegue(task.getTaskData().getDeploymentId())
                        .nombre(task.getName())
                        .prioridad(task.getPriority())
                        .idInstanciaProceso(task.getTaskData().getProcessInstanceId())
                        .fechaCreada(task.getTaskData().getCreatedOn())
                        .tiempoActivacion(task.getTaskData().getActivationTime())
                        .tiempoExpiracion(task.getTaskData().getExpirationTime())
                        .descripcion(descripcion)
                        .build();



            return respuestaTarea;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - nominar tarea a usuario ");
        }
    }
@Override
@Transactional
    public RespuestaTareaBamDTO reasignarTareaPorTercero(EntradaProcesoDTO entrada) throws SystemException {

        try {
            log.info("iniciar - reasignar: {}", entrada);

            int i = em.createNamedQuery("TaskEntity.delegate")
                    .setParameter("id", entrada.getIdTarea())
                    .setParameter("actualowner_id", entrada.getParametros().get("usuarioReasignar").toString())
                    .executeUpdate();
            System.out.println("++++++++++++++"+i);

List<RespuestaTareaBamDTO> tareas =em.createNamedQuery("TaskEntity.getTaskByID",RespuestaTareaBamDTO.class)
        .setParameter("id", entrada.getIdTarea())
        .getResultList();
            if(!tareas.isEmpty())
            return tareas.get(0);
            return RespuestaTareaBamDTO.newInstance().build();
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar tareas  por usuario ");
        }
    }

    /**
     * Permite listar las tareas en progreso por usuario
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return RespuestaTarea
     * @throws MalformedURLException
     */
    @Override
    public RespuestaTareaDTO listarTareasEnProgresoPorUsuario(EntradaProcesoDTO entrada) throws SystemException {

        try {
            log.info("iniciar - muestra cantidad tareas  por usuario por estado: {}", entrada);


            List<RespuestaTareaBamDTO>  tareas=  em.createNamedQuery("TaskEntity.inProgress", RespuestaTareaBamDTO.class)
                    .setParameter("USUARIO", entrada.getParametros().get("usuario").toString())
                    .setParameter("STATUS", "InProgress")
                    .getResultList();
            int cont=0;
            Calendar calendar = GregorianCalendar.getInstance();
            for (RespuestaTareaBamDTO tarea:tareas) {
               if(numeroDiasEntreDosFechas(tarea.getStartdate(),calendar.getTime())<=1){
                   cont++;
               }
            }
            RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                    .estado("ENPROGRESO")
                    .idTarea(Long.valueOf(cont))
                    .idCreador(entrada.getUsuario())
                    .build();
            return respuestaTarea;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar tareas  por usuario en progreso");
        }
    }

    @Override
    public RespuestaTareaDTO listarTareasEnListoPorUsuario(EntradaProcesoDTO entrada) throws SystemException {

        try {
            log.info("iniciar - muestra cantidad tareas  por usuario por estado: {}", entrada);


            List<RespuestaTareaBamDTO>  tareas=  em.createNamedQuery("TaskEntity.ready", RespuestaTareaBamDTO.class)
                    .setParameter("USUARIO", entrada.getParametros().get("usuario").toString())
                    .setParameter("STATUS", "Ready")
                    .getResultList();
            int cont=0;
            Calendar calendar = GregorianCalendar.getInstance();
            for (RespuestaTareaBamDTO tarea:tareas) {
                if(numeroDiasEntreDosFechas(tarea.getStartdate(),calendar.getTime())<=1){
                    cont++;
                }
            }

            RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                    .estado("LISTO")
                    .idTarea(Long.valueOf(cont))
                    .idCreador(entrada.getUsuario())
                    .build();
            return respuestaTarea;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar tareas  listas por usuario ");
        }
    }

    @Override
    public List<RespuestaTareaDTO> listarTareasPorTercero(EntradaProcesoDTO entrada) throws SystemException {

        try {
            log.info("iniciar - muestra cantidad tareas  por usuario por estado: {}", entrada);

            String descripcion = "";
            String estado = "";
            List<TaskEntity>  tareas=  em.createNamedQuery("TaskEntity.findAllByUser", TaskEntity.class)
                    .setParameter("USUARIO", entrada.getUsuario())
                   // .setParameter("USUARIO", entrada.getParametros().get("usuario").toString())
                 //   .setParameter("READY", "Ready")
                    .setParameter("INPROGESS", "InProgress")
                    .setParameter("RESERVERD", "Reserved")
                    .getResultList();
            List<TaskEntity>  tareaslistas=  em.createNamedQuery("TaskEntity.readyWithoutUser", TaskEntity.class)
                    .setParameter("STATUS", "Ready")
                    .getResultList();

            tareas.addAll(tareaslistas);
            List<RespuestaTareaDTO> respuestasDtos = new ArrayList<>();
            for (TaskEntity respuesta:tareas) {
                EntradaProcesoDTO entradaProcesoDTO = EntradaProcesoDTO.newInstance()
                        .instanciaProceso(respuesta.getProcessinstanceid())
                        .idDespliegue(respuesta.getDeploymentid())
                        .build();
                switch (respuesta.getStatus()){
                    case "InProgress":estado="ENPROGRESO";
                    case "Reserved":estado="RESERVADO";
                    default :estado="LISTO";
                }
                log.info("############################" + entradaProcesoDTO + "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                JSONObject respuestaJson = new JSONObject(procesoOperaciones.listarVariablesProcesos(entradaProcesoDTO));
                JSONObject valor = respuestaJson.getJSONObject("variables");
                if (respuesta.getTaskname().equalsIgnoreCase("Crear Unidad Documental")) {
                    WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT_CORRESPONDENCIA));
                    Response response = wt
                            .path("/funcionarios-web-api/funcionarios/by-id/" + valor.getString("idSolicitante"))
                            .request()
                            .get();
                    if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode()) {
                        throw new SystemException("Ocurrio un error al consumir" +
                                " servicio correspondencia, codigo status: " + response.getStatus());
                    }
                    FuncionarioDTO funcionario = response.readEntity(FuncionarioDTO.class);
                    descripcion = formatDescripcion(respuesta.getDescription());
                    String nombreFuncionario = "";
                    String apellido1Funcionario = "";
                    String apellido2Funcionario = "";
                    if (!ObjectUtils.isEmpty(funcionario)) {
                        nombreFuncionario = (ObjectUtils.isEmpty(funcionario.getNomFuncionario()) ? "" : funcionario.getNomFuncionario());
                        apellido1Funcionario = (ObjectUtils.isEmpty(funcionario.getValApellido1()) ? "" : funcionario.getValApellido1());
                        apellido2Funcionario = (ObjectUtils.isEmpty(funcionario.getValApellido2()) ? "" : funcionario.getValApellido2());
                    }
                    RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                            .idTarea(respuesta.getId())
                            .nombre(respuesta.getTaskname())
                            .estado(estado)
                            .idResponsable(respuesta.getActualowner_id())
                            .idCreador(respuesta.getCreatedby_id())
                            .fechaCreada(respuesta.getCreatedon())
                            .tiempoActivacion(respuesta.getActivationtime())
                            .tiempoExpiracion(respuesta.getExpirationtime())
                            .idProceso(respuesta.getProcessid())
                            .idInstanciaProceso(respuesta.getProcessinstanceid())
                            .idDespliegue(respuesta.getDeploymentid())
                            .descripcion(descripcion + " " + nombreFuncionario + " " + apellido1Funcionario + " " + apellido2Funcionario + " " + DateFormat.getDateInstance().format(respuesta.getCreatedon()))
                            .codigoDependencia(valor.getString("codDependencia"))
                            .rol(!StringUtils.isEmpty(respuesta.getTaskname()) ? env.getProperty(respuesta.getTaskname().replaceAll(" ", ".")) : "")
                            .build();
                    respuestasDtos.add(respuestaTarea);
                } else {
                    RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                            .idTarea(respuesta.getId())
                            .nombre(respuesta.getTaskname())
                            .estado(estado)
                            .idResponsable(respuesta.getActualowner_id())
                            .idCreador(respuesta.getCreatedby_id())
                            .fechaCreada(respuesta.getCreatedon())
                            .tiempoActivacion(respuesta.getActivationtime())
                            .tiempoExpiracion(respuesta.getExpirationtime())
                            .idProceso(respuesta.getProcessid())
                            .idInstanciaProceso(respuesta.getProcessinstanceid())
                            .idDespliegue(respuesta.getDeploymentid())
                            .descripcion(respuesta.getDescription())
                            .codigoDependencia(valor.getString("codDependencia"))
                            .rol(!StringUtils.isEmpty(respuesta.getTaskname()) ? env.getProperty(respuesta.getTaskname().replaceAll(" ", ".")) : "")
                            .build();
                    respuestasDtos.add(respuestaTarea);
                }
            }

            return respuestasDtos;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar tareas  listas por usuario ");
        }
    }

    public static int numeroDiasEntreDosFechas(Date fecha1, Date fecha2){
        long startTime = fecha1.getTime();
        long endTime = fecha2.getTime();
        long diffTime = endTime - startTime;
        long diffDays = diffTime / (1000 * 60 * 60 * 24);
        return (int)diffDays;
    }
  public static String formatDescripcion(String descripcionGeneral){

      String descripcion="";
        if(descripcionGeneral.contains("--")){
      String [] descripcionRadicado = descripcionGeneral.split("--");
       descripcion= descripcionRadicado[0];
      String radicado = descripcionRadicado[1];
      String [] descripciones = descripcion.split(" ");
      descripcion="";
      for (int i=0; i<descripciones.length-1;i++){
          if (!descripciones[i].trim().equals(""))
              descripcion+= descripciones[i]+" ";
      }
      descripcion+=radicado;
      return descripcion;
  }
  else {
      descripcion=descripcionGeneral;
      return descripcion;
        }
  }

    public static void main(String[] args) {

    }

}
