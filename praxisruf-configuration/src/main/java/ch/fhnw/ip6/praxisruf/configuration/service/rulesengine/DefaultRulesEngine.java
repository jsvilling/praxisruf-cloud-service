package ch.fhnw.ip6.praxisruf.configuration.service.rulesengine;

import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.configuration.api.rulesengine.RuleEvaluator;
import ch.fhnw.ip6.praxisruf.configuration.api.rulesengine.RulesEngine;
import ch.fhnw.ip6.praxisruf.configuration.domain.RuleParameters;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class DefaultRulesEngine implements RulesEngine {

    private final RuleEvaluatorFactory ruleEvaluatorFactory;

    @Override
    public boolean isAnyRelevant(Collection<RuleParameters> ruleParameters, SendPraxisNotificationDto notification) {
        return ruleParameters.stream().anyMatch(r -> isRelevant(notification, r));
    }

    private boolean isRelevant(SendPraxisNotificationDto notification, RuleParameters ruleParameters) {
        final RuleEvaluator ruleEvaluator = ruleEvaluatorFactory.get(ruleParameters.getType());
        return ruleEvaluator != null && ruleEvaluator.isRelevant(notification, ruleParameters);
    }
}
