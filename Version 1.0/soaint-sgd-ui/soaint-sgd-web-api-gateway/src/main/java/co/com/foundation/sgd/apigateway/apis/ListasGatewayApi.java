package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ListasClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Path("/listas-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class ListasGatewayApi {

    @Autowired
    private ListasClient listasClient;

    private final  List<String> nomencladoreNoEditable = Arrays.asList("TL-DOCOF","TL-DOCM","ME-RECEM");

    public ListasGatewayApi() {

        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


    @Path("/adicionar")
    @POST
    @JWTTokenSecurity
    @RolesAllowed("Administrador")
    public Response addConstante(ConstanteDTO constanteDTO) {

        if (constanteDTO.getCodigo() == null) {
            constanteDTO.setCodigo(generateConstanteCode(constanteDTO.getCodPadre(), constanteDTO.getNombre()));
        }

        int indexPostfix = 1;

        while (listasClient.existeConstantCode(constanteDTO.getCodigo())) {

            constanteDTO.setCodigo(generateConstanteCode(constanteDTO.getCodPadre(), constanteDTO.getNombre(), indexPostfix++));
        }

        Response response = listasClient.adicionarConstante(constanteDTO);

        if (response.getStatus() < 400) {
            return Response.status(response.getStatus()).entity("{\"success\": true}").build();
        }

        return Response.status(response.getStatus()).entity(response.getEntity()).build();

    }


    private String generateConstanteCode(String prefix, String nombre) {

        String codeToGenerate = prefix;

        String posfix = nombre.length() < 3 ? nombre : nombre.substring(0,3).toUpperCase();

        codeToGenerate += posfix;

        return codeToGenerate;
    }

    private String generateConstanteCode(String prefix, String nombre, int apparition) {

        String codeToGenerate = prefix;

        String posfix = nombre.substring(0, 3).toUpperCase();

        codeToGenerate += posfix;

        codeToGenerate += apparition;

        return codeToGenerate;
    }

    @Path("/editar")
    @PUT
    @JWTTokenSecurity
    @RolesAllowed("Administrador")
    public Response editConstante(ConstanteDTO constanteDTO) {

        Response response = listasClient.editarConstante(constanteDTO);

        if (response.getStatus() < 400) {

            return Response.status(response.getStatus()).entity("{\"success\":true}").build();
        }

        return Response.status(response.getStatus()).entity(response.getEntity()).build();

    }

    @Path("/eliminar/{idConstante}")
    @DELETE
    @JWTTokenSecurity
    @RolesAllowed("Administrador")
    public Response deleteConstante(@PathParam("idConstante") BigInteger idConstante) {

       Response response =  listasClient.eliminarConstante(idConstante);

        String entity = response.readEntity(String.class);

       return Response.status(response.getStatus()).entity(entity).build();
    }

    @Path("/listas-editables")
    @GET
    @JWTTokenSecurity
    public Response getListasEditables(@QueryParam("codigos") List<String> codPadres, @QueryParam("nombre") @DefaultValue("") String nombre) {

        Response response =  listasClient.getListasEditables(codPadres, nombre);

        List<ConstanteDTO> entity = response.readEntity(new GenericType<List<ConstanteDTO>>(){});

        List<ConstanteDTO> entityResponse = entity.stream()
                .filter( constanteDTO -> !nomencladoreNoEditable.contains(constanteDTO.getCodigo())).collect(Collectors.toList());

        return Response.status(response.getStatus()).entity(entityResponse).build();
    }

    @Path("/listas-editables-genericas")
    @GET
    @JWTTokenSecurity
    public Response getListasEditablesGenericas() {

        Response response =  listasClient.getListasEditableGenericas();

        String entity = response.readEntity(String.class);

        return Response.status(response.getStatus()).entity(entity).build();

    }


}
