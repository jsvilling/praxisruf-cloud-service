package ch.fhnw.ip5.praxiscloudservice.cloud.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final String TEMPLATE = "Message received: %s %n";

    @Override
    public void send(String message) {
        System.out.printf(TEMPLATE, message);
    }
}
