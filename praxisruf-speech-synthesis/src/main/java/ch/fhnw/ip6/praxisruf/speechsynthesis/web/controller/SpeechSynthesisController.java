package ch.fhnw.ip6.praxisruf.speechsynthesis.web.controller;

import ch.fhnw.ip6.praxisruf.speechsynthesis.api.SpeechSynthesisService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/speech")
@Api(tags = "Speech Synthesis")
@AllArgsConstructor
public class SpeechSynthesisController {

    private final SpeechSynthesisService speechSynthesisService;

    @GetMapping(path = "/{id}", produces = "audio/mp3")
    public ResponseEntity synthesizeNotificationType(@PathVariable("id") UUID id) {
        InputStreamResource inputStreamResource = speechSynthesisService.synthesize(id);
        return new ResponseEntity(inputStreamResource, HttpStatus.OK);
    }

}
