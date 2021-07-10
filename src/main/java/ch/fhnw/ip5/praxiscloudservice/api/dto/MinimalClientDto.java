package ch.fhnw.ip5.praxiscloudservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class MinimalClientDto {
    private UUID id;
    private String name;
}
