package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@AllArgsConstructor
@Api(tags = "Client")
public class ClientsController {

    private final ConfigurationService configurationService;

    /**
     * @param userId - TODO: Move to auth http header.
     * @return Set of key value pairs where each entry consists of (clientName, clientId)
     */
    @GetMapping
    @Operation(description = "Find all available clients for a given user")
    public Set<Pair<String, UUID>> getAvailableClients(@RequestParam("userId") UUID userId) {
        return configurationService.findAvailableClients(userId);
    }

    @PostMapping("/configuration")
    @Operation(description = "Create a new client configuration")
    public void createClientConfiguration(
            @RequestParam(value="userId") UUID userId,
            @RequestParam(value="clientId") UUID clientId,
            @RequestParam(value="name") String name) {
        configurationService.createClientConfiguration(userId, clientId, name);
    }

}
