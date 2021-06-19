package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.api.RulesEngine;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.RuleParametersDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.*;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientConfigurationRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.RegistrationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DefaultConfigurationService implements ConfigurationService {

    private final RegistrationRepository registrationRepository;
    private final ClientRepository clientRepository;
    private final ClientConfigurationRepository clientConfigurationRepository;
    private final RulesEngine rulesEngine;

    @Override
    public void register(UUID clientId, String fcmToken) {
        final Registration registration = new Registration(clientId, fcmToken);
        registrationRepository.save(registration);
    }

    @Override
    public void unregister(UUID clientId) {
        registrationRepository.deleteById(clientId);
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
                .map(Registration::getFcmToken)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Pair<String, UUID>> findAvailableClients(UUID userId) {
        return clientRepository.findAllByUserId(userId)
                .stream()
                .map(c -> Pair.of(c.getName(), c.getClientId()))
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

    @Override
    public UUID createClient(UUID userId, String clientName) {
        final Client client = Client.builder()
                .userId(userId)
                .name(clientName)
                .build();
        return clientRepository.saveAndFlush(client).getClientId();
    }

}
