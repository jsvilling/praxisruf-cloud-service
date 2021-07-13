package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.MinimalClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@AllArgsConstructor
@Api(tags = "Client")
public class ClientsController {

    private final ConfigurationService configurationService;

    // ###### Admin CRUD Operations
    @GetMapping("/{id}")
    public ClientDto getClientById(@PathVariable("id") UUID clientId) {
        return configurationService.findClientById(clientId);
    }

    @GetMapping()
    public Set<ClientDto> getAllClients() {
        return configurationService.findAllClients();
    }

    @PostMapping
    public ClientDto createClient(@RequestBody ClientDto clientDto) {
        return configurationService.createClient(clientDto);
    }

    @PutMapping
    public ClientDto updateClient(@RequestBody ClientDto clientDto){
        return configurationService.updateClient(clientDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){configurationService.deleteClientById(id);}

    @DeleteMapping("/many/{filter}")
    public void deleteMany(@PathVariable List<UUID> filter){
        configurationService.deleteAllById(filter);
    }

    // ###### Client API

    /**
     * @param userId
     * @return Set of key value pairs where each entry consists of (clientName, clientId)
     */
    @GetMapping("/byUser")
    @Operation(description = "Find all available clients for a given user")
    public Set<MinimalClientDto> getAvailableClients(@RequestHeader("userId") UUID userId) {
        return configurationService.findAvailableClients(userId);
    }


    @GetMapping("{clientId}/configuration/notification-types")
    @Operation(description = "Find the active configuration for an existing client")
    public List<NotificationTypeDto> findNotificationTypesForClient(@PathVariable(value = "clientId") UUID clientId) {
        return configurationService.findNotificationTypesForClient(clientId);
    }

    @PostMapping("/configuration")
    @Operation(description = "Create a new client configuration")
    public void createClientConfiguration(@RequestBody ClientConfigurationDto configurationDto) {
        configurationService.createClientConfiguration(configurationDto);
    }

}
