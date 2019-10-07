package co.com.soaint.correspondencia.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by esanchez on 8/15/2017.
 */
@Embeddable
@Getter
@Setter
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
public class AuditColumns implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "FEC_CAMBIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCambio;

    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;

    @Column(name = "COD_USUARIO_CREA")
    private String codUsuarioCrea;

    @PrePersist
    protected void onCreate() {
        fecCreacion = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        fecCambio = new Date();
    }
}
