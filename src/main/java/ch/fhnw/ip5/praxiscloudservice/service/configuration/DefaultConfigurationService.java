package ch.fhnw.ip5.praxiscloudservice.service.configuration;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.api.RulesEngine;
import ch.fhnw.ip5.praxiscloudservice.api.dto.*;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.*;
import ch.fhnw.ip5.praxiscloudservice.persistence.*;
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
    private final RuleParametersRepository ruleParametersRepository;

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
    public ClientConfigurationDto createClientConfiguration(ClientConfigurationDto configurationDto) {
        final UUID clientId = configurationDto.getClientId();
        Client client = clientRepository.findById(clientId)
                                              .orElseThrow(
                                                      () -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND));

        final ClientConfiguration configuration = ClientConfiguration.builder()
                                                                     .name(configurationDto.getName())
                                                                     .client(client)
                                                                     .rules(toRuleParameters(
                                                                             configurationDto.getRuleParameters()))
                                                                     .notificationTypes(toNotificationTypes(
                                                                             configurationDto.getNotificationTypes()))
                                                                     .build();

        client.setClientConfiguration(configuration);
        client = clientRepository.saveAndFlush(client);
        return toClientConfigurationDto(client.getClientConfiguration());
    }

    @Override
    public ClientDto createClient(ClientDto clientDto) {
        final Client client = Client.builder()
                                    .userId(clientDto.getUserId())
                                    .name(clientDto.getName())
                                    .build();
        return toClientDto(clientRepository.saveAndFlush(client));
    }

    @Override
    public List<NotificationTypeDto> findNotificationTypesForClient(UUID clientId) {
        return toNotificationTypeDtos(notificationTypeRepository.findAll());
    }

    @Override
    public Set<ClientDto> findAllClients() {
        return clientRepository.findAll()
                               .stream()
                               .map(c -> ClientDto.builder().id(c.getClientId()).name(c.getName()).userId(c.getUserId())
                                                  .build())
                               .collect(Collectors.toSet());
    }

    @Override
    public ClientDto findClientById(UUID clientId) {
        return toClientDto(clientRepository.findById(clientId)
                                           .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND)));
    }

    @Override
    public ClientDto updateClient(ClientDto clientDto) {
        Client client = clientRepository.findById(clientDto.getId())
                                        .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND));
        client = clientRepository.saveAndFlush(
                Client.builder()
                      .clientId(client.getClientId())
                      .name(clientDto.getName())
                      .userId(clientDto.getUserId())
                      .build());
        return toClientDto(client);
    }

    @Override
    public void deleteClientById(UUID id) {
        clientRepository.deleteById(id);
    }

    @Override
    public void deleteAllById(List<UUID> filter) {
        filter.forEach(this::deleteClientById);
    }

    @Override
    public ClientConfigurationDto findClientConfigurationById(UUID configurationId) {
        return toClientConfigurationDto(clientConfigurationRepository.findById(configurationId).orElseThrow(
                () -> new PraxisIntercomException(ErrorCode.CLIENT_CONFIG_NOT_FOUND)));
    }

    @Override
    public Set<ClientConfigurationDto> findAllClientConfigurations() {
        return clientConfigurationRepository.findAll().stream().map(this::toClientConfigurationDto)
                                            .collect(Collectors.toSet());
    }

    @Override
    public ClientConfigurationDto updateClientConfiguration(ClientConfigurationDto configurationDto) {
        ClientConfiguration configuration = clientConfigurationRepository
                .findById(configurationDto.getId())
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_CONFIG_NOT_FOUND));
        if (!configuration.getClient().getClientId().equals(configurationDto.getClientId())) {
            configuration.setClient(clientRepository.findById(configurationDto.getClientId()).orElseThrow(
                    () -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND)));
        }

        Set<RuleParameters> ruleParameters = configurationDto.getRuleParameters().stream().map(ruleParametersDto -> {
            if(ruleParametersDto.getId() == null){
                return RuleParameters.builder().type(ruleParametersDto.getRuleType()).value(ruleParametersDto.getValue()).build();
            } else {
                RuleParameters parameters = ruleParametersRepository.findById(ruleParametersDto.getId()).orElseThrow();
                parameters.setType(ruleParametersDto.getRuleType());
                parameters.setValue(ruleParametersDto.getValue());
                return parameters;
            }
        }).collect(Collectors.toSet());
        Set<NotificationType> notificationTypes = configurationDto.getNotificationTypes().stream().map(notificationTypeDto -> {
            if(notificationTypeDto.getId() == null){
                return NotificationType.builder().type(notificationTypeDto.getType()).body(notificationTypeDto.getBody()).displayText(notificationTypeDto.getDisplayText()).title(notificationTypeDto.getTitle()).build();
            } else {
                NotificationType notificationType = notificationTypeRepository.findById(notificationTypeDto.getId()).orElseThrow();
                notificationType.setType(notificationTypeDto.getType());
                notificationType.setBody(notificationTypeDto.getBody());
                notificationType.setDisplayText(notificationTypeDto.getDisplayText());
                notificationType.setTitle(notificationTypeDto.getTitle());
                return notificationType;
            }
        }).collect(Collectors.toSet());

        configuration.setName(configurationDto.getName());
        configuration.setRules(ruleParameters);
        configuration.setNotificationTypes(notificationTypes);

        return toClientConfigurationDto(clientConfigurationRepository.save(configuration));

    }

    @Override
    public void deleteClientConfigurationById(UUID configurationId) {

    }

    @Override
    public void deleteAllConfigurationsById(List<UUID> filter) {

    }

    private void validateRegistration(Registration registration) {
        if (registration.getClientId() == null || registration.getFcmToken() == null) {
            log.error("Invalid Registration Data: {}", registration);
            throw new PraxisIntercomException(INVALID_REGISTRATION_INFORMATION);
        }
    }

    private Set<RuleParameters> toRuleParameters(List<RuleParametersDto> dtos) {
        return dtos.stream().map(dto -> RuleParameters.builder()
                                                      .type(dto.getRuleType())
                                                      .value(dto.getValue())
                                                      .build())
                   .collect(Collectors.toSet());
    }

    private Set<NotificationType> toNotificationTypes(List<NotificationTypeDto> dtos) {
        return dtos.stream().map(dto -> NotificationType.builder()
                                                        .body(dto.getBody())
                                                        .type(dto.getType())
                                                        .title(dto.getTitle())
                                                        .displayText(dto.getDisplayText())
                                                        .build()
        ).collect(Collectors.toSet());
    }

    private List<NotificationTypeDto> toNotificationTypeDtos(Collection<NotificationType> notificationTypes) {
        return notificationTypes.stream()
                                .map(type -> NotificationTypeDto.builder()
                                                                .id(type.getId())
                                                                .body(type.getBody())
                                                                .type(type.getType())
                                                                .title(type.getTitle())
                                                                .displayText(type.getDisplayText())
                                                                .build()
                                ).collect(Collectors.toList());
    }

    private ClientDto toClientDto(Client client) {
        return ClientDto.builder()
                        .id(client.getClientId())
                        .userId(client.getUserId())
                        .name(client.getName())
                        .build();
    }

    private ClientConfigurationDto toClientConfigurationDto(ClientConfiguration configuration) {
        return ClientConfigurationDto.builder()
                                     .id(configuration.getClientConfigurationId())
                                     .clientId(configuration.getClient().getClientId())
                                     .name(configuration.getName())
                                     .notificationTypes(configuration.getNotificationTypes().stream()
                                                                     .map(this::toNotificationTypeDto).collect(
                                                     Collectors.toList()))
                                     .ruleParameters(configuration.getRules().stream().map(this::toRuleParameterDto)
                                                                  .collect(Collectors.toList()))
                                     .build();
    }

    private RuleParametersDto toRuleParameterDto(RuleParameters ruleParameters) {
        return RuleParametersDto.builder().id(ruleParameters.getRuleParametersId()).ruleType(ruleParameters.getType())
                                .value(ruleParameters.getValue())
                                .build();
    }

    private NotificationTypeDto toNotificationTypeDto(NotificationType notificationType) {
        return NotificationTypeDto.builder().id(notificationType.getId())
                                  .body(notificationType.getBody())
                                  .title(notificationType.getTitle())
                                  .type(notificationType.getTitle())
                                  .displayText(notificationType.getDisplayText())
                                  .build();
    }

}
