package ch.fhnw.ip6.praxisruf.notification.util;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.NotificationTypeDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.RegistrationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.notification.domain.NotificationSendProcess;
import ch.fhnw.ip6.praxisruf.notification.domain.PraxisNotification;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public class DefaultTestData {

    public static final String TOKEN = "token";
    public static final UUID USER_ID = randomUUID();
    public static final UUID CLIENT_ID = randomUUID();
    public static final String CLIENT_NAME = "name";
    public static final String MESSAGE_ID = randomUUID().toString();
    public static final Long VERSION = 0L;
    public static final Boolean T2S_ENABLED = true;

    public static NotificationTypeDto createNotificationTypeDto() {
        return NotificationTypeDto.builder()
                .id(randomUUID())
                .description("")
                .body("")
                .title("")
                .displayText("")
                .version(VERSION)
                .textToSpeech(T2S_ENABLED)
                .build();
    }

    public static PraxisNotification createNotification() {
        return PraxisNotification.builder()
                .id(randomUUID())
                .notificationTypeId(randomUUID())
                .sender(randomUUID())
                .build();
    }

    public static SendPraxisNotificationDto createSendNotificationDto() {
        return SendPraxisNotificationDto.builder()
                .notificationTypeId(randomUUID())
                .sender(CLIENT_ID)
                .build();
    }

    public static ClientDto createClientDto() {
        return ClientDto.builder()
                .id(CLIENT_ID)
                .name(CLIENT_NAME)
                .userId(USER_ID)
                .build();
    }

    public static RegistrationDto createRegistrationDto() {
        return RegistrationDto.builder()
                .fcmToken(TOKEN)
                .clientName(CLIENT_NAME)
                .build();
    }

    public static NotificationSendProcess createNotificationSendProcess() {
        return NotificationSendProcess.builder()
                .id(CLIENT_ID)
                .clientName(CLIENT_NAME)
                .notificationId(UUID.randomUUID())
                .relevantToken(TOKEN)
                .success(false)
                .build();
    }

}
