package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.api.dto.RegistrationDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationResponseDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.NotificationType;
import ch.fhnw.ip5.praxiscloudservice.domain.notification.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.persistence.configuration.NotificationTypeRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.notification.NotificationRepository;
import ch.fhnw.ip5.praxiscloudservice.service.notification.FcmIntegrationService;
import ch.fhnw.ip5.praxiscloudservice.service.notification.FirebaseNotificationService;
import ch.fhnw.ip5.praxiscloudservice.service.notification.NotificationSendProcessService;
import ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData;
import ch.fhnw.ip5.praxiscloudservice.web.notification.client.ConfigurationWebClient;
import com.google.firebase.messaging.Message;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirebaseNotificationServiceTest {

    @Mock
    private ConfigurationWebClient configurationWebClient;

    @Mock
    private NotificationTypeRepository notificationTypeRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationSendProcessService notificationSendProcessService;

    @Mock
    private FcmIntegrationService fcmIntegrationService;

    @InjectMocks
    private FirebaseNotificationService firebaseNotificationService;

    private final RegistrationDto registration = DefaultTestData.createRegistrationDto();
    
    @Nested
    class Send {
        @Test
        void testSend_ZeroRecipients() {
            // Given
            final SendPraxisNotificationDto notification = createSendNotificationDto();
            final NotificationType notificationType = createNotificationType();
            when(notificationTypeRepository.findById(any())).thenReturn(Optional.of(notificationType));
            when(configurationWebClient.getAllRelevantRegistrations(any())).thenReturn(Collections.emptyList());
            mockSaveNotificationWithGenerateId();

            // When
            firebaseNotificationService.send(notification);

            // Then
            verifyNoInteractions(fcmIntegrationService);
            verifyNoInteractions(notificationSendProcessService);
        }

        @Test
        void testSend_OneRecipient() {
            // Given
            final SendPraxisNotificationDto notification = createSendNotificationDto();
            final NotificationType notificationType = createNotificationType();
            when(notificationTypeRepository.findById(any())).thenReturn(Optional.of(notificationType));
            when(configurationWebClient.getAllRelevantRegistrations(any())).thenReturn(List.of(registration));
            when(fcmIntegrationService.send(any(Message.class))).thenReturn(MESSAGE_ID);
            mockSaveNotificationWithGenerateId();

            // When
            firebaseNotificationService.send(notification);

            // Then
            verify(fcmIntegrationService, times(1)).send(any(Message.class));
            verify(notificationSendProcessService, times(1)).createNotificationSendLogEntry(any(UUID.class), eq(true), eq(registration));
        }

        @Test
        void testSend_NRecipients() {
            // Given
            final SendPraxisNotificationDto notification = createSendNotificationDto();
            final NotificationType notificationType = createNotificationType();
            when(notificationTypeRepository.findById(any())).thenReturn(Optional.of(notificationType));
            when(configurationWebClient.getAllRelevantRegistrations(any())).thenReturn(List.of(registration, registration, registration));
            when(fcmIntegrationService.send(any(Message.class))).thenReturn(MESSAGE_ID);
            mockSaveNotificationWithGenerateId();

            // When
            firebaseNotificationService.send(notification);

            // Then
            verify(fcmIntegrationService, times(3)).send(any(Message.class));
            verify(notificationSendProcessService, times(3)).createNotificationSendLogEntry(any(UUID.class), eq(true), eq(registration));
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
            when(configurationWebClient.getAllRelevantRegistrations(any())).thenReturn(List.of(registration));
            when(fcmIntegrationService.send(any(Message.class))).thenThrow(new RuntimeException());
            mockSaveNotificationWithGenerateId();

            // When
            // Then
            assertThatNoException().isThrownBy(() -> firebaseNotificationService.send(notification));
            verify(fcmIntegrationService, times(1)).send(any(Message.class));
            verify(notificationSendProcessService, times(1)).createNotificationSendLogEntry(any(UUID.class), eq(false), eq(registration));

        }
    }

    @Nested
    class Retry {
        @Test
        void testRetry_ZeroRecipients() {
            // Given
            final PraxisNotification notification = createNotification();
            final UUID notificationId = notification.getId();
            final NotificationType notificationType = createNotificationType();
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
            when(notificationTypeRepository.findById(any())).thenReturn(Optional.of(notificationType));
            when(notificationSendProcessService.findAllFcmTokensForFailed(notificationId)).thenReturn(Collections.emptyList());

            // When
            final SendPraxisNotificationResponseDto response = firebaseNotificationService.retry(notificationId);

            // Then
            assertThat(response.getNotificationId()).isEqualTo(notificationId);
            assertThat(response.isAllSuccess()).isTrue();
            verifyNoInteractions(fcmIntegrationService);
            verifyNoMoreInteractions(notificationSendProcessService);
        }

        @Test
        void testSend_OneRecipient() {
            // Given
            final PraxisNotification notification = createNotification();
            final UUID notificationId = notification.getId();
            final NotificationType notificationType = createNotificationType();
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
            when(notificationTypeRepository.findById(any())).thenReturn(Optional.of(notificationType));
            when(notificationSendProcessService.findAllFcmTokensForFailed(notificationId)).thenReturn(List.of(registration));

            // When
            final SendPraxisNotificationResponseDto response = firebaseNotificationService.retry(notificationId);

            // Then
            assertThat(response.getNotificationId()).isEqualTo(notificationId);
            assertThat(response.isAllSuccess()).isTrue();
            verify(fcmIntegrationService, times(1)).send(any(Message.class));
            verify(notificationSendProcessService, times(1)).createNotificationSendLogEntry(any(), eq(true), any());
        }

        @Test
        void testSend_NRecipients() {
            // Given
            final PraxisNotification notification = createNotification();
            final UUID notificationId = notification.getId();
            final NotificationType notificationType = createNotificationType();
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
            when(notificationTypeRepository.findById(any())).thenReturn(Optional.of(notificationType));
            when(notificationSendProcessService.findAllFcmTokensForFailed(notificationId)).thenReturn(List.of(registration, registration, registration));

            // When
            final SendPraxisNotificationResponseDto response = firebaseNotificationService.retry(notificationId);

            // Then
            assertThat(response.getNotificationId()).isEqualTo(notificationId);
            assertThat(response.isAllSuccess()).isTrue();
            verify(fcmIntegrationService, times(3)).send(any(Message.class));
            verify(notificationSendProcessService, times(3)).createNotificationSendLogEntry(any(UUID.class), eq(true), eq(registration));
        }

        @Test
        void testSend_InvalidNotificationType() {
            // Given
            final PraxisNotification notification = createNotification();
            when(notificationRepository.findById(any())).thenReturn(Optional.of(notification));
            when(notificationTypeRepository.findById(any())).thenReturn(Optional.empty());

            // When
            // Then
            assertThatThrownBy(() -> firebaseNotificationService.retry(UUID.randomUUID()))
                    .isInstanceOf(PraxisIntercomException.class);
        }

        @Test
        void testSend_SendError() {
            // Given
            final PraxisNotification notification = createNotification();
            final UUID notificationId = notification.getId();
            final NotificationType notificationType = createNotificationType();
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
            when(notificationTypeRepository.findById(any())).thenReturn(Optional.of(notificationType));
            when(notificationSendProcessService.findAllFcmTokensForFailed(notificationId)).thenReturn(List.of(registration));
            when(fcmIntegrationService.send(any(Message.class))).thenThrow(new RuntimeException());

            // When
            // Then
            assertThatNoException().isThrownBy(() -> firebaseNotificationService.retry(notificationId));
            verify(fcmIntegrationService, times(1)).send(any(Message.class));
            verify(notificationSendProcessService, times(1)).createNotificationSendLogEntry(any(UUID.class), eq(false), eq(registration));
        }
    }

    private void mockSaveNotificationWithGenerateId() {
        when(notificationRepository.save(any())).thenAnswer(i -> {
            PraxisNotification n = i.getArgument(0);
            return PraxisNotification.builder()
                    .id(UUID.randomUUID())
                    .sender(n.getSender())
                    .notificationTypeId(n.getSender())
                    .build();
        });
    }

}
