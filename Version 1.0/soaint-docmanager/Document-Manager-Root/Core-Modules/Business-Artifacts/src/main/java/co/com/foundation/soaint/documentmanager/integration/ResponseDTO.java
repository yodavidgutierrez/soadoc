package co.com.foundation.soaint.documentmanager.integration;

/**
 * @author erodriguez
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(namespace = "http://sic.co/integration/ResponseDTO/1.0.0")
public class ResponseDTO {
    private String mensaje;
    private String codigo;
    private String value;
}
