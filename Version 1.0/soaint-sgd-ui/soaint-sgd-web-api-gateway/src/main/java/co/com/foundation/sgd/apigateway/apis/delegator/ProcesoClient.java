package co.com.foundation.sgd.apigateway.apis.delegator;


import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.SystemException;
import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.Futures;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EstadosEnum;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.ItemDevolucionDTO;
import co.com.soaint.foundation.canonical.jbpm.ParametrosDto;
import co.com.soaint.foundation.canonical.jbpm.ProcessRequestDto;
import co.com.soaint.foundation.canonical.jbpm.SignalDto;
import co.com.soaint.foundation.canonical.jbpm.ProcessesDto;
import co.com.soaint.foundation.canonical.jbpm.TaskDto;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * Cliente para servicios relacionados a los procesos del bpm
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
@PropertySources(@PropertySource("classpath:/tareas-roles.properties"))
public class ProcesoClient {


    @Autowired
    @Qualifier("dfrThreadPoolTaskExecutor")
    private Executor executor;

    @Autowired
    Environment environment;

    private String endpointOld = SystemParameters.getParameter(SystemParameters.BACKAPI_ENTERPRISE_SERVICE_ENDPOINT_URL);
    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENTERPRISE_SERVICE_ENDPOINT_URL_15);

    //private Client client = ClientBuilder.newClient();

/*    public Response list() {
        log.info("Proccess - [trafic] - listing Proccess with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = new EntradaProcesoDTO();
        return wt.path("/bpm/proceso/listar")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }*/


    public Response list(HttpServletRequest request) {
        log.info("Proccess - [trafic] - listing Proccess with endpoint: " + endpoint);


        Client client = ClientBuilder.newClient();
        WebTarget wt = client.target(endpoint);

        return wt.path("/processes")
                .request()
                .header("Authorization", request.getHeader("authorization"))
                .get();
    }

/*    public Response iniciar(EntradaProcesoDTO entradaProcesoDTO) {
        log.info("Proccess - [trafic] - starting Proccess with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/proceso/iniciar")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }*/

    public Response iniciar(EntradaProcesoDTO entradaProcesoDTO) {
        log.info("Proccess - [trafic] - starting Proccess with endpoint: " + endpoint);
        HttpAuthenticationFeature autentificacion = HttpAuthenticationFeature.basic("pruebasoaint2", "Soaint2@");
        Client client = ClientBuilder.newClient();
        client.register(autentificacion);
        WebTarget wt = client.target(endpoint);
        ProcessRequestDto processRequestDto = new ProcessRequestDto();
        processRequestDto.setProcessesId(entradaProcesoDTO.getIdProceso());
        processRequestDto.setParametros(ParametrosDto.newInstance().values(entradaProcesoDTO.getParametros()).build());

        return wt.path("/processes/" + entradaProcesoDTO.getIdProceso() + "/instances")
                .request()
                .post(Entity.json(processRequestDto.getParametros().getValues()));
    }

/*    public Future<Response> inciarAsync(EntradaProcesoDTO entradaProcesoDTO,InvocationCallback<Response> callback){

        log.info("Proccess - [trafic] - starting Proccess with endpoint: " + endpoint);

        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/proceso/iniciar")
                .request()
                .async()
                .post(Entity.json(entradaProcesoDTO),callback);
    }*/

    public Future<Response> inciarAsync(EntradaProcesoDTO entradaProcesoDTO, InvocationCallback<Response> callback) {
        log.info("Proccess - [trafic] - starting Proccess with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        ProcessRequestDto processRequestDto = new ProcessRequestDto();
        processRequestDto.setProcessesId(entradaProcesoDTO.getIdProceso());
        processRequestDto.setParametros(ParametrosDto.newInstance().values(entradaProcesoDTO.getParametros()).build());

        wt.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, entradaProcesoDTO.getUsuario())
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, entradaProcesoDTO.getPass());
        return wt.path("/processes/" + entradaProcesoDTO.getIdProceso() + "/instances")
                .request()
                .async()
                .post(Entity.json(processRequestDto.getParametros().getValues()), callback);
    }

    //nuevo api
    public Future<Response> inciarAsync(EntradaProcesoDTO entrada) {

        log.info("Proccess - [trafic] - starting Proccess with endpoint: " + endpoint);

        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        wt.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, entrada.getUsuario())
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, entrada.getPass());

        ProcessRequestDto processRequestDto = new ProcessRequestDto();
        processRequestDto.setProcessesId(entrada.getIdProceso());
        processRequestDto.setParametros(ParametrosDto.newInstance().values(entrada.getParametros()).build());

        return wt.path("/processes/" + entrada.getIdProceso() + "/instances")
                .request()
                .async()
                .post(Entity.json(processRequestDto.getParametros().getValues()));
    }


    public Response iniciarTercero(EntradaProcesoDTO entradaProcesoDTO) throws Exception {
        log.info("Proccess - [trafic] - starting Proccess with endpoint: " + endpoint);
        if (StringUtils.isEmpty(entradaProcesoDTO.getPass()) || StringUtils.isEmpty(entradaProcesoDTO.getUsuario())){
            throw new Exception("usuario o contraseña incorrectos");
        }
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        wt.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, entradaProcesoDTO.getUsuario())
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, entradaProcesoDTO.getPass());

        ProcessRequestDto processRequestDto = new ProcessRequestDto();
        processRequestDto.setProcessesId(entradaProcesoDTO.getIdProceso());
        processRequestDto.setParametros(ParametrosDto.newInstance().values(entradaProcesoDTO.getParametros()).build());


        return wt.path("/processes/" + processRequestDto.getProcessesId() + "/instances")
                .request()
                .post(Entity.json(processRequestDto.getParametros().getValues()));
    }


    public Response iniciarProcesoGestorDevoluciones(ItemDevolucionDTO itemDevolucion, EntradaProcesoDTO entradaProceso, String codDependencia) throws Exception {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("numeroRadicado", itemDevolucion.getCorrespondencia().getNroRadicado());
        parametros.put("causalD", itemDevolucion.getCausalDevolucion());
        parametros.put("funDevuelve", itemDevolucion.getFunDevuelve());
        parametros.put("fechaVencimiento", itemDevolucion.getCorrespondencia().getFecVenGestion());
        parametros.put("idAgente", itemDevolucion.getAgente().getIdeAgente().toString());
        parametros.put("estadoFinal", itemDevolucion.getAgente().getCodEstado());
        parametros.put("codDependencia", codDependencia);

        log.info(" parametros para gestor de devoluciones:" + parametros);
        entradaProceso.setParametros(parametros);
        return this.iniciarTercero(entradaProceso);
    }


/*    public Response iniciarManual(EntradaProcesoDTO entradaProcesoDTO) {
        log.info("Proccess - [trafic] - manual starting Proccess with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/proceso/iniciar/manual")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }*/

    public Response iniciarManual(EntradaProcesoDTO entradaProcesoDTO) {
        log.info("Proccess - [trafic] - manual starting Proccess with endpoint: " + endpoint);
        HttpAuthenticationFeature autentificacion = HttpAuthenticationFeature.basic(entradaProcesoDTO.getUsuario(), entradaProcesoDTO.getPass());
        Client client = ClientBuilder.newClient();
        client.register(autentificacion);
        WebTarget wt = client.target(endpoint);
        ProcessRequestDto processRequestDto = new ProcessRequestDto();
        processRequestDto.setProcessesId(entradaProcesoDTO.getIdProceso());
        processRequestDto.setParametros(ParametrosDto.newInstance().values(entradaProcesoDTO.getParametros()).build());

        return wt.path("/processes/"+ entradaProcesoDTO.getIdProceso() +"/instances")
                .request()
                .post(Entity.json(processRequestDto.getParametros()));
    }

    /*
    public Response listarPorIdProceso(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - listing Tasks By Id Proccess with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = entrada;
        return wt.path("/bpm/tareas/listar/estados-instancia/")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }*/

    public Response listarPorIdProceso(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - listing Tasks By Id Proccess with endpoint: " + endpoint);
        HttpAuthenticationFeature autentificacion = HttpAuthenticationFeature.basic(entrada.getUsuario(), entrada.getPass());
        Client client = ClientBuilder.newClient();
        client.register(autentificacion);
        WebTarget wt = client.target(endpoint);

        wt = wt.path("processes/" + entrada.getIdProceso() + "/instances/" + entrada.getInstanciaProceso() + "/tasks");
        for (EstadosEnum estado : entrada.getEstados()
        ) {
            wt = wt.queryParam("states", estado.getNombre());
        }

        String uri = wt.getUri().toString();

        return wt.request()
                .get();
    }

    /*
    public Response listarPorUsuarioYPorIdProceso(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - listing Tasks By Usuario y Id Proccess with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = entrada;
        return wt.path("/bpm/tareas/listar/asignado/")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }*/

    public Response listarPorUsuarioYPorIdProceso(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - listing Tasks By Usuario y Id Proccess with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        wt.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, entrada.getUsuario())
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, entrada.getPass());
        return wt.path("processes/" + entrada.getIdProceso() + "/instances/" + entrada.getInstanciaProceso() + "/tasks")
                .queryParam("page", entrada.getPage())
                .queryParam("pageSize", entrada.getPageSize())
                .queryParam("states", entrada.getEstados())
                .request()
                .get();
    }

    /*
    public Response listarTareas(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - listing Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/listar/estados/")
                .request()
                .post(Entity.json(entrada));
    }*/

    public Response listarTareas(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - listing Task with endpoint: " + endpoint);
        HttpAuthenticationFeature autentificacion = HttpAuthenticationFeature.basic(entrada.getUsuario(), entrada.getPass());
        Client client = ClientBuilder.newClient();
        client.register(autentificacion);

        WebTarget wt = client.target(endpoint).path("queries/tasks");
        for (EstadosEnum estado : entrada.getEstados()
        ) {
            wt = wt.queryParam("states", estado.getNombre());
        }

      wt  = wt.queryParam("page", entrada.getPage())
                .queryParam("pageSize", entrada.getPageSize())
                .queryParam("codDependencia", entrada.getParametros().get("codDependencia"))
                .queryParam("query", "groups");

        Response res = wt.request().get();

        String uri = wt.getUri().toString();
        return res;
    }

    /*
    public Response listarTareasCompletadas(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - listing Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/listar/completadas")
                .request()
                .post(Entity.json(entrada));
    }*/

    public Response listarTareasCompletadas(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - listing Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        wt.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, entrada.getUsuario())
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, entrada.getPass());
        return wt.path("queries/tasks")
                .queryParam("query", "group")
                .queryParam("page", entrada.getPage())
                .queryParam("pageSize", entrada.getPageSize())
                .queryParam("codDependencia", entrada.getParametros().get("codDependencia"))
                .queryParam("states", entrada.getEstados().get(2))
                .request()
                .get();
    }

    /**
     * Revisar
     *
     * @param entrada
     * @return
     */

    public Response listarEstadisticasTareas(EntradaProcesoDTO entrada) {
        log.info("Listando Estadisticas de Tareas " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/listar/usuario")
                .request()
                .post(Entity.json(entrada));
    }

    /*
    public Response listarTareasPorUsuario(EntradaProcesoDTO entrada) {
        log.info("Listando Estadisticas de Tareas " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/listar/tercero")
                .request()
                .post(Entity.json(entrada));
    }*/

    public Response listarTareasPorUsuario(EntradaProcesoDTO entrada) {
        log.info("Listando Estadisticas de Tareas " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        wt.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, entrada.getUsuario())
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, entrada.getPass());
        return wt.path("queries/tasks")
                .queryParam("operacion", "owner")
                .queryParam("page", entrada.getPage())
                .queryParam("pageSize", entrada.getPageSize())
                .request()
                .get();
    }

    /**
     * felipe
     * Revisar
     *
     * @param entrada
     * @return
     */

  /*  public Response listarTareasPorEstadosTercero(EntradaProcesoDTO entrada) {
        log.info("Listando Estadisticas de Tareas " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/listar/tercero/")
                .request()
                .post(Entity.json(entrada));
    }*/
    public Response listarTareasPorEstadosTercero(EntradaProcesoDTO entrada) {
        log.info("Listando Estadisticas de Tareas " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        wt.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, entrada.getUsuario())
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, entrada.getPass());

        return wt.path("queries/tasks?query=groups&groups=fac&page=0&pageSize=10")

                .queryParam("query", "groups")
                .queryParam("groups", "")
                .queryParam("page", entrada.getPage())
                .queryParam("pageSize", entrada.getPageSize())
                .request()
                .get();
    }

    /*
    public Response iniciarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - start Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/iniciar")
                .request()
                .post(Entity.json(entrada));
    }*/

    public Response iniciarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - start Task with endpoint: " + endpoint);

        HttpAuthenticationFeature autentificacion = HttpAuthenticationFeature.basic(entrada.getUsuario(), entrada.getPass());
        Client client = ClientBuilder.newClient();
        client.register(autentificacion);
        WebTarget wt =client.target(endpoint);
        TaskDto taskDto = new TaskDto();
        taskDto.setParametros(ParametrosDto.newInstance().values(entrada.getParametros()).build());

        return wt.path("processes/" + entrada.getIdProceso() + "/instances/" + ((StringUtils.isEmpty(entrada.getInstanciaProceso()))?"1":entrada.getInstanciaProceso()) + "/tasks/" + entrada.getIdTarea())
                .queryParam("status", "started")
                .request()
                .method("PATCH");
    }

    /*
    public Response reservarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - reserve Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/reservar")
                .request()
                .post(Entity.json(entrada));
    }*/

    public Response reservarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - reserve Task with endpoint: " + endpoint);

        HttpAuthenticationFeature autentificacion = HttpAuthenticationFeature.basic(entrada.getUsuario(), entrada.getPass());
        Client client = ClientBuilder.newClient();
        client.register(autentificacion);

        WebTarget wt = client
                .target(endpoint)
                .path("processes/" + entrada.getIdProceso() + "/instances/" + ((StringUtils.isEmpty(entrada.getInstanciaProceso()))?"1":entrada.getInstanciaProceso()) + "/tasks/" + entrada.getIdTarea())
                .queryParam("status", "claimed");

        String uri = wt.getUri().toString();

        return wt.request()
                .method("PATCH");
    }

    /*
    public Response completarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - complete Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/completar")
                .request()
                .post(Entity.json(entrada));
    }*/

    public Response completarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - complete Task with endpoint: " + endpoint);

        HttpAuthenticationFeature autentificacion = HttpAuthenticationFeature.basic(entrada.getUsuario(), entrada.getPass());
        Client client = ClientBuilder.newClient();
        client.register(autentificacion);

        WebTarget wt = client
                .target(endpoint)
                .path("processes/" + entrada.getIdProceso() + "/instances/" + ((StringUtils.isEmpty(entrada.getInstanciaProceso()))?"1":entrada.getInstanciaProceso()) + "/tasks/" + entrada.getIdTarea())
                .queryParam("status", "completed");

        String uri = wt.getUri().toString();
        ParametrosDto parametrosDto  = ParametrosDto.newInstance().values(entrada.getParametros()).build();
        TaskDto taskDto = new TaskDto();
        taskDto.setParametros(parametrosDto);

        return wt.request()
                .method("PATCH", Entity.json(taskDto));

    }

    /*
    public Response abortarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - abort Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/proceso/abortar/")
                .request()
                .post(Entity.json(entrada));
    }*/

    public Response abortarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - abort Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        TaskDto taskDto = new TaskDto();
        taskDto.setParametros(ParametrosDto.newInstance().values(entrada.getParametros()).build());
        wt.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, entrada.getUsuario())
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, entrada.getPass());
        return wt.path("processes/" + entrada.getIdProceso() + "/instances/" + entrada.getInstanciaProceso() + "/tasks/" + entrada.getIdTarea())
                .queryParam("status", "Suspended")
                .request()
                .method("PATCH", Entity.json(taskDto));
    }

    /*
    public Response reasignarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - reasign Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/reasignar")
                .request()
                .post(Entity.json(entrada));
    }*/

    //mirar
    public Response reasignarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - reasign Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        TaskDto taskDto = new TaskDto();
        taskDto.setTaskActualOwner(entrada.getUsuario());

        wt.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, entrada.getUsuario())
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, entrada.getPass());
        return wt.path("processes/" + entrada.getIdProceso() + "/instances/" + entrada.getInstanciaProceso() + "/tasks/" + entrada.getIdTarea())
                .queryParam("action", "forwarded")
                .request()
                .method("PATCH", Entity.json(taskDto));
    }

    /*
    public Response reasignarTareaAOtro(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - reasign Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/reasignar-tercero")
                .request()
                .post(Entity.json(entrada));
    }*/

    public Response reasignarTareaAOtro(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - reasign Task with endpoint: " + endpoint);

        TaskDto taskDto = new TaskDto();
        taskDto.setTaskActualOwner(entrada.getUsuario());
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        wt.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, entrada.getUsuario())
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, entrada.getPass());


        return wt.path("processes/" + entrada.getIdProceso() + "/instances/" + entrada.getInstanciaProceso() + "/tasks/" + entrada.getIdTarea())
                .queryParam("action", "delegate")
                .request()
                .method("PATCH", Entity.json(taskDto));
    }

    /*
    public Response listarIntanciasProceso() {
        log.info("Task - [trafic] - list process instances with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = new EntradaProcesoDTO();
        entradaProcesoDTO.setIdProceso("proceso.correspondencia-entrada");

        return wt.path("/processes?page=0&pageSize=10")
                .queryParam("page",0)
                .queryParam("pageSize",10)
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }*/


    public Response listarIntanciasProceso(int page, int pageSize) {
        log.info("Task - [trafic] - list process instances with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = new EntradaProcesoDTO();
        entradaProcesoDTO.setIdProceso("proceso.correspondencia-entrada");

        return wt.path("/processes/" + entradaProcesoDTO.getIdProceso() + "/intances")
                .queryParam("page", page)
                .queryParam("pageSize", pageSize)
                .request()
                .get();
    }

    /**
     * No esta el servicio para esto
     *
     * @param entradaProcesoDTO
     * @return
     */
    public Response obtenerVariablesTarea(EntradaProcesoDTO entradaProcesoDTO) {
        log.info("Task - [trafic] - get task variables with endpoint: " + endpoint);

        HttpAuthenticationFeature autentificacion = HttpAuthenticationFeature.basic(entradaProcesoDTO.getUsuario(), entradaProcesoDTO.getPass());
        Client client = ClientBuilder.newClient();
        client.register(autentificacion);
        WebTarget wt = client.target(endpoint);

        Response response = wt.path("processes/" + entradaProcesoDTO.getIdProceso() + "/instances/" + entradaProcesoDTO.getInstanciaProceso())
                .queryParam("field", "variables")
                .request()
                .get();

        ProcessesDto parametros = response.readEntity(new GenericType<ProcessesDto>() {
        });
        return Response.status(response.getStatus()).entity(parametros.getParametros()).build();
    }

    public Response tareaEstadisticas(EntradaProcesoDTO entrada, String status) {

        log.info("Task - [trafic] - reasign Task with endpoint: " + endpoint);

        HttpAuthenticationFeature autentificacion = HttpAuthenticationFeature.basic(entrada.getUsuario(), entrada.getPass());
        Client client = ClientBuilder.newClient();
        client.register(autentificacion);

        switch (status) {
            case "completadas":
                status = "COMPLETADO";
                break;
            case "enprogreso":
                status = "ENPROGRESO";
                break;
            case "ready":
                status = "LISTO";
        }

        WebTarget wt = client.target(endpoint);
        wt = wt.path("/queries/tasks")
                .queryParam("query", "groups")
                .queryParam("states", EstadosEnum.valueOf(status).getNombre())
                .queryParam("codDependencia", entrada.getParametros().get("codDependencia"))
                .queryParam("page", 0)
                .queryParam("pageSize", -1)
        ;
        Response response = wt.request().get();
        String uri = wt.getUri().toString();
        List<TaskDto> responseContent = response.readEntity(new GenericType<List<TaskDto>>() {
        });

        List<RespuestaTareaDTO> tareaDTOS = new ArrayList<>();

        tareaDTOS = contructTaskResponse(responseContent, entrada);


        return Response.status(response.getStatus()).entity(tareaDTOS).build();
    }

 /*   public List<Response> reasignarTareas(List<EntradaProcesoDTO> entradas) throws ExecutionException, InterruptedException {

        final List<CompletableFuture<Response>>  completableFutureArrayList =  new ArrayList<>();

        final List<Response> responses = new ArrayList<>();



        for ( EntradaProcesoDTO entry : entradas){


            WebTarget wt = ClientBuilder.newClient().target(endpoint);

            completableFutureArrayList.add(Futures
                    .toCompletable(wt.path("/bpm/tareas/reasignar-tercero")
                            .request().async()
                            .post(Entity.json(entry), new InvocationCallback<Response>() {


                                @Override
                                public void completed(Response response) {

                                    responses.add(response);
                                   
                                }

                                @Override
                                public void failed(Throwable throwable) {


                                }
                            }),executor));
        }

     CompletableFuture.allOf(completableFutureArrayList.toArray(new CompletableFuture[completableFutureArrayList.size()])).get();


      return responses;

    }*/

    /**
     * El servicio generico recibe por body "taskActualOwner":"nuevoUsuario" con el usuario al que se reasignara
     * la tarea, pero este parametro no existe en EntradaProcesoDTO.
     *
     * @param entradas
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<Response> reasignarTareas(List<EntradaProcesoDTO> entradas) throws ExecutionException, InterruptedException {

        final List<CompletableFuture<Response>> completableFutureArrayList = new ArrayList<>();

        final List<Response> responses = new ArrayList<>();

        for (EntradaProcesoDTO entry : entradas) {

            TaskDto taskDto = new TaskDto();
            taskDto.setTaskActualOwner(entry.getUsuario());

            HttpAuthenticationFeature autentificacion = HttpAuthenticationFeature.basic(entry.getUsuario(), entry.getPass());
            Client client = ClientBuilder.newClient();
            client.register(autentificacion);
            WebTarget wt = client.target(endpoint);

            String processId = StringUtils.isEmpty(entry.getIdProceso())?"processId":entry.getIdProceso();
            String processIntance = ObjectUtils.isEmpty(entry.getInstanciaProceso())?"0":entry.getInstanciaProceso()+"";

            wt = wt.path("/processes/"+processId+"/instances/" + processIntance + "/tasks/" + entry.getIdTarea())
            .queryParam("action", "delegated");

            String uri = wt.getUri().toString();
            completableFutureArrayList.add(Futures
                    .toCompletable(wt
                            .request()
                            .async()
                            .method("PATCH", Entity.json(taskDto), new InvocationCallback<Response>() {
                                @Override
                                public void completed(Response response) {
                                    responses.add(response);
                                }
                                @Override
                                public void failed(Throwable throwable) {
                                }
                            }), executor));
        }

        CompletableFuture.allOf(completableFutureArrayList.toArray(new CompletableFuture[completableFutureArrayList.size()])).get();
        return responses;

    }

    //Revisar que hace
    public Future<Response> iniciarTerceroAsync(EntradaProcesoDTO entradaProcesoDTO) {
        Client client = ClientBuilder.newClient();
        WebTarget wt =client.target(endpoint);
        ParametrosDto parametros = ParametrosDto.newInstance().values(entradaProcesoDTO.getParametros()).build();
        wt =  wt.path("/processes/" + entradaProcesoDTO.getIdProceso() + "/instances");
        String b = wt.getUri().toString();
        return wt.request()
                .async()
                .post(Entity.json(parametros));
    }

    //Revisar que hace
    public Future<Response> iniciarTerceroAsync(EntradaProcesoDTO entradaProcesoDTO, InvocationCallback<Response> callback) {
        log.info("Proccess - [trafic] - starting Proccess with endpoint: " + endpoint);

        Client client = ClientBuilder.newClient();
        WebTarget wt =client.target(endpoint);
        ParametrosDto parametros = ParametrosDto.newInstance().values(entradaProcesoDTO.getParametros()).build();
        wt =  wt.path("/processes/" + entradaProcesoDTO.getIdProceso() + "/instances");
        String b = wt.getUri().toString();
        return wt.request()
                .async()
                .post(Entity.json(parametros), callback);
    }

    public List<RespuestaTareaDTO> contructTaskResponse(List<TaskDto> taskList, EntradaProcesoDTO entrada) {
        log.info("Build task response");
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        final String formatoFecha = "dd-MM-yyyy";
        String descripcion = "";
        try {
            for (TaskDto task : taskList) {
                log.info("cantidad de tareas-----------------{}", taskList.size());
                entrada.setIdProceso(task.getTaskProcDefId());
                entrada.setInstanciaProceso(Long.parseLong(task.getInstanceId()));
                if (task.getTaskName().equalsIgnoreCase("Crear Unidad Documental")) {
                    WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL));
                    Response response = wt
                            .path("/funcionarios-web-api/funcionarios/by-id/" + 8)
                            .request()
                            .get();
                    if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode()) {
                        return tareas;
                    }
                    FuncionarioDTO funcionario = response.readEntity(FuncionarioDTO.class);
                    descripcion = formatDescripcion(task.getTaskDescription());
                    String nombreFuncionario = "";
                    String apellido1Funcionario = "";
                    String apellido2Funcionario = "";
                    if (!ObjectUtils.isEmpty(funcionario)) {
                        nombreFuncionario = (ObjectUtils.isEmpty(funcionario.getNomFuncionario()) ? "" : funcionario.getNomFuncionario());
                        apellido1Funcionario = (ObjectUtils.isEmpty(funcionario.getValApellido1()) ? "" : funcionario.getValApellido1());
                        apellido2Funcionario = (ObjectUtils.isEmpty(funcionario.getValApellido2()) ? "" : funcionario.getValApellido2());
                    }
                    RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                            .idTarea(task.getTaskId().longValue())
                            .estado(EstadosEnum.obtenerClave(task.getTaskStatus()).toString())
                            .idProceso(task.getTaskProcDefId())
                            .idDespliegue(entrada.getIdDespliegue())
                            .nombre(task.getTaskName())
                            .prioridad(task.getTaskPriority())
                            .idInstanciaProceso(Long.parseLong(task.getInstanceId()))
                            .fechaCreada(StringUtils.isEmpty(task.getTaskCreatedOn()) ? new Date() : new SimpleDateFormat(formatoFecha).parse(task.getTaskCreatedOn()))
                            .tiempoActivacion(StringUtils.isEmpty(task.getTaskActivationTime()) ? new Date() : new SimpleDateFormat(formatoFecha).parse(task.getTaskActivationTime()))
                            .tiempoExpiracion(StringUtils.isEmpty(task.getTaskExpirationTime()) ? new Date() : new SimpleDateFormat(formatoFecha).parse(task.getTaskExpirationTime()))
                            .descripcion(descripcion + " " + nombreFuncionario + " " + apellido1Funcionario + " " + apellido2Funcionario + " " + new SimpleDateFormat(formatoFecha).parse(task.getTaskCreatedOn()))
                            .codigoDependencia((ObjectUtils.isEmpty(getParametrosProceso(entrada).getValues().get("codDependencia")))? "" :getParametrosProceso(entrada).getValues().get("codDependencia").toString())
                            //.idCreador(valor.getString("idSolicitante"))
                            .variables(task.getParametros().getValues())
                            .rol(!StringUtils.isEmpty(task.getTaskName()) ? environment.getProperty(task.getTaskName().replaceAll(" ", ".")) : "")
                            .build();
                    tareas.add(respuestaTarea);
                } else {
                    descripcion = formatDescripcion(task.getTaskDescription());
                    RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                            .idTarea(task.getTaskId().longValue())
                            .estado(EstadosEnum.obtenerClave(task.getTaskStatus()).toString())
                            .idProceso(task.getTaskProcDefId())
                            .idDespliegue(entrada.getIdDespliegue())
                            .nombre(task.getTaskName())
                            //.idCreador(valor.getString("idSolicitante"))
                            .idResponsable(task.getTaskActualOwner())
                            .prioridad(task.getTaskPriority())
                            .idInstanciaProceso(Long.parseLong(task.getInstanceId()))
                            .fechaCreada(StringUtils.isEmpty(task.getTaskCreatedOn()) ? null : new SimpleDateFormat(formatoFecha).parse(task.getTaskCreatedOn()))
                            .tiempoActivacion(StringUtils.isEmpty(task.getTaskActivationTime()) ? null : new SimpleDateFormat(formatoFecha).parse(task.getTaskActivationTime()))
                            .tiempoExpiracion(StringUtils.isEmpty(task.getTaskExpirationTime()) ? null : new SimpleDateFormat(formatoFecha).parse(task.getTaskExpirationTime()))
                            .descripcion(descripcion)
                            .codigoDependencia((ObjectUtils.isEmpty(getParametrosProceso(entrada)))?"":getParametrosProceso(entrada).getValues().get("codDependencia").toString())
                            .variables((ObjectUtils.isEmpty(task.getParametros()))?new HashMap<String, Object>():task.getParametros().getValues())
                            .rol(!StringUtils.isEmpty(task.getTaskName()) ? environment.getProperty(task.getTaskName().replaceAll(" ", ".")) : "")
                            .build();
                    tareas.add(respuestaTarea);
                }
            }
            log.info("lista de tareas por usuario y dependencia -----------------------------{}", tareas);
            return tareas;
        } catch (Exception e) {
            log.error("Error creando la lista de registros de salida");
            return tareas;
        }
    }

    static String formatDescripcion(String descripcionGeneral) {
        log.info("Ajustando formato para la descripcion de las tareas");
        String descripcion = "";
        if (descripcionGeneral.contains("--")) {
            String[] descripcionRadicado = descripcionGeneral.split("--");
            descripcion = descripcionRadicado[0];
            String radicado = descripcionRadicado[1];
            String[] descripciones = descripcion.split(" ");
            descripcion = "";
            for (int i = 0; i < descripciones.length - 1; i++) {
                if (!descripciones[i].trim().equals(""))
                    descripcion += descripciones[i] + " ";
            }
            descripcion += radicado;
            return descripcion;
        } else {
            descripcion = descripcionGeneral;
            return descripcion;
        }
    }

    public Response sendSignal(EntradaProcesoDTO entradaProcesoDTO) {
        log.info("Proccess - [trafic] - Send signal with endpoint: " + endpoint);

   String usuario =  StringUtils.isEmpty(entradaProcesoDTO.getUsuario()) ? environment.getProperty("user.admin") : entradaProcesoDTO.getUsuario();
   String pass =  StringUtils.isEmpty(entradaProcesoDTO.getPass()) ? environment.getProperty("password.admin") : entradaProcesoDTO.getPass();
        HttpAuthenticationFeature autentificacion = HttpAuthenticationFeature.basic(usuario,pass);
        Client client = ClientBuilder.newClient();
        client.register(autentificacion);

        WebTarget wt = client.target(endpoint);

        SignalDto signalDto = new SignalDto();
        signalDto.setIdDespliegue(entradaProcesoDTO.getIdDespliegue());
        signalDto.setSignalName(entradaProcesoDTO.getParametros().get("nombreSennal").toString());
        signalDto.setParametros(ParametrosDto.newInstance().values(entradaProcesoDTO.getParametros()).build());

        if(ObjectUtils.isEmpty(entradaProcesoDTO.getParametros().get("idInstancia"))){
            wt = wt.path("/processes/instances");
        }else{

            String idProceso = (StringUtils.isEmpty(entradaProcesoDTO.getIdProceso()))?"idProceso":entradaProcesoDTO.getIdProceso();
            wt = wt.path("/processes/"+idProceso+"/instances/"+entradaProcesoDTO.getParametros().get("idInstancia"));
        }
        String uri = wt.getUri().toString();
        return wt.request()
                .post(Entity.json(signalDto));
    }



    private ParametrosDto getParametrosProceso(EntradaProcesoDTO entradaProcesoDTO) {

        HttpAuthenticationFeature autentificacion = HttpAuthenticationFeature.basic(entradaProcesoDTO.getUsuario(), entradaProcesoDTO.getPass());
        Client client = ClientBuilder.newClient();
        client.register(autentificacion);

        WebTarget wt = client.target(endpoint).path("/processes/" + entradaProcesoDTO.getIdProceso() + "/instances/" + entradaProcesoDTO.getInstanciaProceso())
                .queryParam("field", "variables");
        Response response = wt.request().get();
        String uri = wt.getUri().toString();
        if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return null;

        ProcessesDto parametros = response.readEntity(new GenericType<ProcessesDto>() {
        });
        return parametros.getParametros();

    }

}
