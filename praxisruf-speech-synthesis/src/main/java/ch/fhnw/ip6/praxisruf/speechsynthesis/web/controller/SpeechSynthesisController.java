package ch.fhnw.ip6.praxisruf.speechsynthesis.web.controller;

import ch.fhnw.ip6.praxisruf.speechsynthesis.api.SpeechSynthesisService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/speech")
@Api(tags = "Speech Synthesis")
@AllArgsConstructor
public class SpeechSynthesisController {

    private final SpeechSynthesisService speechSynthesisService;

    @GetMapping(path = "/{id}", produces = "audio/mp3")
    public ResponseEntity synthesizeNotificationType(@PathVariable("id") UUID notificationTypeId, @RequestParam(value = "sender", required = false) UUID senderId) {
        final InputStreamResource inputStreamResource = speechSynthesisService.synthesize(notificationTypeId, senderId);
        return new ResponseEntity(inputStreamResource, HttpStatus.OK);
    }

}
