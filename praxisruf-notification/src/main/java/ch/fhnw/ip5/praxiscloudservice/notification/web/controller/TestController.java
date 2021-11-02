package ch.fhnw.ip5.praxiscloudservice.notification.web.controller;

import ch.fhnw.ip5.praxiscloudservice.commons.config.ProfileRegistry;
import ch.fhnw.ip5.praxiscloudservice.notification.api.NotificationTestService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/notifications")
@AllArgsConstructor
@Api(tags = "Test")
@Profile(ProfileRegistry.TEST)
public class TestController {

    private final NotificationTestService notificationTestService;

    @PostMapping("/send")
    @Operation(description = "Send a test notification to the client with the given token")
    public void sendNotification(@RequestBody String token) {
        notificationTestService.sendTestNotification(token);
    }

    @PostMapping("/send/all")
    @Operation(description = "Send a test notification to all registered clients")
    public void sendNotification() {
        notificationTestService.sendTestNotificationToAll();
    }
}
