package ch.fhnw.ip5.praxiscloudservice.configuration.service;

import ch.fhnw.ip5.praxiscloudservice.commons.dto.configuration.ClientDto;
import ch.fhnw.ip5.praxiscloudservice.commons.dto.configuration.MinimalClientDto;
import ch.fhnw.ip5.praxiscloudservice.commons.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.commons.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.configuration.api.ClientService;
import ch.fhnw.ip5.praxiscloudservice.configuration.domain.Client;
import ch.fhnw.ip5.praxiscloudservice.configuration.persistence.ClientRepository;
import ch.fhnw.ip5.praxiscloudservice.configuration.service.mapper.ClientMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultClientService implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public ClientDto create(ClientDto clientDto) {
        final Client client = Client.builder()
                .userId(clientDto.getUserId())
                .name(clientDto.getName())
                .build();
        return ClientMapper.toClientDto(clientRepository.saveAndFlush(client));
    }

    @Override
    public Set<ClientDto> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(c -> ClientDto.builder().id(c.getClientId()).name(c.getName()).userId(c.getUserId())
                        .build())
                .collect(Collectors.toSet());
    }

    @Override
    public ClientDto findById(UUID clientId) {
        return ClientMapper.toClientDto(clientRepository.findById(clientId)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND)));
    }

    @Override
    public ClientDto update(ClientDto clientDto) {
        if(!clientRepository.existsById(clientDto.getId())) {
            throw new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND);
        }
        final Client client = Client.builder()
                .clientId(clientDto.getId())
                .name(clientDto.getName())
                .userId(clientDto.getUserId())
                .build();
        return (ClientMapper.toClientDto(clientRepository.save(client)));
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
    public void deleteAllById(List<UUID> clientIds) {
        clientIds.forEach(this::deleteById);
    }

    @Override
    public Set<MinimalClientDto> findByUserId(UUID userId) {
        return clientRepository.findAllByUserId(userId)
                .stream()
                .map(c -> MinimalClientDto.builder().id(c.getClientId()).name(c.getName()).build())
                .collect(Collectors.toSet());
    }

    private Client findExistingClient(UUID clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CLIENT_NOT_FOUND));
    }

}
