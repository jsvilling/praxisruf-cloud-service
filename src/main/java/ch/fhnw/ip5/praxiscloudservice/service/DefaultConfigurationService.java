package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.domain.Registration;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.RegistrationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DefaultConfigurationService implements ConfigurationService {

    private final RegistrationRepository registrationRepository;
    private final ClientRepository clientRepository;

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
    public Set<Pair<String, UUID>> findAvailableClientConfigurations(UUID userId) {
        return clientRepository.findAllByUserId(userId)
                .stream()
                .map(c -> Pair.of(c.getName(), c.getClientId()))
                .collect(Collectors.toSet());
    }
}
