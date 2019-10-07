package co.com.foundation.sgd.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class MegafUpdateUdResponse {

    private String mensaje;

    private  String codigo;

    private Boolean value;
}
