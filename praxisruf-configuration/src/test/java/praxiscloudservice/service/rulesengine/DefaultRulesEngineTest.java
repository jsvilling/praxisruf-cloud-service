package praxiscloudservice.service.rulesengine;

import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.configuration.api.rulesengine.RuleEvaluator;
import ch.fhnw.ip6.praxisruf.configuration.domain.RuleParameters;
import ch.fhnw.ip6.praxisruf.configuration.domain.RuleType;
import ch.fhnw.ip6.praxisruf.configuration.service.rulesengine.DefaultRulesEngine;
import ch.fhnw.ip6.praxisruf.configuration.service.rulesengine.RuleEvaluatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static praxiscloudservice.util.DefaultTestData.createSendNotificationDto;
import static praxiscloudservice.util.DefaultTestData.createSenderRuleParameters;

@ExtendWith(MockitoExtension.class)
public class DefaultRulesEngineTest {

    @Mock
    private RuleEvaluatorFactory ruleEvaluatorFactory;

    @InjectMocks
    private DefaultRulesEngine rulesEngine;

    @Test
    void isRelevant_noRuleParams() {
        // Given
        final SendPraxisNotificationDto notification = createSendNotificationDto();

        // When
        final boolean isAnyRelevant = rulesEngine.isAnyRelevant(Collections.emptyList(), notification);

        // Then
        assertThat(isAnyRelevant).isFalse();
    }

    @Test
    void isRelevant_nullNotification() {
        // Given
        // When
        final boolean isAnyRelevant = rulesEngine.isAnyRelevant(Collections.emptyList(), null);

        // Then
        assertThat(isAnyRelevant).isFalse();
    }

    @Test
    void isRelevant_noEvaluatorFound() {
        // Given
        final RuleParameters ruleParameters = createSenderRuleParameters();
        when(ruleEvaluatorFactory.get(ruleParameters.getType())).thenReturn(null);

        final SendPraxisNotificationDto notification = createSendNotificationDto();

        // When
        final boolean isAnyRelevant = rulesEngine.isAnyRelevant(List.of(ruleParameters), notification);

        // Then
        assertThat(isAnyRelevant).isFalse();
    }

    @Test
    void isRelevant_oneMatchingEvaluator() {
        // Given
        final SendPraxisNotificationDto notification = createSendNotificationDto();

        final RuleParameters ruleParameters1 = createSenderRuleParameters();
        RuleEvaluator evaluator1 = Mockito.mock(RuleEvaluator.class);
        when(evaluator1.isRelevant(notification, ruleParameters1)).thenReturn(false);

        final RuleParameters ruleParameters2 = createSenderRuleParameters();
        RuleEvaluator evaluator2 = Mockito.mock(RuleEvaluator.class);
        when(evaluator1.isRelevant(notification, ruleParameters1)).thenReturn(true);

        when(ruleEvaluatorFactory.get(RuleType.SENDER)).thenReturn(evaluator1, evaluator2);

        // When
        final boolean isAnyRelevant = rulesEngine.isAnyRelevant(List.of(ruleParameters1, ruleParameters2), notification);

        // Then
        assertThat(isAnyRelevant).isTrue();
    }

    @Test
    void isRelevant_allMatchingEvaluator() {
        // Given
        final SendPraxisNotificationDto notification = createSendNotificationDto();

        final RuleParameters ruleParameters1 = createSenderRuleParameters();
        RuleEvaluator evaluator1 = Mockito.mock(RuleEvaluator.class);
        when(evaluator1.isRelevant(notification, ruleParameters1)).thenReturn(true);

        final RuleParameters ruleParameters2 = createSenderRuleParameters();
        RuleEvaluator evaluator2 = Mockito.mock(RuleEvaluator.class);
        when(evaluator1.isRelevant(notification, ruleParameters1)).thenReturn(true);

        when(ruleEvaluatorFactory.get(RuleType.SENDER)).thenReturn(evaluator1, evaluator2);

        // When
        final boolean isAnyRelevant = rulesEngine.isAnyRelevant(List.of(ruleParameters1, ruleParameters2), notification);

        // Then
        assertThat(isAnyRelevant).isTrue();
    }

    @Test
    void isRelevant_noneMatchingEvaluator() {
        // Given
        final SendPraxisNotificationDto notification = createSendNotificationDto();

        final RuleParameters ruleParameters1 = createSenderRuleParameters();
        RuleEvaluator evaluator1 = Mockito.mock(RuleEvaluator.class);
        when(evaluator1.isRelevant(notification, ruleParameters1)).thenReturn(false);

        final RuleParameters ruleParameters2 = createSenderRuleParameters();
        RuleEvaluator evaluator2 = Mockito.mock(RuleEvaluator.class);
        when(evaluator1.isRelevant(notification, ruleParameters1)).thenReturn(false);

        when(ruleEvaluatorFactory.get(RuleType.SENDER)).thenReturn(evaluator1, evaluator2);

        // When
        final boolean isAnyRelevant = rulesEngine.isAnyRelevant(List.of(ruleParameters1, ruleParameters2), notification);

        // Then
        assertThat(isAnyRelevant).isFalse();
    }

}
