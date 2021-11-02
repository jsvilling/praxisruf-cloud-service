package ch.fhnw.ip5.praxiscloudservice.configuration.service.mapper;

import ch.fhnw.ip5.praxiscloudservice.commons.dto.configuration.ClientDto;
import ch.fhnw.ip5.praxiscloudservice.configuration.domain.Client;

public class ClientMapper {

    public static ClientDto toClientDto(Client client) {
        return ClientDto.builder()
                .id(client.getClientId())
                .userId(client.getUserId())
                .name(client.getName())
                .build();
    }

}
