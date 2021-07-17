package ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper;

import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;
import ch.fhnw.ip5.praxiscloudservice.domain.NotificationType;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NotificationTypesMapper {

    public static List<NotificationTypeDto> toNotificationTypeDtos(Collection<NotificationType> notificationTypes) {
        return notificationTypes.stream()
                .map(NotificationTypesMapper::toNotificationTypeDto)
                .collect(Collectors.toList());
    }

    public static NotificationTypeDto toNotificationTypeDto(NotificationType notificationType) {
        return NotificationTypeDto.builder().id(notificationType.getId())
                .body(notificationType.getBody())
                .title(notificationType.getTitle())
                .type(notificationType.getTitle())
                .displayText(notificationType.getDisplayText())
                .build();
    }

    public static Set<NotificationType> toNotificationTypes(List<NotificationTypeDto> dtos) {
        return dtos.stream().map(dto -> NotificationType.builder()
                .body(dto.getBody())
                .type(dto.getType())
                .title(dto.getTitle())
                .displayText(dto.getDisplayText())
                .build()
        ).collect(Collectors.toSet());
    }

}
