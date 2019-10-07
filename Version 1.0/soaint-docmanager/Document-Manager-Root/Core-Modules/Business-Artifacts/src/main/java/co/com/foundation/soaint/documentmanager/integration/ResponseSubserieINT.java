package co.com.foundation.soaint.documentmanager.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(namespace = "http://sic.co/integration/ResponseSubserieINT/1.0.0")
public class ResponseSubserieINT {

    private String codSubserie;
    private String nomSubserie;

}
