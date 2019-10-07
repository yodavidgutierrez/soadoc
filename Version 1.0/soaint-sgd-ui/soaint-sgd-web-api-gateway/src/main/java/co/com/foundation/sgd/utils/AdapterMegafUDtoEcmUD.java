package co.com.foundation.sgd.utils;

import co.com.foundation.sgd.dto.*;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ObjectUtils;

import java.util.stream.Collectors;

@Log4j2
public class AdapterMegafUDtoEcmUD {

 public    static UnidadDocumetalAGDTO convertoEcmUnidadDocumental(UnidadDocDTO unidadDocDTO){

     UnidadDocumetalAGDTO unidadDocumentalDTO = new UnidadDocumetalAGDTO();

        unidadDocumentalDTO.setCodigoDependencia(unidadDocDTO.getCodOrganigrama());
        unidadDocumentalDTO.setCodigoSerie(unidadDocDTO.getCodSerie());
        unidadDocumentalDTO.setNombreSerie(unidadDocDTO.getNomSerie());
        unidadDocumentalDTO.setCodigoSubSerie(unidadDocDTO.getCodSubserie());
        unidadDocumentalDTO.setNombreSubSerie(unidadDocDTO.getNomSubserie());
        unidadDocumentalDTO.setNombreUnidadDocumental(unidadDocDTO.getNomUnidDocumental());
        unidadDocumentalDTO.setDescriptor1(unidadDocDTO.getValDescriptor1());
        unidadDocumentalDTO.setDescriptor2(unidadDocDTO.getValDescriptor2());
        unidadDocumentalDTO.setId(unidadDocDTO.getValCodigo());
         unidadDocumentalDTO.setFechaExtremaInicial(DateUtils.DateToCalendar(unidadDocDTO.getFecExtremaInicial()));
        unidadDocumentalDTO.setFechaExtremaFinal(DateUtils.DateToCalendar(unidadDocDTO.getFecExtremaFinal()));
        unidadDocumentalDTO.setFechaCierre(DateUtils.DateToCalendar(unidadDocDTO.getFecCierTramite()));
        unidadDocumentalDTO.setObservaciones(unidadDocDTO.getDesUnidDoc());

        if(unidadDocDTO.getCodEstTransf() != null){

            if (unidadDocDTO.getCodEstTransf().equals(MegafEstadoTransferencia.RECHAZADA)){
               unidadDocumentalDTO.setEstado("Rechazado");
            }
           else  if (unidadDocDTO.getCodEstTransf().equals(MegafEstadoTransferencia.APROBADA)){
                unidadDocumentalDTO.setEstado("Aprobado");
            }
          else  if (unidadDocDTO.getCodEstTransf().equals(MegafEstadoTransferencia.UBICADA)){

                if(unidadDocDTO.getFecDispoFinal() == null)
                 unidadDocumentalDTO.setEstado("Ubicada");
                else
                    unidadDocumentalDTO.setEstado("Aprobado");
            }
          else if (unidadDocDTO.getCodEstTransf().equals(MegafEstadoTransferencia.VERIFICADA)){
                unidadDocumentalDTO.setEstado("Confirmada");
            }
        }
        unidadDocumentalDTO.setSoporte(UDTipoSoporte.Fisico);
        unidadDocumentalDTO.setUnidadesConservacion(unidadDocDTO.getUnidadesConservacion());
        unidadDocumentalDTO.setConsecutivoTransferencia(unidadDocDTO.getNumTrans());
        unidadDocumentalDTO.setDisposicion(unidadDocDTO.getCodTipoDispFinal());


        return unidadDocumentalDTO;
    }

       public    static UnidadDocDTO convertoMegafUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO){

              UnidadDocDTO  unidadDocDTO = new UnidadDocDTO();

              unidadDocDTO.setCodOrganigrama(unidadDocumentalDTO.getCodigoDependencia());
              unidadDocDTO.setCodSerie(unidadDocDTO.getCodSerie());
              unidadDocDTO.setCodSubserie(unidadDocDTO.getCodSubserie());
              unidadDocDTO.setNomSerie(unidadDocDTO.getNomSerie());
              unidadDocDTO.setNomSubserie(unidadDocDTO.getNomSubserie());
              unidadDocDTO.setValDescriptor1(unidadDocumentalDTO.getDescriptor1());
              unidadDocDTO.setValDescriptor2(unidadDocumentalDTO.getDescriptor2());
              unidadDocDTO.setValCodigo(unidadDocumentalDTO.getId());
              unidadDocDTO.setFecExtremaFinal(unidadDocumentalDTO.getFechaExtremaFinal().getTime());
              unidadDocDTO.setFecExtremaInicial(unidadDocumentalDTO.getFechaExtremaInicial().getTime());
              unidadDocDTO.setNombreEstado(unidadDocumentalDTO.getEstado());
              unidadDocDTO.setNumTrans(unidadDocumentalDTO.getConsecutivoTransferencia());
              return unidadDocDTO;
       }

      public static UnidadDocumentalUpdateDTO convertoMegafUnidadDocumentalUpdate (UnidadDocumetalAGDTO unidadDocumentalDTO){


         UnidadDocumentalUpdateDTO unidadDocumentalUpdateDTO = new UnidadDocumentalUpdateDTO();

        unidadDocumentalUpdateDTO.setCodUnidDoc(unidadDocumentalDTO.getId());
        unidadDocumentalUpdateDTO.setDescripcion(unidadDocumentalDTO.getObservaciones());
        unidadDocumentalUpdateDTO.setNumTrans(unidadDocumentalDTO.getConsecutivoTransferencia());
        if(!ObjectUtils.isEmpty(unidadDocumentalDTO))
             if(!ObjectUtils.isEmpty(unidadDocumentalDTO.getUnidadesConservacion()))
               unidadDocumentalUpdateDTO.setUnidadesConservacion(  unidadDocumentalDTO.getUnidadesConservacion().stream().map(AdapterMegafUDtoEcmUD::ConvertToUniConservacionUpdFormUniConservacionDTO).collect(Collectors.toList()));
        unidadDocumentalUpdateDTO.setNombreEntidadRecibe(unidadDocumentalDTO.getEntidadRecibe());

        if(unidadDocumentalDTO.getAccion() != null)
        switch (unidadDocumentalDTO.getAccion().toLowerCase()){
            case "cerrar" : unidadDocumentalUpdateDTO.setProceso("1");
             break;
            case "reactivar" : unidadDocumentalUpdateDTO.setProceso("2");
             break;
        }

        if(unidadDocumentalDTO.getEstado() != null)
          switch (unidadDocumentalDTO.getEstado()){

            case "Aprobado":  unidadDocumentalUpdateDTO.setEstadoTrans(MegafEstadoTransferencia.APROBADA);
              break;
            case "Rechazado":  unidadDocumentalUpdateDTO.setEstadoTrans(MegafEstadoTransferencia.RECHAZADA);
                break;
            case "Confirmada" : unidadDocumentalUpdateDTO.setEstadoTrans(MegafEstadoTransferencia.VERIFICADA);
              break;
            case "Ubicada" : unidadDocumentalUpdateDTO.setEstadoTrans(MegafEstadoTransferencia.UBICADA);
                break;
        }

          unidadDocumentalUpdateDTO.setTipoDF(unidadDocumentalDTO.getDisposicion());

        log.info("unidad transformada a megaf" + unidadDocumentalUpdateDTO);

          return unidadDocumentalUpdateDTO;

      }

      public static  UnidadConservacionUpdateDTO  ConvertToUniConservacionUpdFormUniConservacionDTO( UniConservacionDTO uniConservacionDTO){

          UnidadConservacionUpdateDTO unidadConservacionUpdateDTO = new UnidadConservacionUpdateDTO();

          unidadConservacionUpdateDTO.setCodUnidConserv(uniConservacionDTO.getValCodigo());
          unidadConservacionUpdateDTO.setIdeBodega(uniConservacionDTO.getIdeBodega());
          unidadConservacionUpdateDTO.setIdeUbicFisiBod(uniConservacionDTO.getIdeUbicFisiBod());

          return  unidadConservacionUpdateDTO;
      }

      public static String  translateToMegafDF( String disposicion){

       if(disposicion == null)
            return  null;

          switch (disposicion){

              case "CT" : return "1";

              case "E" : return "2";

              case "S" : return "3";

              case "M" : return "4";

              case "D" : return "5";

          }

          return  null;
      }

      public static UnidadDocumentalDTO revertToFatherClass(UnidadDocumetalAGDTO unidadDocumentalAGDTO){

        UnidadDocumentalDTO unidad =  new UnidadDocumentalDTO(
                null,
                 null,
                 unidadDocumentalAGDTO.getAccion(),
                 unidadDocumentalAGDTO.getInactivo(),
                 unidadDocumentalAGDTO.getUbicacionTopografica(),
                 unidadDocumentalAGDTO.getFechaCierre(),
                 unidadDocumentalAGDTO.getId(),
                 unidadDocumentalAGDTO.getFaseArchivo(),
                 unidadDocumentalAGDTO.getFechaExtremaInicial(),
                 unidadDocumentalAGDTO.getSoporte(),
                 unidadDocumentalAGDTO.getCodigoUnidadDocumental(),
                 unidadDocumentalAGDTO.getNombreUnidadDocumental(),
                 unidadDocumentalAGDTO.getDescriptor2(),
                 unidadDocumentalAGDTO.getDescriptor1(),
                 unidadDocumentalAGDTO.getEstado(),
                 unidadDocumentalAGDTO.getDisposicion(),
                 unidadDocumentalAGDTO.getFechaExtremaFinal(),
                 unidadDocumentalAGDTO.getCerrada(),
                 unidadDocumentalAGDTO.getCodigoSubSerie(),
                 unidadDocumentalAGDTO.getNombreSubSerie(),
                 unidadDocumentalAGDTO.getCodigoSerie(),
                 unidadDocumentalAGDTO.getNombreSerie(),
                 unidadDocumentalAGDTO.getCodigoDependencia(),
                 unidadDocumentalAGDTO.getNombreDependencia(),
                 unidadDocumentalAGDTO.getCodigoSede(),
                 unidadDocumentalAGDTO.getNombreSede(),
                 unidadDocumentalAGDTO.getObservaciones(),
                 unidadDocumentalAGDTO.getListaDocumentos()
         ) ;

        unidad.setConsecutivoTransferencia(unidadDocumentalAGDTO.getConsecutivoTransferencia());

        return unidad;

      }




}
