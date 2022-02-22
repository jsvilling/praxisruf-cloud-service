package ch.fhnw.ip6.praxisruf.service;

import ch.fhnw.ip6.praxisruf.domain.ClientConnection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;
import java.util.UUID;

class ConnectionRegistryTest {

    private ConnectionRegistry connectionRegistry;

    @BeforeEach
    public void beforeEach() {
        connectionRegistry = new ConnectionRegistry();
    }

    @Test
    void add_simple() {
        // Given
        final String id = UUID.randomUUID().toString();
        final WebSocketSession session = Mockito.mock(WebSocketSession.class);
        ClientConnection connection = new ClientConnection(id, session);

        // When
        connectionRegistry.register(connection);
        final Optional<ClientConnection> result = connectionRegistry.find(id);

        // Then
        Assertions.assertThat(result).isNotEmpty().get().isSameAs(connection);
    }

    @Test
    void add_withExistingId() {
        // Given
        final String id = UUID.randomUUID().toString();
        final WebSocketSession session = Mockito.mock(WebSocketSession.class);
        final ClientConnection firstConnection = new ClientConnection(id, session);
        final ClientConnection secondConnection = new ClientConnection(id, session);

        // When
        connectionRegistry.register(firstConnection);
        connectionRegistry.register(secondConnection);
        final Optional<ClientConnection> result = connectionRegistry.find(id);

        // Then
        Assertions.assertThat(result).isNotEmpty().get().isSameAs(secondConnection);
    }

}
