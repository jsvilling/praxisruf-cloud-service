package ch.fhnw.ip6.praxisruf.signaling.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Allows access to configuration properties for signaling.
 *
 * The following properties are available
 *
 * - notificationTypeForUnavailable: Id of the notificationType that will be sent if delivery of a signal message is not possible
 *
 * @author J. Villing
 */
@ConfigurationProperties(prefix = "praxis-intercom.signaling")
@Getter
@Setter
public class SignalingProperties {
    private String notificationTypeForUnavailable;
}
