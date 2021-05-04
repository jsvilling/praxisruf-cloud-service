package ch.fhnw.ip5.praxiscloudservice.mock;

import ch.fhnw.ip5.praxiscloudservice.Registration;
import ch.fhnw.ip5.praxiscloudservice.RegistrationRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryRegistrationRepository implements RegistrationRepository {

    private final Set<Registration> registry = new HashSet<>();

    @Override
    public Set<Registration> getAll() {
        return registry;
    }

    @Override
    public Registration save(Registration registration) {
        registry.add(registration);
        return registration;
    }

}
