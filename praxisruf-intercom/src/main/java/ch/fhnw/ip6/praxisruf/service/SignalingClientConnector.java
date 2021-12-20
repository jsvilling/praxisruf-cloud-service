package ch.fhnw.ip6.praxisruf.service;

import ch.fhnw.ip6.praxisruf.api.ClientConnector;
import ch.fhnw.ip6.praxisruf.domain.ClientConnection;
import ch.fhnw.ip6.praxisruf.domain.Signal;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
@AllArgsConstructor
public class SignalingClientConnector implements ClientConnector<WebSocketSession, TextMessage> {

    final List<ClientConnection> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleMessage(TextMessage message) throws Exception {
        final Signal signal = new Gson().fromJson(message.getPayload(), Signal.class);
        log.info("Received signal {} for {}", signal.getType(), signal.getRecipient());
        for (ClientConnection connection : sessions) {
            if (connection.getId().equalsIgnoreCase(signal.getRecipient()) && !signal.getSender().equalsIgnoreCase(connection.getSession().getId())) {
                connection.getSession().sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        final String id = session.getUri().getQuery().split("=")[1];
        log.info("Connection established for clientId {}", id);
        sessions.add(new ClientConnection(id, session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session) throws Exception {
        sessions.removeIf(c -> c.getSession() == session);
        session.close();
    }

}
