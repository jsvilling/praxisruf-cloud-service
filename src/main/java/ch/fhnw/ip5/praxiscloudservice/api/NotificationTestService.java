package ch.fhnw.ip5.praxiscloudservice.api;

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
