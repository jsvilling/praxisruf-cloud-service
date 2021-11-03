package ch.fhnw.ip6.praxisruf.speechsynthesis.web;

import ch.fhnw.ip6.praxisruf.speechsynthesis.api.SpeechSynthesisService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/speechsynthesis")
@Api(tags = "Speech Synthesis Test")
@AllArgsConstructor
public class SpeechSynthesisTestController {

    private final SpeechSynthesisService speechSynthesisService;

    @GetMapping(produces = "audio/mp3")
    public ResponseEntity synthesizeTestAudio() {
        InputStreamResource inputStreamResource = speechSynthesisService.test();
        return new ResponseEntity(inputStreamResource, HttpStatus.OK);
    }

}
