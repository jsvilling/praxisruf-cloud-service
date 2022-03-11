package ch.fhnw.ip6.praxisruf.signaling.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "praxis-intercom.signaling")
@Getter
@Setter
public class SignalingProperties {
    private String notificationTypeForUnavailable;
}
