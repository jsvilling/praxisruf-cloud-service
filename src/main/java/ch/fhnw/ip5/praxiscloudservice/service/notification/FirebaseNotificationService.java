package ch.fhnw.ip5.praxiscloudservice.service.notification;

import ch.fhnw.ip5.praxiscloudservice.api.NotificationService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationResponseDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.NotificationType;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.persistence.NotificationRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.NotificationTypeRepository;
import ch.fhnw.ip5.praxiscloudservice.web.client.ConfigurationWebClient;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * This service implements the NotificationService interface and is used to send Messages to client devices.
 *
 * This implementation is specific to FirebaseCloudMessaging and uses the Firebase AdminAdmin API to send messages.
 *
 * It is expected that the connection to fire Firebase Messaging Service has already initialized when this service
 * is used to send a message. This initialisation should happen with FCMInitializer.
 */
@Service
@Slf4j
@AllArgsConstructor
public class FirebaseNotificationService implements NotificationService {

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
        final List<String> allRelevantFcmTokens = configurationWebClient.getAllRelevantFcmTokens(praxisNotification);
        return send(allRelevantFcmTokens, firebaseNotification, praxisNotification);
    }

    @Override
    public SendPraxisNotificationResponseDto retry(UUID notificationId) {
        final PraxisNotification praxisNotification = findExistingNotification(notificationId);
        final NotificationType notificationType = findExistingNotificationType(praxisNotification.getNotificationTypeId());
        final Notification firebaseNotification = createFirebaseNotification(notificationType);
        final List<String> allRelevantFcmTokens = notificationSendProcessService.findAllFcmTokensForFailed(notificationId);
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

    private SendPraxisNotificationResponseDto send(List<String> allRelevantFcmTokens, Notification firebaseNotification, PraxisNotification praxisNotification) {
        boolean allSuccess = true;
        for (String token : allRelevantFcmTokens) {
            final boolean success = send(firebaseNotification, token);
            notificationSendProcessService.createNotificationSendLogEntry(praxisNotification.getId(), success, token);
            allSuccess = allSuccess && success;
        }
        return new SendPraxisNotificationResponseDto(praxisNotification.getId(), allSuccess);
    }

    private boolean send(Notification firebaseNotification, String token) {
        try {
            Message message = toFirebaseMessage(firebaseNotification, token);
            fcmIntegrationService.send(message);
            return true;
        } catch (Exception e) {
            log.error("Sending message {} to {} failed with exception {}", firebaseNotification, token, e);
            return false;
        }
    }

    private Message toFirebaseMessage(Notification firebaseNotification, String token) {
        return Message.builder()
                .setToken(token)
                .setNotification(firebaseNotification)
                .build();
    }
}
