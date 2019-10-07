package co.com.soaint.foundation.canonical.bpm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * Created by Arce on 6/8/2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/entradaproceso/1.0.0")
@ToString
public class EntradaProcesoDTO {
    private String idDespliegue;
    private String idProceso;
    private Long instanciaProceso;
    private int page;
    private int pageSize;
    private Long idTarea;
    private String usuario;
    private String pass;
    private Map<String, Object> parametros;
    private List<EstadosEnum> estados;
}
