package co.com.foundation.sgd.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class UnidadDocumentalUpdateDTO {

    private String codUnidDoc;
    private String numTrans;
    private String descripcion;
    private String estadoTrans;
    private String tipoDF;
    private String proceso;
    private String nombreEntidadRecibe;
    private List<UnidadConservacionUpdateDTO> unidadesConservacion;
}
