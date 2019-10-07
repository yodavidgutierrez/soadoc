package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.ConsultaDocumentosControl;
import co.com.soaint.foundation.canonical.correspondencia.ModeloConsultaDocumentoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 14-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
public class GestionarConsultaDocumentos {
    // [fields] -----------------------------------

    @Autowired
    ConsultaDocumentosControl control;
    // ----------------------


    public List<ModeloConsultaDocumentoDTO> consultarCorrespondencias(String userLogin, Date startDate, Date endDate, String tipoComunicacion, String nroRadicado, String nroIdentificacion, String depOrigen, String depDestino,
                                                                      String nroGuia, String asunto, String solicitante, String destinatario, String tipoDocumento, String tipologiaDocumental) throws SystemException {
        return control.consultarCorrespondencias(userLogin, startDate, endDate, tipoComunicacion, nroRadicado,
                nroIdentificacion, depOrigen, depDestino, nroGuia, asunto, solicitante, destinatario, tipoDocumento, tipologiaDocumental);
    }

}
