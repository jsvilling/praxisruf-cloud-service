package ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper;

import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.ClientConfiguration;

import java.util.stream.Collectors;

import static ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper.RulesParametersMapper.toRuleParameterDtos;

public class ClientConfigurationMapper {

    public static ClientConfigurationDto toClientConfigurationDto(ClientConfiguration configuration) {
        return ClientConfigurationDto.builder()
                .id(configuration.getClientConfigurationId())
                .clientId(configuration.getClient().getClientId())
                .name(configuration.getName())
                .notificationTypes(configuration.getNotificationTypes().stream().map(n -> n.getId()).collect(Collectors.toSet()))
                .ruleParameters(toRuleParameterDtos(configuration.getRules()))
                .build();
    }
}
