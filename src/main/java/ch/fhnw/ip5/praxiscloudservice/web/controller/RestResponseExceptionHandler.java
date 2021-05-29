package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
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

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Map<ErrorCode, HttpStatus> ERROR_CODE_HTTP_STATUS_MAP = new EnumMap<ErrorCode, HttpStatus>(ErrorCode.class);

    static {
        ERROR_CODE_HTTP_STATUS_MAP.put(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        ERROR_CODE_HTTP_STATUS_MAP.put(ErrorCode.CLIENT_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PraxisIntercomException.class)
    protected ResponseEntity<Object> handleIntercomException(PraxisIntercomException e, WebRequest w) {
        final HttpStatus status = determineResponseStatus(e.getErrorCode());
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), status, w);
    }

    private HttpStatus determineResponseStatus(ErrorCode errorCode) {
        return ERROR_CODE_HTTP_STATUS_MAP.getOrDefault(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleGeneralException(Exception e, WebRequest w) {
        return handleExceptionInternal(e, "An unexpected error occured", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, w);
    }
}
