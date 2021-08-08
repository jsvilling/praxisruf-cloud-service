package ch.fhnw.ip5.praxiscloudservice.api.notification;

/**
 * This interface specifies contracts for sending test messages to client devices.
 *
 * @author J. Villing
 */
public interface NotificationTestService {

    /**
     * Sends a test notification using the given token
     *
     * @param token
     */
    void sendTestNotification(String token);

    /**
     * Sends a test notification to all registered clients
     */
    void sendTestNotificationToAll();
}
