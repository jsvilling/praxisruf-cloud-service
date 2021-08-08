package ch.fhnw.ip5.praxiscloudservice.persistence.configuration;

import ch.fhnw.ip5.praxiscloudservice.domain.configuration.PraxisIntercomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<PraxisIntercomUser, UUID> {

    Optional<PraxisIntercomUser> findByUserName(String userName);
}
