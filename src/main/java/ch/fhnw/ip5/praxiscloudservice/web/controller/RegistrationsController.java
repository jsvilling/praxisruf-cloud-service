package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.RegistrationService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.RegistrationDto;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/registrations")
@AllArgsConstructor
@Api(tags = "Registration")
public class RegistrationsController {

    private final RegistrationService registrationService;

    @PostMapping("/tokens")
    public Set<RegistrationDto> findRelevantRegistrations(@RequestBody PraxisNotification notification) {
        return registrationService.findAllRelevantRegistrations(notification);
    }

    @PostMapping
    public void register(@RequestParam(value="clientId") UUID clientId, @RequestParam(value ="fcmToken")  String fcmToken) {
        registrationService.register(clientId, fcmToken);
    }

    @DeleteMapping
    public void unregister(UUID clientId) {
        registrationService.unregister(clientId);
    }

}
