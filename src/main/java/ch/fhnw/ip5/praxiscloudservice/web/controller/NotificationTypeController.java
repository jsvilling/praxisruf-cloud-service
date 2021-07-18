package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.NotificationTypeService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/notificationtypes")
@AllArgsConstructor
@Api(tags = "Notification Types")
public class NotificationTypeController {

    private final NotificationTypeService notificationTypeService;

    @GetMapping("/{id}")
    public NotificationTypeDto getConfigurationById(@PathVariable("id") UUID configurationId){
        return notificationTypeService.findById(configurationId);
    }

    @GetMapping()
    public Set<NotificationTypeDto> getAllConfigurations(){
        return notificationTypeService.findAll();
    }

    @GetMapping("/search")
    @Operation(description = "Find the active configuration for an existing client")
    public Set<NotificationTypeDto> findNotificationTypesForClient(@RequestParam(value = "clientId") UUID clientId) {
        return notificationTypeService.findNotificationTypesForClient(clientId);
    }

    @PostMapping()
    @Operation(description = "Create a new client configuration")
    public NotificationTypeDto createClientConfiguration(@RequestBody NotificationTypeDto configurationDto) {
        return notificationTypeService.create(configurationDto);
    }

    @PutMapping()
    @Operation(description = "Update an existing client configuration")
    public NotificationTypeDto updateClientConfiguration(@RequestBody NotificationTypeDto configurationDto) {
        return notificationTypeService.update(configurationDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID configurationId){
        notificationTypeService.deleteById(configurationId);
    }

    @DeleteMapping("/many/{filter}")
    public void deleteMany(@PathVariable List<UUID> filter) {
        notificationTypeService.deleteManyById(filter);
    }
    
}
