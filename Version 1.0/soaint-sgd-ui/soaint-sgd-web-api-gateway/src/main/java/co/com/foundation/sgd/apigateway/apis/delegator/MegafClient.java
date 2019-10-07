package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.dto.UnidadConservacionesDTO;
import co.com.foundation.sgd.dto.UnidadDocDTO;
import co.com.foundation.sgd.dto.UnidadDocumentalUpdateDTO;
import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Future;

@ApiDelegator
@Log4j2
public class MegafClient {

    private final  String endpoint = SystemParameters.getParameter(SystemParameters.MEGAF_ENDPOINT);

    public MegafClient(){}

    public Response getHijosDirecto(String codDependencia,String tipoArchivo,String idPadre){

        log.info("Endpoint de megaf :"+ endpoint);

        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        return wt.path("/soadoc-api/buscarUbicaciones")
                .queryParam("codOrg",codDependencia)
                .queryParam("tipoArchivo",tipoArchivo)
                .queryParam("idPadre",idPadre)
                .request()
                .get();
    }

    public Response getUnidadesFisica(String idSerie,String idSubSerie,String idUnidad, String nombreUnidad,String descriptor1,String descriptor2){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        return wt.path("/soadoc-api/buscarUniDocConserv")
                .queryParam("idSerie",idSerie)
                .queryParam("idSubserie",idSubSerie)
                .queryParam("idUniDoc",idUnidad)
                .queryParam("nomUnidadDoc",nombreUnidad)
                .queryParam("desc1",descriptor1)
                .queryParam("desc2",descriptor2)
                .request()
                .get();
    }

    public Response crearUnidadFisica(UnidadConservacionesDTO unidadConservacionesDTO ){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);


        log.info("unidad a guardar :" + unidadConservacionesDTO);

        return  wt.path("/soadoc-api/cargarUnidadesIndividual")
                 .request()
                 .post(Entity.json(unidadConservacionesDTO));

    }

    public Response consultarUnidadFisica(String codSerie,String codSubSerie,String idUnidad,
                                          String nombreUnidad,String descriptor1,String descriptor2,
                                          Boolean indActiva, String codOrg,Boolean indFechaTransPrimaria,
                                          Boolean indFechaTransSecundaria,String estadoTransf,String numTransferencia,
                                          String estadoDisposicion,Boolean indFechaDispoFinal,String faseArchivo
                                          ){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);



        return  wt.path("/soadoc-api/consultaUnidadDoc")
                   .queryParam("codOrg",codOrg)
                   .queryParam("codSerie",codSerie)
                   .queryParam("codSubSerie",codSubSerie)
                   .queryParam("codUniDoc",idUnidad)
                   .queryParam("descriptor1",descriptor1)
                   .queryParam("descriptor2",descriptor2)
                   .queryParam("indActiva",indActiva)
                   .queryParam("indFechaTransPri",indFechaTransPrimaria)
                   .queryParam("indFechaTransSec",indFechaTransSecundaria)
                   .queryParam("estadoTrans",estadoTransf)
                   .queryParam("numTrans",numTransferencia)
                   .queryParam("estDisposicion",estadoDisposicion)
                   .queryParam("nombreUniDoc",nombreUnidad)
                   .queryParam("indFechaDispoFinal",indFechaDispoFinal)
                   .queryParam("codFaseArch ",faseArchivo)
                   .request()
                   .get();
    }

  public Response gestionarUnidadesDocumentales(List<UnidadDocumentalUpdateDTO> unidadDocDTOList){

      WebTarget wt = ClientBuilder.newClient().target(endpoint);

      return wt.path("/soadoc-api/upUnidadDocuemntal")
              .request()
              .put(Entity.json(unidadDocDTOList));


  }

    public Future<Response> gestionarUnidadesDocumentalesAsync(List<UnidadDocumentalUpdateDTO> unidadDocDTOList, InvocationCallback<Response> cb){

        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        return wt.path("/soadoc-api/upUnidadDocuemntal")
                .request()
                .async()
                .put(Entity.json(unidadDocDTOList),cb);

    }


}
