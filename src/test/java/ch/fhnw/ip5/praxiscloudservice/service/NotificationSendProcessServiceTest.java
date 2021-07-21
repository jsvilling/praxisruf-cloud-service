package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.api.dto.RegistrationDto;
import ch.fhnw.ip5.praxiscloudservice.persistence.NotificationSendProcessRepository;
import ch.fhnw.ip5.praxiscloudservice.service.notification.NotificationSendProcessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData.createRegistrationDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

        // When
        notificationSendProcessService.findAllFcmTokensForFailed(notificationId);

        // Then
        verify(notificationSendProcessRepository, times(1)).findAllByNotificationIdAndSuccess(eq(notificationId), eq(false));
    }


}
