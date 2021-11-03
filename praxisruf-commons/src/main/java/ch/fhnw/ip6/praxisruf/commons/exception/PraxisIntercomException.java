package ch.fhnw.ip6.praxisruf.commons.exception;

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
