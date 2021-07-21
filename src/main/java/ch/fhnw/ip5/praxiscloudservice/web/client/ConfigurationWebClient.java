package ch.fhnw.ip5.praxiscloudservice.web.client;

import ch.fhnw.ip5.praxiscloudservice.api.dto.RegistrationDto;
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
                .uri(uriBuilder -> uriBuilder.path("/registrations/tokens/").build())
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }

    public List<RegistrationDto> getAllRelevantRegistrations(PraxisNotification notification) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/registrations/tokens/").build())
                .bodyValue(notification)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<RegistrationDto>>() {})
                .block();
    }

}
