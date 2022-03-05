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

import static ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode.CONNECTION_URI_INVALID;

@Service
@Slf4j
@AllArgsConstructor
public class SignalingService implements ClientConnector<WebSocketSession, TextMessage> {

    private static final String QUERY_PARAM_DELIMITER = "&";
    private static final String CLIENT_ID_KEY = "clientId";
    private static final UUID UNAVAILABLE_NOTIFICATION_ID = UUID.fromString("63d530ab-48af-4597-a9fd-2fb4c9700c55");

    private final ConnectionRegistry registry;
    private final NotificationWebClient notificationWebClient;

    @Override
    public void handleMessage(TextMessage message) {
        final Signal signal = new Gson().fromJson(message.getPayload(), Signal.class);
        final String originalSender = signal.getSender();
        final String originalRecipient = signal.getRecipient();

        boolean success = send(message, originalRecipient);
        if (!success) {
            final TextMessage unavailable = createUnavailableMessage(originalSender, originalRecipient);
            send(unavailable, originalSender);
        }
        if (!success && signal.isNotificationOnFailedDelivery()) {
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
        try {
            final URI uri = session.getUri();
            final String[] parameters = uri.getQuery().split(QUERY_PARAM_DELIMITER);
            for (String parameter : parameters) {
                if (parameter.startsWith(CLIENT_ID_KEY)) {
                    return parameter.substring(CLIENT_ID_KEY.length() + 1);
                }
            }
        } catch (Exception e) {
            log.error("Encountered Error when extracting client Id from session", e);
            throw new PraxisIntercomException(CONNECTION_URI_INVALID, e);
        }
        log.error("Session does contain no client id");
        throw new PraxisIntercomException(CONNECTION_URI_INVALID);
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

        final SendPraxisNotificationResponseDto sendResult = notificationWebClient.send(notification, signal.getRecipient());
        if (!sendResult.isAllSuccess()) {
            log.error("Could not notify unavailable client");
        }
    }
}
