package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/configuration")
@AllArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @GetMapping("/registration/tokens")
    public Set<String> getAllKnownTokens() {
        return configurationService.getAllKnownTokens();
    }

    /**
     *
     * @param userId
     * @return Set of key value pairs where each entry consists of (clientName, clientId)
     */
    @GetMapping("/clients")
    public Set<Pair<String, UUID>> getAvailableClients(UUID userId) {
        return configurationService.findAvailableClientConfigurations(userId);
    }

    @PostMapping("/registration")
    public void register(UUID clientId, String fcmToken) {
        configurationService.register(clientId, fcmToken);
    }

    @DeleteMapping("/registration")
    public void unregister(UUID clientId) {
        configurationService.unregister(clientId);
    }


}
