package ch.fhnw.ip6.praxisruf.service;

import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationResponseDto;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.domain.ClientConnection;
import ch.fhnw.ip6.praxisruf.domain.Signal;
import ch.fhnw.ip6.praxisruf.web.client.NotificationWebClient;
import com.google.gson.Gson;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignalingServiceTest {

    private static final String ORIGINAL_SENDER = UUID.randomUUID().toString();
    private static final String ORIGINAL_RECIPIENT = UUID.randomUUID().toString();

    @Captor
    private ArgumentCaptor<ClientConnection> connectionArgumentCaptor;

    @Mock
    private ConnectionRegistry registry;

    @Mock
    private NotificationWebClient notificationWebClient;

    @InjectMocks
    private SignalingService signalingService;

    @Nested
    class ConnectionEstablished {

        @Test
        void connectionEstablished_success() {
            // Given
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final String id = UUID.randomUUID().toString();
            when(session.getUri()).thenReturn(URI.create("wss://server/signaling?clientId=" + id));

            // When
            signalingService.afterConnectionEstablished(session);

            // Then
            Mockito.verify(registry).register(connectionArgumentCaptor.capture());
            assertThat(connectionArgumentCaptor.getValue()).satisfies(c -> {
                assertThat(c.getId()).isEqualTo(id);
                assertThat(c.getSession()).isSameAs(session);
            });
        }

        @Test
        void connectionEstablished_successClientIdInSecond() {
            // Given
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final String id = UUID.randomUUID().toString();
            when(session.getUri()).thenReturn(URI.create("wss://server/signaling?other=oo&clientId=" + id));

            // When
            signalingService.afterConnectionEstablished(session);

            // Then
            Mockito.verify(registry).register(connectionArgumentCaptor.capture());
            assertThat(connectionArgumentCaptor.getValue()).satisfies(c -> {
                assertThat(c.getId()).isEqualTo(id);
                assertThat(c.getSession()).isSameAs(session);
            });
        }


        @Test
        void connectionEstablished_noUri() {
            // Given
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            when(session.getUri()).thenReturn(null);

            // When
            // Then
            assertThatThrownBy(() -> signalingService.afterConnectionEstablished(session))
                    .isInstanceOf(PraxisIntercomException.class);
        }

        @Test
        void connectionEstablished_noQueryParams() {
            // Given
            // When
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final String id = UUID.randomUUID().toString();
            when(session.getUri()).thenReturn(URI.create("wss://server/signaling"));

            // When
            // Then
            assertThatThrownBy(() -> signalingService.afterConnectionEstablished(session))
                    .isInstanceOf(PraxisIntercomException.class);
        }

        @Test
        void connectionClosed_noClientIdParam() {
            // Given
            // When
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final String id = UUID.randomUUID().toString();
            when(session.getUri()).thenReturn(URI.create("wss://server/signaling?other=smth&othersmth=ot"));

            // When
            // Then
            assertThatThrownBy(() -> signalingService.afterConnectionClosed(session))
                    .isInstanceOf(PraxisIntercomException.class);
        }
    }

    @Nested
    class ConnectionClosed {

        @Test
        void connectionClosed_success() throws Exception {
            // Given
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final String id = UUID.randomUUID().toString();
            when(session.getUri()).thenReturn(URI.create("wss://server/signaling?clientId=" + id));

            // When
            signalingService.afterConnectionClosed(session);

            // Then
            Mockito.verify(registry).unregister(id);
            Mockito.verify(session).close();
        }

        @Test
        void connectionClosed_successClientIdInSecond() throws Exception {
            // Given
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final String id = UUID.randomUUID().toString();
            when(session.getUri()).thenReturn(URI.create("wss://server/signaling?other=oo&clientId=" + id));

            // When
            signalingService.afterConnectionClosed(session);

            // Then
            Mockito.verify(registry).unregister(id);
            Mockito.verify(session).close();
        }


        @Test
        void connectionClosed_noUri() {
            // Given
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            when(session.getUri()).thenReturn(null);

            // When
            // Then
            assertThatThrownBy(() -> signalingService.afterConnectionClosed(session))
                    .isInstanceOf(PraxisIntercomException.class);
        }

        @Test
        void connectionClosed_noQueryParams() {
            // Given
            // When
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final String id = UUID.randomUUID().toString();
            when(session.getUri()).thenReturn(URI.create("wss://server/signaling"));

            // When
            // Then
            assertThatThrownBy(() -> signalingService.afterConnectionClosed(session))
                    .isInstanceOf(PraxisIntercomException.class);
        }

        @Test
        void connectionClosed_noClientIdParam() {
            // Given
            // When
            final WebSocketSession session = Mockito.mock(WebSocketSession.class);
            final String id = UUID.randomUUID().toString();
            when(session.getUri()).thenReturn(URI.create("wss://server/signaling?other=smth&othersmth=ot"));

            // When
            // Then
            assertThatThrownBy(() -> signalingService.afterConnectionClosed(session))
                    .isInstanceOf(PraxisIntercomException.class);
        }
    }

    @Nested
    class HandleMessage {

        @Test
        void handleMessage_succesForward() throws IOException {
            // Given
            final TextMessage message = createSignalingMessage();
            final WebSocketSession recipientSession = Mockito.mock(WebSocketSession.class);
            final ClientConnection recipientConnection = new ClientConnection(ORIGINAL_RECIPIENT, recipientSession);

            when(registry.find(ORIGINAL_RECIPIENT)).thenReturn(Optional.of(recipientConnection));

            // When
            signalingService.handleSignal(message);

            // Then
            verify(recipientSession).sendMessage(message);
            verifyNoMoreInteractions(recipientSession);
            verifyNoInteractions(notificationWebClient);
        }

        @Test
        void handleMessage_connectionNotRegistered() throws IOException {
            // Given
            final TextMessage message = createSignalingMessage();
            final WebSocketSession senderSession = Mockito.mock(WebSocketSession.class);
            final ClientConnection senderConnection = new ClientConnection(ORIGINAL_SENDER, senderSession);

            when(registry.find(ORIGINAL_SENDER)).thenReturn(Optional.of(senderConnection));
            when(registry.find(ORIGINAL_RECIPIENT)).thenReturn(Optional.empty());

            final SendPraxisNotificationResponseDto notificationResponse = SendPraxisNotificationResponseDto.builder().notificationId(UUID.randomUUID()).allSuccess(true).build();
            when(notificationWebClient.send(any(), any())).thenReturn(notificationResponse);

            // When
            signalingService.handleSignal(message);

            // Then
            verify(senderSession).sendMessage(any());
            verify(notificationWebClient).send(any(), eq(ORIGINAL_RECIPIENT));
        }

        @Test
        void handleMessage_sendSignalFailed() throws IOException {
            // Given
            final TextMessage message = createSignalingMessage();
            final WebSocketSession recipientSession = Mockito.mock(WebSocketSession.class);
            final ClientConnection recipientConnection = new ClientConnection(ORIGINAL_RECIPIENT, recipientSession);
            final WebSocketSession senderSession = Mockito.mock(WebSocketSession.class);
            final ClientConnection senderConnection = new ClientConnection(ORIGINAL_SENDER, senderSession);

            when(registry.find(ORIGINAL_SENDER)).thenReturn(Optional.of(senderConnection));
            when(registry.find(ORIGINAL_RECIPIENT)).thenReturn(Optional.of(recipientConnection));
            doThrow(new IOException()).when(recipientSession).sendMessage(any());

            final SendPraxisNotificationResponseDto notificationResponse = SendPraxisNotificationResponseDto.builder().notificationId(UUID.randomUUID()).allSuccess(true).build();
            when(notificationWebClient.send(any(), any())).thenReturn(notificationResponse);

            // When
            signalingService.handleSignal(message);

            // Then
            verify(senderSession).sendMessage(any());
            verifyNoMoreInteractions(recipientSession);
            verify(notificationWebClient).send(any(), eq(ORIGINAL_RECIPIENT));
        }

        @Test
        void handleMessage_connectionNotRegistered_notificationCallFailed() throws IOException {
            // Given
            final TextMessage message = createSignalingMessage();
            final WebSocketSession senderSession = Mockito.mock(WebSocketSession.class);
            final ClientConnection senderConnection = new ClientConnection(ORIGINAL_SENDER, senderSession);

            when(registry.find(ORIGINAL_SENDER)).thenReturn(Optional.of(senderConnection));
            when(registry.find(ORIGINAL_RECIPIENT)).thenReturn(Optional.empty());

            final SendPraxisNotificationResponseDto notificationResponse = SendPraxisNotificationResponseDto.builder().notificationId(UUID.randomUUID()).allSuccess(false).build();
            when(notificationWebClient.send(any(), any())).thenReturn(notificationResponse);

            // When
            signalingService.handleSignal(message);

            // Then
            verify(senderSession).sendMessage(any());
            verify(notificationWebClient).send(any(), eq(ORIGINAL_RECIPIENT));
            verifyNoMoreInteractions(notificationWebClient);
            verifyNoMoreInteractions(senderSession);
        }


        private TextMessage createSignalingMessage() {
            Signal signal = Signal.builder()
                    .sender(ORIGINAL_SENDER)
                    .recipient(ORIGINAL_RECIPIENT)
                    .type("")
                    .payload("")
                    .description("")
                    .notificationOnFailedDelivery(true)
                    .build();

            final String signalJson = new Gson().toJson(signal);
            return new TextMessage(signalJson);
        }

    }


}
