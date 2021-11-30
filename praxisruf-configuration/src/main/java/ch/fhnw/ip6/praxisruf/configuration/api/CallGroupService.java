package ch.fhnw.ip6.praxisruf.configuration.api;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.CallGroupDto;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface CallGroupService {

    /**
     * Creates a Client according to the given DTO.
     *
     * @param callGroupDto
     * @throws PraxisIntercomException - If the ClientDto is invalid
     * @return ClientDto
     */
    CallGroupDto create(CallGroupDto callGroupDto);

    /**
     * Finds the client with the given Id.
     *
     * @param callGroupId
     * @throws PraxisIntercomException - If not client with the given id exists
     * @return ClientDto
     */
    CallGroupDto findById(UUID callGroupId);


    /**
     * Finds all Clients.
     *
     * @return Set<ClientDto>
     */
    Set<CallGroupDto> findAll();

    /**
     * Finds all CallGroups
     *
     * @param clientId
     * @return
     */
    Set<CallGroupDto> findAll();


    /**
     * Creates a CallGroup according to the given DTO.
     *
     * The property id of the given CallGroupDto is used to determine which CallGroup will be updated.
     *
     * @param CallGroupDto
     * @throws PraxisIntercomException - If the CallGroupDto is invalid
     * @return CallGroupDto
     */
    CallGroupDto update(CallGroupDto callGroupDto);


    /**
     * Deletes the CallGroupDto with the given id.
     *
     * If no CallGroupDto with the given id is found it is assumed that this CallGroupDto was already deleted.
     * In this case no change will be made and the operation terminates silently.
     *
     * @param id
     */
    void deleteById(UUID callGroupId);

    /**
     * Deletes all CallGroupDto with any of the given callGroupIds.
     *
     * If the deletion of any of the given CallGroupDto fails, the process will be aborted and the exception
     * is re-thrown.
     * @param callGroupIds
     */
    void deleteAllById(Collection<UUID> callGroupIds);
    
}
