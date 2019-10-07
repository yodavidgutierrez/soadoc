package co.com.foundation.soaint.documentmanager.integration.transformer;

import co.com.foundation.soaint.documentmanager.integration.domain.DisposicionesFinalesDTO;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmDisFinal;
import co.com.foundation.soaint.infrastructure.transformer.Transformer;
import org.springframework.stereotype.Component;

/**
 * Created by jrodriguez on 29/01/2017.
 */

@Component
public class DisposicionFinalTransformer implements Transformer<AdmDisFinal, DisposicionesFinalesDTO> {

    public DisposicionesFinalesDTO transform(AdmDisFinal admDisFinal) {
        DisposicionesFinalesDTO dto = new DisposicionesFinalesDTO();
        dto.setIdeDisFinal(admDisFinal.getIdeDisFinal());
        dto.setNomDisFinal(admDisFinal.getNomDisFinal());
        dto.setDesDisFinal(admDisFinal.getDesDisFinal());
        dto.setStaDisFinal(admDisFinal.getStaDisFinal());
        dto.setFecCambio(admDisFinal.getFecCambio());
        dto.setFecCreacion(admDisFinal.getFecCreacion());
        return dto;
    }

}
