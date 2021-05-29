package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

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
public class Client {

    @Id
    private UUID id;

    private String name;

    private UUID userId;

}