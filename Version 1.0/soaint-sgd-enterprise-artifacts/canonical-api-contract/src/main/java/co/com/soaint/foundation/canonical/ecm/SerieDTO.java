package co.com.soaint.foundation.canonical.ecm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(includeFieldNames = false, of = "nombreSerie")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/serie/1.0.0")
public class SerieDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    protected String codigoSerie;
    protected String nombreSerie;
    protected String grupoSeguridad;

    SerieDTO(String codigoBase, String nombreBase, String codigoSerie, String nombreSerie) {
        super(codigoBase, nombreBase);
        this.codigoSerie = codigoSerie;
        this.nombreSerie = nombreSerie;
    }
}
