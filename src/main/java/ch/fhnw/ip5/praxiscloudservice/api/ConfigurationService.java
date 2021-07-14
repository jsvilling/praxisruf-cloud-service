package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.MinimalClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * This interface specifies contracts retrieving the configuration of client devices.
 *
 * @author J. Villing
 */
public interface ConfigurationService {

    Set<MinimalClientDto> findAvailableClients(UUID userId);

    ClientConfigurationDto createClientConfiguration(ClientConfigurationDto configuratinoDto);

    ClientDto createClient(ClientDto clientDto);

    List<NotificationTypeDto> findNotificationTypesForClient(UUID clientId);

    Set<ClientDto> findAllClients();

    ClientDto findClientById(UUID clientId);

    ClientDto updateClient(ClientDto clientDto);

    void deleteClientById(UUID id);

    void deleteAllById(List<UUID> filter);

    ClientConfigurationDto findClientConfigurationById(UUID configurationId);

    Set<ClientConfigurationDto> findAllClientConfigurations();

    ClientConfigurationDto updateClientConfiguration(ClientConfigurationDto configurationDto);

    void deleteClientConfigurationById(UUID configurationId);

    void deleteAllConfigurationsById(List<UUID> filter);
}
