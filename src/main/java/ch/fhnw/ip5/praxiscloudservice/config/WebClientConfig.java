package ch.fhnw.ip5.praxiscloudservice.config;

import ch.fhnw.ip5.praxiscloudservice.web.notification.client.ConfigurationWebClient;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConfigurationProperties(prefix = "praxis-intercom.web")
@Setter
public class WebClientConfig {

    private String configurationClientUrl;

    @Bean
    public ConfigurationWebClient configurationWebClient(WebClient.Builder webClientBuilder) {
        final WebClient webClient = webClientBuilder.baseUrl(configurationClientUrl).build();
        return new ConfigurationWebClient(webClient);
    }

}
