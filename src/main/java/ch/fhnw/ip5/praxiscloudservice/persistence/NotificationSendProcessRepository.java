package ch.fhnw.ip5.praxiscloudservice.persistence;

import ch.fhnw.ip5.praxiscloudservice.domain.NotificationSendProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationSendProcessRepository extends JpaRepository<NotificationSendProcess, UUID> {

    List<NotificationSendProcess> findAllByNotificationIdAndSuccess(UUID notificationId, boolean success);
}
