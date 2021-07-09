package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationDto;

/**
 * This interface specifies contracts for sending messages to client devices.
 *
 * @author J. Villing
 */
public interface NotificationService {

    void send(SendPraxisNotificationDto notification);

}
