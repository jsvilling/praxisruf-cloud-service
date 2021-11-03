package ch.fhnw.ip6.praxisruf.notification.service;

import ch.fhnw.ip6.praxisruf.commons.config.ProfileRegistry;
import ch.fhnw.ip6.praxisruf.notification.api.NotificationTestService;
import ch.fhnw.ip6.praxisruf.notification.web.client.ConfigurationWebClient;
import com.google.firebase.messaging.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Profile(ProfileRegistry.TEST)
public class DefaultNotificationTestService implements NotificationTestService {

    private static final String SENDER_KEY = "senderName";
    private static final String SENDER_VALUE = "Test Sender";

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

        final Notification notification = Notification.builder()
                .setTitle("Notification Title")
                .setBody("This notification was sent from the cloud service using Firebase Messaging")
                .build();

        final Message firebaseMessage = Message.builder()
                .putData(SENDER_KEY, SENDER_VALUE)
                .setToken(token)
                .setNotification(notification)
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig)
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
