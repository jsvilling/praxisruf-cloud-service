package ch.fhnw.ip5.praxiscloudservice.configuration.persistence;

import ch.fhnw.ip5.praxiscloudservice.configuration.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    Set<Client> findAllByUserId(UUID userId);

    Optional<Client> findByClientConfiguration_ClientConfigurationId(UUID configurationId);
}
