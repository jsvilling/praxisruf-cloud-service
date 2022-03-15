package ch.fhnw.ip6.praxisruf.speechsynthesis.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.Voice;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration initializes the Components needed for the integration of Amazon Polly.
 *
 * The configured instances are used in {@link ch.fhnw.ip6.praxisruf.speechsynthesis.service.AwsPollySpeechSynthesisService}
 *
 * @author J. Villing
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "praxis-intercom.aws")
public class AwsConfiguration {

    private String accessKey;
    private String secretKey;

    private String region;
    private String language;

    @Bean
    public AmazonPollyClient amazonPollyClient() {
        final AWSStaticCredentialsProvider credentialsProvider = awsStaticCredentialsProvider();
        final AmazonPollyClient polly = new AmazonPollyClient(credentialsProvider, new ClientConfiguration());
        polly.setRegion(Region.getRegion(Regions.fromName(region)));
        return polly;
    }

    @Bean
    public Voice voice(AmazonPollyClient polly) {
        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();
        DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
        return describeVoicesResult.getVoices().stream().filter(v -> v.getLanguageName().equals(language)).findFirst().get();
    }

    private AWSStaticCredentialsProvider awsStaticCredentialsProvider() {
        final BasicAWSCredentials basicAWSCredentials = basicAWSCredentials();
        return new AWSStaticCredentialsProvider(basicAWSCredentials);
    }

    private BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }
}
