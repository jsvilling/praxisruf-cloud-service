package ch.fhnw.ip5.praxiscloudservice.cloud.notification;

import ch.fhnw.ip5.praxiscloudservice.cloud.web.notification.NotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

    public FirebaseNotificationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://example.org").build();
    }

    /**
     * Sends a Firebase Message with the given message String as data.
     *
     * In the POC phase all messages are sent to the pre-configured "test" topic.
     *
     * @param token - fcm token of the target client
     * @throws Exception
     */
    @Override
    public void send(String token) throws Exception {

        final Notification notification = Notification.builder()
                .setTitle("Notification Title")
                .setBody("This notification was sent from the cloud service using Firebase Messaging")
                .build();

        final Message firebaseMessage = Message.builder()
                .putData(KEY, DATA)
                .setToken(token)
                .setNotification(notification)
                .build();

        final String response = FirebaseMessaging.getInstance().send(firebaseMessage);

        log.info("Success: Message {} was sent.", response);
    }

    /**
     * Finds all registered clients and sends a test notification to each client.
     *
     * Sending the notification is done via FirebaseNotificationService#send.
     *
     * @throws Exception
     */
    @Override
    public void sendAll() throws Exception {
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
        // This makes using the reactive webclient rather pointless.
        // We should think about how we want to handle rest clients in general.
    }
}
