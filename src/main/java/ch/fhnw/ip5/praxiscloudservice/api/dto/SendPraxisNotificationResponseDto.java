package ch.fhnw.ip5.praxiscloudservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class SendPraxisNotificationResponseDto {

    private UUID notificationId;
    private boolean allSuccess;

}
