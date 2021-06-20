package ch.fhnw.ip5.praxiscloudservice.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ch.fhnw.ip5.praxiscloudservice.domain.NotificationType;

import java.util.UUID;


public interface NotificationTypeRepository extends JpaRepository<NotificationType, UUID> {
}
