package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.api.NotificationService;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.NotificationType;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.persistence.NotificationTypeRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
public class FirebaseNotificationService implements NotificationService {

    private static final String KEY = "Test Key";
    private static final String DATA = "Test Data";

    private final Logger log = LoggerFactory.getLogger(FirebaseNotificationService.class);

    private final WebClient webClient;
    private final NotificationTypeRepository notificationTypeRepository;

    public FirebaseNotificationService(WebClient.Builder webClientBuilder, NotificationTypeRepository notificationTypeRepository) {
        this.webClient = webClientBuilder.baseUrl("http://example.org").build();
        this.notificationTypeRepository = notificationTypeRepository;
    }

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

        Arrays.stream(getAllRelevantFcmTokens(notification))
                .map(n -> {
                    return Notification.builder()
                            .setTitle(notificationType.getTitle())
                            .setBody(notificationType.getBody())
                            .build();
                }).map(n -> {
                    return Message.builder()
                            .setToken("")
                            .setNotification(firebaseNotification)
                            .build();
        }).forEach(m -> {
            // TODO: Do not break if only one is invalid
            final String response = send(m);
            log.info("Success: Message {} was sent.", response);
        });
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
    public void send(String token) {

        final Notification notification = Notification.builder()
                .setTitle("Notification Title")
                .setBody("This notification was sent from the cloud service using Firebase Messaging")
                .build();

        final Message firebaseMessage = Message.builder()
                .putData(KEY, DATA)
                .setToken(token)
                .setNotification(notification)
                .build();

        final String response = send(firebaseMessage);

        log.info("Success: Message {} was sent.", response);
    }

    private String send(Message firebaseMessage) {
        try {
            return FirebaseMessaging.getInstance().send(firebaseMessage);
        } catch (FirebaseMessagingException e) {
            throw new PraxisIntercomException(ErrorCode.FCM_ERROR);
        }
    }

    /**
     * Finds all registered clients and sends a test notification to each client.
     *
     * Sending the notification is done via FirebaseNotificationService#send.
     *
     * @throws Exception
     */
    @Override
    public void sendAll() {
        for (String fcmToken : getAllKnownFcmTokens()) {
            send(fcmToken);
        }
    }

    /**
     * Makes a call to the configuration endpoint to find all registered fcm tokens.
     * @return String[] - All registered tokens.
     */
    public String[] getAllKnownFcmTokens() {
        return this.webClient.get()
                .uri("/configuration/registration/tokens")
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }

    public String[] getAllRelevantFcmTokens(PraxisNotification notification) {
        return this.webClient.get()
                .uri("/configuration/registration/tokens")
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }
}
