package ch.fhnw.ip6.praxisruf.config;

import ch.fhnw.ip6.praxisruf.web.SocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import reactor.util.annotation.Nullable;

import java.util.Map;

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new SocketHandler(), "/signaling")
				.setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor() {

            @Override
            public void afterHandshake(ServerHttpRequest request,
                    ServerHttpResponse response, WebSocketHandler wsHandler,
                    @Nullable Exception ex) {
                log.info("I have intercepted after handshake");
                super.afterHandshake(request, response, wsHandler, ex);
            }

            @Override
            public boolean beforeHandshake(ServerHttpRequest request,
                    ServerHttpResponse response, WebSocketHandler wsHandler,
                    Map<String, Object> attributes) throws Exception {

                request.getHeaders().forEach((t, v) -> log.info("{} : {}", t, v));

                log.info("I have intercepted before handshake");
                return true;
            }
        });
	}

}
