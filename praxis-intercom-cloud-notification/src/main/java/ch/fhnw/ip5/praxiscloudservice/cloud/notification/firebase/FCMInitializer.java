package ch.fhnw.ip5.praxiscloudservice.cloud.notification.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Service
public class FCMInitializer {

    private final String fireBaseConfigPath = "/home/joshua/FHNW/dev/IP5/IP5-praxis-cloud-service/praxis-intercom-cloud-notification/src/main/resources/ip5-praxis-intercom-firebase-adminsdk-6yqtg-e7785a21bd.json";

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
        final FileInputStream credentialsInputStream = new FileInputStream(fireBaseConfigPath);
        return GoogleCredentials.fromStream(credentialsInputStream);
    }

}
