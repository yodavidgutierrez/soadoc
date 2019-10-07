package co.com.foundation.sgd.apigateway.apis.delegator;


import co.com.foundation.sgd.dto.DigilitazacionDTO;
import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class DigitalizacionClient {

   private final String digitalizacionEndPoint = SystemParameters.getParameter(SystemParameters.DIGITALIZACION_ENDPOINT);

   DigitalizacionClient(){
       super();
   }

   public Response digitalizar(DigilitazacionDTO digitalizacionDto){

       log.info("Digitalizacion endpoint :"+ digitalizacionEndPoint);

       WebTarget wt = ClientBuilder.newClient().target(digitalizacionEndPoint);

       return wt.path("/digitalizacion").request().post(Entity.json(digitalizacionDto));
   }
}
