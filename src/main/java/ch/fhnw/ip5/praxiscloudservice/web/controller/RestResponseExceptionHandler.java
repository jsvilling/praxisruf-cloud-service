package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.EnumMap;
import java.util.Map;

import static ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Map<ErrorCode, HttpStatus> ERROR_CODE_HTTP_STATUS_MAP = new EnumMap<ErrorCode, HttpStatus>(ErrorCode.class);

    static {
        ERROR_CODE_HTTP_STATUS_MAP.put(USER_NOT_FOUND, NOT_FOUND);
        ERROR_CODE_HTTP_STATUS_MAP.put(CLIENT_NOT_FOUND, NOT_FOUND);
        ERROR_CODE_HTTP_STATUS_MAP.put(FCM_ERROR, INTERNAL_SERVER_ERROR);
        ERROR_CODE_HTTP_STATUS_MAP.put(INVALID_NOTIFICATION_TYPE, BAD_REQUEST);
        ERROR_CODE_HTTP_STATUS_MAP.put(NOTIFICATION_TYPE_NOT_FOUND, NOT_FOUND);
        ERROR_CODE_HTTP_STATUS_MAP.put(NOTIFICATION_NOT_FOUND, NOT_FOUND);
    }

    @ExceptionHandler(PraxisIntercomException.class)
    protected ResponseEntity<Object> handleIntercomException(PraxisIntercomException e, WebRequest w) {
        final HttpStatus status = determineResponseStatus(e.getErrorCode());
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), status, w);
    }

    private HttpStatus determineResponseStatus(ErrorCode errorCode) {
        return ERROR_CODE_HTTP_STATUS_MAP.getOrDefault(errorCode, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleGeneralException(Exception e, WebRequest w) {
        return handleExceptionInternal(e, "An unexpected error occurred: " + e.getMessage(), new HttpHeaders(), INTERNAL_SERVER_ERROR, w);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = BAD_REQUEST)
    protected ResponseEntity<Object> handleHibernateException(Exception e, WebRequest w){
        return handleExceptionInternal(e,"A constraint Violation occurred: " + e.getMessage(), new HttpHeaders(), BAD_REQUEST, w);
    }

}
