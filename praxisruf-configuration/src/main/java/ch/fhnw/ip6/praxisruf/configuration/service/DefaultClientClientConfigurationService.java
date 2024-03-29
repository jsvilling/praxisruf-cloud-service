package ch.fhnw.ip6.praxisruf.configuration.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientConfigurationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.DisplayClientConfigurationDto;
import ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.configuration.api.ClientConfigurationService;
import ch.fhnw.ip6.praxisruf.configuration.domain.*;
import ch.fhnw.ip6.praxisruf.configuration.persistence.CallTypeRepository;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientConfigurationRepository;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientRepository;
import ch.fhnw.ip6.praxisruf.configuration.persistence.NotificationTypeRepository;
import ch.fhnw.ip6.praxisruf.configuration.service.mapper.ClientConfigurationMapper;
import ch.fhnw.ip6.praxisruf.configuration.service.mapper.RulesParametersMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static ch.fhnw.ip6.praxisruf.configuration.service.mapper.ClientConfigurationMapper.toClientConfigurationDto;
import static ch.fhnw.ip6.praxisruf.configuration.service.mapper.ClientConfigurationMapper.toClientConfigurationDtos;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class DefaultClientClientConfigurationService implements ClientConfigurationService {

    private final ClientRepository clientRepository;
    private final ClientConfigurationRepository clientConfigurationRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final CallTypeRepository callTypeRepository;

    @Override
    public ClientConfigurationDto create(ClientConfigurationDto configurationDto) {
        if (configurationDto.getId() != null && clientConfigurationRepository.existsById(configurationDto.getId())) {
            throw new PraxisIntercomException(ErrorCode.CLIENT_CONFIG_ALREADY_EXISTS);
        }
        return toClientConfigurationDto(createOrUpdate(configurationDto));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientConfigurationDto findById(UUID configurationId) {
        final ClientConfiguration clientConfiguration = findExistingClientConfiguration(configurationId);
        return toClientConfigurationDto(clientConfiguration);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ClientConfigurationDto> findAll() {
        final List<ClientConfiguration> configurations = clientConfigurationRepository.findAll();
        return toClientConfigurationDtos(configurations);
    }

    @Override
    public ClientConfigurationDto update(ClientConfigurationDto configurationDto) {
        if (!clientConfigurationRepository.existsById(configurationDto.getId())) {
            throw new PraxisIntercomException(ErrorCode.CLIENT_CONFIG_NOT_FOUND);
        }
        return toClientConfigurationDto(createOrUpdate(configurationDto));
    }

    @Override
    public void deleteById(UUID configurationId) {
        try {
            ClientConfiguration clientConfiguration = clientConfigurationRepository.findById(configurationId).orElseThrow();
            removeClientConfigurationFromRelatedNotificationType(clientConfiguration);
            clientConfigurationRepository.save(clientConfiguration);
            Client client = clientRepository.findByClientConfiguration_Id(configurationId).orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND));
            client.setClientConfiguration(null);
            clientRepository.save(client);
            clientConfigurationRepository.deleteById(configurationId);
        } catch (IllegalArgumentException e) {
            log.info("Client Configuration with id {} was already deleted", configurationId);
        }
    }

    @Override
    public void deleteAllById(Collection<UUID> clientConfigurationIds) {
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
        final List<NotificationType> notificationTypes = findNotificationTypes(configurationDto);
        final List<CallType> callTypes = findCallTypes(configurationDto);

        final ClientConfiguration updatedClientConfiguration = ClientConfiguration.builder()
                .id(configurationDto.getId())
                .name(configurationDto.getName())
                .client(client)
                .rules(ruleParameters)
                .notificationTypes(new HashSet<>(notificationTypes))
                .callTypes(new HashSet<>(callTypes))
                .build();

        clientConfigurationRepository.save(updatedClientConfiguration);
        notificationTypes.forEach(nt -> nt.addClientConfiguration(updatedClientConfiguration));
        notificationTypeRepository.saveAll(notificationTypes);
        return clientConfigurationRepository.saveAndFlush(updatedClientConfiguration);
    }

    private List<NotificationType> findNotificationTypes(ClientConfigurationDto clientConfigurationDto) {
        if (clientConfigurationDto.getNotificationTypes() == null) {
            return Collections.emptyList();
        }
        return notificationTypeRepository.findAllById(clientConfigurationDto.getNotificationTypes());
    }

    private List<CallType> findCallTypes(ClientConfigurationDto clientConfigurationDto) {
        if (clientConfigurationDto.getCallTypes() == null) {
            return Collections.emptyList();
        }
        return callTypeRepository.findAllById(clientConfigurationDto.getCallTypes());
    }


    private void removeClientConfigurationFromRelatedNotificationType(ClientConfiguration configuration){
        configuration.getNotificationTypes().forEach(n -> n.removeClientConfiguration(configuration));
    }

    @Override
    public DisplayClientConfigurationDto findByClientId(UUID clientId) {
        final ClientConfiguration configuration = clientConfigurationRepository.findByClient_Id(clientId)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_CONFIG_NOT_FOUND));
        return ClientConfigurationMapper.toDisplayClientConfigurationDto(configuration);
    }
}
