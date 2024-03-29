package ch.fhnw.ip6.praxisruf.notification.web.controller;

import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationResponseDto;
import ch.fhnw.ip6.praxisruf.notification.api.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
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
@RequestMapping("/api/notifications")
@AllArgsConstructor
@Api(tags = "Notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(description = "Send the given Notification to all relevant clients")
    public SendPraxisNotificationResponseDto sendNotification(@RequestBody SendPraxisNotificationDto notification) {
        return notificationService.send(notification);
    }

    @PostMapping(path = "/targeted", params = "recipient")
    @Operation(description = "Send the given Notification to all relevant clients")
    public SendPraxisNotificationResponseDto sendNotification(@RequestParam(value = "recipient") UUID recipient, @RequestBody SendPraxisNotificationDto notification) {
        return notificationService.send(notification, recipient);
    }

    @PostMapping(params = "notificationId")
    @Operation(description = "Retry all failed send for the given notification")
    public SendPraxisNotificationResponseDto retryNotification(@RequestParam(value="notificationId") UUID notificationId) {
        return notificationService.retry(notificationId);
    }
}
