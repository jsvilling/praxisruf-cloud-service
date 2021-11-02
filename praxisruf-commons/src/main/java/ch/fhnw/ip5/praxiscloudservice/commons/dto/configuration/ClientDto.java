package ch.fhnw.ip5.praxiscloudservice.commons.dto.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ClientDto {
    private UUID id;
    private String name;
    private UUID userId;
}
