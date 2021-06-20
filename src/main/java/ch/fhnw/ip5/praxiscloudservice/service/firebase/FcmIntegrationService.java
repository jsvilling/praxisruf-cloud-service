package ch.fhnw.ip5.praxiscloudservice.service.firebase;

import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.config.FirebaseProperties;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * This Service initializes the connection to FireBaseMessaging.
 */
@Service
@Slf4j
public class FcmIntegrationService {

    private static final String FIREBASE_CONFIG_PATH = "firebase_config.json";
    private final FirebaseProperties fcmProperties;

    public FcmIntegrationService(FirebaseProperties fcmProperties) {
        this.fcmProperties = fcmProperties;
    }

    @PostConstruct
    public void connectToFirebase() throws Exception {
        final FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(getCredentialStream())
                .build();
        FirebaseApp.initializeApp(options);
        log.info("Firebase Connection initialized");
    }

    private GoogleCredentials getCredentialStream() throws Exception {
        return ServiceAccountCredentials.fromPkcs8(
                fcmProperties.getClientId(),
                fcmProperties.getClientEmail(),
                fcmProperties.getPrivateKey(),
                fcmProperties.getPrivateKeyId(),
                null
        );
    }

    public String send(Message firebaseMessage) {
        try {
            final String response = FirebaseMessaging.getInstance().send(firebaseMessage);
            log.info("Success: Message {} was sent.", response);
            return response;
        } catch (FirebaseMessagingException e) {
            log.error("Failed: Message was not sent.");
            throw new PraxisIntercomException(ErrorCode.FCM_ERROR);
        }
    }

}
