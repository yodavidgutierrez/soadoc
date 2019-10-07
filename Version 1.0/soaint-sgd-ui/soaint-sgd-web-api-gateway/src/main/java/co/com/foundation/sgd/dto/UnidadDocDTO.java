package co.com.foundation.sgd.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class UnidadDocDTO {
    private String codSerie;
    private String nomSerie;
    private String codSubserie;
    private String nomSubserie;
    private String codOrganigrama;
    private Long ideUnidDocumental;
    private String valCodigo;
    private Date fecUnidDocumental;
    private Date fecCierTramite;
    private Date fecReapertura;
    private Date fecExtremaInicial;
    private Date fecExtremaFinal;
    private String codEstado;
    private String nombreEstado;
    private String codEstTransf;
    private String nombreEstTransf;
    private String codEstDispFinal;
    private String nombreEstDispFinal;
    private String codFaseArch;
    private String nombreFaseArch;
    private String nomUnidDocumental;
    private String valDescriptor1;
    private String valDescriptor2;
    private String desUnidDoc;
    private Long ideUnidDocFisica;
    private String codTipSoporte;
    private String nombreTipSoporte;
    private String valUniDocRecord;
    private Date fecTransPri;
    private Date fecTransSec;
    private Date fecDispoFinal;
    private Date fechaRegistroTransPri;
    private Date fechaRegistroTransSec;
    private String numTrans;
    private String codTipoDispFinal;
    private String nomTipoDispFinal;
    private Long trd;
    private List<UniConservacionDTO> unidadesConservacion;
}
