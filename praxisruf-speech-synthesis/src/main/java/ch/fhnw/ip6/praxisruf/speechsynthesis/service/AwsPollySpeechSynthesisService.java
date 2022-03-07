package ch.fhnw.ip6.praxisruf.speechsynthesis.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.NotificationTypeDto;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.commons.web.client.ConfigurationWebClient;
import ch.fhnw.ip6.praxisruf.speechsynthesis.api.SpeechSynthesisService;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

import static ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode.SPEECH_SYNTHESIS_ERROR;

@Service
@Slf4j
@AllArgsConstructor
public class AwsPollySpeechSynthesisService implements SpeechSynthesisService {

    private static final String SAMPLE_TITLE = "Benachrichtigung";
    private static final String SAMPLE_SENDER = "Sender X";
    private final AmazonPollyClient polly;
    private final Voice voice;
    private final ConfigurationWebClient configurationWebClient;


    @Override
    public InputStreamResource synthesize(UUID notificationTypeId, UUID senderId) {
        final NotificationTypeDto notificationType = configurationWebClient.findExistingNotificationType(notificationTypeId);
        final String sender = findSenderName(senderId);
        final String content = notificationType.getTitle();
        return synthesize(content, sender);
    }

    @Override
    public InputStreamResource synthesize(String notificationTitle) {
        return synthesize(SAMPLE_TITLE, SAMPLE_SENDER);
    }

    private String findSenderName(UUID senderId) {
        try {
            return configurationWebClient.findExistingClient(senderId).getName();
        } catch (Exception e) {
            log.error("Could not load sender name for speech synthesis.", e);
        }
        return "";
    }

    private InputStreamResource synthesize(String notificationTitle, String sender) {
        try {
            final StringBuilder content = new StringBuilder(notificationTitle);
            if (sender != null && sender != "") {
                content.append(", ");
                content.append(sender);
            }
            log.debug("Request speech synthesis for content {}", content);
            final SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest()
                    .withText(content.toString())
                    .withVoiceId(voice.getId())
                    .withOutputFormat(OutputFormat.Mp3);
            final SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);
            final InputStream inputStream = synthRes.getAudioStream();

            log.debug("Speech synthesis for content {} successful", content);

            return new InputStreamResource(inputStream);
        } catch (Exception e) {
            log.error("Speech Synthesis request failed.", e);
            throw new PraxisIntercomException(SPEECH_SYNTHESIS_ERROR, e);
        }
    }

}
