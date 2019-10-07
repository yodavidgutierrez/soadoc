package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorObservacion;
import co.com.soaint.foundation.canonical.correspondencia.ObservacionDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@BusinessControl
public class ObservacionControl implements Serializable {

    private static Long serialVersionUID = 111111978L;

    @PersistenceContext
    private EntityManager em;


    public List<ObservacionDTO> listarObservacionesByIdInstancia(String idInstancia) {

        List<ObservacionDTO> observaciones = em.createNamedQuery("CorObservacion.findAllbyIdInstance", ObservacionDTO.class)
                .setParameter("ID_INSTANCIA", idInstancia)
                .getResultList();
        if (!ObjectUtils.isEmpty(observaciones)) {
            return observaciones;
        } else {
            return new ArrayList<>();
        }
    }

    public boolean insertarObservaciones(List<ObservacionDTO> observaciones) {
        boolean respuesta = false;
        if (ObjectUtils.isEmpty(observaciones)) {
            return respuesta;
        } else {
            if (!StringUtils.isEmpty(observaciones.get(0).getIdInstancia())) {
                String idInstancia = observaciones.get(0).getIdInstancia();
                em.createNamedQuery("CorObservacion.deleteAllbyIdInstance")
                        .setParameter("ID_INSTANCIA", idInstancia)
                        .executeUpdate();
                for (ObservacionDTO observ : observaciones) {
                    CorObservacion corObservacion = CorObservacion.newInstance()
                            .fechaCeracion(observ.getFechaCreacion())
                            .idInstancia(observ.getIdInstancia())
                            .idTarea(observ.getIdTarea())
                            .observacion(observ.getObservacion())
                            .usuario(observ.getUsuario())
                            .build();
                    em.persist(corObservacion);
                }
                return true;
            }
        }
        return respuesta;
    }
}