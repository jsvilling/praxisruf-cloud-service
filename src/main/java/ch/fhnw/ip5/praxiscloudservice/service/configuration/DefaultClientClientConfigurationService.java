package ch.fhnw.ip5.praxiscloudservice.service.configuration;

import ch.fhnw.ip5.praxiscloudservice.api.ClientConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.RuleParametersDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.Client;
import ch.fhnw.ip5.praxiscloudservice.domain.ClientConfiguration;
import ch.fhnw.ip5.praxiscloudservice.domain.NotificationType;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientConfigurationRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultClientClientConfigurationService implements ClientConfigurationService {

    private final ClientRepository clientRepository;
    private final ClientConfigurationRepository clientConfigurationRepository;

    @Override
    @Transactional
    public ClientConfigurationDto createClientConfiguration(ClientConfigurationDto configurationDto) {
        final UUID clientId = configurationDto.getClientId();
        Client client = findExistingClient(clientId);

        final ClientConfiguration configuration = ClientConfiguration.builder()
                .name(configurationDto.getName())
                .client(client)
                .rules(toRuleParameters(configurationDto.getRuleParameters()))
                .notificationTypes(toNotificationTypes(configurationDto.getNotificationTypes()))
                .build();

        client.setClientConfiguration(configuration);
        client = clientRepository.saveAndFlush(client);
        return toClientConfigurationDto(client.getClientConfiguration());
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
        final ClientConfiguration oldClientConfiguration = findExistingClientConfiguration(configurationDto.getId());
        final Set<RuleParameters> ruleParameters = toRuleParameters(configurationDto.getRuleParameters());
        final Set<NotificationType> notificationTypes = toNotificationTypes(configurationDto.getNotificationTypes());

        final ClientConfiguration updatedClientConfiguration = ClientConfiguration.builder()
                .clientConfigurationId(configurationDto.getId())
                .name(configurationDto.getName())
                .client(oldClientConfiguration.getClient())
                .rules(ruleParameters)
                .notificationTypes(notificationTypes)
                .build();

        return toClientConfigurationDto(clientConfigurationRepository.save(updatedClientConfiguration));
    }

    @Override
    public void deleteClientConfigurationById(UUID configurationId) {
        try {
            clientConfigurationRepository.deleteById(configurationId);
        } catch (IllegalArgumentException e) {
            log.info("Client Configuration with id {} was already deleted", configurationId);
        }
    }

    @Override
    public void deleteAllClientConfigurationsById(List<UUID> configurationIds) {
        configurationIds.forEach(this::deleteClientConfigurationById);
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

    private Client findExistingClient(UUID clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND));
    }

    private ClientConfiguration findExistingClientConfiguration(UUID clientId) {
        return clientConfigurationRepository
                .findById(clientId)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_CONFIG_NOT_FOUND));
    }

}
