package co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic;

import co.com.foundation.soaint.documentmanager.web.infrastructure.common.HTTPResponse;
import co.com.foundation.soaint.infrastructure.builder.Builder;

/**
 * Created by administrador_1 on 04/09/2016.
 */
public class HTTPResponseBuilder implements Builder<HTTPResponse> {

    private boolean success;
    private String message;
    private Object value;

    private HTTPResponseBuilder() {
    }

    public static HTTPResponseBuilder newBuilder() {
        return new HTTPResponseBuilder();
    }

    public HTTPResponseBuilder withSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public HTTPResponseBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public HTTPResponseBuilder withValue(Object value) {
        this.value = value;
        return this;
    }

    public HTTPResponse build() {
        return new HTTPResponse(success, message, value);
    }

}
