package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/registrations")
@AllArgsConstructor
@Api(tags = "Registration")
public class RegistrationsController {

    private final ConfigurationService configurationService;

    @GetMapping("/tokens")
    public Set<String> getAllKnownTokens() {
        return configurationService.getAllKnownTokens();
    }

    @PostMapping("/tokens/")
    public Set<String> findRelevantTokens(@RequestBody PraxisNotification notification) {
        return configurationService.findAllRelevantTokens(notification);
    }

    @PostMapping("/registration")
    public void register(@RequestParam(value="clientId") UUID clientId, @RequestParam(value ="fcmToken")  String fcmToken) {
        configurationService.register(clientId, fcmToken);
    }

    @DeleteMapping("/registration")
    public void unregister(UUID clientId) {
        configurationService.unregister(clientId);
    }

}
