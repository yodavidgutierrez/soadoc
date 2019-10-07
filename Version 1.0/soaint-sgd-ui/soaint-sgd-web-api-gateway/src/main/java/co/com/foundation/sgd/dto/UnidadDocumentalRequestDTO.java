package co.com.foundation.sgd.dto;

import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;
import java.util.List;

@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class UnidadDocumentalRequestDTO {

    private UnidadConservacionesDTO unidadConservacion;

    private UnidadDocumentalDTO unidadDocumentalDTO;

    private BigInteger idArchivista;

    private BigInteger idSolicitante;
}
