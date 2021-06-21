package ch.fhnw.ip5.praxiscloudservice.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class MinimalClientDto {
    private UUID id;
    private String name;
}