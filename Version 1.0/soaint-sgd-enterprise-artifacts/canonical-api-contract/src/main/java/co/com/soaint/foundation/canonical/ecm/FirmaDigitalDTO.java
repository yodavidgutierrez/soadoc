package co.com.soaint.foundation.canonical.ecm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(includeFieldNames = false, of = "nombreSerie")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/serie/1.0.0")
public class FirmaDigitalDTO  extends DocumentoDTO{

    private static final long serialVersionUID = 1L;

    byte[] pdfDocument;
    String idCliente;
    String passwordCliente;
    List<String> idPolitica;
    String nameDocument;
    String noPagina;
    String stringToFind;
    String passwordCifrado;

    public FirmaDigitalDTO(byte[] pdfDocument, String idCliente, String passwordCliente, List<String> idPolitica, String nameDocument, String noPagina, String stringToFind, String passwordCifrado) {
        this.pdfDocument = pdfDocument;
        this.idCliente = idCliente;
        this.passwordCliente = passwordCliente;
        this.idPolitica = idPolitica;
        this.nameDocument = nameDocument;
        this.noPagina = noPagina;
        this.stringToFind = stringToFind;
        this.passwordCifrado = passwordCifrado;
    }



}
