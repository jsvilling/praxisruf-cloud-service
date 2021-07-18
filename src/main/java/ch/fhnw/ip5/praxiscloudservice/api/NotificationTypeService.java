package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface NotificationTypeService {

    NotificationTypeDto findById(UUID notificationTypeId);

    Set<NotificationTypeDto> findAll();

    /**
     * Finds all Notification Types that are configured for the client with the given id.
     *
     * @param clientId
     * @throws PraxisIntercomException - If no Client with the given id exists.
     * @return
     */
    Set<NotificationTypeDto> findNotificationTypesForClient(UUID clientId);

    NotificationTypeDto create(NotificationTypeDto notificationTypeDto);

    NotificationTypeDto update(NotificationTypeDto notificationTypeDto);

    void deleteById(UUID notificationTypeId);

    void deleteManyById(Collection<UUID> notificationTypeIds);

}
