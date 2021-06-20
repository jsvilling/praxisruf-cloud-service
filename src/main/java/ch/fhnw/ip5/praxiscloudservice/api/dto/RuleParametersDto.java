package ch.fhnw.ip5.praxiscloudservice.api.dto;

import ch.fhnw.ip5.praxiscloudservice.domain.RuleType;
import lombok.Getter;

@Getter
public class RuleParametersDto {

    private RuleType ruleType;

    private String value;

}
