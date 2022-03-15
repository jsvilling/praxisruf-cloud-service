package ch.fhnw.ip6.praxisruf.signaling.service;

import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationResponseDto;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.signaling.api.ClientConnector;
import ch.fhnw.ip6.praxisruf.signaling.config.SignalingProperties;
import ch.fhnw.ip6.praxisruf.signaling.domain.ClientConnection;
import ch.fhnw.ip6.praxisruf.signaling.domain.Signal;
import ch.fhnw.ip6.praxisruf.signaling.web.client.NotificationWebClient;
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

/**
 * Implements the {@link ClientConnector<WebSocketSession, TextMessage>}
 *
 * The SingalingSerivce allow registering and unregistering Websocket connections for signaling exchange in the {@link ConnectionRegistry}.
 * Every opened connection is expected to specify its associated clientId in a query parameter with said name.
 * If a connection has specified a clientId, the WebsocketSession will be stored in the {@link ConnectionRegistry} with that id.
 * After the WebsocketSession is closed, it will be removed from the registry.
 *
 * Signals can be exchanged over opened connections.
 * Every received TextMessage is expected to contain a valid JSON String representation of a {@link Signal}.
 * The recipient field in this signal is used to find the corresponding connection in the {@link ConnectionRegistry}.
 * The signal is then sent over the found connection.
 * If no connection is found or delivery fails, the flag notificationOnFailedDelivery ot the signal is evaluated.
 * If the flag is true, a notification will be sent to the given clientId via the Notification domain.
 *
 * @author J. Villing
 */
@Service
@Slf4j
@AllArgsConstructor
public class SignalingService implements ClientConnector<WebSocketSession, TextMessage> {

    private static final String QUERY_PARAM_DELIMITER = "&";
    private static final String CLIENT_ID_KEY = "clientId";

    private final ConnectionRegistry registry;
    private final NotificationWebClient notificationWebClient;
    private final SignalingProperties signalingProperties;

    /**
     *  Accepts text messages, converts them to a {@link Signal} and forwards it to the recipient.
     *
     *  Every received TextMessage is expected to contain a valid JSON String representation of a {@link Signal}.
     *  The recipient field in this signal is used to find the corresponding connection in the {@link ConnectionRegistry}.
     *  The signal is then sent over the found connection.
     *  If no connection is found or delivery fails, the flag notificationOnFailedDelivery ot the signal is evaluated.
     *  If the falg is true, a notification will be sent to the given clientId via the Notification domain.
     *
     *  @param message - TextMessage containing a JSON String representation of a {@link Signal}
     */
    @Override
    public void handleSignal(TextMessage message) {
        final Signal signal = new Gson().fromJson(message.getPayload(), Signal.class);
        final String originalSender = signal.getSender();
        final String originalRecipient = signal.getRecipient();

        boolean success = send(message, originalRecipient);
        if (!success) {
            final TextMessage unavailable = createUnavailableMessage(originalSender, originalRecipient);
            send(unavailable, originalSender);
        }
        if (!success && signal.isNotificationOnFailedDelivery()) {
            log.debug("Sending notification for failed signal {}", signal.getType());
            sendNotificationToUnavailable(signal);
        }
    }

    /**
     * Adds an opened WebsocketSession to the {@link ConnectionRegistry}.
     * The URL of the opened connection is expected to define a clientId in a query parameter of said name.
     * If a connection has specified a clientId, the WebsocketSession will be stored in the {@link ConnectionRegistry} with that id.
     * Otherwise an error is thrown.
     *
     * There can always be only one connection associated with a clientId.
     * If multiple connections are registered for the same id, the last used connection will be added to the registry.
     * All previousely registered instances will be removed.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        final String id = extractClientId(session);
        registry.register(new ClientConnection(id, session));
        log.debug("Established connection for {}", id);
    }

    /**
     * Removes the given connection from the {@link ConnectionRegistry}
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session) throws Exception {
        final String id = extractClientId(session);
        registry.unregister(id);
        session.close();
        log.debug("Closed connection for {}", id);
    }

    private boolean send(TextMessage message, String recipient) {
        try {
            log.debug("Sending signal {} to {}", message.getPayload(), recipient);
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
        final UUID unavailableNotificationTypeId = UUID.fromString(signalingProperties.getNotificationTypeForUnavailable());
        final SendPraxisNotificationDto notification = SendPraxisNotificationDto.builder()
                .notificationTypeId(unavailableNotificationTypeId)
                .sender(UUID.fromString(signal.getSender()))
                .build();

        final SendPraxisNotificationResponseDto sendResult = notificationWebClient.send(notification, signal.getRecipient());
        if (!sendResult.isAllSuccess()) {
            log.error("Could not notify unavailable client");
        }
    }
}
