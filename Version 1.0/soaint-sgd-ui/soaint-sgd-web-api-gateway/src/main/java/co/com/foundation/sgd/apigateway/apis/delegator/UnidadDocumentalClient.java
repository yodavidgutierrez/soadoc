package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.dto.*;
import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.AdapterMegafUDtoEcmUD;
import co.com.foundation.sgd.utils.AdapterSoliciUDtoSolicitudCreada;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.NotificacionDTO;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudesUnidadDocumentalDTO;
import co.com.soaint.foundation.canonical.ecm.DisposicionFinalDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@ApiDelegator
@Log4j2
public class UnidadDocumentalClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    @Autowired
    private ECMClient ecmClient;

    @Autowired
    private MegafClient megafClient;

    @Autowired
    private NotificationClient notificationClient;




    public UnidadDocumentalClient() {
        super();
    }

    public Response salvarUnidadDocumental(UnidadDocumentalRequestDTO requestDTO) {

         if (!ObjectUtils.isEmpty(requestDTO.getUnidadConservacion())) {
            Response response = megafClient.crearUnidadFisica(requestDTO.getUnidadConservacion());

            log.info("estado de petición de respuesta de megaf:" + response.getStatus() );

            if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
                return response;

            UnidadConservacionesDTO unidadConservacionesDTO = response.readEntity(UnidadConservacionesDTO.class);

            if (unidadConservacionesDTO.getCodResponse().equals("400"))
                return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                        .entity(unidadConservacionesDTO.getDescripcion())
                        .build();

            requestDTO.getUnidadDocumentalDTO().setUbicacionTopografica( unidadConservacionesDTO.getCodUbicacion());
        }

       Response responseEcm  =  ecmClient.crearUnidadDocumental(requestDTO.getUnidadDocumentalDTO());

        return Response.status(responseEcm.getStatus()).entity(responseEcm.readEntity(String.class)).build();
    }

    public Response actualizarSolicitudUnidadDcoumental(SolicitudUnidadDocumentalDTO solicitud) {

        log.info("Delegator: actualizar Solicitud de unidad documentales - [trafic] - Modificar Unidades Documentales");

        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/actualizar-solicitud-um")
                .request()
                .put(Entity.json(solicitud));
    }

    public Response noTramitarSolicitudUnidadDocumental(NoTramiteRequestDTO noTramiteRequestDTO) {

        final SolicitudUnidadDocumentalDTO solicitud = noTramiteRequestDTO.getSolicitudUnidadDocumentalDTO();

        Response response = actualizarSolicitudUnidadDcoumental(solicitud);

        BigInteger idSolicitante = new BigInteger(solicitud.getIdSolicitante());

        if (noTramiteRequestDTO.getIdArchivista().equals(idSolicitante))
            return response;

        AgenteDTO remitente = new AgenteDTO();

        remitente.setIdeFunci(noTramiteRequestDTO.getIdArchivista());

        AgenteDTO destinatario = new AgenteDTO();

        destinatario.setIdeFunci(idSolicitante);

        String asunto = "Solicitud de creación de unidad documental no tramitada. Motivo :" + solicitud.getMotivo();

        NotificacionDTO notificacionDTO = new NotificacionDTO(null, remitente, destinatario, asunto);

        notificationClient.notificate(notificacionDTO);

        return response;

    }

    public Response buscarUnidadesDocumentales(String codSerie, String codSubSerie, String idUnidad,
                                               String nombreUnidad, String descriptor1, String descriptor2,
                                               String accion, String codDependencia) {

        UnidadDocumentalDTO unidadDocumentalDTO = new UnidadDocumentalDTO();

        unidadDocumentalDTO.setCodigoSerie(codSerie);
        unidadDocumentalDTO.setCodigoSubSerie(codSubSerie);
        unidadDocumentalDTO.setId(idUnidad);
        unidadDocumentalDTO.setNombreUnidadDocumental(nombreUnidad);
        unidadDocumentalDTO.setCodigoDependencia(codDependencia);
        unidadDocumentalDTO.setDescriptor1(descriptor1);
        unidadDocumentalDTO.setDescriptor2(descriptor2);
        unidadDocumentalDTO.setAccion(accion);

        Response ecmResponse = ecmClient.listarUnidadesDocumentales(unidadDocumentalDTO);

        if (ecmResponse.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode() || accion.toLowerCase().equals("abrir"))
            return Response.status(ecmResponse.getStatus()).entity(ecmResponse.readEntity(String.class)).build();

        MensajeRespuesta respuesta = ecmResponse.readEntity(MensajeRespuesta.class);

        Response megafResponse = megafClient.consultarUnidadFisica(codSerie, codSubSerie, idUnidad, nombreUnidad, descriptor1, descriptor2, accion.toLowerCase().equals("cerrar"), codDependencia, null, null,null,null,null,null,MegafFaseArchivo.ARCHIVO_GESTION);

        if (megafResponse.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return megafResponse;

        List<UnidadDocDTO> unidadDocDTOS = megafResponse.readEntity(new GenericType<List<UnidadDocDTO>>() {
        });

        log.info("unidades de megaf:" + unidadDocDTOS);

       joinUnidadMegafToEcm(respuesta,unidadDocDTOS,"unidadDocumental");

        log.info("nueva respuesta :" + respuesta.toString());

        return Response.ok().entity(respuesta).build();

    }


    public Response gestionarUnidadDocumentales(List<UnidadDocumetalAGDTO> unidadDocumentalDTOList) {

        if(ObjectUtils.isEmpty(unidadDocumentalDTOList)){
          return  Response.ok().entity(new MensajeRespuesta(EcmCodeStatus.ERROR,"No se seleccionaron unidades documentales")).build();
        }
        final List<UnidadDocumentalUpdateDTO> unidadDocumentalUpdateDTOList = unidadDocumentalDTOList.stream()
                .filter(unidadDocumentalDTO -> !unidadDocumentalDTO.getSoporte().equals(UDTipoSoporte.Electronico) && !unidadDocumentalDTO.getAccion().equals("abrir"))
                .map(AdapterMegafUDtoEcmUD::convertoMegafUnidadDocumentalUpdate)
                .collect(Collectors.toList());

         log.info("unidades fisicas a modificar :" + unidadDocumentalUpdateDTOList);

        final List<UnidadDocumentalDTO> unidadesRequest = unidadDocumentalDTOList.stream()
                .filter(unidadDocumentalDTO -> !unidadDocumentalDTO.getSoporte().equals(UDTipoSoporte.Fisico))
                .map(AdapterMegafUDtoEcmUD::revertToFatherClass)
                .collect(Collectors.toList());

        log.info("petición para cerrar unidades electrónicas: " + unidadesRequest.stream().map( unidadDocumentalDTO -> unidadDocumentalDTO.getId()+":"+unidadDocumentalDTO.getSoporte()+":"+ unidadDocumentalDTO.getAccion()).collect(Collectors.toList()));


        if (unidadesRequest.size() > 0) {

            Response ecmResponse = ecmClient.abrirCerrarReactivarUnidadDocumental(unidadesRequest);

            if (ecmResponse.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode() || unidadDocumentalUpdateDTOList.size() == 0)
                return Response.status(ecmResponse.getStatus()).entity(ecmResponse.readEntity(String.class)).build();

            MensajeRespuesta respuestaEcm = ecmResponse.readEntity(MensajeRespuesta.class);

            if (!respuestaEcm.getCodMensaje().equals(EcmCodeStatus.SUCCESS))
                return Response.status(ecmResponse.getStatus()).entity(respuestaEcm).build();
        }

        Response megafReponse = megafClient.gestionarUnidadesDocumentales(unidadDocumentalUpdateDTOList);

        if (megafReponse.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return Response.status(megafReponse.getStatus()).entity(megafReponse.readEntity(String.class)).build();

        MegafUpdateUdResponse megafUpdateUdResponse = megafReponse.readEntity(MegafUpdateUdResponse.class);

        MensajeRespuesta respuesta = new MensajeRespuesta();

        respuesta.setCodMensaje(megafUpdateUdResponse.getCodigo().equals(MegafResponseStatusCode.SUCCESS) ? EcmCodeStatus.SUCCESS : EcmCodeStatus.ERROR);
        respuesta.setMensaje(megafUpdateUdResponse.getCodigo().equals(MegafResponseStatusCode.SUCCESS) ? "Se ha realizado la operación exitosamente" : megafUpdateUdResponse.getMensaje());

        return Response.ok().entity(respuesta).build();

    }

    public Response listarUnidadesTransferir(String codDependencia, String tipo) {

        Response ecmResponse = ecmClient.listarUnidadesDocumentalesATransferir(tipo, codDependencia);

        MensajeRespuesta respuesta = ecmResponse.readEntity(MensajeRespuesta.class);

        if (!respuesta.getCodMensaje().equals(EcmCodeStatus.SUCCESS)) {

            return Response.status(ecmResponse.getStatus()).entity(respuesta).build();
        }
        Response megafResponse = megafClient.consultarUnidadFisica(null, null, null, null, null, null, false, codDependencia, tipo.equals("primaria") , tipo.equals("secundaria") ,tipo.equals("primaria") ?  MegafEstadoTransferencia.RECHAZADA : null , null, tipo.equals("secundaria") ? "1": null,tipo.equals("secundaria"),tipo.equals("primario") ? MegafFaseArchivo.ARCHIVO_GESTION : MegafFaseArchivo.ARCHIVO_CENTRAL);

        if (megafResponse.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return Response.status(megafResponse.getStatus()).entity(megafResponse.readEntity(String.class)).build();

        List<UnidadDocDTO> unidadDocDTOS = megafResponse.readEntity(new GenericType<List<UnidadDocDTO>>() {
        });

       joinUnidadMegafToEcm(respuesta,unidadDocDTOS,"unidadesDocumentales");

        return Response.ok().entity(respuesta).build();
    }

    public Response listarUnidadesVerificar(String codDependencia,String numTransferencia) {

        Response ecmResponse = ecmClient.listaUnidadesDocumentalesParaVerificar(codDependencia,numTransferencia);

        MensajeRespuesta respuesta = ecmResponse.readEntity(MensajeRespuesta.class);

        if (!respuesta.getCodMensaje().equals(EcmCodeStatus.SUCCESS)) {

            return Response.status(ecmResponse.getStatus()).entity(respuesta).build();
        }
        Response megafResponse = megafClient.consultarUnidadFisica(null, null, null, null, null, null, false, codDependencia, null, null, MegafEstadoTransferencia.APROBADA, numTransferencia, null,null,MegafFaseArchivo.ARCHIVO_GESTION);

        if (megafResponse.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return  Response.status(megafResponse.getStatus()).entity(megafResponse.readEntity(String.class)).build() ;

        List<UnidadDocDTO> unidadDocDTOS = megafResponse.readEntity(new GenericType<List<UnidadDocDTO>>() {
        });

        joinUnidadMegafToEcm(respuesta,unidadDocDTOS,"unidadesDocumentales");

        return Response.ok().entity(respuesta).build();
    }

    public Response listarUnidadesUbicar(String codDependencia,String numTransferencia) {

        Response ecmResponse = ecmClient.listarUnidadesDocumentalesConfirmadas(codDependencia,numTransferencia);

        MensajeRespuesta respuesta = ecmResponse.readEntity(MensajeRespuesta.class);

        if (!respuesta.getCodMensaje().equals(EcmCodeStatus.SUCCESS)) {

            return Response.status(ecmResponse.getStatus()).entity(respuesta).build();
        }
        Response megafResponse = megafClient.consultarUnidadFisica(null, null, null, null, null, null, false, codDependencia, null, null, MegafEstadoTransferencia.VERIFICADA, numTransferencia, null,null,MegafFaseArchivo.ARCHIVO_GESTION);

        if (megafResponse.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return  Response.status(megafResponse.getStatus()).entity(megafResponse.readEntity(String.class)).build() ;

        List<UnidadDocDTO> unidadDocDTOS = megafResponse.readEntity(new GenericType<List<UnidadDocDTO>>() {
        });

        joinUnidadMegafToEcm(respuesta,unidadDocDTOS,"unidadesDocumentales");

        return Response.ok().entity(respuesta).build();
    }

    public Response listarDisposicionFinal(String codDependencia,DisposicionFinalDTO disposicionFinalDTO ){

         Response ecmResponse = ecmClient.listarUnidadesDocumentalesDisposicion(disposicionFinalDTO,codDependencia);

        MensajeRespuesta respuesta = ecmResponse.readEntity(MensajeRespuesta.class);

        if (!respuesta.getCodMensaje().equals(EcmCodeStatus.SUCCESS)) {

            return Response.status(ecmResponse.getStatus()).entity(respuesta).build();
        }

        String tiposDisposicionMegaf = disposicionFinalDTO.getDisposicionFinalList().stream().map(AdapterMegafUDtoEcmUD::translateToMegafDF).collect(Collectors.joining(","));

        final UnidadDocumentalDTO  unidadDocumentalDTO = disposicionFinalDTO.getUnidadDocumentalDTO();

        Response megafResponse = megafClient.consultarUnidadFisica(unidadDocumentalDTO.getCodigoSerie(), unidadDocumentalDTO.getCodigoSubSerie(), unidadDocumentalDTO.getId(), unidadDocumentalDTO.getNombreUnidadDocumental(), unidadDocumentalDTO.getDescriptor1(), unidadDocumentalDTO.getDescriptor2(), null, codDependencia, null, true, MegafEstadoTransferencia.UBICADA, null, tiposDisposicionMegaf,null,MegafFaseArchivo.ARCHIVO_CENTRAL);

        if (megafResponse.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return  Response.status(megafResponse.getStatus()).entity(megafResponse.readEntity(String.class)).build() ;

        List<UnidadDocDTO> unidadDocDTOS = megafResponse.readEntity(new GenericType<List<UnidadDocDTO>>() {
        });

        log.info("respuesta de disposicion final megaf:" + unidadDocDTOS);

        joinUnidadMegafToEcm(respuesta,unidadDocDTOS,"unidadesDocumentales");

        return Response.ok().entity(respuesta).build();

        }

   private void joinUnidadMegafToEcm(MensajeRespuesta respuesta,List<UnidadDocDTO> unidadDocDTOList,String keyUnidadDocumental){

       List<UnidadDocumetalAGDTO> udList;

       log.info("respuesta megaf previa transformacion:" + unidadDocDTOList);
       
       if(respuesta.getResponse() == null)
           respuesta.setResponse(new HashMap<>());

       if (respuesta.getResponse().containsKey(keyUnidadDocumental)) {
          List list = (ArrayList) respuesta.getResponse().get(keyUnidadDocumental);

          ObjectMapper mapper = new ObjectMapper();

          udList = mapper.convertValue(list,new TypeReference<List<UnidadDocumetalAGDTO>>(){});

          for(UnidadDocDTO unidadDocDTO : unidadDocDTOList){

              boolean found = false;
              int i = 0;

              for(UnidadDocumetalAGDTO ud : udList ){
                  if(ud.getId().equals(unidadDocDTO.getValCodigo())){
                      udList.get(i).setUnidadesConservacion(unidadDocDTO.getUnidadesConservacion());
                      found = true;
                      break;
                  }
                  i++;
              }

              if(!found )
              udList.add(AdapterMegafUDtoEcmUD.convertoEcmUnidadDocumental(unidadDocDTO));
          }

       } else
           udList = unidadDocDTOList
                   .stream()
                   .map(AdapterMegafUDtoEcmUD::convertoEcmUnidadDocumental)
                   .collect(Collectors.toList());

          respuesta.getResponse().put(keyUnidadDocumental, udList);
   }

   public Response aprobarRechazarDisposicion(List<UnidadDocumetalAGDTO> unidadDocumentalDTOList){

        if(ObjectUtils.isEmpty(unidadDocumentalDTOList))
            return Response.ok().entity(new MensajeRespuesta(EcmCodeStatus.ERROR,"No se enviaron unidades docuemtales")).build();

       final List<UnidadDocumentalUpdateDTO> unidadDocumentalUpdateDTOList = removeUnidadesElectronicas(unidadDocumentalDTOList,"5");

       log.info("unidades de megaf para disposicion final"+ unidadDocumentalUpdateDTOList);

       final List<UnidadDocumentalDTO> unidadesRequest = removeUnidadesFisicas(unidadDocumentalDTOList);

       if(unidadesRequest.size() > 0) {

           Response ecmResponse = ecmClient.aprobarRechazarUnidadesDocumentalesDisposicion(unidadesRequest);

           if(ecmResponse.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
               return  Response.status(ecmResponse.getStatus()).entity(ecmResponse.readEntity(String.class)).build() ;

           MensajeRespuesta  respuesta = ecmResponse.readEntity(MensajeRespuesta.class);

           if(!respuesta.getCodMensaje().equals(EcmCodeStatus.SUCCESS) || unidadDocumentalUpdateDTOList.size() == 0)
               return Response.status(ecmResponse.getStatus()).entity(respuesta).build();
       }
           return  updateUnidadesFisicas(unidadDocumentalUpdateDTOList);
    }

    public Response aprobarRechazarTransferencia(List<UnidadDocumetalAGDTO> unidadDocumentalDTOList,String tipoTransferencia){

        if(ObjectUtils.isEmpty(unidadDocumentalDTOList))
            return Response.ok().entity(new MensajeRespuesta(EcmCodeStatus.ERROR,"No se enviaron unidades docuemtales")).build();

        final List<UnidadDocumentalUpdateDTO> unidadDocumentalUpdateDTOList = removeUnidadesElectronicas(unidadDocumentalDTOList, tipoTransferencia.equals("primaria")  ? "3" : "6");

        log.info("unidades s transferir:" + unidadDocumentalUpdateDTOList);

        final List<UnidadDocumentalDTO> unidadesRequest = removeUnidadesFisicas(unidadDocumentalDTOList);

        if(unidadesRequest.size() > 0) {

            Response ecmResponse = ecmClient.aprobarRechazarTransferenciasUniadesDocumentales(unidadesRequest, tipoTransferencia);

            if(ecmResponse.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
                return  Response.status(ecmResponse.getStatus()).entity(ecmResponse.readEntity(String.class)).build() ;

          MensajeRespuesta  respuesta = ecmResponse.readEntity(MensajeRespuesta.class);

            if(!respuesta.getCodMensaje().equals(EcmCodeStatus.SUCCESS) || unidadDocumentalUpdateDTOList.size() == 0)
                 return Response.status(ecmResponse.getStatus()).entity(respuesta).build();
        }

       if(unidadDocumentalUpdateDTOList.size() > 0){

            log.info("peticion megaf:" + unidadDocumentalUpdateDTOList );
           return  updateUnidadesFisicas(unidadDocumentalUpdateDTOList);
       }
        return Response.ok().entity(new MensajeRespuesta(EcmCodeStatus.ERROR,"No se enviaron unidades docuemtales")).build();
    }

    public Response verificarRechazarTransferencia(List<UnidadDocumetalAGDTO> unidadDocumentalDTOList){

        if(ObjectUtils.isEmpty(unidadDocumentalDTOList))
            return Response.ok().entity(new MensajeRespuesta(EcmCodeStatus.ERROR,"No se enviaron unidades docuemtales")).build();

        String proceso = !unidadDocumentalDTOList.get(0).getEstado().equals("Ubicada")  ? "3" : "4";

        final List<UnidadDocumentalUpdateDTO> unidadDocumentalUpdateDTOList = removeUnidadesElectronicas(unidadDocumentalDTOList,proceso);

         List<UnidadDocumentalDTO> unidadesRequest = removeUnidadesFisicas(unidadDocumentalDTOList);

        if(proceso.equals("4"))
             unidadesRequest = unidadesRequest.stream().map( unidadDocumentalDTO -> {
                 unidadDocumentalDTO.setEstado("Confirmada");
                 return unidadDocumentalDTO;
             }).collect(Collectors.toList());

        if(unidadesRequest.size() > 0) {

            Response ecmResponse = ecmClient.confirmarUnidadesDocumentalesTransferidas(unidadesRequest);

            if(ecmResponse.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
                return  Response.status(ecmResponse.getStatus()).entity(ecmResponse.readEntity(String.class)).build() ;

            MensajeRespuesta  respuesta = ecmResponse.readEntity(MensajeRespuesta.class);

            if(!respuesta.getCodMensaje().equals(EcmCodeStatus.SUCCESS) || unidadDocumentalUpdateDTOList.size() == 0)
                return Response.status(ecmResponse.getStatus()).entity(respuesta).build();
        }

        if(unidadDocumentalUpdateDTOList.size() > 0){

            log.info("unidades a verificar:"+ unidadDocumentalUpdateDTOList);

           return    updateUnidadesFisicas(unidadDocumentalUpdateDTOList);

        }
        return Response.ok().entity(new MensajeRespuesta(EcmCodeStatus.ERROR,"No se enviaron unidades docuemtales")).build();
    }

     private List<UnidadDocumentalDTO> removeUnidadesFisicas(List<UnidadDocumetalAGDTO> unidadDocumentalDTOList){

        return unidadDocumentalDTOList.stream()
                .filter(unidadDocumentalDTO -> !unidadDocumentalDTO.getSoporte().equals(UDTipoSoporte.Fisico))
                .map(AdapterMegafUDtoEcmUD::revertToFatherClass)
                .collect(Collectors.toList());
    }

    private List<UnidadDocumentalUpdateDTO> removeUnidadesElectronicas(List<UnidadDocumetalAGDTO> unidadDocumentalDTOList,String proceso){

        return   unidadDocumentalDTOList.stream()
                .filter(unidadDocumentalDTO -> !unidadDocumentalDTO.getSoporte().equals(UDTipoSoporte.Electronico))
                .map(unidadDocumetalAGDTO -> {

                    UnidadDocumentalUpdateDTO unidadConservacionUpdateDTO = AdapterMegafUDtoEcmUD.convertoMegafUnidadDocumentalUpdate(unidadDocumetalAGDTO);
                    unidadConservacionUpdateDTO.setProceso(proceso);

                    return unidadConservacionUpdateDTO;
                })
                .collect(Collectors.toList());
    }

    private Response updateUnidadesFisicas(List<UnidadDocumentalUpdateDTO> unidadDocumentalUpdateDTOList){

         MensajeRespuesta respuesta;

        Response megafResponse = megafClient.gestionarUnidadesDocumentales(unidadDocumentalUpdateDTOList);

        if(megafResponse.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return Response.status(megafResponse.getStatus()).entity(megafResponse.readEntity(String.class)).build();

        MegafUpdateUdResponse responseUpdate = megafResponse.readEntity(MegafUpdateUdResponse.class);

        if(!responseUpdate.getCodigo().equals(MegafResponseStatusCode.SUCCESS))
            respuesta = new MensajeRespuesta(EcmCodeStatus.ERROR,responseUpdate.getMensaje());
        else
            respuesta = new MensajeRespuesta("Se han actualizado las unidade sdocumentales exitosamente",EcmCodeStatus.SUCCESS);

        return Response.ok().entity(respuesta).build();
    }

   public Response listaUnidadesSolicitadas(String codSede,String codDependencia,String idSolicitante){


        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        Response response =  wt.path("/correspondencia-web-api/correspondencia/obtener-solicitud-um-solicitante")
                .queryParam("cod_sede", codSede)
                .queryParam("cod_dependencia", codDependencia)
                .queryParam("id_solicitante", idSolicitante)
                .request()
                .get();

        if(response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode())
            return  Response.status(response.getStatus()).entity(response.readEntity(String.class)).build();

       SolicitudesUnidadDocumentalDTO solicitudesUnidadDocumentalDTO = response.readEntity(SolicitudesUnidadDocumentalDTO.class);

       if(solicitudesUnidadDocumentalDTO == null || ObjectUtils.isEmpty(solicitudesUnidadDocumentalDTO.getSolicitudesUnidadDocumentalDTOS()))
           return Response.ok().entity( new ArrayList<>()).build();

       List<SolicitudTramitadaDTO> solicitudTramitadaDTOList = solicitudesUnidadDocumentalDTO.getSolicitudesUnidadDocumentalDTOS().stream()
                                                                      .map(AdapterSoliciUDtoSolicitudCreada::convertToSolicitudCreada)
                                                                      .collect(Collectors.toList());

       Response megafResponse = megafClient.consultarUnidadFisica(null,null,null,null,null,null,null,codDependencia,null,null,null,null,null,null,MegafFaseArchivo.ARCHIVO_GESTION);

       List<UnidadDocDTO> unidadDocDTOList = megafResponse.readEntity(new GenericType<List<UnidadDocDTO>>(){});

       for (int i = 0; i < solicitudTramitadaDTOList.size(); i++){

           final int pos = i;

           Optional<UnidadDocDTO> optionalUnidadDocDTO = unidadDocDTOList.stream().filter( unidadDocDTO -> unidadDocDTO.getValCodigo().equals(solicitudTramitadaDTOList.get(pos).getId())).findFirst();

           if(solicitudTramitadaDTOList.get(i).getAccion().equals("Creación UD") && optionalUnidadDocDTO.isPresent())
             solicitudTramitadaDTOList.get(i).setUnidadesConservacion(optionalUnidadDocDTO.get().getUnidadesConservacion());
       }

       return  Response.ok().entity(solicitudTramitadaDTOList).build();

    }

}
