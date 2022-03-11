package ch.fhnw.ip6.praxisruf.signaling.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@Getter
public class ClientConnection {

    private final String id;

    private final WebSocketSession session;

}
