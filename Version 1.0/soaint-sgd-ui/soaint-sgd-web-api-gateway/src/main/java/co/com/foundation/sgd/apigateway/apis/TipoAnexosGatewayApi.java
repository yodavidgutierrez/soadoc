package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.TipoAnexosClient;
import co.com.foundation.sgd.apigateway.apis.delegator.UnidadTiempoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.correspondencia.AnexoDTO;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.annotations.Body;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tipo-anexos-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class TipoAnexosGatewayApi {

	@Autowired
	private TipoAnexosClient client;

	public TipoAnexosGatewayApi() {
		super();
		 SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@GET
	@Path("/")
	@JWTTokenSecurity
	public Response list() {

		log.info("TipoAnexosGatewayApi - [trafic] - listing TipoAnexos");
		Response response = client.list();
		String responseContent = response.readEntity(String.class);
		log.info("TipoAnexosGatewayApi - [content] : " + responseContent);
		
		return Response.status( response.getStatus() ).entity(responseContent).build();
	}

	@PUT
	@Path("/updateAnexos")
	@JWTTokenSecurity

	public Response updateAnexo(@RequestBody List<AnexoDTO> anexoList){

		Response response = client.updateAnexo(anexoList);

		List<AnexoDTO> responseContent = response.readEntity(new GenericType<List<AnexoDTO>>(){});

		return Response.status(response.getStatus()).entity(responseContent).build();

	}

}
