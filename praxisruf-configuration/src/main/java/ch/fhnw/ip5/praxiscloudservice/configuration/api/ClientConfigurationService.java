package ch.fhnw.ip5.praxiscloudservice.configuration.api;

import ch.fhnw.ip5.praxiscloudservice.commons.dto.configuration.ClientConfigurationDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * This interface specifies interactions with ClientConfigurations.
 *
 * @author J. Villing & K. Zellweger
 */
public interface ClientConfigurationService {

    /**
     * Creates a ClientConfiguration according to the given DTO.
     *
     * @param configuratinoDto
     * @throws ch.fhnw.ip5.praxiscloudservice.commons.exception.PraxisIntercomException - If the ClientConfigurationDto is invalid
     * @return ClientConfigurationDto
     */
    ClientConfigurationDto create(ClientConfigurationDto configuratinoDto);

    /**
     * Finds the ClientConfiguration with the given Id.
     *
     * @param configurationId
     * @throws ch.fhnw.ip5.praxiscloudservice.commons.exception.PraxisIntercomException - If no ClientConfiguration with the given id exists.
     * @return ClientConfigurationDto
     */
    ClientConfigurationDto findById(UUID configurationId);

    /**
     * Finds all ClientConfigurations.
     *
     * @return Set<ClientConfigurationDto>
     */
    Set<ClientConfigurationDto> findAll();

    /**
     * Updates a ClientConfiguration according to the given DTO.
     *
     * The property id of the given ClientConfigurationDto is used to determine which Client will be updated.
     *
     * @param configurationDto
     * @throws ch.fhnw.ip5.praxiscloudservice.commons.exception.PraxisIntercomException - If the ClientConfigurationDto is invalid.
     * @return ClientConfigurationDto
     */
    ClientConfigurationDto update(ClientConfigurationDto configurationDto);

    /**
     * Deletes the ClientConfiguration with the given id.
     *
     * If no ClientConfiguration with the given id is found it is assumed that this client was already deleted.
     * In this case no change will be made and the operation terminates silently.
     *
     * @param configurationId
     */
    void deleteById(UUID configurationId);

    /**
     * Deletes ClientConfigurations with any of the given ids.
     *
     * If the deletion of any of the given clientConfigurationIds fails, the process will be aborted and the exception
     * is re-thrown.
     *
     * @param clientConfigurationIds
     */
    void deleteAllById(List<UUID> clientConfigurationIds);
}
