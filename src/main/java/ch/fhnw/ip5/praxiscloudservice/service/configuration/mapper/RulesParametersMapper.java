package ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper;

import ch.fhnw.ip5.praxiscloudservice.api.dto.RuleParametersDto;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.RuleParameters;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RulesParametersMapper {

    public static List<RuleParametersDto> toRuleParameterDtos(Collection<RuleParameters> rulesParameters) {
        return rulesParameters.stream()
                .map(RulesParametersMapper::toRuleParameterDto)
                .collect(Collectors.toList());
    }

    public static RuleParametersDto toRuleParameterDto(RuleParameters ruleParameters) {
        return RuleParametersDto.builder()
                .id(ruleParameters.getRuleParametersId())
                .ruleType(ruleParameters.getType())
                .value(ruleParameters.getValue())
                .build();
    }

    public static Set<RuleParameters> toRuleParameters(Collection<RuleParametersDto> dtos) {
        return dtos.stream()
                .map(RulesParametersMapper::toRuleParameters)
                .collect(Collectors.toSet());
    }

    public static RuleParameters toRuleParameters(RuleParametersDto dto) {
        return RuleParameters.builder()
                .type(dto.getRuleType())
                .value(dto.getValue())
                .build();
    }


}
