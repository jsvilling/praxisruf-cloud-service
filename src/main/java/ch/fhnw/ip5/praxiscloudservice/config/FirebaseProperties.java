package ch.fhnw.ip5.praxiscloudservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "praxis-intercom.fcm.credentials")
@Getter
@Setter
public class FirebaseProperties {
    private String clientId;
    private String clientEmail;
    private String privateKey;
    private String privateKeyId;
    private String projectKey;
}
