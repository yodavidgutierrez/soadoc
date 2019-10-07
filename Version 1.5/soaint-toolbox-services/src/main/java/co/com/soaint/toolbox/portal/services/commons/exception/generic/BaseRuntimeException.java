package co.com.soaint.toolbox.portal.services.commons.exception.generic;

public abstract class BaseRuntimeException extends RuntimeException {

    public BaseRuntimeException() {
    }

    public BaseRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
