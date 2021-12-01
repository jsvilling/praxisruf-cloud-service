package ch.fhnw.ip6.praxisruf.configuration.api;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.NotificationTypeDto;

import java.util.Set;
import java.util.UUID;

/**
 * This interface specifies administrating NotificationTypes.
 *
 * @author J. Villing
 */
public interface NotificationTypeService extends ConfigurationCrudService<NotificationTypeDto, UUID> {

    /**
     * Finds all NotificationType that are configured for the client with the given id.
     *
     * @param clientId
     * @return
     */
    Set<NotificationTypeDto> findByClientId(UUID clientId);

}
