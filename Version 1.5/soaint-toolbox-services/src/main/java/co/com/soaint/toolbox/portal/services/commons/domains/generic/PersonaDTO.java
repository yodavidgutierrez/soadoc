package co.com.soaint.toolbox.portal.services.commons.domains.generic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonaDTO  implements Serializable {

    private Long id;
    private String tipoIdentificacion;
    private Long numeroIdentificacion;
    private String nombres;
    private String apellidos;
    private Date createDate;

}

