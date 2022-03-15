package ch.fhnw.ip6.praxisruf.signaling.web.client;

import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Client to call the API of the notification module.
 *
 * The NotificationWebClient is used in {@link ch.fhnw.ip6.praxisruf.signaling.service.SignalingService} to notify clients
 * for signals that could not be delivered.
 *
 * @author J. Villing
 */
@AllArgsConstructor
public class NotificationWebClient {

    private final WebClient webClient;

    public SendPraxisNotificationResponseDto send(SendPraxisNotificationDto notification, String recipientId) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/notifications/targeted")
                        .queryParam("recipient", recipientId)
                        .build())
                .bodyValue(notification)
                .retrieve()
                .bodyToMono(SendPraxisNotificationResponseDto.class)
                .block();
    }

}
