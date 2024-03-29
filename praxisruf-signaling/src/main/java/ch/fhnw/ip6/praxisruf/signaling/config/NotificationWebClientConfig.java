package ch.fhnw.ip6.praxisruf.signaling.config;

import ch.fhnw.ip6.praxisruf.signaling.web.client.NotificationWebClient;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configures the NotificationWebClient
 *
 * The configured client is used in {@link ch.fhnw.ip6.praxisruf.signaling.service.SignalingService} to notify clients
 * for signals that could not be delivered.
 *
 * @author J. Villing
 */
@Configuration
@ConfigurationProperties(prefix = "praxis-intercom.web")
@Setter
public class NotificationWebClientConfig {

    private String configurationClientUrl;

    @Bean
    public NotificationWebClient notificationWebClient(WebClient.Builder webClientBuilder) {
        final WebClient webClient = webClientBuilder.baseUrl(configurationClientUrl).build();
        return new NotificationWebClient(webClient);
    }

}
