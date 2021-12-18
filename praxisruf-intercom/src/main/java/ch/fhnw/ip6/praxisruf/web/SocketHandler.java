package ch.fhnw.ip6.praxisruf.web;

import ch.fhnw.ip6.praxisruf.domain.ClientConnection;
import ch.fhnw.ip6.praxisruf.domain.Signal;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class SocketHandler extends TextWebSocketHandler {

    final List<ClientConnection> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws IOException {
        final Signal signal = new Gson().fromJson(message.getPayload(), Signal.class);
        for (ClientConnection connection : sessions) {
            if (!session.getId().equals(connection.getSession().getId()) && connection.getId().equals(signal.getRecipient()) ) {
                connection.getSession().sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String id = session.getUri().getQuery().split("=")[1];
        log.debug("Connection established for clientId {}", id);
        sessions.add(new ClientConnection(id, session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.removeIf(c -> c.getSession() == session);
        session.close();
    }
}
