package ch.fhnw.ip6.praxisruf.configuration.domain;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

/**
 * A RuleConfig contains the parameterized values that can be used to evaluate a Notification Rule in the Rules engine.
 *
 * The {@link RuleType} of a RuleConfig determines what content must be stored in the value field of a RuleConfig.
 * There is no enforcement of those rules on the persistence layer.
 *
 */
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE) // for Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
@Setter
@Builder
public class RuleParameters {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RuleType type;

    private String value;
}
