package ch.fhnw.ip5.praxiscloudservice.persistence.configuration;

import ch.fhnw.ip5.praxiscloudservice.domain.configuration.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;


public interface NotificationTypeRepository extends JpaRepository<NotificationType, UUID> {

    boolean existsById(UUID id);

    Set<NotificationType> findByClientConfigurations_Client_ClientId(UUID clientId);
}
