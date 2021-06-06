package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@AllArgsConstructor
public class ClientsController {

    private final ConfigurationService configurationService;

    /**
     * @param userId - TODO: Move to auth http header.
     * @return Set of key value pairs where each entry consists of (clientName, clientId)
     */
    @GetMapping
    public Set<Pair<String, UUID>> getAvailableClients(@RequestParam("userId") UUID userId) {
        return configurationService.findAvailableClients(userId);
    }

    @PostMapping
    public void createClientConfiguration(@RequestParam(value="userId") UUID userId, @RequestParam(value="clientId") UUID clientId, @RequestParam(value="name") String name) {
        configurationService.createClientConfiguration(userId, clientId, name);
    }

}
