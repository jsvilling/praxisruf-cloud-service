package ch.fhnw.ip6.praxisruf.service;

import ch.fhnw.ip6.praxisruf.domain.ClientConnection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Contracts for managing connections created by ClientConnector.
 */
@Component
public class ConnectionRegistry {

    final List<ClientConnection> registry = new CopyOnWriteArrayList<>();

    /**
     * Registers the given connection in a key value store using clientId as key.
     * @return boolean - whether the registration is registered
     */
    public boolean register(ClientConnection connection) {
        if (isInvalidConnection(connection)) {
            return false;
        }
        registry.removeIf(c -> c.getId().equalsIgnoreCase(connection.getId()));
        return registry.add(connection);
    }
    private boolean isInvalidConnection(ClientConnection connection) {
        return connection == null || connection.getId() == null || connection.getSession() == null;
    }

    /**
     * Removes the given connection from the key value store
     * @return boolean - whether the registration is unregistered
     */
    public boolean unregister(String id) {
        if (id == null) {
            return false;
        }
        registry.removeIf(c -> c.getId().equalsIgnoreCase(id));
        return true;
    }

    public Optional<ClientConnection> find(String id) {
        return registry.stream().filter(c -> c.getId().equalsIgnoreCase(id)).findFirst();
    }
}
