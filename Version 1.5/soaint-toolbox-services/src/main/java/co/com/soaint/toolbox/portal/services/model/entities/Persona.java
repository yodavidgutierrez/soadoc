package co.com.soaint.toolbox.portal.services.model.entities;


import co.com.soaint.toolbox.portal.services.model.entities.base.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false, of = {"numeroIdentificacion"})
public class Persona extends BaseEntity implements Serializable {

    private String tipoIdentificacion;
    private Long numeroIdentificacion;
    private String nombres;
    private String apellidos;
    @Temporal(TemporalType.DATE)
    private Date createDate;


}
