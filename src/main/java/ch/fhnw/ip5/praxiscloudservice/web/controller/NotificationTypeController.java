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
    public NotificationTypeDto findById(@PathVariable("id") UUID notificationTypeId){
        return notificationTypeService.findById(notificationTypeId);
    }

    @GetMapping()
    public Set<NotificationTypeDto> findAll(){
        return notificationTypeService.findAll();
    }

    @GetMapping("/search")
    @Operation(description = "Find the active configuration for an existing client")
    public Set<NotificationTypeDto> findNotificationTypesForClient(@RequestParam(value = "clientId") UUID clientId) {
        return notificationTypeService.findNotificationTypesForClient(clientId);
    }

    @PostMapping()
    @Operation(description = "Create a new client configuration")
    public NotificationTypeDto create(@RequestBody NotificationTypeDto notificationTypeDto) {
        return notificationTypeService.create(notificationTypeDto);
    }

    @PutMapping()
    @Operation(description = "Update an existing client configuration")
    public NotificationTypeDto update(@RequestBody NotificationTypeDto notificationTypeDto) {
        return notificationTypeService.update(notificationTypeDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID notificationTypeId){
        notificationTypeService.deleteById(notificationTypeId);
    }

    @DeleteMapping("/many/{filter}")
    public void deleteMany(@PathVariable List<UUID> filter) {
        notificationTypeService.deleteManyById(filter);
    }
    
}
