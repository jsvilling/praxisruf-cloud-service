package ch.fhnw.ip5.praxiscloudservice;

import ch.fhnw.ip5.praxiscloudservice.cloud.web.configuration.ConfigurationService;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DefaultConfigurationService implements ConfigurationService {

    private final RegistrationRepository registrationRepository;

    public DefaultConfigurationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @Override
    public void register(String clientId, String fcmToken) {
        final Registration registration = new Registration(clientId, fcmToken);
        registrationRepository.save(registration);
    }

    @Override
    public Set<String> getAllKnownTokens() {
        return registrationRepository.getAll()
                .stream()
                .map(Registration::getFcmToken)
                .collect(Collectors.toSet());
    }
}
