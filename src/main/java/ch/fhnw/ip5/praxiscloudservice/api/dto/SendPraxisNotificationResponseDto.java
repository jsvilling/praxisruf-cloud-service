package ch.fhnw.ip5.praxiscloudservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class SendPraxisNotificationResponseDto {

    private UUID notificationId;
    private boolean allSuccess;

}
