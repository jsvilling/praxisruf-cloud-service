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

public class SignalingHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
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
                    .anyMatch(a -> "ADMIN".equals(a) || "USER".equals(a));
    }
}
