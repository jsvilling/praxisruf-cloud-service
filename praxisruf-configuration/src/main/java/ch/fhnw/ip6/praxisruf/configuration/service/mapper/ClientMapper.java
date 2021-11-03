package ch.fhnw.ip6.praxisruf.configuration.service.mapper;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientDto;
import ch.fhnw.ip6.praxisruf.configuration.domain.Client;

public class ClientMapper {

    public static ClientDto toClientDto(Client client) {
        return ClientDto.builder()
                .id(client.getClientId())
                .userId(client.getUserId())
                .name(client.getName())
                .build();
    }

}
