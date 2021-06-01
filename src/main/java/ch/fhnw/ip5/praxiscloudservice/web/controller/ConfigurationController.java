package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
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

    @PostMapping("/registration/relevant-tokens/")
    public Set<String> getAllRelevantTokens(@RequestBody PraxisNotification notification) {
        return configurationService.findAllRelevantTokens(notification);
    }

    @GetMapping("/registration/tokens")
    public Set<String> getAllKnownTokens() {
        return configurationService.getAllKnownTokens();
    }

    /**
     * @param userId - TODO: Move to http header.
     * @return Set of key value pairs where each entry consists of (clientName, clientId)
     */
    @GetMapping("/client-configuration")
    public Set<Pair<String, UUID>> getAvailableClients(UUID userId) {
        return configurationService.findAvailableClientConfigurations(userId);
    }

    @PostMapping("/client-configuration")
    public void createClientConfiguration(UUID userId, UUID clientId, String name) {
        configurationService.createClientConfiguration(userId, clientId, name);
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
