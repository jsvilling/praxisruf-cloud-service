package ch.fhnw.ip6.praxisruf.configuration.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * The ClientConfiguration contains all information needed to determine which Notifications should be sent
 * to a specific client.
 */
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE) // for Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
@Setter
@Builder
public class ClientConfiguration {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @OneToOne
    @JoinColumn(name = "clientId")
    private Client client;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "client_configuration_id")
    private Set<RuleParameters> rules;

    @ManyToMany
    private Set<NotificationType> notificationTypes = new HashSet<>();

    @ManyToMany
    private Set<CallType> callTypes = new HashSet<>();

    public void removeNotificationType(NotificationType notificationType) {
        notificationTypes.remove(notificationType);
    }

}
