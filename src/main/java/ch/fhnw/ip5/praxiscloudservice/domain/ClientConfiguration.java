package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * The ClientConfiguration contains all information needed to determine which Notifications should be sent
 * to a specific client.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
public class ClientConfiguration {

    @Id
    private UUID clientConfigurationId;

    private String name;

    @OneToOne
    @JoinColumn(name = "clientId")
    private Client client;

    @OneToMany(mappedBy = "clientConfiguration")
    private Set<RuleConfig> rules;
}
