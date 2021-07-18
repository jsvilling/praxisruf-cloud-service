package ch.fhnw.ip5.praxiscloudservice.api.dto;

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

}
