package ch.fhnw.ip5.praxiscloudservice.persistence;

import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<PraxisNotification, UUID> {
}
