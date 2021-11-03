package praxiscloudservice.util;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.NotificationTypeDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.configuration.domain.*;

import java.util.Set;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class DefaultTestData {

    public static final String TOKEN = "token";
    public static final UUID USER_ID = randomUUID();
    public static final UUID CLIENT_ID = randomUUID();
    public static final UUID RECIPIENT_CLIENT_ID = randomUUID();
    public static final String CLIENT_NAME = "name";

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

    public static ClientConfiguration createClientConfiguration() {
        return ClientConfiguration.builder()
                .clientConfigurationId(randomUUID())
                .name("ClientConfiguration")
                .client(createClient())
                .rules(Set.of(createSenderRuleParameters()))
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

    public static RuleParameters createSenderRuleParameters() {
        return RuleParameters.builder()
                .ruleParametersId(randomUUID())
                .type(RuleType.SENDER)
                .value(RECIPIENT_CLIENT_ID.toString())
                .build();
    }

    public static RuleParameters createNotificationTypeRuleParameters() {
        return RuleParameters.builder()
                .ruleParametersId(randomUUID())
                .type(RuleType.NOTIFICATION_TYPE)
                .value("TYPE")
                .build();
    }

    public static SendPraxisNotificationDto createSendNotificationDto() {
        return SendPraxisNotificationDto.builder()
                .notificationTypeId(randomUUID())
                .sender(CLIENT_ID)
                .build();
    }

    public static NotificationTypeDto createNotificationTypeDto() {
        return NotificationTypeDto.builder()
                .id(randomUUID())
                .description("")
                .body("")
                .title("")
                .displayText("")
                .build();
    }

}