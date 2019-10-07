package co.com.foundation.soaint.documentmanager.web.infrastructure.transformer.massiveloader;

import co.com.foundation.soaint.documentmanager.web.domain.SubserieVO;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.SubserieVoBuilder;
import co.com.foundation.soaint.infrastructure.transformer.Transformer;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by administrador_1 on 06/10/2016.
 */

@Component
public class CsvRecordToSubserieVOTransformer implements Transformer<CSVRecord, SubserieVO> {

    public static final String ACT_ADMINISTRATIVO = "ACTO_ADMINISTRATIVO_CREACION";
    public static final String CAR_ADMINISTRATIVA = "CARACTE_ADMINISTRATIVA";
    public static final String CAR_LEGAL = "CARACTE_LEGAL";
    public static final String CAR_TECNICA = "CARACTE_TECNICA";
    public static final String COD_SUBSERIE = "CODIGO";
    public static final String EST_SUBSERIE = "ESTADO_SUBSERIE";
    public static final String FUE_BIBLIOGRAFICA = "FUENTE_BIBLIOGRAFICA";
    public static final String NOM_SUBSERIE = "NOMBRE_SUBSERIE";
    public static final String NOT_ALCANCE = "DESCRIPCION";
    public static final String ID_MOTIVO = "MOTIVO_CREACION";
    public static final String COD_SERIE = "CODIGO_SERIE_DOCUMENTAL";
    public static final String CAR_CONTABLE = "CARACTE_CONTABLE";
    public static final String CAR_JURIDICA = "CARACTE_JURIDICA";
    public static final String CON_PUBLICA = "CONFI_PUBLICA";
    public static final String CON_CLASIFICADA = "CONFI_CLASIFICADA";
    public static final String CON_RESERVADA = "CONFI_RESERVADA";

    //public static final String ID_SERIE = "ID_SERIE";


    @Override
    public SubserieVO transform(CSVRecord record) {


        return SubserieVoBuilder.newBuilder()
                .withCodSubserie(record.get(COD_SUBSERIE))
                .withNomSubserie(record.get(NOM_SUBSERIE))
                .withActAdministrativo(record.get(ACT_ADMINISTRATIVO))
                .withNotAlcance(record.get(NOT_ALCANCE))
                .withCarAdministrativa(record.get(CAR_ADMINISTRATIVA))
                .withCarLegal(record.get(CAR_LEGAL))
                .withCarTecnica(record.get(CAR_TECNICA))
                .withCarContable(record.get(CAR_CONTABLE))
                .withCarJuridica(record.get(CAR_JURIDICA))
                .withConPublica(record.get(CON_PUBLICA))
                .withConClasificada(record.get(CON_CLASIFICADA))
                .withConReservada(record.get(CON_RESERVADA))
                .withEstSubserie(record.get(EST_SUBSERIE))
                .withFueBibliografica(record.get(FUE_BIBLIOGRAFICA))
                .withIdSerie( new BigInteger(record.get(COD_SERIE)))
                .withIdMotivo(new BigInteger(record.get(ID_MOTIVO)))
                .build();
    }



}
