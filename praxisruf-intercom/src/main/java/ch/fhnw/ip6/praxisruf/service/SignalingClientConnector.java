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

@Service
@Slf4j
@AllArgsConstructor
public class SignalingClientConnector implements ClientConnector<WebSocketSession, TextMessage> {

    final ConnectionRegistry registry;

    @Override
    public void handleMessage(TextMessage message) throws Exception {
        final Signal signal = new Gson().fromJson(message.getPayload(), Signal.class);
        log.info("Received signal {} for {}", signal.getType(), signal.getRecipient());
        if (!signal.getSender().equals(signal.getRecipient())) {
            final ClientConnection connection = registry.find(signal.getRecipient());
            connection.getSession().sendMessage(message);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        final String id = session.getUri().getQuery().split("=")[1];
        registry.register(new ClientConnection(id, session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session) throws Exception {
        final String id = session.getUri().getQuery().split("=")[1];
        registry.unregister(id);
        session.close();
    }

}
