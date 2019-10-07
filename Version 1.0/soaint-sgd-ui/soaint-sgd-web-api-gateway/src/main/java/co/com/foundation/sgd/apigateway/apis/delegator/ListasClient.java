package co.com.foundation.sgd.apigateway.apis.delegator;


import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.foundation.canonical.correspondencia.ConstantesDTO;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApiDelegator
@Log4j2
public class ListasClient {

    private final  String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

   private final  List<String> listasEditable = Arrays.asList("ANE","BIS","TP-COMP","ME-RECE","TP-MNT","ORIEN","PE-CUAD","TP-CE","TP-ACC","TP-CON","TP-DOC","TP-MC","TP-VIA","E-ENTS","E-ENTE");

    public  ListasClient(){
    }

   public Response adicionarConstante(ConstanteDTO constanteDTO){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);

      return    wt.path("/constantes-web-api/constantes/adicionar")
           .request()
           .post(Entity.json(constanteDTO));
    }

    public Response editarConstante(ConstanteDTO constanteDTO){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);

      return   wt.path("/constantes-web-api/constantes/actualizar")
          .request()
          .put(Entity.json(constanteDTO));
    }

    public  Response eliminarConstante(BigInteger idConstante){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        return   wt.path("/constantes-web-api/constantes/eliminar/"+ idConstante)
                 .request()
                 .delete();
    }

    public boolean existeConstantCode(String codigo){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        Response response = wt.path("/constantes-web-api/constantes")
                               .queryParam("codigos",codigo)
                               .request()
                               .get();

        ConstantesDTO constantesDTO = response.readEntity(ConstantesDTO.class);

        return constantesDTO.getConstantes().size() > 0;

    }

    public  Response getListasEditables(List<String> codigos,String nombre){

        log.info("codigos genericos : "+ codigos );
        log.info("nombre : "+ nombre );

        final List<String> codPadres =  codigos.isEmpty() ? listasEditable : codigos;

        log.info("cod padres a enviar : "+ codPadres);

        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        return wt.path("/constantes-web-api/constantes/buscar/hijos")
                .queryParam("codigos",String.join(",",codPadres))
                .queryParam("nombre",nombre)
                .request()
                .get();
    }

    public Response getListasEditableGenericas() {

        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        return  wt.path("/constantes-web-api/constantes")
                .queryParam("codigos",String.join(",",listasEditable))
                .request()
                .get();
    }

}
