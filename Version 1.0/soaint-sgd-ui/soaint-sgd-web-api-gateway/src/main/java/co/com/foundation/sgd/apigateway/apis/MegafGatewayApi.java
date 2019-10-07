package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.MegafClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.foundation.sgd.dto.MegafFaseArchivo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;

@Path("/megaf-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class MegafGatewayApi {

    @Autowired
    private  MegafClient client;

    public MegafGatewayApi(){
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/hijos-directos/{codDependencia}/{tipoArchivo}/{idPadre}")
    @JWTTokenSecurity
    @RolesAllowed({"Archivista_AC","Aprobador_dependencia","Archivista"})
    public Response getHijosDirectos(@PathParam("codDependencia") String codDependencia,
                                     @PathParam("tipoArchivo") String tipoArchivo,
                                     @PathParam("idPadre") String idPadre){

         Response response = client.getHijosDirecto(codDependencia,tipoArchivo,idPadre);

         return  Response
                 .status(response.getStatus())
                 .entity(response.readEntity(String.class))
                 .build();
    }

    @GET
    @Path("/unidades-fisicas")
    @JWTTokenSecurity
    @RolesAllowed({"Archivista_AC","Aprobador_dependencia","Archivista"})
    public Response getUnidadesFisicas(@QueryParam("idSerie") String idSerie,
                                            @QueryParam("idSubserie")String idSubSerie,
                                            @QueryParam("idUniDoc") @DefaultValue("") String idUnidad,
                                            @QueryParam("nomUnidadDoc") @DefaultValue("") String nombreUnidad,
                                            @QueryParam("descriptor1") @DefaultValue("") String descriptor1,
                                            @QueryParam("descriptor2") @DefaultValue("") String descriptor2
                                            ){

        Response response = client.getUnidadesFisica(idSerie,idSubSerie,idUnidad,nombreUnidad,descriptor1,descriptor2);

        return  Response
                .status(response.getStatus())
                .entity(response.readEntity(String.class))
                .build();
    }

    @GET
    @Path("/obtener-unidades-fisicas")
    @JWTTokenSecurity
    @RolesAllowed({"Archivista_AC","Aprobador_dependencia","Archivista"})
    public Response consultarUnidadesFisicas(@QueryParam("codSerie") String codSerie,@QueryParam("codSubSerie") String codSubSerie,@QueryParam("idUnidad") String idUnidad,
                                             @QueryParam("nombreUnidad") String nombreUnidad,@QueryParam("descriptor1") String descriptor1,@QueryParam("descriptor2") String descriptor2,
                                             @QueryParam("indActiva") Boolean indActiva,@QueryParam("codOrg") String codOrg, @QueryParam("indFechaTransPrimaria") Boolean indFechaTransPrimaria,
                                             @QueryParam("indFechaTransSecundaria") Boolean indFechaTransSecundaria,@QueryParam("estadoTransf") String estadoTransf,
                                             @QueryParam("numTransferencia") String numTransferencia,@QueryParam("estadoDisposicion") String estadoDisposicion){

        Response response = client.consultarUnidadFisica( codSerie,  codSubSerie,  idUnidad, nombreUnidad, descriptor1, descriptor2,indActiva,  codOrg, indFechaTransPrimaria,
                                                           indFechaTransSecundaria, estadoTransf, numTransferencia,estadoDisposicion,false,MegafFaseArchivo.ARCHIVO_GESTION);

        return Response.status(response.getStatus()).entity(response.readEntity(String.class)).build();

    }
}
