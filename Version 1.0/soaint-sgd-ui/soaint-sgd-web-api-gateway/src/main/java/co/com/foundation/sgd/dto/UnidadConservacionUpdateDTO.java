package co.com.foundation.sgd.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class UnidadConservacionUpdateDTO {

    private String codUnidConserv;
    private Long ideBodega;
    private Long ideUbicFisiBod;
}
