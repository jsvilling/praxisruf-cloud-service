package praxiscloudservice.service.rulesengine.evaluators;

import ch.fhnw.ip5.praxiscloudservice.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip5.praxiscloudservice.configuration.domain.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.configuration.domain.RuleType;
import ch.fhnw.ip5.praxiscloudservice.configuration.service.rulesengine.evaluators.SenderRuleEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static praxiscloudservice.util.DefaultTestData.*;

public class SenderRuleEvaluatorTest {

    private SenderRuleEvaluator senderRuleEvaluator;

    @BeforeEach
    public void beforeEach() {
        senderRuleEvaluator = new SenderRuleEvaluator();
    }

    @Test
    void getType() {
        // Given
        // When
        final RuleType actualType = senderRuleEvaluator.getRelevantType();

        // Then
        assertThat(actualType).isEqualTo(RuleType.SENDER);
    }

    @Test
    void isRelevant_nullNotification() {
        // Given
        final RuleParameters ruleParameters = createSenderRuleParameters();

        // When
        final boolean isRelevant = senderRuleEvaluator.isRelevant(null, ruleParameters);

        // Then
        assertThat(isRelevant).isFalse();
    }

    @Test
    void isRelevant_nullRuleParameters() {
        // Given
        final SendPraxisNotificationDto notification = createSendNotificationDto();

        // When
        final boolean isRelevant = senderRuleEvaluator.isRelevant(notification, null);

        // Then
        assertThat(isRelevant).isFalse();
    }

    @Test
    void isRelevant_invalidRuleType() {
        // Given
        final RuleParameters ruleParameters = createNotificationTypeRuleParameters();
        final SendPraxisNotificationDto notification = createSendNotificationDto();

        // When
        final boolean isRelevant = senderRuleEvaluator.isRelevant(notification, ruleParameters);

        // Then
        assertThat(isRelevant).isFalse();
    }


    @Test
    void isRelevant_matchingSender() {
        // Given
        final SendPraxisNotificationDto notification = createSendNotificationDto();
        final RuleParameters ruleParameters = RuleParameters.builder()
                .ruleParametersId(randomUUID())
                .type(RuleType.SENDER)
                .value(notification.getSender().toString())
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
        final SendPraxisNotificationDto notification = createSendNotificationDto();

        // When
        final boolean isRelevant = senderRuleEvaluator.isRelevant(notification, ruleParameters);

        // Then
        assertThat(isRelevant).isFalse();
    }

}
