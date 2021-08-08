package ch.fhnw.ip5.praxiscloudservice.api.notification;

import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationResponseDto;
import ch.fhnw.ip5.praxiscloudservice.domain.notification.PraxisNotification;

import java.util.UUID;

/**
 * This interface specifies contracts for sending messages to client devices.
 *
 * @author J. Villing
 */
public interface NotificationService {

    /**
     * Sends the given {@link SendPraxisNotificationResponseDto} to all relevant recipients.
     *
     * @param notification
     * @return SendPraxisNotificationResponseDto
     */
    SendPraxisNotificationResponseDto send(SendPraxisNotificationDto notification);

    /**
     * Finds the {@link PraxisNotification} with the given notificationId and
     * retries all previously failed send processes.
     *
     * @param notificationId
     * @return SendPraxisNotificationResponseDto
     */
    SendPraxisNotificationResponseDto retry(UUID notificationId);

}
