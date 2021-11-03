package ch.fhnw.ip6.praxisruf.notification.web.client;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.NotificationTypeDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.RegistrationDto;
import ch.fhnw.ip6.praxisruf.notification.domain.PraxisNotification;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

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

    public NotificationTypeDto findExistingNotificationType(UUID notificationTypeId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/notificationtypes/" + notificationTypeId).build())
                .retrieve()
                .bodyToMono(NotificationTypeDto.class)
                .block();
    }

    public ClientDto findExistingClient(UUID clientId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/clients/" + clientId).build())
                .retrieve()
                .bodyToMono(ClientDto.class)
                .block();
    }

}
