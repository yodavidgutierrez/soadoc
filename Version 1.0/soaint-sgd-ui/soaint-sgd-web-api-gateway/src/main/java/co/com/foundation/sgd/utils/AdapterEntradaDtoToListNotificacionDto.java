package co.com.foundation.sgd.utils;

import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.NotificacionDTO;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdapterEntradaDtoToListNotificacionDto {


      public static List<NotificacionDTO> transform(EntradaProcesoDTO entrada){


          final BigInteger remitenteId = new BigInteger(entrada.getParametros().get("remitenteId").toString());
          final List proyectores = (ArrayList) entrada.getParametros().get("proyectores");
          final List<NotificacionDTO> notificationsDto = new ArrayList<NotificacionDTO>(proyectores.size());
          final String numeroRadicado = entrada.getParametros().getOrDefault("numeroRadicado", "").toString();

          for (Object obj:proyectores) {

              Map proyector = (Map) obj;

              final LinkedHashMap funcionario = (LinkedHashMap) proyector.get("funcionario");

              if(entrada.getUsuario().equals(funcionario.get("loginName")))
                   continue;

              final AgenteDTO agenteRemitente = new AgenteDTO();
              agenteRemitente.setIdeFunci(remitenteId);
              agenteRemitente.setCodTipAgent(TipoAgenteEnum.DESTINATARIO.getCodigo());

              BigInteger destinatarioId = new BigInteger(funcionario.get("id").toString());

              final AgenteDTO agenteDestinario = new AgenteDTO();
              agenteDestinario.setIdeFunci(destinatarioId);
              agenteDestinario.setCodTipAgent(TipoAgenteEnum.DESTINATARIO.getCodigo());

              final NotificacionDTO notification = new NotificacionDTO();

              notification.setRemitente(agenteRemitente);
              notification.setDestinatario(agenteDestinario);

              if(!numeroRadicado.isEmpty())
                  notification.setNroRadicado(numeroRadicado);

              notificationsDto.add(notification);
          }

       return  notificationsDto;
      }
}
