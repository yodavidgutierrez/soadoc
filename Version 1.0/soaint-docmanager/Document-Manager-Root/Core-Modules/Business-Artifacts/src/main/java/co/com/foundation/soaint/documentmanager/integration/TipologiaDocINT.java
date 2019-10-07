package co.com.foundation.soaint.documentmanager.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(namespace = "http://sic.co/integration/TipologiaDocINT/1.0.0")
public class TipologiaDocINT {

    private BigInteger ideTpgDoc;
    private String nomTpgDoc;

}
