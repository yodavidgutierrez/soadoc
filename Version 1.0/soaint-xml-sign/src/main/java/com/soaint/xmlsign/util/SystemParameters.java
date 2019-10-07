package com.soaint.xmlsign.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Base64;

/**
 * Clase utilitaria para devolver las constantes
 */
public final class SystemParameters {

    public static final String BUSINESS_PLATFORM_ENDPOINT_ECM = "ecm-endpoint";
    public static final String BUSINESS_PLATFORM_USER = "ecm-user";
    public static final String BUSINESS_PLATFORM_PASS = "ecm-pass";
    public static final String BUSINESS_PLATFORM_RECORD = "record-endpoint";
    public static final String API_SEARCH_ALFRESCO = "search-endpoint";
    public static final String API_CORE_ALFRESCO = "core-endpoint";
    public static final String API_SERVICE_ALFRESCO = "service-endpoint";
    public static final String BUSINESS_PLATFORM_ENDPOINT_BPM = "bpm-api-endpoint";
    public static final String BUSINESS_PLATFORM_ENDPOINT_CORRESPONDENCIA = "correspondencia-api-service";

    @Autowired
    private Environment env;

    private SystemParameters() {
    }

    /**
     * Metodo que devuelve el valor de las constantes
     *
     * @param parameterName Numbre del parametro que se va a devolver
     * @return String
     */
    public static String getParameter(final String parameterName) {

        //servidor de azure
        switch (parameterName) {
            case BUSINESS_PLATFORM_ENDPOINT_ECM:
                return "http://13.85.68.8:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom";
            case BUSINESS_PLATFORM_USER:
                return "admin";
            case BUSINESS_PLATFORM_PASS:
                return "admin2020";
            case BUSINESS_PLATFORM_RECORD:
                return "http://13.85.68.8:8080/alfresco/api/-default-/public/gs/versions/1";
            case API_SEARCH_ALFRESCO:
                return "http://13.85.68.8:8080/alfresco/api/-default-/public/search/versions/1/search";
            case API_CORE_ALFRESCO:
                return "http://13.85.68.8:8080/alfresco/api/-default-/public/alfresco/versions/1";
            case BUSINESS_PLATFORM_ENDPOINT_BPM:
                return "http://13.85.68.8:8080/bpm-integration-services/apis";
            case BUSINESS_PLATFORM_ENDPOINT_CORRESPONDENCIA:
                return "http://localhost:8080/correspondencia-business-services/services";
            default:
                return "";
        }

        //servidor local
        /*switch (parameterName) {
            case BUSINESS_PLATFORM_ENDPOINT_ECM:
                return "http://192.168.1.43:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom";
            case BUSINESS_PLATFORM_USER:
                return "admin";
            case BUSINESS_PLATFORM_PASS:
                return "alfresco2019";
            case BUSINESS_PLATFORM_RECORD:
                return "http://192.168.1.43:8080/alfresco/api/-default-/public/gs/versions/1";
            case API_SEARCH_ALFRESCO:
                return "http://192.168.1.43:8080/alfresco/api/-default-/public/search/versions/1/search";
            case API_CORE_ALFRESCO:
                return "http://192.168.1.43:8080/alfresco/api/-default-/public/alfresco/versions/1";
            case BUSINESS_PLATFORM_ENDPOINT_BPM:
                return "http://10.41.0.43:8080/bpm-integration-services/apis";
            case BUSINESS_PLATFORM_ENDPOINT_CORRESPONDENCIA:
                return "http://localhost:8080/correspondencia-business-services/services";
            default:
                return "";
        }*/
        /*switch (parameterName) {
            case BUSINESS_PLATFORM_ENDPOINT_ECM:
                return "http://10.41.0.82:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom";
            case BUSINESS_PLATFORM_USER:
                return "Admin";
            case BUSINESS_PLATFORM_PASS:
                return "alfresco2018";
            case BUSINESS_PLATFORM_RECORD:
                return "http://10.41.0.82:8080/alfresco/api/-default-/public/gs/versions/1";
            case API_SEARCH_ALFRESCO:
                return "http://10.41.0.82:8080/alfresco/api/-default-/public/search/versions/1/search";
            case API_CORE_ALFRESCO:
                return "http://10.41.0.82:8080/alfresco/api/-default-/public/alfresco/versions/1";
            case BUSINESS_PLATFORM_ENDPOINT_BPM:
                return "http://10.41.0.82:8080/bpm-integration-services/apis";
            case BUSINESS_PLATFORM_ENDPOINT_CORRESPONDENCIA:
                return "http://10.41.0.82:8080/correspondencia-business-services/services";
                default:
                    return "";
        }*/
        //return System.getProperty(parameterName);
    }

    public static String encodeAlfrescCredenciales() {

        String user = getParameter(BUSINESS_PLATFORM_USER);
        String pass = getParameter(BUSINESS_PLATFORM_PASS);

        user = StringUtils.isEmpty(user) ? "admin" : user;
        pass = StringUtils.isEmpty(pass) ? "admin" : pass;

        return Base64.getEncoder()
                .encodeToString((user + ":" + pass).getBytes());
    }

}