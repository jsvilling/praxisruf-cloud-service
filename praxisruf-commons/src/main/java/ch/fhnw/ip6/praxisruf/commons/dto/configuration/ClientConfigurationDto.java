package ch.fhnw.ip6.praxisruf.commons.dto.configuration;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
public class ClientConfigurationDto {

    private UUID id;

    private UUID clientId;

    private String name;

    private List<RuleParametersDto> ruleParameters;

    private Set<UUID> notificationTypes;

    private Set<UUID> callTypes;

}
