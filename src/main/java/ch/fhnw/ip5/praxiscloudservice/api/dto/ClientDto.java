package ch.fhnw.ip5.praxiscloudservice.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ClientDto {
    private UUID id;
    private String name;
    private UUID userId;
}
