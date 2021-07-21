package ch.fhnw.ip5.praxiscloudservice.service.configuration;

import ch.fhnw.ip5.praxiscloudservice.api.RegistrationService;
import ch.fhnw.ip5.praxiscloudservice.api.RulesEngine;
import ch.fhnw.ip5.praxiscloudservice.api.dto.RegistrationDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.Client;
import ch.fhnw.ip5.praxiscloudservice.domain.ClientConfiguration;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.domain.Registration;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientConfigurationRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.RegistrationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode.INVALID_REGISTRATION_INFORMATION;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultRegistrationService implements RegistrationService {

    private static final String UNKNOWN_CLIENT_NAME = "Unknown Client";

    private final RegistrationRepository registrationRepository;
    private final ClientConfigurationRepository clientConfigurationRepository;
    private final ClientRepository clientRepository;
    private final RulesEngine rulesEngine;


    public void register(UUID clientId, String fcmToken) {
        final Registration registration = Registration.builder()
                .clientId(clientId)
                .fcmToken(fcmToken)
                .build();
        validateRegistration(registration);
        registrationRepository.save(registration);
        log.info("Created or Updated Registration: {}", registration);
    }

    public void unregister(UUID clientId) {
        try {
            registrationRepository.deleteById(clientId);
            log.info("Deleted Registration for clientId {}", clientId);
        } catch (IllegalArgumentException e) {
            log.info("Registration for clientId {} is already deleted", clientId);
        }
    }

    public Set<String> getAllKnownTokens() {
        return registrationRepository.findAll()
                .stream()
                .map(Registration::getFcmToken)
                .collect(Collectors.toSet());
    }

    public Set<RegistrationDto> findAllRelevantRegistrations(PraxisNotification notification) {
        return clientConfigurationRepository.findAll()
                .stream().filter(c -> rulesEngine.isAnyRelevant(c.getRules(), notification))
                .map(ClientConfiguration::getClient)
                .map(Client::getClientId)
                .map(registrationRepository::findByClientId)
                .flatMap(Optional::stream)
                .map(this::createRegistrationDto)
                .collect(Collectors.toSet());
    }

    private void validateRegistration(Registration registration) {
        if (registration.getClientId() == null || registration.getFcmToken() == null) {
            log.error("Invalid Registration Data: {}", registration);
            throw new PraxisIntercomException(INVALID_REGISTRATION_INFORMATION);
        }
    }

    private RegistrationDto createRegistrationDto(Registration registration) {
        final String clientName = clientRepository.findById(registration.getClientId())
                .map(Client::getName)
                .orElse(UNKNOWN_CLIENT_NAME);
        return RegistrationDto.builder()
                .clientName(clientName)
                .fcmToken(registration.getFcmToken())
                .build();
    }

}
