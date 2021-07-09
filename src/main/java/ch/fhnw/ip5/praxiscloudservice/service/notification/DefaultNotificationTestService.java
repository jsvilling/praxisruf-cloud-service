package ch.fhnw.ip5.praxiscloudservice.service.notification;

import ch.fhnw.ip5.praxiscloudservice.api.NotificationTestService;
import ch.fhnw.ip5.praxiscloudservice.config.ProfileRegistry;
import ch.fhnw.ip5.praxiscloudservice.web.client.ConfigurationWebClient;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Profile(ProfileRegistry.TEST)
public class DefaultNotificationTestService implements NotificationTestService {

    private static final String KEY = "Test Key";
    private static final String DATA = "Test Data";

    private final FcmIntegrationService fcmIntegrationService;
    private final ConfigurationWebClient configurationWebClient;


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
