package co.com.foundation.sgd.dto;

import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@ToString(callSuper = true)
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class UnidadDocumetalAGDTO extends UnidadDocumentalDTO {

    private List<UniConservacionDTO> unidadesConservacion;

    private String entidadRecibe;


}
