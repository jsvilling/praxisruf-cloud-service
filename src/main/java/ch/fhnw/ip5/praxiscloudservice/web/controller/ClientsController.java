package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;
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
     * @param userId
     * @return Set of key value pairs where each entry consists of (clientName, clientId)
     */
    @GetMapping
    @Operation(description = "Find all available clients for a given user")
    public Set<Pair<String, UUID>> getAvailableClients(@RequestHeader("userId") UUID userId) {
        return configurationService.findAvailableClients(userId);
    }

    @PostMapping
    public UUID createClient(@RequestHeader("userId") UUID userId, @RequestParam(value="clientName") String clientName) {
        return configurationService.createClient(userId, clientName);
    }

    @PostMapping("/configuration")
    @Operation(description = "Create a new client configuration")
    public void createClientConfiguration(@RequestBody ClientConfigurationDto configurationDto) {
        configurationService.createClientConfiguration(configurationDto);
    }

}
