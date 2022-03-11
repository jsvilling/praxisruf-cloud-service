package ch.fhnw.ip6.praxisruf.config;

import ch.fhnw.ip6.praxisruf.signaling.config.SignalingHandshakeInterceptor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.WebSocketHandler;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

class SignalingHandshakeInterceptorTest {

    private final SignalingHandshakeInterceptor interceptor = new SignalingHandshakeInterceptor();

    @Test
    void interceptAuthenticated() throws Exception {
        // Given
        final ServerHttpRequest request = Mockito.mock(ServerHttpRequest.class);
        final ServerHttpResponse response = Mockito.mock(ServerHttpResponse.class);
        final WebSocketHandler wsHandler = Mockito.mock(WebSocketHandler.class);
        final Map<String, Object> attribute = Collections.emptyMap();

        final Principal principal = createPrincipal(true);
        Mockito.when(request.getPrincipal()).thenReturn(principal);

        // When
        final boolean result = interceptor.beforeHandshake(request, response, wsHandler, attribute);

        // Then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void interceptUnauthenticated() throws Exception {
        // Given
        final ServerHttpRequest request = Mockito.mock(ServerHttpRequest.class);
        final ServerHttpResponse response = Mockito.mock(ServerHttpResponse.class);
        final WebSocketHandler wsHandler = Mockito.mock(WebSocketHandler.class);
        final Map<String, Object> attribute = Collections.emptyMap();

        final Principal principal = createPrincipal(false);
        Mockito.when(request.getPrincipal()).thenReturn(principal);

        // When
        final boolean result = interceptor.beforeHandshake(request, response, wsHandler, attribute);

        // Then
        Assertions.assertThat(result).isFalse();
    }

    private Principal createPrincipal(boolean isAuthenticated) {
        if (isAuthenticated) {
            SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
            return new UsernamePasswordAuthenticationToken(null, null, Set.of(admin));
        }
        return new UsernamePasswordAuthenticationToken(null, null);
    }

}
