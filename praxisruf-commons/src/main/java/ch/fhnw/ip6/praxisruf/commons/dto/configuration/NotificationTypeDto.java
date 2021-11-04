package ch.fhnw.ip6.praxisruf.commons.dto.configuration;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class NotificationTypeDto {
    private UUID id;
    private String displayText;
    private String title;
    private String body;
    private String description;
    private Long version;
    private Boolean textToSpeech;
}
