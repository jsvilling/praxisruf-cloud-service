package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Represents a user.
 *
 * Note: This is only a thin placeholder.
 *
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
@Builder
public class PraxisIntercomUser {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

}
