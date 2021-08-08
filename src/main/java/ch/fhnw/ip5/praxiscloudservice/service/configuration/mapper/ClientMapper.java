package ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper;

import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientDto;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.Client;

public class ClientMapper {

    public static ClientDto toClientDto(Client client) {
        return ClientDto.builder()
                .id(client.getClientId())
                .userId(client.getUserId())
                .name(client.getName())
                .build();
    }

}
