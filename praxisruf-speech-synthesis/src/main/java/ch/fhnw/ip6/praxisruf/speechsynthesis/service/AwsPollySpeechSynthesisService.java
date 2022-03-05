package ch.fhnw.ip6.praxisruf.speechsynthesis.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.NotificationTypeDto;
import ch.fhnw.ip6.praxisruf.commons.web.client.ConfigurationWebClient;
import ch.fhnw.ip6.praxisruf.speechsynthesis.api.SpeechSynthesisService;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AwsPollySpeechSynthesisService implements SpeechSynthesisService {

    private static final String SAMPLE = "Ich bin echt froh, dass die Anbindung an AWS Polly funktioniert. Leider funktioniert die Weiterleitung der Daten nicht mehr.";
    private final AmazonPollyClient polly;
    private final Voice voice;
    private final ConfigurationWebClient configurationWebClient;

    @Override
    public InputStreamResource synthesize(UUID notificationTypeId) {
        final NotificationTypeDto notificationType = configurationWebClient.findExistingNotificationType(notificationTypeId);
        final String content = notificationType.getTitle();
        return synthesize(content);
    }

    @Override
    public InputStreamResource synthesize(String content) {
        final SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest()
                .withText(content + " von Behandlungszimmer 1")
                .withVoiceId(voice.getId())
                .withOutputFormat(OutputFormat.Mp3);

        final SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);
        final InputStream inputStream = synthRes.getAudioStream();

        return new InputStreamResource(inputStream);
    }

}
