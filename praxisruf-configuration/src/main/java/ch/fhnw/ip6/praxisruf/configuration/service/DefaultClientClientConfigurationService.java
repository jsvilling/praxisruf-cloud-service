package ch.fhnw.ip6.praxisruf.configuration.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientConfigurationDto;
import ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.configuration.api.ClientConfigurationService;
import ch.fhnw.ip6.praxisruf.configuration.domain.Client;
import ch.fhnw.ip6.praxisruf.configuration.domain.ClientConfiguration;
import ch.fhnw.ip6.praxisruf.configuration.domain.NotificationType;
import ch.fhnw.ip6.praxisruf.configuration.domain.RuleParameters;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientConfigurationRepository;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientRepository;
import ch.fhnw.ip6.praxisruf.configuration.persistence.NotificationTypeRepository;
import ch.fhnw.ip6.praxisruf.configuration.service.mapper.ClientConfigurationMapper;
import ch.fhnw.ip6.praxisruf.configuration.service.mapper.RulesParametersMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class DefaultClientClientConfigurationService implements ClientConfigurationService {

    private final ClientRepository clientRepository;
    private final ClientConfigurationRepository clientConfigurationRepository;
    private final NotificationTypeRepository notificationTypeRepository;

    @Override
    public ClientConfigurationDto create(ClientConfigurationDto configurationDto) {
        if (clientConfigurationRepository.existsByClientConfigurationId(configurationDto.getId())) {
            throw new PraxisIntercomException(ErrorCode.CLIENT_CONFIG_ALREADY_EXISTS);
        }
        return ClientConfigurationMapper.toClientConfigurationDto(createOrUpdate(configurationDto));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientConfigurationDto findById(UUID configurationId) {
        return ClientConfigurationMapper.toClientConfigurationDto(findExistingClientConfiguration(configurationId));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ClientConfigurationDto> findAll() {
        return clientConfigurationRepository.findAll().stream()
                .map(ClientConfigurationMapper::toClientConfigurationDto)
                .collect(Collectors.toSet());
    }

    @Override
    public ClientConfigurationDto update(ClientConfigurationDto configurationDto) {
        if (!clientConfigurationRepository.existsByClientConfigurationId(configurationDto.getId())) {
            throw new PraxisIntercomException(ErrorCode.CLIENT_CONFIG_NOT_FOUND);
        }
        return ClientConfigurationMapper.toClientConfigurationDto(createOrUpdate(configurationDto));
    }

    @Override
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
        final Set<RuleParameters> ruleParameters = RulesParametersMapper.toRuleParameters(configurationDto.getRuleParameters());
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
