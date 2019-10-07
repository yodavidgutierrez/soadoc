package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.canonical.correspondencia.NotificacionDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.Future;

@ApiDelegator
@Log4j2
public class NotificationClient {

    private final String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    @Autowired
    private FuncionarioClient funcionarioClient;

    public NotificationClient(){
        super();
    }

    public  void notificate(NotificacionDTO notificationDto) {

        log.info("notification object :" + notificationDto);

        try {

            WebTarget wt = ClientBuilder.newClient().target(endpoint);

            wt.path("/correspondencia-web-api/correspondencia/notificar-tarea").request().async().post(Entity.json(notificationDto), new InvocationCallback<Response>() {
                @Override
                public void completed(Response response) {

                    if(response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode()){

                        log.info("Error:" + response.readEntity(String.class));

                        log.info("El correo no fue enviado.");

                        return;
                    }

                    log.info("respuesta de envio de correo :" + response.readEntity(String.class));

                    if(response.readEntity(Boolean.class))
                      log.info(" Correo enviado ");

                    else
                    log.info("El correo no fue enviado.");

                }

                @Override
                public void failed(Throwable throwable) {

                     log.info("El correo no fue enviado. Motivo:"+throwable.toString());
                }
            }).notify();
        }
        catch (Exception e){

            log.info("El correo no fue enviado. Motivo:"+ e.getMessage());
        }
    }

    public  void  notificateByRolAndDependency(String codDependencia,String rol,NotificacionDTO notificacionDTO){

    Future<Response> future =  funcionarioClient.buscarFuncionarioAsync(new FuncionarioDTO(), new InvocationCallback<Response>() {
                @Override
                public void completed(Response response) {

                   if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode()) {
                        log.info("No se puedo enviar la notificacion. Motivo :" + response.readEntity(String.class));
                        return;
                    }

                    List<FuncionarioDTO> funcionarios = response.readEntity(FuncionariosDTO.class).getFuncionarios();

                    log.info("funcionarios para filtrar :" + funcionarios);

                    List<FuncionarioDTO> funcionarioDTOList = funcionarioClient.filterFuncionariosRolDependencia(codDependencia, rol, funcionarios);

                    log.info("funcionarios a notificar :" + funcionarioDTOList );

                    funcionarioDTOList.parallelStream().forEach(funcionarioDTO -> {

                        NotificacionDTO notificacionDTOClone = new NotificacionDTO(notificacionDTO.getNroRadicado(), notificacionDTO.getRemitente(), null, notificacionDTO.getNombreTarea());

                        if(funcionarioDTO.getIdeFunci().equals(notificacionDTO.getRemitente().getIdeFunci()))
                             return;

                        AgenteDTO destinatario = new AgenteDTO();

                        destinatario.setIdeFunci(funcionarioDTO.getIdeFunci());

                        notificacionDTOClone.setDestinatario(destinatario);

                        notificate(notificacionDTOClone);
                    });

                }

                @Override
                public void failed(Throwable throwable) {

                    log.info("No se pudieron enviar las notificaciones. Motivo: " + throwable.toString());
                }
            });

         synchronized (future){
             future.notify();
         }
    }

    public  void  notificateToRol(String rol,NotificacionDTO notificacionDTO){

        Future<Response> future =  funcionarioClient.buscarFuncionarioAsync(new FuncionarioDTO(), new InvocationCallback<Response>() {
            @Override
            public void completed(Response response) {

                if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode()) {
                    log.info("No se puedo enviar la notificacion. Motivo :" + response.readEntity(String.class));
                    return;
                }

                List<FuncionarioDTO> funcionarios = response.readEntity(FuncionariosDTO.class).getFuncionarios();

                log.info("funcionarios para filtrar :" + funcionarios);

                List<FuncionarioDTO> funcionarioDTOList = funcionarioClient.filterFuncionariosByRol( rol, funcionarios);

                log.info("funcionarios a notificar :" + funcionarioDTOList );

                funcionarioDTOList.parallelStream().forEach(funcionarioDTO -> {

                    NotificacionDTO notificacionDTOClone = new NotificacionDTO(notificacionDTO.getNroRadicado(), notificacionDTO.getRemitente(), null, notificacionDTO.getNombreTarea());

                    if(funcionarioDTO.getIdeFunci().equals(notificacionDTO.getRemitente().getIdeFunci()))
                        return;

                    AgenteDTO destinatario = new AgenteDTO();

                    destinatario.setIdeFunci(funcionarioDTO.getIdeFunci());

                    notificacionDTOClone.setDestinatario(destinatario);

                    notificate(notificacionDTOClone);
                });

            }

            @Override
            public void failed(Throwable throwable) {

                log.info("No se pudieron enviar las notificaciones. Motivo: " + throwable.toString());
            }
        });

        synchronized (future){
            future.notify();
        }
    }

}
