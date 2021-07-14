package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;
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

    private final ConfigurationService configurationService;

    // ###### Admin CRUD Operations
    @GetMapping("/{id}")
    public ClientConfigurationDto getConfigurationById(@PathVariable("id") UUID configurationId){
        return configurationService.findClientConfigurationById(configurationId);
    }

    @GetMapping()
    public Set<ClientConfigurationDto> getAllConfigurations(){return configurationService.findAllClientConfigurations();}

    @PostMapping()
    @Operation(description = "Create a new client configuration")
    public ClientConfigurationDto createClientConfiguration(@RequestBody ClientConfigurationDto configurationDto) {
        return configurationService.createClientConfiguration(configurationDto);
    }

    @PutMapping()
    @Operation(description = "Update an existing client configuration")
    public ClientConfigurationDto updateClientConfiguration(@RequestBody ClientConfigurationDto configurationDto) {
        return configurationService.updateClientConfiguration(configurationDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID configurationId){
        configurationService.deleteClientConfigurationById(configurationId);
    }

    @DeleteMapping("/many/{filter}")
    public void deleteMany(@PathVariable List<UUID> filter)
    {
        configurationService.deleteAllConfigurationsById(filter);
    }


}
