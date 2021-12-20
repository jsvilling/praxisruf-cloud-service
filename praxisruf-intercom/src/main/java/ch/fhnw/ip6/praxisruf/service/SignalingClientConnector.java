package ch.fhnw.ip6.praxisruf.service;

import ch.fhnw.ip6.praxisruf.api.ClientConnector;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.domain.ClientConnection;
import ch.fhnw.ip6.praxisruf.domain.Signal;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.URI;

import static ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode.CONNECTION_UNKNOWN;

@Service
@Slf4j
@AllArgsConstructor
public class SignalingClientConnector implements ClientConnector<WebSocketSession, TextMessage> {

    final ConnectionRegistry registry;

    @Override
    public void handleMessage(TextMessage message) throws Exception {
        final Signal signal = new Gson().fromJson(message.getPayload(), Signal.class);
        if (!signal.getSender().equals(signal.getRecipient())) {
            final ClientConnection connection = registry.find(signal.getRecipient());
            connection.getSession().sendMessage(message);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        final String id = extractClientId(session);
        registry.register(new ClientConnection(id, session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session) throws Exception {
        final String id = extractClientId(session);
        registry.unregister(id);
        session.close();
    }

    private String extractClientId(WebSocketSession session) {
        final URI uri = session.getUri();
        if (uri != null) {
            String[] parameters = uri.getQuery().split("=");
            return parameters[1];
        }
        throw new PraxisIntercomException(CONNECTION_UNKNOWN);
    }

}
