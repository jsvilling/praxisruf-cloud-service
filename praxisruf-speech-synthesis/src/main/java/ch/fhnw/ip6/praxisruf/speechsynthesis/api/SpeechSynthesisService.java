package ch.fhnw.ip6.praxisruf.speechsynthesis.api;

import org.springframework.core.io.InputStreamResource;

import java.util.UUID;

/**
 *  Contracts for integrating a speech synthesis provider in Praxisruf.
 *
 *  Any implementation must allow it, to synthesize the content of a notificationType along with the name of a given clientId.
 *  For testing purposes it must also be possible to synthesize a given String message.
 */
public interface SpeechSynthesisService {

    /**
     * Synthesizes the given String as speech data.
     *
     * @return InputStreamResource containing audio data
     */
    InputStreamResource synthesize(String message);

    /**
     * Synthesizes the content of a notificationType along with the name of a given clientId.
     *
     * @param notificationTypeId - Id of the relevant notificationType
     * @param senderId - Id of the relevant client
     *
     * @return InputStreamResource containing audio data
     */
    InputStreamResource synthesize(UUID notificationTypeId, UUID senderId);

}
