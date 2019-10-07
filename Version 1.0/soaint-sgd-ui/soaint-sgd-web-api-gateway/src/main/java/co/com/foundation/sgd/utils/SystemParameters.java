package co.com.foundation.sgd.utils;

public class SystemParameters {

    public static final String BACKAPI_ENDPOINT_URL = "correspondencia-api-service";
    public static final String BACKAPI_CARGAMASIVA_ENDPOINT_URL = "massive-loader-api-endpoint";
    public static final String BACKAPI_ENTERPRISE_SERVICE_ENDPOINT_URL = "bpm-api-endpoint";
    public static final String BACKAPI_ENTERPRISE_SERVICE_ENDPOINT_URL_15 = "bpm-api-endpoint-v1.5";
    public static final String BACKAPI_ECM_SERVICE_ENDPOINT_URL = "ecm-api-endpoint";
    public static final String BACKAPI_ECM_RECORD_SERVICE_ENDPOINT_URL = "ecm-record-api-endpoint";
    public static final String BACKAPI_FUNCIONARIO_SERVICE_ENDPOINT_URL = "funcionario-api-endpoint";
    public static final String BACKAPI_DROOLS_SERVICE_ENDPOINT_URL = "drools-endpoint";
    public static final String BACKAPI_DROOLS_SERVICE_TOKEN = "drools-token";
    public static final String INSTRUMENTO_ENDPOINT = "instrumento-api-endpoint";
    public static final String DIGITALIZACION_ENDPOINT = "digitalizacion-api-endpoint";
    public static final String MEGAF_ENDPOINT = "megaf-api-endpoint";

    private SystemParameters() {
    }

    public static String getParameter(final String parameterName) {
        return System.getProperty(parameterName);
    }


}