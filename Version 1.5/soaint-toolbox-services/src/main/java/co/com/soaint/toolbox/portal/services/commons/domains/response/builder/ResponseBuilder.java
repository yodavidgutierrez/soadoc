package co.com.soaint.toolbox.portal.services.commons.domains.response.builder;


import co.com.soaint.toolbox.portal.services.commons.enums.TransactionState;
import com.fasterxml.jackson.annotation.JsonFormat;

import co.com.soaint.toolbox.portal.services.commons.domains.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResponseBuilder<T> {

    private T response;
    private HttpStatus httpStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timeResponse;
    private String path;
    private String message;
    private TransactionState state;

    private ResponseBuilder() {
    }

    public static ResponseBuilder newBuilder() {
        return new ResponseBuilder();
    }

    public ResponseBuilder withResponse(T response) {
        this.response = response;
        this.timeResponse = LocalDateTime.now();
        return this;
    }

    public ResponseBuilder withPath(String path) {
        this.path = path;
        this.timeResponse = LocalDateTime.now();
        return this;
    }

    public ResponseBuilder withStatus(HttpStatus status) {
        this.httpStatus = status;
        return this;
    }

    public ResponseBuilder withMessage(String message) {
        this.message = message;
        this.timeResponse = LocalDateTime.now();
        return this;
    }

    public ResponseBuilder withTransactionState(TransactionState state) {
        this.state = state;
        this.timeResponse = LocalDateTime.now();
        return this;
    }

    public ResponseEntity buildResponse() {
        BaseResponse base = new BaseResponse(this.response, this.httpStatus, this.timeResponse, this.message, this.path, this.state);
        return new ResponseEntity(base, this.httpStatus);
    }
    public BaseResponse buildSimpleResponse() {
        return new BaseResponse(this.response, this.httpStatus, this.timeResponse, this.message, this.path, this.state);
    }
}
