package ch.fhnw.ip5.praxiscloudservice.persistence;

import ch.fhnw.ip5.praxiscloudservice.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, UUID> {

    List<Registration> findAll();

    Registration save(Registration registration);

}
