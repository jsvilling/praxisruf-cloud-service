package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a Notification that is being sent and received.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
@Builder
public class PraxisNotification {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID notificationTypeId;

    private UUID sender;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
