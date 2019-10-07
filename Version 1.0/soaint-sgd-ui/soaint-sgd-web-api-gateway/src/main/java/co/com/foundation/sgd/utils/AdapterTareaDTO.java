package co.com.foundation.sgd.utils;

import co.com.foundation.sgd.dto.ProcessRol;
import co.com.foundation.sgd.dto.TareaDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;

import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AdapterTareaDTO {

   public static TareaDTO convertToTareaDTO(RespuestaTareaDTO respuestaTareaDTO){

        ProcessRol processRol = ProcessRolSettings.GetProcesoPorDemanda()
                                 .parallelStream()
                                 .filter( processRol1 -> processRol1.getIdProceso().equals(respuestaTareaDTO.getIdProceso()))
                                 .collect(Collectors.toList())
                                  .get(0);
      return new TareaDTO(respuestaTareaDTO.getIdTarea()
                                          ,respuestaTareaDTO.getNombre()
                                          ,respuestaTareaDTO.getEstado()
                                          ,respuestaTareaDTO.getPrioridad()
                                          ,respuestaTareaDTO.getIdResponsable()
                                          ,respuestaTareaDTO.getIdCreador()
                                          ,respuestaTareaDTO.getFechaCreada()
                                          ,respuestaTareaDTO.getTiempoActivacion()
                                          ,respuestaTareaDTO.getTiempoExpiracion()
                                          ,respuestaTareaDTO.getIdProceso()
                                          ,respuestaTareaDTO.getIdInstanciaProceso()
                                          ,respuestaTareaDTO.getIdDespliegue()
                                          ,respuestaTareaDTO.getIdParent()
                                          ,respuestaTareaDTO.getDescripcion()
                                          ,respuestaTareaDTO.getCodigoDependencia()
                                          ,respuestaTareaDTO.getRol()
                                          ,processRol.getNombreProceso(),respuestaTareaDTO.getVariables());
    }
}
