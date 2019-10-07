package co.com.foundation.sgd.dto;


import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class UnidadConservacionesDTO {

     private String codigos;

     private String codUnidadDocumental;

     private String nomUnidadDocumental;

     private String descriptor1;

     private String descriptor2;

     private String fechaInicial;

     private String fechaFinal;

     private String carpeta;

     private String ideUbiCarpeta = "";

     private String codOrganigrama;

     private String ideBodega;

    private  String codUbicacion;

    private  String descripcion;

    private String codResponse;
}
