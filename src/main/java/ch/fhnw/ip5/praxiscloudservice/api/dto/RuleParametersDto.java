package ch.fhnw.ip5.praxiscloudservice.api.dto;

import ch.fhnw.ip5.praxiscloudservice.domain.configuration.RuleType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class RuleParametersDto {

    private UUID id;

    private RuleType ruleType;

    private String value;

}
