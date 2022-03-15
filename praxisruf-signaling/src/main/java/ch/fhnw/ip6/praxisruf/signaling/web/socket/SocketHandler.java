package ch.fhnw.ip6.praxisruf.signaling.web.socket;

import ch.fhnw.ip6.praxisruf.signaling.api.ClientConnector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebsocketHandler used to enable Signaling between clients.
 *
 *  The WebsocketHandler allows opening and closing WebsocketSessions and sending TextMessages.
 *  The Socket itself does not contain any business logic.
 *
 *  It delegates all functionality to a {@link ClientConnector<WebSocketSession, TextMessage>} instance.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    final ClientConnector<WebSocketSession, TextMessage> connector;

    /**
     * Is called when receiving a TextMessages.
     *
     * All received messages are delegated to {@link ClientConnector}.handleSignal
     */
    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        connector.handleSignal(message);
    }

    /**
     * Is called when a new websocket connection is opened.
     *
     * The processing of opened connections is delegated to {@link ClientConnector}.afterConnectionEstablished
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        connector.afterConnectionEstablished(session);
    }

    /**
     * Is called when a new websocket connection is closed.
     *
     * The processing of opened connections is delegated to {@link ClientConnector}.afterConnectionClosed
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        connector.afterConnectionClosed(session);
    }
}
