package ch.fhnw.ip5.praxiscloudservice.persistence;

import ch.fhnw.ip5.praxiscloudservice.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Set<Client> findAllByUserId(UUID userId);

}
