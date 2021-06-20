package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/notifications")
@AllArgsConstructor
@Api(tags = "Test")
public class TestController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    @Operation(description = "Send a test notification to the client with the given token")
    public void sendNotification(@RequestBody String token) {
        notificationService.send(token);
    }

    @PostMapping("/send/all")
    @Operation(description = "Send a test notification to all registered clients")
    public void sendNotification() {
        notificationService.sendAll();
    }
}
