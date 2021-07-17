package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * This interface specifies interactions with ClientConfigurations.
 *
 * @author J. Villing
 */
public interface ClientConfigurationService {

    /**
     * Creates a ClientConfiguration according to the given DTO.
     *
     * @param configuratinoDto
     * @throws PraxisIntercomException - If the ClientConfigurationDto is invalid
     * @return ClientConfigurationDto
     */
    ClientConfigurationDto createClientConfiguration(ClientConfigurationDto configuratinoDto);

    /**
     * Finds the ClientConfiguration with the given Id.
     *
     * @param configurationId
     * @throws PraxisIntercomException - If no ClientConfiguration with the given id exists.
     * @return ClientConfigurationDto
     */
    ClientConfigurationDto findClientConfigurationById(UUID configurationId);

    /**
     * Finds all ClientConfigurations.
     *
     * @return Set<ClientConfigurationDto>
     */
    Set<ClientConfigurationDto> findAllClientConfigurations();

    /**
     * Creates a ClientConfiguration according to the given DTO.
     *
     * @param configurationDto
     * @throws PraxisIntercomException - If the ClientConfigurationDto is invalid.
     * @return ClientConfigurationDto
     */
    ClientConfigurationDto updateClientConfiguration(ClientConfigurationDto configurationDto);

    /**
     * Deletes the ClientConfiguration with the given id.
     *
     * @param configurationId
     */
    void deleteClientConfigurationById(UUID configurationId);

    /**
     * Deletes ClientConfigurations with any of the given ids.
     *
     * @param clientConfigurationIds
     */
    void deleteAllClientConfigurationsById(List<UUID> clientConfigurationIds);
}
