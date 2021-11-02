package ch.fhnw.ip5.praxiscloudservice.configuration.persistence;

import ch.fhnw.ip5.praxiscloudservice.configuration.domain.PraxisIntercomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<PraxisIntercomUser, UUID> {

    Optional<PraxisIntercomUser> findByUserName(String userName);
}
