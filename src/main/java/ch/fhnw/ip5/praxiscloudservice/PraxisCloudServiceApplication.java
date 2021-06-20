package ch.fhnw.ip5.praxiscloudservice;

import ch.fhnw.ip5.praxiscloudservice.config.FirebaseProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FirebaseProperties.class})
public class PraxisCloudServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PraxisCloudServiceApplication.class, args);
    }

}
