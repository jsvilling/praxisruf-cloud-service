package ch.fhnw.ip5.praxiscloudservice.service.rulesengine;

import ch.fhnw.ip5.praxiscloudservice.api.configuration.rulesengine.RuleEvaluator;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.RuleType;
import ch.fhnw.ip5.praxiscloudservice.service.configuration.rulesengine.RuleEvaluatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.when;

public class RuleEvaluatorFactoryTest {

    private RuleEvaluatorFactory ruleEvaluatorFactory;
    private RuleEvaluator registeredRuleEvaluator;
    private RuleType registeredRuleType = RuleType.SENDER;

    @BeforeEach
    public void beforeAll() {
        registeredRuleEvaluator = Mockito.mock(RuleEvaluator.class);
        when(registeredRuleEvaluator.getRelevantType()).thenReturn(registeredRuleType);
        ruleEvaluatorFactory = new RuleEvaluatorFactory(List.of(registeredRuleEvaluator));
    }

    @Test
    void testGetEvaluator_registeredType() {
        // Given
        final RuleType requestedRuleType = registeredRuleType;

        // When
        final RuleEvaluator foundRuleEvaluator = ruleEvaluatorFactory.get(requestedRuleType);

        // Then
        Assertions.assertThat(foundRuleEvaluator).isSameAs(this.registeredRuleEvaluator);
    }

    @Test
    void testGetEvaluator_notRegisteredType() {
        // Given
        final RuleType requestedRuleType = RuleType.NOTIFICATION_TYPE;

        // When
        final RuleEvaluator foundRuleEvaluator = ruleEvaluatorFactory.get(requestedRuleType);

        // Then
        Assertions.assertThat(foundRuleEvaluator).isNull();
    }

}
