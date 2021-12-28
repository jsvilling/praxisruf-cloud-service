package ch.fhnw.ip6.praxisruf.notification.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum NotificationContext {

    USER,
    SYSTEM
}
