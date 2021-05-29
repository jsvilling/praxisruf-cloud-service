package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for sending notifications.
 *
 * This endpoint can be used by client devices to send messages to other client devices. In the POC phase all messages
 * will be published to a fixed client.
 *
 */
@RestController
@RequestMapping("/notifications")
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    @CrossOrigin
    public void sendNotification(@RequestBody String token) throws Exception {
        notificationService.send(token);
    }

    @PostMapping("/sendAll")
    public void sendNotification() throws Exception {
        notificationService.sendAll();
    }

}
