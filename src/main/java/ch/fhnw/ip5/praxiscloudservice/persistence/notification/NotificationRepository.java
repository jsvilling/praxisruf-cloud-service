package ch.fhnw.ip5.praxiscloudservice.persistence.notification;

import ch.fhnw.ip5.praxiscloudservice.domain.notification.PraxisNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<PraxisNotification, UUID> {
}
