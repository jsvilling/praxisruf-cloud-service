package ch.fhnw.ip6.praxisruf.commons.dto.configuration;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class MinimalClientConfigurationDto {

    private Set<NotificationTypeDto> notificationTypes;

    private Set<CallTypeDto> callTypes;

}
