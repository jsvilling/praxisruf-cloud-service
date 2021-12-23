package ch.fhnw.ip6.praxisruf.configuration.api;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.CallTypeDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.MinimalClientDto;

import java.util.Set;
import java.util.UUID;

public interface CallTypeService extends ConfigurationCrudService<CallTypeDto, UUID> {

    Set<MinimalClientDto> findParticipants(UUID callTypeId);

}
