package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.KpiReporteControl;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

@Log4j2
@BusinessBoundary
public class GestionarKpiReporte implements Serializable {

    private static final Long serialVersionUID = 879464646464123L;

    private final KpiReporteControl reporteControl;

    @Autowired
    public GestionarKpiReporte(KpiReporteControl reporteControl) {
        this.reporteControl = reporteControl;
    }

    public Response processRep7(List<DocumentoDTO> dtoList) throws SystemException {
        return reporteControl.processRep7(dtoList);
    }

    public Response processRep8(List<UnidadDocumentalDTO> dtoList) throws SystemException {
        return reporteControl.processRep8(dtoList);
    }

    public Response processRep9(List<UnidadDocumentalDTO> dtoList) throws SystemException {
        return reporteControl.processRep9(dtoList);
    }
}
