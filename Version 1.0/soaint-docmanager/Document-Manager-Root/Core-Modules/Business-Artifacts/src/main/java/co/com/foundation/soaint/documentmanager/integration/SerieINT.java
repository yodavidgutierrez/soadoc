package co.com.foundation.soaint.documentmanager.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author erodriguez
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(namespace = "http://sic.co/integration/SerieINT/1.0.0")
public class SerieINT {

    private Long ideSerie;
    private String actAdministrativo;
    private Long carAdministrativa;
    private Long carLegal;
    private Long carTecnica;
    private Long carJuridica;
    private Long carContable;
    private Long conPublica;
    private Long conClasificada;
    private Long conReservada;
    private String codSerie;
    private int estSerie;
    private String fueBibliografica;
    private String ideUuid;
    private Integer nivEscritura;
    private Integer nivLectura;
    private String nomSerie;
    private String notAlcance;
    private String ideMotCreacion;
    private String nombreMotCreacion;
    private List<SubserieINT> subseries;
}
