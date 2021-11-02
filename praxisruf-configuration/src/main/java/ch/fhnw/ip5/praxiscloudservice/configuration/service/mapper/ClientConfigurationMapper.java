package ch.fhnw.ip5.praxiscloudservice.configuration.service.mapper;

import ch.fhnw.ip5.praxiscloudservice.commons.dto.configuration.ClientConfigurationDto;
import ch.fhnw.ip5.praxiscloudservice.configuration.domain.ClientConfiguration;

import java.util.stream.Collectors;

public class ClientConfigurationMapper {

    public static ClientConfigurationDto toClientConfigurationDto(ClientConfiguration configuration) {
        return ClientConfigurationDto.builder()
                .id(configuration.getClientConfigurationId())
                .clientId(configuration.getClient().getClientId())
                .name(configuration.getName())
                .notificationTypes(configuration.getNotificationTypes().stream().map(n -> n.getId()).collect(Collectors.toSet()))
                .ruleParameters(RulesParametersMapper.toRuleParameterDtos(configuration.getRules()))
                .build();
    }
}
