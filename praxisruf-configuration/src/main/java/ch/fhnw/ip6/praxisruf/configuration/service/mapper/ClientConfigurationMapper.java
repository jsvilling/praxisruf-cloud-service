package ch.fhnw.ip6.praxisruf.configuration.service.mapper;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientConfigurationDto;
import ch.fhnw.ip6.praxisruf.configuration.domain.CallType;
import ch.fhnw.ip6.praxisruf.configuration.domain.ClientConfiguration;
import ch.fhnw.ip6.praxisruf.configuration.domain.NotificationType;

import java.util.stream.Collectors;

public class ClientConfigurationMapper {

    public static ClientConfigurationDto toClientConfigurationDto(ClientConfiguration configuration) {
        return ClientConfigurationDto.builder()
                .id(configuration.getClientConfigurationId())
                .clientId(configuration.getClient().getClientId())
                .name(configuration.getName())
                .notificationTypes(configuration.getNotificationTypes().stream().map(NotificationType::getId).collect(Collectors.toSet()))
                .ruleParameters(RulesParametersMapper.toRuleParameterDtos(configuration.getRules()))
                .callTypes(configuration.getCallTypes().stream().map(CallType::getId).collect(Collectors.toSet()))
                .build();
    }
}
