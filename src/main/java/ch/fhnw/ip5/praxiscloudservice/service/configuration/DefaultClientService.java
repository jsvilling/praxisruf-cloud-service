package ch.fhnw.ip5.praxiscloudservice.service.configuration;

import ch.fhnw.ip5.praxiscloudservice.api.ClientService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.MinimalClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.Client;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.NotificationTypeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper.ClientMapper.toClientDto;
import static ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper.NotificationTypesMapper.toNotificationTypeDtos;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultClientService implements ClientService {

    private final ClientRepository clientRepository;
    private final NotificationTypeRepository notificationTypeRepository;

    @Override
    public ClientDto createClient(ClientDto clientDto) {
        final Client client = Client.builder()
                .userId(clientDto.getUserId())
                .name(clientDto.getName())
                .build();
        return toClientDto(clientRepository.saveAndFlush(client));
    }

    @Override
    public Set<ClientDto> findAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(c -> ClientDto.builder().id(c.getClientId()).name(c.getName()).userId(c.getUserId())
                        .build())
                .collect(Collectors.toSet());
    }

    @Override
    public ClientDto findClientById(UUID clientId) {
        return toClientDto(clientRepository.findById(clientId)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND)));
    }

    @Override
    public ClientDto updateClient(ClientDto clientDto) {
        Client client = clientRepository.findById(clientDto.getId())
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND));
        client = clientRepository.saveAndFlush(
                Client.builder()
                        .clientId(client.getClientId())
                        .name(clientDto.getName())
                        .userId(clientDto.getUserId())
                        .build());
        return toClientDto(client);
    }

    @Override
    public void deleteClientById(UUID id) {
        try {
            clientRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            log.info("Client with id {} is already deleted", id);
        }
    }

    @Override
    public void deleteAllById(List<UUID> filter) {
        filter.forEach(this::deleteClientById);
    }

    @Override
    public Set<MinimalClientDto> findAvailableClients(UUID userId) {
        return clientRepository.findAllByUserId(userId)
                .stream()
                .map(c -> MinimalClientDto.builder().id(c.getClientId()).name(c.getName()).build())
                .collect(Collectors.toSet());
    }

    @Override
    public List<NotificationTypeDto> findNotificationTypesForClient(UUID clientId) {
        return toNotificationTypeDtos(notificationTypeRepository.findAll());
    }

}
