package ch.fhnw.ip6.praxisruf.configuration.domain;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;


/**
 * A Client represents a single client device.
 *
 * Each device is uniquely assigned to a user and can have a name assigned by its user. The technical identification
 * however must always be resolved over the UUID id of the Client.
 *
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@AllArgsConstructor (access = AccessLevel.PRIVATE) // for Builder
@Builder
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private UUID userId;

    @OneToOne(mappedBy = "client", orphanRemoval = true)
    @Cascade({CascadeType.ALL})
    private ClientConfiguration clientConfiguration;

}
