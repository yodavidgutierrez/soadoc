package co.com.foundation.soaint.documentmanager.web.infrastructure.common;

/**
 * Created by administrador_1 on 04/09/2016.
 */
public class HTTPResponse {

    private boolean success;
    private String message;
    private Object value;

    public HTTPResponse(boolean success, String message, Object value) {
        this.success = success;
        this.message = message;
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() { return message; }

    public Object getValue() {
        return value;
    }

}
