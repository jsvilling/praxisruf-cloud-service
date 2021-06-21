package ch.fhnw.ip5.praxiscloudservice.api.dto;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class ClientConfigurationDto {

    private UUID clientId;

    private String name;

    private List<RuleParametersDto> ruleParameters;

    private List<NotificationTypeDto> notificationTypes;

}
