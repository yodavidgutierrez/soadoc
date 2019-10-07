package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.SystemException;

public interface RecordStructureTree extends StructureAlfresco {

    MensajeRespuesta crearEstructuraDeContent() throws SystemException;
}
