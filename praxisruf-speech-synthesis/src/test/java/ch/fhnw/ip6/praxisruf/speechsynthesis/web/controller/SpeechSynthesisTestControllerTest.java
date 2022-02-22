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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpeechSynthesisTestControllerTest {

    @Mock
    private SpeechSynthesisService speechSynthesisService;

    @InjectMocks
    private SpeechSynthesisTestController speechSynthesisTestController;

    @org.junit.jupiter.api.Test
    void synthesize_isDelegatedToService_success() {
        // Given
        final InputStreamResource speechData = Mockito.mock(InputStreamResource.class);
        when(speechSynthesisService.synthesize(anyString())).thenReturn(speechData);

        // When
        final ResponseEntity response = speechSynthesisTestController.synthesizeTestAudio();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(speechData);
    }

    @Test
    void synthesize_isExceptionRethrown() {
        // Given
        when(speechSynthesisService.synthesize(anyString())).thenThrow(new PraxisIntercomException(ErrorCode.SPEECH_SYNTHESIS_ERROR));

        // When
        // Then
        assertThatThrownBy(() -> speechSynthesisTestController.synthesizeTestAudio())
                .isInstanceOf(PraxisIntercomException.class);
    }

}
