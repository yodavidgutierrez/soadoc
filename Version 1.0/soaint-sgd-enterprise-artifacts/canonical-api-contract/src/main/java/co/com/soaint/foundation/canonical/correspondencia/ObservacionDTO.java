package co.com.soaint.foundation.canonical.correspondencia;


import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-observacion/1.0.0")
@ToString
public class ObservacionDTO {

    private String observacion;
    private String usuario;
    private String idInstancia;
    private Date fechaCreacion;
    private String idTarea;
}
