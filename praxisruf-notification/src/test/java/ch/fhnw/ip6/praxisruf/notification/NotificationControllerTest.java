package ch.fhnw.ip6.praxisruf.notification;

import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.notification.service.FirebaseNotificationService;
import ch.fhnw.ip6.praxisruf.notification.util.DefaultTestData;
import ch.fhnw.ip6.praxisruf.notification.web.controller.NotificationController;
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
    void send_targeted() {
        // Given
        final UUID id = UUID.randomUUID();
        final SendPraxisNotificationDto sendNotificationDto = DefaultTestData.createSendNotificationDto();

        // When
        notificationController.sendNotification(id, sendNotificationDto);

        // Then
        Mockito.verify(firebaseNotificationService, Mockito.times(1)).send(sendNotificationDto, id);
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
