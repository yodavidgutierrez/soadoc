package co.com.foundation.sgd.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;


@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class UniConservacionDTO {

    private Long ideUniDocConserv;
    private Long ideUnidDocumental;
    private Date fecUniDocConserv;
    private Date fecCierre;
    private Date fecReaperturaUdc;
    private Date fecExtremaIniUdc;
    private Date fecExtremaFinUdc;
    private String codEstadoUdc;
    private String nombreEstadoUdc;
    private String codEstPrestamo;
    private String nombreEstPrestamo;
    private String codTipSoporte;
    private String nombreTipSoporte;
    private String valNroUnidDocPadre;
    private String valCodigo;
    private String nomUniDocConsrv;
    private String valDescriptor1;
    private String valDescriptor2;
    private Long nroFoliosUnidDoc;
    private String valNumTomo;
    private String codBodega;
    private String nombreBodega;
    private String codUbicFisi;
    private String nombreUbicFisi;
    private String valRagoIni;
    private String valRagoFin;
    private Long nroRangoIniFolios;
    private Long nroRangoFinFolios;
    private Long ideBodega;
    private Long ideUbicFisiBod;
}
