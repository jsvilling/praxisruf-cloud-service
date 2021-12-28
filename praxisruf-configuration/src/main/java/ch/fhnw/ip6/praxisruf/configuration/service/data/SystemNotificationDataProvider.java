package ch.fhnw.ip6.praxisruf.configuration.service.data;

import ch.fhnw.ip6.praxisruf.configuration.domain.NotificationType;
import ch.fhnw.ip6.praxisruf.configuration.persistence.NotificationTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SystemNotificationDataProvider {

    private static final UUID UNAVAILABLE_NOTIFICATION_ID = UUID.fromString("e6e6149c-7057-40a9-b937-0dbddb1fa879");
    private final NotificationTypeRepository notificationTypeRepository;

    @PostConstruct
    public void initSystemNotifications() {
        final Optional<NotificationType> existing = notificationTypeRepository.findById(UNAVAILABLE_NOTIFICATION_ID);
        if (existing.isEmpty()) {
            NotificationType notificationType = NotificationType.builder()
                    .id(UNAVAILABLE_NOTIFICATION_ID)
                    .displayText("")
                    .clientConfigurations(Collections.emptySet())
                    .title("Missed Call")
                    .body("Missed Call")
                    .textToSpeech(false)
                    .version(0L)
                    .build();
            notificationTypeRepository.saveAndFlush(notificationType);
        }
    }
}
