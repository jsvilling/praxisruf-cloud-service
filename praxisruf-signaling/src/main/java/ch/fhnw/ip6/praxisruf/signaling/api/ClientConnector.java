package ch.fhnw.ip6.praxisruf.signaling.api;

/**
 * Contracts for clients to register an intercom connection in Praxisruf and exchange signaling messages
 *
 * Once a connection is established it can be used, to negotiate messages between
 * registered clients. This enables signaling instance functionality to
 * establishing Peer To Peer Connections between clients.
 *
 * @param <T> Type of the connection
 * @param <M> Type of messages that will be exchanged
 *
 * @author J. Villing
 */
public interface ClientConnector<T, M> {

    /**
     * Receives a message and forwards it to all relevant registered connections.
     * M is expected to contain the key any relevant connection.
     */
    void handleSignal(M message) throws Exception;

    /**
     * Is called after a connection has been established. The established connection
     * is stored in a ConnectionRegistry with key: clientId and value: connection.
     */
    void afterConnectionEstablished(T connection);

    /**
     * Is called after a connection has been closed. The closed connection is
     * removed from the ConnectionRegistry.
     *
     * @param connection
     */
    void afterConnectionClosed(T connection) throws Exception;
}
