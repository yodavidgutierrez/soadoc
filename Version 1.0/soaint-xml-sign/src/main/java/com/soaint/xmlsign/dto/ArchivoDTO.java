package com.soaint.xmlsign.dto;

import lombok.*;

import java.io.File;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class ArchivoDTO {

    private File documento;
    private String extension;

}
