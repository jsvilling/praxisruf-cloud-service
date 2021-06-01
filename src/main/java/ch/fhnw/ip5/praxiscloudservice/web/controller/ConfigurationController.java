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

    @PostMapping("/registration/tokens/")
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
    @GetMapping("/clients/{userId}")
    public Set<Pair<String, UUID>> getAvailableClients(@PathVariable("userId") UUID userId) {
        return configurationService.findAvailableClients(userId);
    }

    @PostMapping("/clients")
    public void createClientConfiguration(@RequestParam(value="userId") UUID userId, @RequestParam(value="clientId") UUID clientId, @RequestParam(value="name") String name) {
        configurationService.createClientConfiguration(userId, clientId, name);
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
