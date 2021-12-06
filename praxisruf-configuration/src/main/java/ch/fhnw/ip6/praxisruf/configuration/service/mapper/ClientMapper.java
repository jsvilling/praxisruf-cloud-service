package ch.fhnw.ip6.praxisruf.configuration.service.mapper;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.MinimalClientDto;
import ch.fhnw.ip6.praxisruf.configuration.domain.Client;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientMapper {

    public static MinimalClientDto toMinimalClientDto(Client client) {
        return MinimalClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .build();
    }

    public static Set<MinimalClientDto> toMinimalClientDtos(Collection<Client> client) {
        return client.stream()
                .map(ClientMapper::toMinimalClientDto)
                .collect(Collectors.toSet());
    }

    public static Client toClient(ClientDto dto) {
        return Client.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .name(dto.getName())
                .build();
    }

    public static Set<ClientDto> toClientDtos(Collection<Client> clients) {
        return clients.stream()
                .map(ClientMapper::toClientDto)
                .collect(Collectors.toSet());
    }

    public static ClientDto toClientDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .userId(client.getUserId())
                .name(client.getName())
                .build();
    }

}
