package ch.fhnw.ip5.praxiscloudservice.service.configuration.rulesengine;

import ch.fhnw.ip5.praxiscloudservice.api.configuration.rulesengine.RuleEvaluator;
import ch.fhnw.ip5.praxiscloudservice.api.configuration.rulesengine.RulesEngine;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.domain.notification.PraxisNotification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class DefaultRulesEngine implements RulesEngine {

    private final RuleEvaluatorFactory ruleEvaluatorFactory;

    @Override
    public boolean isAnyRelevant(Collection<RuleParameters> ruleParameters, PraxisNotification notification) {
        return ruleParameters.stream().anyMatch(r -> isRelevant(notification, r));
    }

    private boolean isRelevant(PraxisNotification notification, RuleParameters ruleParameters) {
        final RuleEvaluator ruleEvaluator = ruleEvaluatorFactory.get(ruleParameters.getType());
        return ruleEvaluator != null && ruleEvaluator.isRelevant(notification, ruleParameters);
    }
}
