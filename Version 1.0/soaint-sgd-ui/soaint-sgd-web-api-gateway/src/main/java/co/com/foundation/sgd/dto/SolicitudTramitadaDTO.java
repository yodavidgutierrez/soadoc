package co.com.foundation.sgd.dto;

import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@Data
@ToString(callSuper = true)
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class SolicitudTramitadaDTO  extends SolicitudUnidadDocumentalDTO {

    private List<UniConservacionDTO> unidadesConservacion;
}
