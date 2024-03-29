package ch.fhnw.ip6.praxisruf.notification.api;

import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationResponseDto;

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
     * Sends the given {@link SendPraxisNotificationResponseDto} to the given recipient.
     * This can be used for targeted notifications when the target is not determined by the configuration
     * e.g. when notifying for a missed intercom signal.
     *
     * @param notification
     * @return SendPraxisNotificationResponseDto
     */
    SendPraxisNotificationResponseDto send(SendPraxisNotificationDto notification, UUID recipient);

    /**
     * Finds the {@link PraxisNotification} with the given notificationId and
     * retries all previously failed send processes.
     *
     * @param notificationId
     * @return SendPraxisNotificationResponseDto
     */
    SendPraxisNotificationResponseDto retry(UUID notificationId);

}
