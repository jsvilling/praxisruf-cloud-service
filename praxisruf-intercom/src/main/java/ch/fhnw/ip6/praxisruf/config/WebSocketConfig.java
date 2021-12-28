package ch.fhnw.ip6.praxisruf.config;

import ch.fhnw.ip6.praxisruf.web.socket.SocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private SocketHandler socketHandler;

    @Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(socketHandler, "/signaling")
				.setAllowedOrigins("*")
                .addInterceptors(signalingHandshakeInterceptor());
	}

	@Bean
    public SignalingHandshakeInterceptor signalingHandshakeInterceptor() {
        return new SignalingHandshakeInterceptor();
    }

}
