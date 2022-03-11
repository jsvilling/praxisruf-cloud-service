package ch.fhnw.ip6.praxisruf.service;

import ch.fhnw.ip6.praxisruf.signaling.domain.ClientConnection;
import ch.fhnw.ip6.praxisruf.signaling.service.ConnectionRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ConnectionRegistryTest {

    private ConnectionRegistry connectionRegistry;

    @BeforeEach
    public void beforeEach() {
        connectionRegistry = new ConnectionRegistry();
    }

    @Nested
    class Register {

        @Test
        void register_singleValid() {
            // Given
            final String id = UUID.randomUUID().toString();
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final ClientConnection connection = new ClientConnection(id, session);

            // When
            final boolean result = connectionRegistry.register(connection);

            // Then
            assertThat(result).isTrue();
        }

        @Test
        void register_multipleValid() {
            // Given
            final String firstId = UUID.randomUUID().toString();
            final WebSocketSession firstSession = Mockito.mock(WebSocketSession.class);
            final ClientConnection firstConnection = new ClientConnection(firstId, firstSession);
            connectionRegistry.register(firstConnection);

            final String secondId = UUID.randomUUID().toString();
            final WebSocketSession secondSession = Mockito.mock(WebSocketSession.class);
            final ClientConnection secondConnection = new ClientConnection(secondId, secondSession);

            // When
            final boolean result = connectionRegistry.register(secondConnection);

            // Then
            assertThat(result).isTrue();
        }

        @Test
        void register_forExistingId() {
            // Given
            final String id = UUID.randomUUID().toString();
            final WebSocketSession firstSession = Mockito.mock(WebSocketSession.class);
            final ClientConnection firstConnection = new ClientConnection(id, firstSession);
            connectionRegistry.register(firstConnection);

            final WebSocketSession secondSession = Mockito.mock(WebSocketSession.class);
            final ClientConnection secondConnection = new ClientConnection(id, secondSession);

            // When
            final boolean result = connectionRegistry.register(secondConnection);

            // Then
            assertThat(result).isTrue();
        }

        @Test
        void register_null() {
            // Given
            // When
            final boolean result = connectionRegistry.register(null);

            // Then
            assertThat(result).isFalse();
        }

        @Test
        void register_nullId() {
            // Given
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final ClientConnection connection = new ClientConnection(null, session);

            // When
            final boolean result = connectionRegistry.register(connection);

            // Then
            assertThat(result).isFalse();
        }

        @Test
        void register_nullSession() {
            // Given
            final String id = UUID.randomUUID().toString();
            final ClientConnection connection = new ClientConnection(id, null);

            // When
            final boolean result = connectionRegistry.register(connection);

            // Then
            assertThat(result).isFalse();
        }
    }

    @Nested
    class Find {

        @Test
        void find_connectionWasRegisteredOnce() {
            // Given
            final String id = UUID.randomUUID().toString();
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final ClientConnection connection = new ClientConnection(id, session);
            connectionRegistry.register(connection);

            // When
            final Optional<ClientConnection> result = connectionRegistry.find(id);

            // Then
            assertThat(result).isNotEmpty().get().isSameAs(connection);
        }

        @Test
        void findRegistered_connectionWasRegisteredMultiple() {
            // Given
            final String id = UUID.randomUUID().toString();
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final ClientConnection firstConnection = new ClientConnection(id, session);
            final ClientConnection secondConnection = new ClientConnection(id, session);
            connectionRegistry.register(firstConnection);
            connectionRegistry.register(secondConnection);

            // When
            final Optional<ClientConnection> result = connectionRegistry.find(id);

            // Then
            assertThat(result).isNotEmpty().get().isSameAs(secondConnection);
        }

        @Test
        void find_notFound() {
            // Given
            final String id = UUID.randomUUID().toString();

            // When
            final Optional<ClientConnection> result = connectionRegistry.find(id);

            // Then
            assertThat(result).isEmpty();
        }

    }

    @Nested
    class Unregister {

        @Test
        void unregister_success() {
            // Given
            final String id = UUID.randomUUID().toString();
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final ClientConnection connection = new ClientConnection(id, session);
            connectionRegistry.register(connection);
            assertThat(connectionRegistry.find(id)).isNotEmpty();

            // When
            final boolean result = connectionRegistry.unregister(id);

            // Then
            assertThat(result).isTrue();
            assertThat(connectionRegistry.find(id)).isEmpty();
        }

        @Test
        void unregister_nullId() {
            // When
            final boolean result = connectionRegistry.unregister(null);

            // Then
            assertThat(result).isFalse();
        }

        @Test
        void unregister_unknownId() {
            // Given
            final String id = UUID.randomUUID().toString();
            final String unknownId = UUID.randomUUID().toString();
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final ClientConnection connection = new ClientConnection(id, session);
            connectionRegistry.register(connection);
            assertThat(connectionRegistry.find(id)).isNotEmpty();

            // When
            final boolean result = connectionRegistry.unregister(unknownId);

            // Then
            assertThat(result).isTrue();
            assertThat(connectionRegistry.find(id)).isNotEmpty();
            assertThat(connectionRegistry.find(unknownId)).isEmpty();
        }

    }

}
