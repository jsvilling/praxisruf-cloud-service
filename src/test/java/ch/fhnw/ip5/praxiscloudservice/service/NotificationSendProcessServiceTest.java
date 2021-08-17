package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.api.dto.RegistrationDto;
import ch.fhnw.ip5.praxiscloudservice.domain.notification.NotificationSendProcess;
import ch.fhnw.ip5.praxiscloudservice.persistence.notification.NotificationSendProcessRepository;
import ch.fhnw.ip5.praxiscloudservice.service.notification.NotificationSendProcessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData.createNotificationSendProcess;
import static ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData.createRegistrationDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationSendProcessServiceTest {

    @Mock
    private NotificationSendProcessRepository notificationSendProcessRepository;

    @InjectMocks
    private NotificationSendProcessService notificationSendProcessService;

    @Test
    void createNotificationSendLogEntry() {
        // Given
        final UUID notificationId = UUID.randomUUID();
        final boolean success = false;
        final RegistrationDto registration = createRegistrationDto();

        // When
        notificationSendProcessService.createNotificationSendLogEntry(notificationId, success, registration);

        // Then
        verify(notificationSendProcessRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void findAllFcmTokensForFailed() {
        // Given
        final UUID notificationId = UUID.randomUUID();
        final NotificationSendProcess process = createNotificationSendProcess();
        when(notificationSendProcessRepository.findAllByNotificationIdAndSuccess(notificationId, false)).thenReturn(List.of(process));

        // When
        List<RegistrationDto> registrations = notificationSendProcessService.findAllRegistrationsForFailed(notificationId);

        // Then
        assertThat(registrations).hasSize(1);
        assertThat(registrations.get(0).getFcmToken()).isEqualTo(process.getRelevantToken());
    }


}
