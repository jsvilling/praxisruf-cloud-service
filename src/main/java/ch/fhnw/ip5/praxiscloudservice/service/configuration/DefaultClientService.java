package ch.fhnw.ip5.praxiscloudservice.service.configuration;

import ch.fhnw.ip5.praxiscloudservice.api.ClientService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.MinimalClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.Client;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode.CLIENT_NOT_FOUND;
import static ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper.ClientMapper.toClientDto;

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
        return toClientDto(clientRepository.saveAndFlush(client));
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
        return toClientDto(clientRepository.findById(clientId)
                .orElseThrow(() -> new PraxisIntercomException(CLIENT_NOT_FOUND)));
    }

    @Override
    public ClientDto update(ClientDto clientDto) {
        if(!clientRepository.existsById(clientDto.getId())) {
            throw new PraxisIntercomException(CLIENT_NOT_FOUND);
        }
        final Client client = Client.builder()
                .clientId(clientDto.getId())
                .name(clientDto.getName())
                .userId(clientDto.getUserId())
                .build();
        return (toClientDto(clientRepository.save(client)));
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
                .orElseThrow(() -> new PraxisIntercomException(CLIENT_NOT_FOUND));
    }

}
