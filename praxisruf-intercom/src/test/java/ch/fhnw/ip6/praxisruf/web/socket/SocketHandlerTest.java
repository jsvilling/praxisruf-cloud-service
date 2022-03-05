package ch.fhnw.ip6.praxisruf.web.socket;

import ch.fhnw.ip6.praxisruf.api.ClientConnector;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode.CONNECTION_UNKNOWN;

@ExtendWith(MockitoExtension.class)
class SocketHandlerTest {

    @Captor
    private ArgumentCaptor<TextMessage> captor;

    @Mock
    private ClientConnector<WebSocketSession, TextMessage> connector;

    @InjectMocks
    private SocketHandler socketHandler;

    @Nested
    class HandleTextMessage {

        @Test
        void handleTextMessage_success() throws Exception {
            // Given
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final TextMessage message = new TextMessage("PAYLOAD");

            // When
            socketHandler.handleTextMessage(session, message);

            // Then
            Mockito.verify(connector).handleMessage(captor.capture());
            Assertions.assertThat(captor.getValue()).isSameAs(message);
        }

        @Test
        void handleTextMessage_throws() throws Exception {
            // Given
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final TextMessage message = new TextMessage("PAYLOAD");
            final var e = new PraxisIntercomException(CONNECTION_UNKNOWN);
            Mockito.doThrow(e).when(connector).handleMessage(message);

            // When
            // Then
            Assertions.assertThatThrownBy(() -> socketHandler.handleMessage(session, message)).isSameAs(e);
        }
    }

    @Nested
    class ConnectionEstablished {

        @Test
        void connectionEstablished_success() throws Exception {
            // Given
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);

            // When
            socketHandler.afterConnectionEstablished(session);

            // Then
            Mockito.verify(connector).afterConnectionEstablished(session);
        }

        @Test
        void connectionEstablished_throws() {
            // Given
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final var e = new PraxisIntercomException(CONNECTION_UNKNOWN);
            Mockito.doThrow(e).when(connector).afterConnectionEstablished(session);

            // When
            // Then
            Assertions.assertThatThrownBy(() -> socketHandler.afterConnectionEstablished(session)).isSameAs(e);
        }
    }

    @Nested
    class ConnectionClosed {

        @Test
        void connectionEstablished_success() throws Exception {
            // Given
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final CloseStatus closeStatus = CloseStatus.GOING_AWAY;

            // When
            socketHandler.afterConnectionClosed(session, closeStatus);

            // Then
            Mockito.verify(connector).afterConnectionClosed(session);
        }

        @Test
        void connectionEstablished_throws() throws Exception {
            // Given
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final CloseStatus closeStatus = CloseStatus.GOING_AWAY;
            final var e = new PraxisIntercomException(CONNECTION_UNKNOWN);
            Mockito.doThrow(e).when(connector).afterConnectionClosed(session);

            // When
            // Then
            Assertions.assertThatThrownBy(() -> socketHandler.afterConnectionClosed(session, closeStatus)).isSameAs(e);
        }
    }

}
