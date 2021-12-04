package ch.fhnw.ip6.praxisruf.configuration.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE) // for Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
@Setter
@Builder
public class CallType {

    @Id
    @GeneratedValue
    private UUID id;

    private String displayText;

    @ManyToOne
    private CallGroup callGroup;

    @ManyToMany(mappedBy = "callTypes", cascade = CascadeType.DETACH)
    private Set<ClientConfiguration> clientConfigurations = new HashSet<>();

}
