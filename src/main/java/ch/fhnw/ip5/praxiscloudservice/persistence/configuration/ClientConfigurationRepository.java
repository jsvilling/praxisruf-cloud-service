package ch.fhnw.ip5.praxiscloudservice.persistence.configuration;

import ch.fhnw.ip5.praxiscloudservice.domain.configuration.Client;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.ClientConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientConfigurationRepository extends JpaRepository<ClientConfiguration, UUID> {

    boolean existsByClient(Client client);

    boolean existsByClientConfigurationId(UUID clientConfigurationId);

}
