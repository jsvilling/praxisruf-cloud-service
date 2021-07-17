package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;

import java.util.Set;
import java.util.UUID;

public interface RegistrationService {

    void register(UUID clientId, String fcmToken);

    void unregister(UUID clientId);

    Set<String> getAllKnownTokens();

    Set<String> findAllRelevantTokens(PraxisNotification notification);

}
