package com.soaint.xmlsign.dto;

import lombok.*;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResponseDTO {

    String flagExiste;
    Document xml;
    Folder folder;
    String ms;
    String indiceXml;
    boolean vacio = true;

}
