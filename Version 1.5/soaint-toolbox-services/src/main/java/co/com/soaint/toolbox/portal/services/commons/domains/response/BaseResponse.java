package co.com.soaint.toolbox.portal.services.commons.domains.response;

import co.com.soaint.toolbox.portal.services.commons.enums.TransactionState;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseResponse<T> implements Serializable {

    private T body;
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timeResponse;
    private String message;
    private String path;
    private TransactionState transactionState;

    public BaseResponse(T body, HttpStatus status, LocalDateTime timeResponse, String message, String path, TransactionState transactionState) {
        this.body = body;
        this.status = status;
        this.timeResponse = timeResponse;
        this.message = message;
        this.path = path;
        this.transactionState = transactionState;
    }


}
