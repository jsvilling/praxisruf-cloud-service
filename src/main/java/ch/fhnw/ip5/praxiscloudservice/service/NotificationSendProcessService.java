package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.domain.NotificationSendProcess;
import ch.fhnw.ip5.praxiscloudservice.persistence.NotificationSendProcessRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationSendProcessService {

    private final NotificationSendProcessRepository notificationSendProcessRepository;

    @Transactional(propagation = REQUIRES_NEW)
    public void createNotificationSendLogEntry(UUID notificationId, boolean success, String relevantToken) {
        final NotificationSendProcess logEntry = NotificationSendProcess.builder()
                .notificationId(notificationId)
                .relevantToken(relevantToken)
                .success(success)
                .build();
        notificationSendProcessRepository.save(logEntry);
    }
}
