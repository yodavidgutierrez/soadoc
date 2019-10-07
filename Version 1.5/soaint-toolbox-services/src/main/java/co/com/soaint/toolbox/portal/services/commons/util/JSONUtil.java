package co.com.soaint.toolbox.portal.services.commons.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSONUtil {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private JSONUtil() {
    }

    public static <T> T unmarshal(final String input, final Class<T> type) {
        T output = null;
        try {
            output = jsonMapper.readValue(input, type);
        } catch (IOException e) {
            return output;
        }
        return output;
    }

    public static String marshal(final Object input) {
        String output = null;
        try {
            return jsonMapper.writeValueAsString(input);
        } catch (IOException e) {
            return output;
        }
    }

}
