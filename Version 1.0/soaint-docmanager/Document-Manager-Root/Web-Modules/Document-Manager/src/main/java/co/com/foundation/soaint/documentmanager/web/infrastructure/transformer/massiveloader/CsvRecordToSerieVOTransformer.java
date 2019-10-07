package co.com.foundation.soaint.documentmanager.web.infrastructure.transformer.massiveloader;

import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.SerieVoBuilder;
import co.com.foundation.soaint.infrastructure.transformer.Transformer;
import co.com.foundation.soaint.documentmanager.web.domain.SerieVO;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * Created by administrador_1 on 06/10/2016.
 */

@Component
public class CsvRecordToSerieVOTransformer implements Transformer<CSVRecord, SerieVO> {

    public static final String ACT_ADMINISTRATIVO = "ACTO_ADMINISTRATIVO_CREACION";
    public static final String CAR_ADMINISTRATIVA = "CARACTE_ADMINISTRATIVA";
    public static final String CAR_LEGAL = "CARACTE_LEGAL";
    public static final String CAR_TECNICA = "CARACTE_TECNICA";
    public static final String COD_SERIE = "CODIGO";
    public static final String EST_SERIE = "ESTADO_SERIE";
    public static final String FUE_BIBLIOGRAFICA = "FUENTE_BIBLIOGRAFICA";
    public static final String NOMB_SERIE = "NOMBRE_SERIE";
    public static final String NOT_ALCANCE = "DESCRIPCION";
    public static final String ID_MOTIVO = "MOTIVO_CREACION";
    public static final String CAR_JURIDICA = "CARACTE_JURIDICA";
    public static final String CAR_CONTABLE = "CARACTE_CONTABLE";
    public static final String CON_PUBLICA = "CONFI_PUBLICA";
    public static final String CON_CLASIFICADA = "CONFI_CLASIFICADA";
    public static final String CON_RESERVADA = "CONFI_RESERVADA";

    @Override
    public SerieVO transform(CSVRecord record) {

        return SerieVoBuilder.newBuilder()
                .withCodSerie(record.get(COD_SERIE))
                .withNomSerie(record.get(NOMB_SERIE))
                .withCarLegal(record.get(CAR_LEGAL))
                .withCarAdministrativa(record.get(CAR_ADMINISTRATIVA))
                .withCarTecnica(record.get(CAR_TECNICA))
                .withCarContable(record.get(CAR_CONTABLE))
                .withCarJuridica(record.get(CAR_JURIDICA))
                .withConPublica(record.get(CON_PUBLICA))
                .withConClasificada(record.get(CON_CLASIFICADA))
                .withConReservada(record.get(CON_RESERVADA))
                .withActAdministrativo(record.get(ACT_ADMINISTRATIVO))
                .withIdMotivo(new BigInteger(record.get(ID_MOTIVO)))
                .withNotAlcance(record.get(NOT_ALCANCE))
                .withFueBibliografica(record.get(FUE_BIBLIOGRAFICA))
                .withEstSerie(record.get(EST_SERIE))
                .build();
    }

}
