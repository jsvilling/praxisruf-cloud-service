package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * This interface specifies administrating NotificationTypes.
 *
 * @author J. Villing
 */
public interface NotificationTypeService {

    /**
     * Creates a Client according to the given DTO.
     *
     * @param notificationTypeDto
     * @throws PraxisIntercomException - If the ClientDto is invalid
     * @return ClientDto
     */
    NotificationTypeDto create(NotificationTypeDto notificationTypeDto);

    /**
     * Finds the client with the given Id.
     *
     * @param notificationTypeId
     * @throws PraxisIntercomException - If not client with the given id exists
     * @return ClientDto
     */
    NotificationTypeDto findById(UUID notificationTypeId);


    /**
     * Finds all Clients.
     *
     * @return Set<ClientDto>
     */
    Set<NotificationTypeDto> findAll();

    /**
     * Finds all NotificationType that are configured for the client with the given id.
     *
     * @param clientId
     * @return
     */
    Set<NotificationTypeDto> findByClientId(UUID clientId);


    /**
     * Creates a NotificationType according to the given DTO.
     *
     * The property id of the given NotificationTypeDto is used to determine which NotificationType will be updated.
     *
     * @param NotificationTypeDto
     * @throws PraxisIntercomException - If the NotificationTypeDto is invalid
     * @return NotificationTypeDto
     */
    NotificationTypeDto update(NotificationTypeDto notificationTypeDto);


    /**
     * Deletes the NotificationTypeDto with the given id.
     *
     * If no NotificationTypeDto with the given id is found it is assumed that this NotificationTypeDto was already deleted.
     * In this case no change will be made and the operation terminates silently.
     *
     * @param id
     */
    void deleteById(UUID notificationTypeId);

    /**
     * Deletes all NotificationTypeDto with any of the given notificationTypeIds.
     *
     * If the deletion of any of the given NotificationTypeDto fails, the process will be aborted and the exception
     * is re-thrown.
     * @param notificationTypeIds
     */
    void deleteAllById(Collection<UUID> notificationTypeIds);

}
