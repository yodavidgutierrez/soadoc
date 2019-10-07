package com.soaint.xmlsign.api;

import com.soaint.xmlsign.web.ContentControlAlfresco;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/xmlsigner")
@Log4j2
public class SignWebApi {


    private final ContentControlAlfresco contentControlAlfresco;

    @Autowired
    public SignWebApi(ContentControlAlfresco contentControlAlfresco) {
        this.contentControlAlfresco = contentControlAlfresco;
    }

    /**
     * Servicio para crear indice electronico
     * @param idFolder id del folder donde se generara el indice electronico
     * @return mensaje de confirmacion del estado del proceso
     */
    @GetMapping("/create/{idFolder}")
    public String createXml(@PathVariable("idFolder") String idFolder) {
        log.info("Servicio para crear el xml del folder : " + idFolder);
        return contentControlAlfresco.createXmlByIdFolder(idFolder);
    }

    /**
     * Servicio para firmar xml de indice electronico
     * @param idFolder id del folder donde esta el indice electronico para firmar
     * @return mensjae de confirmacion del estado del proceso
     */
    @GetMapping("/sign/{idFolder}")
    public String signXml(@PathVariable("idFolder") String idFolder) {
        log.info("Servicio para firmar el xml del folder : " + idFolder);
        return contentControlAlfresco.signXmlByIdFolder(idFolder);
    }

}
