package ch.fhnw.ip6.praxisruf.configuration.api;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.MinimalClientDto;

import java.util.Set;
import java.util.UUID;

/**
 * This interface specifies interactions with Client entities.
 *
 * @author J. Villing
 */
public interface ClientService extends ConfigurationCrudService<ClientDto, UUID> {

    /**
     * Finds all Clients that are registered for the given userId.
     *
     * @param userId
     * @return Set<MinimalClientDto>
     */
    Set<MinimalClientDto> findByUserId(UUID userId);

}
