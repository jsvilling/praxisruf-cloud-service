package ch.fhnw.ip5.praxiscloudservice.web.speechsynth;

import ch.fhnw.ip5.praxiscloudservice.config.AwsProperties;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequestMapping("/api/speechsynthesis")
@Api(tags = "Speech Synthesis Test")
public class SpeechSynthesisTestController {

    private static final String SAMPLE = "Benachrichtigung empfangen";
    private final AmazonPollyClient polly;
    private final Voice voice;

    public SpeechSynthesisTestController(@Autowired AwsProperties awsProperties) {
        final BasicAWSCredentials credentials = new BasicAWSCredentials(awsProperties.getAccessKey(), awsProperties.getSecretKey());
        final AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(credentials);
        polly = new AmazonPollyClient(awsStaticCredentialsProvider, new ClientConfiguration());
        polly.setRegion(Region.getRegion(Regions.EU_WEST_1));
        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();
        DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
        voice = describeVoicesResult.getVoices().stream().filter(v -> v.getLanguageName().equals("German")).findFirst().get();
    }

    @GetMapping(produces = "audio/mp3")
    public ResponseEntity synthesizeTestAudio() {
        final SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest()
                .withText(SAMPLE)
                .withVoiceId(voice.getId())
                .withOutputFormat(OutputFormat.Mp3);

        final SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);
        final InputStream inputStream = synthRes.getAudioStream();
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        return new ResponseEntity(inputStreamResource, HttpStatus.OK);
    }

}
