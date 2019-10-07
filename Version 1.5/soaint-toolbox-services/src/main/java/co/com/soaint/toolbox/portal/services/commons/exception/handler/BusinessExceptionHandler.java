package co.com.soaint.toolbox.portal.services.commons.exception.handler;

import co.com.soaint.toolbox.portal.services.commons.domains.response.BaseResponse;
import co.com.soaint.toolbox.portal.services.commons.domains.response.builder.ResponseBuilder;
import co.com.soaint.toolbox.portal.services.commons.enums.TransactionState;
import co.com.soaint.toolbox.portal.services.commons.exception.business.NoCreatedException;
import co.com.soaint.toolbox.portal.services.commons.exception.business.NoUpdatedException;
import co.com.soaint.toolbox.portal.services.commons.exception.business.ResponseNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({ResponseNotFoundException.class})
    public ResponseEntity<Object> notFoundHandler(Exception ex, WebRequest request, HttpServletRequest req){
        BaseResponse response = ResponseBuilder.newBuilder()
                .withResponse("Entity not FOUND")
                .withMessage("Entity not found for ID")
                .withStatus(HttpStatus.NOT_FOUND)
                .withTransactionState(TransactionState.OK)
                .withPath(req.getRequestURI())
                .buildSimpleResponse();
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

    }

    @ExceptionHandler({NoCreatedException.class})
    public ResponseEntity<Object> noCreateHandler(Exception ex, WebRequest request, HttpServletRequest req){
        BaseResponse response = ResponseBuilder.newBuilder()
                .withResponse("Entity not CREATED")
                .withMessage("Entity not CREATED or PERSISTED")
                .withStatus(HttpStatus.BAD_REQUEST)
                .withTransactionState(TransactionState.FAIL)
                .withPath(req.getRequestURI())
                .buildSimpleResponse();
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

    }

    @ExceptionHandler({NoUpdatedException.class})
    public ResponseEntity<Object> noUpdateHandler(Exception ex, WebRequest request, HttpServletRequest req){
        BaseResponse response = ResponseBuilder.newBuilder()
                .withResponse("Entity no UPDATED")
                .withMessage("Entity no UPDATED")
                .withStatus(HttpStatus.NO_CONTENT)
                .withTransactionState(TransactionState.FAIL)
                .withPath(req.getRequestURI())
                .buildSimpleResponse();
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

    }

}
