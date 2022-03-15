package ch.fhnw.ip6.praxisruf.signaling.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Collection;
import java.util.Map;

/**
 * Configures an Interceptor to evaluate http handshake requests made to initialize Websocket connections.
 * This allows to verify that a request to open Websockets is authenticated and that the user has been
 * granted the appropriate roles.
 *
 * @author J. Villing
 */
public class SignalingHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    /**
     * Intercepts http handshake requests made to initialize Websockets.
     *
     * This method validates, that the request has been authenticated and contains any of the roles ADMIN or USER.
     * If either condition is not met, the request will be denied and processing stops.
     * In this case no Websocket connection will be established.
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        final UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken) request.getPrincipal();
        if (principal == null || !principal.isAuthenticated()) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        return hasAllowedRole(principal) && super.beforeHandshake(request, response, wsHandler, attributes);
    }

    private boolean hasAllowedRole(UsernamePasswordAuthenticationToken principal) {
        final Collection<GrantedAuthority> authorities = principal.getAuthorities();
        return authorities != null
                && authorities.stream()
                    .map(a -> (SimpleGrantedAuthority) a)
                    .map(SimpleGrantedAuthority::getAuthority)
                    .anyMatch(a -> "ROLE_ADMIN".equals(a) || "ROLE_USER".equals(a));
    }
}
