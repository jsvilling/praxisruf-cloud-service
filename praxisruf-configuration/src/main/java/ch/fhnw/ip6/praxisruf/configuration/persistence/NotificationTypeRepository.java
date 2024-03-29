package ch.fhnw.ip6.praxisruf.configuration.persistence;

import ch.fhnw.ip6.praxisruf.configuration.domain.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface NotificationTypeRepository extends JpaRepository<NotificationType, UUID> {

    boolean existsById(UUID id);

}
