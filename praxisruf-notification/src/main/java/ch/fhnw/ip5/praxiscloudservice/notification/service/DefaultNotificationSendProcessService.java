package ch.fhnw.ip5.praxiscloudservice.notification.service;

import ch.fhnw.ip5.praxiscloudservice.commons.dto.configuration.RegistrationDto;
import ch.fhnw.ip5.praxiscloudservice.notification.api.NotificationSendProcessService;
import ch.fhnw.ip5.praxiscloudservice.notification.domain.NotificationSendProcess;
import ch.fhnw.ip5.praxiscloudservice.notification.persistence.NotificationSendProcessRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class DefaultNotificationSendProcessService implements NotificationSendProcessService {

    private final NotificationSendProcessRepository notificationSendProcessRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
    public List<RegistrationDto> findAllRegistrationsForFailed(UUID notificationId) {
        final List<String> succeededTokens = findSucceededTokens(notificationId);
        return notificationSendProcessRepository.findAllByNotificationIdAndSuccess(notificationId, false)
                .stream()
                .filter(p -> !succeededTokens.contains(p.getRelevantToken()))
                .map(this::createRetryRegistration)
                .collect(Collectors.toList());
    }

    private List<String> findSucceededTokens(UUID notificationId) {
        return notificationSendProcessRepository.findAllByNotificationIdAndSuccess(notificationId, true)
                .stream()
                .map(NotificationSendProcess::getRelevantToken)
                .collect(Collectors.toList());
    }

    private RegistrationDto createRetryRegistration(NotificationSendProcess process) {
        return RegistrationDto.builder()
                .fcmToken(process.getRelevantToken())
                .clientName(process.getClientName())
                .build();
    }
}
