package ch.fhnw.ip6.praxisruf.config;

import ch.fhnw.ip6.praxisruf.commons.config.security.JWTProperties;
import ch.fhnw.ip6.praxisruf.commons.config.security.filter.JWTTokenValidatorFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class SignalingConnectionHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    private final JWTTokenValidatorFilter jwtTokenValidatorFilter = new JWTTokenValidatorFilter(new JWTProperties());

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes)  {
        try {
            jwtTokenValidatorFilter.doFilterInternal(request, response);
            return super.beforeHandshake(request, response, wsHandler, attributes);
        } catch (Exception e) {
            log.error("Bad Credentials.");
            return false;
        }
    }
}
