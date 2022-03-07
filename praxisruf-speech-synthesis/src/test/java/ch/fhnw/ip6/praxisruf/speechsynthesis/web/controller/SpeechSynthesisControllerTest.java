package ch.fhnw.ip6.praxisruf.speechsynthesis.web.controller;

import ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.speechsynthesis.api.SpeechSynthesisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SpeechSynthesisControllerTest {

    private static final UUID NOTIFICATION_TYPE_ID = UUID.randomUUID();
    private static final UUID SENDER_ID = UUID.randomUUID();

    @Mock
    private SpeechSynthesisService speechSynthesisService;

    @InjectMocks
    SpeechSynthesisController speechSynthesisController;

    @Test
    void synthesize_isDelegatedToService_success() {
        // Given
        final InputStreamResource speechData = Mockito.mock(InputStreamResource.class);
        when(speechSynthesisService.synthesize(NOTIFICATION_TYPE_ID, SENDER_ID)).thenReturn(speechData);

        // When
        final ResponseEntity response = speechSynthesisController.synthesizeNotificationType(NOTIFICATION_TYPE_ID, SENDER_ID);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(speechData);
    }

    @Test
    void synthesize_isExceptionRethrown() {
        // Given
        when(speechSynthesisService.synthesize(NOTIFICATION_TYPE_ID, SENDER_ID)).thenThrow(new PraxisIntercomException(ErrorCode.SPEECH_SYNTHESIS_ERROR));

        // When
        // Then
        assertThatThrownBy(() -> speechSynthesisController.synthesizeNotificationType(NOTIFICATION_TYPE_ID, SENDER_ID))
                .isInstanceOf(PraxisIntercomException.class);
    }

}
