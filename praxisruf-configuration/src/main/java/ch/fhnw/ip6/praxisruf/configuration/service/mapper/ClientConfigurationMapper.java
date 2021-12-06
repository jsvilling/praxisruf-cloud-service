package ch.fhnw.ip6.praxisruf.configuration.service.mapper;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.CallTypeDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientConfigurationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.DisplayClientConfigurationDto;
import ch.fhnw.ip6.praxisruf.configuration.domain.CallType;
import ch.fhnw.ip6.praxisruf.configuration.domain.ClientConfiguration;
import ch.fhnw.ip6.praxisruf.configuration.domain.NotificationType;

import java.util.Collection;
import java.util.Set;
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

    public static Set<ClientConfigurationDto> toClientConfigurationDtos(Collection<ClientConfiguration> configurations) {
        return configurations.stream()
                .map(ClientConfigurationMapper::toClientConfigurationDto)
                .collect(Collectors.toSet());
    }

    public static DisplayClientConfigurationDto toDisplayClientConfigurationDto(ClientConfiguration configuration) {
        return DisplayClientConfigurationDto.builder()
                .notificationTypes(configuration.getNotificationTypes().stream().map(NotificationTypesMapper::toNotificationTypeDto).collect(Collectors.toSet()))
                .callTypes(configuration.getCallTypes().stream().map(ClientConfigurationMapper::toCallTypeDto).collect(Collectors.toSet()))
                .build();
    }

    private static CallTypeDto toCallTypeDto(CallType callType) {
        return CallTypeDto.builder()
                .id(callType.getId())
                .displayText(callType.getDisplayText())
                .callGroup(callType.getCallGroup().getId())
                .build();
    }
}
