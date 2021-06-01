package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import org.springframework.data.util.Pair;

import java.util.Set;
import java.util.UUID;

/**
 * This interface specifies contracts retrieving the configuration of client devices.
 *
 * @author J. Villing
 */
public interface ConfigurationService {

    void register(UUID clientId, String fcmToken);

    void unregister(UUID clientId);

    Set<String> getAllKnownTokens();

    Set<String> findAllRelevantTokens(PraxisNotification notification);

    Set<Pair<String, UUID>> findAvailableClients(UUID userId);

    void createClientConfiguration(UUID userId, UUID clientId, String name);

}
