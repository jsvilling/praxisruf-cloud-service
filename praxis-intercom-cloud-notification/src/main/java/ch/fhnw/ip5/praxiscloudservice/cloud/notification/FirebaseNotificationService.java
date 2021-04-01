package ch.fhnw.ip5.praxiscloudservice.cloud.notification;

import ch.fhnw.ip5.praxiscloudservice.cloud.web.notification.NotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    // TODO: Get actual client token or topic
    private static final String KEY = "Test Key";
    private static final String DATA = "Test Data";

    private final Logger log = LoggerFactory.getLogger(FirebaseNotificationService.class);

    /**
     * Sends a Firebase Message with the given message String as data.
     *
     * In the POC phase all messages are sent to the pre-configured "test" topic.
     *
     * @param token
     * @throws Exception
     */
    @Override
    public void send(String token) throws Exception {


        final Notification notification = Notification.builder()
                .setTitle("Title")
                .setBody("Body with a text that can be a tiny bit longer. ")
                .build();

        final Message firebaseMessage = Message.builder()
                .putData(KEY, DATA)
                .setToken(token)
                .setNotification(notification)
                .build();

        final String response = FirebaseMessaging.getInstance().send(firebaseMessage);

        log.info("Success: Message {} was sent.", response);
    }
}
