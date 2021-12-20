package ch.fhnw.ip6.praxisruf.config;

import ch.fhnw.ip6.praxisruf.commons.config.security.JWTProperties;
import ch.fhnw.ip6.praxisruf.commons.config.security.filter.JWTTokenValidatorFilter;
import ch.fhnw.ip6.praxisruf.web.SocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

    private final JWTTokenValidatorFilter jwtTokenValidatorFilter = new JWTTokenValidatorFilter(jwtProperties());

    @Autowired
    private SocketHandler socketHandler;

    @Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(socketHandler, "/signaling")
				.setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor() {

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
                    return true;
                } catch (Exception e) {
                    log.error("Bad Credentials.");
                    return false;
                }
            }
        });
	}

    @Bean
    JWTProperties jwtProperties() {
        return new JWTProperties();
    }

}
