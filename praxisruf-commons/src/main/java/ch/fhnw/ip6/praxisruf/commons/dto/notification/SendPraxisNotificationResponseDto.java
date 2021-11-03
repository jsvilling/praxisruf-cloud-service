package ch.fhnw.ip6.praxisruf.commons.dto.notification;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
public class SendPraxisNotificationResponseDto {

    private UUID notificationId;
    private boolean allSuccess;

}
