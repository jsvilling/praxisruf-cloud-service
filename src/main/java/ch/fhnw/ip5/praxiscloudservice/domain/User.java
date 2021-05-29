package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Represents a user.
 *
 * Note: This is only a thin placeholder.
 *
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
public class User {

    @Id
    private UUID id;

    private String name;

}
