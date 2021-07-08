package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.api.NotificationService;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.NotificationType;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.persistence.NotificationTypeRepository;
import ch.fhnw.ip5.praxiscloudservice.service.firebase.FcmIntegrationService;
import ch.fhnw.ip5.praxiscloudservice.web.client.ConfigurationWebClient;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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

    private static final String KEY = "Test Key";
    private static final String DATA = "Test Data";

    private final ConfigurationWebClient configurationWebClient;
    private final NotificationTypeRepository notificationTypeRepository;
    private final FcmIntegrationService fcmIntegrationService;

    /**
     * Sends a Firebase Message for each client that has an applicable rule
     *
     * @param notification
     */
    @Override
    public void send(PraxisNotification notification) {

        final NotificationType notificationType = notificationTypeRepository.findById(notification.getNotificationTypeId())
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.INVALID_NOTIFICATION_TYPE));

        final Notification firebaseNotification = Notification.builder()
                .setTitle(notificationType.getTitle())
                .setBody(notificationType.getBody())
                .build();

        Arrays.stream(configurationWebClient.getAllRelevantFcmTokens(notification))
                .map(n -> toFirebaseMessage(firebaseNotification, n))
                .forEach(fcmIntegrationService::send);
    }

    private Message toFirebaseMessage(Notification firebaseNotification, String token) {
        return Message.builder()
                .setToken(token)
                .setNotification(firebaseNotification)
                .build();
    }

    /**
     * Sends a Firebase Message with the given message String as data.
     *
     * In the POC phase all messages are sent to the pre-configured "test" topic.
     *
     * @param token - fcm token of the target client
     * package ch.fhnw.ip5.praxiscloudservice.cloud@throws Exception
     */
    @Override
    public void sendTestNotification(String token) {

        final Notification notification = Notification.builder()
                .setTitle("Notification Title")
                .setBody("This notification was sent from the cloud service using Firebase Messaging")
                .build();

        final Message firebaseMessage = Message.builder()
                .putData(KEY, DATA)
                .setToken(token)
                .setNotification(notification)
                .build();

        fcmIntegrationService.send(firebaseMessage);
    }


    /**
     * Finds all registered clients and sends a test notification to each client.
     *
     * Sending the notification is done via FirebaseNotificationService#send.
     *
     * @throws Exception
     */
    @Override
    public void sendTestNotificationToAll() {
        for (String fcmToken : configurationWebClient.getAllKnownFcmTokens()) {
            sendTestNotification(fcmToken);
        }
    }
}
