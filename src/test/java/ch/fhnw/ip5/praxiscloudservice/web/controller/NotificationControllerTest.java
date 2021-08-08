package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationDto;
import ch.fhnw.ip5.praxiscloudservice.service.notification.FirebaseNotificationService;
import ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData;
import ch.fhnw.ip5.praxiscloudservice.web.notification.controller.NotificationController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {

    @Mock
    private FirebaseNotificationService firebaseNotificationService;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void send() {
        // Given
        final SendPraxisNotificationDto sendNotificationDto = DefaultTestData.createSendNotificationDto();

        // When
        notificationController.sendNotification(sendNotificationDto);

        // Then
        Mockito.verify(firebaseNotificationService, Mockito.times(1)).send(sendNotificationDto);
    }

    @Test
    void retry() {
        // Given
        final UUID notificationId = UUID.randomUUID();

        // When
        notificationController.retryNotification(notificationId);

        // Then
        Mockito.verify(firebaseNotificationService, Mockito.times(1)).retry(notificationId);
    }
}
