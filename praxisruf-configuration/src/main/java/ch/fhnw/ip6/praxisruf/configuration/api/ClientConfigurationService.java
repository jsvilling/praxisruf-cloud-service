package ch.fhnw.ip6.praxisruf.configuration.api;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientConfigurationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.DisplayClientConfigurationDto;

import java.util.UUID;

/**
 * This interface specifies interactions with ClientConfigurations.
 *
 * @author J. Villing
 */
public interface ClientConfigurationService extends ConfigurationCrudService<ClientConfigurationDto, UUID> {

    DisplayClientConfigurationDto findByClientId(UUID clientId);

}
