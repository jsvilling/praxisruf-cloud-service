package ch.fhnw.ip5.praxiscloudservice.notification;

import ch.fhnw.ip5.praxiscloudservice.commons.dto.configuration.RegistrationDto;
import ch.fhnw.ip5.praxiscloudservice.notification.domain.NotificationSendProcess;
import ch.fhnw.ip5.praxiscloudservice.notification.persistence.NotificationSendProcessRepository;
import ch.fhnw.ip5.praxiscloudservice.notification.service.DefaultNotificationSendProcessService;
import ch.fhnw.ip5.praxiscloudservice.notification.util.DefaultTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultNotificationSendProcessServiceTest {

    @Mock
    private NotificationSendProcessRepository notificationSendProcessRepository;

    @InjectMocks
    private DefaultNotificationSendProcessService notificationSendProcessService;

    @Test
    void createNotificationSendLogEntry() {
        // Given
        final UUID notificationId = UUID.randomUUID();
        final boolean success = false;
        final RegistrationDto registration = DefaultTestData.createRegistrationDto();

        // When
        notificationSendProcessService.createNotificationSendLogEntry(notificationId, success, registration);

        // Then
        verify(notificationSendProcessRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void findAllFcmTokensForFailed() {
        // Given
        final UUID notificationId = UUID.randomUUID();
        final NotificationSendProcess process = DefaultTestData.createNotificationSendProcess();
        when(notificationSendProcessRepository.findAllByNotificationIdAndSuccess(notificationId, true)).thenReturn(List.of());
        when(notificationSendProcessRepository.findAllByNotificationIdAndSuccess(notificationId, false)).thenReturn(List.of(process));

        // When
        List<RegistrationDto> registrations = notificationSendProcessService.findAllRegistrationsForFailed(notificationId);

        // Then
        assertThat(registrations).hasSize(1);
        assertThat(registrations.get(0).getFcmToken()).isEqualTo(process.getRelevantToken());
    }


}
