package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/configuration")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @PostMapping("/registration")
    public void register(UUID clientId, String fcmToken) {
        configurationService.register(clientId, fcmToken);
    }

    @GetMapping("/registration/tokens")
    public Set<String> getAllKnownTokens() {
        return configurationService.getAllKnownTokens();
    }

    @DeleteMapping("/registration")
    public void unregister(UUID clientId) {
        configurationService.unregister(clientId);
    }

}