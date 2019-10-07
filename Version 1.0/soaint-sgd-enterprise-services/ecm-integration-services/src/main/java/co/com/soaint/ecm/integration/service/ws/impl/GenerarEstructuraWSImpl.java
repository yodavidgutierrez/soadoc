package co.com.soaint.ecm.integration.service.ws.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.StructureAlfresco;
import co.com.soaint.ecm.integration.service.ws.GenerarEstructuraWS;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@Log4j2
@WebService(targetNamespace = "http://co.com.soaint.ecm.integration.service.ws",
        serviceName = "GenerarEstructuraWS", name = "GenerarEstructuraWS")
@Service(value = "generarEstructuraWS")
public class GenerarEstructuraWSImpl implements GenerarEstructuraWS {

    @Autowired
    @Qualifier("recordStructure")
    private StructureAlfresco recordStructure;
    @Autowired
    @Qualifier("contentStructure")
    private StructureAlfresco contentStructure;
    /*@Autowired
    private ContentControl contentControl;*/

    @WebMethod(operationName = "createStructureECM", action = "createStructureECM")
    public MensajeRespuesta crearEstructuraEcm(@WebParam(name = "estructura") List<EstructuraTrdDTO> structure) {
        try {
            System.out.println(structure);
            log.info(structure);
            if (contentStructure == null) {
                log.info("Content Control es nulo en metodo crearEstructuraEcm");
                throw ExceptionBuilder.newBuilder()
                        .withMessage("Content Control es nulo")
                        .buildSystemException();
            }

            final MensajeRespuesta mensajeRespuesta = contentStructure.crearEstructura(structure);

            new Thread(() -> {
                try {
                    this.recordStructure.crearEstructura(structure);
                } catch (SystemException e) {
                    e.printStackTrace();
                }
            }).start();

            return mensajeRespuesta;
        } catch (SystemException s) {
            log.error("Error Ocurrido {}", s.getCause());
            return MensajeRespuesta.newInstance()
                    .codMensaje(ConstantesECM.ERROR_COD_MENSAJE)
                    .mensaje("Error Ocurrido " + s.getMessage())
                    .build();
        }
    }
}
