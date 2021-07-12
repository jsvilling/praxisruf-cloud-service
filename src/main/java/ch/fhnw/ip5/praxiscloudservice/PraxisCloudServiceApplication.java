package ch.fhnw.ip5.praxiscloudservice;

import ch.fhnw.ip5.praxiscloudservice.config.FirebaseProperties;
import ch.fhnw.ip5.praxiscloudservice.config.JWTProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FirebaseProperties.class, JWTProperties.class})
@EnableWebSecurity(debug = true)
public class PraxisCloudServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PraxisCloudServiceApplication.class, args);
    }

}
