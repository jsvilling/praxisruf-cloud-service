package ch.fhnw.ip6.praxisruf.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "praxis-intercom.jwt")
@Getter
@Setter
public class JWTProperties {
    private String key;
}
