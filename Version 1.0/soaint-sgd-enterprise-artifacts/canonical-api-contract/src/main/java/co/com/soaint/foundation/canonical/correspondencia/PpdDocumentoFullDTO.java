package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:6-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ppd-documento/1.0.0")
@ToString
public class PpdDocumentoFullDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger idePpdDocumento;
    private String codTipoDoc;
    private String descTipoDoc;
    private Date fecDocumento;
    private String asunto;
    private BigInteger nroFolios;
    private BigInteger nroAnexos;
    private String codEstDoc;
    private String descEstDoc;
    private String ideEcm;
}
