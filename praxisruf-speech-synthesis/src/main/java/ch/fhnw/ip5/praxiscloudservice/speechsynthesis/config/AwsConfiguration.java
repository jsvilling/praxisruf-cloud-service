package ch.fhnw.ip5.praxiscloudservice.speechsynthesis.config;

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

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "praxis-intercom.aws")
public class AwsConfiguration {

    private String accessKey;
    private String secretKey;

    @Bean
    public AmazonPollyClient amazonPollyClient() {
        final AWSStaticCredentialsProvider credentialsProvider = awsStaticCredentialsProvider();
        final AmazonPollyClient polly = new AmazonPollyClient(credentialsProvider, new ClientConfiguration());
        polly.setRegion(Region.getRegion(Regions.EU_WEST_1));
        return polly;
    }

    @Bean
    public Voice voice(AmazonPollyClient polly) {
        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();
        DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
        return describeVoicesResult.getVoices().stream().filter(v -> v.getLanguageName().equals("German")).findFirst().get();
    }

    private AWSStaticCredentialsProvider awsStaticCredentialsProvider() {
        final BasicAWSCredentials basicAWSCredentials = basicAWSCredentials();
        return new AWSStaticCredentialsProvider(basicAWSCredentials);
    }

    private BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }
}
