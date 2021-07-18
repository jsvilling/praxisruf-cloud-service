package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;
import java.util.UUID;

/**
 * Contains the user provided information for a Notification.
 *
 * This includes the values that will be used when actually sending a notification as well as the
 * texts that can be displayed in the client.
 */
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE) // for Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
@Setter
@Builder
public class NotificationType {

    @Id
    @GeneratedValue
    private UUID id;

    private String displayText;

    private String title;

    private String body;

    private String type;

    @ManyToMany
    private Set<ClientConfiguration> clientConfigurations;

    public void addClientConfiguration(ClientConfiguration clientConfiguration) {
        clientConfigurations.add(clientConfiguration);
    }

}
