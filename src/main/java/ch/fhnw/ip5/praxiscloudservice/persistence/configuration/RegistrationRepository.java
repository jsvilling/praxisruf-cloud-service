package ch.fhnw.ip5.praxiscloudservice.persistence.configuration;

import ch.fhnw.ip5.praxiscloudservice.domain.configuration.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, UUID> {

    List<Registration> findAll();

    Optional<Registration> findByClientId(UUID clientId);

    Registration save(Registration registration);

}
