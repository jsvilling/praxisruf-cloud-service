package ch.fhnw.ip5.praxiscloudservice.cloud.notification;

import ch.fhnw.ip5.praxiscloudservice.cloud.web.NotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FirebaseNotificationService implements NotificationService {

    // TODO: Get actual client token or topic
    private static final String TOPIC = "test";
    private static final String KEY = "Some Key";

    private final Logger log = LoggerFactory.getLogger(FirebaseNotificationService.class);

    @Override
    public void send(String message) throws Exception {
        final Message firebaseMessage = Message.builder()
                .putData(KEY, message)
                .setTopic(TOPIC)
                .build();

        FirebaseMessaging.getInstance().send(firebaseMessage);

        log.info("Success: Message was sent.");
    }
}
