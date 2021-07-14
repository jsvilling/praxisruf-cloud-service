package ch.fhnw.ip5.praxiscloudservice.service.configuration;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.api.RulesEngine;
import ch.fhnw.ip5.praxiscloudservice.api.dto.*;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.*;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientConfigurationRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.NotificationTypeRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.RegistrationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode.INVALID_REGISTRATION_INFORMATION;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultConfigurationService implements ConfigurationService {

    private final RegistrationRepository registrationRepository;
    private final ClientRepository clientRepository;
    private final ClientConfigurationRepository clientConfigurationRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final RulesEngine rulesEngine;

    @Override
    public void register(UUID clientId, String fcmToken) {
        final Registration registration = Registration.builder()
                .clientId(clientId)
                .fcmToken(fcmToken)
                .build();
        validateRegistration(registration);
        registrationRepository.save(registration);
        log.info("Created or Updated Registration: {}", registration);
    }

    private void validateRegistration(Registration registration) {
        if (registration.getClientId() == null || registration.getFcmToken() == null) {
            log.error("Invalid Registration Data: {}", registration);
            throw new PraxisIntercomException(INVALID_REGISTRATION_INFORMATION);
        }
    }

    @Override
    public void unregister(UUID clientId) {
        try {
            registrationRepository.deleteById(clientId);
            log.info("Deleted Registration for clientId {}", clientId);
        } catch (IllegalArgumentException e) {
            log.info("Registration for clientId {} is already deleted", clientId);
        }

    }

    @Override
    public Set<String> getAllKnownTokens() {
        return registrationRepository.findAll()
                .stream()
                .map(Registration::getFcmToken)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> findAllRelevantTokens(PraxisNotification notification) {
        return clientConfigurationRepository.findAll()
                .stream().filter(c -> rulesEngine.isAnyRelevant(c.getRules(), notification))
                .map(ClientConfiguration::getClient)
                .map(Client::getClientId)
                .map(registrationRepository::findByClientId)
                .flatMap(Optional::stream)
                .map(Registration::getFcmToken)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<MinimalClientDto> findAvailableClients(UUID userId) {
        return clientRepository.findAllByUserId(userId)
                .stream()
                .map(c -> MinimalClientDto.builder().id(c.getClientId()).name(c.getName()).build())
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void createClientConfiguration(ClientConfigurationDto configurationDto) {
        final UUID clientId = configurationDto.getClientId();
        final Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND));

        final ClientConfiguration configuration = ClientConfiguration.builder()
                .name(configurationDto.getName())
                .client(client)
                .rules(toRuleParameters(configurationDto.getRuleParameters()))
                .notificationTypes(toNotificationTypes(configurationDto.getNotificationTypes()))
                .build();

        client.setClientConfiguration(configuration);
        clientRepository.saveAndFlush(client);
    }


    private Set<RuleParameters> toRuleParameters(List<RuleParametersDto> dtos) {
        return dtos.stream().map(dto ->
                RuleParameters.builder()
                        .type(dto.getRuleType())
                        .value(dto.getValue())
                        .build())
                .collect(Collectors.toSet());
    }

    private Set<NotificationType> toNotificationTypes(List<NotificationTypeDto> dtos) {
        return dtos.stream().map(dto ->
                NotificationType.builder()
                        .body(dto.getBody())
                        .type(dto.getType())
                        .title(dto.getTitle())
                        .displayText(dto.getDisplayText())
                        .build()
        ).collect(Collectors.toSet());
    }

    @Override
    public UUID createClient(UUID userId, String clientName) {
        final Client client = Client.builder()
                .userId(userId)
                .name(clientName)
                .build();
        return clientRepository.saveAndFlush(client).getClientId();
    }

    @Override
    public List<NotificationTypeDto> findNotificationTypesForClient(UUID clientId) {
        return clientRepository.findById(clientId)
                .map(Client::getClientConfiguration)
                .map(ClientConfiguration::getNotificationTypes)
                .map(this::toNotificationTypeDtos)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND));
    }

    @Override
    public Set<ClientDto> findAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(c -> ClientDto.builder().id(c.getClientId()).name(c.getName()).userId(c.getUserId()).build())
                .collect(Collectors.toSet());
    }

    private List<NotificationTypeDto> toNotificationTypeDtos(Collection<NotificationType> notificationTypes) {
        return notificationTypes.stream()
                .map(type -> NotificationTypeDto.builder()
                        .notificationTypeId(type.getId())
                        .body(type.getBody())
                        .type(type.getType())
                        .title(type.getTitle())
                        .displayText(type.getDisplayText())
                        .build()
                ).collect(Collectors.toList());
    }

}
