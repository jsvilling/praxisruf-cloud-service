package ch.fhnw.ip5.praxiscloudservice.service.rulesengine.evaluators;

import ch.fhnw.ip5.praxiscloudservice.domain.configuration.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.RuleType;
import ch.fhnw.ip5.praxiscloudservice.domain.notification.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.service.configuration.rulesengine.evaluators.NotificationTypeRuleEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData.*;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

public class NotificationTypeRuleEvaluatorTest {

    private NotificationTypeRuleEvaluator senderRuleEvaluator;

    @BeforeEach
    public void beforeEach() {
        senderRuleEvaluator = new NotificationTypeRuleEvaluator();
    }

    @Test
    void getType() {
        // Given
        // When
        final RuleType actualType = senderRuleEvaluator.getRelevantType();

        // Then
        assertThat(actualType).isEqualTo(RuleType.NOTIFICATION_TYPE);
    }

    @Test
    void isRelevant_nullNotification() {
        // Given
        final RuleParameters ruleParameters = createNotificationTypeRuleParameters();

        // When
        final boolean isRelevant = senderRuleEvaluator.isRelevant(null, ruleParameters);

        // Then
        assertThat(isRelevant).isFalse();
    }

    @Test
    void isRelevant_nullRuleParameters() {
        // Given
        final PraxisNotification notification = createNotification();

        // When
        final boolean isRelevant = senderRuleEvaluator.isRelevant(notification, null);

        // Then
        assertThat(isRelevant).isFalse();
    }

    @Test
    void isRelevant_invalidRuleType() {
        // Given
        final RuleParameters ruleParameters = createSenderRuleParameters();
        final PraxisNotification notification = createNotification();

        // When
        final boolean isRelevant = senderRuleEvaluator.isRelevant(notification, ruleParameters);

        // Then
        assertThat(isRelevant).isFalse();
    }

    @Test
    void isRelevant_matchingSender() {
        // Given
        final PraxisNotification notification = createNotification();
        final RuleParameters ruleParameters = RuleParameters.builder()
                .ruleParametersId(randomUUID())
                .type(RuleType.NOTIFICATION_TYPE)
                .value(notification.getNotificationTypeId().toString())
                .build();

        // When
        final boolean isRelevant = senderRuleEvaluator.isRelevant(notification, ruleParameters);

        // Then
        assertThat(isRelevant).isTrue();
    }

    @Test
    void isRelevant_nonMatchingSender() {
        // Given
        final RuleParameters ruleParameters = createSenderRuleParameters();
        final PraxisNotification notification = createNotification();

        // When
        final boolean isRelevant = senderRuleEvaluator.isRelevant(notification, ruleParameters);

        // Then
        assertThat(isRelevant).isFalse();
    }


}
