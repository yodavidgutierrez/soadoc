package co.com.foundation.sgd.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.Map;

@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class TareaDTO {

    private Long idTarea;
    private String nombre;
    private String estado;
    private Integer prioridad;
    private String idResponsable;
    private String idCreador;
    private Date fechaCreada;
    private Date tiempoActivacion;
    private Date tiempoExpiracion;
    private String idProceso;
    private Long idInstanciaProceso;
    private String idDespliegue;
    private Long idParent;
    private String descripcion;
    private String codigoDependencia;
    private String rol;
    private String nombreProceso;
    private Map<String, Object> variables;

}
