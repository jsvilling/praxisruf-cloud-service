package ch.fhnw.ip5.praxiscloudservice.web.client;

import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@AllArgsConstructor
public class ConfigurationWebClient {

    private final WebClient webClient;

    /**
     * Makes a call to the configuration endpoint to find all registered fcm tokens.
     * @return String[] - All registered tokens.
     */
    public String[] getAllKnownFcmTokens() {
        return webClient.get()
                .uri("/registrations/tokens/")
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }

    public List<String> getAllRelevantFcmTokens(PraxisNotification notification) {
        return webClient.post()
                .uri("/registrations/tokens/")
                .bodyValue(notification)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }

}
