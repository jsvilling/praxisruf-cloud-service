package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.*;

import javax.persistence.*;
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
@Builder
public class ClientConfiguration {

    @Id
    @GeneratedValue
    private UUID clientConfigurationId;

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

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "client_configuration_id")
    private Set<NotificationType> notificationTypes;
}
