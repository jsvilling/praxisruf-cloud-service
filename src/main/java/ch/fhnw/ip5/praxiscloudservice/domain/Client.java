package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

import static org.hibernate.annotations.CascadeType.ALL;

/**
 * A Client represents a single client device.
 *
 * Each device is uniquely assigned to a user and can have a name assigned by its user. The technical identification
 * however must always be resolved over the UUID id of the Client.
 *
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
@Setter
public class Client {

    @Id
    private UUID clientId;

    private String name;

    private UUID userId;

    @OneToOne(mappedBy = "client")
    @Cascade(ALL)
    private ClientConfiguration clientConfiguration;

}
