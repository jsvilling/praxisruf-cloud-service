package ch.fhnw.ip6.praxisruf.configuration.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
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

    @ManyToMany(mappedBy = "notificationTypes")
    private Set<ClientConfiguration> clientConfigurations = new HashSet<>();

    @Version
    private Long version;

    private boolean textToSpeech;

    public void addClientConfiguration(ClientConfiguration clientConfiguration) {
        clientConfigurations.add(clientConfiguration);
    }

    public void removeClientConfiguration(ClientConfiguration clientConfiguration) {
        if (clientConfigurations != null && clientConfiguration != null) {
            clientConfigurations.remove(clientConfiguration);
        }
    }

}
