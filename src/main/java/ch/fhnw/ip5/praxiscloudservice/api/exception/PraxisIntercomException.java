package ch.fhnw.ip5.praxiscloudservice.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PraxisIntercomException extends RuntimeException {

    private final ErrorCode errorCode;

}
