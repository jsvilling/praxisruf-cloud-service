package ch.fhnw.ip6.praxisruf.service;

import ch.fhnw.ip6.praxisruf.api.ClientConnector;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationResponseDto;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.domain.ClientConnection;
import ch.fhnw.ip6.praxisruf.domain.Signal;
import ch.fhnw.ip6.praxisruf.web.client.NotificationWebClient;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode.CONNECTION_UNKNOWN;

@Service
@Slf4j
@AllArgsConstructor
public class SignalingService implements ClientConnector<WebSocketSession, TextMessage> {

    private static final UUID UNAVAILABLE_NOTIFICATION_ID = UUID.fromString("e6e6149c-7057-40a9-b937-0dbddb1fa879");

    final ConnectionRegistry registry;
    final NotificationWebClient notificationWebClient;

    @Override
    public void handleMessage(TextMessage message) {
        final Signal signal = new Gson().fromJson(message.getPayload(), Signal.class);
        final String originalSender = signal.getSender();
        final String originalRecipient = signal.getRecipient();

        boolean success = send(message, originalRecipient);
        if (!success) {
            final TextMessage unavailable = createUnavailableMessage(originalSender, originalRecipient);
            send(unavailable, originalSender);
        } else if (signal.isNotificationOnFailedDelivery()) {
            log.info("Sending notification for failed signal {}", signal.getType());
            sendNotificationToUnavailable(signal);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        final String id = extractClientId(session);
        registry.register(new ClientConnection(id, session));
        log.info("Established connection for {}", id);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session) throws Exception {
        final String id = extractClientId(session);
        registry.unregister(id);
        session.close();
        log.info("Closed connection for {}", id);
    }

    private boolean send(TextMessage message, String recipient) {
        try {
            log.info("Sending signal {} to {}", message.getPayload(), recipient);
            final Optional<ClientConnection> connection = registry.find(recipient);
            final boolean success = connection.isPresent();
            if (success) {
                connection.get().getSession().sendMessage(message);
            }
            return success;
        } catch (Exception e) {
            log.error("Error when forwarding signal to {}", recipient, e);
            return false;
        }
    }

    private String extractClientId(WebSocketSession session) {
        final URI uri = session.getUri();
        if (uri != null) {
            String[] parameters = uri.getQuery().split("=");
            return parameters[1];
        }
        throw new PraxisIntercomException(CONNECTION_UNKNOWN);
    }

    private TextMessage createUnavailableMessage(String originalSender, String originalRecipient) {
        final Signal signal = Signal.builder()
                .sender(originalRecipient)
                .recipient(originalSender)
                .type("UNAVAILABLE")
                .payload("")
                .description("")
                .build();
        final String payload = new Gson().toJson(signal);
        return new TextMessage(payload);
    }

    private void sendNotificationToUnavailable(Signal signal) {
        final SendPraxisNotificationDto notification = SendPraxisNotificationDto.builder()
                .notificationTypeId(UNAVAILABLE_NOTIFICATION_ID)
                .sender(UUID.fromString(signal.getSender()))
                .build();

        final SendPraxisNotificationResponseDto sendResult = notificationWebClient.send(notification);
        if (!sendResult.isAllSuccess()) {
            log.error("Could not notify unavailable client");
        }
    }
}