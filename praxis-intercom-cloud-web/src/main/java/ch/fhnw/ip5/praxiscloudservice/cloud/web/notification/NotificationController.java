package ch.fhnw.ip5.praxiscloudservice.cloud.web.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for sending notifications.
 *
 * This endpoint can be used by client devices to send messages to other client devices. In the POC phase all messages
 * will be published to a fixed client.
 *
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    @CrossOrigin
    public void sendNotification(@RequestBody String token) throws Exception {
        notificationService.send(token);
    }
}
