package ch.fhnw.ip6.praxisruf.web.client;

import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@AllArgsConstructor
public class NotificationWebClient {

    private final WebClient webClient;

    public SendPraxisNotificationResponseDto send(SendPraxisNotificationDto notification) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/notifications/send/").build())
                .bodyValue(notification)
                .retrieve()
                .bodyToMono(SendPraxisNotificationResponseDto.class)
                .block();
    }


}
