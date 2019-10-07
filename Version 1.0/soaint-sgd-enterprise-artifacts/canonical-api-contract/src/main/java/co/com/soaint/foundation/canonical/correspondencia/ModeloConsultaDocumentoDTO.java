package co.com.soaint.foundation.canonical.correspondencia;

import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/correspondencia/1.0.0")
@ToString
public class ModeloConsultaDocumentoDTO extends DocumentoDTO {

    private static final long serialVersionUID = 1L;

    private BigInteger idDocDb;
    private String funcionario;
    private Date fechaVencimiento;
    private String nroGuia;
    private String estadoEntrega;
    private String tipoCMC;
    private String origen;

    public ModeloConsultaDocumentoDTO(BigInteger idDocDb, String tipoDocumental, String nroRadicado, Date fechaRadicado, String nroGuia,
                                       String estado, Date fechaVencimiento, String estadoEntrega, String tipoCMC, String codigoDependencia, String origen) {
        this.idDocDb = idDocDb;
        this.tipologiaDocumental = tipoDocumental;
        this.nroRadicado = nroRadicado;
        this.fechaRadicacion = fechaRadicado;
        this.nroGuia = nroGuia;
        super.estado = estado;
        this.fechaVencimiento = fechaVencimiento;
        this.estadoEntrega = estadoEntrega;
        this.tipoCMC = tipoCMC;
        this.codigoDependencia = codigoDependencia;
        this.origen = origen;
    }

    public ModeloConsultaDocumentoDTO(BigInteger idDocDb, String tipoDocumental, String nroRadicado, Date fechaRadicado, String nroGuia,
                                      String estado, Date fechaVencimiento, String estadoEntrega, String tipoCMC, String codigoDependencia) {
        this.idDocDb = idDocDb;
        this.tipologiaDocumental = tipoDocumental;
        this.nroRadicado = nroRadicado;
        this.fechaRadicacion = fechaRadicado;
        this.nroGuia = nroGuia;
        super.estado = estado;
        this.fechaVencimiento = fechaVencimiento;
        this.estadoEntrega = estadoEntrega;
        this.tipoCMC = tipoCMC;
        this.codigoDependencia = codigoDependencia;
    }
}
