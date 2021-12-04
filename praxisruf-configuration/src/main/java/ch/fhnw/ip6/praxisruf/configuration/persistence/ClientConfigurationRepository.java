package ch.fhnw.ip6.praxisruf.configuration.persistence;

import ch.fhnw.ip6.praxisruf.configuration.domain.Client;
import ch.fhnw.ip6.praxisruf.configuration.domain.ClientConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientConfigurationRepository extends JpaRepository<ClientConfiguration, UUID> {

    Optional<ClientConfiguration> findByClient_ClientId(UUID clientId);

    boolean existsByClient(Client client);

    boolean existsByClientConfigurationId(UUID clientConfigurationId);

}
