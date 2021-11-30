package ch.fhnw.ip6.praxisruf.configuration.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    @OneToMany(mappedBy = "callType")
    private List<CallGroup> callGroups;

    private Set<UUID> participants;

}
