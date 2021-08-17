package ch.fhnw.ip5.praxiscloudservice.service.configuration;

import ch.fhnw.ip5.praxiscloudservice.api.configuration.ClientConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.Client;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.ClientConfiguration;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.NotificationType;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.persistence.configuration.ClientConfigurationRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.configuration.ClientRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.configuration.NotificationTypeRepository;
import ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper.ClientConfigurationMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper.ClientConfigurationMapper.toClientConfigurationDto;
import static ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper.RulesParametersMapper.toRuleParameters;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultClientClientConfigurationService implements ClientConfigurationService {

    private final ClientRepository clientRepository;
    private final ClientConfigurationRepository clientConfigurationRepository;
    private final NotificationTypeRepository notificationTypeRepository;

    @Override
    @Transactional
    public ClientConfigurationDto create(ClientConfigurationDto configurationDto) {
        if (clientConfigurationRepository.existsByClientConfigurationId(configurationDto.getId())) {
            throw new PraxisIntercomException(ErrorCode.CLIENT_CONFIG_ALREADY_EXISTS);
        }
        return toClientConfigurationDto(createOrUpdate(configurationDto));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientConfigurationDto findById(UUID configurationId) {
        return toClientConfigurationDto(findExistingClientConfiguration(configurationId));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ClientConfigurationDto> findAll() {
        return clientConfigurationRepository.findAll().stream()
                .map(ClientConfigurationMapper::toClientConfigurationDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public ClientConfigurationDto update(ClientConfigurationDto configurationDto) {
        if (!clientConfigurationRepository.existsByClientConfigurationId(configurationDto.getId())) {
            throw new PraxisIntercomException(ErrorCode.CLIENT_CONFIG_NOT_FOUND);
        }
        return toClientConfigurationDto(createOrUpdate(configurationDto));
    }

    @Override
    @Transactional
    public void deleteById(UUID configurationId) {
        try {
            ClientConfiguration clientConfiguration = clientConfigurationRepository.findById(configurationId).orElseThrow();
            removeClientConfigurationFromRelatedNotificationType(clientConfiguration);
            clientConfigurationRepository.save(clientConfiguration);
            Client client = clientRepository.findByClientConfiguration_ClientConfigurationId(configurationId).orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND));
            client.setClientConfiguration(null);
            clientRepository.save(client);
            clientConfigurationRepository.deleteById(configurationId);
        } catch (IllegalArgumentException e) {
            log.info("Client Configuration with id {} was already deleted", configurationId);
        }
    }

    @Override
    @Transactional
    public void deleteAllById(List<UUID> clientConfigurationIds) {
        clientConfigurationIds.forEach(this::deleteById);
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

    private ClientConfiguration createOrUpdate(ClientConfigurationDto configurationDto) {
        final Client client = findExistingClient(configurationDto.getClientId());
        final Set<RuleParameters> ruleParameters = toRuleParameters(configurationDto.getRuleParameters());
        final List<NotificationType> notificationTypes = notificationTypeRepository.findAllById(configurationDto.getNotificationTypes());

        final ClientConfiguration updatedClientConfiguration = ClientConfiguration.builder()
                .clientConfigurationId(configurationDto.getId())
                .name(configurationDto.getName())
                .client(client)
                .rules(ruleParameters)
                .notificationTypes(new HashSet<>(notificationTypes))
                .build();

        clientConfigurationRepository.save(updatedClientConfiguration);
        notificationTypes.forEach(nt -> nt.addClientConfiguration(updatedClientConfiguration));
        notificationTypeRepository.saveAll(notificationTypes);
        return clientConfigurationRepository.saveAndFlush(updatedClientConfiguration);
    }

    private void removeClientConfigurationFromRelatedNotificationType(ClientConfiguration configuration){
        configuration.getNotificationTypes().forEach(n -> n.removeClientConfiguration(configuration));
    }

}
