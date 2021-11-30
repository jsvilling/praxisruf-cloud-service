package ch.fhnw.ip6.praxisruf.commons.dto.configuration;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
@Builder
public class CallGroupDto {

    private UUID id;

    private String name;

    private Set<UUID> participants;

}
