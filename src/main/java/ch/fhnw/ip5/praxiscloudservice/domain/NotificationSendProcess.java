package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Contains the user provided information for a Notification.
 *
 * This includes the values that will be used when actually sending a notification as well as the
 * texts that can be displayed in the client.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor() // for JPA
@Getter
@Setter
@Builder
public class NotificationSendProcess {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID notificationId;

    private String relevantToken;

    private boolean success;
}

