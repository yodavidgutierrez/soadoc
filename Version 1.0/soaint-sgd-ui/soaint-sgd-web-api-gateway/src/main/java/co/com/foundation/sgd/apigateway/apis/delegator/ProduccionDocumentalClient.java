package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.AdapterEntradaDtoToListNotificacionDto;
import co.com.foundation.sgd.utils.Futures;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.NotificacionDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import java.util.concurrent.Executor;

@ApiDelegator
@Log4j2
public class ProduccionDocumentalClient {

    private String ecm_endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ECM_SERVICE_ENDPOINT_URL);
    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    //Client client = ClientBuilder.newClient();

    @Autowired
    @Qualifier("dfrThreadPoolTaskExecutor")
    private Executor executor;

    @Autowired
    private ProcesoClient procesoClient;

    @Autowired
     private NotificationClient notificationClient;

    public ProduccionDocumentalClient() {
        super();
    }


//    public Response ejecutarProyeccionMultiple(EntradaProcesoDTO entrada) {
//        log.info("\n\r== Produccion Documental - [trafic] - ejecutar proyector:  ==\n\r");
//
//        EntradaProcesoDTO nuevaEntrada = new EntradaProcesoDTO();
//        nuevaEntrada.setIdDespliegue(entrada.getParametros().get("idDespliegue").toString());
//        nuevaEntrada.setIdProceso(entrada.getParametros().get("idProceso").toString());
//        nuevaEntrada.setUsuario(entrada.getUsuario());
//        nuevaEntrada.setPass(entrada.getPass());
//        nuevaEntrada.setParametros(new HashMap<>());
//
//        final String numeroRadicado = entrada.getParametros().getOrDefault("numeroRadicado", "").toString();
//        final String fechaRadicacion = entrada.getParametros().getOrDefault("fechaRadicacion", "").toString();
//        final List observacion = (ArrayList) entrada.getParametros().get("observacion");
//        final String funcionarioAsignador = entrada.getParametros().getOrDefault("funcionario", "").toString();
//
//        final List proyectores = (ArrayList) entrada.getParametros().get("proyectores");
//        for (Object obj :
//                proyectores) {
//            nuevaEntrada.getParametros().clear();
//            final Map proyector = (Map) obj;
//            final LinkedHashMap funcionario = (LinkedHashMap) proyector.get("funcionario");
//            final LinkedHashMap sedeAdministrativa = (LinkedHashMap) proyector.get("sede");
//            final LinkedHashMap dependencia = (LinkedHashMap) proyector.get("dependencia");
//            final LinkedHashMap tipoPlantilla = (LinkedHashMap) proyector.get("tipoPlantilla");
//
//            final Map<String, Object> parameters = new HashMap<>();
//            final String loginName = funcionario.containsKey("loginName") ? funcionario.get("loginName") + "" : "";
//            parameters.put("usuarioProyector", loginName);
//            parameters.put("numeroRadicado", numeroRadicado);
//            parameters.put("fechaRadicacion", fechaRadicacion);
//
//            if(observacion.size() >= 1)
//                parameters.put("observacion", observacion.get(0));
//
//                if(observacion.size() == 2)
//                parameters.put("observacion1", observacion.get(1));
//                parameters.put("funcionario", funcionarioAsignador);
//
//            final String codigoSede = sedeAdministrativa.containsKey("codigo") ? sedeAdministrativa.get("codigo") + "" : null;
//            parameters.put("codigoSede", codigoSede);
//            final String codDependencia = dependencia.containsKey("codigo") ? dependencia.get("codigo") + "" : null;
//            parameters.put("codDependencia", codDependencia);
//            final Object codigoTipoPlantilla = tipoPlantilla.containsKey("codigo") ? tipoPlantilla.get("codigo") : null;
//            parameters.put("codigoTipoPlantilla", codigoTipoPlantilla);
//            nuevaEntrada.getParametros().putAll(parameters);
//            log.info("\n\r== Nueva entrada: " + nuevaEntrada.toString() + " ==\n\r");
//            procesoClient.iniciar(nuevaEntrada);
//        }
//        return procesoClient.listarPorIdProceso(entrada);
//    }



        public Response ejecutarProyeccionMultiple(EntradaProcesoDTO entrada)  {
        log.info("\n\r== Produccion Documental - [trafic] - ejecutar proyector:  ==\n\r");

        try {


            final List proyectores = (ArrayList) entrada.getParametros().get("proyectores");

            final List<CompletableFuture<Response>> completableFutureArrayList = new ArrayList<>();

            for (Object obj :
                    proyectores) {
                EntradaProcesoDTO nuevaEntrada = new EntradaProcesoDTO();
                nuevaEntrada.setIdDespliegue(entrada.getParametros().get("idDespliegue").toString());
                nuevaEntrada.setIdProceso(entrada.getParametros().get("idProceso").toString());
                nuevaEntrada.setUsuario(entrada.getUsuario());
                nuevaEntrada.setPass(entrada.getPass());
                nuevaEntrada.setParametros(new HashMap<>());

                final String numeroRadicado = entrada.getParametros().getOrDefault("numeroRadicado", "").toString();
                final String fechaRadicacion = entrada.getParametros().getOrDefault("fechaRadicacion", "").toString();
                final String funcionarioAsignador = entrada.getParametros().getOrDefault("funcionario", "").toString();

                final Map proyector = (Map) obj;
                final LinkedHashMap funcionario = (LinkedHashMap) proyector.get("funcionario");
                final LinkedHashMap sedeAdministrativa = (LinkedHashMap) proyector.get("sede");
                final LinkedHashMap dependencia = (LinkedHashMap) proyector.get("dependencia");
                final LinkedHashMap tipoPlantilla = (LinkedHashMap) proyector.get("tipoPlantilla");


                final Map<String, Object> parameters = new HashMap<>();
                final String loginName = funcionario.containsKey("loginName") ? funcionario.get("loginName") + "" : "";
                parameters.put("usuarioProyector", loginName);
                parameters.put("numeroRadicado", numeroRadicado);
                parameters.put("fechaRadicacion", fechaRadicacion);

                if(proyector.containsKey("observacion")){
                    final List observaciones = (ArrayList) proyector.get("observacion") ;

                    if(observaciones != null){

                        parameters.put("observacion", observaciones.get(0));

                        if(observaciones.size() == 2)
                            parameters.put("observacion1", observaciones.get(1));
                    }

                }

                parameters.put("funcionario", funcionarioAsignador);

                final String codigoSede = sedeAdministrativa.containsKey("codigo") ? sedeAdministrativa.get("codigo") + "" : null;
                parameters.put("codigoSede", codigoSede);
                final String codDependencia = dependencia.containsKey("codigo") ? dependencia.get("codigo") + "" : null;
                parameters.put("codDependencia", codDependencia);
                final Object codigoTipoPlantilla = tipoPlantilla.containsKey("codigo") ? tipoPlantilla.get("codigo") : null;

                parameters.put("codigoTipoPlantilla", codigoTipoPlantilla);
                nuevaEntrada.getParametros().putAll(parameters);
                log.info("\n\r== Nueva entrada: " + nuevaEntrada.toString() + " ==\n\r");

                //completableFutureArrayList.add(Futures.toCompletable(procesoClient.inciarAsync(nuevaEntrada), executor));
                completableFutureArrayList.add(Futures.toCompletable(procesoClient.iniciarTerceroAsync(nuevaEntrada), executor));

            }

            CompletableFuture.allOf(completableFutureArrayList.toArray(new CompletableFuture[completableFutureArrayList.size()])).get();

          List<NotificacionDTO> notificacionDTOS = AdapterEntradaDtoToListNotificacionDto.transform(entrada);

          log.info("notificaciones produccion documental multiple : " + notificacionDTOS);

            for ( NotificacionDTO notification: notificacionDTOS){
                notification.setNombreTarea("Producción Documental");

                notificationClient.notificate(notification);
            }

            return procesoClient.listarPorIdProceso(entrada);
        }
        catch (Exception ex){
            return  Response.status(500).entity(ex.getMessage()).build();
        }
    }

    public Response obtenerDatosDocumentoPorNroRadicado(String nro) {
        log.info("ProduccionDocumental - [trafic] - Obtener datos del documento por Nro Radicado: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/documento-web-api/documento/" .concat(nro))
                .request()
                .get();
    }

}
