package ch.fhnw.ip6.praxisruf.commons.dto.configuration;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class CallTypeDto {

    private UUID id;

    private String displayText;

    private UUID callGroupId;

}
