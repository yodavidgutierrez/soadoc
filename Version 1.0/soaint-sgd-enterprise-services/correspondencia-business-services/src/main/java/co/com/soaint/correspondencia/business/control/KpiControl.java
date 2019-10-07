package co.com.soaint.correspondencia.business.control;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KpiControl<T extends Serializable> implements Serializable {

    private static final Long serialVersionUID = 879464646464612L;

    private final Date currentDate = new Date();
    private final Class<T> entityClass;

    final Map<String, Map<String, Object>> namedQueryMap = new HashMap<>();
    final Map<String, KpiHelper> kpiHelperMap = new HashMap<>();

    KpiControl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @PersistenceContext
    protected EntityManager em;

    List<T> getListKpi(String namedQuery) {
        final Map<String, Object> parameters = namedQueryMap.get(namedQuery);
        final TypedQuery<T> namedQueryObject = em.createNamedQuery(namedQuery, entityClass);
        parameters.forEach(namedQueryObject::setParameter);
        return namedQueryObject.getResultList();
    }

    BigInteger generateId(String namedQuery) {
        final BigInteger bigInteger = em.createNamedQuery(namedQuery, BigInteger.class)
                .getSingleResult();
        return null == bigInteger ? BigInteger.ONE : bigInteger.add(BigInteger.ONE);
    }

    LocalDateTime atStartOfDay() {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
        return localDateTime.with(LocalTime.MIN);
    }

    @Getter
    @NoArgsConstructor
    final class KpiHelper {

        private String kpiName;
        private String processName;

        void setKpiName(String kpiName) {
            this.kpiName = null != kpiName ? kpiName.trim().replace(' ', '_').toUpperCase() : "";
        }

        void setProcessName(String processName) {
            this.processName = null != processName ? processName.trim().replace(' ', '_').toUpperCase() : "";
        }
    }
}
