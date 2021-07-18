package ch.fhnw.ip5.praxiscloudservice.persistence;

import ch.fhnw.ip5.praxiscloudservice.domain.Client;
import ch.fhnw.ip5.praxiscloudservice.domain.ClientConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientConfigurationRepository extends JpaRepository<ClientConfiguration, UUID> {

    boolean existsByClient(Client client);

    boolean existsByClientConfigurationId(UUID clientConfigurationId);

}
