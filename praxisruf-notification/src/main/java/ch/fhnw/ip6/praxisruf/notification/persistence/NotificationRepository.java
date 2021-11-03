package ch.fhnw.ip6.praxisruf.notification.persistence;

import ch.fhnw.ip6.praxisruf.notification.domain.PraxisNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<PraxisNotification, UUID> {
}
