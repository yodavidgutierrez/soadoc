package co.com.foundation.sgd.dto;


import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class DigilitazacionDTO {

    private  String tipoDocumental;

    private String arcBase64;
}
