package ch.fhnw.ip5.praxiscloudservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "praxis-intercom.aws")
@Getter
@Setter
public class AwsProperties {
    private String accessKey;
    private String secretKey;
}
