package ch.fhnw.ip6.praxisruf.speechsynthesis.api;

import org.springframework.core.io.InputStreamResource;

import java.util.UUID;

public interface SpeechSynthesisService {

    InputStreamResource synthesize(String message);

    InputStreamResource synthesize(UUID notificationTypeId, UUID senderId);

}
