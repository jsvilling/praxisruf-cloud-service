package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.NotificationService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationResponseDto;
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
@RequestMapping("/notifications")
@AllArgsConstructor
@Api(tags = "Notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    @Operation(description = "Send the given Notification to all relevant clients")
    public SendPraxisNotificationResponseDto sendNotification(@RequestBody SendPraxisNotificationDto notification) {
        return notificationService.send(notification);
    }

    @PostMapping("/retry")
    @Operation(description = "Retry all failed send for the given notification")
    public SendPraxisNotificationResponseDto retryNotification(@RequestParam(value="clientId") UUID notificationId) {
        return notificationService.retry(notificationId);
    }
}
