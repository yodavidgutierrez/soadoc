package co.com.foundation.soaint.documentmanager.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author erodriguez
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(namespace = "http://sic.co/integration/FiltroSerSubINT/1.0.0")
public class FiltroSerSubINT {

    private String codigo;
    private String nombre;

    public FiltroSerSubINT(String nombreSerie, String codigoSerie, String nombreSubSerie, String codigoSubSerie){
        this.codigo = codigoSerie + "/" + codigoSubSerie;
        this.nombre = nombreSerie + "/" + nombreSubSerie;
    }
}
