package ch.fhnw.ip5.praxiscloudservice.web.speechsynth;


import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/speechsynthesis")
@AllArgsConstructor
@Api(tags = "Speech")
public class SpeechSynthesisController {

    @GetMapping
    public ResponseEntity getTestAudio() throws IOException {
        File file = ResourceUtils.getFile("classpath:signal.mp3");
        InputStream inputStream = new FileInputStream(file);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(Files.size(file.toPath()));
        return new ResponseEntity(inputStreamResource, headers, HttpStatus.OK);
    }

}
