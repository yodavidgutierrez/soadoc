package co.com.foundation.sgd.dto;

import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class ProcessRol {
    private  String idProceso;
    private  String rol;
    private  Boolean iniciarPorDemanda;
    private  String nombreProceso;
}
