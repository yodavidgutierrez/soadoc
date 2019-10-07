package com.soaint.xmlsign.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/indice-electronico/1.0.0")
public class IndiceElectronicoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    Calendar fechaIndiceElectronico;
    String identificadorIndiceElectronico;
    String fechaCreacionCarpeta;
    List<ElementoIndiceDTO> elementos;

}
