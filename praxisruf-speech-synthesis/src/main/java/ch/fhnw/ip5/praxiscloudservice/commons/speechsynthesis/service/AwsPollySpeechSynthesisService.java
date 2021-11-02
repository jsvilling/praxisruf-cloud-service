package ch.fhnw.ip5.praxiscloudservice.commons.speechsynthesis.service;

import ch.fhnw.ip5.praxiscloudservice.commons.speechsynthesis.api.SpeechSynthesisService;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@AllArgsConstructor
public class AwsPollySpeechSynthesisService implements SpeechSynthesisService {

    private static final String SAMPLE = "Benachrichtigung empfangen";
    private final AmazonPollyClient polly;
    private final Voice voice;

    public InputStreamResource test() {
        final SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest()
                .withText(SAMPLE)
                .withVoiceId(voice.getId())
                .withOutputFormat(OutputFormat.Mp3);

        final SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);
        final InputStream inputStream = synthRes.getAudioStream();
        return new InputStreamResource(inputStream);
    }

}
