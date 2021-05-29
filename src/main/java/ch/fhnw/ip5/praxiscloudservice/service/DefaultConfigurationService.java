package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.domain.Registration;
import ch.fhnw.ip5.praxiscloudservice.persistence.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DefaultConfigurationService implements ConfigurationService {

    private final RegistrationRepository registrationRepository;

    public DefaultConfigurationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

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
}
