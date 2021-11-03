package ch.fhnw.ip6.praxisruf.notification.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.NotificationTypeDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.RegistrationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationResponseDto;
import ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.notification.api.NotificationSendProcessService;
import ch.fhnw.ip6.praxisruf.notification.api.NotificationService;
import ch.fhnw.ip6.praxisruf.notification.domain.PraxisNotification;
import ch.fhnw.ip6.praxisruf.notification.persistence.NotificationRepository;
import ch.fhnw.ip6.praxisruf.notification.web.client.ConfigurationWebClient;
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
    private static final String TEXT_TO_SPEECH_FLAG = "isTextToSpeech";
    private static final String VERSION = "version";
    private static final String NOTIFICATION_TYPE = "notificationType";

    private final ConfigurationWebClient configurationWebClient;
    private final NotificationRepository notificationRepository;
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
        final NotificationTypeDto notificationType = findExistingNotificationTypeDto(notificationDto.getNotificationTypeId());
        final PraxisNotification praxisNotification = createPraxisNotification(notificationDto);
        final Notification firebaseNotification = createFirebaseNotification(notificationType);
        final List<RegistrationDto> registrations = configurationWebClient.getAllRelevantRegistrations(praxisNotification);
        return send(registrations, firebaseNotification, praxisNotification);
    }

    @Override
    public SendPraxisNotificationResponseDto retry(UUID notificationId) {
        final PraxisNotification praxisNotification = findExistingNotification(notificationId);
        final NotificationTypeDto notificationType = findExistingNotificationTypeDto(praxisNotification.getNotificationTypeId());
        final Notification firebaseNotification = createFirebaseNotification(notificationType);
        final List<RegistrationDto> registrations = notificationSendProcessService.findAllRegistrationsForFailed(notificationId);
        return send(registrations, firebaseNotification, praxisNotification);
    }

    private PraxisNotification findExistingNotification(UUID notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }

    private NotificationTypeDto findExistingNotificationTypeDto(UUID notificationTypeId) {
        final NotificationTypeDto notificationType = configurationWebClient.findExistingNotificationType(notificationTypeId);
        if (notificationType == null) {
            throw new PraxisIntercomException(ErrorCode.NOTIFICATION_TYPE_NOT_FOUND);
        }
        return notificationType;
    }

    private PraxisNotification createPraxisNotification(SendPraxisNotificationDto notificationDto) {
        final PraxisNotification notification = PraxisNotification.builder()
                .notificationTypeId(notificationDto.getNotificationTypeId())
                .sender(notificationDto.getSender())
                .build();
        return notificationRepository.save(notification);
    }

    private Notification createFirebaseNotification(NotificationTypeDto notificationType) {
        return Notification.builder()
                .setTitle(notificationType.getTitle())
                .setBody(notificationType.getBody())
                .build();
    }

    private SendPraxisNotificationResponseDto send(List<RegistrationDto> registrations, Notification firebaseNotification, PraxisNotification praxisNotification) {
        boolean allSuccess = true;
        final String senderName = findSenderName(praxisNotification);
        for (RegistrationDto registration : registrations) {
            final Message message = toFirebaseMessage(firebaseNotification, registration, senderName);
            final boolean success = send(message);
            notificationSendProcessService.createNotificationSendLogEntry(praxisNotification.getId(), success, registration);
            allSuccess = allSuccess && success;
        }
        return SendPraxisNotificationResponseDto.builder()
                .notificationId(praxisNotification.getId())
                .allSuccess(allSuccess)
                .build();
    }

    private String findSenderName(PraxisNotification praxisNotification) {
        try {
            return configurationWebClient.findExistingClient(praxisNotification.getSender()).getName();
        } catch (Exception e) {
            throw new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND, e);
        }
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

    private Message toFirebaseMessage(Notification firebaseNotification, RegistrationDto registration, String senderName) {
        // for iOS
        Aps aps = Aps.builder()
                .setSound("default")
                .setContentAvailable(true)
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
                .putData(SENDER_NAME, senderName)
                .putData(TEXT_TO_SPEECH_FLAG, "true")
                .putData(VERSION, "0")
                .putData(NOTIFICATION_TYPE, UUID.randomUUID().toString())
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig)
                .build();
    }

}
