package co.com.foundation.sgd.utils;

import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class AdapterForNotificate {

 public static AgenteDTO convertToAgenteDto(LinkedHashMap agente){

     AgenteDTO agenteDTO = new AgenteDTO();

     if(agente.containsKey("datosContactoList")){

         final List contacts = (ArrayList) agente.get("datosContactoList");

         for(Object c : contacts){

             DatosContactoDTO contact = (DatosContactoDTO) c;

             if(ObjectUtils.isEmpty(contact.getCorrElectronico())){

                 List<DatosContactoDTO> contactoDTOList = new ArrayList<>();
                 contactoDTOList.add(contact);

                 agenteDTO.setDatosContactoList(contactoDTOList);

                 break;
             }

         }
     }

     if(agente.containsKey("codTipAgent"))
         agenteDTO.setCodTipAgent(agente.get("codTipAgent").toString());

     if(agente.containsKey("ideFunci"))
         agenteDTO.setIdeFunci( new BigInteger(agente.get("ideFunci").toString()) );

     return agenteDTO;

  }
}
