package ch.fhnw.ip5.praxiscloudservice.cloud.web.configuration;

import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/configuration")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @PostMapping("/registration")
    public void register(String clientId, String fcmToken) {
        configurationService.register(clientId, fcmToken);
    }

    @GetMapping("/registration/tokens")
    public Set<String> getAllKnownTokens() {
        return configurationService.getAllKnownTokens();
    }

    @DeleteMapping("/registration")
    public void unregister(String clientId) {
        configurationService.unregister(clientId);
    }

}
