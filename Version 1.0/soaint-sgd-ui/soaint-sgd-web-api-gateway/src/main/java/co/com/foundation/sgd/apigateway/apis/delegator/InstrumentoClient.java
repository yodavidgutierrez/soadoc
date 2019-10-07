package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.DependenciasDTO;
import co.com.soaint.foundation.canonical.correspondencia.TvsOrgaAdminXFunciPkDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ObjectUtils;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApiDelegator
@Log4j2

public class InstrumentoClient {


private final String instrumentoEndPoint  = SystemParameters.getParameter(SystemParameters.INSTRUMENTO_ENDPOINT);

private final  String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

 public  InstrumentoClient(){}

 public Response listarDependencias(){

 /*  log.info("Lista Dependencia - [trafic] - listing Actua en calidad with endpoint: " + instrumentoEndPoint);
     WebTarget wt = ClientBuilder.newClient().target(instrumentoEndPoint);
     return wt.path("/organigrama-api/listar_dependencias")
             .request()
             .get();*/

  log.info("DependeciaGrupo - [trafic] - listing Dependecias with endpoint: " + endpoint);
     WebTarget wt = ClientBuilder.newClient().target(endpoint);
     return wt.path("/dependencia-web-api/dependencias")
             .request()
             .get();

 }

 public Response bindFuncionarioDependencias(TvsOrgaAdminXFunciPkDTO funciPkDTO){

     log.info("Relacionar Funcionario Dependencia EndPoint - [trafic] - listing A with endpoint: " + endpoint);
     WebTarget wt = ClientBuilder.newClient().target(endpoint);
     return wt.path("/organigrama-admin-web-api/organigrama")
             .request()
             .put(Entity.json(funciPkDTO));

 }

 public Response listarSeries(){

     log.info("Obtener series de instrumento EndPoint - [trafic] - listing A with endpoint: " + instrumentoEndPoint);
     WebTarget wt = ClientBuilder.newClient().target(instrumentoEndPoint);
     return wt.path("/serie-api/listar_serie")
             .request()
             .get();
 }

 public DependenciaDTO obtenerDependenciaRadicadoraObject(){

     Response response = listarDependencias();

     if(response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
         return  null;

     DependenciasDTO dependenciasDTO = response.readEntity(DependenciasDTO.class);

     if(dependenciasDTO == null )
      return  null;

     if(ObjectUtils.isEmpty(dependenciasDTO.getDependencias()))
         return null;


     Optional<DependenciaDTO> dependenciaDTO = dependenciasDTO.getDependencias()
             .parallelStream()
             .filter( DependenciaDTO::isRadicadora)
             .findFirst();

     return dependenciaDTO.orElseGet(null);
 }

 public Response obtenerDependenciaRadicadora(){

     Response response = listarDependencias();

     if(response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
         return  null;

     DependenciasDTO dependenciasDTO = response.readEntity(DependenciasDTO.class);

     if(dependenciasDTO == null )
          return Response.ok().entity(new DependenciaDTO()).build();

     if(ObjectUtils.isEmpty(dependenciasDTO.getDependencias()))
        return Response.ok().entity(new DependenciaDTO()).build();


     Optional<DependenciaDTO> dependenciaDTO = dependenciasDTO.getDependencias()
                                     .parallelStream()
                                     .filter( DependenciaDTO::isRadicadora)
                                     .findFirst();

    return Response.ok().entity(dependenciaDTO.orElseGet(DependenciaDTO::new)).build();
 }

 public Response obtenerTipologiasDocumentales(String codSerie,String codSubserie){

     log.info("url de instrumento:" + instrumentoEndPoint);

     WebTarget wt = ClientBuilder.newClient().target(instrumentoEndPoint);
     return wt.path("/serie-api/listarTipologia")
             .queryParam("codSerie",codSerie)
             .queryParam("codSubserie",codSubserie)
             .request()
             .get();

 }

    public Response listarSubseriesPorDependecia(String codDependencia){

        WebTarget wt = ClientBuilder.newClient().target(instrumentoEndPoint);

        return wt.path("/serie-api/listarSubserieByCodOrg")
                .queryParam("codOrg",codDependencia)
                .request()
                .get();
    }

    public Response ObtenerDependenciaPorCodigo(String codDependencia){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        return wt.path("/dependencia-web-api/dependencia/"+ codDependencia)
                .request()
                .get();
    }

}
