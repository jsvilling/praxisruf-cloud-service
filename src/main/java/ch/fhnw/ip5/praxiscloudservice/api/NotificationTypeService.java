package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface NotificationTypeService {

    NotificationTypeDto findById(UUID notificationTypeId);

    Set<NotificationTypeDto> findAll();

    NotificationTypeDto create(NotificationTypeDto notificationTypeDto);

    NotificationTypeDto update(NotificationTypeDto notificationTypeDto);

    void deleteById(UUID notificationTypeId);

    void deleteManyById(Collection<UUID> notificationTypeIds);

}
