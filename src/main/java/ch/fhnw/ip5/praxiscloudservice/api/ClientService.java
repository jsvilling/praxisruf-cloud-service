package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.MinimalClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * This interface specifies interactions with Client entities.
 *
 * @author J. Villing & K. Zellweger
 */
public interface ClientService {

    /**
     * Creates a Client according to the given DTO.
     *
     * @param clientDto
     * @throws PraxisIntercomException - If the ClientDto is invalid
     * @return ClientDto
     */
    ClientDto createClient(ClientDto clientDto);

    /**
     * Finds all Clients.
     *
     * @return Set<ClientDto>
     */
    Set<ClientDto> findAllClients();

    /**
     * Creates a Client according to the given DTO.
     *
     * @param clientDto
     * @throws PraxisIntercomException - If the ClientDto is invalid
     * @return ClientDto
     */
    ClientDto findClientById(UUID clientId);

    /**
     * Creates a Client according to the given DTO.
     *
     * The property id of the given ClientDto is used to determine which Client will be updated.
     *
     * @param clientDto
     * @throws PraxisIntercomException - If the ClientDto is invalid
     * @return ClientDto
     */
    ClientDto updateClient(ClientDto clientDto);

    /**
     * Deletes the client with the given id.
     *
     * If no client with the given id is found it is assumed that this client was already deleted. In this case no
     * change will be made and the operation terminates silently.
     *
     * @param id
     */
    void deleteClientById(UUID id);

    /**
     * Deletes all Clients with any of the given clientIds.
     *
     * If the deletion of any of the given clientConfigurationIds fails, the process will be aborted and the exception
     * is re-thrown.
     * @param clientIds
     */
    void deleteAllById(List<UUID> clientIds);

    /**
     * Finds all Clients that are registered for the given userId.
     *
     * @param userId
     * @return Set<MinimalClientDto>
     */
    Set<MinimalClientDto> findAvailableClients(UUID userId);

    /**
     * Finds all Notification Types that are configured for the client with the given id.
     *
     * @param clientId
     * @throws PraxisIntercomException - If no Client with the given id exists.
     * @return
     */
    List<NotificationTypeDto> findNotificationTypesForClient(UUID clientId);

}
