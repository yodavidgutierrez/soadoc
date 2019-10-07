package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Generic Artifact
 * Created: 22-Jun-2017
 * Author: esanchez
 * Type: JAVA class
 * Artifact Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Data
@Builder(builderMethodName="newInstance")
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/tvsorgaadminxfuncipk/1.0.0")
@ToString
public class TvsOrgaAdminXFunciPkDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String idFuncionario;
    private List<String> codigos;
}