package ch.fhnw.ip5.praxiscloudservice.util;

import ch.fhnw.ip5.praxiscloudservice.api.dto.SendPraxisNotificationDto;
import ch.fhnw.ip5.praxiscloudservice.domain.*;

import java.util.Set;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class DefaultTestData {

    public static final String TOKEN = "token";
    public static final UUID USER_ID = randomUUID();
    public static final UUID CLIENT_ID = randomUUID();
    public static final UUID RECIPIENT_CLIENT_ID = randomUUID();
    public static final String CLIENT_NAME = "name";
    public static final String MESSAGE_ID = randomUUID().toString();

    public static Registration createRegistration() {
        return Registration.builder()
                .clientId(CLIENT_ID)
                .fcmToken(TOKEN)
                .build();
    }

    public static NotificationType createNotificationType() {
        return NotificationType.builder()
                .id(randomUUID())
                .type("")
                .body("")
                .title("")
                .displayText("")
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

    public static ClientConfiguration createClientConfiguration() {
        return ClientConfiguration.builder()
                .clientConfigurationId(randomUUID())
                .name("ClientConfiguration")
                .client(createClient())
                .rules(Set.of(createRuleParameter()))
                .notificationTypes(Set.of(createNotificationType()))
                .build();
    }

    public static Client createClient() {
        return Client.builder()
                .clientId(CLIENT_ID)
                .name(CLIENT_NAME)
                .userId(USER_ID)
                .clientConfiguration(null)
                .build();
    }

    public static RuleParameters createRuleParameter() {
        return RuleParameters.builder()
                .ruleParametersId(randomUUID())
                .type(RuleType.SENDER)
                .value(RECIPIENT_CLIENT_ID.toString())
                .build();
    }

}
