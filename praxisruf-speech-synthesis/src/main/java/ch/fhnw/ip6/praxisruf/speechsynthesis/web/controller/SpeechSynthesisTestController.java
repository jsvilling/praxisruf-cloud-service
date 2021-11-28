package ch.fhnw.ip6.praxisruf.speechsynthesis.web.controller;

import ch.fhnw.ip6.praxisruf.commons.config.ProfileRegistry;
import ch.fhnw.ip6.praxisruf.speechsynthesis.api.SpeechSynthesisService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/speechsynthesis")
@Api(tags = "Test")
@Profile(ProfileRegistry.TEST)
@AllArgsConstructor
public class SpeechSynthesisTestController {

    private static final String SAMPLE = "Benachrichtigung empfangen";

    private final SpeechSynthesisService speechSynthesisService;

    @GetMapping(produces = "audio/mp3")
    public ResponseEntity<InputStreamResource> synthesizeTestAudio() {
        InputStreamResource inputStreamResource = speechSynthesisService.synthesize(SAMPLE);
        return new ResponseEntity(inputStreamResource, HttpStatus.OK);
    }

}
