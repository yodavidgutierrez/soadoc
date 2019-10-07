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
@XmlRootElement(namespace = "http://sic.co/integration/SubserieINT/1.0.0")
public class SubserieINT {

    private Long ideSubserie;
    private String actAdministrativo;
    private Long carAdministrativa;
    private Long carLegal;
    private Long carTecnica;
    private String codSubserie;
    private int estSubserie;
    private String fueBibliografica;
    private String ideUuid;
    private Integer nivEscritura;
    private Integer nivLectura;
    private String nomSubserie;
    private String notAlcance;
    private Long carJuridica;
    private  Long carContable;
    private  Long conPublica;
    private  Long conClasificada;
    private  Long conReservada;
    private String ideMotCreacion;
    private String nombreMotCreacion;
    private Long ideSerie;
}
