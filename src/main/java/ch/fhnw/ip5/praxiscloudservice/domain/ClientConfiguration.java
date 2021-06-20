package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

import static org.hibernate.annotations.CascadeType.ALL;

/**
 * The ClientConfiguration contains all information needed to determine which Notifications should be sent
 * to a specific client.
 */
@Entity
@AllArgsConstructor
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

    @OneToMany(mappedBy = "clientConfiguration")
    @Cascade(ALL)
    private Set<RuleParameters> rules;
}
