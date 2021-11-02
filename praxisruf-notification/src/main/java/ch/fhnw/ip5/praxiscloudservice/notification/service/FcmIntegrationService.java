package ch.fhnw.ip5.praxiscloudservice.notification.service;

import ch.fhnw.ip5.praxiscloudservice.commons.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.commons.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.notification.config.FirebaseProperties;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

/**
 * This Service initializes the connection to FireBaseMessaging.
 */
@Service
@Slf4j
@AllArgsConstructor
public class FcmIntegrationService {

    private final FirebaseProperties firebaseProperties;

    @PostConstruct
    public void connectToFirebase() throws Exception {
        final FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(getCredentialStream())
                .build();
        FirebaseApp.initializeApp(options);
        log.info("Firebase Connection initialized");
    }

    private GoogleCredentials getCredentialStream() throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(firebaseProperties.getCredentials());
        final InputStream streamo = new ByteArrayInputStream(decodedBytes);
        return GoogleCredentials.fromStream(streamo);
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
