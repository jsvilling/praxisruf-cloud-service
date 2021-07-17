package ch.fhnw.ip5.praxiscloudservice.service.configuration;

import ch.fhnw.ip5.praxiscloudservice.api.ClientConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.Client;
import ch.fhnw.ip5.praxiscloudservice.domain.ClientConfiguration;
import ch.fhnw.ip5.praxiscloudservice.domain.NotificationType;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientConfigurationRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientRepository;
import ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper.ClientConfigurationMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper.ClientConfigurationMapper.toClientConfigurationDto;
import static ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper.NotificationTypesMapper.toNotificationTypes;
import static ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper.RulesParametersMapper.toRuleParameters;

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
        final Client client = findExistingClient(clientId);

        final ClientConfiguration configuration = ClientConfiguration.builder()
                .name(configurationDto.getName())
                .client(client)
                .rules(toRuleParameters(configurationDto.getRuleParameters()))
                .notificationTypes(toNotificationTypes(configurationDto.getNotificationTypes()))
                .build();

        return toClientConfigurationDto(clientConfigurationRepository.saveAndFlush(configuration));
    }

    @Override
    public ClientConfigurationDto findClientConfigurationById(UUID configurationId) {
        return toClientConfigurationDto(findExistingClientConfiguration(configurationId));
    }

    @Override
    public Set<ClientConfigurationDto> findAllClientConfigurations() {
        return clientConfigurationRepository.findAll().stream()
                .map(ClientConfigurationMapper::toClientConfigurationDto)
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
