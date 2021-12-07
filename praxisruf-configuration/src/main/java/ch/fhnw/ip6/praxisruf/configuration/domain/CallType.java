package ch.fhnw.ip6.praxisruf.configuration.domain;

import lombok.*;

import javax.persistence.*;
import java.util.*;

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

    @ManyToMany
    private List<Client> participants = new ArrayList<>();

    @ManyToMany(mappedBy = "callTypes", cascade = CascadeType.DETACH)
    private Set<ClientConfiguration> clientConfigurations = new HashSet<>();

}
