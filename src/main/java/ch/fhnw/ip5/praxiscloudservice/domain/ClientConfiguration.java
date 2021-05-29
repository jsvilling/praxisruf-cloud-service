package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
public class ClientConfiguration {

    @Id
    private UUID clientConfigurationId;

    private String name;

    private UUID clientId;

    @OneToMany(mappedBy = "clientConfiguration")
    private Set<RuleConfig> rules;
}
