package ch.fhnw.ip5.praxiscloudservice.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PraxisIntercomException extends RuntimeException {

    private final ErrorCode errorCode;

    public PraxisIntercomException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

}
