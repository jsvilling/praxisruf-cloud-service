package ch.fhnw.ip6.praxisruf.service;

import ch.fhnw.ip6.praxisruf.domain.ClientConnection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Contracts for managing connections created by ClientConnector.
 * @param <T> Type of the connections
 */
@Component
public class ConnectionRegistry {

    final List<ClientConnection> registry = new CopyOnWriteArrayList<>();

    /**
     * Registers the given connection in a key value store using clientId as key.
     * @return boolean - whether the registration is registered
     */
    public boolean register(ClientConnection connection) {
        return registry.add(connection);
    }

    /**
     * Removes the given connection from the key value store
     * @return boolean - whether the registration is unregistered
     */
    public boolean unregister(String id) {
        registry.removeIf(c -> c.getId().equals(id));
        return true;
    }

    public ClientConnection find(String id) {
        return registry.stream().filter(c -> c.getId().equals(id)).findFirst().orElseThrow();
    }
}
