package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.FuncionarioClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.foundation.sgd.utils.RolesList;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/funcionario-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class FuncionarioGatewayApi {

    @Autowired
    private FuncionarioClient client;

    public FuncionarioGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/{userName}")
    @JWTTokenSecurity
    public Response get(@PathParam("userName") String userName) {

        log.info("FuncionarioGatewayApi - [trafic] - listing Funcionario");
        Response response = client.obtenerFuncionario(userName);
        String responseContent = response.readEntity(String.class);
        log.info("FuncionarioGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/funcionarios/listar-by-loginnames")
    @JWTTokenSecurity
    public Response listarFuncionariosByLoginnames(@QueryParam("loginNames") String loginNames) {

        log.info("FuncionarioGatewayApi - [trafic] - listing Funcionario");
        log.info("FuncionarioGatewayApi - [trafic] - RequestParam: ".concat(loginNames));
        Response response = client.listarFuncionariosByLoginnames(loginNames);
        String responseContent = response.readEntity(String.class);
        log.info("FuncionarioGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/funcionarios/{cod_dependencia}")
    @JWTTokenSecurity
    public Response listarFuncionarios(@PathParam("cod_dependencia") String codigoDependencia) {

        log.info("FuncionarioGatewayApi - [trafic] - listing Funcionario");
        Response response = client.listarFuncionarios(codigoDependencia);
        String responseContent = response.readEntity(String.class);
        log.info("FuncionarioGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/funcionarios/{cod_dependencia}/{rol}")
    @JWTTokenSecurity
    public Response listarFuncionariosByRol(@PathParam("cod_dependencia") final String codDependencia,
                                            @PathParam("rol") final String rol) {

        log.info("FuncionarioGatewayApi - [trafic] - listing Funcionario By Rol");

       List<FuncionarioDTO> funcionarios = client.getListaFuncionariosRolDependencia(codDependencia,rol);

       return Response.status(HttpStatus.OK.value()).entity(funcionarios).build();
    }

    @GET
    @Path("/funcionarios/roles")
    @JWTTokenSecurity
    public Response listarFuncionariosRoles(){
        return  Response.ok().entity(RolesList.getListaRoles()).build();
    }

    @PUT
    @Path("/funcionarios")
    @JWTTokenSecurity
    @RolesAllowed("Administrador")
    public Response updateFuncionarioRole(FuncionarioDTO funcionarioDTO){
        log.info("processing rest request - actualizar funcionario");
        Response response = client.updateFuncionarioRoles(funcionarioDTO);
        String responseContent = response.readEntity(String.class);
        log.info("FuncionarioGatewayApi - [content] : " + responseContent);
        return response.getStatus() == HttpStatus.OK.value() ?
             Response.status(response.getStatus()).entity(responseContent).build() : response;
    }

    @PUT
    @Path("/actualizar-funcionario")
    @JWTTokenSecurity
    @RolesAllowed("Administrador")
    public Response updateFuncionario(FuncionarioDTO funcionarioDTO){
        log.info("processing rest request - actualizar funcionario");
        Response response = client.updateFuncionario(funcionarioDTO);
        String responseContent = response.readEntity(String.class);
        log.info("FuncionarioGatewayApi - [content] : " + responseContent);
        return ("1".equalsIgnoreCase(responseContent)
                ? Response.ok() : Response.serverError()).entity(responseContent).build();
    }

    @POST
    @Path("/funcionarios/buscar")
    @JWTTokenSecurity
    public Response buscarFuncionarios(FuncionarioDTO funcionarioDTO){
        log.info("FuncionarioGatewayApi - [trafic] - buscar funcionarios");
        Response response = client.buscarFuncionario(funcionarioDTO);
        String responseContent = response.readEntity(String.class);
        log.info("FuncionarioGatewayApi - [content] : " + responseContent);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/funcionarios/firmaPolitica/{login}")
    @JWTTokenSecurity
    public String buscarFirmaPoliticaLogin(@PathParam("login") final String loginName){
        log.info("FuncionarioGatewayApi - [trafic] -buscar Firma Politica Login");
        return  client.buscarFirmaLogin(loginName);
    }

}
