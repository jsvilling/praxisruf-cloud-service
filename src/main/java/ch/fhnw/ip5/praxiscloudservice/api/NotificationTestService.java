package ch.fhnw.ip5.praxiscloudservice.api;

public interface NotificationTestService {
    void sendTestNotification(String token);
    void sendTestNotificationToAll();
}
