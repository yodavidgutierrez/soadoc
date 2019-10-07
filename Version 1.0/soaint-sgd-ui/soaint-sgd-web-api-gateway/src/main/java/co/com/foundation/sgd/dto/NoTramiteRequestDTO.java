package co.com.foundation.sgd.dto;

import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;

@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class NoTramiteRequestDTO {

    private BigInteger idArchivista;

    private SolicitudUnidadDocumentalDTO solicitudUnidadDocumentalDTO;
}
