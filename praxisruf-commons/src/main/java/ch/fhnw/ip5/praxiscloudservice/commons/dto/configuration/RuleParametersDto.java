package ch.fhnw.ip5.praxiscloudservice.commons.dto.configuration;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class RuleParametersDto {

    private UUID id;

    private String ruleType;

    private String value;

}
