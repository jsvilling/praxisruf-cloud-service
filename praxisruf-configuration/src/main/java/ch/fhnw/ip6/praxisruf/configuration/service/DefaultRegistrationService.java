package ch.fhnw.ip6.praxisruf.configuration.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.RegistrationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.configuration.api.RegistrationService;
import ch.fhnw.ip6.praxisruf.configuration.api.rulesengine.RulesEngine;
import ch.fhnw.ip6.praxisruf.configuration.domain.Client;
import ch.fhnw.ip6.praxisruf.configuration.domain.ClientConfiguration;
import ch.fhnw.ip6.praxisruf.configuration.domain.Registration;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientConfigurationRepository;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientRepository;
import ch.fhnw.ip6.praxisruf.configuration.persistence.RegistrationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class DefaultRegistrationService implements RegistrationService {

    private static final String UNKNOWN_CLIENT_NAME = "Unknown Client";

    private final RegistrationRepository registrationRepository;
    private final ClientConfigurationRepository clientConfigurationRepository;
    private final ClientRepository clientRepository;
    private final RulesEngine rulesEngine;


    public RegistrationDto register(UUID clientId, String fcmToken) {
        final Registration registration = Registration.builder()
                .clientId(clientId)
                .fcmToken(fcmToken)
                .build();
        validateRegistration(registration);
        registrationRepository.save(registration);
        log.info("Created or Updated Registration: {}", registration);
        return createRegistrationDto(registration);
    }

    public void unregister(UUID clientId) {
        try {
            registrationRepository.deleteById(clientId);
            log.info("Deleted Registration for clientId {}", clientId);
        } catch (IllegalArgumentException e) {
            log.info("Registration for clientId {} is already deleted", clientId);
        }
    }

    @Transactional(readOnly = true)
    public Set<String> getAllKnownTokens() {
        return registrationRepository.findAll()
                .stream()
                .map(Registration::getFcmToken)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<RegistrationDto> findAllRelevantRegistrations(SendPraxisNotificationDto notification) {
        return clientConfigurationRepository.findAll()
                .stream().filter(c -> rulesEngine.isAnyRelevant(c.getRules(), notification))
                .map(ClientConfiguration::getClient)
                .map(Client::getId)
                .map(registrationRepository::findByClientId)
                .flatMap(Optional::stream)
                .map(this::createRegistrationDto)
                .collect(Collectors.toSet());
    }

    private void validateRegistration(Registration registration) {
        if (registration.getClientId() == null || registration.getFcmToken() == null) {
            log.error("Invalid Registration Data: {}", registration);
            throw new PraxisIntercomException(ErrorCode.INVALID_REGISTRATION_INFORMATION);
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
