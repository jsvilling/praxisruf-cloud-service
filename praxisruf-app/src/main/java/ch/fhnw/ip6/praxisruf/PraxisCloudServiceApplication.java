package ch.fhnw.ip6.praxisruf;

import ch.fhnw.ip6.praxisruf.commons.config.security.JWTProperties;
import ch.fhnw.ip6.praxisruf.notification.config.FirebaseProperties;
import ch.fhnw.ip6.praxisruf.speechsynthesis.config.AwsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableConfigurationProperties({AwsConfiguration.class, FirebaseProperties.class, JWTProperties.class})
@EnableWebSecurity
public class PraxisCloudServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PraxisCloudServiceApplication.class, args);
    }

}
