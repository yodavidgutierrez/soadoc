package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

@Log4j2
class GenericControl<T> implements Serializable {

    private static final Long serialVersionUID = 6546546546L;

    private final Class<T> classType;

    @PersistenceContext
    protected EntityManager em;

    GenericControl(Class<T> classType) {
        this.classType = classType;
    }

    final List<T> getAll() throws SystemException {
        try {
            final CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<T> criteria = builder.createQuery(classType);
            Root<T> from = criteria.from(classType);
            CriteriaQuery<T> select = criteria.select(from);
            log.info("Getting all from {}", from.getModel().getName());
            return em.createQuery(select).getResultList();
        } catch (Exception ex) {
            log.error("Error while getting all entities from class name '{}'", classType.getName());
            throw new SystemException("Error while getting all entities from class name " + classType.getName());
        }
    }

    final void removeAllEntities() throws SystemException {
        try {
            final List<T> all = getAll();
            all.forEach(em::remove);
            em.flush();
            log.info("Deleting {} Success", classType.getName());
        } catch (Exception ex) {
            log.error("Error while deleting entities from class name '{}'", classType.getName());
            throw new SystemException("Error while deleting entities from class name " + classType.getName());
        }
    }
}
