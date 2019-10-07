package co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion;

import co.com.foundation.soaint.documentmanager.infrastructure.builder.common.DataTableTrdVOBuilder;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.common.DataTrdVOBuilder;
import co.com.foundation.soaint.infrastructure.annotations.BusinessControl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@BusinessControl
public class TabRetencionDiposicionControl {

    public static final int NOMBRE_UNIDAD_ADMINISTRATIVA = 2;
    public static final int NOMBRE_OFICINA_PRODUCTORA = 5;
    public static final int OFICINA_PRODUCTORA = 4;
    public static final int ID_SERIE = 6;
    public static final int CODIGO_SERIE = 7;
    public static final int NOMBRE_SERIE = 8;
    public static final int ID_SUBSERIE = 9;
    public static final int CODIGO_SUBSERIE = 10;
    public static final int NOMBRE_SUBSERIE = 11;
    public static final int NOMBRE_TIPO_DOCUMENTAL = 12;
    public static final int ARCHIVO_GESTION = 13;
    public static final int ARCHIVO_CENTRAL = 14;
    public static final int DISPOSICION = 16;
    public static final int PROCEDIMIENTO = 15;
    public static final int SOPORTE = 18;
    public static final int CONF_PUBLICA_SERIE = 19;
    public static final int CONF_CLASIFICADA_SERIE = 20;
    public static final int CONF_RESERVADA_SERIE = 21;
    public static final int CONF_PUBLICA_SUBSERIE = 22;
    public static final int CONF_CLASIFICADA_SUBSERIE = 23;
    public static final int CONF_RESERVADA_SUBSERIE = 24;
    public static final int NOMBRE_COMITE = 25;
    public static final int NUM_ACTA = 26;
    public static final int FECHA_ACTA = 27;

    // -------------------

    public DataTrdVOBuilder prepareTrdBuilder(Object[] row) {
        DataTrdVOBuilder builder = DataTrdVOBuilder.newBuilder();
        String codigo = row[OFICINA_PRODUCTORA].toString().concat("-").concat(row[CODIGO_SERIE].toString());
        builder.withCodigo("<b data-toggle=\"tooltip\" title=\"" + codigo + "\">".concat(codigo).concat("</b>"));

        builder.withArchivoGestion(((BigInteger) row[ARCHIVO_GESTION]).longValue());
        builder.withArchivoCentral(((BigInteger) row[ARCHIVO_CENTRAL]).longValue());
        builder.withDisposicionFinal(((BigDecimal) row[DISPOSICION]).intValue());
        builder.withProcedimientos(row[PROCEDIMIENTO].toString());

        builder.withInstrumentos("<b data-toggle=\"tooltip\" title=\"" + row[NOMBRE_SERIE].toString() + "\">".concat(row[NOMBRE_SERIE].toString()).concat("</b>"));

        String confidencialidad = "";
        if (!Objects.isNull(row[CODIGO_SUBSERIE])) {
            builder.withCodigo("<br/><b data-toggle=\"tooltip\" title=\"" + codigo.concat("-").concat(row[CODIGO_SUBSERIE].toString()) +"\">".concat(codigo).concat("-").concat(row[CODIGO_SUBSERIE].toString().concat("</b>")));
            builder.withInstrumentos("<br/>".concat("<b data-toggle=\"tooltip\" title=\"" + row[NOMBRE_SUBSERIE].toString()+ "\">").concat(row[NOMBRE_SUBSERIE].toString()).concat("</b>"));

            if (row[CONF_PUBLICA_SUBSERIE].toString().equals("1")){
                confidencialidad = "P";
            }
            if (row[CONF_CLASIFICADA_SUBSERIE].toString().equals("1")){
                confidencialidad += confidencialidad.length() == 0 ? "C" : ", C";
            }
            if (row[CONF_RESERVADA_SUBSERIE].toString().equals("1")){
                confidencialidad += confidencialidad.length() == 0 ? "R" : ", R";
            }
        } else{
            if (row[CONF_PUBLICA_SERIE].toString().equals("1")){
                confidencialidad = "P";
            }
            if (row[CONF_CLASIFICADA_SERIE].toString().equals("1")){
                confidencialidad += confidencialidad.length() == 0 ? "C" : ", C";
            }
            if (row[CONF_RESERVADA_SERIE].toString().equals("1")){
                confidencialidad += confidencialidad.length() == 0 ? "R" : ", R";
            }
        }
        builder.withConfidencialidad(confidencialidad);

        builder.withInstrumentos("<br><span data-toggle=\"tooltip\" title=\" " + row[NOMBRE_TIPO_DOCUMENTAL].toString() + "\">".concat(row[NOMBRE_TIPO_DOCUMENTAL].toString()).concat("</span>"));
        return builder;
    }

    public DataTableTrdVOBuilder prepareTrdTableBuilder(Object[] row) {
        DataTableTrdVOBuilder builder = DataTableTrdVOBuilder.newBuilder();
        String codigo = row[OFICINA_PRODUCTORA].toString().concat("-").concat(row[CODIGO_SERIE].toString());
        builder.withCodigo(codigo);

        builder.withArchivoGestion(((BigInteger) row[ARCHIVO_GESTION]).longValue());
        builder.withArchivoCentral(((BigInteger) row[ARCHIVO_CENTRAL]).longValue());

        switch (((BigDecimal) row[DISPOSICION]).intValue()) {
            case 1: {
                builder.withM("X");
                break;
            }
            case 2: {
                builder.withCt("X");
                break;
            }
            case 3: {
                builder.withS("X");
                break;
            }
            case 4: {
                builder.withE("X");
                break;
            }
            case 5: {
                builder.withD("X");
                break;
            }
        }


        builder.withProcedimientos(row[PROCEDIMIENTO].toString());
        builder.withInstrumentos(row[NOMBRE_SERIE].toString());

        String confidencialidad = "";
        if (!Objects.isNull(row[CODIGO_SUBSERIE])) {
            builder.withCodigo("\n".concat(codigo).concat("-").concat(row[CODIGO_SUBSERIE].toString()));
            builder.withInstrumentos("\n".concat(row[NOMBRE_SUBSERIE].toString()));

            if (row[CONF_PUBLICA_SUBSERIE].toString().equals("1")){
                confidencialidad = "P";
            }
            if (row[CONF_CLASIFICADA_SUBSERIE].toString().equals("1")){
                confidencialidad += confidencialidad.length() == 0 ? "C" : ", C";
            }
            if (row[CONF_RESERVADA_SUBSERIE].toString().equals("1")){
                confidencialidad += confidencialidad.length() == 0 ? "R" : ", R";
            }
        }else{
            if (row[CONF_PUBLICA_SERIE].toString().equals("1")){
                confidencialidad = "P";
            }
            if (row[CONF_CLASIFICADA_SERIE].toString().equals("1")){
                confidencialidad += confidencialidad.length() == 0 ? "C" : ", C";
            }
            if (row[CONF_RESERVADA_SERIE].toString().equals("1")){
                confidencialidad += confidencialidad.length() == 0 ? "R" : ", R";
            }
        }
        builder.withConfidencialidad(confidencialidad);

        builder.withInstrumentos("\n-".concat(row[NOMBRE_TIPO_DOCUMENTAL].toString()));
        return builder;
    }

}
