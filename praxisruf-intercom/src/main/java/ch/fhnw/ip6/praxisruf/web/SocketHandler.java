package ch.fhnw.ip6.praxisruf.web;

import ch.fhnw.ip6.praxisruf.domain.ClientConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * This class handles text message inputs, websockets sessions and text message outputs
 * for binary messages extend from BinaryWebSocketHandler instead
 */
@Component
@Slf4j
public class SocketHandler extends TextWebSocketHandler {

    final List<ClientConnection> sessions = new CopyOnWriteArrayList<>();

    /***
     * This method gets called when a message is received from a client
     * @param session The actual websocket session that triggers this method
     * @param message The actual recived message
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String name = message.getPayload();
        TextMessage responseMessage = new TextMessage("Hello " + name + " !");
        //Send response to all other connected sessions
        for (ClientConnection connection : sessions) {
            if (session.getId() != connection.getSession().getId()) {
                connection.getSession().sendMessage(responseMessage);
            }
        }
    }

    /***
     * This method gets called when a client establishes a connection
     * @param session The actual websocket session that triggers this method
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        //Add to the list of connected sessions.
        String id = session.getUri().getQuery().split("=")[1];
        sessions.add(new ClientConnection(id, session));
    }

    /***
     * This method gets called when a client closes the connection
     * @param session The actual websocket session that triggers this method
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        //Remove to the list of connected sessions.
        sessions.removeIf(c -> c.getSession() == session);
        session.close();
    }
}
