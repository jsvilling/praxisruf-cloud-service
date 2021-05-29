package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * Represents a Notification that is being sent and received.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
public class Notification {

    @Id
    private UUID id;

    private UUID notificationTypeId;

    private UUID sender;
}
