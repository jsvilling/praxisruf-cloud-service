package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
public class NotificationType {

    @Id
    private UUID id;

    private String displayText;

    private String title;

    private String body;

    private String type;
}