package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;

import java.io.Serializable;
import java.util.List;

public interface KpiContent extends Serializable {

    void processKpi11(List<DocumentoDTO> documentosArchivados, String dependencyCode) throws SystemException;
}
