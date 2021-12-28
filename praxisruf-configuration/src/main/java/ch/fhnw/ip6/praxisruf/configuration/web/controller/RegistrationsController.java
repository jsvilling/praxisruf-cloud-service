package ch.fhnw.ip6.praxisruf.configuration.web.controller;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.RegistrationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.configuration.api.RegistrationService;
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

    @GetMapping("/{id}")
    public RegistrationDto findRegistration(@PathVariable("id") UUID id) {
        return registrationService.findById(id);
    }

    @PostMapping("/tokens")
    public Set<RegistrationDto> findRelevantRegistrations(@RequestBody SendPraxisNotificationDto notification) {
        return registrationService.findAllRelevantRegistrations(notification);
    }

    @PostMapping
    public RegistrationDto register(@RequestParam(value="clientId") UUID clientId, @RequestParam(value ="fcmToken")  String fcmToken) {
        return registrationService.register(clientId, fcmToken);
    }

    @DeleteMapping("/{id}")
    public void unregister(@PathVariable("id") UUID clientId) {
        registrationService.unregister(clientId);
    }

}
