package ch.fhnw.ip5.praxiscloudservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "praxis-intercom.fcm")
@Getter
@Setter
public class FirebaseProperties {
    private String credentials;
}
