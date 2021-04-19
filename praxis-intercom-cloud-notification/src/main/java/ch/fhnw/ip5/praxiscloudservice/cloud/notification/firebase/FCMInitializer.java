package ch.fhnw.ip5.praxiscloudservice.cloud.notification.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * This Service initializes the connection to FireBaseMessaging.
 */
@Service
public class FCMInitializer {

    private final String fireBaseConfigPath = "classpath:firebase_config.json";

    private final Logger log = LoggerFactory.getLogger(FCMInitializer.class);

    @PostConstruct
    public void connectToFirebase() throws Exception {
        final FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(getCredentialStream())
                .build();
        FirebaseApp.initializeApp(options);
        log.info("Firebase Connection initialized");
    }


    private GoogleCredentials getCredentialStream() throws Exception {
        final InputStream credentialsInputStream = new FileInputStream(ResourceUtils.getFile(fireBaseConfigPath));
        return GoogleCredentials.fromStream(credentialsInputStream);
    }

}
