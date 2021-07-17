package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * This interface specifies contracts retrieving the configuration of client devices.
 *
 * @author J. Villing
 */
public interface ClientConfigurationService {

    ClientConfigurationDto createClientConfiguration(ClientConfigurationDto configuratinoDto);

    ClientConfigurationDto findClientConfigurationById(UUID configurationId);

    Set<ClientConfigurationDto> findAllClientConfigurations();

    ClientConfigurationDto updateClientConfiguration(ClientConfigurationDto configurationDto);

    void deleteClientConfigurationById(UUID configurationId);

    void deleteAllClientConfigurationsById(List<UUID> filter);
}
