package co.com.foundation.soaint.documentmanager.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erodriguez on 06/11/2018.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(namespace = "http://sic.co/integration/DependenciaINT/1.0.0")
public class DependenciaINT {

    private Long id;
    private String codigo;
    private String nombre;
    private Integer nivel;
    private String estado;
    private Long idPadre;
    private List<DependenciaINT> padres;

    public DependenciaINT(Long id, String codigo, String nombre, Integer nivel, String estado, Long idPadre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.nivel = nivel;
        this.estado = estado;
        this.idPadre = idPadre;
        padres = new ArrayList<>();
    }
}
