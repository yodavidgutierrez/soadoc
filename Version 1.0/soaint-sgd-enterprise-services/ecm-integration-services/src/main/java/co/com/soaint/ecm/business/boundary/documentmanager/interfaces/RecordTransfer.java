package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;

import java.io.Serializable;
import java.util.List;

public interface RecordTransfer extends Serializable {

    /**
     * Operacion para devolver Las unidades documentales a Transferir
     *
     * @return MensajeRespuesta
     */
    //1
    MensajeRespuesta listarUnidadesDocumentalesTransferencia(String tipoTransferencia, String dependencyCode) throws SystemException;

    //2
    MensajeRespuesta aprobarRechazarTransferencias(List<UnidadDocumentalDTO> unidadDocumentalDTOS, String tipoTransferencia) throws SystemException;

    //3
    MensajeRespuesta listarUnidadesDocumentalesVerificar(String dependencyCode, String numTransfer) throws SystemException;

    //4 6
    MensajeRespuesta confirmarUnidadesDocumentalesTransferidas(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException;

    //5
    MensajeRespuesta listarUnidadesDocumentalesConfirmadas(String dependencyCode, String numTransfer) throws SystemException;
}
