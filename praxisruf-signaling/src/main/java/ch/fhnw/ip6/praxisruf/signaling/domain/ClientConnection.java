package ch.fhnw.ip6.praxisruf.signaling.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

/**
 * Represents the connection of a client to the signaling instance.
 * The connection consists of the {@link WebSocketSession} instance and its associated client identifier.
 *
 * ClientConnetion is used by the {@link ch.fhnw.ip6.praxisruf.signaling.service.ConnectionRegistry} to manage known connections.
 *
 * @author J. Villing
 */
@AllArgsConstructor
@Getter
public class ClientConnection {

    private final String id;
    private final WebSocketSession session;

}
