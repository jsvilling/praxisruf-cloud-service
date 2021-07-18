package ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper;

import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;
import ch.fhnw.ip5.praxiscloudservice.domain.NotificationType;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class NotificationTypesMapper {

    public static Set<NotificationTypeDto> toNotificationTypeDtos(Collection<NotificationType> notificationTypes) {
        return notificationTypes.stream()
                .map(NotificationTypesMapper::toNotificationTypeDto)
                .collect(Collectors.toSet());
    }

    public static NotificationTypeDto toNotificationTypeDto(NotificationType notificationType) {
        return NotificationTypeDto.builder().id(notificationType.getId())
                .body(notificationType.getBody())
                .title(notificationType.getTitle())
                .description(notificationType.getTitle())
                .displayText(notificationType.getDisplayText())
                .build();
    }

    public static Set<NotificationType> toNotificationTypes(Collection<NotificationTypeDto> dtos) {
        return dtos.stream()
                .map(NotificationTypesMapper::toNotificationType)
                .collect(Collectors.toSet());
    }

    public static NotificationType toNotificationType(NotificationTypeDto dto) {
        return NotificationType.builder()
                .id(dto.getId())
                .body(dto.getBody())
                .type(dto.getDescription())
                .title(dto.getTitle())
                .displayText(dto.getDisplayText())
                .build();
    }

}
