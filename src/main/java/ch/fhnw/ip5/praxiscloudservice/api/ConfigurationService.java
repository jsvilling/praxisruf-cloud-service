package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.MinimalClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;

import java.util.List;
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

    Set<MinimalClientDto> findAvailableClients(UUID userId);

    void createClientConfiguration(ClientConfigurationDto configuratinoDto);

    UUID createClient(UUID userId, String clientName);

    List<NotificationTypeDto> findNotificationTypesForClient(UUID clientId);

}
