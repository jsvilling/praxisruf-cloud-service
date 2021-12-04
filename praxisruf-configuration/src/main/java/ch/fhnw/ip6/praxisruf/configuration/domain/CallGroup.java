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
public class CallGroup {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "callGroup")
    private Set<CallType> callGroups = new HashSet<>();

    @ManyToMany
    private List<Client> participants = new ArrayList<>();

}
