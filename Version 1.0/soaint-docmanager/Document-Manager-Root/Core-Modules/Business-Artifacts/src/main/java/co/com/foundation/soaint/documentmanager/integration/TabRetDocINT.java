package co.com.foundation.soaint.documentmanager.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;

/**
 * @author erodriguez
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(namespace = "http://sic.co/integration/TabRetDocINT/1.0.0")
public class TabRetDocINT {

    private BigInteger ideTabRetDoc;
    private Long acTrd;
    private Long agTrd;
    private Integer estTabRetDoc;
    private String ideUuid;
    private Integer nivEscritura;
    private Integer nivLectura;
    private String proTrd;
    private BigInteger idDisFinal;
    private String nomDisFinal;
    private String codOrg;
    private String ideUniAmt;
    private BigInteger ideSerie;
    private BigInteger ideSubserie;
}
