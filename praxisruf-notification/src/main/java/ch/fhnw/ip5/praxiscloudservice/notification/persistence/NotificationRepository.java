package ch.fhnw.ip5.praxiscloudservice.notification.persistence;

import ch.fhnw.ip5.praxiscloudservice.notification.domain.PraxisNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<PraxisNotification, UUID> {
}
