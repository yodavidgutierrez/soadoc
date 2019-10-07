package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.KpiDatacenterControl;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Map;

@BusinessBoundary
public class GestionarKpiDatacenter implements Serializable {

    private static final Long serialVersionUID = 879464646464623L;

    private final KpiDatacenterControl kpiDatacenterControl;

    @Autowired
    public GestionarKpiDatacenter(KpiDatacenterControl kpiDatacenterControl) {
        this.kpiDatacenterControl = kpiDatacenterControl;
    }

    public Response processKpi11(Map<String, String> processMap) throws SystemException {
        return kpiDatacenterControl.processKpi11(processMap);
    }
}
