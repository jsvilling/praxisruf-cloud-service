package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.MinimalClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ClientService {

    ClientDto createClient(ClientDto clientDto);

    Set<ClientDto> findAllClients();

    ClientDto findClientById(UUID clientId);

    ClientDto updateClient(ClientDto clientDto);

    void deleteClientById(UUID id);

    void deleteAllById(List<UUID> filter);

    Set<MinimalClientDto> findAvailableClients(UUID userId);

    List<NotificationTypeDto> findNotificationTypesForClient(UUID clientId);

}
