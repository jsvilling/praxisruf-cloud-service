package ch.fhnw.ip6.praxisruf.configuration.web.controller;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientConfigurationDto;
import ch.fhnw.ip6.praxisruf.configuration.api.ClientConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/configurations")
@AllArgsConstructor
@Api(tags = "Client Configuration")
public class ClientConfigurationController {

    private final ClientConfigurationService clientConfigurationService;

    @GetMapping("/{id}")
    public ClientConfigurationDto getConfigurationById(@PathVariable("id") UUID configurationId){
        return clientConfigurationService.findById(configurationId);
    }

    @GetMapping(params = {"minimal", "clientId"})
    public Set<ClientConfigurationDto> findByClientId(@RequestParam("clientId") UUID clientId) {
        return clientConfigurationService.findByClientId(clientId);
    }

    @GetMapping
    public Set<ClientConfigurationDto> getAllConfigurations(){
        return clientConfigurationService.findAll();
    }

    @PostMapping
    @Operation(description = "Create a new client configuration")
    public ClientConfigurationDto createClientConfiguration(@RequestBody ClientConfigurationDto configurationDto) {
        return clientConfigurationService.create(configurationDto);
    }

    @PutMapping
    @Operation(description = "Update an existing client configuration")
    public ClientConfigurationDto updateClientConfiguration(@RequestBody ClientConfigurationDto configurationDto) {
        return clientConfigurationService.update(configurationDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID configurationId){
        clientConfigurationService.deleteById(configurationId);
    }

    @DeleteMapping("/many/{filter}")
    public void deleteMany(@PathVariable List<UUID> filter) {
        clientConfigurationService.deleteAllById(filter);
    }

}
