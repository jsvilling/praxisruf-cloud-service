package ch.fhnw.ip5.praxiscloudservice.commons.dto.notification;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * Represents a Notification that is being sent and received.
 */
@Getter
@Builder
public class SendPraxisNotificationDto {
    private UUID notificationTypeId;
    private UUID sender;
}
