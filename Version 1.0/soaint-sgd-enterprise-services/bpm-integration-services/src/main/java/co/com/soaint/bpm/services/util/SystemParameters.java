package co.com.soaint.bpm.services.util;

public class SystemParameters {

    public static final String BUSINESS_PLATFORM_ENDPOINT = "business-platform-endpoint";

    public static final String BUSINESS_PLATFORM_ENDPOINT_CORRESPONDENCIA = "correspondencia-api-service";

    private SystemParameters() {
    }

    public static String getParameter(final String parameterName) {
        return System.getProperty(parameterName);
    }

}
