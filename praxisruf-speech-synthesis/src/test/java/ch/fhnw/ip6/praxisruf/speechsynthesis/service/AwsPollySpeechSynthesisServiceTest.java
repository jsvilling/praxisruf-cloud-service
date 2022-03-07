package ch.fhnw.ip6.praxisruf.speechsynthesis.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.NotificationTypeDto;
import ch.fhnw.ip6.praxisruf.commons.web.client.ConfigurationWebClient;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AwsPollySpeechSynthesisServiceTest {

    private static final String CONTENT = "content";
    private static final UUID NOTIFICATION_TYPE_ID = UUID.randomUUID();
    private static final UUID SENDER_ID = UUID.randomUUID();
    private static final String VOICE_ID = UUID.randomUUID().toString();

    @Mock
    private SynthesizeSpeechResult synthesizeSpeechResult;

    @Mock
    private InputStream inputStream;

    @Mock
    private AmazonPollyClient polly;

    @Mock
    private Voice voice;

    @Mock
    private ConfigurationWebClient configurationWebClient;

    @InjectMocks
    private AwsPollySpeechSynthesisService service;

    @Test
    void synthesize_text() throws IOException {
        // Given
        when(voice.getId()).thenReturn(VOICE_ID);
        when(polly.synthesizeSpeech(any())).thenReturn(synthesizeSpeechResult);
        when(synthesizeSpeechResult.getAudioStream()).thenReturn(inputStream);

        // When
        final InputStreamResource result = service.synthesize(CONTENT);

        // Then
        verify(polly).synthesizeSpeech(any());
        final InputStream resultInputStream = result.getInputStream();
        Assertions.assertThat(resultInputStream).isSameAs(this.inputStream);
    }

    @Test
    void synthesize_notificationTypeId() throws IOException {
        // Given
        final NotificationTypeDto notificationTypeDto = createNotificationTypeDto();
        when(configurationWebClient.findExistingNotificationType(NOTIFICATION_TYPE_ID)).thenReturn(notificationTypeDto);
        when(voice.getId()).thenReturn(VOICE_ID);
        when(polly.synthesizeSpeech(any())).thenReturn(synthesizeSpeechResult);
        when(synthesizeSpeechResult.getAudioStream()).thenReturn(inputStream);

        // When
        final InputStreamResource result = service.synthesize(NOTIFICATION_TYPE_ID, SENDER_ID);

        // Then
        verify(polly).synthesizeSpeech(any());
        final InputStream resultInputStream = result.getInputStream();
        Assertions.assertThat(resultInputStream).isSameAs(this.inputStream);
    }


    private static NotificationTypeDto createNotificationTypeDto() {
        return NotificationTypeDto.builder()
                .id(randomUUID())
                .description("")
                .body("")
                .title("")
                .displayText(CONTENT)
                .build();
    }


}
