package ch.fhnw.ip6.praxisruf.notification.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a Notification that is being sent and received.
 */
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE) // for Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
@Builder
public class PraxisNotification {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID notificationTypeId;

    private UUID sender;

    @Enumerated(value = EnumType.STRING)
    private NotificationContext context = NotificationContext.USER;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
