package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.SystemException;

import java.io.Serializable;

public interface QueryContent extends Serializable {

    MensajeRespuesta consultarDocumentos(String userLogin, String startDate, String endDate, String tipoComunicacion, String nroRadicado,
                                         String nroIdentificacion, String depOrigen, String depDestino, String depCode, String sCode, String ssCode,
                                         String nroGuia, String nombre, String asunto, String solicitante, String destinatario,
                                         String tipoDocumento, String tramite, String evento, String actuacion, String tipologiaDocumental) throws SystemException;

    MensajeRespuesta consultarExpedientes(String userLogin, String depJ, String depP, String sCode, String ssCode,
                                          String udId, String udName, String desc1, String desc2) throws SystemException;

    MensajeRespuesta verDetalleDocumento(String docEcmId) throws SystemException;

    MensajeRespuesta consultarDocsExpediente(String ecmFolderId) throws SystemException;
}
