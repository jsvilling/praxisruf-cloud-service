package ch.fhnw.ip6.praxisruf.configuration.persistence;

import ch.fhnw.ip6.praxisruf.configuration.domain.PraxisIntercomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<PraxisIntercomUser, UUID> {

    Optional<PraxisIntercomUser> findByUserName(String userName);
}
