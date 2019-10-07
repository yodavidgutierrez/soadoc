package com.soaint.xmlsign.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/elemento-indice-electronico/1.0.0")
public class ElementoIndiceDTO {

    String nombreDocumento;
    String tipologiaDocumental;
    Calendar fechaCreacionDocumento;
    Calendar fechaIncorporacionExpediente;
    String valorHuella;
    String funcionResumen;
    String ordenDocumentoExpediente;
    String paginaInicio;
    String paginaFin;
    String formato;
    String tamano;

}
