package ch.fhnw.ip5.praxiscloudservice.service.rulesengine;

import ch.fhnw.ip5.praxiscloudservice.api.RuleEvaluator;
import ch.fhnw.ip5.praxiscloudservice.api.RulesEngine;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;
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
