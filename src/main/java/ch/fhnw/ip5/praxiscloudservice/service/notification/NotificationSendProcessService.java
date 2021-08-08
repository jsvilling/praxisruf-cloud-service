package ch.fhnw.ip5.praxiscloudservice.service.notification;

import ch.fhnw.ip5.praxiscloudservice.api.dto.RegistrationDto;
import ch.fhnw.ip5.praxiscloudservice.domain.notification.NotificationSendProcess;
import ch.fhnw.ip5.praxiscloudservice.persistence.notification.NotificationSendProcessRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationSendProcessService {

    private final NotificationSendProcessRepository notificationSendProcessRepository;

    @Transactional(propagation = REQUIRES_NEW)
    public void createNotificationSendLogEntry(UUID notificationId, boolean success, RegistrationDto registration) {
        final NotificationSendProcess logEntry = NotificationSendProcess.builder()
                .notificationId(notificationId)
                .relevantToken(registration.getFcmToken())
                .clientName(registration.getClientName())
                .success(success)
                .build();
        notificationSendProcessRepository.saveAndFlush(logEntry);
    }

    @Transactional(readOnly = true)
    public List<RegistrationDto> findAllFcmTokensForFailed(UUID notificationId) {
        return notificationSendProcessRepository.findAllByNotificationIdAndSuccess(notificationId, false).stream()
                .map(this::createRetryRegistration)
                .collect(Collectors.toList());
    }

    private RegistrationDto createRetryRegistration(NotificationSendProcess process) {
        return RegistrationDto.builder()
                .fcmToken(process.getRelevantToken())
                .clientName(process.getClientName())
                .build();
    }
}
