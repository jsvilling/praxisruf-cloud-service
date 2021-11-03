package ch.fhnw.ip6.praxisruf.notification;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.NotificationTypeDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.RegistrationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationResponseDto;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.notification.domain.PraxisNotification;
import ch.fhnw.ip6.praxisruf.notification.persistence.NotificationRepository;
import ch.fhnw.ip6.praxisruf.notification.service.DefaultNotificationSendProcessService;
import ch.fhnw.ip6.praxisruf.notification.service.FcmIntegrationService;
import ch.fhnw.ip6.praxisruf.notification.service.FirebaseNotificationService;
import ch.fhnw.ip6.praxisruf.notification.util.DefaultTestData;
import ch.fhnw.ip6.praxisruf.notification.web.client.ConfigurationWebClient;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirebaseNotificationServiceTest {

    @Mock
    private ConfigurationWebClient configurationWebClient;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private DefaultNotificationSendProcessService notificationSendProcessService;

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
            final SendPraxisNotificationDto notification = DefaultTestData.createSendNotificationDto();
            final NotificationTypeDto notificationType = DefaultTestData.createNotificationTypeDto();
            final ClientDto clientDto = DefaultTestData.createClientDto();
            when(configurationWebClient.findExistingClient(any())).thenReturn(clientDto);
            when(configurationWebClient.findExistingNotificationType(any())).thenReturn(notificationType);
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
            final SendPraxisNotificationDto notification = DefaultTestData.createSendNotificationDto();
            final NotificationTypeDto notificationType = DefaultTestData.createNotificationTypeDto();
            final ClientDto clientDto = DefaultTestData.createClientDto();
            when(configurationWebClient.findExistingClient(any())).thenReturn(clientDto);
            when(configurationWebClient.findExistingNotificationType(any())).thenReturn(notificationType);
            when(configurationWebClient.getAllRelevantRegistrations(any())).thenReturn(List.of(registration));
            when(fcmIntegrationService.send(any(Message.class))).thenReturn(DefaultTestData.MESSAGE_ID);
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
            final SendPraxisNotificationDto notification = DefaultTestData.createSendNotificationDto();
            final NotificationTypeDto notificationType = DefaultTestData.createNotificationTypeDto();
            final ClientDto clientDto = DefaultTestData.createClientDto();
            when(configurationWebClient.findExistingClient(any())).thenReturn(clientDto);
            when(configurationWebClient.findExistingNotificationType(any())).thenReturn(notificationType);
            when(configurationWebClient.getAllRelevantRegistrations(any())).thenReturn(List.of(registration, registration, registration));
            when(fcmIntegrationService.send(any(Message.class))).thenReturn(DefaultTestData.MESSAGE_ID);
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
            final SendPraxisNotificationDto notification = DefaultTestData.createSendNotificationDto();
            when(configurationWebClient.findExistingNotificationType(any())).thenReturn(null);

            // When
            // Then
            assertThatThrownBy(() -> firebaseNotificationService.send(notification))
                    .isInstanceOf(PraxisIntercomException.class);
        }

        @Test
        void testSend_SendError() {
            // Given
            final SendPraxisNotificationDto notification = DefaultTestData.createSendNotificationDto();
            final NotificationTypeDto notificationType = DefaultTestData.createNotificationTypeDto();
            final ClientDto clientDto = DefaultTestData.createClientDto();
            when(configurationWebClient.findExistingClient(any())).thenReturn(clientDto);
            when(configurationWebClient.findExistingNotificationType(any())).thenReturn(notificationType);
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
            final PraxisNotification notification = DefaultTestData.createNotification();
            final UUID notificationId = notification.getId();
            final NotificationTypeDto notificationType = DefaultTestData.createNotificationTypeDto();
            final ClientDto clientDto = DefaultTestData.createClientDto();
            when(configurationWebClient.findExistingClient(any())).thenReturn(clientDto);
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
            when(configurationWebClient.findExistingNotificationType(any())).thenReturn(notificationType);
            when(notificationSendProcessService.findAllRegistrationsForFailed(notificationId)).thenReturn(Collections.emptyList());

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
            final PraxisNotification notification = DefaultTestData.createNotification();
            final UUID notificationId = notification.getId();
            final NotificationTypeDto notificationType = DefaultTestData.createNotificationTypeDto();
            final ClientDto clientDto = DefaultTestData.createClientDto();
            when(configurationWebClient.findExistingClient(any())).thenReturn(clientDto);
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
            when(configurationWebClient.findExistingNotificationType(any())).thenReturn(notificationType);
            when(notificationSendProcessService.findAllRegistrationsForFailed(notificationId)).thenReturn(List.of(registration));

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
            final PraxisNotification notification = DefaultTestData.createNotification();
            final UUID notificationId = notification.getId();
            final NotificationTypeDto notificationType = DefaultTestData.createNotificationTypeDto();
            final ClientDto clientDto = DefaultTestData.createClientDto();
            when(configurationWebClient.findExistingClient(any())).thenReturn(clientDto);
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
            when(configurationWebClient.findExistingNotificationType(any())).thenReturn(notificationType);
            when(notificationSendProcessService.findAllRegistrationsForFailed(notificationId)).thenReturn(List.of(registration, registration, registration));

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
            final PraxisNotification notification = DefaultTestData.createNotification();
            when(notificationRepository.findById(any())).thenReturn(Optional.of(notification));

            // When
            // Then
            assertThatThrownBy(() -> firebaseNotificationService.retry(UUID.randomUUID()))
                    .isInstanceOf(PraxisIntercomException.class);
        }

        @Test
        void testSend_SendError() {
            // Given
            final PraxisNotification notification = DefaultTestData.createNotification();
            final UUID notificationId = notification.getId();
            final NotificationTypeDto notificationType = DefaultTestData.createNotificationTypeDto();
            final ClientDto clientDto = DefaultTestData.createClientDto();
            when(configurationWebClient.findExistingClient(any())).thenReturn(clientDto);
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
            when(configurationWebClient.findExistingNotificationType(any())).thenReturn(notificationType);
            when(notificationSendProcessService.findAllRegistrationsForFailed(notificationId)).thenReturn(List.of(registration));
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
