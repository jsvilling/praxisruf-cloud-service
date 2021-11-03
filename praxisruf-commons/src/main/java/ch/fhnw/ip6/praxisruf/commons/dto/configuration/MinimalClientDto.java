package ch.fhnw.ip6.praxisruf.commons.dto.configuration;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
public class MinimalClientDto {
    private UUID id;
    private String name;
}
