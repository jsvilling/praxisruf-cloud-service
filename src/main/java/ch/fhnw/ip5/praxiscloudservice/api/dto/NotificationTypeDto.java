package ch.fhnw.ip5.praxiscloudservice.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class NotificationTypeDto {
    private UUID notificationTypeId;
    private String displayText;
    private String title;
    private String body;
    private String type;
}
