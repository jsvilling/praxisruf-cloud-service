package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationResponseDto;

/**
 * This interface specifies contracts for sending messages to client devices.
 *
 * @author J. Villing
 */
public interface NotificationService {

    SendPraxisNotificationResponseDto send(SendPraxisNotificationDto notification);

}
