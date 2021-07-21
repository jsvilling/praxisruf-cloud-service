package ch.fhnw.ip5.praxiscloudservice.api.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class RegistrationDto {

    private String clientName;
    private String fcmToken;

}
