package ch.fhnw.ip5.praxiscloudservice.configuration.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * The Registration maps a Client to its Firebase Messaging token.
 */
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE) // for Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Builder
@Getter
public class Registration {

    @Id
    private UUID clientId;

    private String fcmToken;
}
