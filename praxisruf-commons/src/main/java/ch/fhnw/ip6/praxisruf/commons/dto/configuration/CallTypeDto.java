package ch.fhnw.ip6.praxisruf.commons.dto.configuration;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
public class CallTypeDto {

    private UUID id;

    private String displayText;

    private Set<UUID> participants;

}
