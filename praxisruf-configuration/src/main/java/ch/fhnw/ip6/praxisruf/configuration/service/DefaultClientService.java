package ch.fhnw.ip6.praxisruf.configuration.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.MinimalClientDto;
import ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.configuration.api.ClientService;
import ch.fhnw.ip6.praxisruf.configuration.domain.Client;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static ch.fhnw.ip6.praxisruf.configuration.service.mapper.ClientMapper.*;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class DefaultClientService implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public ClientDto create(ClientDto clientDto) {
        final Client client = Client.builder()
                .userId(clientDto.getUserId())
                .name(clientDto.getName())
                .build();
        return toClientDto(clientRepository.saveAndFlush(client));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ClientDto> findAll() {
        final List<Client> clients = clientRepository.findAll();
        return toClientDtos(clients);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto findById(UUID clientId) {
        final Client client = findExisting(clientId);
        return toClientDto(client);
    }

    @Override
    public ClientDto update(ClientDto clientDto) {
        final Client client = findExisting(clientDto.getId());
        client.setName(clientDto.getName());
        client.setUserId(clientDto.getUserId());
        return (toClientDto(client));
    }

    @Override
    public void deleteById(UUID id) {
        try {
            clientRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            log.info("Client with id {} was already deleted", id);
        }
    }

    @Override
    public void deleteAllById(Collection<UUID> clientIds) {
        clientIds.forEach(this::deleteById);
    }

    @Override
    public Set<MinimalClientDto> findByUserId(UUID userId) {
        final Set<Client> clients = clientRepository.findAllByUserId(userId);
        return toMinimalClientDtos(clients);
    }

    private Client findExisting(UUID clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND));
    }

}
