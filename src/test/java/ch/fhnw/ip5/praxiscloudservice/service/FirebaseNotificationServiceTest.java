package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.NotificationType;
import ch.fhnw.ip5.praxiscloudservice.persistence.NotificationTypeRepository;
import ch.fhnw.ip5.praxiscloudservice.web.client.ConfigurationWebClient;
import com.google.firebase.messaging.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData.*;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirebaseNotificationServiceTest {

    @Mock
    private ConfigurationWebClient configurationWebClient;

    @Mock
    private NotificationTypeRepository notificationTypeRepository;

    @Mock
    private NotificationSendProcessService notificationSendProcessService;

    @Mock
    private FcmIntegrationService fcmIntegrationService;

    @InjectMocks
    private FirebaseNotificationService firebaseNotificationService;



    @Test
    void testSend_ZeroRecipients() {
        // Given
        final SendPraxisNotificationDto notification = createSendNotificationDto();
        final NotificationType notificationType = createNotificationType();
        when(notificationTypeRepository.findById(any())).thenReturn(Optional.of(notificationType));
        when(configurationWebClient.getAllRelevantFcmTokens(any())).thenReturn(new String[] {});

        // When
        firebaseNotificationService.send(notification);

        // Then
        verify(fcmIntegrationService, times(0)).send(any(Message.class));
        verify(notificationSendProcessService, times(0)).createNotificationSendLogEntry(any(), any(), any());
    }

    @Test
    void testSend_OneRecipient() {
        // Given
        final SendPraxisNotificationDto notification = createSendNotificationDto();
        final NotificationType notificationType = createNotificationType();
        when(notificationTypeRepository.findById(any())).thenReturn(Optional.of(notificationType));
        when(configurationWebClient.getAllRelevantFcmTokens(any())).thenReturn(new String[] {TOKEN});
        when(fcmIntegrationService.send(any(Message.class))).thenReturn(MESSAGE_ID);

        // When
        firebaseNotificationService.send(notification);

        // Then
        verify(fcmIntegrationService, times(1)).send(any(Message.class));
        verify(notificationSendProcessService, times(1)).createNotificationSendLogEntry(any(), any(), any());
    }

    @Test
    void testSend_NRecipients() {
        // Given
        final SendPraxisNotificationDto notification = createSendNotificationDto();
        final NotificationType notificationType = createNotificationType();
        when(notificationTypeRepository.findById(any())).thenReturn(Optional.of(notificationType));
        when(configurationWebClient.getAllRelevantFcmTokens(any())).thenReturn(new String[] {TOKEN, TOKEN, TOKEN});
        when(fcmIntegrationService.send(any(Message.class))).thenReturn(MESSAGE_ID);

        // When
        firebaseNotificationService.send(notification);

        // Then
        verify(fcmIntegrationService, times(3)).send(any(Message.class));
        verify(notificationSendProcessService, times(3)).createNotificationSendLogEntry(any(), any(), any());
    }

    @Test
    void testSend_InvalidNotificationType() {
        // Given
        final SendPraxisNotificationDto notification = createSendNotificationDto();
        when(notificationTypeRepository.findById(any())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> firebaseNotificationService.send(notification))
                .isInstanceOf(PraxisIntercomException.class);
    }

    @Test
    void testSend_SendError() {
        // Given
        final SendPraxisNotificationDto notification = createSendNotificationDto();
        final NotificationType notificationType = createNotificationType();
        when(notificationTypeRepository.findById(any())).thenReturn(Optional.of(notificationType));
        when(configurationWebClient.getAllRelevantFcmTokens(any())).thenReturn(new String[] {TOKEN});
        when(fcmIntegrationService.send(any(Message.class))).thenThrow(new RuntimeException());

        // When
        // Then
        assertThatNoException().isThrownBy(() -> firebaseNotificationService.send(notification));
    }

}
