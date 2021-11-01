package ch.fhnw.ip5.praxiscloudservice;

import ch.fhnw.ip5.praxiscloudservice.config.AwsProperties;
import ch.fhnw.ip5.praxiscloudservice.config.FirebaseProperties;
import ch.fhnw.ip5.praxiscloudservice.config.security.JWTProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableConfigurationProperties({AwsProperties.class, FirebaseProperties.class, JWTProperties.class})
@EnableWebSecurity
public class PraxisCloudServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PraxisCloudServiceApplication.class, args);
    }

}
