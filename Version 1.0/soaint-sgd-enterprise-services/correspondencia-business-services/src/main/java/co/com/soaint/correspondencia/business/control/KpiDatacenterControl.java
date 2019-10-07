package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.KpiDatacenter;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Transactional
@BusinessControl
public class KpiDatacenterControl extends KpiControl<KpiDatacenter> implements Serializable {

    private static final Long serialVersionUID = 87946464646465L;

    private final String kpiNumber11 = "kpi11";

    private final String nQFindNumberAndDateTime = "KpiDatacenter.findByKpiNumberAndDateTime";
    private final String nQfindMaxId = "KpiDatacenter.findMaxId";

    public KpiDatacenterControl() {
        super(KpiDatacenter.class);
    }

    @PostConstruct
    public void init() {

        final KpiHelper helper = new KpiHelper();
        helper.setProcessName("Organización y Archivo");
        helper.setKpiName("Cantidad de documentos electrónicos pendientes por archivar");
        kpiHelperMap.put(kpiNumber11, helper);

        final Map<String, Object> parameters = new HashMap<>();
        final Timestamp startDateTime = Timestamp.valueOf(atStartOfDay());
        final Timestamp currentDateTime = Timestamp.valueOf(LocalDateTime.now());

        parameters.put("START_DATE", startDateTime);
        parameters.put("END_DATE", currentDateTime);
        parameters.put("KPI_NUMBER", kpiNumber11);

        namedQueryMap.put(nQFindNumberAndDateTime, parameters);
        namedQueryMap.put(nQfindMaxId, new HashMap<>());
    }

    public Response processKpi11(Map<String, String> processMap) throws SystemException {
        try {
            log.info("Mapa --------------------- {}", processMap);
            final String dependencyCode = String.valueOf(processMap.get("dependencyCode"));
            //final String producedDocumentsAmount = String.valueOf(processMap.get("producedDocumentsAmount"));
            final String filedDocuments = String.valueOf(processMap.get("filedDocuments"));
            final String unfiledDocuments = String.valueOf(processMap.get("unfiledDocuments"));
            final String dependencyName = String.valueOf(processMap.get("dependencyName"));

            final List<KpiDatacenter> listKpi = getListKpi(nQFindNumberAndDateTime);

            if (listKpi.isEmpty()) {
                KpiDatacenter kpiDatacenter = KpiDatacenter.newInstance()
                        .nombre(kpiHelperMap.get(kpiNumber11).getKpiName())
                        .procesoNombre(kpiHelperMap.get(kpiNumber11).getProcessName())
                        .datacenterId(generateId(nQfindMaxId))
                        .dataN01(new BigInteger(filedDocuments))
                        .dataN02(new BigInteger(unfiledDocuments))
                        //.dataN03(new BigInteger(producedDocumentsAmount))
                        .dataX01(kpiNumber11)
                        .dataX02(dependencyCode)
                        .dataX03(dependencyName)
                        .dataX04("Pendientes por Archivar")
                        .build();

                em.persist(kpiDatacenter);

                kpiDatacenter = (KpiDatacenter) kpiDatacenter.clone();
                kpiDatacenter.setDatacenterId(generateId(nQfindMaxId));
                kpiDatacenter.setDataX04("Archivados");

                em.persist(kpiDatacenter);
                em.flush();
                return Response.status(Response.Status.CREATED).build();
            }
            boolean flush = false;
            for (KpiDatacenter kpiDatacenter : listKpi) {
                //kpiDatacenter.setDataN01(new BigInteger(producedDocumentsAmount));
                final BigInteger filed = new BigInteger(filedDocuments);
                final BigInteger unFiled = new BigInteger(unfiledDocuments);

                boolean flag = false;
                if (!filed.equals(kpiDatacenter.getDataN01())) {
                    flag = true;
                    kpiDatacenter.setDataN01(filed);
                }
                if (!unFiled.equals(kpiDatacenter.getDataN02())) {
                    flag = true;
                    kpiDatacenter.setDataN02(unFiled);
                }

                if (flag) {
                    em.merge(kpiDatacenter);
                    flush = true;
                }
            }
            if (flush)
                em.flush();
            return Response.status(Response.Status.OK).build();
        } catch (Exception ex) {
            log.error("An error has occurred {}", ex.getMessage());
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }
}
