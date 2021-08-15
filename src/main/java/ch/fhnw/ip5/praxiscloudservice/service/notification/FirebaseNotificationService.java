package ch.fhnw.ip5.praxiscloudservice.service.notification;

import ch.fhnw.ip5.praxiscloudservice.api.dto.RegistrationDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationResponseDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.api.notification.NotificationService;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.NotificationType;
import ch.fhnw.ip5.praxiscloudservice.domain.notification.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.persistence.configuration.NotificationTypeRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.notification.NotificationRepository;
import ch.fhnw.ip5.praxiscloudservice.web.notification.client.ConfigurationWebClient;
import com.google.firebase.messaging.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * This service implements the NotificationService interface and is used to send Messages to client devices.
 * <p>
 * This implementation is specific to FirebaseCloudMessaging and uses the Firebase AdminAdmin API to send messages.
 * <p>
 * It is expected that the connection to fire Firebase Messaging Service has already initialized when this service
 * is used to send a message. This initialisation should happen with FCMInitializer.
 */
@Service
@Slf4j
@AllArgsConstructor
public class FirebaseNotificationService implements NotificationService {

    private static final String SENDER_NAME = "senderName";

    private final ConfigurationWebClient configurationWebClient;
    private final NotificationRepository notificationRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final FcmIntegrationService fcmIntegrationService;
    private final NotificationSendProcessService notificationSendProcessService;

    /**
     * Sends a Firebase Message for each client that has an applicable rule
     *
     * @param notificationDto
     * @return
     */
    @Override
    public SendPraxisNotificationResponseDto send(SendPraxisNotificationDto notificationDto) {
        final NotificationType notificationType = findExistingNotificationType(notificationDto.getNotificationTypeId());
        final PraxisNotification praxisNotification = createPraxisNotification(notificationDto);
        final Notification firebaseNotification = createFirebaseNotification(notificationType);
        final List<RegistrationDto> registrations = configurationWebClient.getAllRelevantRegistrations(praxisNotification);
        return send(registrations, firebaseNotification, praxisNotification);
    }

    @Override
    public SendPraxisNotificationResponseDto retry(UUID notificationId) {
        final PraxisNotification praxisNotification = findExistingNotification(notificationId);
        final NotificationType notificationType = findExistingNotificationType(praxisNotification.getNotificationTypeId());
        final Notification firebaseNotification = createFirebaseNotification(notificationType);
        final List<RegistrationDto> allRelevantFcmTokens = notificationSendProcessService.findAllFcmTokensForFailed(notificationId);
        return send(allRelevantFcmTokens, firebaseNotification, praxisNotification);
    }

    private NotificationType findExistingNotificationType(UUID notificationTypeId) {
        return notificationTypeRepository.findById(notificationTypeId)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.INVALID_NOTIFICATION_TYPE));
    }

    private PraxisNotification findExistingNotification(UUID notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }

    private PraxisNotification createPraxisNotification(SendPraxisNotificationDto notificationDto) {
        final PraxisNotification notification = PraxisNotification.builder()
                .notificationTypeId(notificationDto.getNotificationTypeId())
                .sender(notificationDto.getSender())
                .build();
        return notificationRepository.save(notification);
    }

    private Notification createFirebaseNotification(NotificationType notificationType) {
        return Notification.builder()
                .setTitle(notificationType.getTitle())
                .setBody(notificationType.getBody())
                .build();
    }

    private SendPraxisNotificationResponseDto send(List<RegistrationDto> registrations, Notification firebaseNotification, PraxisNotification praxisNotification) {
        boolean allSuccess = true;
        for (RegistrationDto registration : registrations) {
            final Message message = toFirebaseMessage(firebaseNotification, registration);
            final boolean success = send(message);
            notificationSendProcessService.createNotificationSendLogEntry(praxisNotification.getId(), success, registration);
            allSuccess = allSuccess && success;
        }
        return SendPraxisNotificationResponseDto.builder()
                .notificationId(praxisNotification.getId())
                .allSuccess(allSuccess)
                .build();
    }

    private boolean send(Message message) {
        try {
            fcmIntegrationService.send(message);
            return true;
        } catch (Exception e) {
            log.error("Sending message {} failed with exception {}", message, e);
            return false;
        }
    }

    private Message toFirebaseMessage(Notification firebaseNotification, RegistrationDto registration) {
        // for iOS
        Aps aps = Aps.builder()
                .setSound("default")
                .build();

        ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(aps)
                .build();

        // for Android
        AndroidNotification androidNofi = AndroidNotification.builder()
                .setSound("default")
                .build();

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(androidNofi)
                .build();

        return Message.builder()
                .setToken(registration.getFcmToken())
                .setNotification(firebaseNotification)
                .putData(SENDER_NAME, registration.getClientName())
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig)
                .build();
    }
}
