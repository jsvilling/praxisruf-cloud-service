package ch.fhnw.ip5.praxiscloudservice.util;

import ch.fhnw.ip5.praxiscloudservice.domain.*;

import java.util.Set;
import java.util.UUID;

public class DefaultTestData {

    public static final String TOKEN = "token";
    public static final UUID USER_ID = UUID.randomUUID();
    public static final UUID CLIENT_ID = UUID.randomUUID();
    public static final UUID RECIPIENT_CLIENT_ID = UUID.randomUUID();
    public static final String CLIENT_NAME = "name";
    public static final String MESSAGE_ID = UUID.randomUUID().toString();
    public static final UUID CLIENT_CONFIGURATION_ID = UUID.randomUUID();

    public static Registration createRegistration() {
        return new Registration(CLIENT_ID, TOKEN);
    }

    public static NotificationType createNotificationType() {
        return new NotificationType(UUID.randomUUID(), "", "", "", "");
    }

    public static PraxisNotification createNotification() {
        return new PraxisNotification(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static ClientConfiguration createClientConfiguration() {
        return new ClientConfiguration(
                UUID.randomUUID(),
                "ClientConfiguration",
                createClient(),
                Set.of(createRuleParameter()),
                Set.of(createNotificationType())
            );
    }

    public static Client createClient() {
        return new Client(
                CLIENT_ID,
                CLIENT_NAME,
                USER_ID,
                null
        );
    }

    public static RuleParameters createRuleParameter() {
        return new RuleParameters(UUID.randomUUID(), RuleType.SENDER, RECIPIENT_CLIENT_ID.toString());
    }
}