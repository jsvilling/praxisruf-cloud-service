package ch.fhnw.ip6.praxisruf.notification.api;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.RegistrationDto;

import java.util.List;
import java.util.UUID;

public interface NotificationSendProcessService {

    /**
     * Creates an entry in the NotificationSendProcess Table
     *
     * @param notificationId
     * @param success
     * @param registration
     */
    void createNotificationSendLogEntry(UUID notificationId, boolean success, RegistrationDto registration);

    /**
     * Finds all NotificationSendProcess Entries that belong to the given
     * notificationId and have state failed. The found entries are then mapped to RegistrationDtos.
     *
     * @param notificationId
     * @return List<RegistrationDto>
     */
    List<RegistrationDto> findAllRegistrationsForFailed(UUID notificationId);
}
