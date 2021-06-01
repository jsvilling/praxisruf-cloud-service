package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;

/**
 * This interface specifies contracts for sending messages to client devices.
 *
 * @author J. Villing
 */
public interface NotificationService {

    void send(PraxisNotification notification);

    void send(String token);

    void sendAll();

}
