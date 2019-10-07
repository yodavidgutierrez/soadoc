package co.com.foundation.soaint.documentmanager.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erodriguez on 24/10/2018.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(namespace = "http://sic.co/integration/OrganigramaINT/1.0.0")
public class OrganigramaINT {

    private Long ideOrgaAdmin;
    private String codOrg;
    private String nomOrg;
    private String descOrg;
    private String indEsActivo;
    private Integer codNivel;
    private int nivLectura;
    private int nivEscritura;
    private String ideUuid;
    private String abrevOrg;
    private Long ideOrgaAdminPadre;
    private List<OrganigramaINT> hijos;

    public OrganigramaINT(Long ideOrgaAdmin, String codOrg, String nomOrg, String descOrg, String indEsActivo,
                          Integer codNivel, int nivLectura, int nivEscritura, String ideUuid, String abrevOrg,
                          Long ideOrgaAdminPadre) {
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.descOrg = descOrg;
        this.indEsActivo = indEsActivo;
        this.codNivel = codNivel;
        this.nivLectura = nivLectura;
        this.nivEscritura = nivEscritura;
        this.ideUuid = ideUuid;
        this.abrevOrg = abrevOrg;
        this.ideOrgaAdminPadre = ideOrgaAdminPadre;
        this.hijos = new ArrayList<>();
    }
}
