package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.NotificationService;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Api(tags = "Notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    @Operation(description = "Send the given Notification to all relevant clients")
    public void sendNotification(@RequestBody PraxisNotification notification) {
        notificationService.send(notification);
    }

    @PostMapping("/sendTest")
    @Operation(description = "Send a test notification to the client with the given token")
    public void sendNotification(@RequestBody String token) {
        notificationService.send(token);
    }

    @PostMapping("/sendAll")
    @Operation(description = "Send a test notification to all registered clients")
    public void sendNotification() {
        notificationService.sendAll();
    }

}
