package ch.fhnw.ip5.praxiscloudservice.commons.web;

import ch.fhnw.ip5.praxiscloudservice.commons.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.commons.exception.PraxisIntercomException;
import lombok.extern.slf4j.Slf4j;
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

@ControllerAdvice
@Slf4j
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Map<ErrorCode, HttpStatus> ERROR_CODE_HTTP_STATUS_MAP = new EnumMap<ErrorCode, HttpStatus>(ErrorCode.class);

    static {
        ERROR_CODE_HTTP_STATUS_MAP.put(ErrorCode.CLIENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        ERROR_CODE_HTTP_STATUS_MAP.put(ErrorCode.CLIENT_CONFIG_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        ERROR_CODE_HTTP_STATUS_MAP.put(ErrorCode.CLIENT_CONFIG_NOT_FOUND, HttpStatus.NOT_FOUND);
        ERROR_CODE_HTTP_STATUS_MAP.put(ErrorCode.FCM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        ERROR_CODE_HTTP_STATUS_MAP.put(ErrorCode.INVALID_NOTIFICATION_TYPE, HttpStatus.BAD_REQUEST);
        ERROR_CODE_HTTP_STATUS_MAP.put(ErrorCode.INVALID_REGISTRATION_INFORMATION, HttpStatus.BAD_REQUEST);
        ERROR_CODE_HTTP_STATUS_MAP.put(ErrorCode.NOTIFICATION_NOT_FOUND, HttpStatus.NOT_FOUND);
        ERROR_CODE_HTTP_STATUS_MAP.put(ErrorCode.NOTIFICATION_TYPE_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        ERROR_CODE_HTTP_STATUS_MAP.put(ErrorCode.NOTIFICATION_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND);
        ERROR_CODE_HTTP_STATUS_MAP.put(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(PraxisIntercomException.class)
    protected ResponseEntity<Object> handleIntercomException(PraxisIntercomException e, WebRequest w) {
        final HttpStatus status = determineResponseStatus(e.getErrorCode());
        log.error("Request failed with Error Code {} - StatusCode {} will be returned", e.getErrorCode(), status);
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), status, w);
    }

    private HttpStatus determineResponseStatus(ErrorCode errorCode) {
        return ERROR_CODE_HTTP_STATUS_MAP.getOrDefault(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleGeneralException(Exception e, WebRequest w) {
        return handleExceptionInternal(e, "An unexpected error occurred: " + e.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, w);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHibernateException(Exception e, WebRequest w){
        return handleExceptionInternal(e,"A constraint Violation occurred: " + e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, w);
    }

}
